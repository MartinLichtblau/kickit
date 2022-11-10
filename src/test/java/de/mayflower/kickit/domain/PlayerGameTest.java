package de.mayflower.kickit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import de.mayflower.kickit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlayerGameTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlayerGame.class);
        PlayerGame playerGame1 = new PlayerGame();
        playerGame1.setId(1L);
        PlayerGame playerGame2 = new PlayerGame();
        playerGame2.setId(playerGame1.getId());
        assertThat(playerGame1).isEqualTo(playerGame2);
        playerGame2.setId(2L);
        assertThat(playerGame1).isNotEqualTo(playerGame2);
        playerGame1.setId(null);
        assertThat(playerGame1).isNotEqualTo(playerGame2);
    }
}
