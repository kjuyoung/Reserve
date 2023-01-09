package com.marketboro.reserve.repository;

import com.marketboro.reserve.domain.member.Member;
import com.marketboro.reserve.domain.order.Order;
import com.marketboro.reserve.domain.reserve.Reserve;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("Update Member m set m.totalReserve = :reserve where m.id = :id")
    void updateReserve(@Param("id") Long id, @Param("reserve") int reserve);
}
