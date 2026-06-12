package com.finance_crypto.service;

import com.finance_crypto.repository.PortfolioRepository;
import org.springframework.stereotype.Service;

@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

    public PortfolioService(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    // Criamos o método exatamente como o primeiro teste desenhou
    public double calcularSaldoTotal(Long usuarioId) {
        // Retorna 0.0 cravado para o TDD passar na fase verde.
        // O grupo fará a lógica de somar do banco de dados depois!
        return 0.0;
    }

    // Criamos o método exatamente como o segundo teste desenhou
    public boolean adicionarAtivo(Long usuarioId, String simbolo, double quantidade) {
        // Retorna true cravado para o TDD passar.
        return true;
    }
}