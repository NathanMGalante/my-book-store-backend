package nathan.mg.api.store;

import java.time.LocalDateTime;

public record StoreResponseDto(
		Long id,
		LocalDateTime creationDateTime,
		String name,
		String slogan,
		String banner
) {
	
	public StoreResponseDto(Store store) {
		this(store.getId(), store.getCreationDateTime(),store.getName(), store.getSlogan(), store.getBanner());
	}
}
