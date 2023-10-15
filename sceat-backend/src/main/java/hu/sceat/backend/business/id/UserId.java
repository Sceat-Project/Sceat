package hu.sceat.backend.business.id;

/**
 * Implemented by classes that represent a user.
 * This interface exposes the identifier of the user.
 * By using this class instead of the {@link Long} type, we can make sure that the identifier is not misused.
 */
public interface UserId {
	default Long getId() {
		return id();
	}
	
	default Long id() {
		return getId();
	}
	
	static UserId of(Long id) {
		return new IdHolder.UserIdHolder(id);
	}
}
