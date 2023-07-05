package ru.practicum.main.compilation.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.main.compilation.model.Compilation;
import ru.practicum.main.exception.exceptions.NotFoundException;

import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    default Compilation getByIdAndCheck(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException("Compilation with id: " + id + " is not found"));
    }

    List<Compilation> getAllByPinned(Boolean pinned, Pageable pageable);
}
