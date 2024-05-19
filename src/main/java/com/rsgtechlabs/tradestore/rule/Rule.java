package com.rsgtechlabs.tradestore.rule;

import com.rsgtechlabs.tradestore.model.Trade;

public interface Rule {

    String getId();
    RuleResult apply(final Trade trade);
}
