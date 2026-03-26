package com.ingabak.integration.webhook_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ingabak.integration.webhook_service.service.MarketDataService;

import tools.jackson.databind.JsonNode;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    private final MarketDataService stockService;

    public StockController(MarketDataService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/{symbol}")

    public JsonNode getStockPrice(@PathVariable String symbol) throws Exception {

        return stockService.getPrice(symbol);

    }
}