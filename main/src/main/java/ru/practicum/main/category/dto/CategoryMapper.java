package ru.practicum.main.category.dto;

import ru.practicum.main.category.model.Category;

public class CategoryMapper {
    public static Category newCategoryToCategory(NewCategoryRequest newCategoryRequest) {
        Category category = new Category();
        category.setName(newCategoryRequest.getName());
        return category;
    }

    public static Category categoryDtoToCategory(CategoryDto categoryDto) {
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());
        return category;
    }

    public static CategoryDto toCategoryDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }
}
