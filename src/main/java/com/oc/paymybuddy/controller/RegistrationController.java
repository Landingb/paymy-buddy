package com.oc.paymybuddy.controller;


import com.oc.paymybuddy.config.CurrenciesAllowed;
import com.oc.paymybuddy.domain.User;
import com.oc.paymybuddy.domain.UserForm;
import com.oc.paymybuddy.service.interfaces.SecurityService;
import com.oc.paymybuddy.service.interfaces.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CurrenciesAllowed currenciesAllowed;

    @GetMapping("/registration")
    public String registration(Model model) {
        logger.info("GET: /registration");
        model.addAttribute("userForm", new UserForm());

        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@Valid @ModelAttribute("userForm") UserForm userFormDTO, BindingResult bindingResult, Model model) {
        logger.info("POST: /registration");

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        if ( userService.existsByEmail(userFormDTO.getEmail()) ) {
            bindingResult.rejectValue("email", "", "This email already exists");
            return "registration";
        }

        //UnknownCurrency
        if ( !currenciesAllowed.getCurrenciesAllowedList().contains(userFormDTO.getCurrency()) ) {
            bindingResult.rejectValue("currency", "UnknownCurrency", "This currency is not allowed.");
            return "registration";
        }

        User user = convertToEntity(userFormDTO);
        userService.create(user);

        securityService.autoLogin(userFormDTO.getEmail(), userFormDTO.getPassword());

        return "redirect:/";
    }


    private User convertToEntity(UserForm userFormDTO) {
        User user = modelMapper.map(userFormDTO, User.class);

        return user;
    }

}
