package com.bit.orderbook.services;

import com.bit.orderbook.services.impl.OrderBookFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class OrderBookFetcherImpl implements OrderBookFetcher {

    private static final Logger logger = LoggerFactory.getLogger(OrderBookFetcherImpl.class);

    private static final String BINANCE_API_URL = "https://api.binance.com/api/v3/depth?limit=5000&symbol=";

    private ConcurrentHashMap<String, String> tickerToOrderBook = new ConcurrentHashMap<>();

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String getBinanceOrderBook(String ticker) {
        if (tickerToOrderBook.containsKey(ticker)) {
            return tickerToOrderBook.get(ticker);
        } else {
            String orderBook = fetchBinanceOrderBook(ticker);
            tickerToOrderBook.put(ticker, orderBook);
            return orderBook;
        }
    }

    @Override
    @Scheduled(fixedDelay = 30000)
    public void scheduledFetch() {
        tickerToOrderBook.forEach((ticker, orderBook) -> {
            tickerToOrderBook.put(ticker, fetchBinanceOrderBook(ticker));
        });
    }

    private String fetchBinanceOrderBook(String ticker) {
        ResponseEntity<String> response = restTemplate.getForEntity(BINANCE_API_URL + ticker, String.class);
        logger.debug("Response code: " + response.getStatusCodeValue());
        logger.debug("Response body: " + response.getBody());

        return response.getBody();
    }

}
