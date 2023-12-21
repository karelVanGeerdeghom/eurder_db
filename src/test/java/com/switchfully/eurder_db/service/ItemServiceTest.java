package com.switchfully.eurder_db.service;

import com.switchfully.eurder_db.dto.*;
import com.switchfully.eurder_db.entity.Currency;
import com.switchfully.eurder_db.entity.Item;
import com.switchfully.eurder_db.entity.Price;
import com.switchfully.eurder_db.entity.StockIndicator;
import com.switchfully.eurder_db.exception.UnknownItemIdException;
import com.switchfully.eurder_db.mapper.ItemMapper;
import com.switchfully.eurder_db.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
class ItemServiceTest {
    private Item item;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemService itemService;

    @BeforeEach
    public void setup() {
        item = itemRepository.save(new Item("name", "description", new Price(10.0, Currency.EUR), 10));
    }

    @Test
    void givenCreateItemDto_whenCreateItem_thenGetItemDto() {
        // GIVEN
        String name = "name";
        String description = "description";
        CreatePriceDto createPriceDto = new CreatePriceDto(10.0, Currency.EUR);
        int amountInStock = 10;
        CreateItemDto createItemDto = new CreateItemDto(name, description, createPriceDto, amountInStock);

        // WHEN
        ItemDto actual = itemService.createItem(createItemDto);

        // THEN
        assertThat(actual).isInstanceOf(ItemDto.class);
        assertThat(actual.getId()).isEqualTo(2L);
        assertThat(actual.getName()).isEqualTo(createItemDto.getName());
        assertThat(actual.getDescription()).isEqualTo(createItemDto.getDescription());
        assertThat(actual.getPrice().getAmount()).isEqualTo(createItemDto.getPrice().getAmount());
        assertThat(actual.getPrice().getAmount()).isEqualTo(createItemDto.getPrice().getAmount());
        assertThat(actual.getAmountInStock()).isEqualTo(createItemDto.getAmountInStock());
    }

    @Test
    void givenUpdateItemDto_whenUpdateItem_thenGetItemDto() {
        // GIVEN
        String name = "newName";
        String description = "newDescription";
        UpdatePriceDto updatePriceDto = new UpdatePriceDto(20.0, Currency.EUR);
        int amountInStock = 20;
        UpdateItemDto updateItemDto = new UpdateItemDto(name, description, updatePriceDto, amountInStock);

        // WHEN
        ItemDto actual = itemService.updateItem(1L, updateItemDto);

        // THEN
        assertThat(actual).isInstanceOf(ItemDto.class);
        assertThat(actual.getId()).isEqualTo(1L);
        assertThat(actual.getName()).isEqualTo(updateItemDto.getName());
        assertThat(actual.getDescription()).isEqualTo(updateItemDto.getDescription());
        assertThat(actual.getPrice().getAmount()).isEqualTo(updatePriceDto.getAmount());
        assertThat(actual.getPrice().getCurrency()).isEqualTo(updatePriceDto.getCurrency());
        assertThat(actual.getAmountInStock()).isEqualTo(updateItemDto.getAmountInStock());
    }

    @Test
    void givenExistingId_whenGetItemById_thenGetItemWithGivenId() {
        // GIVEN
        Long id = 1L;

        // WHEN
        ItemDto actual = itemService.findById(id);

        // THEN
        assertThat(actual).isInstanceOf(ItemDto.class);
        assertThat(actual.getId()).isEqualTo(id);
    }

    @Test
    void givenWrongId_whenGetItemById_thenThrowUnknownItemIdException() {
        // GIVEN
        Long id = 2L;

        // WHEN + THEN
        assertThatThrownBy(() -> itemService.findById(id)).isInstanceOf(UnknownItemIdException.class);
    }

    @Test
    void whenGetAllItems_thenGetAllItemDtos() {
        // WHEN
        List<ItemDto> actual = itemService.findAllItems();

        // THEN
        assertThat(actual).satisfiesExactly(itemDto -> {
            assertThat(itemDto.getId()).isEqualTo(item.getId());
            assertThat(itemDto.getName()).isEqualTo(item.getName());
            assertThat(itemDto.getDescription()).isEqualTo(item.getDescription());
            assertThat(itemDto.getPrice().getAmount()).isEqualTo(item.getPrice().getAmount());
            assertThat(itemDto.getPrice().getCurrency()).isEqualTo(item.getPrice().getCurrency());
            assertThat(itemDto.getAmountInStock()).isEqualTo(item.getAmountInStock());
        });
    }

