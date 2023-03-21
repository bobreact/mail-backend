package dgdn.mf.gouv.dz.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dgdn.mf.gouv.dz.security.model.ERole;
import dgdn.mf.gouv.dz.security.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(ERole name);
}
