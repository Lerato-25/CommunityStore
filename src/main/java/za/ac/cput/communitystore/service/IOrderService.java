package za.ac.cput.communitystore.service;

import za.ac.cput.communitystore.domain.Order;

import java.util.List;

public interface IOrderService extends IService<Order, Integer> {

    List<Order> findByBuyerId(int userId);
}
