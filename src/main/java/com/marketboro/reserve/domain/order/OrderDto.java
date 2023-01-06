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

    @Builder
    public OrderDto(Member member, String itemName, int itemPrice) {
        this.member = member;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }
}
