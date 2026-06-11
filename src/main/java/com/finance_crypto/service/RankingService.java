package com.finance_crypto.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance_crypto.dto.RankingAtivoDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RankingService {

    static final String COINGECKO_URL =
            "https://api.coingecko.com/api/v3/coins/markets" +
            "?vs_currency=brl" +
            "&order=market_cap_desc" +
            "&per_page=10" +
            "&page=1" +
            "&sparkline=true" +
            "&price_change_percentage=24h";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public RankingService(RestTemplate restTemplate) {
        this.restTemplate   = restTemplate;
        this.objectMapper   = new ObjectMapper();
    }

    public List<RankingAtivoDTO> obterRankingCalculado() {
        String json = restTemplate.getForObject(COINGECKO_URL, String.class);

        if (json == null || json.isBlank()) {
            return Collections.emptyList();
        }

        return parsearResposta(json);
    }

    public List<RankingAtivoDTO> obterAtivosComPrejuizo() {
        return obterRankingCalculado()
                .stream()
                .filter(ativo -> ativo.getPercentualLucro() < 0)
                .collect(Collectors.toList());
    }

    public double calcularPorcentagemLucro(double precoCompra, double precoAtual) {
        if (precoCompra == 0) return 0.0;
        return ((precoAtual - precoCompra) / precoCompra) * 100;
    }

    private List<RankingAtivoDTO> parsearResposta(String json) {
        List<RankingAtivoDTO> lista = new ArrayList<>();

        try {
            JsonNode array = objectMapper.readTree(json);

            if (!array.isArray()) {
                return Collections.emptyList();
            }

            for (JsonNode node : array) {
                String  simbolo        = node.path("symbol").asText("").toUpperCase();
                double  precoAtual     = node.path("current_price").asDouble(0.0);
                double  percentual24h  = node.path("price_change_percentage_24h").asDouble(0.0);

                double precoMedioCompra = 0.0;

                lista.add(new RankingAtivoDTO(
                    simbolo,
                    precoMedioCompra,
                    precoAtual,
                    percentual24h
                ));
            }

        } catch (JsonProcessingException e) {
            System.err.println("[RankingService] Erro ao parsear JSON da CoinGecko: "
                    + e.getMessage());
            return Collections.emptyList();
        }

        return lista;
    }
}