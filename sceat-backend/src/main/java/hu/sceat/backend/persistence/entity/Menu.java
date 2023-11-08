package hu.sceat.backend.persistence.entity;

import hu.sceat.backend.persistence.Validation;
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
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "menus", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"organization_id", "name", "date", "occasion"})
})
public class Menu {
	
	public static Menu create(Organization organization, String name,
			LocalDate date, Occasion occasion, int cost, List<String> foods, Set<Allergen> allergens) {
		Menu menu = new Menu();
		menu.organization = organization;
		menu.name = name;
		menu.date = date;
		menu.occasion = occasion;
		menu.cost = cost;
		menu.foods = new ArrayList<>(foods);
		menu.allergens = allergens.isEmpty() ? EnumSet.noneOf(Allergen.class) : EnumSet.copyOf(allergens);
		menu.purchasers = new ArrayList<>();
		return menu;
	}
	
	public static Menu fromId(Long id) {
		Menu menu = new Menu();
		menu.id = id;
		return menu;
	}
	
	@Id
	@GeneratedValue
	@Basic(optional = false)
	@Column(name = "id", nullable = false, unique = true)
	private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "organization_id", nullable = false)
	private Organization organization;
	
	@Basic(optional = false)
	@Column(name = "name", nullable = false)
	private @Pattern(regexp = Validation.GENERAL_NAME_REGEX) String name;
	
	@Basic(optional = false)
	@Column(name = "date", nullable = false)
	private LocalDate date;
	
	@Basic(optional = false)
	@Column(name = "occasion", nullable = false)
	@Enumerated(EnumType.STRING)
	private Occasion occasion;
	
	@Basic(optional = false)
	@Column(name = "cost", nullable = false)
	private int cost;
	
	@ElementCollection
	@CollectionTable(name = "menu_foods", joinColumns = @JoinColumn(name = "menu_id"))
	@OrderColumn(name = "food_index")
	@Column(name = "foods", nullable = false)
	private List<String> foods;
	
	@ElementCollection
	@CollectionTable(name = "menu_allergens", joinColumns = @JoinColumn(name = "menu_id"))
	@Column(name = "allergens", nullable = false)
	@Enumerated(EnumType.STRING)
	private Set<Allergen> allergens;
	
	@ManyToMany(mappedBy = "purchasedMenus")
	private Collection<Consumer> purchasers;
	
	protected Menu() {}
	
	public Long getId() {
		return id;
	}
	
	public Organization getOrganization() {
		return organization;
	}
	
	public String getName() {
		return name;
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public Occasion getOccasion() {
		return occasion;
	}
	
	public int getCost() {
		return cost;
	}
	
	public List<String> getFoods() {
		return Collections.unmodifiableList(foods);
	}
	
	public Set<Allergen> getAllergens() {
		return Collections.unmodifiableSet(allergens);
	}
	
	public Collection<Consumer> getPurchasers() {
		return Collections.unmodifiableCollection(purchasers);
	}
}
