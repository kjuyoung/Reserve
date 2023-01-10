package com.marketboro.reserve.controller;

import com.marketboro.reserve.domain.member.Member;
import com.marketboro.reserve.domain.member.MemberDto;
import com.marketboro.reserve.domain.order.OrderDto;
import com.marketboro.reserve.domain.reserve.ReserveDto;
import com.marketboro.reserve.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원별 적립금 합계 조회 API
     * @param memberId
     * @return
     */
    @GetMapping("/members/{id}/total-reserve")
    public ResponseEntity<MemberDto> findTotalReserve(@PathVariable("id") Long memberId) {

        return ResponseEntity.ok(memberService.findTotalReserve(memberId));
    }

    /**
     * 회원별 적립금 적립 API
     * @param memberId
     * @param itemName
     * @param itemPrice
     */
    @PostMapping("/members/{id}/reserve")
    public void order(@RequestParam("memberId") Long memberId
            , @RequestParam("itemName") String itemName
            , @RequestParam("itemPrice") int itemPrice) {

        memberService.saveReserve(memberId, itemName, itemPrice);
    }

    /**
     * 회원별 적립금 적립 내역 조회 API
     * @param memberId
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/members/{id}/reserve/usage-details")
    public ResponseEntity<List<OrderDto>> historySaveReserve(@RequestParam Long memberId,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());

        return ResponseEntity.ok(memberService.historySaveReserve(memberId, pageRequest));
    }

    /**
     * 회원별 적립금 사용 API
     * @param memberId
     */
    @PutMapping("/members/{id}/reserve")
    public void useReserve(@PathVariable("id") Long memberId) {
        memberService.useReserve(memberId);
    }

    /**
     * 회원별 적립금 사용 내역 조회 API
     * @param memberId
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/members/{id}/reserve/accumulated-details")
    public ResponseEntity<List<ReserveDto>> historyUseReserve(@RequestParam Long memberId,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());

        return ResponseEntity.ok(memberService.historyUseReserve(memberId, pageRequest));
    }
}
