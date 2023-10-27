package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.kata.spring.boot_security.demo.dao.UserRepository;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public void add(User user) {
        userRepository.saveAndFlush(user);

    }


    @Override
    public User getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        User user;
        if (optionalUser.isPresent()){
            user=optionalUser.get();
        }else {
            throw new RuntimeException("Не найден");
        }
        return user;
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);

    }

    @Override
    public User findUserByName(String name) {
        return userRepository.findUserByName(name);
    }


}
