package dz.irdcfb.dgdn.mf.gouv.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dz.irdcfb.dgdn.mf.gouv.entities.ERole;
import dz.irdcfb.dgdn.mf.gouv.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(ERole name);
}
