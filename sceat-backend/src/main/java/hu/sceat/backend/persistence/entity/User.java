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
	
	public static User createServer(String email, String password, String name, Organization organization) {
		User user = new User();
		user.email = email;
		user.password = password;
		user.name = name;
		user.serverProfile = Server.create(user, organization);
		return user;
	}
	
	public static User createConsumer(String email, String password, String name, Organization organization) {
		User user = new User();
		user.email = email;
		user.password = password;
		user.name = name;
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
	@Column(name = "email", nullable = false, unique = true)
	private @Pattern(regexp = Validation.EMAIL_REGEX) String email;
	
	@Basic(optional = false, fetch = FetchType.LAZY)
	@Column(name = "password", nullable = false)
	private @Pattern(regexp = Validation.PASSWORD_REGEX) String password;
	
	@Basic(optional = false)
	@Column(name = "name", nullable = false)
	private @Pattern(regexp = Validation.GENERAL_NAME_REGEX) String name;
	
	@OneToOne(mappedBy = "user", cascade = {CascadeType.ALL}, orphanRemoval = true)
	private Server serverProfile;
	
	@OneToOne(mappedBy = "user", cascade = {CascadeType.ALL}, orphanRemoval = true)
	private Consumer consumerProfile;
	
	@Basic(optional = false)
	@Column(name = "first_login_flag", nullable = false)
	private boolean firstLoginFlag;
	
	public Long getId() {
		return id;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getName() {
		return name;
	}
	
	public Optional<Server> getServerProfile() {
		return Optional.ofNullable(serverProfile);
	}
	
	public Optional<Consumer> getConsumerProfile() {
		return Optional.ofNullable(consumerProfile);
	}
	
	public boolean isServer() {
		return serverProfile != null;
	}
	
	public boolean isConsumer() {
		return consumerProfile != null;
	}
	
	public boolean getFirstLoginFlag() {
		return firstLoginFlag;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void clearFirstLoginFlag() {
		this.firstLoginFlag = false;
	}
}
