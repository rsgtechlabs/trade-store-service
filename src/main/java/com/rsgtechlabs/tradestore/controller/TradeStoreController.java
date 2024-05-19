package com.rsgtechlabs.tradestore.controller;

import com.rsgtechlabs.tradestore.dto.TradeDTO;
import com.rsgtechlabs.tradestore.dto.TradeGetRequest;
import com.rsgtechlabs.tradestore.dto.TradeGetResponse;
import com.rsgtechlabs.tradestore.dto.TradeStoreResponse;
import com.rsgtechlabs.tradestore.mapper.TradeMapper;
import com.rsgtechlabs.tradestore.model.Trade;
import com.rsgtechlabs.tradestore.service.TradeStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@RestController
@RequestMapping("/api/v1")
public class TradeStoreController {

    private static final Logger logger = LoggerFactory.getLogger(TradeStoreController.class);

    @Autowired
    private TradeMapper tradeMapper;

    @Autowired
    @Qualifier("tradeStoreTaskExecutor")
    private Executor tradeStoreTaskExecutor;

    @Autowired
    public TradeStoreService tradeStoreService;

    @PostMapping("/trade")
    @Async
    public CompletableFuture<TradeStoreResponse> store(@RequestBody final TradeDTO tradeDTO) {

        logger.info("Started adding data to trade store , tradeDTO: {}", tradeDTO);
        final Trade trade = tradeMapper.toModel(tradeDTO);
        CompletableFuture<TradeStoreResponse> tradeStoreResponseCompletableFuture
                = CompletableFuture.supplyAsync(() -> tradeStoreService.store(trade), tradeStoreTaskExecutor);
        logger.info("Ended adding data to trade store");
        return tradeStoreResponseCompletableFuture;
    }

   // @TODO - Follow Rest Standards for API naming/URI conventions/definition
    @GetMapping("/trade")
    public ResponseEntity<TradeGetResponse> getTrade(@RequestBody final TradeGetRequest request) {
        logger.info("Get Trade data from trade store started for request: {}", request);
        final TradeGetResponse response = tradeStoreService.get(request);
        logger.info("Get Trade data from trade store ended");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return new ResponseEntity<>("UP", HttpStatus.OK);
    }
}
