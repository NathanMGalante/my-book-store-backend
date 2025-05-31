package nathan.mg.api.store;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import nathan.mg.api.auth.AuthService;
import nathan.mg.api.book.Book;
import nathan.mg.api.book.BookRepository;
import nathan.mg.api.book.BookRequestDto;
import nathan.mg.api.book.BookResponseDto;
import nathan.mg.api.user.Role;
import nathan.mg.api.user.UserRepository;
import nathan.mg.api.user.UserResponseDto;

@RestController
@RequestMapping("stores")
public class StoreController {
	
	@Autowired
	private AuthService authService;

	@Autowired
	private StoreRepository repository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
    private BookRepository bookRepository;

	@Transactional
	@PostMapping
	@Secured({"ROLE_USER"})
	public ResponseEntity<StoreResponseDto> register(
			HttpServletRequest request,
			UriComponentsBuilder uriBuilder,
			@RequestBody @Valid StoreRequestDto data) {		
        Store store = new Store(data);
        repository.save(store);

		var admin = authService.getCurrentUser(request);
		admin.setStore(store);
		admin.setRole(Role.ROLE_ADMIN);

        var uri = uriBuilder.path("/stores/{id}").buildAndExpand(store.getId()).toUri();
        return ResponseEntity.created(uri).body(new StoreResponseDto(store));
	}

	@Transactional
	@PutMapping("/{id}")
	@Secured({"ROLE_ADMIN"})
	public ResponseEntity<StoreResponseDto> updateStore(
			@PathVariable Long id,
			@RequestBody @Valid StoreRequestDto data
			) {
        var store = repository.getReferenceById(id);
        store.update(data);
        
        return ResponseEntity.ok(new StoreResponseDto(store));
	}

	@Transactional
	@DeleteMapping("/{id}")
	@Secured({"ROLE_ADMIN"})
	public ResponseEntity<?> deleteStore(HttpServletRequest request, @PathVariable Long id) {
		var admin = authService.getCurrentUser(null);
		admin.setRole(Role.ROLE_USER);
		admin.setStore(null);
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

	@Transactional
	@PostMapping("/{id}/employee/{userId}")
	@Secured({"ROLE_ADMIN"})
	public ResponseEntity<UserResponseDto> registerEmployee(
			@PathVariable Long id,
			@PathVariable Long userId) {
        var store = repository.getReferenceById(id);
        
        var employee = userRepository.getReferenceById(userId);
        
        employee.setStore(store);
        employee.setRole(Role.ROLE_EMPLOYEE);

        return ResponseEntity.noContent().build();
	}

	@Transactional
	@DeleteMapping("/{id}/employee/{userId}")
	@Secured({"ROLE_ADMIN"})
	public ResponseEntity<?> removeEmployee(
			@PathVariable Long id,
			@PathVariable Long userId) {
        var store = repository.getReferenceById(id);
        
        var employee = userRepository.getReferenceById(userId);
        var employeeStore = employee.getStore();
        
        if(employeeStore != null && employeeStore.getId() == store.getId()) {
	        employee.setStore(null);
	        employee.setRole(Role.ROLE_USER);

	        return ResponseEntity.noContent().build();
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário não encontrado");
	}

	@GetMapping("/{id}/employees")
	@Secured({"ROLE_ADMIN"})
	public ResponseEntity<List<UserResponseDto>> getEmployees(
			@PathVariable Long id) {
        var store = repository.getReferenceById(id);
        var page = userRepository.findAllByStoreAndRole(store, Role.ROLE_EMPLOYEE).stream().map(UserResponseDto::new).toList();
        
        return ResponseEntity.ok(page);
	}

	@Transactional
	@PostMapping("/{id}/book")
	@Secured({"ROLE_ADMIN"})
	public ResponseEntity<BookResponseDto> registerBook(
			@PathVariable Long id,
			UriComponentsBuilder uriBuilder,
			@RequestBody @Valid BookRequestDto data) {
        var store = repository.getReferenceById(id);
        
        Book book = new Book(data, store);
        
        bookRepository.save(book);
        
        var uri = uriBuilder.path("/books/{id}").buildAndExpand(book.getId()).toUri();
        return ResponseEntity.created(uri).body(new BookResponseDto(book));
	}

	@GetMapping("/{id}/books")
	@Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
	public ResponseEntity<List<BookResponseDto>> getBooks(@PathVariable Long id) {
        var store = repository.getReferenceById(id);
        var books = bookRepository.findAllByStore(store).stream().map(BookResponseDto::new).toList();
        
        return ResponseEntity.ok(books);
	}
}
