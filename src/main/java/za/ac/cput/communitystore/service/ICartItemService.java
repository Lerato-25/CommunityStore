package za.ac.cput.communitystore.service;

import za.ac.cput.communitystore.domain.CartItem;

import java.util.List;

public interface ICartItemService extends IService<CartItem, Integer> {

    List<CartItem> findByCartId(int cartId);
}
