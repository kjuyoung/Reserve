package com.marketboro.reserve.service;

import com.marketboro.reserve.domain.discount.RateReservePolicy;
import com.marketboro.reserve.domain.member.Member;
import com.marketboro.reserve.domain.member.MemberDto;
import com.marketboro.reserve.repository.ReserveRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ReserveService {

    private final ReserveRepository reserveRepository;
    private final RateReservePolicy rateReservePolicy;
    private final ModelMapper modelMapper;

    @Transactional
    public void save(MemberDto memberDto) {
        Member member = Member.builder()
                            .email(memberDto.getEmail())
                            .password(memberDto.getPassword())
                            .reserve(memberDto.getReserve())
                            .build();

        reserveRepository.save(member);
    }

    public List<MemberDto> findAll() {
        List<Member> findMembers = reserveRepository.findAll();
        return findMembers.stream()
                .map(m -> new MemberDto(m.getId(), m.getEmail()))
                .collect(Collectors.toList());
    }

    public MemberDto findById(Long id) {
        Optional<Member> member = reserveRepository.findById(id);
        modelMapper.typeMap(Member.class, MemberDto.class)
                .addMappings(mapper -> {
                    mapper.skip(MemberDto::setPassword);
                    mapper.skip(MemberDto::setItems);
                    mapper.map(Member::getId, MemberDto::setId);
                    mapper.map(Member::getEmail, MemberDto::setEmail);
                    mapper.map(Member::getReserve, MemberDto::setReserve);
                });
        return modelMapper.map(member.get(), MemberDto.class);
    }

    @Transactional
    public void updateReserve(MemberDto memberDto, int price) {
        int reserve = rateReservePolicy.collect(price);
        reserve += memberDto.getReserve() + reserve;
        reserveRepository.updateReserve(memberDto.getId(), reserve);
    }
}
