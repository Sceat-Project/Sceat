package hu.sceat.backend.persistence.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "consumers")
public class Consumer {
	
	public static Consumer create(User user, Organization organization) {
		Consumer consumer = new Consumer();
		consumer.user = user;
		consumer.organization = organization;
		consumer.allergies = EnumSet.noneOf(Allergen.class);
		consumer.purchasedMenus = new ArrayList<>();
		return consumer;
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
	
	@ElementCollection
	@CollectionTable(name = "consumer_allergies", joinColumns = @JoinColumn(name = "consumer_id"))
	@Column(name = "allergies", nullable = false)
	@Enumerated(EnumType.STRING)
	private Set<Allergen> allergies;
	
	@ManyToMany
	@JoinTable(name = "consumer_purchased_menus",
			joinColumns = @JoinColumn(name = "consumer_id", nullable = false),
			inverseJoinColumns = @JoinColumn(name = "menu_id", nullable = false))
	private Collection<Menu> purchasedMenus;
	
	@Lob
	@Basic
	@Column(name = "photo")
	private byte[] photo;
	
	protected Consumer() {}
	
	public User getUser() {
		return user;
	}
	
	public Organization getOrganization() {
		return organization;
	}
	
	public Set<Allergen> getAllergies() {
		return Collections.unmodifiableSet(allergies);
	}
	
	public Collection<Menu> getPurchasedMenus() {
		return Collections.unmodifiableCollection(purchasedMenus);
	}
	
	public Optional<byte[]> getPhoto() {
		return Optional.ofNullable(photo);
	}
	
	public void setAllergies(Set<Allergen> allergies) {
		this.allergies = EnumSet.copyOf(allergies);
	}
	
	public void addPurchasedMenu(Menu menu) {
		if (purchasedMenus.contains(menu)) throw new IllegalArgumentException("Menu already purchased");
		purchasedMenus.add(menu);
	}
	
	public void removePurchasedMenu(Menu menu) {
		if (!purchasedMenus.contains(menu)) throw new IllegalArgumentException("Menu already purchased");
		purchasedMenus.remove(menu);
	}
	
	public void setPhoto(byte[] photo) {
		this.photo = Arrays.copyOf(photo, photo.length);
	}
}
