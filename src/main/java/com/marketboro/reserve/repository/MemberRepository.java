package com.marketboro.reserve.repository;

import com.marketboro.reserve.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Member m set m.totalReserve = :reserve where m.id = :id")
    void updateReserve(@Param("id") Long id, @Param("reserve") int reserve);
}
