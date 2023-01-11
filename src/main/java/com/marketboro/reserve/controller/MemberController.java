package com.marketboro.reserve.controller;

import com.marketboro.reserve.domain.member.MemberDto;
import com.marketboro.reserve.domain.order.OrderDto;
import com.marketboro.reserve.domain.reserve.ReserveDto;
import com.marketboro.reserve.service.MemberService;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.local.LocalBucketBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;
import io.github.bucket4j.Bucket;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class MemberController {

    private final MemberService memberService;
    private final Bucket bucket;

    public MemberController(MemberService memberService) {

        // 1초에 5개 요청 허용, 1분에 50개 요청 허용
        Bandwidth minuteBandwidth = Bandwidth.simple(50, Duration.ofMinutes(1));
        Bandwidth secondBandwidth = Bandwidth.simple(5, Duration.ofSeconds(1));

        LocalBucketBuilder bucketBuilder = Bucket.builder();
        bucketBuilder.addLimit(minuteBandwidth);
        bucketBuilder.addLimit(secondBandwidth);

        this.bucket = bucketBuilder.build();
        this.memberService = memberService;
    }

    /**
     * 회원별 적립금 합계 조회 API
     * @param memberId
     * @return
     */
    @GetMapping("/members/{id}/total-reserve")
    public ResponseEntity<MemberDto> findTotalReserve(@PathVariable("id") Long memberId) {

        if (bucket.tryConsume(1)) {
            return ResponseEntity.ok(memberService.findTotalReserve(memberId));
        }

        return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
    }

    /**
     * 회원별 적립금 적립 API
     * @param memberId
     * @param itemName
     * @param itemPrice
     */
    @PostMapping("/members/{id}/reserve")
    public ResponseEntity<String> order(@RequestParam("memberId") Long memberId
            , @RequestParam("itemName") String itemName
            , @RequestParam("itemPrice") int itemPrice) {

        if (bucket.tryConsume(1)) {
            memberService.saveReserve(memberId, itemName, itemPrice);
            return ResponseEntity.ok("Request success");
        }

        return new ResponseEntity<>("Too many requests", HttpStatus.TOO_MANY_REQUESTS);
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

        if (bucket.tryConsume(1)) {
            PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
            return ResponseEntity.ok(memberService.historySaveReserve(memberId, pageRequest));
        }

        return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
    }

    /**
     * 회원별 적립금 사용 API
     * @param memberId
     */
    @PutMapping("/members/{id}/reserve")
    public ResponseEntity<String> useReserve(@PathVariable("id") Long memberId) {

        if (bucket.tryConsume(1)) {
            memberService.useReserve(memberId);
            return ResponseEntity.ok("Request success");
        }

        return new ResponseEntity<>("Too many requests", HttpStatus.TOO_MANY_REQUESTS);
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

        if (bucket.tryConsume(1)) {
            PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
            return ResponseEntity.ok(memberService.historyUseReserve(memberId, pageRequest));
        }

        return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
    }
}
