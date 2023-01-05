package com.marketboro.reserve.domain.discount;

import org.springframework.stereotype.Component;

@Component
public class FixReservePolicy implements ReservePolicy {

    private int discountFixAmount = 1000;

    @Override
    public int collect(int price) {
        return discountFixAmount;
    }
}
