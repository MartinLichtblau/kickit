package de.mayflower.kickit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.mayflower.kickit.domain.enumeration.Location;
import de.mayflower.kickit.domain.enumeration.Team;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Game.
 */
@Entity
@Table(name = "game")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "location", nullable = false)
    private Location location;

    @Enumerated(EnumType.STRING)
    @Column(name = "winner_team")
    private Team winnerTeam;

    @OneToMany(mappedBy = "game")
    @JsonIgnoreProperties(value = { "player", "game" }, allowSetters = true)
    private Set<PlayerGame> gamePlayers = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "contestPlayers", "games" }, allowSetters = true)
    private Contest contest;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Game id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Location getLocation() {
        return this.location;
    }

    public Game location(Location location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Team getWinnerTeam() {
        return this.winnerTeam;
    }

    public Game winnerTeam(Team winnerTeam) {
        this.setWinnerTeam(winnerTeam);
        return this;
    }

    public void setWinnerTeam(Team winnerTeam) {
        this.winnerTeam = winnerTeam;
    }

    public Set<PlayerGame> getGamePlayers() {
        return this.gamePlayers;
    }

    public void setGamePlayers(Set<PlayerGame> playerGames) {
        if (this.gamePlayers != null) {
            this.gamePlayers.forEach(i -> i.setGame(null));
        }
        if (playerGames != null) {
            playerGames.forEach(i -> i.setGame(this));
        }
        this.gamePlayers = playerGames;
    }

    public Game gamePlayers(Set<PlayerGame> playerGames) {
        this.setGamePlayers(playerGames);
        return this;
    }

    public Game addGamePlayers(PlayerGame playerGame) {
        this.gamePlayers.add(playerGame);
        playerGame.setGame(this);
        return this;
    }

    public Game removeGamePlayers(PlayerGame playerGame) {
        this.gamePlayers.remove(playerGame);
        playerGame.setGame(null);
        return this;
    }

    public Contest getContest() {
        return this.contest;
    }

    public void setContest(Contest contest) {
        this.contest = contest;
    }

    public Game contest(Contest contest) {
        this.setContest(contest);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Game)) {
            return false;
        }
        return id != null && id.equals(((Game) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Game{" +
            "id=" + getId() +
            ", location='" + getLocation() + "'" +
            ", winnerTeam='" + getWinnerTeam() + "'" +
            "}";
    }
}
