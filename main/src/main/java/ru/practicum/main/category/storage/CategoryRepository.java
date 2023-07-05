package ru.practicum.main.category.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.main.category.model.Category;
import ru.practicum.main.exception.exceptions.NotFoundException;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    default Category getByIdAndCheck(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException("Category with id: " + id + " is not found"));
    }

    Page<Category> findAll(Pageable pageable);
}
