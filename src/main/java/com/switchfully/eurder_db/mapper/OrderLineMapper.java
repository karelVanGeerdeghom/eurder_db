package com.switchfully.eurder_db.mapper;

import com.switchfully.eurder_db.entity.Item;
import com.switchfully.eurder_db.entity.OrderLine;
import com.switchfully.eurder_db.dto.CreateOrderLineDto;
import com.switchfully.eurder_db.dto.OrderLineDto;
import com.switchfully.eurder_db.entity.Price;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class OrderLineMapper {
    private PriceMapper priceMapper;

    public OrderLineMapper(PriceMapper priceMapper) {
        this.priceMapper = priceMapper;
    }

    public OrderLineDto orderLineToOrderLineDto(OrderLine orderLine) {
        return new OrderLineDto(orderLine.getItemId(), orderLine.getItemName(), priceMapper.priceToPriceDto(orderLine.getItemPrice()), orderLine.getAmountInOrder(), orderLine.getShippingDate(), priceMapper.priceToPriceDto(orderLine.calculateTotalPrice()));
    }

    public CreateOrderLineDto orderLineToCreateOrderLineDto(OrderLine orderLine) {
        return new CreateOrderLineDto(orderLine.getItemId(), orderLine.getAmountInOrder());
    }

    public OrderLine createOrderLineDtoToOrderLine(Item item, CreateOrderLineDto createOrderLineDto, LocalDate shippingDate) {
        return new OrderLine(item.getId(), item.getName(), new Price(item.getPrice().getAmount(), item.getPrice().getCurrency()), createOrderLineDto.getAmountInOrder(), shippingDate);
    }
}
