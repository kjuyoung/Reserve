package com.marketboro.reserve.domain.reserve;

import com.marketboro.reserve.domain.member.Member;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "reserves")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reserve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    private int amount;

    //== 연관관계 메서드==//
//    public void saveMember(Member member) {
//        this.member = member;
//        member.getReserves().add(this);
//    }

    //==생성 메서드==//
//    public static Reserve createReserve(Member member, int amount) {
//        Reserve reserve = new Reserve();
//        reserve.saveMember(member);
//        reserve.setAmount(amount);
//        return reserve;
//    }
}
