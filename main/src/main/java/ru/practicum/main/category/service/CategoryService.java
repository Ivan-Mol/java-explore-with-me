package ru.practicum.main.category.service;


import ru.practicum.main.category.dto.CategoryDto;
import ru.practicum.main.category.dto.NewCategoryRequest;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(NewCategoryRequest newCategoryRequest);

    CategoryDto updateCategory(CategoryDto categoryDto);

    void deleteCategory(Long id);

    CategoryDto getCategoryById(Long id);

    List<CategoryDto> getAllCategories(Integer from, Integer size);
}