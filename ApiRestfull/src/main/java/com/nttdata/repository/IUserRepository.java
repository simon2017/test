package com.nttdata.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nttdata.mapper.UserDao;

public interface IUserRepository extends JpaRepository<UserDao, String> {

	@Query("select u FROM UserDao u WHERE u.email = ?1")
	Optional<UserDao> findByEmail(String email);
	
}
