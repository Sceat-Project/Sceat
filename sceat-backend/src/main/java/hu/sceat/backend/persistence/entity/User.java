package hu.sceat.backend.persistence.entity;

import hu.sceat.backend.persistence.Validation;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "users")
public class User {
	
	public static User create(String username, String password) {
		User user = new User();
		user.username = username;
		user.password = password;
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
	
	public Long getId() {
		return id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
}
