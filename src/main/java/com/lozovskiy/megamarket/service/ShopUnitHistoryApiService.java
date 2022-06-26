package com.lozovskiy.megamarket.service;

import com.lozovskiy.megamarket.domain.ShopUnitCategoryPriceDetails;
import com.lozovskiy.megamarket.domain.ShopUnitType;
import com.lozovskiy.megamarket.domain.ShopUnitsHistory;
import com.lozovskiy.megamarket.dto.ShopUnitImport;
import com.lozovskiy.megamarket.mapper.ShopUnitMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopUnitHistoryApiService {

    private final ShopUnitHistoryService shopUnitHistoryService;
    private final ShopUnitMapper shopUnitMapper;

    @Transactional
    public void addShopUnit(ShopUnitImport shopUnitImport, Instant updateDate){
        ShopUnitsHistory shopUnitsHistory = shopUnitMapper.toShopUnitHistory(shopUnitImport);
        shopUnitsHistory.setDate(updateDate);
        shopUnitHistoryService.save(shopUnitsHistory);
    }

    @Transactional
    public void addShopUnitAndUpdateRelatedCategories(ShopUnitImport shopUnitImport,
                                                      Instant updateDate,
                                                      List<ShopUnitCategoryPriceDetails> categoriesForUpdate){
        addShopUnit(shopUnitImport, updateDate);
        List<ShopUnitsHistory> shopUnitsCategoryHistory = new ArrayList<>(categoriesForUpdate.size());
        for (var categoryForUpdateHistory : categoriesForUpdate) {
            ShopUnitsHistory categoryHistory = new ShopUnitsHistory();
            categoryHistory.setUnitId(categoryForUpdateHistory.getId());
            categoryHistory.setParentId(categoryForUpdateHistory.getParentId());
            categoryHistory.setName(categoryForUpdateHistory.getName());
            categoryHistory.setType(ShopUnitType.CATEGORY);
            categoryHistory.setPrice(categoryForUpdateHistory.getPrice() / categoryForUpdateHistory.getNumberOfOffers());
            categoryHistory.setDate(updateDate);
            shopUnitsCategoryHistory.add(categoryHistory);
        }
        shopUnitHistoryService.saveAll(shopUnitsCategoryHistory);
    }

    public List<ShopUnitsHistory> getStatisticBetween(String id, Instant start, Instant end){
        return shopUnitHistoryService.getBetween(id, start, end);
    }

    public List<ShopUnitsHistory> getOfferBetween(Instant start, Instant end){
        return shopUnitHistoryService.getOfferBetween(start, end);
    }

    public void deleteByUnitId(List<String> ids){
        shopUnitHistoryService.deleteByUnitId(ids);
    }
}
