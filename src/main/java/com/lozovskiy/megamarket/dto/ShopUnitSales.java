package com.lozovskiy.megamarket.dto;

import com.lozovskiy.megamarket.domain.ShopUnitType;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.ZonedDateTime;

@Getter
@Setter
public class ShopUnitSales {
    private String id;

    private String name;

    private Instant date;

    private String parentId;

    private Integer price;

    private ShopUnitType type;
}
