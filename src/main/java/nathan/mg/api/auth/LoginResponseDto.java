package nathan.mg.api.auth;

import nathan.mg.api.store.StoreResponseDto;
import nathan.mg.api.user.UserResponseDto;

public record LoginResponseDto(String accessToken, String refreshToken, UserResponseDto user, StoreResponseDto store) {

}
