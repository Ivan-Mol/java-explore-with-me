package ru.practicum.main.user.model;

import ru.practicum.main.user.dto.NewUserRequest;
import ru.practicum.main.user.dto.UserDto;
import ru.practicum.main.user.dto.UserShortDto;

import java.util.Collection;
import java.util.stream.Collectors;

public class UserMapper {

    public static User toUser(NewUserRequest newUserRequest) {
        User user = new User();
        user.setName(newUserRequest.getName());
        user.setEmail(newUserRequest.getEmail());

        return user;
    }

    public static UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());

        return userDto;
    }

    public static UserShortDto toUserShortDto(User user) {
        UserShortDto userDto = new UserShortDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        return userDto;
    }

    public static Collection<UserDto> toUserDto(Collection<User> users) {
        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toUnmodifiableList());
    }
}