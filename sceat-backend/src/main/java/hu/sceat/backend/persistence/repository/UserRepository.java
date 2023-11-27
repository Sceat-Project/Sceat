package hu.sceat.backend.persistence.repository;

import hu.sceat.backend.persistence.entity.User;
import hu.sceat.backend.persistence.entity.User_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
	
	static Specification<User> same(User other) {
		return (root, query, cb) -> cb.equal(root, other);
	}
	
	static Specification<User> hasEmail(String email) {
		return (root, query, cb) -> cb.equal(root.get(User_.EMAIL), email);
	}
	
	static Specification<User> hasName(String name) {
		return (root, query, cb) -> cb.equal(root.get(User_.NAME), name);
	}
	
	Optional<User> findByEmail(String email);
}
