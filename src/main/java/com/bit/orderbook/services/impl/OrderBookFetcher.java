package com.bit.orderbook.services.impl;

import com.bit.orderbook.dto.BinanceOrderBookDto;
import org.springframework.scheduling.annotation.Scheduled;

public interface OrderBookFetcher {

    BinanceOrderBookDto getBinanceOrderBook(String ticker);

    @Scheduled(fixedDelay = 30000)
    void scheduledFetch();
}
