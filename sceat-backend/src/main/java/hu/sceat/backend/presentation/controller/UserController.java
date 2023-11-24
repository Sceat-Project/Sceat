package hu.sceat.backend.presentation.controller;

import hu.sceat.backend.business.PrincipalUser;
import hu.sceat.backend.business.dto.UserDto;
import hu.sceat.backend.business.service.UserService;
import hu.sceat.backend.presentation.util.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/id/{userId}")
	public ResponseEntity<UserDto> get(PrincipalUser principal, @PathVariable Long userId) {
		return userService.findById(principal, userId)
				.get(ResponseUtil::respondOk, ResponseUtil::respondFail);
	}
	
	@GetMapping("/find/email/{email}")
	public ResponseEntity<UserDto> findEmail(PrincipalUser principal, @PathVariable String email) {
		return userService.findByEmail(principal, email)
				.get(ResponseUtil::respondOk, ResponseUtil::respondFail);
	}
	
	@GetMapping("/find/name/{name}")
	public ResponseEntity<UserDto> findName(PrincipalUser principal, @PathVariable String name) {
		return userService.findByName(principal, name)
				.get(ResponseUtil::respondOk, ResponseUtil::respondFail);
	}
	
	@GetMapping("/self")
	public ResponseEntity<UserDto> self(PrincipalUser principal) {
		return ResponseUtil.respondOk(userService.getSelf(principal));
	}
}
