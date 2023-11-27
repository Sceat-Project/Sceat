package hu.sceat.backend.presentation.controller;

import hu.sceat.backend.business.PrincipalUser;
import hu.sceat.backend.business.service.AuthService;
import hu.sceat.backend.presentation.util.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	private final AuthService authService;
	
	public AuthController(AuthService authService) {
		this.authService = authService;
	}
	
	@PostMapping("/csrf")
	public ResponseEntity<Void> csrf() {
		return ResponseUtil.respondOk(); //CSRF token is automatically attached to each post response
	}
	
	@PostMapping("/register/consumer")
	public ResponseEntity<Void> registerConsumer(@RequestParam String email, @RequestParam String password,
			@RequestParam String name, @RequestParam Long organization) {
		return authService.registerConsumer(email, password, name, organization)
				.get(u -> ResponseUtil.respondOk(), ResponseUtil::respondFail);
	}
	
	@PostMapping("/password")
	public ResponseEntity<Void> postPassword(PrincipalUser principal, @RequestParam String password) {
		return authService.setPassword(principal, password)
				.get(u -> ResponseUtil.respondOk(), ResponseUtil::respondFail);
	}
}
