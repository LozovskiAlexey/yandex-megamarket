package com.lozovskiy.megamarket.dto;

import com.lozovskiy.megamarket.domain.ShopUnitType;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class ShopUnitImport {

    private String id;

    @NonNull
    private String name;

    private String parentId;

    @NonNull
    private ShopUnitType type;

    private Integer price;
}