package com.bit.orderbook.services;

import com.bit.orderbook.services.impl.OrderBookFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderBookFetcherImpl implements OrderBookFetcher {

    private static final Logger logger = LoggerFactory.getLogger(OrderBookFetcherImpl.class);

    private static final String BINANCE_API_URL = "https://api.binance.com/api/v3/depth?limit=5000&symbol=";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String fetchBinanceOrderBook(String ticker) {
        ResponseEntity<String> response = restTemplate.getForEntity(BINANCE_API_URL + ticker, String.class);
        logger.debug("Response code: " + response.getStatusCodeValue());
        logger.debug("Response body: " + response.getBody());

        if (response.getStatusCodeValue() == 429) {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                logger.info("Thread was interrupted");
                return null;
            }
        }

        return response.getBody();
    }

}
