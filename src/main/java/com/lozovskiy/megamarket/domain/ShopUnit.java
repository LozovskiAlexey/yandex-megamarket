package com.lozovskiy.megamarket.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "shop_unit")
public class ShopUnit {

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ShopUnitType type;

    @Column(name = "name", nullable = false)
    private String name;

    @Id
    private String id;

    @ManyToOne(targetEntity = ShopUnit.class)
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private ShopUnit parentId;

    @Column(name = "price")
    private Integer price;

    @Column(name = "date", nullable = false)
    private Instant date;

    @Column(name = "children")
    @OneToMany(mappedBy = "parentId", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Set<ShopUnit> children;

    public String getParentId() {
        if (parentId != null) {
            return parentId.getId();
        }
        return null;
    }

    public Set<ShopUnit> getChildren() {
        if (this.type == ShopUnitType.OFFER){
            return null;
        }
        return children;
    }
}
