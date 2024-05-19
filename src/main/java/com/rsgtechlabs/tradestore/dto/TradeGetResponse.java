package com.rsgtechlabs.tradestore.dto;

import java.util.List;

public record TradeGetResponse(boolean success , String message, List<TradeDTO> trades) {
}
