package com.marketboro.reserve.domain.reserve;

import lombok.Getter;

@Getter
public class ReserveDto {

    private Long id;
    private int usedReserve;

    public ReserveDto(Long id, int usedReserve) {
        this.id = id;
        this.usedReserve = usedReserve;
    }
}
