package hu.sceat.backend.persistence.repository;

import hu.sceat.backend.persistence.entity.Consumer;
import hu.sceat.backend.persistence.entity.Organization;
import hu.sceat.backend.persistence.entity.Organization_;
import hu.sceat.backend.persistence.entity.Server;
import hu.sceat.backend.persistence.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Long>,
		JpaSpecificationExecutor<Organization> {
	
	static Specification<Organization> same(Organization other) {
		return (root, query, cb) -> cb.equal(root, other);
	}
	
	static Specification<Organization> hasUser(User user) {
		List<Specification<Organization>> specs = new ArrayList<>();
		user.getServerProfile().ifPresent(server -> specs.add(hasServer(server)));
		user.getConsumerProfile().ifPresent(consumer -> specs.add(hasConsumer(consumer)));
		return Specification.anyOf(specs);
	}
	
	static Specification<Organization> hasServer(Server server) {
		return (root, query, cb) -> cb.isMember(server, root.get(Organization_.servers));
	}
	
	static Specification<Organization> hasConsumer(Consumer consumer) {
		return (root, query, cb) -> cb.isMember(consumer, root.get(Organization_.consumers));
	}
	
	Optional<Organization> findByName(String name);
}
