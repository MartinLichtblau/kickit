package de.mayflower.kickit.web.rest;

import de.mayflower.kickit.domain.PlayerGame;
import de.mayflower.kickit.repository.PlayerGameRepository;
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
 * REST controller for managing {@link de.mayflower.kickit.domain.PlayerGame}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PlayerGameResource {

    private final Logger log = LoggerFactory.getLogger(PlayerGameResource.class);

    private static final String ENTITY_NAME = "playerGame";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlayerGameRepository playerGameRepository;

    public PlayerGameResource(PlayerGameRepository playerGameRepository) {
        this.playerGameRepository = playerGameRepository;
    }

    /**
     * {@code POST  /player-games} : Create a new playerGame.
     *
     * @param playerGame the playerGame to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new playerGame, or with status {@code 400 (Bad Request)} if the playerGame has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/player-games")
    public ResponseEntity<PlayerGame> createPlayerGame(@RequestBody PlayerGame playerGame) throws URISyntaxException {
        log.debug("REST request to save PlayerGame : {}", playerGame);
        if (playerGame.getId() != null) {
            throw new BadRequestAlertException("A new playerGame cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlayerGame result = playerGameRepository.save(playerGame);
        return ResponseEntity
            .created(new URI("/api/player-games/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /player-games/:id} : Updates an existing playerGame.
     *
     * @param id the id of the playerGame to save.
     * @param playerGame the playerGame to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated playerGame,
     * or with status {@code 400 (Bad Request)} if the playerGame is not valid,
     * or with status {@code 500 (Internal Server Error)} if the playerGame couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/player-games/{id}")
    public ResponseEntity<PlayerGame> updatePlayerGame(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PlayerGame playerGame
    ) throws URISyntaxException {
        log.debug("REST request to update PlayerGame : {}, {}", id, playerGame);
        if (playerGame.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, playerGame.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!playerGameRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PlayerGame result = playerGameRepository.save(playerGame);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, playerGame.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /player-games/:id} : Partial updates given fields of an existing playerGame, field will ignore if it is null
     *
     * @param id the id of the playerGame to save.
     * @param playerGame the playerGame to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated playerGame,
     * or with status {@code 400 (Bad Request)} if the playerGame is not valid,
     * or with status {@code 404 (Not Found)} if the playerGame is not found,
     * or with status {@code 500 (Internal Server Error)} if the playerGame couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/player-games/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PlayerGame> partialUpdatePlayerGame(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PlayerGame playerGame
    ) throws URISyntaxException {
        log.debug("REST request to partial update PlayerGame partially : {}, {}", id, playerGame);
        if (playerGame.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, playerGame.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!playerGameRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PlayerGame> result = playerGameRepository
            .findById(playerGame.getId())
            .map(existingPlayerGame -> {
                if (playerGame.getPlayerPosition() != null) {
                    existingPlayerGame.setPlayerPosition(playerGame.getPlayerPosition());
                }

                return existingPlayerGame;
            })
            .map(playerGameRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, playerGame.getId().toString())
        );
    }

    /**
     * {@code GET  /player-games} : get all the playerGames.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of playerGames in body.
     */
    @GetMapping("/player-games")
    public List<PlayerGame> getAllPlayerGames() {
        log.debug("REST request to get all PlayerGames");
        return playerGameRepository.findAll();
    }

    /**
     * {@code GET  /player-games/:id} : get the "id" playerGame.
     *
     * @param id the id of the playerGame to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the playerGame, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/player-games/{id}")
    public ResponseEntity<PlayerGame> getPlayerGame(@PathVariable Long id) {
        log.debug("REST request to get PlayerGame : {}", id);
        Optional<PlayerGame> playerGame = playerGameRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(playerGame);
    }

    /**
     * {@code DELETE  /player-games/:id} : delete the "id" playerGame.
     *
     * @param id the id of the playerGame to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/player-games/{id}")
    public ResponseEntity<Void> deletePlayerGame(@PathVariable Long id) {
        log.debug("REST request to delete PlayerGame : {}", id);
        playerGameRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
