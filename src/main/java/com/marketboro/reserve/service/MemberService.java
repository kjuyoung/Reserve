package com.marketboro.reserve.service;

import com.marketboro.reserve.domain.discount.RateReservePolicy;
import com.marketboro.reserve.domain.member.Member;
import com.marketboro.reserve.domain.order.Order;
import com.marketboro.reserve.domain.order.OrderDto;
import com.marketboro.reserve.domain.reserve.Reserve;
import com.marketboro.reserve.domain.reserve.ReserveDto;
import com.marketboro.reserve.repository.MemberRepository;
import com.marketboro.reserve.repository.OrderRepository;
import com.marketboro.reserve.repository.ReserveRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ReserveRepository reserveRepository;
    private final RateReservePolicy rateReservePolicy;

    /**
     * 회원별 적립금 합계 조회
     * @param id
     * @return 적립금 합계금액
     */
    public int findTotalReserve(Long id) {
        Optional<Member> member = memberRepository.findById(id);

        return member.get().getTotalReserve();
    }

    /**
     * 회원별 적립금 적립
     * @param memberId
     * @param itemName
     * @param itemPrice
     * @return
     */
    @Transactional
    public Long saveReserve(Long memberId, String itemName, int itemPrice) {
        Optional<Member> findMember = memberRepository.findById(memberId);

        int reserve = rateReservePolicy.calculateReserve(itemPrice);
        Order order = Order.createOrder(findMember.get(), itemName, itemPrice, reserve);
        findMember.get().saveOrder(order);

        reserve += findMember.get().getTotalReserve();
        findMember.get().setTotalReserve(reserve);

        orderRepository.save(order);

        return order.getId();
    }

    /**
     * 회원별 적립금 적립 내역 조회
     * @param memberId
     * @param pageRequest
     * @return
     */
    public List<OrderDto> historySaveReserve(Long memberId, PageRequest pageRequest) {
        Page<Order> findOrders = orderRepository.findAll(memberId, pageRequest);

        return findOrders
                .map(o -> new OrderDto(o.getId(), o.getItemName(), o.getItemPrice(), o.getReserveFund()))
                .getContent();
    }

    /**
     * 회원별 적립금 사용
     * @param memberId
     */
    @Transactional
    public Long useReserve(Long memberId) {
        Optional<Member> findMember = memberRepository.findById(memberId);

        PageRequest pageRequest = PageRequest.of(0, 1);
        Order findOrder = orderRepository.findOne(pageRequest);

        Reserve newReserve = Reserve.createReserve(findMember.get(), findOrder.getReserveFund());
        findMember.get().saveReserve(newReserve);

        if(findMember.get().getTotalReserve() - findOrder.getReserveFund() >= 0) {
            findMember.get().changeTotalReserve(findMember.get().getTotalReserve()-findOrder.getReserveFund());
        }

        findOrder.changeReserveFund(0);
        reserveRepository.save(newReserve);

        return newReserve.getId();
    }

    /**
     * 회원별 적립금 사용 내역 조회
     * @param memberId
     * @param pageRequest
     * @return
     */
    public List<ReserveDto> historyUseReserve(Long memberId, PageRequest pageRequest) {
        Page<Reserve> findReserves = reserveRepository.findAll(memberId, pageRequest);

        return findReserves
                .map(r -> new ReserveDto(r.getId(), r.getUsedReserve()))
                .getContent();
    }
}
