package com.marketboro.reserve.repository;

import com.marketboro.reserve.domain.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
