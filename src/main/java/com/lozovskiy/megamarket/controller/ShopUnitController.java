package com.lozovskiy.megamarket.controller;

import com.lozovskiy.megamarket.domain.ShopUnit;
import com.lozovskiy.megamarket.dto.ShopUnitImportRequest;
import com.lozovskiy.megamarket.dto.ShopUnitSalesResponse;
import com.lozovskiy.megamarket.dto.ShopUnitStatisticsResponse;
import com.lozovskiy.megamarket.service.ApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;


@Validated
@RestController
@RequiredArgsConstructor
public class ShopUnitController {

    private final ApiService apiService;

    @GetMapping(path = "/nodes/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShopUnit> getUnitsHierarchyById(@PathVariable
            @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")  String id) {
        ShopUnit shopUnit = apiService.findUnitHierarchy(id);
        return ResponseEntity.ok(shopUnit);
    }

    @PostMapping(path = "/imports", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createShopUnit(@RequestBody @Valid ShopUnitImportRequest shopUnitImportRequest) {
        apiService.createShopUnits(shopUnitImportRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Void> deleteShopUnit(@PathVariable
             @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$") String id) {
        apiService.deleteShopUnits(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/node/{id}/statistic", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShopUnitStatisticsResponse> getStatistic(@PathVariable
                                                               @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$") String id,
                                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateEnd,
                                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateStart) {
        ShopUnitStatisticsResponse histories = apiService.getStatisticBetween(id, dateStart, dateEnd);
        return ResponseEntity.ok(histories);
    }

    @GetMapping(path = "/sales", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShopUnitSalesResponse> getSales(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime date) {
        ShopUnitSalesResponse sales = apiService.getSales(date);
        return ResponseEntity.ok(sales);
    }
}
