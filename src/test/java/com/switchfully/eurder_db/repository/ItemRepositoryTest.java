package com.switchfully.eurder_db.repository;

import com.switchfully.eurder_db.entity.Currency;
import com.switchfully.eurder_db.entity.Item;
import com.switchfully.eurder_db.entity.Price;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;

    @Test
    void givenItem_whenCreateItem_thenGetCreatedItemWithIdOne() {
        // GIVEN
        String name = "name";
        String description = "description";
        Price price = new Price(10.0, Currency.EUR);
        int amountInStock = 10;
        Item item = new Item(name, description, price, amountInStock);

        // WHEN
        Item actual = itemRepository.save(item);

        // THEN
        assertThat(actual).isInstanceOf(Item.class);
        assertThat(actual.getId()).isEqualTo(1L);
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getDescription()).isEqualTo(description);
        assertThat(actual.getPrice()).isEqualTo(price);
        assertThat(actual.getAmountInStock()).isEqualTo(amountInStock);
    }

    @Test
    void givenExistingId_whenGetItemById_thenGetItemWithGivenId() {
        // GIVEN
        Long id = 1L;

        String name = "name";
        String description = "description";
        Price price = new Price(10.0, Currency.EUR);
        int amountInStock = 10;
        itemRepository.save(new Item(name, description, price, amountInStock));

        // WHEN
        Item actual = itemRepository.findById(id).get();

        // THEN
        assertThat(actual).isInstanceOf(Item.class);
        assertThat(actual.getId()).isEqualTo(id);
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getDescription()).isEqualTo(description);
        assertThat(actual.getPrice().getAmount()).isEqualTo(price.getAmount());
        assertThat(actual.getPrice().getCurrency()).isEqualTo(price.getCurrency());
        assertThat(actual.getAmountInStock()).isEqualTo(amountInStock);
    }

    @Test
    void givenMultipleItems_whenGetAllItems_thenGetAllItems() {
        // GIVEN
        Item itemOne = new Item("nameOne", "descriptionOne", new Price(10.0, Currency.EUR), 10);
        itemRepository.save(itemOne);

        Item itemTwo = new Item("nameTwo", "descriptionTwo", new Price(10.0, Currency.EUR), 10);
        itemRepository.save(itemTwo);

        // WHEN
        List<Item> actual = itemRepository.findAll();

        // THEN
        assertThat(actual).satisfiesExactly(
                one -> {
                    assertThat(one.getName()).isEqualTo(itemOne.getName());
                    assertThat(one.getDescription()).isEqualTo(itemOne.getDescription());
                },
                two -> {
                    assertThat(two.getName()).isEqualTo(itemTwo.getName());
                    assertThat(two.getDescription()).isEqualTo(itemTwo.getDescription());
                }
        );
    }
}