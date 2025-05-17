package dz.irdcfb.dgdn.mf.gouv.dao;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;

import dz.irdcfb.dgdn.mf.gouv.entities.MailDepart;


public interface MailDepartRepository extends JpaRepository <MailDepart, Integer> {
	Boolean existsByNumDepartAndDateDepart(Integer numDepart,
			Date dateDepart);
	Boolean existsByNumDepartAndAnnee(Integer numArrive, Integer annee);


}
