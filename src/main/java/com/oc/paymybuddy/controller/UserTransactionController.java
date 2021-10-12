package com.oc.paymybuddy.controller;

import java.math.BigDecimal;
import java.util.Map;

import javax.validation.Valid;


import com.oc.paymybuddy.config.CurrenciesAllowed;
import com.oc.paymybuddy.domain.Transaction;
import com.oc.paymybuddy.domain.TransactionForm;
import com.oc.paymybuddy.domain.User;
import com.oc.paymybuddy.exceptions.AmountException;
import com.oc.paymybuddy.service.interfaces.CalculationService;
import com.oc.paymybuddy.service.interfaces.UserService;
import com.oc.paymybuddy.service.interfaces.UserTransactionService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class UserTransactionController {

    Logger logger = LoggerFactory.getLogger(UserTransactionController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private UserTransactionService userTransactionService;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CurrenciesAllowed currenciesAllowed;
    @Autowired
    private CalculationService calculationService;

    @GetMapping("/transaction")
    public String getUsertransaction(
            @RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size,
            Model model) {
        logger.info("GET: /transaction");

        User user = userService.getCurrentUser();
        model.addAttribute("user", user);//needed to display current user amount + currency
        model.addAttribute("paged", userTransactionService.getCurrentUserUserTransactionPage(pageNumber, size));

        TransactionForm transactionFormDTO = new TransactionForm();
        transactionFormDTO.setCurrency(user.getCurrency()); //sets by default the form currency to currency of the user.
        model.addAttribute("transactionFormDTO", transactionFormDTO);
        return "transaction";
    }


    @Transactional
    @PostMapping("/transaction")
    public String postUsertransactionGetMoney(
            @Valid @ModelAttribute("transactionFormDTO") TransactionForm transactionFormDTO,
            BindingResult bindingResult,
            Model model) {

        logger.info("POST: /transaction");
        User sourceUser = userService.getCurrentUser();
        model.addAttribute("user", sourceUser);//list of transactions + preferred currency
        model.addAttribute("paged", userTransactionService.getCurrentUserUserTransactionPage(1, 5));

        if (bindingResult.hasErrors()) {
            return "transaction";
        }

        User userDestination = userService.findById(transactionFormDTO.getUserDestinationId());


        if ( !sourceUser.getConnections().contains(userDestination) ) {
            logger.debug("Failure: unknown buddy");
            bindingResult.rejectValue("userDestinationId", "userDestinationNotABuddy", "Please select a buddy !");
            return "transaction";
        }


        if ( !currenciesAllowed.getCurrenciesAllowedList().contains(transactionFormDTO.getCurrency()) ) {
            bindingResult.rejectValue("currency", "NotAllowedCurrency", "This currency is not allowed.");
            return "transaction";
        }


        Map<String, BigDecimal> feesMap = calculationService.calculateFees(transactionFormDTO.getAmount());


        Transaction transaction = convertToEntity(transactionFormDTO, userDestination);


        BigDecimal sourceUserAmountAfterTransaction;
        BigDecimal destinationUserAmountAfterTransaction;
        try {
            sourceUserAmountAfterTransaction = userService.sumAmountCalculate(
                    sourceUser,
                    transaction.getAmount().negate(), //Must be negative for sourceUser
                    transaction.getCurrency()
            );
            destinationUserAmountAfterTransaction = userService.sumAmountCalculate(
                    userDestination,
                    feesMap.get("finalAmount"),
                    transaction.getCurrency()
            );

        } catch (AmountException e) {
            logger.debug("UserAmountException");
            bindingResult.rejectValue("amount", e.getErrorCode(), e.getDefaultMessage());
            return "transaction";
        }


        sourceUser.setAmount(sourceUserAmountAfterTransaction);
        userDestination.setAmount(destinationUserAmountAfterTransaction);


        userTransactionService.create(transaction, feesMap);


        return "redirect:/transaction";
    }



    private Transaction convertToEntity(TransactionForm transactionFormDTO, User userDestination) {

        logger.debug("DTO object to Entity conversion");


        Transaction transaction = modelMapper.map(transactionFormDTO, Transaction.class);

        transaction.setId(null);

        transaction.setUserDestination(userDestination);

        return transaction;
    }

}