package nathan.mg.api.store;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {

	Page<Store> findAllByDeletionDateTimeNull(Pageable pagination);

}
