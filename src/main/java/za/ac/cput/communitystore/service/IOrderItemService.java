package za.ac.cput.communitystore.service;

import za.ac.cput.communitystore.domain.OrderItem;

import java.util.List;

public interface IOrderItemService extends IService<OrderItem, Integer> {

    List<OrderItem> findByOrderId(int orderId);
}
