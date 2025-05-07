package nathan.mg.api.store.book;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import nathan.mg.api.store.Store;

public interface BookRepository extends JpaRepository<Book, Long> {
	
	List<Book> findAllByStore(Store store);
	
}
