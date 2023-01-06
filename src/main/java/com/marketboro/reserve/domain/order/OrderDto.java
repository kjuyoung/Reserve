package com.marketboro.reserve.domain.order;

import com.marketboro.reserve.domain.member.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderDto {

    private Long id;

    private Member member;
    private String itemName;
    private int itemPrice;
    private int reserve;

    @Builder
    public OrderDto(String itemName, int itemPrice, int reserve) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.reserve = reserve;
    }
}
