package com.marketboro.reserve.controller;

import com.marketboro.reserve.domain.discount.RateReservePolicy;
import com.marketboro.reserve.domain.order.OrderDto;
import com.marketboro.reserve.domain.member.Member;
import com.marketboro.reserve.domain.member.MemberDto;
import com.marketboro.reserve.service.OrderService;
import com.marketboro.reserve.service.ReserveService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReserveController {

    private Logger logger = LoggerFactory.getLogger(ReserveController.class);
    private final ReserveService reserveService;
    private final OrderService orderService;
    private final RateReservePolicy rateReservePolicy;
    private final ModelMapper modelMapper;

    @PostMapping("/members/new")
    public String create(String name) {
        Member member = Member.builder()
                .name(name)
                .reserve(0)
                .build();
        reserveService.join(member);
        return "redirect:/";
    }

    @PostMapping("/orders")
    public void order(@RequestParam("memberId") Long memberId
                    , @RequestParam("itemName") String itemName
                    , @RequestParam("itemPrice") int itemPrice) {

        orderService.order(memberId, itemName, itemPrice);
    }

    @GetMapping("/orders")
    public List<OrderDto> findOrders() {
        return orderService.findAll();
    }

    @GetMapping("/members")
    public List<MemberDto> findMembers() {
        List<Member> findMembers = reserveService.findAll();
        return findMembers.stream()
                .map(m -> new MemberDto(m.getId(), m.getName(), m.getReserve()))
                .collect(Collectors.toList());
    }

    @GetMapping("/members/{id}")
    public MemberDto findMember(@PathVariable("id") Long id) {
        Member member = reserveService.findById(id);
        modelMapper.typeMap(Member.class, MemberDto.class)
                .addMappings(mapper -> {
                    mapper.skip(MemberDto::setOrders);
                    mapper.map(Member::getId, MemberDto::setId);
                    mapper.map(Member::getName, MemberDto::setName);
                    mapper.map(Member::getReserve, MemberDto::setReserve);
                });
        return modelMapper.map(member, MemberDto.class);
    }

    @GetMapping("/members/{id}/reserves")
    public int findTotalReserve(@PathVariable("id") Long id) {
        return reserveService.findTotalReserve(id);
    }

    @Data
    static class CreateMemberRequest {
        private String name;
        private int reserve;
    }
    @Data
    static class CreateMemberResponse {
        private Long id;
        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}
