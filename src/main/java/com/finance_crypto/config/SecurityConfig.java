package com.finance_crypto.config;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;

@Configuration // indica uma classe de configuração do Spring
@EnableWebSecurity // habilita a segurança web do Spring Security
public class SecurityConfig {

    @Value("${jwt.public.key}") // aponta para properties
    private RSAPublicKey publicKey; // chave pública para validar tokens JWT

    @Value("${jwt.private.key}") // aponta para properties
    private RSAPrivateKey privateKey; // chave privada para assinar tokens JWT

    @Bean // indica que esse metodo retorna um bean gerenciado pelo Spring
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http

                // todas as requisições precisam ser autenticadas
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())

                // desabilita proteção contra CSRF para testes (Cross-Site Request Forgery)
                .csrf(csrf -> csrf.disable())

                // configura o app como um Resource Server que valida tokens JWT
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))

                // não precisa guardar nada em sessão
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // constrói a cadeia de filtros de segurança e a torna um bean
        return http.build();
    }

    @Bean
    // cria um bean para codificar (assinar) tokens JWT
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(this.publicKey).privateKey(privateKey).build();
        var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));

        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    // cria um bean para decodificar (validar) tokens JWT
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    @Bean
    // cria um bean para codificar senhas usando BCrypt
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
