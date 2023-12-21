package com.switchfully.eurder_db.mapper;

import com.switchfully.eurder_db.dto.CreateItemDto;
import com.switchfully.eurder_db.dto.ItemDto;
import com.switchfully.eurder_db.dto.ItemStockIndicatorDto;
import com.switchfully.eurder_db.dto.UpdateItemDto;
import com.switchfully.eurder_db.entity.Item;
import com.switchfully.eurder_db.entity.StockIndicator;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {
    public final static int STOCK_LOW_LESS_THAN = 5;
    public final static int STOCK_MEDIUM_LESS_THAN = 10;

    private PriceMapper priceMapper;

    public ItemMapper(PriceMapper priceMapper) {
        this.priceMapper = priceMapper;
    }

    public ItemDto itemToItemDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), priceMapper.priceToPriceDto(item.getPrice()), item.getAmountInStock());
    }

    public ItemStockIndicatorDto itemToItemStockIndicatorDto(Item item) {
        StockIndicator stockIndicator = StockIndicator.STOCK_HIGH;

        if (item.getAmountInStock() < STOCK_MEDIUM_LESS_THAN) {
            stockIndicator = StockIndicator.STOCK_MEDIUM;
        }

        if (item.getAmountInStock() < STOCK_LOW_LESS_THAN) {
            stockIndicator = StockIndicator.STOCK_LOW;
        }

        return new ItemStockIndicatorDto(item.getId(), item.getName(), stockIndicator);
    }

    public Item createItemDtoToItem(CreateItemDto createItemDto) {
        return new Item(createItemDto.getName(), createItemDto.getDescription(), priceMapper.createPriceDtoToPrice(createItemDto.getPrice()), createItemDto.getAmountInStock());
    }

    public Item updateItemDtoToItem(Item item, UpdateItemDto updateItemDto) {
        item.setName(updateItemDto.getName());
        item.setDescription(updateItemDto.getDescription());
        item.setPrice(priceMapper.updatePriceDtoToPrice(updateItemDto.getPrice()));
        item.setAmountInStock(updateItemDto.getAmountInStock());

        return item;
    }
}
