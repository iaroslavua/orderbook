package com.bit.orderbook.services;

import com.bit.orderbook.dto.BinanceOrderBookDto;
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

    private ConcurrentHashMap<String, BinanceOrderBookDto> tickerToOrderBook = new ConcurrentHashMap<>();

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public BinanceOrderBookDto getBinanceOrderBook(String ticker) {
        if (tickerToOrderBook.containsKey(ticker)) {
            return tickerToOrderBook.get(ticker);
        } else {
            BinanceOrderBookDto orderBook = fetchBinanceOrderBook(ticker);
            tickerToOrderBook.put(ticker, orderBook);
            return orderBook;
        }
    }

    @Override
    @Scheduled(cron = "0 * * * * *")
    public void scheduledFetch() {
        logger.trace("Fetching data for all tickers");
        tickerToOrderBook.forEach((ticker, orderBook) -> {
            try {
                tickerToOrderBook.put(ticker, fetchBinanceOrderBook(ticker));
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                logger.info("Interrupted during fetching " + ticker);
            } catch (Exception e) {
                logger.error("Failed to get order book for " + ticker);
            }
        });
    }

    private BinanceOrderBookDto fetchBinanceOrderBook(String ticker) {
        ResponseEntity<BinanceOrderBookDto> response = restTemplate.getForEntity(BINANCE_API_URL + ticker, BinanceOrderBookDto.class);
        logger.debug(String.format("\nTicker: %s\nResponse Code: %d\nBody: %s", ticker, response.getStatusCodeValue(), response.getBody()));

        return response.getBody();
    }

}
