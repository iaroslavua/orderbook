package com.bit.orderbook.services.impl;

import org.springframework.scheduling.annotation.Scheduled;

public interface OrderBookFetcher {

    String getBinanceOrderBook(String ticker);

    @Scheduled(fixedDelay = 30000)
    void scheduledFetch();
}
