package za.ac.cput.communitystore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.communitystore.domain.ShoppingCart;
import za.ac.cput.communitystore.repository.ShoppingCartRepository;

import java.util.List;

@Service
public class ShoppingCartService implements IShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;

    @Autowired
    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }

    @Override
    public ShoppingCart create(ShoppingCart cart) {
        return shoppingCartRepository.save(cart);
    }

    @Override
    public ShoppingCart read(Integer id) {
        return shoppingCartRepository.findById(id).orElse(null);
    }

    @Override
    public ShoppingCart update(ShoppingCart cart) {
        if (!shoppingCartRepository.existsById(cart.getCartID())) {
            return null;
        }
        return shoppingCartRepository.save(cart);
    }

    @Override
    public void delete(Integer id) {
        shoppingCartRepository.deleteById(id);
    }

    @Override
    public List<ShoppingCart> getAll() {
        return shoppingCartRepository.findAll();
    }

    @Override
    public ShoppingCart findByUserId(int userId) {
        return shoppingCartRepository.findByUser_UserID(userId).orElse(null);
    }
}
