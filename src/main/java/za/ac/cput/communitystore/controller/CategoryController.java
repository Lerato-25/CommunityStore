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
import za.ac.cput.communitystore.domain.Category;
import za.ac.cput.communitystore.factory.CategoryFactory;
import za.ac.cput.communitystore.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/create")
    public Category create(@RequestBody Category category) {
        try {
            Category valid = CategoryFactory.createCategory(
                    category.getCategoryName(),
                    category.getDescription());

            return categoryService.create(valid);

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage());
        }
    }

    @GetMapping("/read/{id}")
    public Category read(@PathVariable int id) {

        Category category = categoryService.read(id);

        if (category == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Category not found: " + id);
        }

        return category;
    }

    @PutMapping("/update")
    public Category update(@RequestBody Category category) {

        try {
            Category valid = CategoryFactory.withDetails(
                    category,
                    category.getCategoryName(),
                    category.getDescription());

            Category updated = categoryService.update(valid);

            if (updated == null) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Category not found: " + category.getCategoryID());
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
        categoryService.delete(id);
    }

    @GetMapping("/getAll")
    public List<Category> getAll() {
        return categoryService.getAll();
    }
}