package com.switchfully.eurder_db.controller;

import com.switchfully.eurder_db.entity.Currency;
import com.switchfully.eurder_db.dto.*;
import com.switchfully.eurder_db.service.OrderService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
class OrderControllerIntegrationTest {
    @LocalServerPort
    private int port;

    @Test
    void givenCreateOrderDto_whenPlaceOrder_thenGetOrderDto() {
        // GIVEN
        String email = "e@mail";
        String password = "password";
        String firstName = "firstName";
        String lastName = "lastName";
        String phoneNumber = "phoneNumber";
        String address = "address";
        CreateCustomerDto createCustomerDto = new CreateCustomerDto(email, password, firstName, lastName, phoneNumber, address);

        CustomerDto customerDto =
                RestAssured
                        .given()
                        .body(createCustomerDto)
                        .accept(JSON)
                        .contentType(JSON)
                        .when()
                        .port(port)
                        .post("/customers")
                        .then()
                        .extract()
                        .as(CustomerDto.class);

        String name = "e@mail";
        String description = "password";
        CreatePriceDto createPriceDto = new CreatePriceDto(10.0, Currency.EUR);
        Integer amountInStock = 10;
        CreateItemDto createItemDto = new CreateItemDto(name, description, createPriceDto, amountInStock);

        ItemDto itemDto =
                RestAssured
                        .given()
                        .body(createItemDto)
                        .accept(JSON)
                        .contentType(JSON)
                        .header("email", "admin@eurder_db.com")
                        .header("password", "admin")
                        .when()
                        .port(port)
                        .post("/items")
                        .then()
                        .extract()
                        .as(ItemDto.class);

        Integer amountInOrder = 1;
        LocalDate orderDate = LocalDate.now();
        List<CreateOrderLineDto> createOrderLineDtos = new ArrayList<>(){{
            add(new CreateOrderLineDto(itemDto.getId(), amountInOrder));
        }};
        CreateOrderDto createOrderDto = new CreateOrderDto(createOrderLineDtos, orderDate);

        // WHEN
        OrderDto actual =
                RestAssured
                        .given()
                        .body(createOrderDto)
                        .accept(JSON)
                        .contentType(JSON)
                        .header("email", email)
                        .header("password", password)
                        .when()
                        .port(port)
                        .post("/orders")
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract()
                        .as(OrderDto.class);

        assertThat(actual).isInstanceOf(OrderDto.class);
        assertThat(actual.getCustomerId()).isEqualTo(customerDto.getId());
        assertThat(actual.getCustomerAddress()).isEqualTo(customerDto.getAddress());
        assertThat(actual.getOrderLines()).hasSize(1);
        assertThat(actual.getOrderDate()).isEqualTo(orderDate);
    }

    @Test
    void givenExistingId_whenReOrder_thenGetOrderDto() {
        // GIVEN
        String email = "e@mail";
        String password = "password";
        String firstName = "firstName";
        String lastName = "lastName";
        String phoneNumber = "phoneNumber";
        String address = "address";
        CreateCustomerDto createCustomerDto = new CreateCustomerDto(email, password, firstName, lastName, phoneNumber, address);

        CustomerDto customerDto =
                RestAssured
                        .given()
                        .body(createCustomerDto)
                        .accept(JSON)
                        .contentType(JSON)
                        .when()
                        .port(port)
                        .post("/customers")
                        .then()
                        .extract()
                        .as(CustomerDto.class);

        String name = "e@mail";
        String description = "password";
        CreatePriceDto createPriceDto = new CreatePriceDto(10.0, Currency.EUR);
        Integer amountInStock = 10;
        CreateItemDto createItemDto = new CreateItemDto(name, description, createPriceDto, amountInStock);

        ItemDto itemDto =
                RestAssured
                        .given()
                        .body(createItemDto)
                        .accept(JSON)
                        .contentType(JSON)
                        .header("email", "admin@eurder_db.com")
                        .header("password", "admin")
                        .when()
                        .port(port)
                        .post("/items")
                        .then()
                        .extract()
                        .as(ItemDto.class);

        Integer amountInOrder = 1;
        LocalDate orderDate = LocalDate.now();
        List<CreateOrderLineDto> createOrderLineDtos = new ArrayList<>(){{
            add(new CreateOrderLineDto(itemDto.getId(), amountInOrder));
        }};
        CreateOrderDto createOrderDto = new CreateOrderDto(createOrderLineDtos, orderDate);

        OrderDto orderDto =
                RestAssured
                        .given()
                        .body(createOrderDto)
                        .accept(JSON)
                        .contentType(JSON)
                        .header("email", email)
                        .header("password", password)
                        .when()
                        .port(port)
                        .post("/orders")
                        .then()
                        .extract()
                        .as(OrderDto.class);

        // WHEN
        OrderDto actual =
                RestAssured
                        .given()
                        .body(createOrderDto)
                        .accept(JSON)
                        .contentType(JSON)
                        .header("email", email)
                        .header("password", password)
                        .when()
                        .port(port)
                        .post("/orders/{id}", orderDto.getId())
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract()
                        .as(OrderDto.class);

        assertThat(actual).isInstanceOf(OrderDto.class);
        assertThat(actual.getCustomerId()).isEqualTo(customerDto.getId());
        assertThat(actual.getCustomerAddress()).isEqualTo(customerDto.getAddress());
        assertThat(actual.getOrderLines()).hasSize(1);
        assertThat(actual.getOrderDate()).isEqualTo(orderDate);
    }

