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

    // -----------------------------------------------------------------
    // URL completa da API — static para ser referenciável nos testes
    // com eq(RankingService.COINGECKO_URL) se necessário no futuro.
    // -----------------------------------------------------------------
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

    // -----------------------------------------------------------------
    // Construtor principal — o @InjectMocks do Mockito usa este para
    // injetar o RestTemplate mockado sem precisar de contexto Spring.
    // -----------------------------------------------------------------
    public RankingService(RestTemplate restTemplate) {
        this.restTemplate   = restTemplate;
        this.objectMapper   = new ObjectMapper();
    }

    // =================================================================
    // MÉTODO REFATORADO — remove hardcode, passa a consumir a API real
    // =================================================================

    public List<RankingAtivoDTO> obterRankingCalculado() {
        String json = restTemplate.getForObject(COINGECKO_URL, String.class);

        // Comportamento defensivo: API retornou nulo ou vazio
        if (json == null || json.isBlank()) {
            return Collections.emptyList();
        }

        return parsearResposta(json);
    }

    // =================================================================
    // MÉTODOS ORIGINAIS — preservados intactos
    // =================================================================

    public List<RankingAtivoDTO> obterAtivosComPrejuizo() {
        return obterRankingCalculado()
                .stream()
                .filter(ativo -> ativo.getPercentualLucro() < 0)
                .collect(Collectors.toList());
    }

    /**
     * Calcula variação percentual entre preço de compra e preço atual.
     *
     * Assinatura confirmada pelos testes:
     *   calcularPorcentagemLucro(100.0, 150.0) → 50.0   (lucro)
     *   calcularPorcentagemLucro(100.0, 80.0)  → -20.0  (prejuízo)
     *   calcularPorcentagemLucro(0.0,   150.0) →  0.0   (guarda divisão por zero)
     */
    public double calcularPorcentagemLucro(double precoCompra, double precoAtual) {
        if (precoCompra == 0) return 0.0;
        return ((precoAtual - precoCompra) / precoCompra) * 100;
    }

    // =================================================================
    // PARSE PRIVADO — converte JSON do CoinGecko em List<RankingAtivoDTO>
    // =================================================================

    /**
     * Mapeamento de campos (JSON CoinGecko → RankingAtivoDTO):
     *
     *   symbol                       → simbolo          (forçado uppercase)
     *   current_price                → precoAtual
     *   price_change_percentage_24h  → percentualLucro
     *   precoMedioCompra             → 0.0 (não existe na API; campo de carteira)
     */
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

                // precoMedioCompra não existe na API do CoinGecko — é um dado
                // de carteira do usuário. Inicializado como 0.0 aqui; quando
                // a feature de carteira for implementada, este valor virá do
                // repositório do usuário e sobrescreverá este padrão.
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