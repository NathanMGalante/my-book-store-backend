package nathan.mg.api.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import nathan.mg.api.config.security.TokenService;
import nathan.mg.api.user.User;
import nathan.mg.api.user.UserRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private AuthenticationManager manager;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private UserRepository userRepository;

	@PostMapping("/login")
	public ResponseEntity<AuthResponseDto> login(@RequestBody @Valid LoginRequestDto data) {
		var authToken = new UsernamePasswordAuthenticationToken(data.username(), data.password());
		var auth = manager.authenticate(authToken);
		var user = auth.getPrincipal();

		var accessToken = tokenService.generateAccessToken((User) user);
		var refreshToken = tokenService.generateRefreshToken((User) user);
		
		return ResponseEntity.ok(new AuthResponseDto(accessToken, refreshToken));
	}

	@PostMapping("/refresh")
	public ResponseEntity<AuthResponseDto> refresh(@RequestBody @Valid RefreshRequestDto data) {
		var refreshToken = data.refreshToken();
		System.out.println("refreshToken: " + refreshToken);
		var subject = tokenService.getSubjec(refreshToken);
		var user = userRepository.findByEmail(subject);

		var newAccessToken = tokenService.generateAccessToken((User) user);
		var newRefreshToken = tokenService.generateRefreshToken((User) user);

		return ResponseEntity.ok(new AuthResponseDto(newAccessToken, newRefreshToken));
	}
	
}
