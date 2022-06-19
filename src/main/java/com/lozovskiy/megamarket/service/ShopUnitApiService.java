package com.lozovskiy.megamarket.service;

import com.lozovskiy.megamarket.domain.ShopUnit;
import com.lozovskiy.megamarket.domain.ShopUnitType;
import com.lozovskiy.megamarket.domain.ShopUnitsHistory;
import com.lozovskiy.megamarket.dto.ShopUnitImport;
import com.lozovskiy.megamarket.dto.ShopUnitImportRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShopUnitApiService {

    private final static long HOURS_IN_DAY = 24;

    private final ShopUnitHistoryService shopUnitHistoryService;
    private final ShopUnitService shopUnitService;


    @Transactional(readOnly = true)
    public Optional<ShopUnit> findUnitHierarchy(String shopUnitId) {
        return shopUnitService.findById(shopUnitId);
    }

    public List<ShopUnitsHistory> getStatisticBetween(String id, ZonedDateTime start, ZonedDateTime end) {
        List<ShopUnitsHistory> historyList = shopUnitHistoryService.getBetween(id, start.toInstant(), end.toInstant());
        // todo тут сделать конвертацию в dto
        return historyList;
    }

    public List<ShopUnitsHistory> getSales(ZonedDateTime date) {
        Instant start = date.minusHours(HOURS_IN_DAY).toInstant();
        Instant end = date.toInstant();
        return shopUnitHistoryService.getOfferBetween(start, end);
    }

    @Transactional
    public void createShopUnits(ShopUnitImportRequest shopUnitsImportRequest) {
        Instant updateDate = shopUnitsImportRequest.getUpdateDate().toInstant();
        List<ShopUnitImport> shopUnitImports = shopUnitsImportRequest.getItems();
        for (ShopUnitImport shopUnitImport : shopUnitImports) {
            if (shopUnitImport.getType() == ShopUnitType.CATEGORY) {
                createCategory(shopUnitImport, updateDate);
            } else {
                createOffer(shopUnitImport, updateDate);
            }

            // todo вынести в маппер
            ShopUnitsHistory shopUnitsHistory = new ShopUnitsHistory();
            shopUnitsHistory.setUnitId(shopUnitImport.getId());
            shopUnitsHistory.setName(shopUnitImport.getName());
            shopUnitsHistory.setPrice(shopUnitImport.getPrice());
            shopUnitsHistory.setParentId(shopUnitImport.getParentId());
            shopUnitsHistory.setDate(updateDate);
            shopUnitsHistory.setType(shopUnitImport.getType());

            shopUnitHistoryService.save(shopUnitsHistory);
        }
    }

    @Transactional
    public void deleteShopUnits(String id) {
        ShopUnit shopUnit = shopUnitService.getById(id);
        Deque<ShopUnit> stack = new ArrayDeque<>();
        stack.push(shopUnit);
        List<ShopUnit> shopUnitForRemove = new ArrayList<>();
        while (!stack.isEmpty()) {
            ShopUnit currShopUnit = stack.pop();
            shopUnitForRemove.add(currShopUnit);
            stack.addAll(currShopUnit.getChildren());
        }
        List<String> shopUnitForRemoveIdList = shopUnitForRemove.stream()
                .map(ShopUnit::getId)
                .collect(Collectors.toList());
        shopUnitHistoryService.deleteByUnitId(shopUnitForRemoveIdList);
        shopUnitService.deleteAll(shopUnitForRemove);
    }

    private void createCategory(ShopUnitImport shopUnitImport, Instant date) {
        ShopUnit category = shopUnitService.findById(shopUnitImport.getId()).orElse(new ShopUnit());

        // todo mapper
        category.setId(shopUnitImport.getId());
        category.setType(shopUnitImport.getType());
        category.setName(shopUnitImport.getName());
        category.setDate(date);

        String parentId = shopUnitImport.getParentId();
        if (parentId != null) {
            Optional<ShopUnit> optionalParent = shopUnitService.findById(parentId);
            ShopUnit parent = optionalParent.orElse(null);

            if (parent != null) {
                category.setParentUnit(parent);
                Set<ShopUnit> children = parent.getChildren() != null ? parent.getChildren() : new HashSet<>();
                children.add(category);
                parent.setChildren(children);
            }
        }

        shopUnitService.save(category);
    }

    private void createOffer(ShopUnitImport shopUnitImport, Instant date) {
        ShopUnit offer = shopUnitService.findById(shopUnitImport.getId()).orElse(new ShopUnit());

        // todo mapper
        offer.setId(shopUnitImport.getId());
        offer.setType(shopUnitImport.getType());
        offer.setName(shopUnitImport.getName());
        offer.setPrice(shopUnitImport.getPrice());
        offer.setDate(date);

        String parentId = shopUnitImport.getParentId();
        if (parentId != null) {
            Optional<ShopUnit> optionalParent = shopUnitService.findById(parentId);
            ShopUnit parent = optionalParent.orElse(null);

            if (parent != null) {
                offer.setParentUnit(parent);
                parent.setChildren(null);
            }
        }
        shopUnitService.save(offer);
    }
}
