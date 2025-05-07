package nathan.mg.api.book;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BookRequestDto(
		@NotBlank(message = "Título obrigatório")
		String title,
		@NotBlank(message = "Sinopse obrigatória")
		String synopsis,
		@NotBlank(message = "Autor obrigatório")
		String author,
		@NotNull(message = "Data de publicação obrigatória")
		LocalDate publicationDate,
		@NotNull(message = "Nome obrigatório")
		Boolean available,
		@NotNull(message = "Avaliação obrigatória")
		Integer rating,
		String cover
		)
{}
