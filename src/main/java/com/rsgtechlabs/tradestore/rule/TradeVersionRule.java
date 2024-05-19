package com.rsgtechlabs.tradestore.rule;

import com.rsgtechlabs.tradestore.model.Trade;
import com.rsgtechlabs.tradestore.model.TradeIdentifier;
import com.rsgtechlabs.tradestore.store.Store;

//Rule to check if the version for the trade to reject older versions
public class TradeVersionRule implements Rule {
    public static final String RULE_ID = "TradeVersionRule";

    private final Store<TradeIdentifier, Trade> tradeStore;

    public TradeVersionRule(final Store<TradeIdentifier, Trade> tradeStore) {
        this.tradeStore = tradeStore;
    }

    @Override
    public String getId() {
        return RULE_ID;
    }

    @Override
    public RuleResult apply(final Trade trade) {
        boolean result = true;
        String message = "TradeVersionRule is passed for trade id " + trade.tradeId();
        int storedLatestVersion = tradeStore.getLatestVersion(new TradeIdentifier(trade.tradeId(), trade.version()));
        if (storedLatestVersion > trade.version()) {
            result = false;
            message = "TradeVersionRule failed for trade id '" + trade.tradeId() + "' as new version " + trade.version() + " is older then stored version " + storedLatestVersion;
        }

        return new RuleResult(RULE_ID, result, message);
    }
}
