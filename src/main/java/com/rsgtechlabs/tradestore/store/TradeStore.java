package com.rsgtechlabs.tradestore.store;

import com.rsgtechlabs.tradestore.model.Trade;
import com.rsgtechlabs.tradestore.model.TradeIdentifier;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class TradeStore implements Store<TradeIdentifier, Trade> {
    private static final Logger logger = LoggerFactory.getLogger(TradeStore.class);
    //Concurrent hashmap to store the trades
    private Map<TradeIdentifier, Trade> tradeContainer;

    //Concurrent Hashmap to store the latest version for ease of use to avoid checking the map everytime for versions
    private Map<String, Integer> versionContainer;
    public TradeStore() {
        this.tradeContainer = new ConcurrentHashMap<>();
        this.versionContainer = new  ConcurrentHashMap<>();
    }

    @Override
    public void add(final TradeIdentifier key, final Trade value) {
        tradeContainer.put(key, value);
        versionContainer.merge(key.tradeId(), value.version(), (oldValue, newValue)-> oldValue < newValue ? newValue : oldValue);
    }

    @Override
    public boolean contains(final TradeIdentifier key) {
        return tradeContainer.containsKey(key);
    }

    @Override
    public int getLatestVersion(final TradeIdentifier key) {
        return versionContainer.containsKey(key.tradeId()) ? versionContainer.get(key.tradeId()) : -1 ;
    }

    @Override
    public Map<TradeIdentifier, Trade> get() {
        //TODO - need to return deep clone copy
        return tradeContainer;
    }

    @Override
    public void inspect() {
        logger.debug("inspect - enter , tradeContainer : {} "+ tradeContainer);
        LocalDate now = LocalDate.now();
        tradeContainer.forEach((key, value)-> {
            if(value.maturityDate().isBefore(now)) {
                logger.debug("Trade maturity expired : {} "+ value);
                tradeContainer.merge(key, value, (oldTrade, newTrade) -> new Trade(oldTrade.tradeId(), oldTrade.version(), oldTrade.counterPartyId(), oldTrade.bookId(), oldTrade.maturityDate(), oldTrade.createdDate(), "Y")
                );
            }
        });
        logger.debug("inspect - leave , tradeContainer : {} "+ tradeContainer);
    }

    @Override
    public List<Trade> get(final TradeIdentifier key) {
        List<Trade> trades = new ArrayList<>();
        if(key.version() == -1) {
            tradeContainer.forEach((k,v) -> {
                if(k.tradeId().equals(key.tradeId())) {
                    trades.add(v);
                }
            });
        } else {
            Trade trade = tradeContainer.get(key);
            if(Objects.nonNull(trade)) {
                trades.add(trade);
            }
        }
        return trades;
    }
}
