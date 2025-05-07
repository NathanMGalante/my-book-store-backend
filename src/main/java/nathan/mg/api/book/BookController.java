package nathan.mg.api.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("books")
public class BookController {

	@Autowired
	private BookRepository repository;

	@PutMapping("/{id}")
	@Secured({"ROLE_ADMIN"})
	@Transactional
	public ResponseEntity<BookResponseDto> updateBook(
			@PathVariable Long id,
			@RequestBody @Valid BookRequestDto data) {
        var book = repository.getReferenceById(id);
        book.update(data);
        
        return ResponseEntity.ok(new BookResponseDto(book));
	}

	@DeleteMapping("/{id}")
	@Secured({"ROLE_ADMIN"})
	@Transactional
	public ResponseEntity<?> deleteBook(@PathVariable Long id) {
		repository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping
	@Secured({"ROLE_ADMIN", "ROLE_EMPLOYEER"})
	public ResponseEntity<Page<BookResponseDto>> getBooks(
			@PageableDefault(size = 10, sort = {"title"}) Pageable pagination
			) {
        var page = repository.findAll(pagination).map(BookResponseDto::new);
        
        return ResponseEntity.ok(page);
	}
}














