package com.rsgtechlabs.tradestore.rule;

import com.rsgtechlabs.tradestore.model.Trade;

import java.time.LocalDate;

//Rule to disallow trades with older maturity date
public class TradeMaturityRule implements Rule {
    public static final String RULE_ID = "TradeMaturityRule";

    @Override
    public String getId() {
        return RULE_ID;
    }

    @Override
    public RuleResult apply(final Trade trade) {
        LocalDate now = LocalDate.now();
        boolean result = true;
        String message = "TradeMaturityRule is passed for trade id "+ trade.tradeId();
        if(trade.maturityDate().isBefore(now)) {
            result = false;
            message =  "TradeMaturityRule is failed for trade id '"+trade.tradeId()+"' as maturity date "+  trade.maturityDate() + " is before today "+ now;
        }
        return new RuleResult(RULE_ID, result, message);
    }
}
