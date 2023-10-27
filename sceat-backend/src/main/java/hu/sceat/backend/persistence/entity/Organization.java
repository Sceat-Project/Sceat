package hu.sceat.backend.persistence.entity;

import hu.sceat.backend.persistence.Validation;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "organizations")
public class Organization {
	
	public static Organization create(String name) {
		Organization menu = new Organization();
		menu.name = name;
		menu.menus = new ArrayList<>();
		menu.servers = new ArrayList<>();
		menu.consumers = new ArrayList<>();
		return menu;
	}
	
	public static Organization fromId(Long id) {
		Organization menu = new Organization();
		menu.id = id;
		return menu;
	}
	
	@Id
	@GeneratedValue
	@Basic(optional = false)
	@Column(name = "id", nullable = false, unique = true)
	private Long id;
	
	@Basic(optional = false)
	@Column(name = "name", nullable = false, unique = true)
	private @Pattern(regexp = Validation.GENERAL_NAME_REGEX) String name;
	
	@OneToMany(mappedBy = "organization", cascade = {CascadeType.ALL}, orphanRemoval = true)
	private Collection<Menu> menus;
	
	@OneToMany(mappedBy = "organization", cascade = {CascadeType.ALL}, orphanRemoval = true)
	private Collection<Server> servers;
	
	@OneToMany(mappedBy = "organization", cascade = {CascadeType.ALL}, orphanRemoval = true)
	private Collection<Consumer> consumers;
	
	protected Organization() {}
	
	public Long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public Collection<Menu> getMenus() {
		return Collections.unmodifiableCollection(menus);
	}
	
	public Collection<Server> getServers() {
		return Collections.unmodifiableCollection(servers);
	}
	
	public Collection<Consumer> getConsumers() {
		return Collections.unmodifiableCollection(consumers);
	}
}
