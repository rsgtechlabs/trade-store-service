package com.rsgtechlabs.tradestore.rule;

import com.rsgtechlabs.tradestore.model.Trade;
import com.rsgtechlabs.tradestore.model.TradeIdentifier;
import com.rsgtechlabs.tradestore.rule.Rule;
import com.rsgtechlabs.tradestore.rule.RuleResult;
import com.rsgtechlabs.tradestore.rule.TradeVersionRule;
import com.rsgtechlabs.tradestore.store.TradeStore;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class TradeVersionRuleTest {

    @Test
    public void whenTradeVersionDoesNotExist_apply_ruleShouldBeSuccess() {

        TradeStore tradeStore = Mockito.mock(TradeStore.class);
        final Rule rule = new TradeVersionRule(tradeStore);

        //given
        Trade t1 =new Trade("T1", 1, "CP-1", "B1",
                LocalDate.of(2020, 5, 20),
                LocalDate.now(), "N");

        //when
        Mockito.when(tradeStore.getLatestVersion(Mockito.any(TradeIdentifier.class))).thenReturn(-1);
        RuleResult ruleResult = rule.apply(t1);

        //then
        assertThat(ruleResult.success()).isTrue();
        assertThat(ruleResult.message()).isEqualTo("TradeVersionRule is passed for trade id T1");
    }

    @Test
    public void whenTradeLowerTradeVersionExistInStore_apply_ruleShouldBeSuccess() {

        TradeStore tradeStore = Mockito.mock(TradeStore.class);
        final Rule rule = new TradeVersionRule(tradeStore);

        //given
        Trade t1 =new Trade("T1", 2, "CP-1", "B1",
                LocalDate.of(2020, 5, 20),
                LocalDate.now(), "N");

        //when
        Mockito.when(tradeStore.getLatestVersion(Mockito.any(TradeIdentifier.class))).thenReturn(1);
        RuleResult ruleResult = rule.apply(t1);
        System.out.println(ruleResult);
        //then
        assertThat(ruleResult.success()).isTrue();
        assertThat(ruleResult.message()).isEqualTo("TradeVersionRule is passed for trade id T1");
    }

    @Test
    public void whenTradeHigherTradeVersionExistInStore_apply_ruleShouldBeFail() {

        TradeStore tradeStore = Mockito.mock(TradeStore.class);
        final Rule rule = new TradeVersionRule(tradeStore);

        //given
        Trade t1 =new Trade("T1", 1, "CP-1", "B1",
                LocalDate.of(2020, 5, 20),
                LocalDate.now(), "N");

        //when
        Mockito.when(tradeStore.getLatestVersion(Mockito.any(TradeIdentifier.class))).thenReturn(2);
        RuleResult ruleResult = rule.apply(t1);

        //then
        assertThat(ruleResult.success()).isFalse();
        assertThat(ruleResult.message()).isEqualTo("TradeVersionRule failed for trade id 'T1' as new version 1 is older then stored version 2");
    }

    @Test
    public void testRuleName() {
        final Rule rule = new TradeVersionRule(null);
        assertThat(rule.getId()).isEqualTo(TradeVersionRule.RULE_ID);
    }
}
