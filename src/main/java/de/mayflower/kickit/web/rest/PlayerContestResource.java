package de.mayflower.kickit.web.rest;

import de.mayflower.kickit.domain.PlayerContest;
import de.mayflower.kickit.repository.PlayerContestRepository;
import de.mayflower.kickit.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link de.mayflower.kickit.domain.PlayerContest}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PlayerContestResource {

    private final Logger log = LoggerFactory.getLogger(PlayerContestResource.class);

    private static final String ENTITY_NAME = "playerContest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlayerContestRepository playerContestRepository;

    public PlayerContestResource(PlayerContestRepository playerContestRepository) {
        this.playerContestRepository = playerContestRepository;
    }

    /**
     * {@code POST  /player-contests} : Create a new playerContest.
     *
     * @param playerContest the playerContest to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new playerContest, or with status {@code 400 (Bad Request)} if the playerContest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/player-contests")
    public ResponseEntity<PlayerContest> createPlayerContest(@RequestBody PlayerContest playerContest) throws URISyntaxException {
        log.debug("REST request to save PlayerContest : {}", playerContest);
        if (playerContest.getId() != null) {
            throw new BadRequestAlertException("A new playerContest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlayerContest result = playerContestRepository.save(playerContest);
        return ResponseEntity
            .created(new URI("/api/player-contests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /player-contests/:id} : Updates an existing playerContest.
     *
     * @param id the id of the playerContest to save.
     * @param playerContest the playerContest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated playerContest,
     * or with status {@code 400 (Bad Request)} if the playerContest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the playerContest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/player-contests/{id}")
    public ResponseEntity<PlayerContest> updatePlayerContest(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PlayerContest playerContest
    ) throws URISyntaxException {
        log.debug("REST request to update PlayerContest : {}, {}", id, playerContest);
        if (playerContest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, playerContest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!playerContestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PlayerContest result = playerContestRepository.save(playerContest);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, playerContest.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /player-contests/:id} : Partial updates given fields of an existing playerContest, field will ignore if it is null
     *
     * @param id the id of the playerContest to save.
     * @param playerContest the playerContest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated playerContest,
     * or with status {@code 400 (Bad Request)} if the playerContest is not valid,
     * or with status {@code 404 (Not Found)} if the playerContest is not found,
     * or with status {@code 500 (Internal Server Error)} if the playerContest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/player-contests/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PlayerContest> partialUpdatePlayerContest(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PlayerContest playerContest
    ) throws URISyntaxException {
        log.debug("REST request to partial update PlayerContest partially : {}, {}", id, playerContest);
        if (playerContest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, playerContest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!playerContestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PlayerContest> result = playerContestRepository
            .findById(playerContest.getId())
            .map(existingPlayerContest -> {
                if (playerContest.getTeam() != null) {
                    existingPlayerContest.setTeam(playerContest.getTeam());
                }

                return existingPlayerContest;
            })
            .map(playerContestRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, playerContest.getId().toString())
        );
    }

    /**
     * {@code GET  /player-contests} : get all the playerContests.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of playerContests in body.
     */
    @GetMapping("/player-contests")
    public List<PlayerContest> getAllPlayerContests() {
        log.debug("REST request to get all PlayerContests");
        return playerContestRepository.findAll();
    }

    /**
     * {@code GET  /player-contests/:id} : get the "id" playerContest.
     *
     * @param id the id of the playerContest to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the playerContest, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/player-contests/{id}")
    public ResponseEntity<PlayerContest> getPlayerContest(@PathVariable Long id) {
        log.debug("REST request to get PlayerContest : {}", id);
        Optional<PlayerContest> playerContest = playerContestRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(playerContest);
    }

    /**
     * {@code DELETE  /player-contests/:id} : delete the "id" playerContest.
     *
     * @param id the id of the playerContest to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/player-contests/{id}")
    public ResponseEntity<Void> deletePlayerContest(@PathVariable Long id) {
        log.debug("REST request to delete PlayerContest : {}", id);
        playerContestRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
