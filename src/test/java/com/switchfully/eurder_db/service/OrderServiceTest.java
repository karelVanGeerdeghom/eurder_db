package com.switchfully.eurder_db.service;

import com.switchfully.eurder_db.entity.*;
import com.switchfully.eurder_db.dto.CreateOrderDto;
import com.switchfully.eurder_db.dto.CreateOrderLineDto;
import com.switchfully.eurder_db.dto.OrderDto;
import com.switchfully.eurder_db.dto.ReOrderDto;
import com.switchfully.eurder_db.exception.InvalidAmountInOrderInOrderLineException;
import com.switchfully.eurder_db.exception.NoOrderLinesException;
import com.switchfully.eurder_db.exception.OrderIsNotForCustomerException;
import com.switchfully.eurder_db.exception.UnknownItemIdException;
import com.switchfully.eurder_db.mapper.OrderLineMapper;
import com.switchfully.eurder_db.mapper.OrderMapper;
import com.switchfully.eurder_db.repository.CustomerRepository;
import com.switchfully.eurder_db.repository.ItemRepository;
import com.switchfully.eurder_db.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
class OrderServiceTest {
    private Customer customerOne;
    private Customer customerTwo;
    private Item itemOne;
    private Item itemTwo;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private OrderService orderService;

    @BeforeEach
    public void setup() {
        customerOne = customerRepository.save(new Customer("customer.one@mail.com", "password", "firstName", "lastName", "phoneNumber", "address"));
        customerTwo = customerRepository.save(new Customer("customer.two@mail.com", "password", "firstName", "lastName", "phoneNumber", "address"));

        itemOne = itemRepository.save(new Item("nameOne", "descriptionOne", new Price(10.0, Currency.EUR), 10));
        itemTwo = itemRepository.save(new Item("nameTwo", "descriptionTwo", new Price(10.0, Currency.EUR), 10));
    }

    @Test
    void verifyShippingDaysInStock() {
        assertThat(OrderService.SHIPPING_DAYS_IN_STOCK).isEqualTo(1);
    }

    @Test
    void verifyShippingDaysNotInStock() {
        assertThat(OrderService.SHIPPING_DAYS_NOT_IN_STOCK).isEqualTo(7);
    }

    @Test
    void givenCustomerAndCreateOrderDto_whenPlaceOrder_thenGetOrderDto() {
        // GIVEN
        LocalDate orderDate = LocalDate.now();
        List<CreateOrderLineDto> createOrderLineDtos = new ArrayList<>(){{
            add(new CreateOrderLineDto(itemOne.getId(), 1));
            add(new CreateOrderLineDto(itemTwo.getId(), 1));
        }};
        CreateOrderDto createOrderDto = new CreateOrderDto(createOrderLineDtos, orderDate);

        // WHEN
        OrderDto actual = orderService.placeOrder(customerOne, createOrderDto);

        // THEN
        assertThat(actual).isInstanceOf(OrderDto.class);
        assertThat(actual.getOrderLines()).hasSize(2);
        assertThat(actual.getTotalPrice().getAmount()).isEqualTo(20.0);
        assertThat(actual.getTotalPrice().getCurrency()).isEqualTo(Currency.EUR);
        assertThat(itemRepository.findById(1L).get().getAmountInStock()).isEqualTo(9);
        assertThat(itemRepository.findById(2L).get().getAmountInStock()).isEqualTo(9);
    }

    @Test
    void givenCustomerAndExistingId_whenDuplicateOrder_thenGetOrderDto() {
        // GIVEN
        LocalDate orderDate = LocalDate.now();
        List<CreateOrderLineDto> createOrderLineDtos = new ArrayList<>(){{
            add(new CreateOrderLineDto(itemOne.getId(), 1));
            add(new CreateOrderLineDto(itemTwo.getId(), 1));
        }};
        CreateOrderDto createOrderDto = new CreateOrderDto(createOrderLineDtos, orderDate);
        OrderDto orderDto = orderService.placeOrder(customerOne, createOrderDto);

        ReOrderDto reOrderDto = new ReOrderDto(orderDate);

        // WHEN
        OrderDto actual = orderService.reOrder(customerOne, orderDto.getId(), reOrderDto);
        // THEN
        assertThat(actual).isInstanceOf(OrderDto.class);
        assertThat(actual.getOrderLines()).hasSize(2);
        assertThat(actual.getTotalPrice().getAmount()).isEqualTo(20.0);
        assertThat(actual.getTotalPrice().getCurrency()).isEqualTo(Currency.EUR);
        assertThat(itemRepository.findById(1L).get().getAmountInStock()).isEqualTo(8);
        assertThat(itemRepository.findById(2L).get().getAmountInStock()).isEqualTo(8);
    }

