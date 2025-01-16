package br.ufrn.imd.SIGResAPI;

import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.ufrn.imd.SIGResAPI.enums.ERole;
import br.ufrn.imd.SIGResAPI.models.Role;
import br.ufrn.imd.SIGResAPI.models.User;
import br.ufrn.imd.SIGResAPI.repository.RoleRepository;
import br.ufrn.imd.SIGResAPI.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Verifica se já existe o role "ROLE_ADMIN"
        Optional<Role> adminOptRole = roleRepository.findByName(ERole.ROLE_ADMIN);
        Role adminRole = new Role();
        if (adminOptRole.isEmpty()) {
            adminRole.setName(ERole.ROLE_ADMIN);
            roleRepository.save(adminRole);
        } else {
            adminRole = adminOptRole.get();
        }
        // Verifica se o usuário "admin" já existe
        Optional<User> adminOptUser = userRepository.findByUsername("admin");
        User adminUser = new User();
        if (adminOptUser.isEmpty()) {
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("admin"));
            userRepository.save(adminUser);
        } else {
            adminUser = adminOptUser.get();
        }
        // Verifica se o usuário já possui o role "ROLE_ADMIN"
        if (!adminUser.getRoles().contains(adminRole)) {
            adminUser.getRoles().add(adminRole);
            userRepository.save(adminUser);
        }

        // Verifica se já existe o role "ROLE_USER"
        Optional<Role> userOptRole = roleRepository.findByName(ERole.ROLE_USER);
        Role userRole = new Role();
        if (userOptRole.isEmpty()) {
            userRole.setName(ERole.ROLE_USER);
            roleRepository.save(userRole);
        } else {
            userRole = userOptRole.get();
        }
        // Verifica se o usuário "user" já existe
        Optional<User> userOptUser = userRepository.findByUsername("user");
        User userUser = new User();
        if (userOptUser.isEmpty()) {
            userUser.setUsername("user");
            userUser.setPassword(passwordEncoder.encode("user"));
            userRepository.save(userUser);
        } else {
            userUser = userOptUser.get();
        }
        // Verifica se o usuário já possui o role "ROLE_USER"
        if (!userUser.getRoles().contains(userRole)) {
            userUser.getRoles().add(userRole);
            userRepository.save(userUser);
        }
    }
}
