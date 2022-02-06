package kma.topic2.junit.service;

import kma.topic2.junit.exceptions.UserNotFoundException;
import kma.topic2.junit.model.NewUser;
import kma.topic2.junit.model.User;
import kma.topic2.junit.repository.UserRepository;
import kma.topic2.junit.validation.UserValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;


@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @SpyBean
    private UserValidator userValidator;

    private NewUser newUser = NewUser.builder()
            .fullName("FullName")
            .login("Login")
            .password("Passwor")
            .build();

    @Test
    void createNewUser_success() {

        userService.createNewUser(newUser);

        assertThat(userRepository.getUserByLogin(newUser.getLogin()))
                .returns("Passwor", User::getPassword)
                .returns("Login", User::getLogin);
        verify(userValidator).validateNewUser(newUser);
    }

    @Test
    void getUserByLogin_success() {
        userRepository.saveNewUser(newUser);

        assertThat(userRepository.getUserByLogin(newUser.getLogin()))
                .returns("Passwor", User::getPassword)
                .returns("Login", User::getLogin);
    }

    @Test
    void getUserByLogin_success_throwUserNotFoundException() {

        String login = "NoLogin";
        assertThatThrownBy(() -> userService.getUserByLogin(login))
                .isInstanceOfSatisfying(UserNotFoundException.class,
                        x -> assertThat(x.getMessage()).isEqualTo("Can't find user by login: " + login));
    }
}
