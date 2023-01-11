package com.marketboro.reserve.domain.discount;

public class FixReservePolicy implements ReservePolicy {

    private static final int FIX_RESERVE = 1000;

    @Override
    public int calculateReserve(int price) {
        return FIX_RESERVE;
    }
}
