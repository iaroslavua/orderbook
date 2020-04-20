package com.bit.orderbook.controllers;

import com.bit.orderbook.services.impl.OrderBookFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(path = "/order-book")
public class BinanceController {

    private static final Logger logger = LoggerFactory.getLogger(BinanceController.class);

    private OrderBookFetcher orderBookFetcher;

    public BinanceController(@Autowired OrderBookFetcher orderBookFetcher) {
        this.orderBookFetcher = orderBookFetcher;
    }

    @GetMapping(path = "/binance/{ticker}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getOrderBook(@PathVariable(name = "ticker") String ticker) {
        return Flux.<String>generate(generator -> {
            try {
                generator.next(orderBookFetcher.getBinanceOrderBook(ticker));
                logger.debug("Sleeping...");
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                generator.complete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).doOnTerminate(() -> {
            logger.info("Binance order book Flux terminated");
        }).doOnCancel(() -> {
            logger.info("Binance order book Flux cancelled");
        }).doOnError(err -> {
            err.printStackTrace();
            logger.error("Error in Binance order book fetching...", err);
        });

    }

}
