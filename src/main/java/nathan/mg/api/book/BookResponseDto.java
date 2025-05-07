package nathan.mg.api.book;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record BookResponseDto(
		Long id,
		LocalDateTime creationDateTime,
		String title,
		String synopsis,
		String author,
		LocalDate publicationDate,
		Boolean available,
		Integer rating,
		String cover
		)
{

	public BookResponseDto(Book book) {
		this(
			book.getId(),
			book.getCreationDateTime(),
			book.getTitle(),
			book.getSynopsis(),
			book.getAuthor(),
			book.getPublicationDate(),
			book.getAvailable(),
			book.getRating(),
			book.getCover()
			);
	}

}
