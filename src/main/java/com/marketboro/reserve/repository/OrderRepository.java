package com.marketboro.reserve.repository;

import com.marketboro.reserve.domain.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "select o from Order o " +
            "join fetch o.member m " +
            "where m.id = :id",
            countQuery = "select count(o) from Order o")
    Page<Order> findAll(@Param("id") Long memberId, PageRequest pageRequest);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value ="2000")})
    @Query(value = "select o from Order o " +
            "join fetch o.member m " +
            "where m.id = :id and o.itemPrice != 0 and o.reserveFund != 0 " +
            "ORDER BY o.id asc")
    Order findOne(@Param("id") Long memberId, PageRequest pageRequest);
}
