package kma.topic2.junit.validation;

import kma.topic2.junit.exceptions.ConstraintViolationException;
import kma.topic2.junit.exceptions.LoginExistsException;
import kma.topic2.junit.model.NewUser;
import kma.topic2.junit.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserValidatorTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserValidator userValidator;

    private NewUser newUser = NewUser.builder()
            .fullName("FullName")
            .login("Login")
            .password("Passwor")
            .build();

    @Test
    void validateNewUserTest_success() {

        userValidator.validateNewUser(newUser);
        verify(userRepository).isLoginExists(newUser.getLogin());
    }

    @Test
    void validateNewUserTest_throwLoginExistsException() {

        when(userRepository.isLoginExists(newUser.getLogin())).thenReturn(true);
        assertThatThrownBy(() -> userValidator.validateNewUser(newUser)).isInstanceOf(LoginExistsException.class);
    }

    @Test
    void validatePasswordTest_invalidSize_throwConstraintViolationException() {

        NewUser user1 = NewUser.builder()
                .fullName("FullName")
                .login("Login")
                .password("p")
                .build();

        NewUser user2 = NewUser.builder()
                .fullName("FullName")
                .login("Login")
                .password("ppppppppppppp")
                .build();

        assertThatThrownBy(() -> userValidator.validateNewUser(user1)).isInstanceOf(ConstraintViolationException.class);
        assertThatThrownBy(() -> userValidator.validateNewUser(user2)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void validatePasswordTest_notMatchRegex_throwConstraintViolationException() {

        NewUser user1 = NewUser.builder()
                .fullName("FullName")
                .login("Login")
                .password("p@#$")
                .build();

        assertThatThrownBy(() -> userValidator.validateNewUser(user1)).isInstanceOf(ConstraintViolationException.class);
    }

}
