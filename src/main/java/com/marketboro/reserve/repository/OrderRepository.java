package com.marketboro.reserve.repository;

import com.marketboro.reserve.domain.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "select o from Order o " +
            "join fetch o.member m " +
            "where m.id = :id",
            countQuery = "select count(o) from Order o")
    Page<Order> findAll(@Param("id") Long memberId, PageRequest pageRequest);

    @Query(value = "select o from Order o " +
            "where o.itemPrice != 0 and o.reserveFund != 0 " +
            "ORDER BY o.id asc")
    Order findOne(PageRequest pageRequest);
}
