package com.marketboro.reserve.domain.order;

import lombok.Getter;

@Getter
public class OrderDto {

    private Long id;
    private String itemName;
    private int itemPrice;
    private int reserveFund;

    public OrderDto(Long id, String itemName, int itemPrice, int reserveFund) {
        this.id = id;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.reserveFund = reserveFund;
    }
}
