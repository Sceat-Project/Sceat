package hu.sceat.backend.business.id;

/**
 * Implemented by classes that represent an organization.
 * This interface exposes the identifier of the organization.
 * By using this class instead of the {@link Long} type, we can make sure that the identifier is not misused.
 */
public interface OrganizationId {
	default Long getId() {
		return id();
	}
	
	default Long id() {
		return getId();
	}
	
	static OrganizationId of(Long id) {
		return new IdHolder.OrganizationIdHolder(id);
	}
}
