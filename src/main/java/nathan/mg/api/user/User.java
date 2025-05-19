package nathan.mg.api.user;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nathan.mg.api.store.Store;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User implements UserDetails {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, updatable = false)
    private LocalDateTime creationDateTime;

    private String name;

    private String email;

    private String password;

    @Column(columnDefinition = "LONGTEXT", nullable = true)
    private String photo;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER;

    public User(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    	this.creationDateTime = LocalDateTime.now();
    }

    public User(String name, String email, String password) {
    	this(name, email, password, Role.ROLE_USER);
    }
    
    public void setStore(@Valid Store store) {
    	this.store = store;
    }
    
    public void setRole(Role role) {
    	this.role = role;
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(this.role.name()));
	}
	
	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}
	
	public List<String> getAuthoritiesAsStringList() {
	    return this.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
	}
}
