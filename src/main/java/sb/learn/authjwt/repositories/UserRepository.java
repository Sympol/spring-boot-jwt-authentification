package sb.learn.authjwt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sb.learn.authjwt.models.User;

import java.util.Optional;

/**
 * @author Symplice BONI
 * project auth-jwt
 * date 21/10/2020
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
