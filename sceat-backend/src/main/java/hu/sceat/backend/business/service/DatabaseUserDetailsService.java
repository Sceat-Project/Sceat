package hu.sceat.backend.business.service;

import hu.sceat.backend.business.PrincipalUser;
import hu.sceat.backend.persistence.entity.User;
import hu.sceat.backend.persistence.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final UserRepository userRepository;
	
	public DatabaseUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Transactional
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByUsername(username);
		if (user.isPresent()) {
			logger.debug("Loaded user {} for username '{}'", user.get(), username);
			return new PrincipalUser(user.get());
		} else {
			logger.debug("No user found by username '{}'", username);
			throw new UsernameNotFoundException("User not found");
		}
	}
}
