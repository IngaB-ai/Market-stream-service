package com.ingabak.integration.webhook_service.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.beans.factory.annotation.Value;

import com.ingabak.integration.webhook_service.model.Trade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.JsonNode;

@Slf4j
@Service
@RequiredArgsConstructor
public class MarketDataService {

    @Value("${yahoo.finance-url}")
    private String url;


    RestClient restClient = RestClient.create();

    public JsonNode getPrice(String symbol) {

        JsonNode data = restClient.get()
                .uri(url + symbol + "?interval=1m")
                .retrieve()
                .body(JsonNode.class);

        System.out.println("tree " + data);
        return data;
    }

    public void processTrade(Trade trade) {
        try {

            log.info("Trade received: {} price={}", trade.getSymbol(), trade.getPrice());

        } catch (Exception e) {
            log.error("Failed to process trade for {}: {}", trade.getSymbol(), e.getMessage(), e);
        }
    }

}