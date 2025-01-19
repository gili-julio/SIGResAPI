package br.ufrn.imd.SIGResAPI.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufrn.imd.SIGResAPI.dto.CreateUserDTO;
import br.ufrn.imd.SIGResAPI.dto.UserDTO;
import br.ufrn.imd.SIGResAPI.enums.ERole;
import br.ufrn.imd.SIGResAPI.models.Role;
import br.ufrn.imd.SIGResAPI.models.User;
import br.ufrn.imd.SIGResAPI.repository.RoleRepository;
import br.ufrn.imd.SIGResAPI.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @GetMapping
    public ResponseEntity<List<User>> allUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserDTO body) {
        // Verifica se o username já existe
        if (userRepository.existsByUsername(body.username())) {
            return ResponseEntity.badRequest().build();
        }

        // Converte os nomes das roles para objetos Role
        Set<Role> roles = new HashSet<>();
        for (String roleName : body.roles()) {
            ERole eRole = ERole.valueOf(roleName); // Obtém o enum ERole baseado no nome da role
            Role role = roleRepository.findByName(eRole)
                    .orElseThrow(() -> new RuntimeException("Role not found")); // Busca a Role no banco de dados
            roles.add(role);
        }

        // Cria o usuário com as roles
        User user = new User(null, body.username(), passwordEncoder.encode(body.password()), roles, null, null, null);
        userRepository.save(user);

        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> user(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserDTO body) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(passwordEncoder.encode(body.password()));
        user.setUsername(body.username());
        user.setRoles(body.roles());
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
