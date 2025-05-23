package nathan.mg.api.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class ExceptionsManager {
	
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<?> exception404() {
		return ResponseEntity.notFound().build();
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> exception400(MethodArgumentNotValidException ex) {
		var errors = ex.getFieldErrors();
		return ResponseEntity.badRequest().body(errors.stream().map(ErrorValidation::new).toList());
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> exception400(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
	
	@ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> tratarErroBadCredentials() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> tratarErroAuthentication() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Falha na autenticação");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> tratarErroAcessoNegado() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> tratarErro500(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro: " +ex.getLocalizedMessage());
    }
	
	private record ErrorValidation(String field, String message) {
		public ErrorValidation(FieldError error) {
			this(error.getField(), error.getDefaultMessage());
		}
	}
	
}
