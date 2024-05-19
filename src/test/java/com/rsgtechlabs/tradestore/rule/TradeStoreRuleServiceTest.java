package com.rsgtechlabs.tradestore.rule;

import com.rsgtechlabs.tradestore.rule.Rule;
import com.rsgtechlabs.tradestore.rule.TradeMaturityRule;
import com.rsgtechlabs.tradestore.rule.TradeStoreRuleService;
import com.rsgtechlabs.tradestore.rule.TradeVersionRule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TradeStoreRuleServiceTest {

    @Autowired
    private TradeStoreRuleService tradeStoreRuleService;

    @Test
    public void whenBeanIsInitialize_loadRule_shouldLoadAllRule() {
        List<Rule> rules = tradeStoreRuleService.getRules();
        assertThat(rules.size()).isEqualTo(2);
        assertThat(rules.get(0).getId()).isEqualTo(TradeVersionRule.RULE_ID);
        assertThat(rules.get(1).getId()).isEqualTo(TradeMaturityRule.RULE_ID);

    }

}
