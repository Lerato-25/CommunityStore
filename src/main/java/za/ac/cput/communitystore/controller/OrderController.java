package za.ac.cput.communitystore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import za.ac.cput.communitystore.domain.Order;
import za.ac.cput.communitystore.factory.OrderFactory;
import za.ac.cput.communitystore.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public Order create(@RequestBody Order order) {
        try {
            // factory validates the input, sets orderDate and starts the status at "Pending"
            Order valid = OrderFactory.createOrder(
                    order.getBuyer(), order.getShippingAddress(), order.getTotalAmount());
            return orderService.create(valid);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/read/{id}")
    public Order read(@PathVariable int id) {
        Order order = orderService.read(id);
        if (order == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found: " + id);
        }
        return order;
    }

    @PutMapping("/update")
    public Order update(@RequestBody Order order) {
        Order valid;
        try {
            // checks the status is one of the allowed values and keeps the id
            valid = OrderFactory.withStatus(order, order.getOrderStatus());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        Order updated = orderService.update(valid);
        if (updated == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Order not found: " + order.getOrderID());
        }
        return updated;
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable int id) {
        orderService.delete(id);
    }

    @GetMapping("/getAll")
    public List<Order> getAll() {
        return orderService.getAll();
    }

    @GetMapping("/findByBuyerId/{userId}")
    public List<Order> findByBuyerId(@PathVariable int userId) {
        return orderService.findByBuyerId(userId);
    }
}
