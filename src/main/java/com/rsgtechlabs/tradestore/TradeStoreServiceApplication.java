package com.rsgtechlabs.tradestore;

import com.rsgtechlabs.tradestore.model.Trade;
import com.rsgtechlabs.tradestore.service.impl.TradeStoreServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.LocalDate;
import java.util.concurrent.Executor;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class TradeStoreServiceApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(TradeStoreServiceApplication.class, args);
		TradeStoreServiceImpl storeService = applicationContext.getBean(TradeStoreServiceImpl.class);

		//Test data
		//@TODO - TO be removed
		Trade t1 =new Trade("T1", 1, "CP-1", "B1",
				LocalDate.of(2020, 5, 20),
				LocalDate.now(), "N");

		Trade t2 =new Trade("T2", 2, "CP-2", "B1",
				LocalDate.of(2021, 5, 20),
				LocalDate.now(), "N");

		Trade t3 =new Trade("T2", 1, "CP-1", "B1",
				LocalDate.of(2021, 5, 20),
				LocalDate.of(2015, 3, 14), "N");

		Trade t4 =new Trade("T3", 3, "CP3", "B2",
				LocalDate.of(2014, 5, 20),
				LocalDate.now(), "Y");

		Trade t5 =new Trade("T3", 3, "CP3", "B2",
				LocalDate.of(2025, 5, 20),
				LocalDate.now(), "N");

		Trade t6 =new Trade("T3", 2, "CP3", "B2",
				LocalDate.of(2025, 5, 20),
				LocalDate.now(), "N");

		Trade t7 =new Trade("T4", 1, "CP3", "B2",
				LocalDate.of(2024, 5, 17),
				LocalDate.now(), "N");

//		storeService.store(t1);
//		storeService.store(t2);
//		storeService.store(t3);
//		storeService.store(t4);
//		storeService.store(t5);
//		storeService.store(t6);
//      storeService.store(t7);
	}

	//@todo - Properties to be externalized to property file or injected later
	@Bean("tradeStoreTaskExecutor")
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(2);
		executor.setThreadNamePrefix("TradeStorePoolWorker-");
		executor.initialize();
		return executor;
	}
}
