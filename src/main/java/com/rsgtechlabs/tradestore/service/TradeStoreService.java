package com.rsgtechlabs.tradestore.service;

import com.rsgtechlabs.tradestore.dto.TradeGetRequest;
import com.rsgtechlabs.tradestore.dto.TradeGetResponse;
import com.rsgtechlabs.tradestore.dto.TradeStoreResponse;
import com.rsgtechlabs.tradestore.model.Trade;

import java.util.List;

public interface TradeStoreService {
    TradeStoreResponse store(final Trade trade);
    List<TradeStoreResponse> store(final List<Trade> trades);
    TradeGetResponse get(final TradeGetRequest request);
}
