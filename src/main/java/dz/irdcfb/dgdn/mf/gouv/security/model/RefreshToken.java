package dz.irdcfb.dgdn.mf.gouv.security.model;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "refreshtoken")
@Getter
@Setter
public class RefreshToken {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@OneToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;
	@Column(nullable = false, unique = true)
	private String token;
	@Column(nullable = false)
	private Instant expiryDate;
}
