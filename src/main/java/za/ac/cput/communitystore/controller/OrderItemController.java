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
import za.ac.cput.communitystore.domain.OrderItem;
import za.ac.cput.communitystore.factory.OrderItemFactory;
import za.ac.cput.communitystore.service.OrderItemService;

import java.util.List;

@RestController
@RequestMapping("/api/orderitems")
public class OrderItemController {

    private final OrderItemService orderItemService;

    @Autowired
    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @PostMapping("/create")
    public OrderItem create(@RequestBody OrderItem orderItem) {
        try {
            OrderItem valid = OrderItemFactory.createOrderItem(
                    orderItem.getOrder(), orderItem.getProduct(),
                    orderItem.getQuantity(), orderItem.getUnitPrice());
            return orderItemService.create(valid);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/read/{id}")
    public OrderItem read(@PathVariable int id) {
        OrderItem orderItem = orderItemService.read(id);
        if (orderItem == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order item not found: " + id);
        }
        return orderItem;
    }

    @PutMapping("/update")
    public OrderItem update(@RequestBody OrderItem orderItem) {
        try {
            // validation only: throws if any field is invalid
            OrderItemFactory.createOrderItem(
                    orderItem.getOrder(), orderItem.getProduct(),
                    orderItem.getQuantity(), orderItem.getUnitPrice());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        OrderItem updated = orderItemService.update(orderItem);
        if (updated == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Order item not found: " + orderItem.getOrderItemID());
        }
        return updated;
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable int id) {
        orderItemService.delete(id);
    }

    @GetMapping("/getAll")
    public List<OrderItem> getAll() {
        return orderItemService.getAll();
    }

    @GetMapping("/findByOrderId/{orderId}")
    public List<OrderItem> findByOrderId(@PathVariable int orderId) {
        return orderItemService.findByOrderId(orderId);
    }
}
