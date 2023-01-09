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
    private int reserveFund;

    @Builder
    public Order(Member member, String itemName, int itemPrice, int reserveFund) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.reserveFund = reserveFund;
        if(member != null) {
            setMember(member);
        }
    }

    //==생성 메서드==//
    public static Order createOrder(Member member, String itemName, int itemPrice, int reserveFund) {
        Order order = new Order();
        order.setMember(member);
        order.setItemName(itemName);
        order.setItemPrice(itemPrice);
        order.setReserveFund(reserveFund);
        return order;
    }

    public void changeReserveFund(int reserveFund) {
        this.reserveFund = reserveFund;
    }
}