    @Test
    void givenMultipleItems_whenGetAllItemStockIndicators_thenGetAllItemStockIndicatorsSortedByStockIndicator() {
        // GIVEN
        itemService.createItem(new CreateItemDto("name", "description", new CreatePriceDto(10.0, Currency.EUR), 15));
        itemService.createItem(new CreateItemDto("name", "description", new CreatePriceDto(10.0, Currency.EUR), 8));
        itemService.createItem(new CreateItemDto("name", "description", new CreatePriceDto(10.0, Currency.EUR), 6));
        itemService.createItem(new CreateItemDto("name", "description", new CreatePriceDto(10.0, Currency.EUR), 4));
        itemService.createItem(new CreateItemDto("name", "description", new CreatePriceDto(10.0, Currency.EUR), 2));

        // WHEN
        List<ItemStockIndicatorDto> actual = itemService.findAllItemStockIndicators(null);

        // THEN
        assertThat(actual).satisfiesExactly(
                itemStockIndicatorDtoOne -> assertThat(itemStockIndicatorDtoOne.getStockIndicator()).isEqualTo(StockIndicator.STOCK_LOW),
                itemStockIndicatorDtoTwo -> assertThat(itemStockIndicatorDtoTwo.getStockIndicator()).isEqualTo(StockIndicator.STOCK_LOW),
                itemStockIndicatorDtoThree -> assertThat(itemStockIndicatorDtoThree.getStockIndicator()).isEqualTo(StockIndicator.STOCK_MEDIUM),
                itemStockIndicatorDtoFour -> assertThat(itemStockIndicatorDtoFour.getStockIndicator()).isEqualTo(StockIndicator.STOCK_MEDIUM),
                itemStockIndicatorDtoFive -> assertThat(itemStockIndicatorDtoFive.getStockIndicator()).isEqualTo(StockIndicator.STOCK_HIGH),
                itemStockIndicatorDtoSix -> assertThat(itemStockIndicatorDtoSix.getStockIndicator()).isEqualTo(StockIndicator.STOCK_HIGH)
        );
    }

    @Test
    void givenMultipleItems_whenGetAllItemStockIndicatorsWithLowStock_thenGetAllItemStockIndicatorsSortedByStockIndicatorWithLowStock() {
        // GIVEN
        itemService.createItem(new CreateItemDto("name", "description", new CreatePriceDto(10.0, Currency.EUR), 15));
        itemService.createItem(new CreateItemDto("name", "description", new CreatePriceDto(10.0, Currency.EUR), 8));
        itemService.createItem(new CreateItemDto("name", "description", new CreatePriceDto(10.0, Currency.EUR), 6));
        itemService.createItem(new CreateItemDto("name", "description", new CreatePriceDto(10.0, Currency.EUR), 4));
        itemService.createItem(new CreateItemDto("name", "description", new CreatePriceDto(10.0, Currency.EUR), 2));

        // WHEN
        List<ItemStockIndicatorDto> actual = itemService.findAllItemStockIndicators(StockIndicator.STOCK_LOW);

        // THEN
        assertThat(actual).satisfiesExactly(
                itemStockIndicatorDtoOne -> assertThat(itemStockIndicatorDtoOne.getStockIndicator()).isEqualTo(StockIndicator.STOCK_LOW),
                itemStockIndicatorDtoTwo -> assertThat(itemStockIndicatorDtoTwo.getStockIndicator()).isEqualTo(StockIndicator.STOCK_LOW)
        );
    }

    @Test
    void givenMultipleItems_whenGetAllItemStockIndicatorsWithMediumStock_thenGetAllItemStockIndicatorsSortedByStockIndicatorWithMediumStock() {
        // GIVEN
        itemService.createItem(new CreateItemDto("name", "description", new CreatePriceDto(10.0, Currency.EUR), 15));
        itemService.createItem(new CreateItemDto("name", "description", new CreatePriceDto(10.0, Currency.EUR), 8));
        itemService.createItem(new CreateItemDto("name", "description", new CreatePriceDto(10.0, Currency.EUR), 6));
        itemService.createItem(new CreateItemDto("name", "description", new CreatePriceDto(10.0, Currency.EUR), 4));
        itemService.createItem(new CreateItemDto("name", "description", new CreatePriceDto(10.0, Currency.EUR), 2));

        // WHEN
        List<ItemStockIndicatorDto> actual = itemService.findAllItemStockIndicators(StockIndicator.STOCK_MEDIUM);

        // THEN
        assertThat(actual).satisfiesExactly(
                itemStockIndicatorDtoOne -> assertThat(itemStockIndicatorDtoOne.getStockIndicator()).isEqualTo(StockIndicator.STOCK_MEDIUM),
                itemStockIndicatorDtoTwo -> assertThat(itemStockIndicatorDtoTwo.getStockIndicator()).isEqualTo(StockIndicator.STOCK_MEDIUM)
        );
    }

    @Test
    void givenMultipleItems_whenGetAllItemStockIndicatorsWithHighStock_thenGetAllItemStockIndicatorsSortedByStockIndicatorWithHighStock() {
        // GIVEN
        itemService.createItem(new CreateItemDto("name", "description", new CreatePriceDto(10.0, Currency.EUR), 15));
        itemService.createItem(new CreateItemDto("name", "description", new CreatePriceDto(10.0, Currency.EUR), 8));
        itemService.createItem(new CreateItemDto("name", "description", new CreatePriceDto(10.0, Currency.EUR), 6));
        itemService.createItem(new CreateItemDto("name", "description", new CreatePriceDto(10.0, Currency.EUR), 4));
        itemService.createItem(new CreateItemDto("name", "description", new CreatePriceDto(10.0, Currency.EUR), 2));

        // WHEN
        List<ItemStockIndicatorDto> actual = itemService.findAllItemStockIndicators(StockIndicator.STOCK_HIGH);

        // THEN
        assertThat(actual).satisfiesExactly(
                itemStockIndicatorDtoOne -> assertThat(itemStockIndicatorDtoOne.getStockIndicator()).isEqualTo(StockIndicator.STOCK_HIGH),
                itemStockIndicatorDtoTwo -> assertThat(itemStockIndicatorDtoTwo.getStockIndicator()).isEqualTo(StockIndicator.STOCK_HIGH)
        );
    }
}