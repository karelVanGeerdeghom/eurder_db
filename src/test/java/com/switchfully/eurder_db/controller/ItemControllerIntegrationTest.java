package com.switchfully.eurder_db.controller;

import com.switchfully.eurder_db.dto.*;
import com.switchfully.eurder_db.entity.Currency;
import com.switchfully.eurder_db.entity.StockIndicator;
import com.switchfully.eurder_db.mapper.ItemMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
class ItemControllerIntegrationTest {
    @LocalServerPort
    private int port;

    @Test
    void givenCreateItemDto_whenCreateItem_thenGetItemDto() {
        // GIVEN
        String name = "e@mail";
        String description = "password";
        CreatePriceDto createPriceDto = new CreatePriceDto(10.0, Currency.EUR);
        Integer amountInStock = 10;
        CreateItemDto createItemDto = new CreateItemDto(name, description, createPriceDto, amountInStock);

        // WHEN
        ItemDto actual =
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
                        .assertThat()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract()
                        .as(ItemDto.class);

        // THEN
        assertThat(actual).isInstanceOf(ItemDto.class);
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getDescription()).isEqualTo(description);
        assertThat(actual.getPrice().getAmount()).isEqualTo(createPriceDto.getAmount());
        assertThat(actual.getPrice().getCurrency()).isEqualTo(createPriceDto.getCurrency());
        assertThat(actual.getAmountInStock()).isEqualTo(amountInStock);
    }

    @Test
    void givenExistingIdAndUpdateItemDto_whenUpdateItem_thenGetItemDto() {
        // GIVEN
        CreateItemDto createItemDto = new CreateItemDto("name", "description", new CreatePriceDto(10.0, Currency.EUR), 10);
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

        String name = "updatedName";
        String description = "updatedDescription";
        UpdatePriceDto updatePriceDto = new UpdatePriceDto(20.0, Currency.EUR);
        Integer amountInStock = 20;
        UpdateItemDto updateItemDto = new UpdateItemDto(name, description, updatePriceDto, amountInStock);

        // WHEN
        ItemDto actual =
                RestAssured
                        .given()
                        .body(updateItemDto)
                        .accept(JSON)
                        .contentType(JSON)
                        .header("email", "admin@eurder_db.com")
                        .header("password", "admin")
                        .when()
                        .port(port)
                        .put("/items/{id}", itemDto.getId())
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(ItemDto.class);

        // THEN
        assertThat(actual).isInstanceOf(ItemDto.class);
        assertThat(actual.getId()).isEqualTo(itemDto.getId());
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getDescription()).isEqualTo(description);
        assertThat(actual.getPrice().getAmount()).isEqualTo(updatePriceDto.getAmount());
        assertThat(actual.getPrice().getCurrency()).isEqualTo(updatePriceDto.getCurrency());
        assertThat(actual.getAmountInStock()).isEqualTo(amountInStock);
    }

    @Test
    void givenExistingId_whenGetItem_thenGetItemDto() {
        // GIVEN
        String name = "name";
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

        // WHEN
        ItemDto actual =
                RestAssured
                        .given()
                        .accept(JSON)
                        .contentType(JSON)
                        .header("email", "admin@eurder_db.com")
                        .header("password", "admin")
                        .when()
                        .port(port)
                        .get("/items/{id}", itemDto.getId())
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(ItemDto.class);

        // THEN
        assertThat(actual).isInstanceOf(ItemDto.class);
        assertThat(actual.getId()).isEqualTo(itemDto.getId());
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getDescription()).isEqualTo(description);
        assertThat(actual.getPrice().getAmount()).isEqualTo(createPriceDto.getAmount());
        assertThat(actual.getPrice().getCurrency()).isEqualTo(createPriceDto.getCurrency());
        assertThat(actual.getAmountInStock()).isEqualTo(amountInStock);
    }

    @Test
    void givenExistingItems_whenGetAllItems_thenGetItemDtos() {
        // GIVEN
        RestAssured
                .given()
                .body(new CreateItemDto("name", "description", new CreatePriceDto(10.0, Currency.EUR), 10))
                .accept(JSON)
                .contentType(JSON)
                .header("email", "admin@eurder_db.com")
                .header("password", "admin")
                .when()
                .port(port)
                .post("/items");

        // WHEN
        List<ItemDto> actual =
                RestAssured
                        .given()
                        .accept(JSON)
                        .contentType(JSON)
                        .header("email", "admin@eurder_db.com")
                        .header("password", "admin")
                        .when()
                        .port(port)
                        .get("/items")
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .body()
                        .jsonPath()
                        .getList(".", ItemDto.class);

        // THEN
        assertThat(actual).hasSize(1);
        assertThat(actual).allSatisfy(itemDto -> assertThat(itemDto).isInstanceOf(ItemDto.class));
    }

    @Test
    void givenExistingItems_whenGetAllItemStockIndicators_thenGetItemStockIndicatorDtos() {
        // GIVEN
        RestAssured
                .given()
                .body(new CreateItemDto("name", "description", new CreatePriceDto(10.0, Currency.EUR), ItemMapper.STOCK_LOW_LESS_THAN - 1))
                .accept(JSON)
                .contentType(JSON)
                .header("email", "admin@eurder_db.com")
                .header("password", "admin")
                .when()
                .port(port)
                .post("/items");

        RestAssured
                .given()
                .body(new CreateItemDto("name", "description", new CreatePriceDto(10.0, Currency.EUR), ItemMapper.STOCK_MEDIUM_LESS_THAN - 1))
                .accept(JSON)
                .contentType(JSON)
                .header("email", "admin@eurder_db.com")
                .header("password", "admin")
                .when()
                .port(port)
                .post("/items");

        RestAssured
                .given()
                .body(new CreateItemDto("name", "description", new CreatePriceDto(10.0, Currency.EUR), ItemMapper.STOCK_MEDIUM_LESS_THAN + 1))
                .accept(JSON)
                .contentType(JSON)
                .header("email", "admin@eurder_db.com")
                .header("password", "admin")
                .when()
                .port(port)
                .post("/items");

        // WHEN
        List<ItemStockIndicatorDto> actual =
                RestAssured
                        .given()
                        .accept(JSON)
                        .contentType(JSON)
                        .header("email", "admin@eurder_db.com")
                        .header("password", "admin")
                        .when()
                        .port(port)
                        .get("/items/stock")
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .body()
                        .jsonPath()
                        .getList(".", ItemStockIndicatorDto.class);

        // THEN
        assertThat(actual).hasSize(3);
        assertThat(actual).allSatisfy(itemStockIndicatorDto -> assertThat(itemStockIndicatorDto).isInstanceOf(ItemStockIndicatorDto.class));
    }

    @Test
    void givenExistingItems_whenGetLowStockItemStockIndicators_thenGetLowStockItemStockIndicatorDtos() {
        // GIVEN
        ItemDto lowStockItemDto =
                RestAssured
                        .given()
                        .body(new CreateItemDto("name", "description", new CreatePriceDto(10.0, Currency.EUR), ItemMapper.STOCK_LOW_LESS_THAN - 1))
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

        ItemDto mediumStockItemDto =
                RestAssured
                        .given()
                        .body(new CreateItemDto("name", "description", new CreatePriceDto(10.0, Currency.EUR), ItemMapper.STOCK_MEDIUM_LESS_THAN - 1))
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

        ItemDto highStockItemDto =
                RestAssured
                        .given()
                        .body(new CreateItemDto("name", "description", new CreatePriceDto(10.0, Currency.EUR), ItemMapper.STOCK_MEDIUM_LESS_THAN + 1))
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

        // WHEN
        List<ItemStockIndicatorDto> actual =
                RestAssured
                        .given()
                        .accept(JSON)
                        .contentType(JSON)
                        .header("email", "admin@eurder_db.com")
                        .header("password", "admin")
                        .when()
                        .port(port)
                        .queryParam("stockIndicator", StockIndicator.STOCK_LOW)
                        .get("/items/stock")
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .body()
                        .jsonPath()
                        .getList(".", ItemStockIndicatorDto.class);

        // THEN
        assertThat(actual).hasSize(1);
        assertThat(actual.getFirst().getId()).isEqualTo(lowStockItemDto.getId());
    }

    @Test
    void givenExistingItems_whenGetMediumStockItemStockIndicators_thenGetMediumStockItemStockIndicatorDtos() {
        // GIVEN
        ItemDto lowStockItemDto =
                RestAssured
                        .given()
                        .body(new CreateItemDto("name", "description", new CreatePriceDto(10.0, Currency.EUR), ItemMapper.STOCK_LOW_LESS_THAN - 1))
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

        ItemDto mediumStockItemDto =
                RestAssured
                        .given()
                        .body(new CreateItemDto("name", "description", new CreatePriceDto(10.0, Currency.EUR), ItemMapper.STOCK_MEDIUM_LESS_THAN - 1))
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

        ItemDto highStockItemDto =
                RestAssured
                        .given()
                        .body(new CreateItemDto("name", "description", new CreatePriceDto(10.0, Currency.EUR), ItemMapper.STOCK_MEDIUM_LESS_THAN + 1))
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

        // WHEN
        List<ItemStockIndicatorDto> actual =
                RestAssured
                        .given()
                        .accept(JSON)
                        .contentType(JSON)
                        .header("email", "admin@eurder_db.com")
                        .header("password", "admin")
                        .when()
                        .port(port)
                        .queryParam("stockIndicator", StockIndicator.STOCK_MEDIUM)
                        .get("/items/stock")
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .body()
                        .jsonPath()
                        .getList(".", ItemStockIndicatorDto.class);

        // THEN
        assertThat(actual).hasSize(1);
        assertThat(actual.getFirst().getId()).isEqualTo(mediumStockItemDto.getId());
    }

    @Test
    void givenExistingItems_whenGetHighStockItemStockIndicators_thenGetHighStockItemStockIndicatorDtos() {
        // GIVEN
        ItemDto lowStockItemDto =
                RestAssured
                        .given()
                        .body(new CreateItemDto("name", "description", new CreatePriceDto(10.0, Currency.EUR), ItemMapper.STOCK_LOW_LESS_THAN - 1))
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

        ItemDto mediumStockItemDto =
                RestAssured
                        .given()
                        .body(new CreateItemDto("name", "description", new CreatePriceDto(10.0, Currency.EUR), ItemMapper.STOCK_MEDIUM_LESS_THAN - 1))
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

        ItemDto highStockItemDto =
                RestAssured
                        .given()
                        .body(new CreateItemDto("name", "description", new CreatePriceDto(10.0, Currency.EUR), ItemMapper.STOCK_MEDIUM_LESS_THAN + 1))
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

        // WHEN
        List<ItemStockIndicatorDto> actual =
                RestAssured
                        .given()
                        .accept(JSON)
                        .contentType(JSON)
                        .header("email", "admin@eurder_db.com")
                        .header("password", "admin")
                        .when()
                        .port(port)
                        .queryParam("stockIndicator", StockIndicator.STOCK_HIGH)
                        .get("/items/stock")
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .body()
                        .jsonPath()
                        .getList(".", ItemStockIndicatorDto.class);

        // THEN
        assertThat(actual).hasSize(1);
        assertThat(actual.getFirst().getId()).isEqualTo(highStockItemDto.getId());
    }
}