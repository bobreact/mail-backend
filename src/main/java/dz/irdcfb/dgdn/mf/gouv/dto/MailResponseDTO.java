package dz.irdcfb.dgdn.mf.gouv.dto;

public class MailResponseDTO {
	private String message;

	public MailResponseDTO(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}