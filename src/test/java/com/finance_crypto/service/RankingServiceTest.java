package com.finance_crypto.service;

import com.finance_crypto.dto.RankingAtivoDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RankingServiceTest {

    private final RankingService rankingService = new RankingService();

    @Test
    void deveCalcularLucroCorretamenteQuandoPrecoAtualForMaior() {
        double precoCompra = 100.0;
        double precoAtual = 150.0;

        double resultado = rankingService.calcularPorcentagemLucro(precoCompra, precoAtual);

        assertEquals(50.0, resultado, "O lucro deveria ser de 50%");
    }

    @Test
    void deveCalcularPrejuizoCorretamenteQuandoPrecoAtualForMenor() {
        double precoCompra = 100.0;
        double precoAtual = 80.0;

        double resultado = rankingService.calcularPorcentagemLucro(precoCompra, precoAtual);

        assertEquals(-20.0, resultado, "O prejuízo deveria ser de -20%");
    }

    @Test
    void deveRetornarZeroQuandoPrecoCompraForInvalido() {
        double precoCompra = 0.0;
        double precoAtual = 150.0;

        double resultado = rankingService.calcularPorcentagemLucro(precoCompra, precoAtual);

        assertEquals(0.0, resultado, "Deve retornar 0 para evitar divisão por zero");
    }

    @Test
    void deveRetornarListaDeRankingPreenchida() {
        List<RankingAtivoDTO> ranking = rankingService.obterRankingCalculado();

        assertNotNull(ranking, "A lista de ranking não pode ser nula");
        assertFalse(ranking.isEmpty(), "A lista de ranking não deve estar vazia");
    }

    @Test
    void deveRetornarTresAtivosNoRanking() {
        List<RankingAtivoDTO> ranking = rankingService.obterRankingCalculado();

        assertNotNull(ranking);
        assertEquals(3, ranking.size());
    }

    @Test
    void deveRetornarBtcComoPrimeiroAtivo() {
        List<RankingAtivoDTO> ranking = rankingService.obterRankingCalculado();

        assertEquals("BTC", ranking.get(0).getSimbolo());
        assertEquals(16.6, ranking.get(0).getPercentualLucro(), 0.001);
    }

    @Test
    void deveConterAtivoComPrejuizo() {
        List<RankingAtivoDTO> ranking = rankingService.obterRankingCalculado();

        boolean existePrejuizo = ranking.stream()
                .anyMatch(ativo -> ativo.getPercentualLucro() < 0);

        assertTrue(existePrejuizo);
    }

    @Test
    void deveRetornarSomenteAtivosComPrejuizo() {
        List<RankingAtivoDTO> ativosComPrejuizo = rankingService.obterAtivosComPrejuizo();

        assertNotNull(ativosComPrejuizo);
        assertEquals(1, ativosComPrejuizo.size());
        assertEquals("SOL", ativosComPrejuizo.get(0).getSimbolo());
        assertEquals(-10.0, ativosComPrejuizo.get(0).getPercentualLucro(), 0.001);
    }
}