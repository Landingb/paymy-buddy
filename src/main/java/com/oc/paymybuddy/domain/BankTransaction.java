package com.oc.paymybuddy.domain;

import com.sun.istack.NotNull;

import javax.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;



@Entity
@Table(name = "transactions_bank")
public class BankTransaction {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;


	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

	@NotNull
	private String bankaccountnumber;

	@NotNull
	private LocalDateTime datetime;

	@NotNull
	private BigDecimal amount;

	@NotNull
	private Currency currency;




}
