package nathan.mg.api.store;

import jakarta.validation.constraints.NotBlank;

public record StoreRequestDto(
		@NotBlank(message = "Nome obrigatório")
		String name,
		@NotBlank(message = "Slogan obrigatório")
		String slogan,
		String banner
) {
}
