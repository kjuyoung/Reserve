package com.marketboro.reserve;

import com.marketboro.reserve.domain.member.Member;
import com.marketboro.reserve.domain.order.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@SpringBootTest
public class MemberTest {

    @PersistenceContext
    EntityManager em;
    @Test
    @Transactional
    @Rollback(false)
    public void testEntity() {
        Member member1 = new Member("member1", 0);
        Member member2 = new Member("member2", 0);
        em.persist(member1);
        em.persist(member2);

        Order orderA = new Order(member1, "itemA", 1000, 1000*10/100);
        Order orderB = new Order(member1, "itemB", 2000, 2000*10/100);
        Order orderC = new Order(member2, "itemC", 3000, 3000*10/100);
        Order orderD = new Order(member2, "itemD", 4000, 4000*10/100);
        em.persist(orderA);
        em.persist(orderB);
        em.persist(orderC);
        em.persist(orderD);

        //초기화
        em.flush();
        em.clear();

        //확인
        List<Order> orders = em.createQuery("select o from Order o", Order.class)
                .getResultList();
        for (Order order : orders) {
            System.out.println("Order=" + order);
            System.out.println("-> Order.member=" + order.getMember());
        }
    }
}
