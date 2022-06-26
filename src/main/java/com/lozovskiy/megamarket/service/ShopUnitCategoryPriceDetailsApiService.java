package com.lozovskiy.megamarket.service;

import com.lozovskiy.megamarket.domain.ShopUnit;
import com.lozovskiy.megamarket.domain.ShopUnitCategoryPriceDetails;
import com.lozovskiy.megamarket.domain.ShopUnitType;
import com.lozovskiy.megamarket.dto.ShopUnitImport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShopUnitCategoryPriceDetailsApiService {

    private final ShopUnitCategoryPriceDetailsService shopUnitCategoryPriceDetailsService;

    public void addCategory(ShopUnitImport category){
        ShopUnitCategoryPriceDetails shopUnitCategoryPriceDetails = new ShopUnitCategoryPriceDetails();
        shopUnitCategoryPriceDetails.setId(category.getId());
        shopUnitCategoryPriceDetails.setPrice(0);
        shopUnitCategoryPriceDetails.setParentId(category.getParentId());
        shopUnitCategoryPriceDetails.setNumberOfOffers(0);
        shopUnitCategoryPriceDetails.setName(category.getName());
        shopUnitCategoryPriceDetailsService.save(shopUnitCategoryPriceDetails);
    }

    public List<ShopUnitCategoryPriceDetails> addOffer(ShopUnitImport offer){
        Optional<ShopUnitCategoryPriceDetails> categoryOpt = shopUnitCategoryPriceDetailsService.findById(offer.getParentId());
        List<ShopUnitCategoryPriceDetails> categories = new ArrayList<>();
        while (categoryOpt.isPresent()) {
            ShopUnitCategoryPriceDetails category = categoryOpt.get();
            category.setPrice(category.getPrice() + offer.getPrice());
            category.setNumberOfOffers(category.getNumberOfOffers() + 1);
            categories.add(category);
            categoryOpt = shopUnitCategoryPriceDetailsService.findById(category.getParentId());
        }
        return categories;
    }


    public void deleteShopUnits(List<ShopUnit> shopUnits) {
        for (ShopUnit shopUnit : shopUnits) {
            if (shopUnit.getType() == ShopUnitType.CATEGORY) {
                deleteCategory(shopUnit);
                continue;
            }
            if (shopUnit.getType() == ShopUnitType.OFFER) {
                deleteOffer(shopUnit);
            }
        }
    }
    private void deleteCategory(ShopUnit category) {
        Optional<ShopUnitCategoryPriceDetails> categoryForRemoveOpt = shopUnitCategoryPriceDetailsService.findById(category.getId());
        if (categoryForRemoveOpt.isEmpty()) {
             return;
        }
        ShopUnitCategoryPriceDetails categoryForRemove = categoryForRemoveOpt.get();
        int price = categoryForRemove.getPrice();
        int numberOfOffers = categoryForRemove.getNumberOfOffers();
        Optional<ShopUnitCategoryPriceDetails> currCategoryOpt = shopUnitCategoryPriceDetailsService.findById(categoryForRemove.getParentId());
        while (currCategoryOpt.isPresent()) {
            ShopUnitCategoryPriceDetails currCategory = currCategoryOpt.get();
            currCategory.setPrice(currCategory.getPrice() - price);
            currCategory.setNumberOfOffers(currCategory.getNumberOfOffers() - numberOfOffers);

            currCategoryOpt = shopUnitCategoryPriceDetailsService.findById(currCategory.getParentId());
        }
        shopUnitCategoryPriceDetailsService.deleteById(categoryForRemove.getId());
    }

    private void deleteOffer(ShopUnit offer) {
        int price = offer.getPrice();
        Optional<ShopUnitCategoryPriceDetails> currCategoryOpt = shopUnitCategoryPriceDetailsService.findById(offer.getParentId());
        while (currCategoryOpt.isPresent()) {
            ShopUnitCategoryPriceDetails currCategory = currCategoryOpt.get();
            currCategory.setPrice(currCategory.getPrice() - price);
            currCategory.setNumberOfOffers(currCategory.getNumberOfOffers() - 1);
            currCategoryOpt = shopUnitCategoryPriceDetailsService.findById(currCategory.getParentId());
        }
    }
}
