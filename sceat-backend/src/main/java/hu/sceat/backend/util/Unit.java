package hu.sceat.backend.util;

/**
 * A type with a single possible value.
 * If we allow {@code null} values, then this type has two possible values.
 * This is a replacement for the {@link Void} type for non-null contexts.
 */
public final class Unit {
	private Unit() {}
	
	@SuppressWarnings("InstantiationOfUtilityClass")
	private static final Unit INSTANCE = new Unit();
	
	public static Unit get() {
		return INSTANCE;
	}
}
