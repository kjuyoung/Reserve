package com.marketboro.reserve.repository;

import com.marketboro.reserve.domain.reserve.Reserve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReserveRepository extends JpaRepository<Reserve, Long> {

    @Query("select rv from Reserve rv")
    List<Reserve> findAllReserves(@Param("memberId") Long memberId);
}
