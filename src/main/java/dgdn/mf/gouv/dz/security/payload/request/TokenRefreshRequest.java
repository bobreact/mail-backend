package dgdn.mf.gouv.dz.security.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRefreshRequest {
	private String refreshToken;
}
