package com.rsgtechlabs.tradestore.dto;
import com.fasterxml.jackson.annotation.JsonProperty;

public record TradeDTO(@JsonProperty(required = true) String tradeId,
                       @JsonProperty(required = true) int version,
                       @JsonProperty(required = true) String counterPartyId,
                       @JsonProperty(required = true) String bookId,
                       @JsonProperty(required = true) String maturityDate,
                       @JsonProperty(required = true) String createdDate,
                       @JsonProperty(required = true) String expired) {
}