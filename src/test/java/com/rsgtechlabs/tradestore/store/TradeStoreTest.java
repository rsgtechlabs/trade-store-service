package com.rsgtechlabs.tradestore.store;

import com.rsgtechlabs.tradestore.model.Trade;
import com.rsgtechlabs.tradestore.model.TradeIdentifier;
import com.rsgtechlabs.tradestore.store.TradeStore;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class TradeStoreTest {

    @Test
    public void whenStoreIsEmpty_get_shouldReturnEmptyMap() {
        TradeStore store = new TradeStore();

        Map<TradeIdentifier, Trade> tradeIdentifierTradeMap = store.get();

        assertThat(tradeIdentifierTradeMap.size()).isEqualTo(0);
    }

    @Test
    public void whenStoreIsEmpty_getById_shouldReturnEmptyList() {
        TradeStore store = new TradeStore();

        List<Trade> trades = store.get(new TradeIdentifier("t1", 1));

        assertThat(trades.size()).isEqualTo(0);
    }

    @Test
    public void whenStoreIsEmpty_getLatestVersion_shouldReturnNegativeOne() {
        TradeStore store = new TradeStore();

        int latestVersion = store.getLatestVersion(new TradeIdentifier("t1", 1));

        assertThat(latestVersion).isEqualTo(-1);
    }

    @Test
    public void whenStoreAddOneTrade_get_shouldReturnOneTrade() {
        TradeStore store = new TradeStore();

        //given
        Trade t1 =new Trade("T1", 1, "CP-1", "B1",
                LocalDate.of(2020, 5, 20),
                LocalDate.now(), "N");

        //when
        store.add(new TradeIdentifier(t1.tradeId(), t1.version()), t1);
        Map<TradeIdentifier, Trade> tradeIdentifierTradeMap = store.get();

        //then
        assertThat(tradeIdentifierTradeMap.size()).isEqualTo(1);
    }

    @Test
    public void whenStoreAddTwoTrade_get_shouldReturnTwoTrades() {
        TradeStore store = new TradeStore();

        //given
        Trade t1 =new Trade("T1", 1, "CP-1", "B1",
                LocalDate.of(2020, 5, 20),
                LocalDate.now(), "N");

        Trade t2 =new Trade("T2", 1, "CP-1", "B1",
                LocalDate.of(2020, 5, 20),
                LocalDate.now(), "N");

        //when
        store.add(new TradeIdentifier(t1.tradeId(), t1.version()), t1);
        store.add(new TradeIdentifier(t2.tradeId(), t2.version()), t2);
        Map<TradeIdentifier, Trade> tradeIdentifierTradeMap = store.get();

        //then
        assertThat(tradeIdentifierTradeMap.size()).isEqualTo(2);
    }

    @Test
    public void whenStoreTwoVersionForATrade_getLatestVersion_shouldReturnLatestVersion() {
        TradeStore store = new TradeStore();

        //given
        Trade t1 =new Trade("T1", 1, "CP-1", "B1",
                LocalDate.of(2020, 5, 20),
                LocalDate.now(), "N");

        Trade t2 =new Trade("T1", 2, "CP-1", "B1",
                LocalDate.of(2020, 5, 20),
                LocalDate.now(), "N");

        //when
        store.add(new TradeIdentifier(t1.tradeId(), t1.version()), t1);
        store.add(new TradeIdentifier(t2.tradeId(), t2.version()), t2);
        int latestVersion = store.getLatestVersion(new TradeIdentifier("T1", -1));

        //then
        assertThat(latestVersion).isEqualTo(2);
    }

    @Test
    public void whenStoreTwoVersionForATrade_getByIdentifier_shouldReturnCorrespondingTrade() {
        TradeStore store = new TradeStore();

        //given
        Trade t1 =new Trade("T1", 1, "CP-1", "B1",
                LocalDate.of(2020, 5, 20),
                LocalDate.now(), "N");

        Trade t2 =new Trade("T1", 2, "CP-1", "B1",
                LocalDate.of(2020, 5, 20),
                LocalDate.now(), "N");

        //when
        store.add(new TradeIdentifier(t1.tradeId(), t1.version()), t1);
        store.add(new TradeIdentifier(t2.tradeId(), t2.version()), t2);
        List<Trade> trades = store.get(new TradeIdentifier("T1", 1));

        //then
        assertThat(trades.size()).isEqualTo(1);
        assertThat(trades.get(0).tradeId()).isEqualTo("T1");
        assertThat(trades.get(0).version()).isEqualTo(1);
    }

    @Test
    public void whenStoreSameVersionAdded_add_shouldOverrideExistingTrade() {
        TradeStore store = new TradeStore();

        //given
        Trade t1 =new Trade("T1", 1, "CP-1", "B1",
                LocalDate.of(2020, 5, 20),
                LocalDate.now(), "N");

        Trade t2 =new Trade("T1", 1, "CP-1", "B2",
                LocalDate.of(2020, 5, 20),
                LocalDate.now(), "N");

        //when
        store.add(new TradeIdentifier(t1.tradeId(), t1.version()), t1);
        store.add(new TradeIdentifier(t2.tradeId(), t2.version()), t2);
        List<Trade> trades = store.get(new TradeIdentifier("T1", 1));

        //then
        assertThat(trades.size()).isEqualTo(1);
        assertThat(trades.get(0).bookId()).isEqualTo("B2");
    }

    @Test
    public void whenStoreTwoTradesAreAdded_get_shouldReturnAllVersion() {
        TradeStore store = new TradeStore();

        //given
        Trade t1 =new Trade("T1", 1, "CP-1", "B1",
                LocalDate.of(2020, 5, 20),
                LocalDate.now(), "N");

        Trade t2 =new Trade("T1", 2, "CP-1", "B2",
                LocalDate.of(2020, 5, 20),
                LocalDate.now(), "N");

        //when
        store.add(new TradeIdentifier(t1.tradeId(), t1.version()), t1);
        store.add(new TradeIdentifier(t2.tradeId(), t2.version()), t2);
        List<Trade> trades = store.get(new TradeIdentifier("T1", -1));

        //then
        assertThat(trades.size()).isEqualTo(2);
        assertThat(trades.get(0).bookId()).containsAnyOf("B1", "B2");
        assertThat(trades.get(1).bookId()).containsAnyOf("B1", "B2");
    }

    @Test
    public void whenMaturityExpired_inspect_shouldMarkExpireFlagToTrue() {
        TradeStore store = new TradeStore();

        //given
        Trade t1 =new Trade("T1", 1, "CP-1", "B1",
                LocalDate.of(2020, 5, 20),
                LocalDate.now(), "N");

        //when
        store.add(new TradeIdentifier(t1.tradeId(), t1.version()), t1);
        store.inspect();
        List<Trade> trades = store.get(new TradeIdentifier("T1", 1));

        //then
        assertThat(trades.size()).isEqualTo(1);
        assertThat(trades.get(0).expired()).isEqualTo("Y");
    }

    @Test
    public void whenMaturityNotExpired_inspect_shouldNotUpdateExpireFlag() {
        TradeStore store = new TradeStore();

        //given
        Trade t1 =new Trade("T1", 1, "CP-1", "B1",
                LocalDate.of(2027, 5, 20),
                LocalDate.now(), "N");

        //when
        store.add(new TradeIdentifier(t1.tradeId(), t1.version()), t1);
        store.inspect();
        List<Trade> trades = store.get(new TradeIdentifier("T1", 1));

        //then
        assertThat(trades.size()).isEqualTo(1);
        assertThat(trades.get(0).expired()).isEqualTo("N");
    }

    @Test
    public void whenStoreHasTrade_contains_shouldReturnTrue() {
        TradeStore store = new TradeStore();

        //given
        Trade t1 =new Trade("T1", 1, "CP-1", "B1",
                LocalDate.of(2027, 5, 20),
                LocalDate.now(), "N");

        //when
        store.add(new TradeIdentifier(t1.tradeId(), t1.version()), t1);

        boolean contains = store.contains(new TradeIdentifier("T1", 1));

        //then
        assertThat(contains).isTrue();
    }

    @Test
    public void whenStoreDoesNotHaveTrade_contains_shouldReturnFalse() {
        TradeStore store = new TradeStore();

        //given
        Trade t1 =new Trade("T1", 1, "CP-1", "B1",
                LocalDate.of(2027, 5, 20),
                LocalDate.now(), "N");

        //when
        store.add(new TradeIdentifier(t1.tradeId(), t1.version()), t1);
        boolean contains = store.contains(new TradeIdentifier("ghost", 1));

        //then
        assertThat(contains).isFalse();
    }
}
