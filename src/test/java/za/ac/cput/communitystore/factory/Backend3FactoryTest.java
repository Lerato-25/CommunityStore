package za.ac.cput.communitystore.factory;
import org.junit.jupiter.api.Test;
import za.ac.cput.communitystore.domain.*;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
class Backend3FactoryTest {
    User user=new User.Builder().setUserID(1).build();
    Order order=new Order.Builder().setOrderID(1).build();
    @Test void rejectsInvalidMoneyAndKeepsPendingDefaults() {
        assertThrows(IllegalArgumentException.class,()->PaymentFactory.createPayment(order,new BigDecimal("1.001"),"Demo"));
        assertThrows(IllegalArgumentException.class,()->PaymentFactory.createPayment(order,new BigDecimal("-1"),"Demo"));
        assertEquals("Pending",PaymentFactory.createPayment(order,new BigDecimal("1.00"),"Demo").getPaymentStatus());
    }
    @Test void checksTextLimitsAndPreservesPostIdentity() {
        assertThrows(IllegalArgumentException.class,()->CommunityPostFactory.createCommunityPost(user,"x".repeat(151),"Content"));
        var original=new CommunityPost.Builder().copy(CommunityPostFactory.createCommunityPost(user,"Title","Content")).setPostID(2).build();
        var changed=CommunityPostFactory.withDetails(original,"Updated","New");
        assertEquals(2,changed.getPostID());assertEquals(original.getPostDate(),changed.getPostDate());assertEquals("Title",original.getTitle());
    }
    @Test void validatesTargetsAndNotificationDefaults() {
        assertThrows(IllegalArgumentException.class,()->ReportFactory.createReport(user,"Invalid",1,"Reason",null));
        assertFalse(NotificationsFactory.createNotifications(user,"Hello").isRead());
    }
}
