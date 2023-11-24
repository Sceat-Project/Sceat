package hu.sceat.backend.business.service;

import hu.sceat.backend.business.dto.DtoMapper;
import hu.sceat.backend.business.dto.UserDto;
import hu.sceat.backend.business.fail.CommonFail;
import hu.sceat.backend.business.fail.Fail;
import hu.sceat.backend.persistence.Validation;
import hu.sceat.backend.persistence.entity.User;
import hu.sceat.backend.persistence.repository.OrganizationRepository;
import hu.sceat.backend.persistence.repository.UserRepository;
import hu.sceat.backend.util.Try;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
	
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepo;
	private final OrganizationRepository organizationRepo;
	
	public AuthService(PasswordEncoder passwordEncoder, UserRepository userRepo,
			OrganizationRepository organizationRepo) {
		this.passwordEncoder = passwordEncoder;
		this.userRepo = userRepo;
		this.organizationRepo = organizationRepo;
	}
	
	//login is handled by Spring Security
	
	//logout is handled by Spring Security
	
	@Transactional
	public Try<UserDto, Fail> registerServer(String username, String password, Long organization) {
		return Try.<String, Fail>success(username)
				.filter(n -> n.matches(Validation.ID_NAME_REGEX),
						CommonFail.invalidInputFormat("username"))
				.filter(n -> userRepo.findByUsername(n).isEmpty(),
						CommonFail.invalidInputAlreadyTaken("username"))
				.flatMap(n -> organizationRepo.findById(organization),
						CommonFail.notFound("organization " + organization))
				.map(o -> User.createServer(username, passwordEncoder.encode(password), o))
				.map(userRepo::save)
				.map(DtoMapper.INSTANCE::toUser);
	}
	
	@Transactional
	public Try<UserDto, Fail> registerConsumer(String username, String password, Long organization) {
		return Try.<String, Fail>success(username)
				.filter(n -> n.matches(Validation.ID_NAME_REGEX),
						CommonFail.invalidInputFormat("username"))
				.filter(n -> userRepo.findByUsername(n).isEmpty(),
						CommonFail.invalidInputAlreadyTaken("username"))
				.flatMap(n -> organizationRepo.findById(organization),
						CommonFail.notFound("organization " + organization))
				.map(o -> User.createConsumer(username, passwordEncoder.encode(password), o))
				.map(userRepo::save)
				.map(DtoMapper.INSTANCE::toUser);
	}
}
