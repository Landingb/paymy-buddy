package com.oc.paymybuddy.IT;

import com.oc.paymybuddy.domain.Transaction;
import com.oc.paymybuddy.domain.User;
import com.oc.paymybuddy.service.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TransactionControllerTestIT {

    Logger logger = LoggerFactory.getLogger(TransactionControllerTestIT.class);

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserService userservice;

    @BeforeEach
    void initializeDatabaseValues () {
        User userTest = userservice.findByEmail("test@mail.com");
        userTest.setAmount(new BigDecimal("1000"));
        userservice.update(userTest);

        User userAnotherTest = userservice.findByEmail("anothertest@mail.com");
        userAnotherTest.setAmount(new BigDecimal("1000"));
        userservice.update(userAnotherTest);
    }

    @Test
    @WithMockUser(username="test@mail.com") //test@mail.com exists in our test database
    void getUsertransactionShouldReturnOK() throws Exception {
        mvc.perform(get("/usertransaction")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="test@mail.com") //test@mail.com exists in our test database
    void postUsertransaction() throws Exception {
        mvc.perform(post("/usertransaction")
                .param("userDestinationId", "54")
                .param("amount", "100")
                .param("currency", "USD")
                .with(csrf())
        )//.andDo(print())
                .andExpect(status().is3xxRedirection());

        User userTest = userservice.findByEmail("test@mail.com");
        User userAnotherTest = userservice.findByEmail("anothertest@mail.com");
        assertEquals(new BigDecimal("900.00"),userTest.getAmount(), "1000 - user transaction");
        assertEquals(new BigDecimal("1099.50"),userAnotherTest.getAmount(), "1000 + user transaction - fees (0.5%)");

        Transaction transaction = userTest.getUsertransactions().iterator().next();
        assertEquals(userTest, transaction.getUserSource());
        assertEquals(userAnotherTest, transaction.getUserDestination());
        //get time in second between transaction datetime and now :
        long durationInSec = Duration.between(transaction.getDatetime(), LocalDateTime.now()).getSeconds();
        assertTrue(durationInSec<2, "time difference between transaction time and now can't be more than 2s");
        assertEquals(new BigDecimal("99.50"), transaction.getAmount());
        assertEquals(Currency.getInstance("USD"), transaction.getCurrency());
        assertEquals(new BigDecimal("0.50"), transaction.getFees());


    }

}