package com.oc.paymybuddy.domain;


import com.sun.istack.NotNull;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Currency;


public class BankTransactionForm {
	
	@NotBlank
	private String getOrSendRadioOptions;
	
	@NotNull
	@Positive
	@Max(99999) //due to Mysql setting : DECIMAL(10,2) to store amount
	private BigDecimal amount;
	
	//validation is done in BankTransactionController
	private Currency currency;

	public String getGetOrSendRadioOptions() {
		return getOrSendRadioOptions;
	}

	public void setGetOrSendRadioOptions(String getOrSendRadioOptions) {
		this.getOrSendRadioOptions = getOrSendRadioOptions;
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
