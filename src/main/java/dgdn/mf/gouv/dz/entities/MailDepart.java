package dgdn.mf.gouv.dz.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class MailDepart {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private long id;
	private long numDepart;
	@DateTimeFormat(pattern = "yyyy-mm-dd")
	private Date datedepart;
	private long nombrePiece;
	private String destinataire;
	private String objet;
	private long numArchive;
	private String observation;

	public MailDepart() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MailDepart(long numDepart, Date datedepart, long nombrePiece, String destinataire, String objet,
			long numArchive, String observation) {
		super();
		this.numDepart = numDepart;
		this.datedepart = datedepart;
		this.nombrePiece = nombrePiece;
		this.destinataire = destinataire;
		this.objet = objet;
		this.numArchive = numArchive;
		this.observation = observation;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getNumDepart() {
		return numDepart;
	}

	public void setNumDepart(long numDepart) {
		this.numDepart = numDepart;
	}

	public Date getDatedepart() {
		return datedepart;
	}

	public void setDatedepart(Date datedepart) {
		String pattern = "dd/mm/yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		@SuppressWarnings("unused")
		String date = simpleDateFormat.format(datedepart);
		this.datedepart = datedepart;

	}

	public long getNombrePiece() {
		return nombrePiece;
	}

	public void setNombrePiece(long nombrePiece) {
		this.nombrePiece = nombrePiece;
	}

	public String getDestinataire() {
		return destinataire;
	}

	public void setDestinataire(String destinataire) {
		this.destinataire = destinataire;
	}

	public String getObjet() {
		return objet;
	}

	public void setObjet(String objet) {
		this.objet = objet;
	}

	public long getNumArchive() {
		return numArchive;
	}

	public void setNumArchive(long numArchive) {
		this.numArchive = numArchive;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

}
