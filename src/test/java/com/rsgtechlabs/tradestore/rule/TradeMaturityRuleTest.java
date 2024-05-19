package com.rsgtechlabs.tradestore.rule;

import com.rsgtechlabs.tradestore.model.Trade;
import com.rsgtechlabs.tradestore.rule.Rule;
import com.rsgtechlabs.tradestore.rule.RuleResult;
import com.rsgtechlabs.tradestore.rule.TradeMaturityRule;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;

public class TradeMaturityRuleTest {

    @Test
    public void whenTradeMaturityBeforeCurrentDate_apply_ruleShouldBeFailed() {
        final Rule rule = new TradeMaturityRule();

        //given
        Trade t1 =new Trade("T1", 1, "CP-1", "B1",
                LocalDate.of(2020, 5, 20),
                LocalDate.now(), "N");

        //when
        RuleResult ruleResult = rule.apply(t1);

        //then
        assertThat(ruleResult.success()).isFalse();
    }

    @Test
    public void whenTradeMaturityEqualCurrentDate_apply_ruleShouldBeSuccess() {
        final Rule rule = new TradeMaturityRule();

        //given
        Trade t1 =new Trade("T1", 1, "CP-1", "B1",
                LocalDate.now(),
                LocalDate.now(), "N");

        //when
        RuleResult ruleResult = rule.apply(t1);

        //then
        assertThat(ruleResult.success()).isTrue();
    }

    @Test
    public void whenTradeMaturityAfterCurrentDate_apply_ruleShouldBeSuccess() {
        final Rule rule = new TradeMaturityRule();

        //given
        Trade t1 =new Trade("T1", 1, "CP-1", "B1",
                LocalDate.now().plusDays(1),
                LocalDate.now(), "N");

        //when
        RuleResult ruleResult = rule.apply(t1);

        //then
        assertThat(ruleResult.success()).isTrue();
    }

    @Test
    public void testRuleName() {
        final Rule rule = new TradeMaturityRule();
        assertThat(rule.getId()).isEqualTo(TradeMaturityRule.RULE_ID);
    }
}
