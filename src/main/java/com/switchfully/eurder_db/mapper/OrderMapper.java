package com.switchfully.eurder_db.mapper;

import com.switchfully.eurder_db.entity.Order;
import com.switchfully.eurder_db.dto.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {
    private final OrderLineMapper orderLineMapper;
    private final PriceMapper priceMapper;

    public OrderMapper(OrderLineMapper orderLineMapper, PriceMapper priceMapper) {
        this.orderLineMapper = orderLineMapper;
        this.priceMapper = priceMapper;
    }

    public OrderDto orderToOrderDto(Order order) {
        List<OrderLineDto> orderLineDtos = order.getOrderLines().stream().map(orderLineMapper::orderLineToOrderLineDto).toList();

        return new OrderDto(order.getId(), order.getCustomerId(), order.getCustomerAddress(), orderLineDtos, order.getOrderDate(), priceMapper.priceToPriceDto(order.calculateTotalPrice()));
    }

    public CreateOrderDto reOrderDtoToCreateOrderDto(Order order, ReOrderDto reOrderDto) {
        List<CreateOrderLineDto> createOrderLineDtos = order.getOrderLines().stream().map(orderLineMapper::orderLineToCreateOrderLineDto).toList();

        return new CreateOrderDto(createOrderLineDtos, reOrderDto.getOrderDate());
    }
}
