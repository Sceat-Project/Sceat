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
import hu.sceat.backend.util.Unit;
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
	public Try<UserDto, Fail> registerServer(String email, String password, String name, Long organization) {
		return registrationValidateDetails(email, password, name)
				.flatMap(n -> organizationRepo.findById(organization),
						CommonFail.notFound("organization " + organization))
				.map(o -> User.createServer(email, passwordEncoder.encode(password), name, o))
				.map(userRepo::save)
				.map(DtoMapper.INSTANCE::toUser);
	}
	
	@Transactional
	public Try<UserDto, Fail> registerConsumer(String email, String password, String name, Long organization) {
		return registrationValidateDetails(email, password, name)
				.flatMap(n -> organizationRepo.findById(organization),
						CommonFail.notFound("organization " + organization))
				.map(o -> User.createConsumer(email, passwordEncoder.encode(password), name, o))
				.map(userRepo::save)
				.map(DtoMapper.INSTANCE::toUser);
	}
	
	private Try<Unit, Fail> registrationValidateDetails(String email, String password, String name) {
		return Try.<Unit, Fail>success(Unit.get())
				.filter(u -> email.matches(Validation.EMAIL_REGEX),
						CommonFail.invalidInputFormat("email"))
				.filter(u -> name.matches(Validation.GENERAL_NAME_REGEX),
						CommonFail.invalidInputFormat("name"))
				.filter(u -> userRepo.findByEmail(email).isEmpty(),
						CommonFail.invalidInputAlreadyTaken("email"));
	}
}
