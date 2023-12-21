package com.switchfully.eurder_db.mapper;

import com.switchfully.eurder_db.entity.Currency;
import com.switchfully.eurder_db.entity.Item;
import com.switchfully.eurder_db.entity.OrderLine;
import com.switchfully.eurder_db.entity.Price;
import com.switchfully.eurder_db.dto.CreateOrderLineDto;
import com.switchfully.eurder_db.dto.OrderLineDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class OrderLineMapperTest {
    private final OrderLineMapper orderLineMapper = new OrderLineMapper(new PriceMapper());

    @Test
    void givenOrderLine_whenMapOrderLineToOrderLineDto_thenGetOrderLineDto() {
        // GIVEN
        Long itemId = 1L;
        String itemName = "itemName";
        Price itemPrice = new Price(10.0, Currency.EUR);
        int amountInOrder = 10;
        LocalDate shippingDate = LocalDate.now();
        OrderLine orderLine = new OrderLine(itemId, itemName, itemPrice, amountInOrder, shippingDate);

        // WHEN
        OrderLineDto actual = orderLineMapper.orderLineToOrderLineDto(orderLine);

        // THEN
        assertThat(actual).isInstanceOf(OrderLineDto.class);
        assertThat(actual.getItemId()).isEqualTo(orderLine.getItemId());
        assertThat(actual.getItemName()).isEqualTo(orderLine.getItemName());
        assertThat(actual.getItemPrice().getAmount()).isEqualTo(orderLine.getItemPrice().getAmount());
        assertThat(actual.getItemPrice().getCurrency()).isEqualTo(orderLine.getItemPrice().getCurrency());
        assertThat(actual.getAmountInOrder()).isEqualTo(orderLine.getAmountInOrder());
        assertThat(actual.getShippingDate()).isEqualTo(orderLine.getShippingDate());
    }

    @Test
    void givenCreateOrderLineDto_whenMapCreateOrderLineDtoToOrderLine_thenGetOrderLine() {
        // GIVEN
        String name = "name";
        String description = "description";
        Price price = new Price(10.0, Currency.EUR);
        int amountInStock = 10;
        Item item = new Item(name, description, price, amountInStock);

        int amountInOrder = 1;
        CreateOrderLineDto createOrderLineDto = new CreateOrderLineDto(item.getId(), amountInOrder);

        LocalDate shippingDate = LocalDate.now();

        // WHEN
        OrderLine actual = orderLineMapper.createOrderLineDtoToOrderLine(item, createOrderLineDto, shippingDate);

        // THEN
        assertThat(actual).isInstanceOf(OrderLine.class);
        assertThat(actual.getItemId()).isEqualTo(createOrderLineDto.getItemId());
        assertThat(actual.getItemName()).isEqualTo(item.getName());
        assertThat(actual.getItemPrice().getAmount()).isEqualTo(item.getPrice().getAmount());
        assertThat(actual.getItemPrice().getCurrency()).isEqualTo(item.getPrice().getCurrency());
        assertThat(actual.getAmountInOrder()).isEqualTo(createOrderLineDto.getAmountInOrder());
        assertThat(actual.getShippingDate()).isEqualTo(shippingDate);
    }
}