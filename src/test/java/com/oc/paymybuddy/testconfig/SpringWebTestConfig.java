package com.oc.paymybuddy.testconfig;


import com.oc.paymybuddy.config.CurrenciesAllowed;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;

@TestConfiguration
public class SpringWebTestConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        User basicUser = new User("user@company.com", "password", Arrays.asList(
                new SimpleGrantedAuthority("ROLE_USER")
        ));
        return new InMemoryUserDetailsManager(Arrays.asList(
                basicUser /*, managerActiveUser*/
        ));
    }

    @Bean
    public CurrenciesAllowed currenciesAllowed() {
        String[] listCurrencies = {"USD","EUR"};
        return new CurrenciesAllowed( listCurrencies );
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
