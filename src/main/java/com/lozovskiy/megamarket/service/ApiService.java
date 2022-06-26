package com.lozovskiy.megamarket.service;

import com.lozovskiy.megamarket.domain.ShopUnit;
import com.lozovskiy.megamarket.domain.ShopUnitCategoryPriceDetails;
import com.lozovskiy.megamarket.domain.ShopUnitType;
import com.lozovskiy.megamarket.domain.ShopUnitsHistory;
import com.lozovskiy.megamarket.dto.*;
import com.lozovskiy.megamarket.mapper.ShopUnitMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApiService {

    private final static long HOURS_IN_DAY = 24;

    private final ShopUnitHistoryApiService shopUnitHistoryApiService;
    private final ShopUnitApiService shopUnitApiService;
    private final ShopUnitMapper shopUnitMapper;

    public ShopUnit findUnitHierarchy(String shopUnitId) {
        return shopUnitApiService.findUnitHierarchy(shopUnitId);
    }

    @Transactional(readOnly = true)
    public ShopUnitStatisticsResponse getStatisticBetween(String id, ZonedDateTime start, ZonedDateTime end) {
        List<ShopUnitsHistory> historyList = shopUnitHistoryApiService.getStatisticBetween(id, start.toInstant(), end.toInstant());
        List<ShopUnitStatistics> historyListDto = historyList
                .stream()
                .map(shopUnitMapper::toShopUnitStatistics)
                .collect(Collectors.toList());
        ShopUnitStatisticsResponse shopUnitStatisticsResponse = new ShopUnitStatisticsResponse();
        shopUnitStatisticsResponse.setItems(historyListDto);
        return shopUnitStatisticsResponse;
    }

    @Transactional(readOnly = true)
    public ShopUnitSalesResponse getSales(ZonedDateTime date) {
        Instant start = date.minusHours(HOURS_IN_DAY).toInstant();
        Instant end = date.toInstant();
        List<ShopUnitsHistory> salesList = shopUnitHistoryApiService.getOfferBetween(start, end);
        List<ShopUnitSales> salesListDTO = salesList
                .stream()
                .map(shopUnitMapper::toShopUnitSales)
                .collect(Collectors.toList());
        ShopUnitSalesResponse shopUnitSalesResponse = new ShopUnitSalesResponse();
        shopUnitSalesResponse.setItems(salesListDTO);
        return shopUnitSalesResponse;
    }

    @Transactional
    public void createShopUnits(ShopUnitImportRequest shopUnitsImportRequest) {
        Instant updateDate = shopUnitsImportRequest.getUpdateDate().toInstant();
        List<ShopUnitImport> shopUnitImports = shopUnitsImportRequest.getItems();
        List<ShopUnitCategoryPriceDetails> categoriesForUpdateHistory = List.of();
        for (ShopUnitImport shopUnitImport : shopUnitImports) {
            if (shopUnitImport.getType() == ShopUnitType.CATEGORY) {
                shopUnitApiService.addCategory(shopUnitImport, updateDate);
                shopUnitHistoryApiService.addShopUnit(shopUnitImport, updateDate);
            }
            if (shopUnitImport.getType() == ShopUnitType.OFFER){
                categoriesForUpdateHistory = shopUnitApiService.addOffer(shopUnitImport, updateDate);
                shopUnitHistoryApiService.addShopUnitAndUpdateRelatedCategories(shopUnitImport, updateDate, categoriesForUpdateHistory);
            }
        }
    }

    @Transactional
    public void deleteShopUnits(String id) {
        List<ShopUnit> shopUnitForRemove = shopUnitApiService.deleteShopUnit(id);
        if (shopUnitForRemove != null) {
            List<String> shopUnitForRemoveIdList = shopUnitForRemove.stream()
                    .map(ShopUnit::getId)
                    .collect(Collectors.toList());
            shopUnitHistoryApiService.deleteByUnitId(shopUnitForRemoveIdList);
        }
    }
}
