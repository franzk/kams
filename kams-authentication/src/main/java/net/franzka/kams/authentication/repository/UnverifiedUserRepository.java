package net.franzka.kams.authentication.repository;

import net.franzka.kams.authentication.model.UnverifiedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Jpa Repository for {@link UnverifiedUser}
 */
@Repository
public interface UnverifiedUserRepository extends JpaRepository<UnverifiedUser, Integer> {

    Optional<UnverifiedUser> findByEmail(String email);

    Optional<UnverifiedUser> findByActivationToken(String activationToken);

}
