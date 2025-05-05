package nathan.mg.api.user;

public record UserResponseDto(
		Long id,
		String name,
		String email,
		String photo
) {
	public UserResponseDto(User user) {
		this(user.getId(), user.getName(), user.getEmail(), user.getPhoto());
	}
}