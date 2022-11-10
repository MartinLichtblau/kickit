package de.mayflower.kickit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.mayflower.kickit.domain.enumeration.Location;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Player.
 */
@Entity
@Table(name = "player")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "location", nullable = false)
    private Location location;

    @OneToMany(mappedBy = "player")
    @JsonIgnoreProperties(value = { "player", "game" }, allowSetters = true)
    private Set<PlayerGame> playedGames = new HashSet<>();

    @OneToMany(mappedBy = "player")
    @JsonIgnoreProperties(value = { "player", "contest" }, allowSetters = true)
    private Set<PlayerContest> playedContests = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Player id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Player name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return this.location;
    }

    public Player location(Location location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Set<PlayerGame> getPlayedGames() {
        return this.playedGames;
    }

    public void setPlayedGames(Set<PlayerGame> playerGames) {
        if (this.playedGames != null) {
            this.playedGames.forEach(i -> i.setPlayer(null));
        }
        if (playerGames != null) {
            playerGames.forEach(i -> i.setPlayer(this));
        }
        this.playedGames = playerGames;
    }

    public Player playedGames(Set<PlayerGame> playerGames) {
        this.setPlayedGames(playerGames);
        return this;
    }

    public Player addPlayedGames(PlayerGame playerGame) {
        this.playedGames.add(playerGame);
        playerGame.setPlayer(this);
        return this;
    }

    public Player removePlayedGames(PlayerGame playerGame) {
        this.playedGames.remove(playerGame);
        playerGame.setPlayer(null);
        return this;
    }

    public Set<PlayerContest> getPlayedContests() {
        return this.playedContests;
    }

    public void setPlayedContests(Set<PlayerContest> playerContests) {
        if (this.playedContests != null) {
            this.playedContests.forEach(i -> i.setPlayer(null));
        }
        if (playerContests != null) {
            playerContests.forEach(i -> i.setPlayer(this));
        }
        this.playedContests = playerContests;
    }

    public Player playedContests(Set<PlayerContest> playerContests) {
        this.setPlayedContests(playerContests);
        return this;
    }

    public Player addPlayedContests(PlayerContest playerContest) {
        this.playedContests.add(playerContest);
        playerContest.setPlayer(this);
        return this;
    }

    public Player removePlayedContests(PlayerContest playerContest) {
        this.playedContests.remove(playerContest);
        playerContest.setPlayer(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Player)) {
            return false;
        }
        return id != null && id.equals(((Player) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Player{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", location='" + getLocation() + "'" +
            "}";
    }
}
