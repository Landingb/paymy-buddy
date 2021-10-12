package com.oc.paymybuddy.repositories;

import com.oc.paymybuddy.domain.BankTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface BankTransactionRepository extends JpaRepository<BankTransaction, Long>{
	
	@Query(value = 
			"SELECT * "
			+ "FROM transactions_bank t "
			+ "WHERE t.user_id = :userid",
			nativeQuery = true)
	public Page<BankTransaction> findBankTransactionByUserId(@Param("userid") Long userid, Pageable pageRequest);
	
}
