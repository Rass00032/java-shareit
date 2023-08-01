package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.Variables;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public User add(UserDto userDto) {
        User user = userMapper.fromDto(userDto);
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User edit(Long id, User updatedUser) {
        boolean isUpdated = false;
        User user = returnUserOrThrowUserNotFoundException(id);
        log.info("Обновление: {}", user);
        String updatedUserEmail = updatedUser.getEmail();
        if (updatedUserEmail != null && !updatedUserEmail.equals(user.getEmail()) && !updatedUserEmail.isBlank()) {
            user.setEmail(updatedUserEmail);
            log.info("Электронная почта обновлена");
            isUpdated = true;
        }
        String updatedUserName = updatedUser.getName();
        if (updatedUserName != null && !updatedUserName.equals(user.getName()) && !updatedUserName.isBlank()) {
            user.setName(updatedUser.getName());
            log.info("Имя ползователя обновлено");
            isUpdated = true;
        }
        if (isUpdated) {
            userRepository.save(user);
        }
        return user;
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public User getById(Long id) {
        return returnUserOrThrowUserNotFoundException(id);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    private User returnUserOrThrowUserNotFoundException(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.orElseThrow(
                () -> {
                    log.info("Throw new NotFoundException");
                    return new NotFoundException(Variables.USER_WITH_ID_NOT_FOUND, id);
                }
        );
    }
}
