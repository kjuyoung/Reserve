package com.marketboro.reserve.domain.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.marketboro.reserve.domain.member.Member;
import lombok.*;

import javax.persistence.*;

@Getter @Setter
@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    private String itemName;
    private int itemPrice;
    private int reserve;

    @Builder
    public Order(Member member, String itemName, int itemPrice, int reserve) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.reserve = reserve;
        if(member != null) {
            setMember(member);
        }
    }

    //== 연관관계 메서드==//
    public void saveMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    //==생성 메서드==//
    public static Order createOrder(Member member, String itemName, int itemPrice, int reserve) {
        Order order = new Order();
        order.saveMember(member);
        order.setItemName(itemName);
        order.setItemPrice(itemPrice);
        order.setReserve(reserve);
        return order;
    }
}
