package com.lozovskiy.megamarket.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "shop_unit")
public class ShopUnit {
    @Id
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "date", nullable = false)
    private Instant date;

    @ManyToOne(targetEntity = ShopUnit.class)
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private ShopUnit parentUnit;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ShopUnitType type;

    @Column(name = "price")
    private Integer price;

    @Column(name = "children")
    @OneToMany(mappedBy = "parentUnit", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Set<ShopUnit> children;

    @JsonIgnore
    public ShopUnit getParentUnit() {
        return parentUnit;
    }
}
