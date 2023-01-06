package com.marketboro.reserve.domain.member;

import com.marketboro.reserve.domain.order.Order;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
public class MemberDto {

    private Long id;

    private String name;
    private List<Order> orders = new ArrayList<>();
    private int reserve;

    public MemberDto(Long id, String name, int reserve) {
        this.id = id;
        this.name = name;
        this.reserve = reserve;
    }

    public void saveReserve(int reserve) {
        this.reserve += reserve;
    }
}
