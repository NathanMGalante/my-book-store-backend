package nathan.mg.api.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

public record UserRequestDto(
		@NotBlank(message = "Nome obrigatório")
		String name,
		@NotBlank(message = "Email obrigatório")
		@Email(message = "Email inválido")
		String email,
		@NotBlank(message = "Senha obrigatória")
		String password
) {}
