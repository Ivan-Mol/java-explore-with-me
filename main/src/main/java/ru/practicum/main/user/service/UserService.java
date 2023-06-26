package ru.practicum.main.user.service;


import ru.practicum.main.user.dto.UserDto;
import ru.practicum.main.user.dto.NewUserRequest;

import java.util.Collection;

public interface UserService {

    UserDto createUser(NewUserRequest user);

    Collection<UserDto> getAll(Long[] ids, Integer from, Integer size);

    void deleteUser(Long id);
}