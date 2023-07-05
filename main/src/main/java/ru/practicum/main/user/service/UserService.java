package ru.practicum.main.user.service;


import ru.practicum.main.user.dto.NewUserRequest;
import ru.practicum.main.user.dto.UserDto;

import java.util.Collection;
import java.util.List;

public interface UserService {

    UserDto createUser(NewUserRequest user);

    Collection<UserDto> getAll(List<Long> ids, Integer from, Integer size);

    void deleteUser(Long id);
}