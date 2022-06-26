package com.lozovskiy.megamarket.service;

import com.lozovskiy.megamarket.domain.ShopUnitType;
import com.lozovskiy.megamarket.dto.ShopUnitImport;
import com.lozovskiy.megamarket.exception.ShopUnitImportInvalidPriceException;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {

    public void validateCategoryPrice(ShopUnitImport category){
        if (category.getType() == ShopUnitType.CATEGORY){
            if (category.getPrice() != null){
                throw new ShopUnitImportInvalidPriceException();
            }
        }
    }

    public void validateOfferPrice(ShopUnitImport offer){
        if (offer.getType() == ShopUnitType.OFFER){
            if (offer.getPrice() == null || offer.getPrice() < 0){
                throw new ShopUnitImportInvalidPriceException();
            }
        }
    }
}
