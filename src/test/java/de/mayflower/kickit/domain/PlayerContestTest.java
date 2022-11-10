package de.mayflower.kickit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import de.mayflower.kickit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlayerContestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlayerContest.class);
        PlayerContest playerContest1 = new PlayerContest();
        playerContest1.setId(1L);
        PlayerContest playerContest2 = new PlayerContest();
        playerContest2.setId(playerContest1.getId());
        assertThat(playerContest1).isEqualTo(playerContest2);
        playerContest2.setId(2L);
        assertThat(playerContest1).isNotEqualTo(playerContest2);
        playerContest1.setId(null);
        assertThat(playerContest1).isNotEqualTo(playerContest2);
    }
}
