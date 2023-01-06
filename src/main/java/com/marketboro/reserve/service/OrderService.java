package com.marketboro.reserve.service;

import com.marketboro.reserve.domain.discount.RateReservePolicy;
import com.marketboro.reserve.domain.member.Member;
import com.marketboro.reserve.domain.order.Order;
import com.marketboro.reserve.domain.order.OrderDto;
import com.marketboro.reserve.repository.MemberRepository;
import com.marketboro.reserve.repository.OrderRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final RateReservePolicy rateReservePolicy;

    public List<Order> findAll() {
        List<Order> findOrders = orderRepository.findAll();
        return findOrders;
    }

    @Transactional
    public Long order(Long memberId, String itemName, int itemPrice) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        int reserve = rateReservePolicy.calculateReserve(itemPrice);
        Order order = Order.createOrder(findMember.get(), itemName, itemPrice, reserve);
        orderRepository.save(order);

        return order.getId();
    }
}
