package com.rsgtechlabs.tradestore.rule;

import com.rsgtechlabs.tradestore.model.Trade;
import com.rsgtechlabs.tradestore.model.TradeIdentifier;
import com.rsgtechlabs.tradestore.store.Store;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
// Applies all the rules before saving the trade
// Rules can be externalized to some DB
//@TODO - Add active/inactive flag for the rules, probably save it in a map or some location
// This service can be exposed as rest endpoint for users to add, remove rules

@Component
public class TradeStoreRuleService {
    private List<Rule> rules;
    @Autowired
    private Store<TradeIdentifier, Trade> tradeStore;

    @PostConstruct
    public void loadRule() {
        rules = new ArrayList<>();

        TradeVersionRule tradeVersionRule = new TradeVersionRule(tradeStore);
        rules.add(tradeVersionRule);

        TradeMaturityRule tradeMaturityRule = new TradeMaturityRule();
        rules.add(tradeMaturityRule);
    }

    public List<Rule> getRules() {
        return rules;
    }
}
