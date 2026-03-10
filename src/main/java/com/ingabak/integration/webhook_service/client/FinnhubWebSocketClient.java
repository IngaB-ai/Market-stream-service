package com.ingabak.integration.webhook_service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.ingabak.integration.webhook_service.model.Trade;
import com.ingabak.integration.webhook_service.service.MarketDataService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class FinnhubWebSocketClient extends TextWebSocketHandler {

    @Value("${finnhub.api-key}")
    private String apiKey;

    @Value("${finnhub.symbols}")
    private String[] symbols;

    private final MarketDataService marketDataService;
    private final ObjectMapper objectMapper;

    @EventListener(ApplicationReadyEvent.class)
    public void connectAndSubscribe() {
        String url = "wss://ws.finnhub.io?token=" + apiKey;
        log.info("Connecting to Finnhub WebSocket...");
        new StandardWebSocketClient().execute(this, url);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Finnhub WebSocket connected. Subscribing to stocks...");
        for (String symbol : symbols) {
            String msg = String.format(
                    "{\"type\":\"subscribe\",\"symbol\":\"%s\"}", symbol.trim().toUpperCase());
            session.sendMessage(new TextMessage(msg));
            log.info("Subscribed to: {}", symbol);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        log.info("Received: {}", message);
        String payload = message.getPayload();

        JsonNode parsed = objectMapper.readTree(payload);
        String type = parsed.path("type").asString();

        switch (type) {
            case "trade" -> handleTradeMessage(parsed);
            case "ping" -> log.info("Finnhub ping received");
            default -> log.info("Unhandled message type: {}", type);
        }
    }

    private void handleTradeMessage(JsonNode parsed) {
        JsonNode dataArray = parsed.path("data");
        if (!dataArray.isArray())
            return;

        for (JsonNode tradeNode : dataArray) {
            try {
                Trade trade = objectMapper.treeToValue(tradeNode, Trade.class);
                log.info("Trade: {} price={}", trade.getSymbol(), trade.getPrice());
                marketDataService.processTrade(trade);
            } catch (Exception e) {
                log.error("Failed to parse trade: {}", tradeNode, e);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        log.info("Finnhub WebSocket closed: {}", status);
    }
}