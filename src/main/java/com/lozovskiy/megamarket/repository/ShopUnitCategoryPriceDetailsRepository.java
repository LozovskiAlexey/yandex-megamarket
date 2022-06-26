package com.lozovskiy.megamarket.repository;

import com.lozovskiy.megamarket.domain.ShopUnitCategoryPriceDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopUnitCategoryPriceDetailsRepository extends JpaRepository<ShopUnitCategoryPriceDetails, String> {

}
