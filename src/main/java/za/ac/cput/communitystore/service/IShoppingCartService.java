package za.ac.cput.communitystore.service;

import za.ac.cput.communitystore.domain.ShoppingCart;

public interface IShoppingCartService extends IService<ShoppingCart, Integer> {

    ShoppingCart findByUserId(int userId);
}
