package sb.learn.authjwt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sb.learn.authjwt.models.ERole;
import sb.learn.authjwt.models.Role;

import java.util.Optional;

/**
 * @author Symplice BONI
 * project auth-jwt
 * date 21/10/2020
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
