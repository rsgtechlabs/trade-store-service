package com.rsgtechlabs.tradestore.service.impl;

import com.rsgtechlabs.tradestore.dto.TradeGetRequest;
import com.rsgtechlabs.tradestore.dto.TradeGetResponse;
import com.rsgtechlabs.tradestore.dto.TradeStoreResponse;
import com.rsgtechlabs.tradestore.mapper.TradeMapper;
import com.rsgtechlabs.tradestore.model.Trade;
import com.rsgtechlabs.tradestore.model.TradeIdentifier;
import com.rsgtechlabs.tradestore.rule.*;
import com.rsgtechlabs.tradestore.rule.RuleResult;
import com.rsgtechlabs.tradestore.rule.TradeStoreRuleService;
import com.rsgtechlabs.tradestore.service.TradeStoreService;
import com.rsgtechlabs.tradestore.store.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TradeStoreServiceImpl implements TradeStoreService {
    private static final Logger logger = LoggerFactory.getLogger(TradeStoreServiceImpl.class);

    //@TODO Can be externalized to file
    public static final String TRADE_SUCCESSFULLY_STORED = "Trade successfully stored";

    @Autowired
    private TradeStoreRuleService tradeStoreRuleService;
    @Autowired
    private Store<TradeIdentifier, Trade> tradeStore;
    @Autowired
    private TradeMapper tradeMapper;

    public TradeStoreServiceImpl() {
    }

    public List<TradeStoreResponse> store(final List<Trade> trades) {
        logger.info("store - enter for trades {}", trades.size());
        List<TradeStoreResponse> result = trades.stream().map(trade -> store(trade)).collect(Collectors.toList());
        logger.info("store - leave with result {}", result.size());
        return result;
    }

    @Override
    public TradeGetResponse get(final TradeGetRequest request) {
        logger.info("readTrade - enter , radeGetRequest {}", request);
        if(Objects.isNull(request)) {
            throw new IllegalArgumentException("Throwing exception as empty request is not allowed");
        }
        final List<Trade> trades = tradeStore.get(new TradeIdentifier(request.tradeId(), request.version()));
        TradeGetResponse tradeGetResponse = new TradeGetResponse(trades.size() > 0, "", tradeMapper.toDto(trades));

        logger.info("readTrade - enter , with tradeGetResponse {}", tradeGetResponse);
        return tradeGetResponse;
    }

    public TradeStoreResponse store(final Trade trade) {
        logger.info("store - enter , for trade {}", trade);
        boolean isStored = false;
        String message = TRADE_SUCCESSFULLY_STORED;

        //Run all rules, even if one rule fails returns error
        Optional<RuleResult> failedRuleOptional = tradeStoreRuleService.getRules().stream().map(rule -> rule.apply(trade)).filter(ruleResult -> !ruleResult.success()).findAny();
        if(failedRuleOptional.isEmpty()) {
            tradeStore.add(new TradeIdentifier(trade.tradeId(), trade.version()), trade);
            isStored = true;
        } else {
            message = "Trade '"+trade.tradeId()+"' is not stored as validation rule failed "+failedRuleOptional.get();
        }
        TradeStoreResponse response = new TradeStoreResponse(trade.tradeId(), isStored, message);
        logger.info("store - leave , with response {}", response);
        return response;
    }
}
