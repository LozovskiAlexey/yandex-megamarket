package com.lozovskiy.megamarket.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
public class ShopUnitImportRequest {
    @NonNull
    private List<ShopUnitImport> items;

    @NonNull
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime updateDate;
}
