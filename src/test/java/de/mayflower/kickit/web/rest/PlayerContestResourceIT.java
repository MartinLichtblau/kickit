package de.mayflower.kickit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import de.mayflower.kickit.IntegrationTest;
import de.mayflower.kickit.domain.PlayerContest;
import de.mayflower.kickit.domain.enumeration.Team;
import de.mayflower.kickit.repository.PlayerContestRepository;
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
 * Integration tests for the {@link PlayerContestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PlayerContestResourceIT {

    private static final Team DEFAULT_TEAM = Team.T1;
    private static final Team UPDATED_TEAM = Team.T2;

    private static final String ENTITY_API_URL = "/api/player-contests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PlayerContestRepository playerContestRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlayerContestMockMvc;

    private PlayerContest playerContest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlayerContest createEntity(EntityManager em) {
        PlayerContest playerContest = new PlayerContest().team(DEFAULT_TEAM);
        return playerContest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlayerContest createUpdatedEntity(EntityManager em) {
        PlayerContest playerContest = new PlayerContest().team(UPDATED_TEAM);
        return playerContest;
    }

    @BeforeEach
    public void initTest() {
        playerContest = createEntity(em);
    }

    @Test
    @Transactional
    void createPlayerContest() throws Exception {
        int databaseSizeBeforeCreate = playerContestRepository.findAll().size();
        // Create the PlayerContest
        restPlayerContestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playerContest))
            )
            .andExpect(status().isCreated());

        // Validate the PlayerContest in the database
        List<PlayerContest> playerContestList = playerContestRepository.findAll();
        assertThat(playerContestList).hasSize(databaseSizeBeforeCreate + 1);
        PlayerContest testPlayerContest = playerContestList.get(playerContestList.size() - 1);
        assertThat(testPlayerContest.getTeam()).isEqualTo(DEFAULT_TEAM);
    }

    @Test
    @Transactional
    void createPlayerContestWithExistingId() throws Exception {
        // Create the PlayerContest with an existing ID
        playerContest.setId(1L);

        int databaseSizeBeforeCreate = playerContestRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlayerContestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playerContest))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerContest in the database
        List<PlayerContest> playerContestList = playerContestRepository.findAll();
        assertThat(playerContestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPlayerContests() throws Exception {
        // Initialize the database
        playerContestRepository.saveAndFlush(playerContest);

        // Get all the playerContestList
        restPlayerContestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(playerContest.getId().intValue())))
            .andExpect(jsonPath("$.[*].team").value(hasItem(DEFAULT_TEAM.toString())));
    }

    @Test
    @Transactional
    void getPlayerContest() throws Exception {
        // Initialize the database
        playerContestRepository.saveAndFlush(playerContest);

        // Get the playerContest
        restPlayerContestMockMvc
            .perform(get(ENTITY_API_URL_ID, playerContest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(playerContest.getId().intValue()))
            .andExpect(jsonPath("$.team").value(DEFAULT_TEAM.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPlayerContest() throws Exception {
        // Get the playerContest
        restPlayerContestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPlayerContest() throws Exception {
        // Initialize the database
        playerContestRepository.saveAndFlush(playerContest);

        int databaseSizeBeforeUpdate = playerContestRepository.findAll().size();

        // Update the playerContest
        PlayerContest updatedPlayerContest = playerContestRepository.findById(playerContest.getId()).get();
        // Disconnect from session so that the updates on updatedPlayerContest are not directly saved in db
        em.detach(updatedPlayerContest);
        updatedPlayerContest.team(UPDATED_TEAM);

        restPlayerContestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPlayerContest.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPlayerContest))
            )
            .andExpect(status().isOk());

        // Validate the PlayerContest in the database
        List<PlayerContest> playerContestList = playerContestRepository.findAll();
        assertThat(playerContestList).hasSize(databaseSizeBeforeUpdate);
        PlayerContest testPlayerContest = playerContestList.get(playerContestList.size() - 1);
        assertThat(testPlayerContest.getTeam()).isEqualTo(UPDATED_TEAM);
    }

    @Test
    @Transactional
    void putNonExistingPlayerContest() throws Exception {
        int databaseSizeBeforeUpdate = playerContestRepository.findAll().size();
        playerContest.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlayerContestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, playerContest.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playerContest))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerContest in the database
        List<PlayerContest> playerContestList = playerContestRepository.findAll();
        assertThat(playerContestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlayerContest() throws Exception {
        int databaseSizeBeforeUpdate = playerContestRepository.findAll().size();
        playerContest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayerContestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playerContest))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerContest in the database
        List<PlayerContest> playerContestList = playerContestRepository.findAll();
        assertThat(playerContestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlayerContest() throws Exception {
        int databaseSizeBeforeUpdate = playerContestRepository.findAll().size();
        playerContest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayerContestMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playerContest))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlayerContest in the database
        List<PlayerContest> playerContestList = playerContestRepository.findAll();
        assertThat(playerContestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlayerContestWithPatch() throws Exception {
        // Initialize the database
        playerContestRepository.saveAndFlush(playerContest);

        int databaseSizeBeforeUpdate = playerContestRepository.findAll().size();

        // Update the playerContest using partial update
        PlayerContest partialUpdatedPlayerContest = new PlayerContest();
        partialUpdatedPlayerContest.setId(playerContest.getId());

        partialUpdatedPlayerContest.team(UPDATED_TEAM);

        restPlayerContestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlayerContest.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlayerContest))
            )
            .andExpect(status().isOk());

        // Validate the PlayerContest in the database
        List<PlayerContest> playerContestList = playerContestRepository.findAll();
        assertThat(playerContestList).hasSize(databaseSizeBeforeUpdate);
        PlayerContest testPlayerContest = playerContestList.get(playerContestList.size() - 1);
        assertThat(testPlayerContest.getTeam()).isEqualTo(UPDATED_TEAM);
    }

    @Test
    @Transactional
    void fullUpdatePlayerContestWithPatch() throws Exception {
        // Initialize the database
        playerContestRepository.saveAndFlush(playerContest);

        int databaseSizeBeforeUpdate = playerContestRepository.findAll().size();

        // Update the playerContest using partial update
        PlayerContest partialUpdatedPlayerContest = new PlayerContest();
        partialUpdatedPlayerContest.setId(playerContest.getId());

        partialUpdatedPlayerContest.team(UPDATED_TEAM);

        restPlayerContestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlayerContest.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlayerContest))
            )
            .andExpect(status().isOk());

        // Validate the PlayerContest in the database
        List<PlayerContest> playerContestList = playerContestRepository.findAll();
        assertThat(playerContestList).hasSize(databaseSizeBeforeUpdate);
        PlayerContest testPlayerContest = playerContestList.get(playerContestList.size() - 1);
        assertThat(testPlayerContest.getTeam()).isEqualTo(UPDATED_TEAM);
    }

    @Test
    @Transactional
    void patchNonExistingPlayerContest() throws Exception {
        int databaseSizeBeforeUpdate = playerContestRepository.findAll().size();
        playerContest.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlayerContestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, playerContest.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(playerContest))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerContest in the database
        List<PlayerContest> playerContestList = playerContestRepository.findAll();
        assertThat(playerContestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlayerContest() throws Exception {
        int databaseSizeBeforeUpdate = playerContestRepository.findAll().size();
        playerContest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayerContestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(playerContest))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerContest in the database
        List<PlayerContest> playerContestList = playerContestRepository.findAll();
        assertThat(playerContestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlayerContest() throws Exception {
        int databaseSizeBeforeUpdate = playerContestRepository.findAll().size();
        playerContest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayerContestMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(playerContest))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlayerContest in the database
        List<PlayerContest> playerContestList = playerContestRepository.findAll();
        assertThat(playerContestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlayerContest() throws Exception {
        // Initialize the database
        playerContestRepository.saveAndFlush(playerContest);

        int databaseSizeBeforeDelete = playerContestRepository.findAll().size();

        // Delete the playerContest
        restPlayerContestMockMvc
            .perform(delete(ENTITY_API_URL_ID, playerContest.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PlayerContest> playerContestList = playerContestRepository.findAll();
        assertThat(playerContestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
