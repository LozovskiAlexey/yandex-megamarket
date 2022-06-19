package com.lozovskiy.megamarket.domain;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "shop_unit")
public class ShopUnitModel {
    @Id
    @Column(name="id", nullable = false)
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private String id;

    // name
    @Column(name="name", nullable = false)
    private String name;

    // date
    @Column(name="date", nullable = false)
    private String date;

    // parentId
    @ManyToOne
    @JoinColumn(name = "parentId")
    private ShopUnitModel parentNode;
    //private String parentId;

    // type
    @Column(name="type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ShopUnitType type;

    // price
    @Column(name="price")
    private Integer price;

    
}