    @Test
    void givenCustomerAndCreateOrderDtoWithoutCreateOrderLineDtos_whenPlaceOrder_thenThrowNoOrderLinesException() {
        // GIVEN
        LocalDate orderDate = LocalDate.now();
        List<CreateOrderLineDto> createOrderLineDtos = new ArrayList<>();
        CreateOrderDto createOrderDto = new CreateOrderDto(createOrderLineDtos, orderDate);

        // WHEN + THEN
        assertThatThrownBy(() -> orderService.placeOrder(customerOne, createOrderDto)).isInstanceOf(NoOrderLinesException.class);
    }

    @Test
    void givenCustomerAndCreateOrderDtoWithInvalidAmountInOrder_whenPlaceOrder_thenThrowInvalidAmountInOrderInOrderLineException() {
        // GIVEN
        LocalDate orderDate = LocalDate.now();
        List<CreateOrderLineDto> createOrderLineDtos = new ArrayList<>(){{
            add(new CreateOrderLineDto(1L, 0));
        }};
        CreateOrderDto createOrderDto = new CreateOrderDto(createOrderLineDtos, orderDate);

        // WHEN + THEN
        assertThatThrownBy(() -> orderService.placeOrder(customerOne, createOrderDto)).isInstanceOf(InvalidAmountInOrderInOrderLineException.class);
    }

    @Test
    void givenCustomerAndCreateOrderDtoWithUnknownItemId_whenPlaceOrder_thenThrowUnknownItemIdException() {
        // GIVEN
        LocalDate orderDate = LocalDate.now();
        List<CreateOrderLineDto> createOrderLineDtos = new ArrayList<>(){{
            add(new CreateOrderLineDto(10L, 1));
        }};
        CreateOrderDto createOrderDto = new CreateOrderDto(createOrderLineDtos, orderDate);

        // WHEN + THEN
        assertThatThrownBy(() -> orderService.placeOrder(customerOne, createOrderDto)).isInstanceOf(UnknownItemIdException.class);
    }

    @Test
    void givenCustomerAndCreateOrderDto_whenPlaceOrderAndAmountInOrderIsLessThanAmountInStock_thenShippingDateIsInStockShippingDate() {
        // GIVEN
        LocalDate orderDate = LocalDate.now();
        List<CreateOrderLineDto> createOrderLineDtos = new ArrayList<>(){{
            add(new CreateOrderLineDto(itemOne.getId(), 1));
        }};
        CreateOrderDto createOrderDto = new CreateOrderDto(createOrderLineDtos, orderDate);

        // WHEN
        OrderDto actual = orderService.placeOrder(customerOne, createOrderDto);

        // THEN
        assertThat(actual.getOrderLines().getFirst().getShippingDate()).isEqualTo(LocalDate.now().plusDays(OrderService.SHIPPING_DAYS_IN_STOCK));
    }

    @Test
    void givenCustomerAndCreateOrderDto_whenPlaceOrderAndAmountInOrderIsMoreThanAmountInStock_thenShippingDateIsNotInStockShippingDate() {
        // GIVEN
        LocalDate orderDate = LocalDate.now();
        List<CreateOrderLineDto> createOrderLineDtos = new ArrayList<>(){{
            add(new CreateOrderLineDto(itemOne.getId(), 100));
        }};
        CreateOrderDto createOrderDto = new CreateOrderDto(createOrderLineDtos, orderDate);

        // WHEN
        OrderDto actual = orderService.placeOrder(customerOne, createOrderDto);

        // THEN
        assertThat(actual.getOrderLines().getFirst().getShippingDate()).isEqualTo(LocalDate.now().plusDays(OrderService.SHIPPING_DAYS_NOT_IN_STOCK));
    }

