package com.example.smartrestaurant.config;

import com.example.smartrestaurant.model.Role;
import com.example.smartrestaurant.model.Users;
import com.example.smartrestaurant.repositories.RoleRepository;
import com.example.smartrestaurant.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BootstrapAdmin implements ApplicationRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final String username;
    private final String password;

    public BootstrapAdmin(UserRepository userRepository, RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder,
                          @Value("${app.bootstrap.admin-username:admin}") String username,
                          @Value("${app.bootstrap.admin-password:}") String password) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.username = username;
        this.password = password;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (password == null || password.isBlank() || userRepository.count() > 0) {
            return;
        }
        Role adminRole = roleRepository.findByName("ADMIN").orElseGet(() -> {
            Role role = new Role();
            role.setName("ADMIN");
            role.setDescription("Administrator");
            return roleRepository.save(role);
        });
        Users admin = new Users();
        admin.setUsername(username);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setRole(adminRole);
        admin.setStatus("ACT");
        userRepository.save(admin);
        log.info("Created first admin user '{}'. Remove ADMIN_PASSWORD from .env now.", username);
    }
}