package za.ac.cput.communitystore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.communitystore.domain.Product;
import za.ac.cput.communitystore.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product create(Product product) {
        return productRepository.save(product);
    }

    public Product read(int id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product update(Product product) {
        if (productRepository.existsById(product.getProductID())) {
            return productRepository.save(product);
        }
        return null;
    }

    public void delete(int id) {
        productRepository.deleteById(id);
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public List<Product> findByCategoryId(int categoryID) {
        return productRepository.findByCategory_CategoryID(categoryID);
    }

    public List<Product> findBySellerId(int userID) {
        return productRepository.findBySeller_UserID(userID);
    }
}