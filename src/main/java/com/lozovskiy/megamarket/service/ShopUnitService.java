package com.lozovskiy.megamarket.service;

import com.lozovskiy.megamarket.domain.ShopUnit;
import com.lozovskiy.megamarket.repository.ShopUnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ShopUnitService {

    private final ShopUnitRepository shopUnitRepository;

    @Transactional(readOnly = true)
    public ShopUnit getById(String id) {
        return shopUnitRepository.getById(id);
    }

    @Transactional(readOnly = true)
    public Optional<ShopUnit> findById(String id) {
        return shopUnitRepository.findById(id);
    }

    @Transactional
    public void save(ShopUnit shopUnit) {
        shopUnitRepository.save(shopUnit);
    }

    @Transactional
    public void deleteAll(List<ShopUnit> shopUnitList) {
        shopUnitRepository.deleteAll(shopUnitList);
    }
}
