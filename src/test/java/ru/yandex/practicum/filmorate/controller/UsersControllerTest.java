package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UsersControllerTest {
   /* public static UserController userController = new UserController();

    @AfterEach
    void afterEach() {
        userController = new UserController();
    }

    @DisplayName("Создание пользователя с пустым email")
    @Test
    void userController_Create_NullEmailAdd() {
        User user = new User();
        user.setId(0);
        user.setEmail("");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.of(1999, 12, 8));

        assertThrows(ConditionsNotMetException.class, () -> {
            userController.create(user);
        }, "Поле email не может быть пустым");

        assertEquals(0, userController.getAll().size(), "Пользователь не должен был добавиться");
    }

    @DisplayName("Создание пользователя с некорректным email")
    @Test
    void userController_Create_IncorrectEmail() {
        User user = new User();
        user.setId(0);
        user.setEmail("test");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.of(1999, 12, 8));

        assertThrows(ConditionsNotMetException.class, () -> {
            userController.create(user);
        }, "Поле email должно быть со знаком @");

        assertEquals(0, userController.getAll().size(), "Пользователь не должен был добавиться");
    }

    @DisplayName("Создание пользователя с пустым login")
    @Test
    void userController_Create_EmptyLogin() {
        User user = new User();
        user.setId(0);
        user.setEmail("test@test.ru");
        user.setLogin("");
        user.setName("name");
        user.setBirthday(LocalDate.of(1999, 12, 8));

        assertThrows(ConditionsNotMetException.class, () -> {
            userController.create(user);
        }, "Поле login не должно быть пустым");

        assertEquals(0, userController.getAll().size(), "Пользователь не должен был добавиться");
    }

    @DisplayName("Создание не родившегося пользователя")
    @Test
    void userController_Create_FutureBirthday() {
        User user = new User();
        user.setId(0);
        user.setEmail("test@test.ru");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.of(2077, 10, 23));

        assertThrows(ConditionsNotMetException.class, () -> {
            userController.create(user);
        }, "Поле birthday не должно отсылать в будущие");

        assertEquals(0, userController.getAll().size(), "Пользователь не должен был добавиться");
    }

    @DisplayName("Обновление пользователя с пустым id")
    @Test
    void userController_Update_EmptyId() {
        User user = new User();
        user.setId(0);
        user.setEmail("test@test.ru");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.of(1999, 8, 12));

        userController.create(user);
        assertEquals(1, userController.getAll().size());

        User newUser = new User();
        newUser.setId(null);
        newUser.setEmail("test@test.ru");
        newUser.setLogin("login");
        newUser.setName("name");
        newUser.setBirthday(LocalDate.of(1999, 8, 12));

        assertThrows(ConditionsNotMetException.class, () -> {
            userController.update(newUser);
        }, "id не должен быть пустым");
    }

    @DisplayName("Обновление пользователя c неизвестным id")
    @Test
    void userController_Update_NonExistId() {
        User user = new User();
        user.setId(0);
        user.setEmail("test@test.ru");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.of(1999, 8, 12));

        userController.create(user);
        assertEquals(1, userController.getAll().size());

        User newUser = new User();
        newUser.setId(16165);
        newUser.setEmail("test@test.ru");
        newUser.setLogin("login");
        newUser.setName("name");
        newUser.setBirthday(LocalDate.of(1999, 8, 12));

        assertThrows(NotFoundException.class, () -> {
            userController.update(newUser);
        }, "id не должен быть списке users");
    }

    @DisplayName("Обновление пользователя c пустым email")
    @Test
    void userController_Update_EmptyEmail() {
        User user = new User();
        user.setId(0);
        user.setEmail("test@test.ru");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.of(1999, 8, 12));

        userController.create(user);
        assertEquals(1, userController.getAll().size());

        User newUser = new User();
        newUser.setId(1);
        newUser.setEmail("");
        newUser.setLogin("login");
        newUser.setName("name");
        newUser.setBirthday(LocalDate.of(1999, 8, 12));

        assertThrows(ConditionsNotMetException.class, () -> {
            userController.update(newUser);
        }, "Email не должен быть пустым");
    }

    @DisplayName("Обновление пользователя c некорректным email")
    @Test
    void userController_Update_IncorrectEmail() {
        User user = new User();
        user.setId(0);
        user.setEmail("test@test.ru");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.of(1999, 8, 12));

        userController.create(user);
        assertEquals(1, userController.getAll().size());

        User newUser = new User();
        newUser.setId(1);
        newUser.setEmail("incorrect email");
        newUser.setLogin("login");
        newUser.setName("name");
        newUser.setBirthday(LocalDate.of(1999, 8, 12));

        assertThrows(ConditionsNotMetException.class, () -> {
            userController.update(newUser);
        }, "Email должен быть корректен");
    }

    @DisplayName("Обновление пользователя c пустым login")
    @Test
    void userController_Update_EmptyLogin() {
        User user = new User();
        user.setId(0);
        user.setEmail("test@test.ru");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.of(1999, 8, 12));

        userController.create(user);
        assertEquals(1, userController.getAll().size());

        User newUser = new User();
        newUser.setId(1);
        newUser.setEmail("test@test.ru");
        newUser.setLogin("");
        newUser.setName("name");
        newUser.setBirthday(LocalDate.of(1999, 8, 12));

        assertThrows(ConditionsNotMetException.class, () -> {
            userController.update(newUser);
        }, "Login не может быть пустым");
    }

    @DisplayName("Обновление пользователя c пустым login")
    @Test
    void userController_Update_FutureBirthday() {
        User user = new User();
        user.setId(0);
        user.setEmail("test@test.ru");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.of(1999, 8, 12));

        userController.create(user);
        assertEquals(1, userController.getAll().size());

        User newUser = new User();
        newUser.setId(1);
        newUser.setEmail("test@test.ru");
        newUser.setLogin("");
        newUser.setName("name");
        newUser.setBirthday(LocalDate.of(2077, 10, 23));

        assertThrows(ConditionsNotMetException.class, () -> {
            userController.update(newUser);
        }, "Поле birthday не должно отсылать в будущие");
    }
*/

}
