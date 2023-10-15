package hu.sceat.backend.business.fail;

/**
 * Quite like an exception, this class represents a failure.
 * It is used to return a failure from a method instead of throwing an exception.
 */
public interface Fail {
	String message();
}
