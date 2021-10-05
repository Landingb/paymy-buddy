package com.oc.paymybuddy.services;

import com.oc.paymybuddy.domain.User;
import com.oc.paymybuddy.exceptions.AmountException;
import com.oc.paymybuddy.utils.page.Paged;

import java.math.BigDecimal;
import java.util.Currency;

public interface UserService {

    void create(User user);

    void update (User user);

    User findByEmail(String email);

    User findById(Long id);

    Boolean existsByEmail(String email);

    User getCurrentUser();

    BigDecimal sumAmountCalculate(User user, BigDecimal amount, Currency currency) throws AmountException;

    Paged<User> getCurrentUserConnectionPage(int pageNumber, int size);



}
