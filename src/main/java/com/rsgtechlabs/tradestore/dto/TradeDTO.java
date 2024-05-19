package com.rsgtechlabs.tradestore.dto;
public record TradeDTO(String tradeId, int version, String counterPartyId, String bookId, String maturityDate,
                       String createdDate, String expired) {
}
