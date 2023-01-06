package com.marketboro.reserve.domain.order;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    private String itemName;
    private int itemPrice;

    @Builder
    public Order(Member member, String itemName, int itemPrice) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
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
    public static Order createOrder(Member member, String itemName, int itemPrice) {
        Order order = new Order();
        order.saveMember(member);
        order.setItemName(itemName);
        order.setItemPrice(itemPrice);
        return order;
    }
}
