package com.marketboro.reserve.controller;

import com.marketboro.reserve.domain.discount.RateReservePolicy;
import com.marketboro.reserve.domain.item.ItemDto;
import com.marketboro.reserve.domain.member.Member;
import com.marketboro.reserve.domain.member.MemberDto;
import com.marketboro.reserve.service.ItemService;
import com.marketboro.reserve.service.ReserveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ReserveController {

    private Logger logger = LoggerFactory.getLogger(ReserveController.class);
    private final ReserveService reserveService;
    private final ItemService itemService;
    private final RateReservePolicy rateReservePolicy;

    @PostMapping("/members/new")
    public void join(String email, String password) {
        MemberDto memberDto = MemberDto.builder()
                .email(email)
                .password(password)
                .build();
        reserveService.save(memberDto);
    }

    @PostMapping("/items")
    public void order(@RequestParam("memberId") Long id, @RequestParam("name") String itemName, @RequestParam("price") int price) {
        MemberDto memberDto = reserveService.findById(id);
        reserveService.updateReserve(memberDto, price);

        ItemDto itemDto = ItemDto.builder()
                        .member(new Member(memberDto))
                        .name(itemName)
                        .price(price)
                        .build();
        itemService.saveItem(itemDto);
    }

    @GetMapping("/members")
    public List<MemberDto> findMembers() {
        return reserveService.findAll();
    }

    @GetMapping("/members/{id}")
    public MemberDto findMember(@PathVariable("id") Long id) {
        MemberDto member = reserveService.findById(id);
        return member;
    }

    @GetMapping("/members/reserve")
    public void findReserveAmount() {

    }
}
