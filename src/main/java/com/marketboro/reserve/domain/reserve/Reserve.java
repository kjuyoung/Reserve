package com.marketboro.reserve.domain.reserve;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.marketboro.reserve.domain.member.Member;
import com.marketboro.reserve.domain.order.Order;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter
@Entity
@Table(name = "reserve")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reserve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    private int usedReserve;

    public Reserve(int usedReserve) {
        this.usedReserve = usedReserve;
    }

    // 생성 메서드
    public static Reserve createReserve(Member member, int usedReserve) {
        Reserve reserve = new Reserve();
        reserve.setMember(member);
        reserve.setUsedReserve(usedReserve);
        return reserve;
    }
}
