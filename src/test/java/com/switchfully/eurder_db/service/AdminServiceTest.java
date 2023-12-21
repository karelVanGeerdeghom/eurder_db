package com.switchfully.eurder_db.service;

import com.switchfully.eurder_db.entity.Admin;
import com.switchfully.eurder_db.exception.UnknownAdminEmailException;
import com.switchfully.eurder_db.exception.WrongPasswordException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
class AdminServiceTest {
    @Autowired
    private AdminService adminService;

    @Test
    void givenRightEmailAndRightPassword_whenAuthenticate_thenGetAuthenticatedAdmin() {
        // GIVEN
        String email = "admin@eurder_db.com";
        String password = "admin";

        // WHEN
        Admin actual = adminService.authenticate(email, password);

        // THEN
        assertThat(actual).isInstanceOf(Admin.class);
    }

    @Test
    void givenRightEmailAndWrongPassword_whenAuthenticate_thenThrowWrongPasswordException() {
        // GIVEN
        String email = "admin@eurder_db.com";
        String password = "password";

        // WHEN + THEN
        assertThatThrownBy(() -> adminService.authenticate(email, password)).isInstanceOf(WrongPasswordException.class);
    }

    @Test
    void givenWrongEmailAndRightPassword_whenAuthenticate_thenThrowUnknownAdminEmailException() {
        // GIVEN
        String email = "firstName.lastName@mail.com";
        String password = "admin";

        // WHEN + THEN
        assertThatThrownBy(() -> adminService.authenticate(email, password)).isInstanceOf(UnknownAdminEmailException.class);
    }

    @Test
    void givenWrongEmailAndWrongPassword_whenAuthenticate_thenThrowUnknownAdminEmailException() {
        // GIVEN
        String email = "firstName.lastName@mail.com";
        String password = "password";

        // WHEN + THEN
        assertThatThrownBy(() -> adminService.authenticate(email, password)).isInstanceOf(UnknownAdminEmailException.class);
    }
}