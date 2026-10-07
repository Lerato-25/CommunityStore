package za.ac.cput.communitystore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.communitystore.domain.ProductImage;
import za.ac.cput.communitystore.repository.ProductImageRepository;

import java.util.List;

@Service
public class IProductImageService {

    private final ProductImageRepository productImageRepository;

    @Autowired
    public IProductImageService(ProductImageRepository productImageRepository) {
        this.productImageRepository = productImageRepository;
    }

    public ProductImage create(ProductImage productImage) {
        return productImageRepository.save(productImage);
    }

    public ProductImage read(int id) {
        return productImageRepository.findById(id).orElse(null);
    }

    public ProductImage update(ProductImage productImage) {
        if (productImageRepository.existsById(productImage.getImageId())) {
            return productImageRepository.save(productImage);
        }
        return null;
    }

    public void delete(int id) {
        productImageRepository.deleteById(id);
    }

    public List<ProductImage> getAll() {
        return productImageRepository.findAll();
    }

    public List<ProductImage> findByProductId(int productID) {
        return productImageRepository.findByProduct_ProductID(productID);
    }
}