package com.ingabak.integration.webhook_service.service;

import org.springframework.stereotype.Service;

import com.ingabak.integration.webhook_service.model.Trade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class MarketDataService {

    public void processTrade(Trade trade) {
        try {

             log.info("Trade received: {} price={}", trade.getSymbol(), trade.getPrice());

        } catch (Exception e) {
            log.error("Failed to process trade for {}: {}", trade.getSymbol(), e.getMessage(), e);
        }
    }

}