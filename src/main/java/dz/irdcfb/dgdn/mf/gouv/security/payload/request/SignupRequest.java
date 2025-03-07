package dz.irdcfb.dgdn.mf.gouv.security.payload.request;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
	private String username;
	private String email;
	private Set<String> role;
	private String password;
}
