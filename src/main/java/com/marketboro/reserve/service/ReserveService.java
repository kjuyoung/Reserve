package com.marketboro.reserve.service;

import com.marketboro.reserve.domain.discount.RateReservePolicy;
import com.marketboro.reserve.domain.member.Member;
import com.marketboro.reserve.domain.member.MemberDto;
import com.marketboro.reserve.repository.ReserveRepository;
import org.modelmapper.ModelMapper;
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
    private final RateReservePolicy rateReservePolicy;

    public List<Member> findAll() {
        return reserveRepository.findAll();
    }

    public Member findById(Long id) {
        Optional<Member> member = reserveRepository.findById(id);
        return member.get();
    }

    public int findTotalReserve(Long id) {
        Optional<Member> member = reserveRepository.findById(id);
        return member.get().getReserve();
    }

    @Transactional
    public Long join(Member member) {

        reserveRepository.save(member);
        return member.getId();
    }

    @Transactional
    public void updateReserve(Member member, int price) {
        int reserve = rateReservePolicy.collect(price);
        reserve += member.getReserve();
        reserveRepository.updateReserve(member.getId(), reserve);
    }
}
