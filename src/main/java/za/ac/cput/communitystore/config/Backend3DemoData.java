package za.ac.cput.communitystore.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import za.ac.cput.communitystore.domain.*;
import za.ac.cput.communitystore.factory.*;
import za.ac.cput.communitystore.repository.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Configuration @Profile("demo")
public class Backend3DemoData {
    @Bean CommandLineRunner demoData(UserRepository users,RoleRepository roles,OrderRepository orders,
            NotificationsRepository notifications,PasswordEncoder encoder,PlatformTransactionManager manager) {
        return args -> new TransactionTemplate(manager).executeWithoutResult(tx -> {
            var student=roles.save(new Role.Builder().setRoleName("Student").setDescription("Demo buyer").build());
            var admin=roles.save(new Role.Builder().setRoleName("Admin").setDescription("Demo moderator").build());
            var buyer=users.save(account(student,"Reece","buyer@demo.local",encoder));
            users.save(account(student,"Other","other@demo.local",encoder));
            users.save(account(admin,"Moderator","admin@demo.local",encoder));
            orders.save(OrderFactory.createOrder(buyer,"Campus collection",new BigDecimal("150.00")));
            orders.save(OrderFactory.createOrder(buyer,"Campus collection",new BigDecimal("75.00")));
            notifications.save(NotificationsFactory.createNotifications(buyer,"Welcome! This demo uses disposable data and no real payments."));
        });
    }
    private User account(Role role,String name,String email,PasswordEncoder encoder) {
        return new User.Builder().setRole(role).setFirstName(name).setLastName("Demo").setEmail(email)
            .setPasswordHash(encoder.encode("DemoPass123!")).setStatus("Active").setDateCreated(LocalDateTime.now()).build();
    }
}
