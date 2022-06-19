package com.lozovskiy.megamarket.controller;

import com.lozovskiy.megamarket.domain.ShopUnit;
import com.lozovskiy.megamarket.domain.ShopUnitsHistory;
import com.lozovskiy.megamarket.dto.ShopUnitImportRequest;
import com.lozovskiy.megamarket.service.ShopUnitApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ShopUnitController {
    // clean, package
    // docker build -t alexlozovski/mega-market:1.1 . (заменить версию 1.1 -> 1.2)
    // docker push alexlozovski/mega-market:1.1 (с новой версией)
    // docker run --add-host host.docker.internal:host-gateway -p 80:80 alexlozovski/mega-market:1.1

    // TODO прочитать про tmux attach -t для запуска в отдельном потоке
    // TODO заменить в app.properties создание бд, чтоб она не пересоздавалась
    // TODO добавить везде мапперы и DTO
    // TODO заполнить среднюю стоимость категорий

    private final ShopUnitApiService shopUnitApiService;
    // TODO Добавить  global exception handler для валидации

    // TODO Добавить валидацию данных
    // TODO Добавить иерархию исключений

    @GetMapping(path = "/nodes/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShopUnit> getUnitsHierarchyById(@PathVariable String id) {
        Optional<ShopUnit> shopUnitOptional = shopUnitApiService.findUnitHierarchy(id);
        if (shopUnitOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(shopUnitOptional.get());
    }

    @PostMapping(path = "/imports", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createShopUnit(@RequestBody ShopUnitImportRequest shopUnitImportRequest) {
        shopUnitApiService.createShopUnits(shopUnitImportRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Void> deleteShopUnit(@PathVariable String id) {
        shopUnitApiService.deleteShopUnits(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/node/{id}/statistic", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ShopUnitsHistory>> getStatistic(@PathVariable String id,
                                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateEnd,
                                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateStart) {
        List<ShopUnitsHistory> histories = shopUnitApiService.getStatisticBetween(id, dateStart, dateEnd);
        return ResponseEntity.ok(histories);
    }

    @GetMapping(path = "/sales", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ShopUnitsHistory>> getSales(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime date) {
        List<ShopUnitsHistory> sales = shopUnitApiService.getSales(date);
        return ResponseEntity.ok(sales);
    }
}
