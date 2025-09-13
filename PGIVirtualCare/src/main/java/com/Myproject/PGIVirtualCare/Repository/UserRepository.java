package com.Myproject.PGIVirtualCare.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Myproject.PGIVirtualCare.Model.Users;
import com.Myproject.PGIVirtualCare.Model.Users.UserRole;

public interface UserRepository extends JpaRepository<Users, Long> {

	boolean existsByEmail(String email);

	Users findByEmail(String email);

	List<Users> findAllByRole(UserRole patient);

	Object countByRole(UserRole doctor);
	

}
