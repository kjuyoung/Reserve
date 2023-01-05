package com.marketboro.reserve.domain.member;

import com.marketboro.reserve.domain.item.Item;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "member")
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    private List<Item> items = new ArrayList<>();

    private Long reserve;

    @Builder
    public Member(Long id, String email, String password, List<Item> items, Long reserve) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.items = items;
        this.reserve = reserve;
    }

    public Member (MemberDto memberDto) {
        this.id = memberDto.getId();
        this.email = memberDto.getEmail();
        this.password = memberDto.getPassword();
        this.items = memberDto.getItems();
        this.reserve = memberDto.getReserve();
    }
}
