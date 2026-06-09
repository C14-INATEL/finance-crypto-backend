package com.finance_crypto.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finance_crypto.dto.RankingAtivoDTO;
import com.finance_crypto.service.RankingService;
import com.finance_crypto.dto.RankingAtivoDTO;
import com.finance_crypto.service.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ranking")
public class RankingController {

    private final RankingService rankingService;

    public RankingController(RankingService rankingService) {
        this.rankingService = rankingService;
    }
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
}
