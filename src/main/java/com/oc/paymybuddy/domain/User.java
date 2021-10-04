package com.oc.paymybuddy.domain;



import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Objects;
import java.util.Set;

/**
 * Entity object that represents a User.
 *
 *
 */


@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank
	private String firstname;
	@NotBlank
	private String lastname;
	@NotBlank
	@Email
	private String email;
	private LocalDateTime inscriptiondatetime;
	@NotBlank
	private String password;
	@NotNull
	private Boolean enabled;
	@NotBlank
	private String bankaccountnumber;
	@NotNull
	private BigDecimal amount;
	@NotNull
	private Currency currency;
	
	@ManyToMany //FetchType.LAZY by default
	@JoinTable(
			name = "user_roles", 
			joinColumns = @JoinColumn(name = "users_id"), 
			inverseJoinColumns = @JoinColumn(name = "roles_id"))
	private Set<Role> roles;
	
	@ManyToMany //FetchType.LAZY by default
	@JoinTable(
			name = "user_connections", 
			joinColumns = @JoinColumn(name = "user_id"), 
			inverseJoinColumns = @JoinColumn(name = "connection_id"))
	private Set<User> connections;
	
	@OneToMany(mappedBy = "user", //mappedBy indicates the entity BankTransaction owns the relationship
			fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<BankTransaction> banktransactions;
	
	@OneToMany(mappedBy = "userSource", //mappedBy indicates the entity UserTransaction owns the relationship
			fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Transaction> usertransactions;

	@Override
	public int hashCode() {
		return Objects.hash(email);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof User)) {
			return false;
		}
		User other = (User) obj;
		return Objects.equals(email, other.email);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDateTime getInscriptiondatetime() {
		return inscriptiondatetime;
	}

	public void setInscriptiondatetime(LocalDateTime inscriptiondatetime) {
		this.inscriptiondatetime = inscriptiondatetime;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getBankaccountnumber() {
		return bankaccountnumber;
	}

	public void setBankaccountnumber(String bankaccountnumber) {
		this.bankaccountnumber = bankaccountnumber;
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

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Set<User> getConnections() {
		return connections;
	}

	public void setConnections(Set<User> connections) {
		this.connections = connections;
	}

	public Set<BankTransaction> getBanktransactions() {
		return banktransactions;
	}

	public void setBanktransactions(Set<BankTransaction> banktransactions) {
		this.banktransactions = banktransactions;
	}

	public Set<Transaction> getUsertransactions() {
		return usertransactions;
	}

	public void setUsertransactions(Set<Transaction> usertransactions) {
		this.usertransactions = usertransactions;
	}
}
