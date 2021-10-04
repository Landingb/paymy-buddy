package com.oc.paymybuddy.domain;

import com.sun.istack.NotNull;

import javax.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;



@Entity
@Table(name = "transactions_user")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)//child entity, owner of the relationship
	@JoinColumn(name = "usersource_id", nullable = false)
	private User userSource;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)//child entity, owner of the relationship
	@JoinColumn(name = "userdestination_id", nullable = false)
	private User userDestination;

	@NotNull
	private LocalDateTime datetime;
	@NotNull
	private BigDecimal amount;
	@NotNull
	private Currency currency;
	@NotNull
	private BigDecimal fees;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUserSource() {
		return userSource;
	}

	public void setUserSource(User userSource) {
		this.userSource = userSource;
	}

	public User getUserDestination() {
		return userDestination;
	}

	public void setUserDestination(User userDestination) {
		this.userDestination = userDestination;
	}

	public LocalDateTime getDatetime() {
		return datetime;
	}

	public void setDatetime(LocalDateTime datetime) {
		this.datetime = datetime;
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

	public BigDecimal getFees() {
		return fees;
	}

	public void setFees(BigDecimal fees) {
		this.fees = fees;
	}
}
