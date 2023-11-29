package hu.sceat.backend.persistence.repository;

import hu.sceat.backend.persistence.entity.Allergen;
import hu.sceat.backend.persistence.entity.Menu;
import hu.sceat.backend.persistence.entity.Menu_;
import hu.sceat.backend.persistence.entity.Occasion;
import hu.sceat.backend.persistence.entity.Organization;
import hu.sceat.backend.persistence.entity.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long>, JpaSpecificationExecutor<Menu> {
	
	static Specification<Menu> same(Menu other) {
		return (root, query, cb) -> cb.equal(root, other);
	}
	
	static Specification<Menu> hasOrganization(Organization organization) {
		return (root, query, cb) -> cb.equal(root.get(Menu_.organization), organization);
	}
	
	static Specification<Menu> hasSharedOrganization(User user) {
		List<Specification<Menu>> specs = new ArrayList<>();
		user.getServerProfile().ifPresent(server -> specs.add(hasOrganization(server.getOrganization())));
		user.getConsumerProfile().ifPresent(consumer -> specs.add(hasOrganization(consumer.getOrganization())));
		return Specification.anyOf(specs);
	}
	
	static Specification<Menu> hasNoneOfAllergens(Collection<Allergen> allergens) {
		return (root, query, cb) -> cb.and(
				allergens.stream()
						.map(allergen -> cb.isNotMember(allergen, root.get(Menu_.allergens)))
						.toArray(Predicate[]::new)
		);
	}
	
	static Specification<Menu> hasName(String name) {
		return (root, query, cb) -> cb.equal(root.get(Menu_.name), name);
	}
	
	static Specification<Menu> hasDate(LocalDate date) {
		return (root, query, cb) -> cb.equal(root.get(Menu_.date), date);
	}
	
	static Specification<Menu> hasDateBetween(LocalDate minIncl, LocalDate maxExcl) {
		return (root, query, cb) -> cb.and(
				cb.greaterThanOrEqualTo(root.get(Menu_.date), minIncl),
				cb.lessThan(root.get(Menu_.date), maxExcl)
		);
	}
	
	static Specification<Menu> hasOccasion(Occasion occasion) {
		return (root, query, cb) -> cb.equal(root.get(Menu_.occasion), occasion);
	}
}
