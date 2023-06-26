package ru.practicum.main.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.main.user.dto.UserDto;
import ru.practicum.main.user.dto.NewUserRequest;
import ru.practicum.main.user.model.User;
import ru.practicum.main.user.model.UserMapper;
import ru.practicum.main.user.storage.UserRepository;
import java.util.Collection;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto createUser(NewUserRequest newUserRequest) {
        log.debug("Create user, SERVICE");
        User user = userRepository.save(UserMapper.toUser(newUserRequest));
        log.debug("User with id = {}, created", user.getId());
        return UserMapper.toUserDto(user);
    }

    @Override
    public Collection<UserDto> getAll(Long[] userIds, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from/size, size);
        if (userIds != null && userIds.length > 0) {
            return UserMapper.toUserDto(userRepository.findByIdIn(userIds, pageable));
        } else {
            return UserMapper.toUserDto(userRepository.findAll(pageable).getContent());
        }
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.getByIdAndCheck(id);
        userRepository.delete(user);
    }
}