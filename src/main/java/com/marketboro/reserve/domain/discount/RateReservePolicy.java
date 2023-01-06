package com.marketboro.reserve.domain.discount;

import org.springframework.stereotype.Component;

@Component
public class RateReservePolicy implements ReservePolicy {

    private static final int RATEDISCOUNT = 10;

    @Override
    public int collect(int price) {
        return price * RATEDISCOUNT / 100;
    }
}
