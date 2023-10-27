package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.RoleRepository;
import ru.kata.spring.boot_security.demo.entity.Role;

import java.util.Collection;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    private RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Collection<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Role getRole(Long id) {
        Optional<Role> optionalRole = roleRepository.findById(id);
        Role role;
        if (optionalRole.isPresent()) {
            role = optionalRole.get();
        } else {
            throw new RuntimeException("Нету роли");
        }
        return role;
    }
}
