package com.lozovskiy.megamarket.repository;

import com.lozovskiy.megamarket.domain.ShopUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ShopUnitRepository extends JpaRepository<ShopUnit, String> {

}
