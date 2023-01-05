package com.marketboro.reserve.domain.member;

import com.marketboro.reserve.domain.item.Item;
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

    private String email;
    private String password;
    private List<Item> items = new ArrayList<>();
    private int reserve;

    @Builder
    public MemberDto(Long id, String email, String password, List<Item> items, int reserve) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.items = items;
        this.reserve = reserve;
    }

    public MemberDto(Long id, String email, List<Item> items, int reserve) {
        this.id = id;
        this.email = email;
        this.items = items;
        this.reserve = reserve;
    }

    public MemberDto(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public void saveReserve(int reserve) {
        this.reserve += reserve;
    }
}
