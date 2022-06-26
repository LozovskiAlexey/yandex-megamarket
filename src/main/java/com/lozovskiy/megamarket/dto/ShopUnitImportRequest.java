package com.lozovskiy.megamarket.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
public class ShopUnitImportRequest {

    @NotNull
    @Valid
    private List<ShopUnitImport> items;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime updateDate;
}
