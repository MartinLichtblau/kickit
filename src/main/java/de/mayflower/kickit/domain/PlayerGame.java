package de.mayflower.kickit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.mayflower.kickit.domain.enumeration.PlayerPosition;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A PlayerGame.
 */
@Entity
@Table(name = "player_game")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlayerGame implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "player_position")
    private PlayerPosition playerPosition;

    @ManyToOne
    @JsonIgnoreProperties(value = { "playedGames", "playedContests" }, allowSetters = true)
    private Player player;

    @ManyToOne
    @JsonIgnoreProperties(value = { "gamePlayers", "contest" }, allowSetters = true)
    private Game game;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PlayerGame id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlayerPosition getPlayerPosition() {
        return this.playerPosition;
    }

    public PlayerGame playerPosition(PlayerPosition playerPosition) {
        this.setPlayerPosition(playerPosition);
        return this;
    }

    public void setPlayerPosition(PlayerPosition playerPosition) {
        this.playerPosition = playerPosition;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public PlayerGame player(Player player) {
        this.setPlayer(player);
        return this;
    }

    public Game getGame() {
        return this.game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public PlayerGame game(Game game) {
        this.setGame(game);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlayerGame)) {
            return false;
        }
        return id != null && id.equals(((PlayerGame) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlayerGame{" +
            "id=" + getId() +
            ", playerPosition='" + getPlayerPosition() + "'" +
            "}";
    }
}
