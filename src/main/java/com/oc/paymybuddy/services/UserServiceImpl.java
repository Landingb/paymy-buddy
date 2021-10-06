package com.oc.paymybuddy.services;

import com.oc.paymybuddy.domain.Role;
import com.oc.paymybuddy.domain.User;
import com.oc.paymybuddy.exceptions.AmountException;
import com.oc.paymybuddy.repositories.RoleRepository;
import com.oc.paymybuddy.repositories.UserRepository;
import com.oc.paymybuddy.utils.page.Paged;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashSet;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private CalculationService calculationService;
    @Autowired
    private LocalTimeService localTimeService;
    @Autowired
    private PagingService pagingService;

    @Override
    public void create(User user) {
        logger.debug("Calling create(User)");
        String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        user.setEnabled(true);
        user.setInscriptiondatetime(localTimeService.now());
        user.setAmount(new BigDecimal(0));

        HashSet<Role> hashSetRoleUserOnly = new HashSet<>();
        hashSetRoleUserOnly.add(roleRepository.findByRolename("USER"));
        userRepository.save(user);

    }

    @Override
    public void update(User user) {
        logger.debug("calling update(User)");
        userRepository.save(user);

    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findById(Long id) {
        logger.debug("Calling findById({})", id);
        Optional<User> optuser = userRepository.findById(id);
        if (optuser.isEmpty()) {
            return null;
        }

        return optuser.get();

    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User getCurrentUser() {
        return findByEmail(securityService.getCurrentUserDetailsUserName());
    }

    @Override
    public BigDecimal sumAmountCalculate(User user, BigDecimal amount, Currency currency) throws AmountException {
        BigDecimal resultAmount = calculationService.sumCurrencies(user.getAmount(), user.getCurrency(), amount, currency);
        if (resultAmount.compareTo(new BigDecimal(0)) < 0) {
            throw new AmountException("InsufficientFunds", "This amount exceeds your account value.");
        }
        if (resultAmount.compareTo(new BigDecimal(9999999)) > 0) {
            throw new AmountException("UserAmountExceedsMax", "Destination account can not exceed max value.");
        }

        return resultAmount;
    }

    @Override
    public Paged<User> getCurrentUserConnectionPage(int pageNumber, int size) {
    PageRequest request = PageRequest.of(pageNumber -1, size, Sort.by(Sort.Direction.DESC, "id"));
    Page<User> page = userRepository.findConnectionById(getCurrentUser().getId(), request);
    return new Paged<>(page, pagingService.of(page.getTotalPages(), pageNumber));
    }
}