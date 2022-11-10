package de.mayflower.kickit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.mayflower.kickit.domain.enumeration.ContestMode;
import de.mayflower.kickit.domain.enumeration.Location;
import de.mayflower.kickit.domain.enumeration.Team;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Contest.
 */
@Entity
@Table(name = "contest")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Contest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "contest_mode", nullable = false)
    private ContestMode contestMode;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "location", nullable = false)
    private Location location;

    @Enumerated(EnumType.STRING)
    @Column(name = "winner_team")
    private Team winnerTeam;

    @OneToMany(mappedBy = "contest")
    @JsonIgnoreProperties(value = { "player", "contest" }, allowSetters = true)
    private Set<PlayerContest> contestPlayers = new HashSet<>();

    @OneToMany(mappedBy = "contest")
    @JsonIgnoreProperties(value = { "gamePlayers", "contest" }, allowSetters = true)
    private Set<Game> games = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Contest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Contest date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public ContestMode getContestMode() {
        return this.contestMode;
    }

    public Contest contestMode(ContestMode contestMode) {
        this.setContestMode(contestMode);
        return this;
    }

    public void setContestMode(ContestMode contestMode) {
        this.contestMode = contestMode;
    }

    public Location getLocation() {
        return this.location;
    }

    public Contest location(Location location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Team getWinnerTeam() {
        return this.winnerTeam;
    }

    public Contest winnerTeam(Team winnerTeam) {
        this.setWinnerTeam(winnerTeam);
        return this;
    }

    public void setWinnerTeam(Team winnerTeam) {
        this.winnerTeam = winnerTeam;
    }

    public Set<PlayerContest> getContestPlayers() {
        return this.contestPlayers;
    }

    public void setContestPlayers(Set<PlayerContest> playerContests) {
        if (this.contestPlayers != null) {
            this.contestPlayers.forEach(i -> i.setContest(null));
        }
        if (playerContests != null) {
            playerContests.forEach(i -> i.setContest(this));
        }
        this.contestPlayers = playerContests;
    }

    public Contest contestPlayers(Set<PlayerContest> playerContests) {
        this.setContestPlayers(playerContests);
        return this;
    }

    public Contest addContestPlayers(PlayerContest playerContest) {
        this.contestPlayers.add(playerContest);
        playerContest.setContest(this);
        return this;
    }

    public Contest removeContestPlayers(PlayerContest playerContest) {
        this.contestPlayers.remove(playerContest);
        playerContest.setContest(null);
        return this;
    }

    public Set<Game> getGames() {
        return this.games;
    }

    public void setGames(Set<Game> games) {
        if (this.games != null) {
            this.games.forEach(i -> i.setContest(null));
        }
        if (games != null) {
            games.forEach(i -> i.setContest(this));
        }
        this.games = games;
    }

    public Contest games(Set<Game> games) {
        this.setGames(games);
        return this;
    }

    public Contest addGames(Game game) {
        this.games.add(game);
        game.setContest(this);
        return this;
    }

    public Contest removeGames(Game game) {
        this.games.remove(game);
        game.setContest(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contest)) {
            return false;
        }
        return id != null && id.equals(((Contest) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Contest{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", contestMode='" + getContestMode() + "'" +
            ", location='" + getLocation() + "'" +
            ", winnerTeam='" + getWinnerTeam() + "'" +
            "}";
    }
}
