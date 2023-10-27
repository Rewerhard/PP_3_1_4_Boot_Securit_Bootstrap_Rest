package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public AdminController(UserService userService, RoleService roleService, BCryptPasswordEncoder encoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.encoder = encoder;
    }

    @GetMapping("/list")
    public String showUserList(Model model) {
        model.addAttribute("users", userService.getUsers());
        return "list-users";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.findAll());
        return "user-form";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") User user, @RequestParam("roles") String[] roles) {
        String encode = user.getPassword();
        if (user.getId() == null) { //new user
            passwordChanged(user, encode);
        }else {
            if (encode.isEmpty()) {
                user.setPassword(userService.getUserById(user.getId()).getPassword());
            } else {
                passwordChanged(user, encode);
            }

        }
        /*if (user.getId() != 0) {
            if (encode.isEmpty()) {
                user.setPassword(userService.getUserById(user.getId()).getPassword());
            } else {
                passwordChanged(user, encode);
            }
        } else { //new user
            passwordChanged(user, encode);
        }*/

        user.getRoles().clear();
        for (String r : roles) {
            user.addRole(roleService.getRole(Long.valueOf(r)));
        }

        userService.add(user);
        return "redirect:/admin/list";
    }

    private void passwordChanged(User user, String encode) {
        encode = encoder.encode(encode);
        user.setPassword(encode);
    }

    @PostMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("userId") Long id,
                                    Model model) {

        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.findAll());
        return "user-form";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("userId") Long id) {
        userService.deleteUserById(id);
        return "redirect:/admin/list";
    }
}
