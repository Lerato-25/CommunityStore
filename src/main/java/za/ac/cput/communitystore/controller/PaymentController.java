package za.ac.cput.communitystore.controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;
import za.ac.cput.communitystore.service.IPaymentService;
import static za.ac.cput.communitystore.controller.Backend3Views.*;
@RestController @RequestMapping("/api/payments")
public class PaymentController {
    private final IPaymentService service;
    public PaymentController(IPaymentService service) { this.service=service; }
    public record CreatePayment(@Positive int orderId,@NotNull @DecimalMin("0.00") BigDecimal amount,@NotBlank @Size(max=50) String paymentMethod) { }
    public record DemoResult(@NotNull Boolean successful) { }
    @PostMapping({"","/create"}) @ResponseStatus(HttpStatus.CREATED)
    public PaymentView create(@Valid @RequestBody CreatePayment r) { return PaymentView.of(service.create(r.orderId(),r.amount(),r.paymentMethod())); }
    @GetMapping({"/{id}","/read/{id}"}) public PaymentView read(@PathVariable int id) { return PaymentView.of(service.read(id)); }
    @GetMapping({"","/getAll"}) public List<PaymentView> all() { return service.getAll().stream().map(PaymentView::of).toList(); }
    @PostMapping("/{id}/demo-result") public PaymentView complete(@PathVariable int id,@Valid @RequestBody DemoResult r) { return PaymentView.of(service.completeDemo(id,r.successful())); }
}
