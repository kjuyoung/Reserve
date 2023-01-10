package com.marketboro.reserve.controller;

import com.marketboro.reserve.domain.member.Member;
import com.marketboro.reserve.domain.order.Order;
import com.marketboro.reserve.repository.MemberRepository;
import com.marketboro.reserve.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

    private static final int RATERESERVE = 10;
    @Autowired private MockMvc mockMvc;
    @Autowired private MemberRepository memberRepository;
    @Autowired private MemberService memberService;

    @Test
    @DisplayName("회원별 적립금 합계를 조회하는 테스트입니다.")
    void 적립금_합계_조회_API_테스트() throws Exception{
        //given
        String uri = "/api/v1/members/1/total-reserve";
        Member memberA = new Member(1L, "member_A", 0);
        Order book1 = Order.createOrder(memberA, "testBook", 20000, 20000 * RATERESERVE / 100);
        Order book2 = Order.createOrder(memberA, "testBook", 20000, 20000 * RATERESERVE / 100);
        Order book3 = Order.createOrder(memberA, "testBook", 20000, 20000 * RATERESERVE / 100);

        int reserve = book1.getReserveFund() + book2.getReserveFund() + book3.getReserveFund();
        reserve += memberA.getTotalReserve();
        memberA.setTotalReserve(reserve);
        memberRepository.save(memberA);

        // expect
        mockMvc.perform(MockMvcRequestBuilders.get(uri).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalReserve").value(6000))
                .andDo(print());
    }

    @Test
    @DisplayName("회원별 적립금 적립을 확인하는 테스트입니다.")
    void 적립금_적립_확인_API_테스트() throws Exception{
        // given
        String uri = "/api/v1/members/1/reserve";
        Member memberA = new Member(1L, "member_A", 0);
        Order testBook = Order.createOrder(memberA, "testBook", 20000, 20000 * RATERESERVE / 100);

        // expect
        int reserve = testBook.getReserveFund();
        reserve += memberA.getTotalReserve();
        memberA.setTotalReserve(reserve);
        memberRepository.save(memberA);

        mockMvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.totalReserve").value(2000))
                .andDo(print());
    }

    @Test
    @DisplayName("회원별 적립금 적립 내역을 조회하는 테스트입니다.")
    void 적립금_적립내역_조회_API_테스트() throws Exception{
        // given
        String uri = "/api/v1/members/1/reserve";
        Member memberA = new Member(1L, "member_A", 1000);

        Order book1 = Order.createOrder(memberA, "book1", 20000, 20000 * RATERESERVE / 100);
        Order book2 = Order.createOrder(memberA, "book2", 30000, 20000 * RATERESERVE / 100);
        Order book3 = Order.createOrder(memberA, "book3", 10000, 20000 * RATERESERVE / 100);

        // expect

    }

    @Test
    @DisplayName("회원별 적립금 사용을 확인하는 테스트입니다.")
    void 적립금_사용_확인_API_테스트() throws Exception{
        // given
        String uri = "/api/v1/members/1/reserve";
        Member memberA = new Member(1L, "member_A", 10000);

        Order book1 = Order.createOrder(memberA, "book1", 20000, 20000 * RATERESERVE / 100);

        // expect

    }

    @Test
    @DisplayName("회원별 적립금 사용 내역을 조회하는 테스트입니다.")
    void 적립금_사용내역_조회_API_테스트() throws Exception{
        // given
        String uri = "/api/v1/members/1/reserve";
        Member memberA = new Member(1L, "member_A", 10000);

        Order book1 = Order.createOrder(memberA, "book1", 20000, 20000 * RATERESERVE / 100);
        Order book2 = Order.createOrder(memberA, "book2", 30000, 20000 * RATERESERVE / 100);
        Order book3 = Order.createOrder(memberA, "book3", 10000, 20000 * RATERESERVE / 100);

        // expect

    }
}