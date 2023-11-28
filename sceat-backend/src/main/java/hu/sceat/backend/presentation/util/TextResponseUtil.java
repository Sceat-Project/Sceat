package hu.sceat.backend.presentation.util;

import hu.sceat.backend.business.fail.Fail;
import org.springframework.http.ResponseEntity;

public final class TextResponseUtil {
	private TextResponseUtil() {}
	
	public static String respondOk() {
		return toJson(ResponseUtil.respondOk());
	}
	
	public static String respondOk(Object value) {
		return toJson(ResponseUtil.respondOk(value));
	}
	
	public static String respondFail(Fail fail) {
		return toJson(ResponseUtil.respondFail(fail));
	}
	
	private static String toJson(ResponseEntity<?> entity) {
		return entity.getStatusCode() + ": " + entity.getBody();
	}
}
