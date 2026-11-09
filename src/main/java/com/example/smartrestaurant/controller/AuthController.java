package com.example.smartrestaurant.controller;

import com.example.smartrestaurant.dto.request.LoginRequest;
import com.example.smartrestaurant.dto.request.RegisterRequest;
import com.example.smartrestaurant.dto.response.AuthResponse;
import com.example.smartrestaurant.dto.response.UserRoleResponse;
import com.example.smartrestaurant.exception.ApiException;
import com.example.smartrestaurant.model.Users;
import com.example.smartrestaurant.model.Role;
import com.example.smartrestaurant.repositories.RoleRepository;
import com.example.smartrestaurant.repositories.UserRepository;
import com.example.smartrestaurant.security.jwt.JwtUtils;
import com.example.smartrestaurant.security.services.TokenRevocationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRevocationService tokenRevocationService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils,
                          UserRepository userRepository, RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder,
                          TokenRevocationService tokenRevocationService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRevocationService = tokenRevocationService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username().trim(), request.password()));
        String token = jwtUtils.generateToken(authentication);

        Users user = loadUser(authentication.getName());
        userRepository.touchLogin(user.getId(), Instant.now());

        return ResponseEntity.ok(new AuthResponse(token, jwtUtils.getExpirationMs(),
                user.getUsername(), authentication.getAuthorities().stream()
                        .map(authority -> authority.getAuthority())
                        .toList()));
    }

    @PostMapping("/register")
    public ResponseEntity<UserRoleResponse> register(@Valid @RequestBody RegisterRequest request) {
        String username = request.username().trim();
        if (userRepository.existsByUsername(username)) {
            throw ApiException.duplicate("Username already exists", "ឈ្មោះអ្នកប្រើមានរួចហើយ");
        }

        Role cashierRole = roleRepository.findByName("CASHIER").orElseGet(() -> {
            Role role = new Role();
            role.setName("CASHIER");
            role.setDescription("Cashier");
            return roleRepository.save(role);
        });

        Users user = new Users();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(cashierRole);
        user.setStatus("ACT");
        return ResponseEntity.status(201).body(UserRoleResponse.from(userRepository.save(user)));
    }

    @GetMapping("/me")
    public ResponseEntity<UserRoleResponse> me(Authentication authentication) {
        return ResponseEntity.ok(UserRoleResponse.from(loadUser(authentication.getName())));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            jwtUtils.parseClaims(token).ifPresent(claims ->
                    tokenRevocationService.revoke(token, claims.getExpiration().toInstant()));
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    }

    private Users loadUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> ApiException.notFound("User", "អ្នកប្រើ", username));
    }
}