    @Test
    void givenExistingId_whenGetOrderByIdForCustomer_thenGetOrderDto() {
        // GIVEN
        String email = "e@mail";
        String password = "password";
        String firstName = "firstName";
        String lastName = "lastName";
        String phoneNumber = "phoneNumber";
        String address = "address";
        CreateCustomerDto createCustomerDto = new CreateCustomerDto(email, password, firstName, lastName, phoneNumber, address);

        CustomerDto customerDto =
                RestAssured
                        .given()
                        .body(createCustomerDto)
                        .accept(JSON)
                        .contentType(JSON)
                        .when()
                        .port(port)
                        .post("/customers")
                        .then()
                        .extract()
                        .as(CustomerDto.class);

        String name = "e@mail";
        String description = "password";
        CreatePriceDto createPriceDto = new CreatePriceDto(10.0, Currency.EUR);
        Integer amountInStock = 10;
        CreateItemDto createItemDto = new CreateItemDto(name, description, createPriceDto, amountInStock);

        ItemDto itemDto =
                RestAssured
                        .given()
                        .body(createItemDto)
                        .accept(JSON)
                        .contentType(JSON)
                        .header("email", "admin@eurder_db.com")
                        .header("password", "admin")
                        .when()
                        .port(port)
                        .post("/items")
                        .then()
                        .extract()
                        .as(ItemDto.class);

        Integer amountInOrder = 1;
        LocalDate orderDate = LocalDate.now();
        List<CreateOrderLineDto> createOrderLineDtos = new ArrayList<>(){{
            add(new CreateOrderLineDto(itemDto.getId(), amountInOrder));
        }};
        CreateOrderDto createOrderDto = new CreateOrderDto(createOrderLineDtos, orderDate);

        OrderDto orderDto =
                RestAssured
                        .given()
                        .body(createOrderDto)
                        .accept(JSON)
                        .contentType(JSON)
                        .header("email", email)
                        .header("password", password)
                        .when()
                        .port(port)
                        .post("/orders")
                        .then()
                        .extract()
                        .as(OrderDto.class);

        // WHEN
        OrderDto actual =
                RestAssured
                        .given()
                        .body(createOrderDto)
                        .accept(JSON)
                        .contentType(JSON)
                        .header("email", email)
                        .header("password", password)
                        .when()
                        .port(port)
                        .get("/orders/{id}", orderDto.getId())
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(OrderDto.class);

        assertThat(actual).isInstanceOf(OrderDto.class);
        assertThat(actual.getCustomerId()).isEqualTo(customerDto.getId());
        assertThat(actual.getCustomerAddress()).isEqualTo(customerDto.getAddress());
        assertThat(actual.getOrderLines()).hasSize(1);
        assertThat(actual.getOrderDate()).isEqualTo(orderDate);
    }

