package com.finance_crypto.service;

import com.finance_crypto.dto.RankingAtivoDTO;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class RankingService {

    public List<RankingAtivoDTO> obterRankingCalculado() {
        // Proximo passo: Implementar a lógica real de cálculo do ranking com base nos dados do banco de dados.

        // Dados mockados para teste
        return Arrays.asList(
                new RankingAtivoDTO("BTC", 300000.0, 350000.0, 16.6),
                new RankingAtivoDTO("ETH", 15000.0, 18000.0, 20.0),
                new RankingAtivoDTO("SOL", 500.0, 450.0, -10.0)
        );
    }

    public List<RankingAtivoDTO> obterAtivosComPrejuizo() {
        return obterRankingCalculado().stream()
                .filter(ativo -> ativo.getPercentualLucro() < 0)
                .toList();
    }

    public double calcularPorcentagemLucro(double precoMedioCompra, double precoAtual) {
        if (precoMedioCompra <= 0) {
            return 0.0;
        }

        return ((precoAtual - precoMedioCompra) / precoMedioCompra) * 100.0;
    }
}