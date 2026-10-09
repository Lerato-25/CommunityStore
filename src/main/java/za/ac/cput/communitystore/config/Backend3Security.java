package za.ac.cput.communitystore.config;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import za.ac.cput.communitystore.repository.UserRepository;

@Configuration
public class Backend3Security {
    @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
    @Bean UserDetailsService backendUsers(UserRepository users) {
        return email -> {
            var user = users.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Unknown account"));
            return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                .password(user.getPasswordHash()).roles(user.getRole().getRoleName())
                .disabled(!"Active".equalsIgnoreCase(user.getStatus())).build();
        };
    }
    @Bean @Order(1) SecurityFilterChain backend3(HttpSecurity http) throws Exception {
        return http.securityMatcher("/api/payments/**","/api/community-posts/**","/api/notifications/**","/api/reports/**","/api/backend3/**")
            .authorizeHttpRequests(a -> a.anyRequest().authenticated()).httpBasic(Customizer.withDefaults())
            .build();
    }
    // Preserve existing routes: authentication and CSRF apply only to Backend 3 above.
    @Bean @Order(2) SecurityFilterChain existingRoutes(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(a -> a.anyRequest().permitAll())
            .csrf(csrf -> csrf.disable()).build();
    }
}
