package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


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
    public String showUserList(Model model, Authentication authentication) {
        User user = new User();
        String email = authentication.getName();
        User editUser = userService.findUserByEmail(email);
        String roleString = editUser.getRoles().stream().map(role -> role.getName().replaceAll("ROLE_", ""))
                .collect(Collectors.joining(" "));
        model.addAttribute("user", user);
        model.addAttribute("edit_user", editUser);
        model.addAttribute("edit_roles", roleString);
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("roles", roleService.findAll());
        return "list-users";
    }

   /* @GetMapping("/list")
    public String listCustomers(Model theModel, Authentication authentication) {
        List<User> theUsers = userService.getUsers();
        theModel.addAttribute("users", theUsers);
        User theUser = new User();
        theModel.addAttribute("user", theUser);
        theModel.addAttribute("roles", roleService.findAll());
        String name = authentication.getName();
        User principalUser = userService.getUserByName(name);
        theModel.addAttribute("principal_user", principalUser);

        String roleString = principalUser.getRoles().stream().map(role -> role.getName().replaceAll("ROLE_", ""))
                .collect(Collectors.joining(" "));
        theModel.addAttribute("principal_roles", roleString);
        return "list_users_with_modal";
    }
*/

/*
    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.findAll());
        return "user-form";
    }
*/

   /* @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") @Valid User user, BindingResult result,
                           @RequestParam("roles") String[] roles, Model model) {

        if (result.hasErrors()) { //valid
            model.addAttribute("user", user);
            model.addAttribute("roles", roleService.findAll());
            return "user-form";
        }
        String encode = user.getPassword();        //new user
        passwordChanged(user, encode);
        for (String r : roles) {
            user.addRole(roleService.getRole(Long.valueOf(r)));
        }
        userService.add(user);
        return "redirect:/admin/list";
    }*/

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") User user, @RequestParam("roles") String[] roles) {
        String encode = user.getPassword();
        if (user.getId() != 0) { // update user
            if (encode.isEmpty()) { //  password not changed
                user.setPassword(userService.getUserById(user.getId()).getPassword());
            } else {
                passwordChanged(user, encode);
            }
        } else { //new user
            passwordChanged(user, encode);
        }

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

   /* @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("userId") Long id, Model model) {

        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.findAll());
        return "user-form-for-update";
    }

    @PostMapping("/updateUser")
    public String updateUser(@ModelAttribute("user") @Valid User user, BindingResult result,
                             @RequestParam("roles") String[] roles, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("roles", roleService.findAll());
            return "user-form-for-update";
        }
        String encode = user.getPassword();
        if (encode.isEmpty()) {
            user.setPassword(userService.getUserById(user.getId()).getPassword());
        } else {
            passwordChanged(user, encode);
        }
        user.getRoles().clear();
        for (String r : roles) {
            user.addRole(roleService.getRole(Long.valueOf(r)));
        }
        userService.update(user);
        return "redirect:/admin/list";
    }*/

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("userId") Long id) {
        userService.deleteUserById(id);
        return "redirect:/admin/list";
    }
}
