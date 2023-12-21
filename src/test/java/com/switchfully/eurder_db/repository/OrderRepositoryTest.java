package com.switchfully.eurder_db.repository;

import com.switchfully.eurder_db.entity.Currency;
import com.switchfully.eurder_db.entity.Order;
import com.switchfully.eurder_db.entity.OrderLine;
import com.switchfully.eurder_db.entity.Price;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.switchfully.eurder_db.service.OrderService.SHIPPING_DAYS_IN_STOCK;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;

    @Test
    void givenOrder_whenCreateOrder_thenGetCreatedOrderWithIdOne() {
        // GIVEN
        Long itemId = 1L;
        String itemName = "name";
        Price price = new Price(10.0, Currency.EUR);
        Integer amountInOrder = 1;
        LocalDate shippingDate = LocalDate.now().plusDays(SHIPPING_DAYS_IN_STOCK);
        OrderLine orderLine = new OrderLine(itemId, itemName, price, amountInOrder, shippingDate);

        Long customerId = 1L;
        String customerAddress = "address";
        LocalDate orderDate = LocalDate.now();
        Order order = new Order(customerId, customerAddress, orderDate, new ArrayList<>(){{
            add(orderLine);
        }});

        // WHEN
        Order actual = orderRepository.save(order);

        // THEN
        assertThat(actual).isInstanceOf(Order.class);
        assertThat(actual.getId()).isEqualTo(1L);
        assertThat(actual.getCustomerId()).isEqualTo(customerId);
        assertThat(actual.getCustomerAddress()).isEqualTo(customerAddress);
        assertThat(actual.getOrderDate()).isEqualTo(orderDate);
        assertThat(actual.getOrderLines()).satisfiesExactly(line -> {
            assertThat(line.getItemId()).isEqualTo(itemId);
            assertThat(line.getItemName()).isEqualTo(itemName);
            assertThat(line.getItemPrice().getAmount()).isEqualTo(price.getAmount());
            assertThat(line.getItemPrice().getCurrency()).isEqualTo(price.getCurrency());
            assertThat(line.getAmountInOrder()).isEqualTo(amountInOrder);
            assertThat(line.getShippingDate()).isEqualTo(shippingDate);
        });
    }

    @Test
    void givenExistingId_whenGetOrderById_thenGetOrderWithGivenId() {
        // GIVEN
        Long id = 1L;

        Long customerId = 1L;
        String customerAddress = "address";
        LocalDate orderDate = LocalDate.now();
        List<OrderLine> orderLines = new ArrayList<>();
        orderRepository.save(new Order(customerId, customerAddress, orderDate, orderLines));

        // WHEN
        Order actual = orderRepository.findById(id).get();

        // THEN
        assertThat(actual).isInstanceOf(Order.class);
        assertThat(actual.getId()).isEqualTo(id);
        assertThat(actual.getCustomerId()).isEqualTo(customerId);
        assertThat(actual.getCustomerAddress()).isEqualTo(customerAddress);
        assertThat(actual.getOrderDate()).isEqualTo(orderDate);
    }
}