    @Test
    void givenExistingOrders_whenGetAllOrdersForCustomer_thenGetOrderDtos() {
        // GIVEN
        String email = "e@mail";
        String password = "password";
        String firstName = "firstName";
        String lastName = "lastName";
        String phoneNumber = "phoneNumber";
        String address = "address";
        CreateCustomerDto createCustomerDto = new CreateCustomerDto(email, password, firstName, lastName, phoneNumber, address);

        CustomerDto customerDto =
                RestAssured
                        .given()
                        .body(createCustomerDto)
                        .accept(JSON)
                        .contentType(JSON)
                        .when()
                        .port(port)
                        .post("/customers")
                        .then()
                        .extract()
                        .as(CustomerDto.class);

        String name = "e@mail";
        String description = "password";
        CreatePriceDto createPriceDto = new CreatePriceDto(10.0, Currency.EUR);
        Integer amountInStock = 10;
        CreateItemDto createItemDto = new CreateItemDto(name, description, createPriceDto, amountInStock);

        ItemDto itemDto =
                RestAssured
                        .given()
                        .body(createItemDto)
                        .accept(JSON)
                        .contentType(JSON)
                        .header("email", "admin@eurder_db.com")
                        .header("password", "admin")
                        .when()
                        .port(port)
                        .post("/items")
                        .then()
                        .extract()
                        .as(ItemDto.class);

        Integer amountInOrder = 1;
        LocalDate orderDate = LocalDate.now();
        List<CreateOrderLineDto> createOrderLineDtos = new ArrayList<>(){{
            add(new CreateOrderLineDto(itemDto.getId(), amountInOrder));
        }};
        CreateOrderDto createOrderDto = new CreateOrderDto(createOrderLineDtos, orderDate);

        OrderDto orderDto =
            RestAssured
                    .given()
                    .body(createOrderDto)
                    .accept(JSON)
                    .contentType(JSON)
                    .header("email", email)
                    .header("password", password)
                    .when()
                    .port(port)
                    .post("/orders")
                    .then()
                    .extract()
                    .as(OrderDto.class);

        // WHEN
        List<OrderDto> actual =
                RestAssured
                        .given()
                        .accept(JSON)
                        .contentType(JSON)
                        .header("email", email)
                        .header("password", password)
                        .when()
                        .port(port)
                        .get("/orders")
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .body()
                        .jsonPath()
                        .getList(".", OrderDto.class);

        // THEN
        assertThat(actual).hasSize(1);
        assertThat(actual.getFirst().getId()).isEqualTo(orderDto.getId());
    }

    @Test
    void givenExistingOrders_whenGetAllOrdersShippingOnDate_thenGetOrderDtos() {
        // GIVEN
        String email = "e@mail";
        String password = "password";
        String firstName = "firstName";
        String lastName = "lastName";
        String phoneNumber = "phoneNumber";
        String address = "address";
        CreateCustomerDto createCustomerDto = new CreateCustomerDto(email, password, firstName, lastName, phoneNumber, address);

        CustomerDto customerDto =
                RestAssured
                        .given()
                        .body(createCustomerDto)
                        .accept(JSON)
                        .contentType(JSON)
                        .when()
                        .port(port)
                        .post("/customers")
                        .then()
                        .extract()
                        .as(CustomerDto.class);

        String name = "e@mail";
        String description = "password";
        CreatePriceDto createPriceDto = new CreatePriceDto(10.0, Currency.EUR);
        Integer amountInStock = 10;
        CreateItemDto createItemDto = new CreateItemDto(name, description, createPriceDto, amountInStock);

        ItemDto itemDto =
                RestAssured
                        .given()
                        .body(createItemDto)
                        .accept(JSON)
                        .contentType(JSON)
                        .header("email", "admin@eurder_db.com")
                        .header("password", "admin")
                        .when()
                        .port(port)
                        .post("/items")
                        .then()
                        .extract()
                        .as(ItemDto.class);

        Integer amountInOrder = 1;
        LocalDate orderDate = LocalDate.now();
        List<CreateOrderLineDto> createOrderLineDtos = new ArrayList<>(){{
            add(new CreateOrderLineDto(itemDto.getId(), amountInOrder));
        }};
        CreateOrderDto createOrderDto = new CreateOrderDto(createOrderLineDtos, orderDate);

        OrderDto orderDto =
                RestAssured
                        .given()
                        .body(createOrderDto)
                        .accept(JSON)
                        .contentType(JSON)
                        .header("email", email)
                        .header("password", password)
                        .when()
                        .port(port)
                        .post("/orders")
                        .then()
                        .extract()
                        .as(OrderDto.class);

        // WHEN
        List<OrderDto> actual =
                RestAssured
                        .given()
                        .accept(JSON)
                        .contentType(JSON)
                        .header("email", "admin@eurder_db.com")
                        .header("password", "admin")
                        .when()
                        .port(port)
                        .get("/orders/shipping-date/{shippingDate}", orderDate.plusDays(OrderService.SHIPPING_DAYS_IN_STOCK).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .body()
                        .jsonPath()
                        .getList(".", OrderDto.class);

        // THEN
        assertThat(actual).hasSize(1);
        assertThat(actual.getFirst().getId()).isEqualTo(orderDto.getId());
    }
}