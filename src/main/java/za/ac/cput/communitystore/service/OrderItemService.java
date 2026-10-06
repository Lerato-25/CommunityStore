package za.ac.cput.communitystore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.communitystore.domain.OrderItem;
import za.ac.cput.communitystore.repository.OrderItemRepository;

import java.util.List;

@Service
public class OrderItemService implements IOrderItemService {

    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public OrderItem create(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    @Override
    public OrderItem read(Integer id) {
        return orderItemRepository.findById(id).orElse(null);
    }

    @Override
    public OrderItem update(OrderItem orderItem) {
        if (!orderItemRepository.existsById(orderItem.getOrderItemID())) {
            return null;
        }
        return orderItemRepository.save(orderItem);
    }

    @Override
    public void delete(Integer id) {
        orderItemRepository.deleteById(id);
    }

    @Override
    public List<OrderItem> getAll() {
        return orderItemRepository.findAll();
    }

    @Override
    public List<OrderItem> findByOrderId(int orderId) {
        return orderItemRepository.findByOrder_OrderID(orderId);
    }
}
