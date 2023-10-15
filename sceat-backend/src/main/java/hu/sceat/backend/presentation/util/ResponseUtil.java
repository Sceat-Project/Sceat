package hu.sceat.backend.presentation.util;

import hu.sceat.backend.business.fail.CommonFail;
import hu.sceat.backend.business.fail.Fail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public final class ResponseUtil {
	private ResponseUtil() {}
	
	public static <T> ResponseEntity<T> respondOk() {
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	public static <T> ResponseEntity<T> respondOk(T value) {
		return new ResponseEntity<>(value, HttpStatus.OK);
	}
	
	public static <T> ResponseEntity<T> respondFail(Fail fail) {
		Map<String, Object> body = Map.of("message", fail.message());
		ResponseEntity<?> response = new ResponseEntity<>(body, toHttpStatus(fail));
		//We hide the return type of the error responses, so that the method declarations in the controllers
		// are more informative: they showcase the good-case's return type.
		//noinspection unchecked
		return (ResponseEntity<T>) response;
	}
	
	public static HttpStatus toHttpStatus(Fail fail) {
		if (!(fail instanceof CommonFail commonFail)) return HttpStatus.BAD_REQUEST;
		return switch (commonFail.type()) {
			case INTERNAL -> HttpStatus.INTERNAL_SERVER_ERROR;
			case NOT_FOUND -> HttpStatus.NOT_FOUND;
			case FORBIDDEN -> HttpStatus.FORBIDDEN;
			case INVALID_INPUT, INVALID_ACTION -> HttpStatus.BAD_REQUEST;
		};
	}
}
