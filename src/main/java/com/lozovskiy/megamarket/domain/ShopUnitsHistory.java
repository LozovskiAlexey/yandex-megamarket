package com.lozovskiy.megamarket.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "shop_unit_history")
public class ShopUnitsHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "unit_id")
    private String unitId;

    @Column(name = "date")
    private Instant date;

    @Column(name = "name")
    private String name;

    @Column(name = "parent_id")
    private String parentId;

    @Column(name = "price")
    private Integer price;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ShopUnitType type;
}
