package com.marketboro.reserve.domain.discount;

import org.springframework.stereotype.Component;

@Component
public class RateReservePolicy implements ReservePolicy {

    private static final double RATEDISCOUNT = 0.1;

    @Override
    public int collect(int price) {
        return (int) (price * RATEDISCOUNT);
    }
}
