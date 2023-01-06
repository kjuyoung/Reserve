package com.marketboro.reserve.controller;

import com.marketboro.reserve.domain.order.Order;
import com.marketboro.reserve.domain.order.OrderDto;
import com.marketboro.reserve.service.MemberService;
import com.marketboro.reserve.service.OrderService;
import com.marketboro.reserve.service.ReserveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class OrderController {

    private Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final ReserveService reserveService;
    private final OrderService orderService;
    private final MemberService memberService;

    @PostMapping("/orders")
    public void order(@RequestParam("memberId") Long memberId
                    , @RequestParam("itemName") String itemName
                    , @RequestParam("itemPrice") int itemPrice) {

        orderService.order(memberId, itemName, itemPrice);
//        reserveService.save(memberId, itemPrice);
        memberService.updateReserve(memberId, itemName, itemPrice);
//        memberService.updateTotalReserve(memberId, itemPrice);
    }

    @GetMapping("/orders")
    public List<Order> findOrders() {
        List<Order> findOrders = orderService.findAll();
        return findOrders;
//        return findOrders.stream()
//                .map(o -> new OrderDto(o.getItemName(), o.getItemPrice(), o.getReserve()))
//                .collect(Collectors.toList());
    }
}
