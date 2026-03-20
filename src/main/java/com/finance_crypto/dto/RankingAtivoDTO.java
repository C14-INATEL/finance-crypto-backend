package com.finance_crypto.dto;

public class RankingAtivoDTO {
    private String simbolo; // ex: BTC, ETH
    private double precoMedioCompra;
    private double precoAtual;
    private double percentualLucro;

    // Construtor
    public RankingAtivoDTO(String simbolo, double precoMedioCompra, double precoAtual, double percentualLucro) {
        this.simbolo = simbolo;
        this.precoMedioCompra = precoMedioCompra;
        this.precoAtual = precoAtual;
        this.percentualLucro = percentualLucro;
    }

    // Getters
    public String getSimbolo() { return simbolo; }
    public double getPrecoMedioCompra() { return precoMedioCompra; }
    public double getPrecoAtual() { return precoAtual; }
    public double getPercentualLucro() { return percentualLucro; }
}