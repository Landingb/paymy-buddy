package com.oc.paymybuddy.repositories;

import com.oc.paymybuddy.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface UserRepository extends JpaRepository<User, Long> {

	public User findByEmail(String email);

	@Query("SELECT CASE "
			+ "WHEN COUNT(u) > 0 THEN true"
			+ " ELSE false END "
			+ "FROM User u "
			+ "WHERE u.email = :email")
	public Boolean existsByEmail(@Param("email") String email);

	@Query(value =
			"SELECT * "
					+ "FROM user u INNER JOIN user_connections ucon "
					+ "ON u.id = ucon.connection_id "
					+ "WHERE ucon.user_id = :id",
			nativeQuery = true)
	public Page<User> findConnectionById(@Param("id") Long id, Pageable pageRequest);

}
