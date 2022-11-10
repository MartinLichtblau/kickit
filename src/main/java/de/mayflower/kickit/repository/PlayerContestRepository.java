package de.mayflower.kickit.repository;

import de.mayflower.kickit.domain.PlayerContest;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PlayerContest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlayerContestRepository extends JpaRepository<PlayerContest, Long> {}
