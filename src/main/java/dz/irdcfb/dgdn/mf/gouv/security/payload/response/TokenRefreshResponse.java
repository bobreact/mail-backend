package dz.irdcfb.dgdn.mf.gouv.security.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TokenRefreshResponse {
	private String accessToken;
	private String refreshToken;
	private final String tokenType = "Bearer";

}
