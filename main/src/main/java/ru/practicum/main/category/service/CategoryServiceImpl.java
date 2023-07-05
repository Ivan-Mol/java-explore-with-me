package ru.practicum.main.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.main.category.dto.CategoryDto;
import ru.practicum.main.category.dto.CategoryMapper;
import ru.practicum.main.category.dto.NewCategoryRequest;
import ru.practicum.main.category.model.Category;
import ru.practicum.main.category.storage.CategoryRepository;
import ru.practicum.main.event.storage.EventRepository;
import ru.practicum.main.exception.exceptions.ConflictException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public CategoryDto createCategory(NewCategoryRequest newCategoryRequest) {
        Category category = null;
        try {
            category = categoryRepository.save(CategoryMapper.newCategoryToCategory(newCategoryRequest));
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Name of created category is not unique");
        }
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        Category category = categoryRepository.getByIdAndCheck(categoryDto.getId());
        category.setName(categoryDto.getName());
        Category updatedCategory;
        try {
            updatedCategory = categoryRepository.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Name of updated category is not unique");
        }
        return CategoryMapper.toCategoryDto(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.getByIdAndCheck(id);
        if (!eventRepository.getFirstByCategoryId(id).isEmpty()) {
            throw new ConflictException("Can't delete category with events");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        return CategoryMapper.toCategoryDto(categoryRepository.getByIdAndCheck(id));
    }

    @Override
    public List<CategoryDto> getAllCategories(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return categoryRepository.findAll(pageable)
                .stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }
}
