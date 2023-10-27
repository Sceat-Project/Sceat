package hu.sceat.backend.business.id;

public final class IdHolder {
	private IdHolder() {}
	
	record UserIdHolder(Long id) implements UserId {}
	record OrganizationIdHolder(Long id) implements OrganizationId {}
	record MenuIdHolder(Long id) implements MenuId {}
}
