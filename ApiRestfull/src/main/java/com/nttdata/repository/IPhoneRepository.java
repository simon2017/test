package com.nttdata.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nttdata.mapper.PhoneDao;

public interface IPhoneRepository extends JpaRepository<PhoneDao, Long> {
	
	@Query("select p FROM PhoneDao p WHERE p.user.uuid = ?1")
	Optional<List<PhoneDao>> findByUUID(String UUID);
	
}
