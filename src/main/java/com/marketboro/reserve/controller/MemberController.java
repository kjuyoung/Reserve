package com.marketboro.reserve.controller;

import com.marketboro.reserve.domain.discount.RateReservePolicy;
import com.marketboro.reserve.domain.member.Member;
import com.marketboro.reserve.domain.member.MemberDto;
import com.marketboro.reserve.domain.reserve.Reserve;
import com.marketboro.reserve.service.MemberService;
import com.marketboro.reserve.service.ReserveService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MemberController {

    private final ReserveService reserveService;
    private final MemberService memberService;
    private final ModelMapper modelMapper;

    @PostMapping("/members/new")
    public String create(String name) {
        Member member = Member.builder()
                .name(name)
                .build();
        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/members")
    public List<MemberDto> findMembers() {
        List<Member> findMembers = memberService.findAll();
        return findMembers.stream()
                .map(m -> new MemberDto(m.getId(), m.getName(), m.getTotalReserve()))
                .collect(Collectors.toList());
    }

    @GetMapping("/members/{id}")
    public MemberDto findMember(@PathVariable("id") Long id) {
        Member member = memberService.findById(id);
        modelMapper.typeMap(Member.class, MemberDto.class)
                .addMappings(mapper -> {
                    mapper.skip(MemberDto::setOrders);
                    mapper.map(Member::getId, MemberDto::setId);
                    mapper.map(Member::getName, MemberDto::setName);
                });
        return modelMapper.map(member, MemberDto.class);
    }

    @GetMapping("/members/{id}/reserve")
    public int findTotalReserve(@PathVariable("id") Long id) {
        return memberService.findTotalReserve(id);
    }

    @GetMapping("/members/{id}/reserves")
    public List<Reserve> findReserves(@PathVariable("id") Long memberId) {
        return reserveService.findAllReserves(memberId);
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
