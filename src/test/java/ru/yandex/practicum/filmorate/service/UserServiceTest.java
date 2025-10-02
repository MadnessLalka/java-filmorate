package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @DisplayName("Получение списка друзей у конкретного пользователя")
    @Test
    void getCollection_GetCollection_Friends(){
        User user = new User();
        user.setEmail("test@mail.ru");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.of(1999,12,8));
        user.setFriends(Set.of(2,3));
        System.out.println(user);


        User user1 = new User();
        user.setEmail("test1@mail.ru");
        user.setLogin("login1");
        user.setName("name1");
        user.setBirthday(LocalDate.of(2000,12,8));
        System.out.println(user1);

        User user2 = new User();
        user.setEmail("test2@mail.ru");
        user.setLogin("login2");
        user.setName("name2");
        user.setBirthday(LocalDate.of(2001,12,8));
        System.out.println(user2);

        userService.create(user1);
        userService.create(user2);
        userService.create(user);

        Assertions.assertEquals(3, userService.getAll().size(), "Было добавлено 3 пользователя");

        Assertions.assertEquals(2, userService.getUserFriends(user).size(), "У пользователя user 2 друга");

    }

}
