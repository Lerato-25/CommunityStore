package za.ac.cput.communitystore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.communitystore.domain.Category;
import za.ac.cput.communitystore.repository.CategoryRepository;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    public Category read(int id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public Category update(Category category) {
        if (categoryRepository.existsById(category.getCategoryID())) {
            return categoryRepository.save(category);
        }
        return null;
    }

    public void delete(int id) {
        categoryRepository.deleteById(id);
    }

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }
}