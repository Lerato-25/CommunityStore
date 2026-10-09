package za.ac.cput.communitystore;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;
import za.ac.cput.communitystore.service.PaymentService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class Backend3ProfileGuardTest {
    @Autowired PaymentService payments;

    @Test void demoSettlementIsUnavailableWithoutDemoProfile() {
        var failure = assertThrows(ResponseStatusException.class, () -> payments.completeDemo(1, true));
        assertEquals(404, failure.getStatusCode().value());
    }
}
