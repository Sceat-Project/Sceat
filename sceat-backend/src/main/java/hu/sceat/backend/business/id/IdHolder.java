package hu.sceat.backend.business.id;

public final class IdHolder {
	private IdHolder() {}
	
	record UserIdHolder(Long id) implements UserId {}
}
