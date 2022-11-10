package de.mayflower.kickit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import de.mayflower.kickit.IntegrationTest;
import de.mayflower.kickit.domain.PlayerGame;
import de.mayflower.kickit.domain.enumeration.PlayerPosition;
import de.mayflower.kickit.repository.PlayerGameRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PlayerGameResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PlayerGameResourceIT {

    private static final PlayerPosition DEFAULT_PLAYER_POSITION = PlayerPosition.FRONT;
    private static final PlayerPosition UPDATED_PLAYER_POSITION = PlayerPosition.BACK;

    private static final String ENTITY_API_URL = "/api/player-games";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PlayerGameRepository playerGameRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlayerGameMockMvc;

    private PlayerGame playerGame;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlayerGame createEntity(EntityManager em) {
        PlayerGame playerGame = new PlayerGame().playerPosition(DEFAULT_PLAYER_POSITION);
        return playerGame;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlayerGame createUpdatedEntity(EntityManager em) {
        PlayerGame playerGame = new PlayerGame().playerPosition(UPDATED_PLAYER_POSITION);
        return playerGame;
    }

    @BeforeEach
    public void initTest() {
        playerGame = createEntity(em);
    }

    @Test
    @Transactional
    void createPlayerGame() throws Exception {
        int databaseSizeBeforeCreate = playerGameRepository.findAll().size();
        // Create the PlayerGame
        restPlayerGameMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playerGame))
            )
            .andExpect(status().isCreated());

        // Validate the PlayerGame in the database
        List<PlayerGame> playerGameList = playerGameRepository.findAll();
        assertThat(playerGameList).hasSize(databaseSizeBeforeCreate + 1);
        PlayerGame testPlayerGame = playerGameList.get(playerGameList.size() - 1);
        assertThat(testPlayerGame.getPlayerPosition()).isEqualTo(DEFAULT_PLAYER_POSITION);
    }

    @Test
    @Transactional
    void createPlayerGameWithExistingId() throws Exception {
        // Create the PlayerGame with an existing ID
        playerGame.setId(1L);

        int databaseSizeBeforeCreate = playerGameRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlayerGameMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playerGame))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerGame in the database
        List<PlayerGame> playerGameList = playerGameRepository.findAll();
        assertThat(playerGameList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPlayerGames() throws Exception {
        // Initialize the database
        playerGameRepository.saveAndFlush(playerGame);

        // Get all the playerGameList
        restPlayerGameMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(playerGame.getId().intValue())))
            .andExpect(jsonPath("$.[*].playerPosition").value(hasItem(DEFAULT_PLAYER_POSITION.toString())));
    }

    @Test
    @Transactional
    void getPlayerGame() throws Exception {
        // Initialize the database
        playerGameRepository.saveAndFlush(playerGame);

        // Get the playerGame
        restPlayerGameMockMvc
            .perform(get(ENTITY_API_URL_ID, playerGame.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(playerGame.getId().intValue()))
            .andExpect(jsonPath("$.playerPosition").value(DEFAULT_PLAYER_POSITION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPlayerGame() throws Exception {
        // Get the playerGame
        restPlayerGameMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPlayerGame() throws Exception {
        // Initialize the database
        playerGameRepository.saveAndFlush(playerGame);

        int databaseSizeBeforeUpdate = playerGameRepository.findAll().size();

        // Update the playerGame
        PlayerGame updatedPlayerGame = playerGameRepository.findById(playerGame.getId()).get();
        // Disconnect from session so that the updates on updatedPlayerGame are not directly saved in db
        em.detach(updatedPlayerGame);
        updatedPlayerGame.playerPosition(UPDATED_PLAYER_POSITION);

        restPlayerGameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPlayerGame.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPlayerGame))
            )
            .andExpect(status().isOk());

        // Validate the PlayerGame in the database
        List<PlayerGame> playerGameList = playerGameRepository.findAll();
        assertThat(playerGameList).hasSize(databaseSizeBeforeUpdate);
        PlayerGame testPlayerGame = playerGameList.get(playerGameList.size() - 1);
        assertThat(testPlayerGame.getPlayerPosition()).isEqualTo(UPDATED_PLAYER_POSITION);
    }

    @Test
    @Transactional
    void putNonExistingPlayerGame() throws Exception {
        int databaseSizeBeforeUpdate = playerGameRepository.findAll().size();
        playerGame.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlayerGameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, playerGame.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playerGame))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerGame in the database
        List<PlayerGame> playerGameList = playerGameRepository.findAll();
        assertThat(playerGameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlayerGame() throws Exception {
        int databaseSizeBeforeUpdate = playerGameRepository.findAll().size();
        playerGame.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayerGameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playerGame))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerGame in the database
        List<PlayerGame> playerGameList = playerGameRepository.findAll();
        assertThat(playerGameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlayerGame() throws Exception {
        int databaseSizeBeforeUpdate = playerGameRepository.findAll().size();
        playerGame.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayerGameMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playerGame))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlayerGame in the database
        List<PlayerGame> playerGameList = playerGameRepository.findAll();
        assertThat(playerGameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlayerGameWithPatch() throws Exception {
        // Initialize the database
        playerGameRepository.saveAndFlush(playerGame);

        int databaseSizeBeforeUpdate = playerGameRepository.findAll().size();

        // Update the playerGame using partial update
        PlayerGame partialUpdatedPlayerGame = new PlayerGame();
        partialUpdatedPlayerGame.setId(playerGame.getId());

        partialUpdatedPlayerGame.playerPosition(UPDATED_PLAYER_POSITION);

        restPlayerGameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlayerGame.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlayerGame))
            )
            .andExpect(status().isOk());

        // Validate the PlayerGame in the database
        List<PlayerGame> playerGameList = playerGameRepository.findAll();
        assertThat(playerGameList).hasSize(databaseSizeBeforeUpdate);
        PlayerGame testPlayerGame = playerGameList.get(playerGameList.size() - 1);
        assertThat(testPlayerGame.getPlayerPosition()).isEqualTo(UPDATED_PLAYER_POSITION);
    }

    @Test
    @Transactional
    void fullUpdatePlayerGameWithPatch() throws Exception {
        // Initialize the database
        playerGameRepository.saveAndFlush(playerGame);

        int databaseSizeBeforeUpdate = playerGameRepository.findAll().size();

        // Update the playerGame using partial update
        PlayerGame partialUpdatedPlayerGame = new PlayerGame();
        partialUpdatedPlayerGame.setId(playerGame.getId());

        partialUpdatedPlayerGame.playerPosition(UPDATED_PLAYER_POSITION);

        restPlayerGameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlayerGame.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlayerGame))
            )
            .andExpect(status().isOk());

        // Validate the PlayerGame in the database
        List<PlayerGame> playerGameList = playerGameRepository.findAll();
        assertThat(playerGameList).hasSize(databaseSizeBeforeUpdate);
        PlayerGame testPlayerGame = playerGameList.get(playerGameList.size() - 1);
        assertThat(testPlayerGame.getPlayerPosition()).isEqualTo(UPDATED_PLAYER_POSITION);
    }

    @Test
    @Transactional
    void patchNonExistingPlayerGame() throws Exception {
        int databaseSizeBeforeUpdate = playerGameRepository.findAll().size();
        playerGame.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlayerGameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, playerGame.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(playerGame))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerGame in the database
        List<PlayerGame> playerGameList = playerGameRepository.findAll();
        assertThat(playerGameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlayerGame() throws Exception {
        int databaseSizeBeforeUpdate = playerGameRepository.findAll().size();
        playerGame.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayerGameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(playerGame))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerGame in the database
        List<PlayerGame> playerGameList = playerGameRepository.findAll();
        assertThat(playerGameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlayerGame() throws Exception {
        int databaseSizeBeforeUpdate = playerGameRepository.findAll().size();
        playerGame.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayerGameMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(playerGame))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlayerGame in the database
        List<PlayerGame> playerGameList = playerGameRepository.findAll();
        assertThat(playerGameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlayerGame() throws Exception {
        // Initialize the database
        playerGameRepository.saveAndFlush(playerGame);

        int databaseSizeBeforeDelete = playerGameRepository.findAll().size();

        // Delete the playerGame
        restPlayerGameMockMvc
            .perform(delete(ENTITY_API_URL_ID, playerGame.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PlayerGame> playerGameList = playerGameRepository.findAll();
        assertThat(playerGameList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
