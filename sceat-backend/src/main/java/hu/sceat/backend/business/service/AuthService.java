package hu.sceat.backend.business.service;

import hu.sceat.backend.business.dto.DtoMapper;
import hu.sceat.backend.business.dto.UserDto;
import hu.sceat.backend.business.fail.CommonFail;
import hu.sceat.backend.business.fail.Fail;
import hu.sceat.backend.persistence.Validation;
import hu.sceat.backend.persistence.entity.User;
import hu.sceat.backend.persistence.repository.UserRepository;
import hu.sceat.backend.util.Try;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
	
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepo;
	
	public AuthService(PasswordEncoder passwordEncoder, UserRepository userRepo) {
		this.passwordEncoder = passwordEncoder;
		this.userRepo = userRepo;
	}
	
	//login is handled by Spring Security
	
	//logout is handled by Spring Security
	
	@Transactional
	public Try<UserDto, Fail> register(String username, String password) {
		return Try.<String, Fail>success(username)
				.filter(n -> n.matches(Validation.USERNAME_REGEX),
						CommonFail.invalidInputFormat("username"))
				.map(n -> User.create(n, passwordEncoder.encode(password)))
				.filter(u -> userRepo.findByUsername(u.getUsername()).isEmpty(),
						CommonFail.invalidInputAlreadyTaken("username"))
				.map(userRepo::save)
				.map(DtoMapper.INSTANCE::toUser);
	}
}
