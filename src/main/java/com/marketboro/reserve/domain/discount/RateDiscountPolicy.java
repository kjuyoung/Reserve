package com.marketboro.reserve.domain.discount;

public class RateDiscountPolicy implements DiscountPolicy{

    private static final double RATEDISCOUNT = 0.1;

    @Override
    public int discount(int price) {
        return (int) (price * RATEDISCOUNT);
    }
}
