package com.marketboro.reserve.domain.member;

import com.marketboro.reserve.domain.order.Order;
import com.marketboro.reserve.domain.reserve.Reserve;
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
    private int totalReserve;

    public MemberDto(Long id, String name, int totalReserve) {
        this.id = id;
        this.name = name;
        this.totalReserve = totalReserve;
    }
}
