package com.nttdata.service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nttdata.dto.Phone;
import com.nttdata.dto.RegisterResponse;
import com.nttdata.dto.UserCredentials;
import com.nttdata.dto.UserData;
import com.nttdata.mapper.PhoneDao;
import com.nttdata.mapper.UserDao;
import com.nttdata.repository.IPhoneRepository;
import com.nttdata.repository.IUserRepository;

@Service
public class UserService {
	private static final DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	@Value("${common.regex.mail}")
	private String MAIL_RGX;

	@Value("${common.regex.password}")
	private String PSW_RGX;

	@Autowired
	IUserRepository repository;
	@Autowired
	IPhoneRepository phoneRepository;
	@Autowired
	JWTTokenService jwtTokenService;

	public RegisterResponse register(UserData userData) throws Exception {
		RegisterResponse rr = null;

		// Si caso el correo conste en la base de datos, deberá retornar un error "El
		// correo ya registrado".

		// Reglas ->

		if (repository.findByEmail(userData.getEmail()).isPresent())
			throw new Exception("El correo ya registrado");

		if (userData.getEmail() == null
				|| (userData.getEmail() != null && !Pattern.matches(MAIL_RGX, userData.getEmail())))
			throw new Exception("Correo no cumple con Regex");

		if (userData.getPassword() == null
				|| (userData.getPassword() != null && !Pattern.matches(PSW_RGX, userData.getPassword())))
			throw new Exception("Password no cumple con Regex");
		// <-

		UserDao userDao = new UserDao();
		// campos calculados->
		// fecha actual
		Date date = new Date();
		String created = formatter.format(date);

		userDao.setUuid(this.generateUUID(userData));
		userDao.setCreated(created);
		userDao.setModified(created);
		userDao.setLast_login(created);
		userDao.setToken(jwtTokenService.getJWTToken(userDao.getUuid()).getBytes(StandardCharsets.UTF_8));
		userDao.setIsActive(new Boolean(true));
		// <-
		userDao.setEmail(userData.getEmail());
		userDao.setName(userData.getName());
		userDao.setPassword(userData.getPassword());

		List<PhoneDao> phones = new CopyOnWriteArrayList<PhoneDao>();
		userData.getPhones().forEach(t -> phones.add(phoneToDAO(t, userDao)));

		userDao.setPhones(phones);

		repository.save(userDao);
		phoneRepository.saveAll(phones);

		phoneRepository.flush();
		repository.flush();

		rr = mapResponse(userDao);

		return rr;
	}

	public RegisterResponse updateByEmail(UserData userData) throws Exception {
		Optional<UserDao> optional = repository.findByEmail(userData.getUuid());
		if (!optional.isPresent())
			throw new Exception("Usuario no existe");
		userData.setUuid(optional.get().getUuid());
		return update(userData);
	}

	public RegisterResponse update(UserData userData) throws Exception {
		RegisterResponse rr = null;

		// Si caso el correo conste en la base de datos, deberá retornar un error "El
		// correo ya registrado".

		Optional<UserDao> optional = repository.findById(userData.getUuid());
		if (!optional.isPresent())
			throw new Exception("Usuario no existe");

		UserDao userDao = optional.get();

		// Reglas ->
		if (userData.getEmail() == null
				|| (userData.getEmail() != null && !Pattern.matches(MAIL_RGX, userData.getEmail())))
			throw new Exception("Correo no cumple con Regex");

		/*
		 * if (userData.getPassword() == null || (userData.getPassword() != null &&
		 * !Pattern.matches(PSW_RGX, userData.getPassword()))) throw new
		 * Exception("Password no cumple con Regex");
		 */
		// <-
		// campos calculados->
		// fecha actual
		Date date = new Date();
		String created = formatter.format(date);

		userDao.setModified(created);

		// <-
		userDao.setEmail(userData.getEmail());
		userDao.setName(userData.getName());
		// userDao.setPassword(userData.getPassword());

		List<PhoneDao> phones = userDao.getPhones();

		userData.getPhones().forEach(t -> phones.add(phoneToDAO(t, userDao)));

		userDao.setPhones(phones);

		repository.save(userDao);
		repository.flush();

		rr = mapResponse(userDao);

		return rr;
	}

	private String generateUUID(UserData userData) {
		return UUID.randomUUID().toString();
	}

	private RegisterResponse mapResponse(UserDao userDao) throws UnsupportedEncodingException {
		RegisterResponse rr = new RegisterResponse();

		rr.setCreated(userDao.getCreated());
		rr.setUuid(userDao.getUuid());
		rr.setIsactive(userDao.getIsActive().booleanValue());
		rr.setLast_login(userDao.getLast_login());
		rr.setModified(userDao.getModified());
		rr.setToken(new String(userDao.getToken(), StandardCharsets.UTF_8));
		rr.setName(userDao.getName());
		rr.setEmail(userDao.getEmail());
		rr.setPassword(userDao.getPassword());
		List<Phone> phones = new CopyOnWriteArrayList<Phone>();
		userDao.getPhones().forEach(t -> phones.add(phoneToData(t)));
		rr.setPhones(phones);
		return rr;
	}

	private PhoneDao phoneToDAO(Phone dto, UserDao user) {
		PhoneDao response = new PhoneDao();
		response.setCitycode(dto.getCitycode());
		response.setCountrycode(dto.getCountrycode());
		response.setNumber(dto.getNumber());
		response.setUser(user);

		return response;
	}

	private Phone phoneToData(PhoneDao dao) {
		Phone response = new Phone();
		response.setCitycode(dao.getCitycode());
		response.setCountrycode(dao.getCountrycode());
		response.setNumber(dao.getNumber());

		return response;
	}

	public UserData authenticate(UserCredentials userCredentials) throws Exception {
		Optional<UserDao> optional = repository.findByEmail(userCredentials.getEmail());
		if (!optional.isPresent())
			throw new Exception("Usuario no existe");

		UserDao user = optional.get();

		// validar credencial
		if (userCredentials.getPassword().equals(user.getPassword()) == false)
			throw new Exception("Error de login, contraseña incorrecta");

		Date date = new Date();
		user.setLast_login(formatter.format(date));
		// se establece token para usuario, o se entrega uno nuevo si ya existe usuario
		String token = jwtTokenService.getJWTToken(user.getUuid());
		user.setToken(token.getBytes(StandardCharsets.UTF_8));
		// luego salvamos el token en bd
		//repository.save(user);

		return userToData(user);
	}

	private UserData userToData(UserDao dao) {

		UserData data = new UserData();
		data.setEmail(dao.getEmail());
		data.setName(dao.getName());
		data.setPassword(dao.getPassword());
		data.setToken(new String(dao.getToken(), StandardCharsets.UTF_8));
		data.setUuid(dao.getUuid());
		List<Phone> phones = new CopyOnWriteArrayList<Phone>();
		dao.getPhones().forEach(t -> phones.add(phoneToData(t)));
		data.setPhones(phones);

		return data;
	}
}
