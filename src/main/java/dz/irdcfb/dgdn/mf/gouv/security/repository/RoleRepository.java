package dz.irdcfb.dgdn.mf.gouv.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dz.irdcfb.dgdn.mf.gouv.security.model.ERole;
import dz.irdcfb.dgdn.mf.gouv.security.model.Role;


public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(ERole name);
}
