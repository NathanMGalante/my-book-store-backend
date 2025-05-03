package nathan.mg.api.store;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Store {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime creationDateTime;

    private LocalDateTime deletionDateTime;

    private String name;

    private String slogan;

    @Column(columnDefinition = "LONGTEXT", nullable = true)
    private String banner;

    public Store(StoreRequestDto data) {
    	this.creationDateTime = LocalDateTime.now();
        this.name = data.name();
        this.slogan = data.slogan();
        this.banner = data.banner();
    }

	public void update(@Valid StoreRequestDto data) {
    	if (data.name() != null) {
    		this.name = data.name();
    	}
    	if (data.slogan() != null) {
    		this.slogan = data.slogan();
    	}
    	if (data.banner() != null) {
    		this.banner = data.banner();
    	}
	}

	public void delete() {
    	this.deletionDateTime = LocalDateTime.now();
	}
}
