package nathan.mg.api.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import nathan.mg.api.config.security.TokenService;

@RestController
@RequestMapping("users")
public class UserController {
	
	@Autowired
	private TokenService tokenService;

	@Autowired
	private UserRepository repository;
	
	@GetMapping("/me")
	@Secured({"ROLE_ADMIN", "ROLE_EMPLOYEER"})
	public ResponseEntity<UserResponseDto> getUser(HttpServletRequest request) {
		var subject = tokenService.getSubjec(request);
		var user = repository.findByEmail(subject);
		return ResponseEntity.ok(new UserResponseDto((User) user));
	}
}














