package com.finance_crypto.controller;

import com.finance_crypto.dto.RankingAtivoDTO;
import com.finance_crypto.service.RankingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/ranking")
public class RankingController {

    private final RankingService rankingService;

    // O Spring injeta o Service automaticamente aqui
    public RankingController(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @GetMapping("/lucrativos")
    public List<RankingAtivoDTO> obterRanking() {
        return rankingService.obterRankingCalculado();
    }
}