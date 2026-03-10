package com.ingabak.integration.webhook_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Field mapping:
 * s symbol (ticker, stock symbols, or stock ticker symbols, are unique
 * identifiers assigned to shares of companies that are publicly traded, as well
 * as to other securities)
 * p price (trade price)
 * v volume (number of shares traded)
 * t timestamp (Unix milliseconds)
 * c conditions (trade condition codes, can be null. Up to 41 different codes
 * that provide additional information about the trade, such as whether it was a
 * regular trade, an opening or closing trade, etc. For example, "1" indicates a
 * regular trade)
 */
@Data
public class Trade {

    @JsonProperty("s")
    private String symbol;

    @JsonProperty("p")
    private double price;

    @JsonProperty("v")
    private long volume;

    @JsonProperty("t")
    private long timestamp;

    @JsonProperty("c")
    private java.util.List<String> conditions;
}