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
import za.ac.cput.communitystore.domain.Product;
import za.ac.cput.communitystore.factory.ProductFactory;
import za.ac.cput.communitystore.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    public Product create(@RequestBody Product product) {
        try {
            Product valid = ProductFactory.createProduct(
                    product.getSeller(),
                    product.getCategory(),
                    product.getProductName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getQuantity(),
                    product.getCondition(),
                    product.getStatus());

            return productService.create(valid);

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage());
        }
    }

    @GetMapping("/read/{id}")
    public Product read(@PathVariable int id) {

        Product product = productService.read(id);

        if (product == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Product not found: " + id);
        }

        return product;
    }

    @PutMapping("/update")
    public Product update(@RequestBody Product product) {

        try {
            Product valid = ProductFactory.withDetails(
                    product,
                    product.getSeller(),
                    product.getCategory(),
                    product.getProductName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getQuantity(),
                    product.getCondition(),
                    product.getStatus());

            Product updated = productService.update(valid);

            if (updated == null) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product not found: " + product.getProductID());
            }

            return updated;

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable int id) {
        productService.delete(id);
    }

    @GetMapping("/getAll")
    public List<Product> getAll() {
        return productService.getAll();
    }

    @GetMapping("/findByCategoryId/{categoryID}")
    public List<Product> findByCategoryId(@PathVariable int categoryID) {
        return productService.findByCategoryId(categoryID);
    }

    @GetMapping("/findBySellerId/{userID}")
    public List<Product> findBySellerId(@PathVariable int userID) {
        return productService.findBySellerId(userID);
    }
}