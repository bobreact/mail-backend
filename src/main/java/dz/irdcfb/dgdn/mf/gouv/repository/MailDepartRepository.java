package dz.irdcfb.dgdn.mf.gouv.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;

import dz.irdcfb.dgdn.mf.gouv.entities.MailDepart;

public interface MailDepartRepository extends JpaRepository<MailDepart, Integer> {
	Boolean existsByNumDepartAndDateDepart(Integer numDepart, Date dateDepart);

	Boolean existsByNumDepartAndAnneeAndStructure(Integer numArrive, Integer annee, Integer structure);

}
