package com.marketboro.reserve.domain.discount;

public class FixDiscountPolicy implements DiscountPolicy {

    private int discountFixAmount = 1000;

    @Override
    public int discount(int price) {

        return discountFixAmount;
    }
}
