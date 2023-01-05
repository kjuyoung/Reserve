package com.marketboro.reserve.repository;

import com.marketboro.reserve.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReserveRepository extends JpaRepository<Member, Long> {
}
