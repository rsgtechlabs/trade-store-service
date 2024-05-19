package com.rsgtechlabs.tradestore.service.impl;

import com.rsgtechlabs.tradestore.dto.TradeGetRequest;
import com.rsgtechlabs.tradestore.dto.TradeGetResponse;
import com.rsgtechlabs.tradestore.dto.TradeStoreResponse;
import com.rsgtechlabs.tradestore.mapper.TradeMapper;
import com.rsgtechlabs.tradestore.model.Trade;
import com.rsgtechlabs.tradestore.model.TradeIdentifier;
import com.rsgtechlabs.tradestore.rule.RuleResult;
import com.rsgtechlabs.tradestore.rule.TradeMaturityRule;
import com.rsgtechlabs.tradestore.rule.TradeStoreRuleService;
import com.rsgtechlabs.tradestore.rule.TradeVersionRule;
import com.rsgtechlabs.tradestore.service.TradeStoreService;
import com.rsgtechlabs.tradestore.store.Store;
import com.rsgtechlabs.tradestore.store.TradeStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class TradeStoreServiceImplTest {

    private ArgumentCaptor<TradeIdentifier> tradeIdentifierArgumentCaptor = ArgumentCaptor.forClass(TradeIdentifier.class);
    private ArgumentCaptor<Trade> tradeArgumentCaptor = ArgumentCaptor.forClass(Trade.class);

    private Store<TradeIdentifier, Trade> tradeStore;
    private TradeStoreRuleService tradeStoreRuleService;
    private TradeMapper tradeMapper;
    private TradeStoreService tradeStoreService;
    private TradeMaturityRule tradeMaturityRule;
    private TradeVersionRule tradeVersionRule;

    @BeforeEach
    public void setup() {
        this.tradeStore = mock(TradeStore.class);
        this.tradeStoreRuleService = mock(TradeStoreRuleService.class);
        this.tradeMapper = new TradeMapper();
        this.tradeStoreService = new TradeStoreServiceImpl(tradeStore, tradeStoreRuleService, tradeMapper);
        this.tradeMaturityRule = mock(TradeMaturityRule.class);
        this.tradeVersionRule = mock(TradeVersionRule.class);
    }

    @Test
    public void whenTradeMaturityRuleIsFiled_store_tradeShouldNotStore() {
        //given
        when(tradeStoreRuleService.getRules()).thenReturn(Arrays.asList(tradeMaturityRule, tradeVersionRule));
        when(tradeMaturityRule.apply(any())).thenReturn(new RuleResult("TradeMaturityRule", false, ""));
        when(tradeVersionRule.apply(any())).thenReturn(new RuleResult("TradeVersionRule", true, ""));

        Trade t1 = new Trade("T1", 1, "CP-1", "B1",
                LocalDate.of(2020, 5, 20),
                LocalDate.now(), "N");

        //when
        TradeStoreResponse tradeStoreResponse = tradeStoreService.store(t1);

        //then
        verify(tradeStore, times(0)).add(tradeIdentifierArgumentCaptor.capture(), tradeArgumentCaptor.capture());
        assertThat(tradeStoreResponse.isStored()).isFalse();
    }

    @Test
    public void whenTradeVersionRuleIsFiled_store_tradeShouldNotStore() {
        //given
        when(tradeStoreRuleService.getRules()).thenReturn(Arrays.asList(tradeMaturityRule, tradeVersionRule));
        when(tradeMaturityRule.apply(any())).thenReturn(new RuleResult("TradeMaturityRule", true, ""));
        when(tradeVersionRule.apply(any())).thenReturn(new RuleResult("TradeVersionRule", false, ""));

        Trade t1 = new Trade("T1", 1, "CP-1", "B1",
                LocalDate.of(2020, 5, 20),
                LocalDate.now(), "N");

        //when
        TradeStoreResponse tradeStoreResponse = tradeStoreService.store(t1);

        //then
        verify(tradeStore, times(0)).add(tradeIdentifierArgumentCaptor.capture(), tradeArgumentCaptor.capture());
        assertThat(tradeStoreResponse.isStored()).isFalse();
    }

    @Test
    public void whenBothRuleIsFiled_store_tradeShouldNotStore() {
        //given
        when(tradeStoreRuleService.getRules()).thenReturn(Arrays.asList(tradeMaturityRule, tradeVersionRule));
        when(tradeMaturityRule.apply(any())).thenReturn(new RuleResult("TradeMaturityRule", false, ""));
        when(tradeVersionRule.apply(any())).thenReturn(new RuleResult("TradeVersionRule", false, ""));

        Trade t1 = new Trade("T1", 1, "CP-1", "B1",
                LocalDate.of(2020, 5, 20),
                LocalDate.now(), "N");

        //when
        TradeStoreResponse tradeStoreResponse = tradeStoreService.store(t1);

        //then
        verify(tradeStore, times(0)).add(tradeIdentifierArgumentCaptor.capture(), tradeArgumentCaptor.capture());
        assertThat(tradeStoreResponse.isStored()).isFalse();
    }

    @Test
    public void whenBothRuleIsSuccess_store_tradeShouldStore() {
        //given
        when(tradeStoreRuleService.getRules()).thenReturn(Arrays.asList(tradeMaturityRule, tradeVersionRule));
        when(tradeMaturityRule.apply(any())).thenReturn(new RuleResult("TradeMaturityRule", true, ""));
        when(tradeVersionRule.apply(any())).thenReturn(new RuleResult("TradeVersionRule", true, ""));

        Trade t1 = new Trade("T1", 1, "CP-1", "B1",
                LocalDate.of(2020, 5, 20),
                LocalDate.now(), "N");

        //when
        TradeStoreResponse tradeStoreResponse = tradeStoreService.store(t1);

        //then
        verify(tradeStore, times(1)).add(tradeIdentifierArgumentCaptor.capture(), tradeArgumentCaptor.capture());
        assertThat(tradeStoreResponse.isStored()).isTrue();
    }

    @Test
    public void whenStoreEmptyList_readTrade_shouldReturnEmptyTradeList() {
        //given
        when(tradeStoreRuleService.getRules()).thenReturn(Arrays.asList(tradeMaturityRule, tradeVersionRule));
        when(tradeMaturityRule.apply(any())).thenReturn(new RuleResult("TradeMaturityRule", true, ""));
        when(tradeVersionRule.apply(any())).thenReturn(new RuleResult("TradeVersionRule", true, ""));

        when(tradeStore.get(any())).thenReturn(List.of());

        //when
        TradeGetResponse tradeGetResponse = tradeStoreService.get(new TradeGetRequest("T1", 2));

        //then
        assertThat(tradeGetResponse.success()).isFalse();
    }

    @Test
    public void whenStoreHasOneTrade_readTrade_shouldReturnOneTrade() {
        //given
        when(tradeStoreRuleService.getRules()).thenReturn(Arrays.asList(tradeMaturityRule, tradeVersionRule));
        when(tradeMaturityRule.apply(any())).thenReturn(new RuleResult("TradeMaturityRule", true, ""));
        when(tradeVersionRule.apply(any())).thenReturn(new RuleResult("TradeVersionRule", true, ""));

        Trade t1 = new Trade("T1", 1, "CP-1", "B1",
                LocalDate.of(2020, 5, 20),
                LocalDate.now(), "N");
        when(tradeStore.get(any())).thenReturn(List.of(t1));

        //when
        TradeGetResponse tradeGetResponse = tradeStoreService.get(new TradeGetRequest("T1", 2));

        //then
        assertThat(tradeGetResponse.success()).isTrue();
        assertThat(tradeGetResponse.trades().size()).isEqualTo(1);
        assertThat(tradeGetResponse.trades().get(0).tradeId()).isEqualTo("T1");
    }
}
