package com.marketboro.reserve.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class MemberDto {

    private Long id;
    private String name;
    private int totalReserve;

    public MemberDto(Long id, String name, int totalReserve) {
        this.id = id;
        this.name = name;
        this.totalReserve = totalReserve;
    }
}
