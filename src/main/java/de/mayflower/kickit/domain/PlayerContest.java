package de.mayflower.kickit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.mayflower.kickit.domain.enumeration.Team;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A PlayerContest.
 */
@Entity
@Table(name = "player_contest")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlayerContest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "team")
    private Team team;

    @ManyToOne
    @JsonIgnoreProperties(value = { "playedGames", "playedContests" }, allowSetters = true)
    private Player player;

    @ManyToOne
    @JsonIgnoreProperties(value = { "contestPlayers", "games" }, allowSetters = true)
    private Contest contest;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PlayerContest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Team getTeam() {
        return this.team;
    }

    public PlayerContest team(Team team) {
        this.setTeam(team);
        return this;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public PlayerContest player(Player player) {
        this.setPlayer(player);
        return this;
    }

    public Contest getContest() {
        return this.contest;
    }

    public void setContest(Contest contest) {
        this.contest = contest;
    }

    public PlayerContest contest(Contest contest) {
        this.setContest(contest);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlayerContest)) {
            return false;
        }
        return id != null && id.equals(((PlayerContest) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlayerContest{" +
            "id=" + getId() +
            ", team='" + getTeam() + "'" +
            "}";
    }
}
