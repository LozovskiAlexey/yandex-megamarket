package com.lozovskiy.megamarket.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name="shop_unit_category_price_details")
public class ShopUnitCategoryPriceDetails {
    @Id
    private String id;

    @Column(name = "parent_id")
    private String parentId;

    @Column(name = "price")
    private Integer price;

    @Column(name = "number_of_offers")
    private Integer numberOfOffers;

    @Column(name = "name")
    private String name;
}
