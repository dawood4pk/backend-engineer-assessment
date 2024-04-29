package com.midas.app.controllers;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.midas.app.models.Account;
import com.midas.app.services.AccountService;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private AccountService accountService;

  @Test
  void testCreateUserAccount() throws Exception {
    Account account = new Account();
    account.setId(UUID.randomUUID());
    account.setEmail("john.doe@example.com");
    account.setFirstName("John");
    account.setLastName("Doe");

    given(accountService.createAccount(any(Account.class))).willReturn(account);

    mockMvc
        .perform(
            post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@example.com\"}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.email", is("john.doe@example.com")));
  }
}
