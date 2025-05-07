package nathan.mg.api.store;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.validation.Valid;
import nathan.mg.api.book.Book;
import nathan.mg.api.book.BookRepository;
import nathan.mg.api.book.BookRequestDto;
import nathan.mg.api.book.BookResponseDto;
import nathan.mg.api.user.Role;
import nathan.mg.api.user.User;
import nathan.mg.api.user.UserRequestDto;
import nathan.mg.api.user.UserRepository;

@RestController
@RequestMapping("stores")
public class StoreController {

	@Autowired
	private StoreRepository repository;

	@Autowired
    private BookRepository bookRepository; 

	@Autowired
    private UserRepository userRepository; 
	
	@Autowired
    private PasswordEncoder passwordEncoder;

	@PostMapping
	@Transactional
	public ResponseEntity<StoreResponseDto> register(@RequestBody @Valid UserRequestDto data, UriComponentsBuilder uriBuilder) {
        Store store = new Store(data.store());
        
        User admin = new User(data.name(), data.email(), passwordEncoder.encode(data.password()), data.photo(), store, Role.ROLE_ADMIN);
        
        repository.save(store);
        userRepository.save(admin);
        
        var uri = uriBuilder.path("/stores/{id}").buildAndExpand(store.getId()).toUri();
        
        return ResponseEntity.created(uri).body(new StoreResponseDto(store));
	}

	@PutMapping("/{id}")
	@Secured({"ROLE_ADMIN"})
	@Transactional
	public ResponseEntity<StoreResponseDto> updateStore(
			@PathVariable Long id,
			@RequestBody @Valid StoreRequestDto data
			) {
        var store = repository.getReferenceById(id);
        store.update(data);
        
        return ResponseEntity.ok(new StoreResponseDto(store));
	}

	@DeleteMapping("/{id}")
	@Secured({"ROLE_ADMIN"})
	@Transactional
	public ResponseEntity<?> deleteStore(@PathVariable Long id) {
		repository.getReferenceById(id).delete();
		
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}")
	@Secured({"ROLE_ADMIN"})
	public ResponseEntity<StoreResponseDto> getStore(@PathVariable Long id) {
		var store = repository.getReferenceById(id);
		return ResponseEntity.ok(new StoreResponseDto(store));
	}

	@GetMapping
	@Secured({"ROLE_ADMIN"})
	public ResponseEntity<Page<StoreResponseDto>> getStores(
			@PageableDefault(size = 10, sort = {"name"}) Pageable pagination
			) {
        var page = repository.findAllByDeletionDateTimeNull(pagination).map(StoreResponseDto::new);
        
        return ResponseEntity.ok(page);
	}
	
	@PostMapping("/{id}/book")
	@Secured({"ROLE_ADMIN"})
	@Transactional
	public ResponseEntity<BookResponseDto> registerBook(
			@PathVariable Long id,
			@RequestBody @Valid BookRequestDto data) {
        var store = repository.getReferenceById(id);
        
        Book book = new Book(data, store);
        
        bookRepository.save(book);
        
        return ResponseEntity.ok(new BookResponseDto(book));
	}

	@GetMapping("/{id}/books")
	@Secured({"ROLE_ADMIN", "ROLE_EMPLOYEER"})
	public ResponseEntity<List<BookResponseDto>> getBooks(@PathVariable Long id) {
        var store = repository.getReferenceById(id);
        var books = bookRepository.findAllByStore(store).stream().map(BookResponseDto::new).toList();
        
        return ResponseEntity.ok(books);
	}
}














