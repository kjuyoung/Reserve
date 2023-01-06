package com.marketboro.reserve.service;

import com.marketboro.reserve.domain.member.Member;
import com.marketboro.reserve.domain.order.Order;
import com.marketboro.reserve.domain.order.OrderDto;
import com.marketboro.reserve.repository.OrderRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ReserveService reserveService;

    public List<OrderDto> findAll() {
        List<Order> findOrders = orderRepository.findAll();
        return findOrders.stream()
                .map(o -> new OrderDto(o.getMember(), o.getItemName(), o.getItemPrice()))
                .collect(Collectors.toList());
    }

    @Transactional
    public Long order(Long memberId, String itemName, int itemPrice) {
        Member member = reserveService.findById(memberId);

        Order order = Order.createOrder(member, itemName, itemPrice);

        orderRepository.save(order);
        reserveService.updateReserve(member, itemPrice);
        return order.getId();
    }
}
