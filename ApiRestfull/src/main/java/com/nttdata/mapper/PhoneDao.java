package com.nttdata.mapper;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "PHONE")
public class PhoneDao {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PHONE_ID", nullable = false)
	private Long phoneId;

	@Column(name = "NUMBER", nullable = false)
	private String number;

	@Column(name = "CITY_CODE", nullable = false)
	private Integer citycode;

	@Column(name = "COUNTRY_CODE", nullable = false)
	private Integer countrycode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UUID", nullable = false)
	private UserDao user;

}
