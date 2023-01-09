package com.marketboro.reserve.controller;

import com.marketboro.reserve.domain.member.Member;
import com.marketboro.reserve.domain.order.Order;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MemberControllerTest {

    private static final int RATERESERVE = 10;

    @Test
    void findTotalReserve() {
        // given
        Member memberA = new Member("member_A", 0);
        Order book1 = new Order(memberA, "book1", 20000, 20000 * RATERESERVE / 100);
        Order book2 = new Order(memberA, "book2", 30000, 30000 * RATERESERVE / 100);

        // when
        memberA.saveOrder(book1);
        memberA.saveOrder(book2);
        memberA.setTotalReserve(book1.getReserveFund() + book2.getReserveFund());

        // then
        assertThat(memberA.getTotalReserve()).isEqualTo(5000);
    }

    @Test
    void order() {
        // given

        // when

        // then
    }

    @Test
    void historySaveReserve() {
        // given

        // when

        // then
    }

    @Test
    void useReserve() {
        // given

        // when

        // then
    }

    @Test
    void historyUseReserve() {
        // given

        // when

        // then
    }
}