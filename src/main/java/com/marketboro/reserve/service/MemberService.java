package com.marketboro.reserve.service;

import com.marketboro.reserve.domain.discount.RateReservePolicy;
import com.marketboro.reserve.domain.member.Member;
import com.marketboro.reserve.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final RateReservePolicy rateReservePolicy;

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Member findById(Long id) {
        Optional<Member> member = memberRepository.findById(id);
        return member.get();
    }

    public int findTotalReserve(Long id) {
        Optional<Member> member = memberRepository.findById(id);
        return member.get().getTotalReserve();
    }

    @Transactional
    public Long join(Member member) {

        memberRepository.save(member);
        return member.getId();
    }

    @Transactional
    public void updateTotalReserve(Long memberId, int price) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        int reserve = rateReservePolicy.calculateReserve(price);
        reserve += findMember.get().getTotalReserve();
        memberRepository.updateReserve(findMember.get().getId(), reserve);
    }
}
