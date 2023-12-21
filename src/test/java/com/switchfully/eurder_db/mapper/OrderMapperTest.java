package com.switchfully.eurder_db.mapper;

import com.switchfully.eurder_db.entity.Order;
import com.switchfully.eurder_db.entity.OrderLine;
import com.switchfully.eurder_db.dto.OrderDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderMapperTest {
    private final OrderMapper orderMapper = new OrderMapper(new OrderLineMapper(new PriceMapper()), new PriceMapper());

    @Test
    void givenOrder_whenMapOrderToOrderDto_thenGetOrderDto() {
        // GIVEN
        Long customerId = 1L;
        String customerAddress = "address";
        List<OrderLine> orderLines = new ArrayList<>();
        LocalDate orderDate = LocalDate.now();
        Order order = new Order(customerId, customerAddress, orderDate, orderLines);

        // WHEN
        OrderDto actual = orderMapper.orderToOrderDto(order);

        // THEN
        assertThat(actual).isInstanceOf(OrderDto.class);
        assertThat(actual.getId()).isNull();
        assertThat(actual.getCustomerId()).isEqualTo(order.getCustomerId());
        assertThat(actual.getCustomerAddress()).isEqualTo(order.getCustomerAddress());
        assertThat(actual.getOrderLines()).isEmpty();
        assertThat(actual.getOrderDate()).isEqualTo(order.getOrderDate());
    }
}