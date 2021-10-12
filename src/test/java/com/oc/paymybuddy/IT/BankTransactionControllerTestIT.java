package com.oc.paymybuddy.IT;

import com.oc.paymybuddy.domain.BankTransaction;
import com.oc.paymybuddy.domain.User;
import com.oc.paymybuddy.service.interfaces.BankTransactionService;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BankTransactionControllerTestIT {

    Logger logger = LoggerFactory.getLogger(TransactionControllerTestIT.class);

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserService userservice;

    @Autowired
    private BankTransactionService bankTransactionService;

    @BeforeEach
    void initializeDatabaseValues () {
        logger.debug("@BeforeEach");
        User userTest = userservice.findByEmail("test@mail.com");
        //Passage du montant a 1000:
        userTest.setAmount(new BigDecimal("1000"));
        userservice.update(userTest);
        //suppression de tous les BankTransactions
        bankTransactionService.deleteAll();
        logger.debug("Out @BeforeEach");

    }

    @Test
    @WithMockUser(username="test@mail.com") //test@mail.com exists in our test database
    void getBanktransactionShouldReturnOK() throws Exception {
        mvc.perform(get("/banktransaction"))//.andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(username="test@mail.com") //test@mail.com exists in our test database
    void postBanktransactionGetMoney() throws Exception {
        mvc.perform(post("/banktransaction")
                .param("getOrSendRadioOptions", "get")
                .param("amount", "100")
                .param("currency", "USD")
                .with(csrf())
        )//.andDo(print())
                .andExpect(status().is3xxRedirection());

        User userTest = userservice.findByEmail("test@mail.com");
        assertEquals(new BigDecimal("1100.00"),userTest.getAmount(), "1000 + bank transaction");
        Set<BankTransaction> banktransactions = userTest.getBanktransactions();
        assertEquals(1,banktransactions.size());

        BankTransaction bankTransaction = banktransactions.iterator().next();
        assertEquals(userTest, bankTransaction.getUser());
        //get time in second between transaction datetime and now :
        long durationInSec = Duration.between(bankTransaction.getDatetime(), LocalDateTime.now()).getSeconds();
        assertTrue(durationInSec<2, "time difference between transaction time and now can't be more than 2s");
        assertEquals(new BigDecimal("100"), bankTransaction.getAmount());
        assertEquals(Currency.getInstance("USD"), bankTransaction.getCurrency());

    }

    @Test
    @WithMockUser(username="test@mail.com") //test@mail.com exists in our test database
    void postBanktransactionSendMoney() throws Exception {
        mvc.perform(post("/banktransaction")
                .param("getOrSendRadioOptions", "send")
                .param("amount", "100")
                .param("currency", "USD")
                .with(csrf())
        )//.andDo(print())
                .andExpect(status().is3xxRedirection());

        User userTest = userservice.findByEmail("test@mail.com");
        assertEquals(new BigDecimal("900.00"),userTest.getAmount(), "1000 - bank transaction");
        Set<BankTransaction> banktransactions = userTest.getBanktransactions();
        assertEquals(1,banktransactions.size());

        BankTransaction bankTransaction = banktransactions.iterator().next();
        assertEquals(userTest, bankTransaction.getUser());
        //get time in second between transaction datetime and now :
        long durationInSec = Duration.between(bankTransaction.getDatetime(),LocalDateTime.now()).getSeconds();
        assertTrue(durationInSec<2, "time difference between transaction time and now can't be more than 2s");
        assertEquals(new BigDecimal("-100"), bankTransaction.getAmount());
        assertEquals(Currency.getInstance("USD"), bankTransaction.getCurrency());


    }



}