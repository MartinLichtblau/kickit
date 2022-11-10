package de.mayflower.kickit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import de.mayflower.kickit.IntegrationTest;
import de.mayflower.kickit.domain.Contest;
import de.mayflower.kickit.domain.enumeration.ContestMode;
import de.mayflower.kickit.domain.enumeration.Location;
import de.mayflower.kickit.domain.enumeration.Team;
import de.mayflower.kickit.repository.ContestRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link ContestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContestResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final ContestMode DEFAULT_CONTEST_MODE = ContestMode.ONE;
    private static final ContestMode UPDATED_CONTEST_MODE = ContestMode.BO3;

    private static final Location DEFAULT_LOCATION = Location.WUERZBURG;
    private static final Location UPDATED_LOCATION = Location.BERLIN;

    private static final Team DEFAULT_WINNER_TEAM = Team.T1;
    private static final Team UPDATED_WINNER_TEAM = Team.T2;

    private static final String ENTITY_API_URL = "/api/contests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContestMockMvc;

    private Contest contest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contest createEntity(EntityManager em) {
        Contest contest = new Contest()
            .date(DEFAULT_DATE)
            .contestMode(DEFAULT_CONTEST_MODE)
            .location(DEFAULT_LOCATION)
            .winnerTeam(DEFAULT_WINNER_TEAM);
        return contest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contest createUpdatedEntity(EntityManager em) {
        Contest contest = new Contest()
            .date(UPDATED_DATE)
            .contestMode(UPDATED_CONTEST_MODE)
            .location(UPDATED_LOCATION)
            .winnerTeam(UPDATED_WINNER_TEAM);
        return contest;
    }

    @BeforeEach
    public void initTest() {
        contest = createEntity(em);
    }

    @Test
    @Transactional
    void createContest() throws Exception {
        int databaseSizeBeforeCreate = contestRepository.findAll().size();
        // Create the Contest
        restContestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contest))
            )
            .andExpect(status().isCreated());

        // Validate the Contest in the database
        List<Contest> contestList = contestRepository.findAll();
        assertThat(contestList).hasSize(databaseSizeBeforeCreate + 1);
        Contest testContest = contestList.get(contestList.size() - 1);
        assertThat(testContest.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testContest.getContestMode()).isEqualTo(DEFAULT_CONTEST_MODE);
        assertThat(testContest.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testContest.getWinnerTeam()).isEqualTo(DEFAULT_WINNER_TEAM);
    }

    @Test
    @Transactional
    void createContestWithExistingId() throws Exception {
        // Create the Contest with an existing ID
        contest.setId(1L);

        int databaseSizeBeforeCreate = contestRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contest))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contest in the database
        List<Contest> contestList = contestRepository.findAll();
        assertThat(contestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = contestRepository.findAll().size();
        // set the field null
        contest.setDate(null);

        // Create the Contest, which fails.

        restContestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contest))
            )
            .andExpect(status().isBadRequest());

        List<Contest> contestList = contestRepository.findAll();
        assertThat(contestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContestModeIsRequired() throws Exception {
        int databaseSizeBeforeTest = contestRepository.findAll().size();
        // set the field null
        contest.setContestMode(null);

        // Create the Contest, which fails.

        restContestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contest))
            )
            .andExpect(status().isBadRequest());

        List<Contest> contestList = contestRepository.findAll();
        assertThat(contestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLocationIsRequired() throws Exception {
        int databaseSizeBeforeTest = contestRepository.findAll().size();
        // set the field null
        contest.setLocation(null);

        // Create the Contest, which fails.

        restContestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contest))
            )
            .andExpect(status().isBadRequest());

        List<Contest> contestList = contestRepository.findAll();
        assertThat(contestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContests() throws Exception {
        // Initialize the database
        contestRepository.saveAndFlush(contest);

        // Get all the contestList
        restContestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contest.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].contestMode").value(hasItem(DEFAULT_CONTEST_MODE.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].winnerTeam").value(hasItem(DEFAULT_WINNER_TEAM.toString())));
    }

    @Test
    @Transactional
    void getContest() throws Exception {
        // Initialize the database
        contestRepository.saveAndFlush(contest);

        // Get the contest
        restContestMockMvc
            .perform(get(ENTITY_API_URL_ID, contest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contest.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.contestMode").value(DEFAULT_CONTEST_MODE.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.winnerTeam").value(DEFAULT_WINNER_TEAM.toString()));
    }

    @Test
    @Transactional
    void getNonExistingContest() throws Exception {
        // Get the contest
        restContestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContest() throws Exception {
        // Initialize the database
        contestRepository.saveAndFlush(contest);

        int databaseSizeBeforeUpdate = contestRepository.findAll().size();

        // Update the contest
        Contest updatedContest = contestRepository.findById(contest.getId()).get();
        // Disconnect from session so that the updates on updatedContest are not directly saved in db
        em.detach(updatedContest);
        updatedContest.date(UPDATED_DATE).contestMode(UPDATED_CONTEST_MODE).location(UPDATED_LOCATION).winnerTeam(UPDATED_WINNER_TEAM);

        restContestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedContest.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedContest))
            )
            .andExpect(status().isOk());

        // Validate the Contest in the database
        List<Contest> contestList = contestRepository.findAll();
        assertThat(contestList).hasSize(databaseSizeBeforeUpdate);
        Contest testContest = contestList.get(contestList.size() - 1);
        assertThat(testContest.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testContest.getContestMode()).isEqualTo(UPDATED_CONTEST_MODE);
        assertThat(testContest.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testContest.getWinnerTeam()).isEqualTo(UPDATED_WINNER_TEAM);
    }

    @Test
    @Transactional
    void putNonExistingContest() throws Exception {
        int databaseSizeBeforeUpdate = contestRepository.findAll().size();
        contest.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contest.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contest))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contest in the database
        List<Contest> contestList = contestRepository.findAll();
        assertThat(contestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContest() throws Exception {
        int databaseSizeBeforeUpdate = contestRepository.findAll().size();
        contest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contest))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contest in the database
        List<Contest> contestList = contestRepository.findAll();
        assertThat(contestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContest() throws Exception {
        int databaseSizeBeforeUpdate = contestRepository.findAll().size();
        contest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContestMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contest))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contest in the database
        List<Contest> contestList = contestRepository.findAll();
        assertThat(contestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContestWithPatch() throws Exception {
        // Initialize the database
        contestRepository.saveAndFlush(contest);

        int databaseSizeBeforeUpdate = contestRepository.findAll().size();

        // Update the contest using partial update
        Contest partialUpdatedContest = new Contest();
        partialUpdatedContest.setId(contest.getId());

        partialUpdatedContest.date(UPDATED_DATE).contestMode(UPDATED_CONTEST_MODE);

        restContestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContest.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContest))
            )
            .andExpect(status().isOk());

        // Validate the Contest in the database
        List<Contest> contestList = contestRepository.findAll();
        assertThat(contestList).hasSize(databaseSizeBeforeUpdate);
        Contest testContest = contestList.get(contestList.size() - 1);
        assertThat(testContest.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testContest.getContestMode()).isEqualTo(UPDATED_CONTEST_MODE);
        assertThat(testContest.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testContest.getWinnerTeam()).isEqualTo(DEFAULT_WINNER_TEAM);
    }

    @Test
    @Transactional
    void fullUpdateContestWithPatch() throws Exception {
        // Initialize the database
        contestRepository.saveAndFlush(contest);

        int databaseSizeBeforeUpdate = contestRepository.findAll().size();

        // Update the contest using partial update
        Contest partialUpdatedContest = new Contest();
        partialUpdatedContest.setId(contest.getId());

        partialUpdatedContest
            .date(UPDATED_DATE)
            .contestMode(UPDATED_CONTEST_MODE)
            .location(UPDATED_LOCATION)
            .winnerTeam(UPDATED_WINNER_TEAM);

        restContestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContest.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContest))
            )
            .andExpect(status().isOk());

        // Validate the Contest in the database
        List<Contest> contestList = contestRepository.findAll();
        assertThat(contestList).hasSize(databaseSizeBeforeUpdate);
        Contest testContest = contestList.get(contestList.size() - 1);
        assertThat(testContest.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testContest.getContestMode()).isEqualTo(UPDATED_CONTEST_MODE);
        assertThat(testContest.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testContest.getWinnerTeam()).isEqualTo(UPDATED_WINNER_TEAM);
    }

    @Test
    @Transactional
    void patchNonExistingContest() throws Exception {
        int databaseSizeBeforeUpdate = contestRepository.findAll().size();
        contest.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contest.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contest))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contest in the database
        List<Contest> contestList = contestRepository.findAll();
        assertThat(contestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContest() throws Exception {
        int databaseSizeBeforeUpdate = contestRepository.findAll().size();
        contest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contest))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contest in the database
        List<Contest> contestList = contestRepository.findAll();
        assertThat(contestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContest() throws Exception {
        int databaseSizeBeforeUpdate = contestRepository.findAll().size();
        contest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContestMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contest))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contest in the database
        List<Contest> contestList = contestRepository.findAll();
        assertThat(contestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContest() throws Exception {
        // Initialize the database
        contestRepository.saveAndFlush(contest);

        int databaseSizeBeforeDelete = contestRepository.findAll().size();

        // Delete the contest
        restContestMockMvc
            .perform(delete(ENTITY_API_URL_ID, contest.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Contest> contestList = contestRepository.findAll();
        assertThat(contestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
