package com.switchfully.eurder_db.controller;

import com.switchfully.eurder_db.dto.CreateItemDto;
import com.switchfully.eurder_db.dto.ItemDto;
import com.switchfully.eurder_db.dto.ItemStockIndicatorDto;
import com.switchfully.eurder_db.dto.UpdateItemDto;
import com.switchfully.eurder_db.entity.StockIndicator;
import com.switchfully.eurder_db.service.AdminService;
import com.switchfully.eurder_db.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping(path = "/items")
public class ItemController {
    private final AdminService adminService;
    private final ItemService itemService;

    public ItemController(AdminService adminService, ItemService itemService) {
        this.adminService = adminService;
        this.itemService = itemService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(@RequestHeader String email, @RequestHeader String password, @Valid @RequestBody CreateItemDto createItemDto) {
        adminService.authenticate(email, password);

        return itemService.createItem(createItemDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto updateItem(@RequestHeader String email, @RequestHeader String password, @PathVariable Long id, @Valid @RequestBody UpdateItemDto updateItemDto) {
        adminService.authenticate(email, password);

        return itemService.updateItem(id, updateItemDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto getItem(@PathVariable Long id) {
        return itemService.findById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> findAllItems() {
        return itemService.findAllItems();
    }

    @GetMapping("/stock")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemStockIndicatorDto> findAllItemStockIndicators(@RequestHeader String email, @RequestHeader String password, @RequestParam(required = false) StockIndicator stockIndicator) {
        adminService.authenticate(email, password);

        return itemService.findAllItemStockIndicators(stockIndicator);
    }
}
