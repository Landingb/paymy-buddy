package com.oc.paymybuddy.controller;


import com.oc.paymybuddy.config.CurrenciesAllowed;
import com.oc.paymybuddy.domain.BankTransaction;
import com.oc.paymybuddy.domain.BankTransactionForm;
import com.oc.paymybuddy.domain.User;
import com.oc.paymybuddy.exceptions.AmountException;
import com.oc.paymybuddy.service.interfaces.BankTransactionService;
import com.oc.paymybuddy.service.interfaces.UserService;
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

import javax.validation.Valid;
import java.math.BigDecimal;

@Controller
public class BankTransactionController {

    Logger logger = LoggerFactory.getLogger(BankTransactionController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private BankTransactionService bankTransactionService;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CurrenciesAllowed currenciesAllowed;

    @GetMapping("/banktransaction")
    public String getBanktransaction(
            @RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size,
            Model model) {
        logger.info("GET: /banktransaction");

        User user = userService.getCurrentUser();
        model.addAttribute("user", user);//needed to display current user amount + currency
        model.addAttribute("paged", bankTransactionService.getCurrentUserBankTransactionPage(pageNumber, size));

        BankTransactionForm bankTransactionFormDTO = new BankTransactionForm();
        bankTransactionFormDTO.setCurrency(user.getCurrency()); //sets by default the form currency to currency of the user.
        bankTransactionFormDTO.setGetOrSendRadioOptions("send"); //sets by default the form GetOrSendRadioOptions to "send".
        model.addAttribute("banktransactionFormDTO",bankTransactionFormDTO);
        return "banktransaction";
    }

    @Transactional
    @PostMapping("/banktransaction")
    public String postBanktransactionGetMoney(
            @Valid @ModelAttribute("banktransactionFormDTO") BankTransactionForm bankTransactionFormDTO,
            BindingResult bindingResult,
            Model model) {

        logger.info("POST: /banktransaction");
        User connectedUser = userService.getCurrentUser();
        model.addAttribute("user", connectedUser);//list of transactions + preferred currency
        model.addAttribute("paged", bankTransactionService.getCurrentUserBankTransactionPage(1, 5));

        if (bindingResult.hasErrors()) {
            return "banktransaction";
        }

        //cross-record validation : Currency not allowed in our list
        if ( !currenciesAllowed.getCurrenciesAllowedList().contains(bankTransactionFormDTO.getCurrency()) ) {
            bindingResult.rejectValue("currency", "UnknownCurrency", "This currency is not allowed.");
            return "banktransaction";
        }

        BankTransaction bankTransaction = convertToEntity(bankTransactionFormDTO);

        //cross-record validation : calculate user amount after transaction, UserAmountException thrown if amount is invalid
        BigDecimal connectedUserAmountAfterTransaction;
        try {
            connectedUserAmountAfterTransaction = userService.sumAmountCalculate(connectedUser, bankTransaction.getAmount(), bankTransaction.getCurrency());
        } catch (AmountException e) {
            logger.debug("UserAmountException");
            bindingResult.rejectValue("amount", e.getErrorCode(), e.getDefaultMessage());
            return "banktransaction";
        }

        //update user amount:
        connectedUser.setAmount(connectedUserAmountAfterTransaction);

        //create banktransaction:
        bankTransactionService.create(bankTransaction);


        //redirection do not use the current Model, it goes to GET /bantransaction
        return "redirect:/banktransaction";
    }



    private BankTransaction convertToEntity(BankTransactionForm bankTransactionFormDTO) {
        BankTransaction bankTransaction = modelMapper.map(bankTransactionFormDTO, BankTransaction.class);

        //If money sent to bank then amount becomes negative:
        if (bankTransactionFormDTO.getGetOrSendRadioOptions().equalsIgnoreCase("send")) {
            bankTransaction.setAmount(bankTransactionFormDTO.getAmount().negate());
        }
        return bankTransaction;
    }


}
