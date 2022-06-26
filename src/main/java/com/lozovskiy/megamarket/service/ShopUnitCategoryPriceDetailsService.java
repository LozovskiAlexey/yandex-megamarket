package com.lozovskiy.megamarket.service;

import com.lozovskiy.megamarket.domain.ShopUnitCategoryPriceDetails;
import com.lozovskiy.megamarket.repository.ShopUnitCategoryPriceDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShopUnitCategoryPriceDetailsService {

    private final ShopUnitCategoryPriceDetailsRepository repository;

    @Transactional(readOnly = true)
    public Optional<ShopUnitCategoryPriceDetails> findById(String id) {
        if (id == null) {
            return Optional.empty();
        }
        return repository.findById(id); }

    @Transactional
    public void save(ShopUnitCategoryPriceDetails shopUnitCategoryPriceDetails) { repository.save(shopUnitCategoryPriceDetails); }

    @Transactional
    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
