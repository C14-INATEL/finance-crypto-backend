package com.finance_crypto.entity;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.finance_crypto.controller.dto.LoginRequestDTO;

@ExtendWith(MockitoExtension.class)
public class UserTest {

  private User user;

  @Mock
  private PasswordEncoder passwordEncoder;

  @BeforeEach
  void setUp() {
    // Cria mocks
    user = new User();
    user.setUsername("testuser");
    user.setPassword("hashed_password");
  }

  @Test
  void encoderConfirmaDadosValidos() {

    LoginRequestDTO request = new LoginRequestDTO("testuser", "correct_password");
    when(passwordEncoder.matches("correct_password", "hashed_password")).thenReturn(true);

    boolean result = user.isLoginCorrect(request, passwordEncoder);

    // Permite login
    assertTrue(result, "O login deve ser válido quando a senha está correta e bate com o hash (codificação).");
  }

  @Test
  void encoderNaoConfirmaDadosInvalidos() {

    LoginRequestDTO request = new LoginRequestDTO("testuser", "wrong_password");
    when(passwordEncoder.matches("wrong_password", "hashed_password")).thenReturn(false);

    boolean result = user.isLoginCorrect(request, passwordEncoder);

    // Nao permite login
    assertFalse(result, "O login precisa ser considerado inválido se a senha fornecida não for idêntica à do banco.");
  }
}
