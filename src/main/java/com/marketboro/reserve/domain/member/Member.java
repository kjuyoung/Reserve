package com.marketboro.reserve.domain.member;

import com.marketboro.reserve.domain.order.Order;
import com.marketboro.reserve.domain.reserve.Reserve;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    private int totalReserve;

    @Builder
    public Member(String name, int totalReserve) {
        this.name = name;
        this.totalReserve = totalReserve;
    }

    public Member (MemberDto memberDto) {
        this.name = memberDto.getName();
        this.totalReserve = memberDto.getTotalReserve();
    }
}
