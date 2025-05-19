package nathan.mg.api.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import nathan.mg.api.config.security.TokenService;
import nathan.mg.api.user.User;
import nathan.mg.api.user.UserRepository;

@Service
public class AuthService implements UserDetailsService {
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return repository.findByEmail(username);
	}
	
	public User getCurrentUser(HttpServletRequest request) {
		var subject = tokenService.getSubjec(request);
		return (User) repository.findByEmail(subject);
	}

}
