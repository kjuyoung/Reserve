package com.marketboro.reserve.repository;

import com.marketboro.reserve.domain.order.Order;
import com.marketboro.reserve.domain.reserve.Reserve;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReserveRepository extends JpaRepository<Reserve, Long> {

    @Query(value = "select r from Reserve r " +
            "join fetch r.member m " +
            "where m.id = :id",
            countQuery = "select count(r) from Reserve r")
    Page<Reserve> findAll(@Param("id") Long memberId, PageRequest pageRequest);
}
