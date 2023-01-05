package com.marketboro.reserve.domain.item;

import com.marketboro.reserve.domain.member.Member;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "item")
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    private String name;
    private int price;

    @Builder
    public Item(Member member, String name, int price) {
        this.member = member;
        this.name = name;
        this.price = price;
    }

    //== 연관관계 메서드==//
    public void setMember(Member member) {
        this.member = member;
        member.getItems().add(this);
    }
}
