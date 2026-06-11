package com.finance_crypto.controller;

import com.finance_crypto.dto.RankingAtivoDTO;
import com.finance_crypto.service.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ranking")
public class RankingController {

    @Autowired
    private RankingService rankingService;

    @GetMapping("/lucrativos")
    public List<RankingAtivoDTO> obterRanking() {
        return rankingService.obterRankingCalculado();
    }

    @GetMapping("/prejuizos")
    public List<RankingAtivoDTO> obterAtivosComPrejuizo() {
        return rankingService.obterAtivosComPrejuizo();
    }
}