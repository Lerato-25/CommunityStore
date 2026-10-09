package za.ac.cput.communitystore.controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.web.csrf.CsrfToken;
import za.ac.cput.communitystore.service.Backend3Access;
import za.ac.cput.communitystore.repository.OrderRepository;
import java.util.Map;
@RestController
@RequestMapping("/api/backend3")
public class Backend3SessionController {
    private final Backend3Access access;
    private final OrderRepository orders;
    public Backend3SessionController(Backend3Access access, OrderRepository orders) { this.access=access; this.orders=orders; }
    @GetMapping("/session") public Map<String,Object> session(CsrfToken csrf) {
        var user=access.current();
        return Map.of("userId",user.getUserID(),"name",user.getFirstName(),"role",user.getRole().getRoleName(),
            "csrfToken",csrf.getToken(),"csrfHeader",csrf.getHeaderName());
    }
    @GetMapping("/orders") public java.util.List<Map<String,Object>> orders() {
        return orders.findByBuyer_UserID(access.current().getUserID()).stream()
            .map(o -> Map.<String,Object>of("orderId",o.getOrderID(),"amount",o.getTotalAmount(),"status",o.getOrderStatus())).toList();
    }
}
