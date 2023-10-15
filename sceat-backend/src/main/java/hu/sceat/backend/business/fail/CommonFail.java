package hu.sceat.backend.business.fail;

public record CommonFail(Type type, String message) implements Fail {
	
	public static CommonFail internal(String explanation) {
		return new CommonFail(Type.INTERNAL, String.format("Internal error: %s", explanation));
	}
	
	public static CommonFail notFound(String resource) {
		return new CommonFail(Type.NOT_FOUND, String.format("Resource '%s' not found", resource));
	}
	
	public static CommonFail forbidden(String explanation) {
		return new CommonFail(Type.FORBIDDEN, "Access denied: " + explanation);
	}
	
	public static CommonFail invalidInput(String inputName, String explanation) {
		return new CommonFail(Type.INVALID_INPUT,
				String.format("Invalid %s: %s", inputName, explanation));
	}
	
	public static CommonFail invalidInputFormat(String inputName) {
		return invalidInput(inputName, "invalid format");
	}
	
	public static CommonFail invalidInputAlreadyTaken(String inputName) {
		return invalidInput(inputName, "already taken");
	}
	
	public static CommonFail invalidInputNotAmongValid(String inputName) {
		return invalidInput(inputName, "not among valid values");
	}
	
	public static CommonFail invalidAction(String explanation) {
		return new CommonFail(Type.INVALID_ACTION,
				String.format("Invalid action: %s", explanation));
	}
	
	public enum Type {
		INTERNAL,
		NOT_FOUND,
		FORBIDDEN,
		INVALID_INPUT,
		INVALID_ACTION
	}
}
