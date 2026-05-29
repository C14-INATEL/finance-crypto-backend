package com.finance_crypto.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finance_crypto.dto.RankingAtivoDTO;
import com.finance_crypto.service.RankingService;

@RestController
@RequestMapping("/api/ranking")
public class RankingController {

    private final RankingService rankingService;

    public RankingController(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @GetMapping("/lucrativos")
    public List<RankingAtivoDTO> obterRanking() {
        return rankingService.obterRankingCalculado();
    }

    @GetMapping("/prejuizos")
    public List<RankingAtivoDTO> obterAtivosComPrejuizo() {
        return rankingService.obterAtivosComPrejuizo();
    }
}
