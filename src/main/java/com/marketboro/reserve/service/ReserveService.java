package com.marketboro.reserve.service;

import com.marketboro.reserve.domain.discount.RateReservePolicy;
import com.marketboro.reserve.domain.member.Member;
import com.marketboro.reserve.domain.order.Order;
import com.marketboro.reserve.domain.reserve.Reserve;
import com.marketboro.reserve.repository.MemberRepository;
import com.marketboro.reserve.repository.ReserveRepository;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReserveService {

    private final ReserveRepository reserveRepository;
    private final MemberRepository memberRepository;
    private final RateReservePolicy rateReservePolicy;

    public List<Reserve> findAllReserves(Long memberId) {
        List<Reserve> findReserves = reserveRepository.findAllReserves(memberId);
        return findReserves;
    }

//    @Transactional
//    public void save(Long memberId, int itemPrice) {
//        Optional<Member> findMember = memberRepository.findById(memberId);
//        int amount = rateReservePolicy.calculateReserve(itemPrice);
//        Reserve reserve = Reserve.createReserve(findMember.get(), amount);
//
//        reserveRepository.save(reserve);
//    }
}
