package nathan.mg.api.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import nathan.mg.api.store.Store;

public interface UserRepository extends JpaRepository<User, Long> {

	UserDetails findByEmail(String email);

	List<User> findAllByRoleOrderByName(Role roleUser);

	List<User> findAllByOrderByNameAsc();

	List<User> findAllByStoreAndRole(Store store, Role roleEmployee);

}
