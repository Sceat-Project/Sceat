package hu.sceat.backend.persistence.entity;

import hu.sceat.backend.persistence.Validation;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;

import java.util.Optional;

@Entity
@Table(name = "users")
public class User {
	
	public static User createServer(String username, String password, Organization organization) {
		User user = new User();
		user.username = username;
		user.password = password;
		user.serverProfile = Server.create(user, organization);
		return user;
	}
	
	public static User createConsumer(String username, String password, Organization organization) {
		User user = new User();
		user.username = username;
		user.password = password;
		user.consumerProfile = Consumer.create(user, organization);
		return user;
	}
	
	public static User fromId(Long id) {
		User user = new User();
		user.id = id;
		return user;
	}
	
	@Id
	@GeneratedValue
	@Basic(optional = false)
	@Column(name = "id", nullable = false, unique = true)
	private Long id;
	
	@Basic(optional = false)
	@Column(name = "username", nullable = false, unique = true)
	private @Pattern(regexp = Validation.USERNAME_REGEX) String username;
	
	@Basic(optional = false, fetch = FetchType.LAZY)
	@Column(name = "password", nullable = false)
	private String password;
	
	@OneToOne(mappedBy = "user", cascade = {CascadeType.ALL}, orphanRemoval = true)
	private Server serverProfile;
	
	@OneToOne(mappedBy = "user", cascade = {CascadeType.ALL}, orphanRemoval = true)
	private Consumer consumerProfile;
	
	public Long getId() {
		return id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public Optional<Server> getServerProfile() {
		return Optional.ofNullable(serverProfile);
	}
	
	public Optional<Consumer> getConsumerProfile() {
		return Optional.ofNullable(consumerProfile);
	}
}
