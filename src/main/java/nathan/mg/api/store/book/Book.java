package nathan.mg.api.store.book;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nathan.mg.api.store.Store;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime creationDateTime;
    
    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
    
    private String title;

    private String synopsis;
    
    private String author;

    @Column(nullable = false, updatable = false)
    private LocalDate publicationDate;
    
    private Boolean available;
    
    private Integer rating;

    @Column(columnDefinition = "LONGTEXT", nullable = true)
    private String cover;
    
    public Book(BookRequestDto data, Store store) {
    	this.creationDateTime = LocalDateTime.now();
    	this.store = store;
        this.title = data.title();
        this.synopsis = data.synopsis();
        this.author = data.author();
        this.publicationDate = data.publicationDate();
        this.available = data.available();
        this.rating = data.rating();
        this.cover = data.cover();
    }
    
	public void update(@Valid BookRequestDto data) {
    	if (data.title() != null) {
            this.title = data.title();
    	}
    	if (data.synopsis() != null) {
            this.synopsis = data.synopsis();
    	}
    	if (data.author() != null) {
            this.author = data.author();
    	}
    	if (data.publicationDate() != null) {
            this.publicationDate = data.publicationDate();
    	}
    	if (data.available() != null) {
            this.available = data.available();
    	}
    	if (data.rating() != null) {
            this.rating = data.rating();
    	}
    	if (data.cover() != null) {
            this.cover = data.cover();
    	}
	}

}
