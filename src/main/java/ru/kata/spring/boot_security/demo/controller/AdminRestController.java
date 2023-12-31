package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/adminAPI")
public class AdminRestController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }
    @GetMapping("/list")
    public ResponseEntity<List<User>> listUser() {
        List<User> users = userService.getUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> listRoles() {
        final List<Role> roles = roleService.findAll();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @PutMapping("/saveUser")
    public ResponseEntity<?> saveUser(@RequestBody @Valid User user) {
        userService.add(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(@RequestBody User user) {
        userService.deleteUserById(user.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
