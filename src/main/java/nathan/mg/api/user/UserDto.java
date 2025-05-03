package nathan.mg.api.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import nathan.mg.api.store.StoreRequestDto;

public record UserDto(
		@NotBlank(message = "Nome obrigatório")
		String name,
		@NotBlank(message = "Email obrigatório")
		@Email(message = "Email inválido")
		String email,
		@NotBlank(message = "Senha obrigatória")
		String password,
		String photo,
		@Valid
		StoreRequestDto store
) {}
