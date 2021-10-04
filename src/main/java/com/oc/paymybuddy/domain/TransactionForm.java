package com.oc.paymybuddy.domain;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Currency;


public class TransactionForm {

	@NotNull
	//A cross-record validation is also done in UserTransactionController
	private Long userDestinationId;

	@NotNull
	@Positive
	@Max(99999999) //due to Mysql setting : DECIMAL(10,2) to store amount
	private BigDecimal amount;

	@NotNull
	//A cross-record validation is also done in UserTransactionController
	private Currency currency;

	public Long getUserDestinationId() {
		return userDestinationId;
	}

	public void setUserDestinationId(Long userDestinationId) {
		this.userDestinationId = userDestinationId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
}
