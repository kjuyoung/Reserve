package com.marketboro.reserve.service;

import com.marketboro.reserve.domain.item.Item;
import com.marketboro.reserve.domain.item.ItemDto;
import com.marketboro.reserve.repository.ItemRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public void saveItem(ItemDto itemDto) {
        Item item = Item.builder()
                .member(itemDto.getMember())
                .name(itemDto.getName())
                .price(itemDto.getPrice())
                .build();
        itemRepository.save(item);
    }
}
