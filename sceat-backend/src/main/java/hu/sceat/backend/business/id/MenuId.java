package hu.sceat.backend.business.id;

/**
 * Implemented by classes that represent a menu.
 * This interface exposes the identifier of the menu.
 * By using this class instead of the {@link Long} type, we can make sure that the identifier is not misused.
 */
public interface MenuId {
	default Long getId() {
		return id();
	}
	
	default Long id() {
		return getId();
	}
	
	static MenuId of(Long id) {
		return new IdHolder.MenuIdHolder(id);
	}
}
