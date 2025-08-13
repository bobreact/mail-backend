package dz.irdcfb.dgdn.mf.gouv.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dz.irdcfb.dgdn.mf.gouv.entities.MailArrive;

@Repository
public interface MailArriveRepository extends JpaRepository<dz.irdcfb.dgdn.mf.gouv.entities.MailArrive, Integer> {

	/*
	 * @Query("SELECT m FROM MailArrive m WHERE m.objet LIKE %?1%") public
	 * Page<MailArrive> findByObjet(@Param("motCle") String motCle, Pageable
	 * pageable);
	 *
	 * @Query("select m from MailArrive m where m.objet like %?1%" +
	 * "and m.dateArrivee between ?2 and ?3") public Page<MailArrive>
	 * findByObjetAndDateBetween(@Param("motCle") String motCle,
	 *
	 * @DateTimeFormat(pattern = "yyyy-MM-dd") Date date1, @DateTimeFormat(pattern =
	 * "yyyy-MM-dd") Date date2, Pageable pageable);
	 *
	 * @Query("select m from MailArrive m where m.dateArrivee between ?2 and ?3")
	 * public Page<MailArrive> findByDateBetween(@DateTimeFormat(pattern =
	 * "yyyy-MM-dd") Date date1,
	 *
	 * @DateTimeFormat(pattern = "yyyy-MM-dd") Date date2, Pageable pageable);
	 *
	 * @Query("select m from MailArrive m where m.objet like %?1%" +
	 * " and m.dateArrivee >= ?2") public Page<MailArrive>
	 * findByObjetAndDate(@Param("motCle") String motCle,
	 *
	 * @DateTimeFormat(pattern = "yyyy-MM-dd") Date date, Pageable pageable);
	 *
	 * @Query(value = "SELECT max(m.numArrive) from MailArrive m") public Integer
	 * max();
	 */

	Boolean existsByNumArriveAndNumDepartExpAndDateDepartExp(Integer numArrive, Integer numDepartExp,
			Date dateDepartExp);

	Boolean existsByNumArriveAndAnneeAndStructure(Integer numArrive, Integer annee, Integer structure);

	List<MailArrive> findByStructure(Integer structure);
}
