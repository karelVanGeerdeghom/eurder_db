package com.switchfully.eurder_db.mapper;

import com.switchfully.eurder_db.dto.*;
import com.switchfully.eurder_db.entity.Currency;
import com.switchfully.eurder_db.entity.Item;
import com.switchfully.eurder_db.entity.Price;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ItemMapperTest {
    private final ItemMapper itemMapper = new ItemMapper(new PriceMapper());

    @Test
    void verifyShippingDaysInStock() {
        assertThat(ItemMapper.STOCK_LOW_LESS_THAN).isEqualTo(5);
    }

    @Test
    void verifyShippingDaysNotInStock() {
        assertThat(ItemMapper.STOCK_MEDIUM_LESS_THAN).isEqualTo(10);
    }

    @Test
    void givenItem_whenMapItemToItemDto_thenGetItemDto() {
        // GIVEN
        String name = "name";
        String description = "description";
        Price price = new Price(10.0, Currency.EUR);
        int amountInStock = 10;
        Item item = new Item(name, description, price, amountInStock);

        // WHEN
        ItemDto actual = itemMapper.itemToItemDto(item);

        // THEN
        assertThat(actual).isInstanceOf(ItemDto.class);
        assertThat(actual.getId()).isNull();
        assertThat(actual.getName()).isEqualTo(item.getName());
        assertThat(actual.getDescription()).isEqualTo(item.getDescription());
        assertThat(actual.getPrice().getAmount()).isEqualTo(item.getPrice().getAmount());
        assertThat(actual.getPrice().getCurrency()).isEqualTo(item.getPrice().getCurrency());
        assertThat(actual.getAmountInStock()).isEqualTo(item.getAmountInStock());
    }

    @Test
    void givenCreateItemDto_whenMapCreateItemDtoToItem_thenGetItem() {
        // GIVEN
        String name = "name";
        String description = "description";
        CreatePriceDto createPriceDto = new CreatePriceDto(10.0, Currency.EUR);
        int amountInStock = 10;
        CreateItemDto createItemDto = new CreateItemDto(name, description, createPriceDto, amountInStock);

        // WHEN
        Item actual = itemMapper.createItemDtoToItem(createItemDto);

        // THEN
        assertThat(actual).isInstanceOf(Item.class);
        assertThat(actual.getId()).isNull();
        assertThat(actual.getName()).isEqualTo(createItemDto.getName());
        assertThat(actual.getDescription()).isEqualTo(createItemDto.getDescription());
        assertThat(actual.getPrice().getAmount()).isEqualTo(createItemDto.getPrice().getAmount());
        assertThat(actual.getPrice().getCurrency()).isEqualTo(createItemDto.getPrice().getCurrency());
        assertThat(actual.getAmountInStock()).isEqualTo(createItemDto.getAmountInStock());
    }

    @Test
    void givenUpdateItemDto_whenMapUpdateItemDtoToItem_thenGetItem() {
        // GIVEN
        String name = "name";
        String description = "description";
        Price price = new Price(10.0, Currency.EUR);
        UpdatePriceDto updatePriceDto = new UpdatePriceDto(20.0, Currency.EUR);
        int amountInStock = 10;
        Item item = new Item(name, description, price, amountInStock);
        UpdateItemDto updateItemDto = new UpdateItemDto(name, description, updatePriceDto, amountInStock);

        // WHEN
        Item actual = itemMapper.updateItemDtoToItem(item, updateItemDto);

        // THEN
        assertThat(actual).isInstanceOf(Item.class);
        assertThat(actual.getId()).isNull();
        assertThat(actual.getName()).isEqualTo(updateItemDto.getName());
        assertThat(actual.getDescription()).isEqualTo(updateItemDto.getDescription());
        assertThat(actual.getPrice().getAmount()).isEqualTo(updateItemDto.getPrice().getAmount());
        assertThat(actual.getPrice().getCurrency()).isEqualTo(updateItemDto.getPrice().getCurrency());
        assertThat(actual.getAmountInStock()).isEqualTo(updateItemDto.getAmountInStock());
    }
}