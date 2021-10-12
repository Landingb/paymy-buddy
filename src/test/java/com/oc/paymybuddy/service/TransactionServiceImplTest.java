package com.oc.paymybuddy.service;

import com.oc.paymybuddy.domain.Transaction;
import com.oc.paymybuddy.domain.User;
import com.oc.paymybuddy.repositories.UserTransactionRepository;
import com.oc.paymybuddy.service.impl.LocalDateTimeServiceImpl;
import com.oc.paymybuddy.service.impl.UserTransactionServiceImpl;
import com.oc.paymybuddy.service.interfaces.PagingService;
import com.oc.paymybuddy.service.interfaces.UserService;
import com.oc.paymybuddy.utils.page.Paged;
import com.oc.paymybuddy.utils.page.Paging;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

    @InjectMocks
    UserTransactionServiceImpl userTransactionServiceImpl;

    @Mock
    UserTransactionRepository userTransactionRepository;
    @Mock
    UserService userService;
    @Mock
    LocalDateTimeServiceImpl localDateTimeServiceImpl;
    @Mock
    PagingService pagingService;

    LocalDateTime now;
    User user1;
    User user2;
    Transaction transaction1;

    @BeforeEach
    void initialize() {
        now = LocalDateTime.of(2019, Month.MARCH, 28, 14, 33, 48);
        user1 = new User(1L,"John","Doe","johndoe@mail.com",now,"password",true,"1234",
                null, Currency.getInstance("USD"),new HashSet<>(),new HashSet<>(),new HashSet<>(),new HashSet<>());
        user2 = new User(2L,"Jane","Doe","janedoe@mail.com",now,"password",true,"4321",
                null,Currency.getInstance("USD"),new HashSet<>(),new HashSet<>(),new HashSet<>(),new HashSet<>());

        transaction1 = new Transaction(50L, user1, user2, now, new BigDecimal("90"), Currency.getInstance("USD"), new BigDecimal("10"));
    }

    @Test
    void testCreate() {
        //Arrange

        when(userService.getCurrentUser()).thenReturn(user1);
        when(localDateTimeServiceImpl.now()).thenReturn(now);

        Transaction transactionExpected = new Transaction(null, user1, user2, now, new BigDecimal("90"), Currency.getInstance("USD"), new BigDecimal("10"));

        Transaction transactionToCreate = new Transaction(); //initial data data from DTO
        transactionToCreate.setUserDestination(user2);
        transactionToCreate.setAmount(new BigDecimal("1000"));
        transactionToCreate.setCurrency(Currency.getInstance("USD"));;

        Map<String,BigDecimal> fees = new HashMap<>();
        fees.put("fees", new BigDecimal("10"));
        fees.put("finalAmount", new BigDecimal("90"));

        //Act
        userTransactionServiceImpl.create(transactionToCreate, fees);

        // Assert
        verify(userTransactionRepository, times(1)).save(transactionToCreate);
        assertNull(transactionToCreate.getId());

        assertEquals(transactionExpected.getAmount(), transactionToCreate.getAmount());
        assertEquals(transactionExpected.getCurrency(), transactionToCreate.getCurrency());
        assertEquals(transactionExpected.getDatetime(), transactionToCreate.getDatetime());
        assertEquals(transactionExpected.getFees(), transactionToCreate.getFees());
        assertEquals(transactionExpected.getId(), transactionToCreate.getId());
        assertEquals(transactionExpected.getUserDestination(), transactionToCreate.getUserDestination());
        assertEquals(transactionExpected.getUserSource(), transactionToCreate.getUserSource());

    }


    @Test
    void test_getCurrentUserUserTransactionPage() throws Exception {
        // Arrange

        //Page:
        when(userService.getCurrentUser()).thenReturn(user1);
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        Page<Transaction> expectedPage = new PageImpl<>(transactions);
        when(userTransactionRepository.findUserTransactionByUserId(any(Long.class),any(Pageable.class))).thenReturn(expectedPage);
        //Paging:
        Paging expectedPaging = new Paging(false, false, 1, new ArrayList<>());
        when(pagingService.of(any(Integer.class), any(Integer.class))).thenReturn(expectedPaging);

        // Act
        Paged<Transaction> pagedUserTransaction =  userTransactionServiceImpl.getCurrentUserUserTransactionPage(1, 5);

        // Assert
        assertEquals(expectedPage,pagedUserTransaction.getPage());
        assertEquals(expectedPaging,pagedUserTransaction.getPaging());

    }
}
