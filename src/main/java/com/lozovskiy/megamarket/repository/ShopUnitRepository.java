package com.lozovskiy.megamarket.repository;

import com.lozovskiy.megamarket.domain.ShopUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ShopUnitRepository extends JpaRepository<ShopUnit, String> {

    @Query(nativeQuery = true,
            value = "select unit " +
                    "from ShopUnit unit " +
                    "inner join unit.parentUnit p " +
                    "where p.id = :parentId")
    List<ShopUnit> findShopUnitsByParentId(@Param("parentId") String parentId);

}
