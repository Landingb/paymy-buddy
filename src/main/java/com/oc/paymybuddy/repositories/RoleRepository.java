package com.oc.paymybuddy.repositories;

import com.oc.paymybuddy.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Long>{
	Role findByRolename(String rolename); 
}
