package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;

public interface UserService {
    List<User> getUsers();

    void add(User user);

    void update(User user);

    User getUserById(Long id);

    void deleteUserById(Long id);

    User findUserByEmail(String email);
}
