package nathan.mg.api.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import nathan.mg.api.auth.AuthService;
import nathan.mg.api.book.BookResponseDto;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private AuthService authService;

	@Autowired
	private UserRepository repository;

	@GetMapping("/{id}")
	@Secured({"ROLE_ADMIN"})
	public ResponseEntity<UserResponseDto> getUser(@PathVariable Long id) {
		var user = repository.getReferenceById(id);
		return ResponseEntity.ok(new UserResponseDto(user));
	}
	
	@GetMapping("/me")
	@Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
	public ResponseEntity<UserResponseDto> getLoggedUser(HttpServletRequest request) {
		return ResponseEntity.ok(new UserResponseDto(authService.getCurrentUser(request)));
	}
	
	@GetMapping("/all")
	@Secured({"ROLE_ADMIN"})
	public ResponseEntity<List<UserResponseDto>> getAll() {
        var users = repository.findAllByOrderByNameAsc().stream().map(UserResponseDto::new).toList();
        
        return ResponseEntity.ok(users);
	}
	
	@GetMapping("/all-employees")
	@Secured({"ROLE_ADMIN"})
	public ResponseEntity<List<UserResponseDto>> getAllEmployees() {
        var users = repository.findAllByRoleOrderByName(Role.ROLE_EMPLOYEE).stream().map(UserResponseDto::new).toList();
        
        return ResponseEntity.ok(users);
	}
	
	@GetMapping("/all-without-role")
	@Secured({"ROLE_ADMIN"})
	public ResponseEntity<List<UserResponseDto>> getAllNormalUsers() {
        var users = repository.findAllByRoleOrderByName(Role.ROLE_USER).stream().map(UserResponseDto::new).toList();
        
        return ResponseEntity.ok(users);
	}
}
