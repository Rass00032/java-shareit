package ru.practicum.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    private User user;
    private UserDto userDto;
    private List<User> users;
    private List<UserDto> userDtoList;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("Viktor")
                .email("viktor@gmail.com").build();
        users = new ArrayList<>();
        users.add(user);

        userDto = UserDto.builder()
                .id(1L)
                .name("Viktor")
                .email("viktor@gmail.com").build();
        userDtoList = new ArrayList<>();
        userDtoList.add(userDto);

    }

    @Test
    void createUserWithValidInputsShouldReturnsUser() {
        when(userRepository.save(any()))
                .thenReturn(user.toBuilder()
                        .id(1L)
                        .build());

        User result = userService.add(userDto);

        assertEquals(result.getId(), user.getId());
        assertEquals(result.getName(), user.getName());
        assertEquals(result.getEmail(), user.getEmail());
    }

    @Test
    void editWithValidInputsShouldReturnsUser() {
        User updateUser = User.builder()
                .id(1L)
                .name("Biktor")
                .email("biktor@mail.au").build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any())).then(invocation -> invocation.getArgument(0));

        User result = userService.edit(1L, updateUser);

        assertEquals(1, result.getId());
        assertEquals("Biktor", result.getName());
        assertEquals("biktor@mail.au", result.getEmail());
    }

    @Test
    void editWithValidInputsShouldUpdateName() {
        User updateUser = User.builder()
                .name("rrr").build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any())).then(invocation -> invocation.getArgument(0));

        User result = userService.edit(1L, updateUser);

        assertEquals(1, result.getId());
        assertEquals("rrr", result.getName());
        assertEquals("viktor@gmail.com", result.getEmail());
    }

    @Test
    void editWithValidInputsShouldUpdateEmail() {
        User updateUser = User.builder()
                .email("troro@jim.up").build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any())).then(invocation -> invocation.getArgument(0));

        User result = userService.edit(1L, updateUser);

        assertEquals(1, result.getId());
        assertEquals("Viktor", result.getName());
        assertEquals("troro@jim.up", result.getEmail());
    }

    @Test
    void editWithValidInputsDoNotShouldUpdate() {
        User updateUser = user.toBuilder().build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any())).then(invocation -> invocation.getArgument(0));

        User result = userService.edit(1L, updateUser);

        assertEquals(1, result.getId());
        assertEquals("Viktor", result.getName());
        assertEquals("viktor@gmail.com", result.getEmail());
    }

    @Test
    void editWithNonExistingUserShouldThrowsNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> userService.edit(1L, user)
        );

        assertEquals("User with ID: 1 not found", exception.getMessage());
    }

    @Test
    void getUserByIdWithValidInputsShouldReturnsUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        User result = userService.getById(1L);

        assertEquals(result.getId(), user.getId());
        assertEquals(result.getName(), user.getName());
        assertEquals(result.getEmail(), user.getEmail());
    }

    @Test
    void getAllByUserIdWithValidInputsShouldReturnsUsersWithBookings() {
        when(userRepository.findAll()).thenReturn(users);

        List<UserDto> result = userService.getAll();

        assertEquals(result.get(0).getId(), user.getId());
        assertEquals(result.get(0).getName(), user.getName());
        assertEquals(result.get(0).getEmail(), user.getEmail());
    }

    @Test
    void deleteByUserId() {
        userRepository.deleteById(anyLong());
    }
}