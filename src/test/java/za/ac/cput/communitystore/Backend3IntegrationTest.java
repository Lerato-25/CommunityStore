package za.ac.cput.communitystore;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import za.ac.cput.communitystore.domain.*;
import za.ac.cput.communitystore.factory.*;
import za.ac.cput.communitystore.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles({"test","demo"})
class Backend3IntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired UserRepository users;
    @Autowired OrderRepository orders;
    @Autowired PaymentRepository payments;
    @Autowired CommunityPostRepository posts;
    @Autowired NotificationsRepository notifications;
    @Autowired ReportRepository reports;
    @Autowired PasswordEncoder encoder;
    @Autowired za.ac.cput.communitystore.service.PaymentService paymentService;
    User buyer,other,admin;
    za.ac.cput.communitystore.domain.Order order;
    @BeforeEach void setup() {
        reports.deleteAll();notifications.deleteAll();payments.deleteAll();posts.deleteAll();orders.deleteAll();
        buyer=users.findByEmail("buyer@demo.local").orElseThrow();other=users.findByEmail("other@demo.local").orElseThrow();admin=users.findByEmail("admin@demo.local").orElseThrow();
        order=orders.save(OrderFactory.createOrder(buyer,"Campus",new BigDecimal("150.00")));
    }
    String basic(String email) { return "Basic "+java.util.Base64.getEncoder().encodeToString((email+":DemoPass123!").getBytes(java.nio.charset.StandardCharsets.UTF_8)); }
    @Test void authenticationAndCsrfAreRequired() throws Exception {
        mvc.perform(get("/api/community-posts")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/backend3/session").header("Authorization",basic("buyer@demo.local"))).andExpect(status().isOk()).andExpect(jsonPath("$.csrfToken").isNotEmpty());
        mvc.perform(post("/api/community-posts").header("Authorization",basic("buyer@demo.local")).contentType("application/json").content("{\"title\":\"A\",\"content\":\"B\"}")).andExpect(status().isForbidden());
    }
    @Test void postLifecycleAndOwnership() throws Exception {
        mvc.perform(post("/api/community-posts").with(httpBasic("buyer@demo.local","DemoPass123!")).with(csrf()).contentType("application/json").content("{\"title\":\"Books\",\"content\":\"Textbooks available\",\"userId\":999}"))
            .andExpect(status().isCreated()).andExpect(jsonPath("$.userId").value(buyer.getUserID())).andExpect(jsonPath("$.passwordHash").doesNotExist());
        int id=posts.findAll().get(0).getPostID();
        mvc.perform(put("/api/community-posts/"+id).with(httpBasic("other@demo.local","DemoPass123!")).with(csrf()).contentType("application/json").content("{\"title\":\"Changed\",\"content\":\"No\"}")).andExpect(status().isForbidden());
        mvc.perform(put("/api/community-posts/"+id).with(httpBasic("buyer@demo.local","DemoPass123!")).with(csrf()).contentType("application/json").content("{\"title\":\"Updated\",\"content\":\"Yes\"}")).andExpect(status().isOk());
        mvc.perform(delete("/api/community-posts/"+id).with(httpBasic("buyer@demo.local","DemoPass123!")).with(csrf())).andExpect(status().isNoContent());
        assertFalse(posts.existsById(id));
        mvc.perform(get("/api/community-posts/"+id).with(httpBasic("buyer@demo.local","DemoPass123!"))).andExpect(status().isNotFound());
    }
    @Test void invalidInputAndMissingRelations() throws Exception {
        mvc.perform(post("/api/community-posts").with(httpBasic("buyer@demo.local","DemoPass123!")).with(csrf()).contentType("application/json").content("{\"title\":\" \",\"content\":\"B\"}")).andExpect(status().isBadRequest());
        mvc.perform(post("/api/reports").with(httpBasic("buyer@demo.local","DemoPass123!")).with(csrf()).contentType("application/json").content("{\"targetType\":\"CommunityPost\",\"targetId\":99999,\"reason\":\"Spam\"}" )).andExpect(status().isNotFound());
    }
    @Test void paymentAmountDuplicateAndResultAreControlled() throws Exception {
        String request="{\"orderId\":"+order.getOrderID()+",\"amount\":150.00,\"paymentMethod\":\"Demo\"}";
        mvc.perform(post("/api/payments").with(httpBasic("other@demo.local","DemoPass123!")).with(csrf()).contentType("application/json").content(request)).andExpect(status().isForbidden());
        mvc.perform(post("/api/payments").with(httpBasic("buyer@demo.local","DemoPass123!")).with(csrf()).contentType("application/json").content(request.replace("150.00","1.00"))).andExpect(status().isBadRequest());
        mvc.perform(post("/api/payments").with(httpBasic("buyer@demo.local","DemoPass123!")).with(csrf()).contentType("application/json").content(request)).andExpect(status().isCreated());
        mvc.perform(post("/api/payments").with(httpBasic("buyer@demo.local","DemoPass123!")).with(csrf()).contentType("application/json").content(request)).andExpect(status().isConflict());
        int id=payments.findAll().get(0).getPaymentID();
        mvc.perform(post("/api/payments/"+id+"/demo-result").with(httpBasic("buyer@demo.local","DemoPass123!")).with(csrf()).contentType("application/json").content("{\"successful\":true}")).andExpect(status().isOk()).andExpect(jsonPath("$.paymentStatus").value("Successful"));
        assertEquals("Paid",orders.findById(order.getOrderID()).orElseThrow().getOrderStatus());
        assertEquals(1,notifications.count());
        mvc.perform(post("/api/payments/"+id+"/demo-result").with(httpBasic("buyer@demo.local","DemoPass123!")).with(csrf()).contentType("application/json").content("{\"successful\":true}")).andExpect(status().isOk());
        assertEquals(1,notifications.count());
        mvc.perform(post("/api/payments/"+id+"/demo-result").with(httpBasic("buyer@demo.local","DemoPass123!")).with(csrf()).contentType("application/json").content("{\"successful\":false}")).andExpect(status().isConflict());
    }
    @Test void failureDoesNotMarkOrderPaid() throws Exception {
        var payment=payments.save(PaymentFactory.createPayment(order,new BigDecimal("150"),"Demo"));
        mvc.perform(post("/api/payments/"+payment.getPaymentID()+"/demo-result").with(httpBasic("buyer@demo.local","DemoPass123!")).with(csrf()).contentType("application/json").content("{\"successful\":false}")).andExpect(status().isOk()).andExpect(jsonPath("$.paymentStatus").value("Failed"));
        assertEquals("Pending",orders.findById(order.getOrderID()).orElseThrow().getOrderStatus());
    }
    @Test void notificationsArePrivateAndReadStatePersists() throws Exception {
        var notification=notifications.save(NotificationsFactory.createNotifications(buyer,"Hello"));int id=notification.getNotificationID();
        mvc.perform(get("/api/notifications/"+id).with(httpBasic("other@demo.local","DemoPass123!"))).andExpect(status().isForbidden());
        mvc.perform(get("/api/notifications").with(httpBasic("other@demo.local","DemoPass123!"))).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(0));
        mvc.perform(patch("/api/notifications/"+id+"/read").with(httpBasic("buyer@demo.local","DemoPass123!")).with(csrf())).andExpect(status().isOk()).andExpect(jsonPath("$.isRead").value(true));
        assertTrue(notifications.findById(id).orElseThrow().isRead());
        mvc.perform(get("/api/notifications?unreadOnly=true").with(httpBasic("buyer@demo.local","DemoPass123!"))).andExpect(jsonPath("$.length()").value(0));
        mvc.perform(post("/api/notifications").with(httpBasic("buyer@demo.local","DemoPass123!")).with(csrf()).contentType("application/json").content("{\"userId\":"+other.getUserID()+",\"message\":\"Hi\"}")).andExpect(status().isForbidden());
    }
    @Test void reportsRequireModeratorAndTrackResolution() throws Exception {
        var post=posts.save(CommunityPostFactory.createCommunityPost(other,"Event","Details"));
        mvc.perform(post("/api/reports").with(httpBasic("buyer@demo.local","DemoPass123!")).with(csrf()).contentType("application/json").content("{\"targetType\":\"CommunityPost\",\"targetId\":"+post.getPostID()+",\"reason\":\"Spam\"}" )).andExpect(status().isCreated());
        int id=reports.findAll().get(0).getReportID();
        mvc.perform(get("/api/reports/"+id).with(httpBasic("other@demo.local","DemoPass123!"))).andExpect(status().isForbidden());
        mvc.perform(patch("/api/reports/"+id+"/review").with(httpBasic("buyer@demo.local","DemoPass123!")).with(csrf()).contentType("application/json").content("{\"status\":\"Dismissed\"}")).andExpect(status().isForbidden());
        mvc.perform(patch("/api/reports/"+id+"/review").with(httpBasic("admin@demo.local","DemoPass123!")).with(csrf()).contentType("application/json").content("{\"status\":\"Reviewed\"}")).andExpect(status().isOk());
        mvc.perform(patch("/api/reports/"+id+"/review").with(httpBasic("admin@demo.local","DemoPass123!")).with(csrf()).contentType("application/json").content("{\"status\":\"Dismissed\"}")).andExpect(status().isOk()).andExpect(jsonPath("$.resolvedDate").isNotEmpty());
        assertEquals(admin.getUserID(),reports.findById(id).orElseThrow().getReviewedBy().getUserID());
        mvc.perform(patch("/api/reports/"+id+"/review").with(httpBasic("admin@demo.local","DemoPass123!")).with(csrf()).contentType("application/json").content("{\"status\":\"Reviewed\"}")).andExpect(status().isConflict());
    }
    @Test void legacyAccountEndpointsCannotBypassFeaturePermissions() throws Exception {
        mvc.perform(get("/api/users/getAll")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/users/getAll").with(httpBasic("buyer@demo.local","DemoPass123!")))
            .andExpect(status().isForbidden());
        mvc.perform(get("/api/users/getAll").with(httpBasic("admin@demo.local","DemoPass123!")))
            .andExpect(status().isOk()).andExpect(jsonPath("$[0].passwordHash").doesNotExist());
    }

    @Test void concurrentPaymentCreationAndSettlementRemainSingle() throws Exception {
        var executor = java.util.concurrent.Executors.newFixedThreadPool(2);
        try {
            java.util.concurrent.Callable<Integer> create = () -> asBuyer(() -> {
                try {
                    paymentService.create(order.getOrderID(),new BigDecimal("150.00"),"Demo");
                    return 201;
                } catch (org.springframework.web.server.ResponseStatusException e) {
                    return e.getStatusCode().value();
                }
            });
            var results = executor.invokeAll(java.util.List.of(create,create),15,java.util.concurrent.TimeUnit.SECONDS);
            var codes = new java.util.ArrayList<Integer>();
            for (var result : results) codes.add(result.get());
            java.util.Collections.sort(codes);
            assertEquals(java.util.List.of(201,409),codes);
            int id = payments.findAll().get(0).getPaymentID();
            java.util.concurrent.Callable<String> settle = () -> asBuyer(() -> paymentService.completeDemo(id,true).getPaymentStatus());
            for (var result : executor.invokeAll(java.util.List.of(settle,settle),15,java.util.concurrent.TimeUnit.SECONDS))
                assertEquals("Successful",result.get());
            assertEquals(1,payments.count());
            assertEquals(1,notifications.count());
            assertEquals("Paid",orders.findById(order.getOrderID()).orElseThrow().getOrderStatus());
        } finally {
            executor.shutdownNow();
        }
    }

    private <T> T asBuyer(java.util.concurrent.Callable<T> operation) throws Exception {
        var context = org.springframework.security.core.context.SecurityContextHolder.createEmptyContext();
        context.setAuthentication(org.springframework.security.authentication.UsernamePasswordAuthenticationToken.authenticated(
            buyer.getEmail(),null,java.util.List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_Student"))));
        org.springframework.security.core.context.SecurityContextHolder.setContext(context);
        try { return operation.call(); }
        finally { org.springframework.security.core.context.SecurityContextHolder.clearContext(); }
    }
}
