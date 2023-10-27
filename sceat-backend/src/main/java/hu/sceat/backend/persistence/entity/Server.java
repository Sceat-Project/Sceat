package hu.sceat.backend.persistence.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "servers")
public class Server {
	
	public static Server create(User user, Organization organization) {
		Server server = new Server();
		server.user = user;
		server.organization = organization;
		return server;
	}
	
	@Id
	@GeneratedValue
	@Basic(optional = false)
	@Column(name = "id", nullable = false, unique = true)
	private Long id;
	
	@OneToOne(optional = false)
	@JoinColumn(name = "user_id", nullable = false, unique = true)
	private User user;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "organization_id", nullable = false)
	private Organization organization;
	
	protected Server() {}
	
	public User getUser() {
		return user;
	}
	
	public Organization getOrganization() {
		return organization;
	}
}
