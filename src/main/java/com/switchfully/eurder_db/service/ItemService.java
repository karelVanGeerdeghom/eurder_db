package com.switchfully.eurder_db.service;

import com.switchfully.eurder_db.dto.CreateItemDto;
import com.switchfully.eurder_db.dto.ItemDto;
import com.switchfully.eurder_db.dto.ItemStockIndicatorDto;
import com.switchfully.eurder_db.dto.UpdateItemDto;
import com.switchfully.eurder_db.entity.Item;
import com.switchfully.eurder_db.entity.StockIndicator;
import com.switchfully.eurder_db.exception.UnknownItemIdException;
import com.switchfully.eurder_db.mapper.ItemMapper;
import com.switchfully.eurder_db.repository.ItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ItemService {
    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;

    public ItemService(ItemMapper itemMapper, ItemRepository itemRepository) {
        this.itemMapper = itemMapper;
        this.itemRepository = itemRepository;
    }



    public ItemDto createItem(CreateItemDto createItemDto) {
        Item item = itemRepository.save(itemMapper.createItemDtoToItem(createItemDto));

        return itemMapper.itemToItemDto(item);
    }

    public ItemDto updateItem(Long id, UpdateItemDto updateItemDto) throws UnknownItemIdException {
        Item item = itemMapper.updateItemDtoToItem(itemRepository.findById(id).orElseThrow(UnknownItemIdException::new), updateItemDto);

        return itemMapper.itemToItemDto(itemRepository.save(item));
    }

    public ItemDto findById(Long id) throws UnknownItemIdException {
        Item item = itemRepository.findById(id).orElseThrow(UnknownItemIdException::new);

        return itemMapper.itemToItemDto(item);
    }

    public List<ItemDto> findAllItems() {
        return itemRepository.findAll().stream().map(itemMapper::itemToItemDto).collect(Collectors.toList());
    }

    public List<ItemStockIndicatorDto> findAllItemStockIndicators(StockIndicator stockIndicator) {
        return itemRepository.findAll().stream()
                .sorted(Comparator.comparing(Item::getAmountInStock))
                .map(itemMapper::itemToItemStockIndicatorDto)
                .filter(itemStockIndicator -> stockIndicator == null || itemStockIndicator.getStockIndicator().equals(stockIndicator))
                .collect(Collectors.toList());
    }
}

