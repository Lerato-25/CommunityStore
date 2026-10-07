package za.ac.cput.communitystore.factory;

import za.ac.cput.communitystore.domain.Category;

public class CategoryFactory {

    public static Category createCategory(String categoryName, String description) {

        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name is required");
        }

        return new Category.Builder()
                .setCategoryName(categoryName)
                .setDescription(description)
                .build();
    }

    public static Category withDetails(Category category,
                                       String categoryName,
                                       String description) {

        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name is required");
        }

        return new Category.Builder()
                .setCategoryID(category.getCategoryID())
                .setCategoryName(categoryName)
                .setDescription(description)
                .build();
    }
}