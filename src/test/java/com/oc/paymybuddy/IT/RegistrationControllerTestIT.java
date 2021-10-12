package com.oc.paymybuddy.IT;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class RegistrationControllerTestIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void GETregistrationTest_ShouldReturnOK() throws Exception {
        mvc.perform(get("/registration"))//.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void POSTregistrationTest_ShouldReturnOK() throws Exception {
        mvc.perform(
                post("/registration")
                        .param("firstname", "firstname")
                        .param("lastname", "lastname")
                        .param("email", "register@mail.com")
                        .param("password", "0000")
                        .param("passwordconfirm", "0000")
                        .param("bankaccountnumber", "12345")
                        .param("currency", "GBP")
                        .with(csrf())
        )
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
        ;
    }
}