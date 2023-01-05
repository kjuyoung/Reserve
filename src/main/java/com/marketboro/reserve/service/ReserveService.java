package com.marketboro.reserve.service;

import com.marketboro.reserve.domain.item.Item;
import com.marketboro.reserve.domain.item.ItemDto;
import com.marketboro.reserve.domain.member.Member;
import com.marketboro.reserve.domain.member.MemberDto;
import com.marketboro.reserve.repository.ItemRepository;
import com.marketboro.reserve.repository.ReserveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//@RequiredArgsConstructor
@Service
public class ReserveService {

    private final ReserveRepository reserveRepository;
    private final ItemRepository itemRepository;

    public ReserveService(ReserveRepository reserveRepository,
                          ItemRepository itemRepository) {
        this.reserveRepository = reserveRepository;
        this.itemRepository = itemRepository;
    }

    public void save(MemberDto memberDto) {
        Member member = Member.builder()
                            .email(memberDto.getEmail())
                            .password(memberDto.getPassword())
                            .build();

        reserveRepository.save(member);
    }

    public void saveItem(ItemDto itemDto) {
        Item item = Item.builder()
                .member(itemDto.getMember())
                .name(itemDto.getName())
                .price(itemDto.getPrice())
                .build();
        itemRepository.save(item);
    }

    public List<MemberDto> findAll() {
        List<Member> findMembers = reserveRepository.findAll();
        return findMembers.stream()
                .map(m -> new MemberDto(m.getId(), m.getEmail()))
                .collect(Collectors.toList());
    }

    public MemberDto findById(Long id) {
        Optional<Member> member = reserveRepository.findById(id);
        return member.stream().map(m -> new MemberDto(m.getId(), m.getEmail(), m.getItems(), m.getReserve())).findAny().get();
    }
}
