package nathan.mg.api.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import nathan.mg.api.config.security.TokenService;
import nathan.mg.api.user.Role;
import nathan.mg.api.user.User;
import nathan.mg.api.user.UserRequestDto;
import nathan.mg.api.user.UserRepository;

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














