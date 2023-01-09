package com.marketboro.reserve.domain.discount;

import org.springframework.stereotype.Component;

public class FixReservePolicy implements ReservePolicy {

    private static final int FIXRESERVE = 1000;

    @Override
    public int calculateReserve(int price) {
        return FIXRESERVE;
    }
}
