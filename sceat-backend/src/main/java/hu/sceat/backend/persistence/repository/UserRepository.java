package hu.sceat.backend.persistence.repository;

import hu.sceat.backend.persistence.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
	
	static Specification<User> same(User other) {
		return (root, query, cb) -> cb.equal(root, other);
	}
	
	Optional<User> findByUsername(String username);
}
