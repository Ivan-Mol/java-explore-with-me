package ru.practicum.main.category.dto;

import ru.practicum.main.category.model.Category;

public class CategoryMapper {
    public static Category toCategory(NewCategoryRequest newCategoryRequest) {
        Category category = new Category();
        category.setName(newCategoryRequest.getName());
        return category;
    }

    public static CategoryDto toCategoryDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }
}
