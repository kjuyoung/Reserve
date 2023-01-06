package com.marketboro.reserve.domain.member;

import com.marketboro.reserve.domain.order.Order;
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

    private int reserve;

    @Builder
    public Member(String name, int reserve) {
        this.name = name;
        this.reserve = reserve;
    }

    public Member (MemberDto memberDto) {
        this.name = memberDto.getName();
        this.reserve = memberDto.getReserve();
    }
}
