package dz.irdcfb.dgdn.mf.gouv.security.payload.response;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class JwtResponse {
	private String token;
	private final String type = "Bearer";
	private String refreshToken;
	private Long id;
	private String username;
	private String email;
	private List<String> roles;
	private Integer structure;

	public JwtResponse(String token, String refreshToken, Long id, String username, String email, List<String> roles,
			Integer structure) {
		this.token = token;
		this.refreshToken = refreshToken;
		this.id = id;
		this.username = username;
		this.email = email;
		this.roles = roles;
		this.structure = structure;
	}
}
