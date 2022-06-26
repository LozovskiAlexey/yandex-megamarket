package com.lozovskiy.megamarket.service;

import com.lozovskiy.megamarket.domain.ShopUnitType;
import com.lozovskiy.megamarket.domain.ShopUnitsHistory;
import com.lozovskiy.megamarket.exception.ShopUnitNotFoundException;
import com.lozovskiy.megamarket.repository.ShopUnitHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopUnitHistoryService {

    private final ShopUnitHistoryRepository shopUnitHistoryRepository;

    @Transactional(readOnly = true)
    public List<ShopUnitsHistory> getBetween(String id, Instant start, Instant end){
        if (!shopUnitHistoryRepository.existsShopUnitsHistoryByUnitId(id)){
            throw new ShopUnitNotFoundException();
        }
        return shopUnitHistoryRepository.findByUnitIdAndDateBetween(id, start, end);
    }

    @Transactional(readOnly = true)
    public List<ShopUnitsHistory> getOfferBetween(Instant start, Instant end){
        return shopUnitHistoryRepository.findByDateBetweenAndType(start, end, ShopUnitType.OFFER);
    }

    @Transactional
    public void save(ShopUnitsHistory shopUnitsHistory){
        shopUnitHistoryRepository.save(shopUnitsHistory);
    }

    @Transactional
    public void saveAll(List<ShopUnitsHistory> shopUnitsHistories){
        shopUnitHistoryRepository.saveAll(shopUnitsHistories);
    }

    @Transactional
    public void deleteByUnitId(List<String> ids) {
        shopUnitHistoryRepository.deleteAllByUnitIdIn(ids);
    }
}