    @Test
    void givenCustomerAndMultipleOrders_whenGetAllOrdersForCustomer_thenGetCustomerOrderDtos() {
        // GIVEN
        LocalDate orderDate = LocalDate.now();
        orderService.placeOrder(customerOne, new CreateOrderDto(new ArrayList<>(){{
            add(new CreateOrderLineDto(itemOne.getId(), 1));
        }}, orderDate));
        orderService.placeOrder(customerOne, new CreateOrderDto(new ArrayList<>(){{
            add(new CreateOrderLineDto(itemTwo.getId(), 1));
        }}, orderDate));

        // WHEN
        List<OrderDto> actual = orderService.findAllOrdersForCustomer(customerOne);

        // THEN
        assertThat(actual).hasSize(2);
        assertThat(actual).allSatisfy(orderDto -> assertThat(orderDto).isInstanceOf(OrderDto.class));
    }

    @Test
    void givenMultipleCustomersAndMultipleOrdersShippingToday_whenGetAllOrdersShippingToday_thenGetAllOrderDtosShippingToday() {
        // GIVEN
        LocalDate orderDate = LocalDate.now();
        orderService.placeOrder(customerOne, new CreateOrderDto(new ArrayList<>(){{
            add(new CreateOrderLineDto(itemOne.getId(), 1));
            add(new CreateOrderLineDto(itemTwo.getId(), 20));
        }}, orderDate));
        orderService.placeOrder(customerTwo, new CreateOrderDto(new ArrayList<>(){{
            add(new CreateOrderLineDto(itemOne.getId(), 1));
            add(new CreateOrderLineDto(itemTwo.getId(), 20));
        }}, orderDate));

        // WHEN
        List<OrderDto> actual = orderService.findAllOrdersForShippingDate(orderDate.plusDays(1));

        // THEN
        assertThat(actual).hasSize(2);
        assertThat(actual).allSatisfy(orderDto -> assertThat(orderDto).isInstanceOf(OrderDto.class));
        assertThat(actual).allSatisfy(orderDto -> assertThat(orderDto.getOrderLines()).hasSize(1));
    }

    @Test
    void givenExistingId_whenGetOrderById_thenGetOrderDtoWithGivenId() {
        // GIVEN
        Long id = 1L;

        LocalDate orderDate = LocalDate.now();
        orderService.placeOrder(customerOne, new CreateOrderDto(new ArrayList<>(){{
            add(new CreateOrderLineDto(itemOne.getId(), 1));
        }}, orderDate));

        // WHEN
        OrderDto actual = orderService.findById(id);

        // THEN
        assertThat(actual).isInstanceOf(OrderDto.class);
        assertThat(actual.getId()).isEqualTo(id);
    }

    @Test
    void givenMultipleCustomersAndMultipleOrders_whenValidateOrderByIdForCustomer_thenGetOrderDtoWithGivenId() {
        // GIVEN
        LocalDate orderDate = LocalDate.now().minusDays(1);
        orderService.placeOrder(customerOne, new CreateOrderDto(new ArrayList<>(){{
            add(new CreateOrderLineDto(itemOne.getId(), 1));
        }}, orderDate));
        orderService.placeOrder(customerTwo, new CreateOrderDto(new ArrayList<>(){{
            add(new CreateOrderLineDto(itemTwo.getId(), 1));
        }}, orderDate));

        // WHEN + THEN
        assertThatNoException().isThrownBy(() -> orderService.validateOrderByIdForCustomer(customerOne, 1L));
    }

    @Test
    void givenMultipleCustomersAndMultipleOrders_whenValidateOrderByIdForAnotherCustomer_thenThrowOrderIsNotForCustomerException() {
        // GIVEN
        LocalDate orderDate = LocalDate.now().minusDays(1);
        orderService.placeOrder(customerOne, new CreateOrderDto(new ArrayList<>(){{
            add(new CreateOrderLineDto(itemOne.getId(), 1));
        }}, orderDate));
        orderService.placeOrder(customerTwo, new CreateOrderDto(new ArrayList<>(){{
            add(new CreateOrderLineDto(itemTwo.getId(), 1));
        }}, orderDate));

        // WHEN + THEN
        assertThatThrownBy(() -> orderService.validateOrderByIdForCustomer(customerOne, 2L)).isInstanceOf(OrderIsNotForCustomerException.class);
    }
}