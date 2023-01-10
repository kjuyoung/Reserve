package com.marketboro.reserve.service;

import com.marketboro.reserve.domain.discount.ReservePolicy;
import com.marketboro.reserve.domain.member.Member;
import com.marketboro.reserve.domain.order.Order;
import com.marketboro.reserve.domain.reserve.Reserve;
import com.marketboro.reserve.exception.InvalidRequestException;
import com.marketboro.reserve.repository.MemberRepository;
import com.marketboro.reserve.repository.OrderRepository;
import com.marketboro.reserve.repository.ReserveRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class MemberServiceTest {

    private static final int RATERESERVE = 10;
    @Autowired
    private ReservePolicy reservePolicy;
    @Autowired private MemberRepository memberRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private ReserveRepository reserveRepository;

    @Test
    @Transactional
    @DisplayName("회원별 적립금 합계를 조회하는 테스트입니다.")
    void 적립금_합계_조회_테스트() {
        // given
        Member memberA = new Member(1L, "member_A", 1000);
        Order book1 = Order.createOrder(memberA, "book1", 20000, 20000 * RATERESERVE / 100);
        Order book2 = Order.createOrder(memberA, "book2", 30000, 30000 * RATERESERVE / 100);
        Order book3 = Order.createOrder(memberA, "book3", 10000, 10000 * RATERESERVE / 100);

        // when
        int reserve = book1.getReserveFund() + book2.getReserveFund() + book3.getReserveFund();
        reserve += memberA.getTotalReserve();
        memberA.setTotalReserve(reserve);
        memberRepository.save(memberA);
        Optional<Member> findMember = memberRepository.findById(1L);

        // then
        assertThat(findMember.get().getTotalReserve()).isEqualTo(7000);
    }

    @Test
    @Transactional
    @DisplayName("회원별 적립금 적립을 확인하는 테스트입니다.")
    void 적립금_적립_확인_테스트() {
        // given
        Member memberA = new Member(1L, "member_A", 0);
        Order testBook = Order.createOrder(memberA, "testBook", 20000, 20000 * RATERESERVE / 100);

        // when
        int reserve = testBook.getReserveFund();
        reserve += memberA.getTotalReserve();
        memberA.setTotalReserve(reserve);
        memberRepository.save(memberA);

        Optional<Member> findMember = memberRepository.findById(1L);

        // then
        assertThat(findMember.get().getTotalReserve()).isEqualTo(2000);
    }

    @Test
    @Transactional
    @DisplayName("회원별 적립금 적립 내역을 조회하는 테스트입니다.")
    void 적립금_적립내역_조회_테스트() {
        // given
        Member memberA = new Member(1L, "member_A", 1000);

        int book1Reserve = reservePolicy.calculateReserve(20000);
        int book2Reserve = reservePolicy.calculateReserve(30000);
        int book3Reserve = reservePolicy.calculateReserve(10000);
        Order book1 = Order.createOrder(memberA, "book1", 20000, book1Reserve);
        Order book2 = Order.createOrder(memberA, "book2", 30000, book2Reserve);
        Order book3 = Order.createOrder(memberA, "book3", 10000, book3Reserve);

        // when
        orderRepository.save(book1);
        orderRepository.save(book2);
        orderRepository.save(book3);
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by("id").descending());
        Page<Order> findOrders = orderRepository.findAll(1L, pageRequest);

        // then
        assertThat(findOrders.getSize()).isEqualTo(3);
    }

    @Test
    @Transactional
    @DisplayName("회원별 적립금 사용을 확인하는 테스트입니다.")
    void 적립금_사용_확인_테스트() {
        // given
        Member memberA = new Member(1L, "member_A", 10000);

        int book1Reserve = reservePolicy.calculateReserve(20000);
        Order book1 = Order.createOrder(memberA, "book1", 20000, book1Reserve);

        // when
        orderRepository.save(book1);
        PageRequest pageRequest = PageRequest.of(0, 1);
        Order findOrder = orderRepository.findOne(1L, pageRequest);

        if(memberA.getTotalReserve() - findOrder.getReserveFund() >= 0) {
            memberA.changeTotalReserve(memberA.getTotalReserve()-findOrder.getReserveFund());
            findOrder.changeReserveFund(0);
            memberRepository.save(memberA);
        }
        else {
            throw new InvalidRequestException("회원 별 적립금 합계는 마이너스가 될 수 없습니다.");
        }

        Optional<Member> findMember = memberRepository.findById(1L);

        // then
        assertThat(findMember.get().getTotalReserve()).isEqualTo(8000);
    }

    @Test
    @Transactional
    @DisplayName("회원별 적립금 사용 내역을 조회하는 테스트입니다.")
    void 적립금_사용내역_조회_테스트() {
        // given
        Member memberA = new Member(1L, "member_A", 10000);

        int book1Reserve = reservePolicy.calculateReserve(20000);
        int book2Reserve = reservePolicy.calculateReserve(30000);
        int book3Reserve = reservePolicy.calculateReserve(10000);
        Order book1 = Order.createOrder(memberA, "book1", 20000, book1Reserve);
        Order book2 = Order.createOrder(memberA, "book2", 30000, book2Reserve);
        Order book3 = Order.createOrder(memberA, "book3", 10000, book3Reserve);

        // when
        memberRepository.save(memberA);
        orderRepository.save(book1);
        orderRepository.save(book2);
        orderRepository.save(book3);
        PageRequest pageRequest = PageRequest.of(0, 1);

        for(int i=1; i<=2; i++) {
            Order findOrder = orderRepository.findOne(1L, pageRequest);

            Reserve newReserve = Reserve.createReserve(memberA, findOrder.getReserveFund());
            memberA.saveReserve(newReserve);

            if(memberA.getTotalReserve() - findOrder.getReserveFund() >= 0) {
                memberA.changeTotalReserve(memberA.getTotalReserve()-findOrder.getReserveFund());
                findOrder.changeReserveFund(0);
                memberRepository.save(memberA);
                reserveRepository.save(newReserve);
            }
            else {
                throw new InvalidRequestException("회원 별 적립금 합계는 마이너스가 될 수 없습니다.");
            }
        }

        pageRequest = PageRequest.of(0, 2);
        Page<Reserve> findReserves = reserveRepository.findAll(1L, pageRequest);
        Optional<Member> findMember = memberRepository.findById(1L);

        // then
        assertThat(findReserves.getSize()).isEqualTo(2);
        assertThat(findMember.get().getTotalReserve()).isEqualTo(5000);
    }
}