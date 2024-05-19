package com.rsgtechlabs.tradestore.store;

import com.rsgtechlabs.tradestore.model.Trade;
import com.rsgtechlabs.tradestore.model.TradeIdentifier;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StoreInspector {
    private static final Logger logger = LoggerFactory.getLogger(StoreInspector.class);
    @Autowired
    private Store<TradeIdentifier, Trade> tradeStore;
    @Scheduled(fixedDelayString = "${trade.store.inspection.schedule.timeInMillis}")
    public void validateStore() {
        logger.info("validateStore - enter");
        tradeStore.inspect();
        logger.info("validateStore - leave");
    }
}
