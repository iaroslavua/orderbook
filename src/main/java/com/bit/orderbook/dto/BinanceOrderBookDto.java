package com.bit.orderbook.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BinanceOrderBookDto {

    private Long lastUpdateId;

    private Double[][] bids;

    private Double[][] asks;
}
