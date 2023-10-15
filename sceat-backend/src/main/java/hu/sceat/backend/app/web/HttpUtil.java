package hu.sceat.backend.app.web;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public final class HttpUtil {
	private HttpUtil() {}
	
	public static void respondOk(HttpServletResponse response) {
		response.setStatus(200);
	}
	
	public static void respondError(HttpServletResponse response, int statusCode, String message) throws IOException {
		response.setStatus(statusCode);
		response.setContentType("application/json");
		response.getWriter().write("{\"message\":\"" + message + "\"}");
	}
}