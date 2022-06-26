package com.lozovskiy.megamarket.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lozovskiy.megamarket.domain.ShopUnitType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ShopUnitImport {

    @NotNull
    @JsonFormat(pattern = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
    private String id;

    @NotNull
    private String name;

    private String parentId;

    @NotNull
    private ShopUnitType type;

    private Integer price;
}