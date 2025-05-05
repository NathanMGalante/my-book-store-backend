package nathan.mg.api.config.security;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import jakarta.servlet.http.HttpServletRequest;
import nathan.mg.api.user.User;

@Service
public class TokenService {
	
	@Value("${api.security.token.secret}")
	private String secret;

	@Value("${api.security.token.issuer}")
	private String issuer;
	
	private String generateToken(User user, LocalDateTime expiration) {
		try {
		    Algorithm algorithm = Algorithm.HMAC256(secret);
		    return JWT.create().withIssuer(issuer)
		    		.withSubject(user.getUsername())
		    		.withClaim("authorities", user.getAuthoritiesAsStringList())
		    		.withExpiresAt(expiration.toInstant(ZoneOffset.of("-03:00")))
		    		.sign(algorithm);
		} catch (JWTCreationException exception){
		    throw new RuntimeException("Erro ao gerar token", exception);
		}
	}
	
	public String generateAccessToken(User user) {
		return generateToken(user, LocalDateTime.now().plusMinutes(15));
	}
	
	public String generateRefreshToken(User user) {
		return generateToken(user, LocalDateTime.now().plusMonths(1));
	}

	public String getToken(HttpServletRequest request) {
		var authorizationHeader = request.getHeader("Authorization");
		if(authorizationHeader != null) {
			return authorizationHeader.replace("Bearer ", "");
		}
		return null;
	}

	public String getSubjec(HttpServletRequest request) {
		var authorizationHeader = request.getHeader("Authorization");
		if(authorizationHeader != null) {
			return getSubjecFromToken(authorizationHeader.replace("Bearer ", ""));
		}
		return null;
	}
	
	public String getSubjecFromToken(String token) {
		try {
		    Algorithm algorithm = Algorithm.HMAC256(secret);
		    return JWT.require(algorithm)
		        .withIssuer(issuer)
		        .build()
		        .verify(token)
		        .getSubject();
		} catch (JWTVerificationException exception){
		    throw new RuntimeException("Token inválido", exception);
		}
	}
}
