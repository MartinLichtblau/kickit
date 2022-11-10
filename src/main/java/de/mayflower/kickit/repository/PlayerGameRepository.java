package de.mayflower.kickit.repository;

import de.mayflower.kickit.domain.PlayerGame;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PlayerGame entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlayerGameRepository extends JpaRepository<PlayerGame, Long> {}
