package com.nttdata.mapper;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "USERS")
public class UserDao {
	@Id
	@Column(name = "UUID", nullable = false)
	private String uuid;
	
	@Column(name = "EMAIL", nullable = false, unique = true)
	private String email;

	@Column(name = "NAME", nullable = false)
	private String name;

	@Column(name = "PASSWORD", nullable = false)
	private String password;

	@Column(name = "CREATED_DATE", nullable = false)
	private String created;

	@Column(name = "MODIFIED_DATE", nullable = false)
	private String modified;

	@Column(name = "LASTLOGIN_DATE", nullable = false)
	private String last_login;
	
	@Lob
	@Column(name = "TOKEN", nullable = false, length=16777216)
	private String token;

	@Column(name = "IS_ACTIVE", nullable = false)
	private Boolean isActive;

	@OneToMany(mappedBy="user")
	private List<PhoneDao> phones;
}
