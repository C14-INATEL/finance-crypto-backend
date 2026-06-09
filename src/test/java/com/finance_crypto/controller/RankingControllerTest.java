package com.finance_crypto.controller;

import com.finance_crypto.dto.RankingAtivoDTO;
import com.finance_crypto.service.RankingService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RankingControllerTest {

    @Test
    void deveRetornarAtivosComPrejuizoPeloController() {
        RankingService rankingService = mock(RankingService.class);
        RankingController controller = new RankingController(rankingService);

        List<RankingAtivoDTO> prejuizos = List.of(
                new RankingAtivoDTO("SOL", 500.0, 450.0, -10.0)
        );

        when(rankingService.obterAtivosComPrejuizo()).thenReturn(prejuizos);

        List<RankingAtivoDTO> response = controller.obterAtivosComPrejuizo();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("SOL", response.get(0).getSimbolo());
        verify(rankingService, times(1)).obterAtivosComPrejuizo();
    }

    @Test
    void deveRetornarRankingComSucessoQuandoServiceRetornarDados() {
        RankingService rankingService = mock(RankingService.class);
        RankingController controller = new RankingController(rankingService);

        List<RankingAtivoDTO> mockLista = List.of(
                new RankingAtivoDTO("BTC", 300000.0, 350000.0, 16.6)
        );

        when(rankingService.obterRankingCalculado()).thenReturn(mockLista);

        List<RankingAtivoDTO> response = controller.obterRanking();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("BTC", response.get(0).getSimbolo());
        verify(rankingService, times(1)).obterRankingCalculado();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverAtivosNoService() {
        RankingService rankingService = mock(RankingService.class);
        RankingController controller = new RankingController(rankingService);

        when(rankingService.obterRankingCalculado()).thenReturn(List.of());

        List<RankingAtivoDTO> response = controller.obterRanking();

        assertNotNull(response);
        assertTrue(response.isEmpty());
        verify(rankingService, times(1)).obterRankingCalculado();
    }

    @Test
    void deveLancarExcecaoQuandoServicoDePrejuizoFalhar() {
        RankingService rankingService = mock(RankingService.class);
        RankingController controller = new RankingController(rankingService);

        when(rankingService.obterAtivosComPrejuizo()).thenThrow(new RuntimeException("Erro interno no servidor"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            controller.obterAtivosComPrejuizo();
        });

        assertEquals("Erro interno no servidor", exception.getMessage());
        verify(rankingService, times(1)).obterAtivosComPrejuizo();
    }

    @Test
    void deveLancarExcecaoQuandoServicoDeRankingFalhar() {
        RankingService rankingService = mock(RankingService.class);
        RankingController controller = new RankingController(rankingService);

        when(rankingService.obterRankingCalculado()).thenThrow(new RuntimeException("Falha ao calcular ranking"));

        assertThrows(RuntimeException.class, () -> {
            controller.obterRanking();
        });

        verify(rankingService, times(1)).obterRankingCalculado();
    }
}
