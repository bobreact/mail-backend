package dz.irdcfb.dgdn.mf.gouv.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@SuppressWarnings("serial")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MailDepart implements Serializable {
	/**
	 *
	 */
	// private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotNull(message = "Renseigner ce Champ")
	@Column(name = "num_depart")
	private Integer numDepart;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Past(message = "Date de départ incorrecte")
	@NotNull(message = "Renseigner ce Champ")
	@Column(name = "date_depart")
	private Date dateDepart;

	@NotBlank(message = "Champ Expediteur ne doit pas être vide")
	@Column(name = "destinataire")
	private String destinataire;

	@NotBlank(message = "Champ Objet ne doit pas être vide")
	@Column(name = "objet")
	private String objet;

	private Integer annee;

	private Integer structure;

	@Column(name = "fichier")
	private String file;

}
