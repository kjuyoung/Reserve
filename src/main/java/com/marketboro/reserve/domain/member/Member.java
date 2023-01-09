package com.marketboro.reserve.domain.member;

import com.marketboro.reserve.domain.order.Order;
import com.marketboro.reserve.domain.reserve.Reserve;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
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

    @OneToMany(mappedBy = "member")
    private List<Reserve> reserves = new ArrayList<>();

    private int totalReserve;

    public void saveOrder(Order order) {
        this.orders.add(order);
    }

    public void saveReserve(Reserve reserve) {
        this.reserves.add(reserve);
    }

    public void changeTotalReserve(int totalReserve) {
        this.totalReserve = totalReserve;
    }

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
