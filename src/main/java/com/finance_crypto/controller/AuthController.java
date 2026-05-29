package com.finance_crypto.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finance_crypto.dto.MeResponseDTO;
import com.finance_crypto.dto.RegisterRequestDTO;
import com.finance_crypto.dto.RegisterResponseDTO;
import com.finance_crypto.entity.User;
import com.finance_crypto.repository.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class AuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<RegisterResponseDTO> register(@Valid @RequestBody RegisterRequestDTO dto) {
        if (userRepository.findByUsername(dto.username()).isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        var user = new User();
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        userRepository.save(user);

        return ResponseEntity.ok(new RegisterResponseDTO(
                user.getUserId().toString(),
                user.getUsername(),
                user.getEmail()));
    }

    @GetMapping("/me")
    public ResponseEntity<MeResponseDTO> me(JwtAuthenticationToken token) {
        var userId = UUID.fromString(token.getName());
        return userRepository.findById(userId)
                .map(user -> ResponseEntity.ok(new MeResponseDTO(
                        user.getUserId().toString(),
                        user.getUsername(),
                        user.getEmail())))
                .orElse(ResponseEntity.notFound().build());
    }
}
