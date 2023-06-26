package ru.practicum.main.user.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.main.exception.exceptions.NotFoundException;
import ru.practicum.main.user.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    default User getByIdAndCheck(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException("User with id: " + id + " is not found"));
    }

    List<User> findByEmailContainingIgnoreCase(String emailSearch);

    List<User> findByIdIn(Long[] ids, Pageable pageable);
}