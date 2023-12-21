package com.switchfully.eurder_db.repository;

import com.switchfully.eurder_db.entity.Admin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
class AdminRepositoryTest {
    @Autowired
    private AdminRepository adminRepository;

    @Test
    void givenAdmin_whenCreateAdmin_thenGetCreatedAdminWithIdOne() {
        // GIVEN
        String email = "firstName.lastName@mail.com";
        String password = "password";

        Admin admin = new Admin(email, password);

        // WHEN
        Admin actual = adminRepository.save(admin);

        // THEN
        assertThat(actual).isInstanceOf(Admin.class);
        assertThat(actual.getId()).isNotEqualTo(1L);
        assertThat(actual.getEmail()).isEqualTo(email);
        assertThat(actual.getPassword()).isEqualTo(password);
    }

    @Test
    void givenExistingId_whenGetAdminById_thenGetAdminWithGivenId() {
        // GIVEN
        Long id = 2L;

        String email = "firstName.lastName@mail.com";
        String password = "password";

        adminRepository.save(new Admin(email, password));

        // WHEN
        Admin actual = adminRepository.findById(id).get();

        // THEN
        assertThat(actual).isInstanceOf(Admin.class);
        assertThat(actual.getId()).isEqualTo(id);
        assertThat(actual.getEmail()).isEqualTo(email);
        assertThat(actual.getPassword()).isEqualTo(password);
    }

    @Test
    void givenExistingEmail_whenGetAdminByEmail_thenGetAdminWithGivenEmail() {
        // GIVEN
        String email = "firstName.lastName@mail.com";
        String password = "password";

        adminRepository.save(new Admin(email, password));

        // WHEN
        Admin actual = adminRepository.findByEmail(email);

        // THEN
        assertThat(actual).isInstanceOf(Admin.class);
        assertThat(actual.getId()).isEqualTo(2L);
        assertThat(actual.getEmail()).isEqualTo(email);
        assertThat(actual.getPassword()).isEqualTo(password);
    }
}