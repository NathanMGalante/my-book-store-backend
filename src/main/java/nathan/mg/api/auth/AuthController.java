package nathan.mg.api.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.validation.Valid;
import nathan.mg.api.config.security.TokenService;
import nathan.mg.api.store.StoreResponseDto;
import nathan.mg.api.user.User;
import nathan.mg.api.user.UserRepository;
import nathan.mg.api.user.UserRequestDto;
import nathan.mg.api.user.UserResponseDto;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager manager;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private UserRepository userRepository;

	@Transactional
	@PostMapping("/register")
	public ResponseEntity<UserResponseDto> register(@RequestBody @Valid UserRequestDto data, UriComponentsBuilder uriBuilder) {
        User user = new User(data.name(), data.email(), passwordEncoder.encode(data.password()));
        userRepository.save(user);

        var uri = uriBuilder.path("/users/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).body(new UserResponseDto(user));
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto data) {
		var authToken = new UsernamePasswordAuthenticationToken(data.username(), data.password());
		var auth = manager.authenticate(authToken);
		var user = (User) auth.getPrincipal();

		var accessToken = tokenService.generateAccessToken(user);
		var refreshToken = tokenService.generateRefreshToken(user);
		
		var userDto = new UserResponseDto(user);
		var storeDto = user.getStore() == null ? null : new StoreResponseDto(user.getStore());
		
		return ResponseEntity.ok(new LoginResponseDto(accessToken, refreshToken, userDto, storeDto));
	}

	@PostMapping("/refresh")
	public ResponseEntity<AuthResponseDto> refresh(@RequestBody @Valid RefreshRequestDto data) {
		var refreshToken = data.refreshToken();
		var subject = tokenService.getSubjecFromToken(refreshToken);
		var user = (User) userRepository.findByEmail(subject);

		var newAccessToken = tokenService.generateAccessToken(user);
		var newRefreshToken = tokenService.generateRefreshToken(user);

		return ResponseEntity.ok(new AuthResponseDto(newAccessToken, newRefreshToken));
	}
	
}
