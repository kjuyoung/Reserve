package com.marketboro.reserve.domain.item;

import com.marketboro.reserve.domain.member.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ItemDto {

    private Long id;

    private Member member;
    private String name;
    private int price;

    @Builder
    public ItemDto(Member member, String name, int price) {
        this.member = member;
        this.name = name;
        this.price = price;
    }
}
