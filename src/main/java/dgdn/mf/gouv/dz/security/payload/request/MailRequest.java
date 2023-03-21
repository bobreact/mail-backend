package dgdn.mf.gouv.dz.security.payload.request;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailRequest {
	private Integer numdepart;
	private Date datedepart;

}
