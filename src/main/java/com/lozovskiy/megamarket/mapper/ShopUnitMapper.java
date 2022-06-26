package com.lozovskiy.megamarket.mapper;

import com.lozovskiy.megamarket.domain.ShopUnit;
import com.lozovskiy.megamarket.domain.ShopUnitsHistory;
import com.lozovskiy.megamarket.dto.ShopUnitImport;
import com.lozovskiy.megamarket.dto.ShopUnitSales;
import com.lozovskiy.megamarket.dto.ShopUnitStatistics;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ShopUnitMapper {
    @Mappings({
            @Mapping(target="id", source="shopUnitsHistory.unitId")
    })
    ShopUnitStatistics toShopUnitStatistics(ShopUnitsHistory shopUnitsHistory);

    @Mappings({
            @Mapping(target="id", source="shopUnitsHistory.unitId")
    })
    ShopUnitSales toShopUnitSales(ShopUnitsHistory shopUnitsHistory);

    @Mappings({
            @Mapping(target="unitId", source="shopUnitImport.id"),
            @Mapping(ignore = true, target = "id")
    })
    ShopUnitsHistory toShopUnitHistory(ShopUnitImport shopUnitImport);
}
