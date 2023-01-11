package com.marketboro.reserve.controller;

import com.marketboro.reserve.domain.member.Member;
import com.marketboro.reserve.domain.order.Order;
import com.marketboro.reserve.domain.reserve.Reserve;
import com.marketboro.reserve.exception.InvalidRequestException;
import com.marketboro.reserve.repository.MemberRepository;
import com.marketboro.reserve.repository.OrderRepository;
import com.marketboro.reserve.repository.ReserveRepository;
import com.marketboro.reserve.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

    private static final int RATERESERVE = 10;
    @Autowired private MockMvc mockMvc;
    @Autowired private MemberRepository memberRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private ReserveRepository reserveRepository;
    @Autowired private MemberService memberService;

    @Test
    @Transactional
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
    @Transactional
    @DisplayName("회원별 적립금 적립을 확인하는 테스트입니다.")
    void 적립금_적립_확인_API_테스트() throws Exception{
        // given
        String uri = "/api/v1/members/1/reserve";
        Member memberA = new Member(1L, "member_A", 0);
        Order testBook = Order.createOrder(memberA, "testBook", 20000, 20000 * RATERESERVE / 100);

        int reserve = testBook.getReserveFund();
        reserve += memberA.getTotalReserve();
        memberA.setTotalReserve(reserve);
        memberRepository.save(memberA);

        // expect
        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .param("memberId", String.valueOf(1))
                        .param("itemName", "book1")
                        .param("itemPrice", String.valueOf(10000)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Transactional
    @DisplayName("회원별 적립금 적립 내역을 조회하는 테스트입니다.")
    void 적립금_적립내역_조회_API_테스트() throws Exception{
        // given
        String uri = "/api/v1/members/1/reserve/usage-details";
        Member memberA = new Member(1L, "member_A", 1000);

        Order book1 = Order.createOrder(memberA, "book1", 20000, 20000 * RATERESERVE / 100);
        Order book2 = Order.createOrder(memberA, "book2", 30000, 20000 * RATERESERVE / 100);
        Order book3 = Order.createOrder(memberA, "book3", 10000, 20000 * RATERESERVE / 100);

        orderRepository.save(book1);
        orderRepository.save(book2);
        orderRepository.save(book3);
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by("id").descending());
        Page<Order> findOrders = orderRepository.findAll(1L, pageRequest);

        // expect
        mockMvc.perform(MockMvcRequestBuilders.get(uri)
                        .param("memberId", String.valueOf(1))
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(5)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].itemName").value("book3"))
                .andExpect(jsonPath("$[1].itemName").value("book2"))
                .andExpect(jsonPath("$[2].itemName").value("book1"))
                .andDo(print());
    }

    @Test
    @Transactional
    @DisplayName("회원별 적립금 사용을 확인하는 테스트입니다.")
    void 적립금_사용_확인_API_테스트() throws Exception{
        // given
        String uri = "/api/v1/members/1/reserve";
        Member memberA = new Member(1L, "member_A", 10000);
        Order book1 = Order.createOrder(memberA, "book1", 20000, 20000 * RATERESERVE / 100);
        Order book2 = Order.createOrder(memberA, "book2", 30000, 30000 * RATERESERVE / 100);

        memberRepository.save(memberA);
        orderRepository.save(book1);
        orderRepository.save(book2);
        PageRequest pageRequest = PageRequest.of(0, 1);
        Order findOrder = orderRepository.findOne(1L, pageRequest);
        findOrder.setExpiryDate(findOrder.getCreatedDate().plusYears(1));

        if(memberA.getTotalReserve() - findOrder.getReserveFund() >= 0) {
            findOrder.changeReserveFund(0);
            memberA.changeTotalReserve(memberA.getTotalReserve()-findOrder.getReserveFund());
            memberRepository.save(memberA);
            orderRepository.save(findOrder);
        }
        else {
            throw new InvalidRequestException("회원 별 적립금 합계는 마이너스가 될 수 없습니다.");
        }

        // expect
        mockMvc.perform(MockMvcRequestBuilders.put(uri))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Transactional
    @DisplayName("회원별 적립금 사용 내역을 조회하는 테스트입니다.")
    void 적립금_사용내역_조회_API_테스트() throws Exception{
        // given
        String uri = "/api/v1/members/1/reserve/accumulated-details";
        Member memberA = new Member(1L, "member_A", 10000);

        Order book1 = Order.createOrder(memberA, "book1", 20000, 20000 * RATERESERVE / 100);
        Order book2 = Order.createOrder(memberA, "book2", 30000, 30000 * RATERESERVE / 100);

        memberRepository.save(memberA);
        orderRepository.save(book1);
        orderRepository.save(book2);
        PageRequest pageRequest = PageRequest.of(0, 1);

        for(int i=1; i<=2; i++) {
            Order findOrder = orderRepository.findOne(1L, pageRequest);

            Reserve newReserve = Reserve.createReserve(memberA, findOrder.getReserveFund());
            memberA.saveReserve(newReserve);

            if(memberA.getTotalReserve() - findOrder.getReserveFund() >= 0) {
                memberA.changeTotalReserve(memberA.getTotalReserve()-findOrder.getReserveFund());
                findOrder.changeReserveFund(0);
                orderRepository.save(findOrder);
                memberRepository.save(memberA);
                reserveRepository.save(newReserve);
            }
            else {
                throw new InvalidRequestException("회원 별 적립금 합계는 마이너스가 될 수 없습니다.");
            }
        }

        pageRequest = PageRequest.of(0, 2);
        Page<Reserve> findReserves = reserveRepository.findAll(1L, pageRequest);

        // expect
        mockMvc.perform(MockMvcRequestBuilders.get(uri)
                        .param("memberId", String.valueOf(1))
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(5)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].usedReserve").value(3000))
                .andExpect(jsonPath("$[1].usedReserve").value(2000))
                .andDo(print());
    }
}