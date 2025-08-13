package dz.irdcfb.dgdn.mf.gouv.security.security.jwt.advice;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("unused")
@AllArgsConstructor
@Getter
@Setter
public class ErrorMessage {
	private int statusCode;
	private Date timestamp;
	private String message;
	private String description;
}
