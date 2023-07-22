package com.nttdata.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nttdata.mapper.PhoneDao;

public interface IPhoneRepository extends JpaRepository<PhoneDao, Long> {

}
