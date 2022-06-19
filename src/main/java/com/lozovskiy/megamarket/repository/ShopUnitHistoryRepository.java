package com.lozovskiy.megamarket.repository;

import com.lozovskiy.megamarket.domain.ShopUnit;
import com.lozovskiy.megamarket.domain.ShopUnitType;
import com.lozovskiy.megamarket.domain.ShopUnitsHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ShopUnitHistoryRepository extends JpaRepository<ShopUnitsHistory, String> {

    List<ShopUnitsHistory> findByUnitIdAndDateBetween(String id, Instant start, Instant end);

    List<ShopUnitsHistory> findByDateBetweenAndType(Instant start, Instant end, ShopUnitType type);

    void deleteAllByUnitIdIn(List<String> ids);
}
