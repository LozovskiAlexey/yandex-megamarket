package com.lozovskiy.megamarket.service;

import com.lozovskiy.megamarket.domain.ShopUnit;
import com.lozovskiy.megamarket.domain.ShopUnitCategoryPriceDetails;
import com.lozovskiy.megamarket.dto.ShopUnitImport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShopUnitApiService {

    private final ShopUnitService shopUnitService;
    private final ShopUnitCategoryPriceDetailsApiService shopUnitCategoryPriceDetailsApiService;
    private final ValidationService validationService;

    @Transactional
    public void addCategory(ShopUnitImport shopUnitImport, Instant updateDate){
        validationService.validateCategoryPrice(shopUnitImport);
        ShopUnit category = shopUnitService.findById(shopUnitImport.getId()).orElse(new ShopUnit());
        updateCategory(category, shopUnitImport, updateDate);
        String parentId = shopUnitImport.getParentId();
        if (parentId != null) {
            Optional<ShopUnit> optionalParent = shopUnitService.findById(parentId);
            if (optionalParent.isPresent()){
                ShopUnit parent = optionalParent.get();
                category.setParentId(parent);
                Set<ShopUnit> children = parent.getChildren() != null ? parent.getChildren() : new HashSet<>();
                children.add(category);
                parent.setChildren(children);
            }
        }
        shopUnitService.save(category);
        shopUnitCategoryPriceDetailsApiService.addCategory(shopUnitImport);
    }

    @Transactional
    public List<ShopUnitCategoryPriceDetails> addOffer(ShopUnitImport shopUnitImport, Instant updateDate) {
        validationService.validateOfferPrice(shopUnitImport);
        ShopUnit offer = shopUnitService.findById(shopUnitImport.getId()).orElse(new ShopUnit());
        updateOffer(offer, shopUnitImport, updateDate);
        String parentId = shopUnitImport.getParentId();
        if (parentId != null) {
            Optional<ShopUnit> optionalParent = shopUnitService.findById(parentId);
            if (optionalParent.isPresent()){
                ShopUnit parent = optionalParent.get();
                offer.setParentId(parent);
                parent.setChildren(null);
            }
        }
        shopUnitService.save(offer);
        List<ShopUnitCategoryPriceDetails> categoriesForUpdate = shopUnitCategoryPriceDetailsApiService.addOffer(shopUnitImport);
        updateAvgPriceAndDateForCategories(categoriesForUpdate, updateDate);
        return categoriesForUpdate;
    }

    @Transactional
    public void updateAvgPriceAndDateForCategories(List<ShopUnitCategoryPriceDetails> categoriesForUpdate, Instant updateDate) {
        List<String> categoriesForUpdateIds = categoriesForUpdate.stream()
                .map(ShopUnitCategoryPriceDetails::getId)
                .collect(Collectors.toList());
        Map<String, Integer> categoryIdAvgPriceMap = categoriesForUpdate.stream()
                .collect(Collectors.toMap(
                        ShopUnitCategoryPriceDetails::getId,
                        category -> category.getPrice() / category.getNumberOfOffers()
                        )
                );
        List<ShopUnit> currCategoriesForUpdate = shopUnitService.findByIds(categoriesForUpdateIds);
        currCategoriesForUpdate.forEach(category -> {
            category.setPrice(categoryIdAvgPriceMap.get(category.getId()));
            category.setDate(updateDate);
        });

        shopUnitService.saveAll(currCategoriesForUpdate);
    }

    @Transactional(readOnly = true)
    public ShopUnit findUnitHierarchy(String shopUnitId){
            return shopUnitService.getById(shopUnitId);
        }


    @Transactional
    public List<ShopUnit> deleteShopUnit(String id){
        List<ShopUnit> shopUnitForRemove;
        ShopUnit shopUnit = shopUnitService.getById(id);
        Deque<ShopUnit> stack = new ArrayDeque<>();
        stack.push(shopUnit);
        shopUnitForRemove = new ArrayList<>();
        while (!stack.isEmpty()) {
            ShopUnit currShopUnit = stack.pop();
            shopUnitForRemove.add(currShopUnit);
            if (currShopUnit.getChildren() != null) {
                stack.addAll(currShopUnit.getChildren());
            }
        }
        shopUnitService.deleteAll(shopUnitForRemove);
        shopUnitCategoryPriceDetailsApiService.deleteShopUnits(shopUnitForRemove);

        return shopUnitForRemove;
    }

    private void updateCategory(ShopUnit categoryToUpdate, ShopUnitImport updateData, Instant updateDate){
        categoryToUpdate.setId(updateData.getId());
        categoryToUpdate.setType(updateData.getType());
        categoryToUpdate.setName(updateData.getName());
        categoryToUpdate.setDate(updateDate);
    }

    private void updateOffer(ShopUnit offerToUpdate, ShopUnitImport updateData, Instant updateDate){
        offerToUpdate.setId(updateData.getId());
        offerToUpdate.setType(updateData.getType());
        offerToUpdate.setName(updateData.getName());
        offerToUpdate.setPrice(updateData.getPrice());
        offerToUpdate.setDate(updateDate);
    }
}
