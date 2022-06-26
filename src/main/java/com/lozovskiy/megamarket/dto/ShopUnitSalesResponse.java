package com.lozovskiy.megamarket.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ShopUnitSalesResponse {
    List<ShopUnitSales> items;
}
