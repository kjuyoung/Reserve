package com.marketboro.reserve.domain.discount;

import org.springframework.stereotype.Component;

@Component
public class RateReservePolicy implements ReservePolicy {

    private static final int RATERESERVE = 10;

    @Override
    public int calculateReserve(int price) {
        return price * RATERESERVE / 100;
    }
}
