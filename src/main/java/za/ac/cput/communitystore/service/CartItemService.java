package za.ac.cput.communitystore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.communitystore.domain.CartItem;
import za.ac.cput.communitystore.repository.CartItemRepository;

import java.util.List;

@Service
public class CartItemService implements ICartItemService {

    private final CartItemRepository cartItemRepository;

    @Autowired
    public CartItemService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public CartItem create(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    @Override
    public CartItem read(Integer id) {
        return cartItemRepository.findById(id).orElse(null);
    }

    @Override
    public CartItem update(CartItem cartItem) {
        if (!cartItemRepository.existsById(cartItem.getCartItemID())) {
            return null;
        }
        return cartItemRepository.save(cartItem);
    }

    @Override
    public void delete(Integer id) {
        cartItemRepository.deleteById(id);
    }

    @Override
    public List<CartItem> getAll() {
        return cartItemRepository.findAll();
    }

    @Override
    public List<CartItem> findByCartId(int cartId) {
        return cartItemRepository.findByCart_CartID(cartId);
    }
}
