package chat.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;


/**
 * @author Alexander Diachenko.
 */
@Getter
@Setter
public class Game {

    private String name;
    private String platform;
    private String genre;
    private String description;
    private String release;
    private String startedDate;
    private String passedDate;
    private List<String> records;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(name, game.name) &&
                Objects.equals(platform, game.platform) &&
                Objects.equals(genre, game.genre) &&
                Objects.equals(release, game.release);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, platform, genre, release);
    }

    @Override
    public String toString() {
        return "Game{" +
                "name='" + name + '\'' +
                ", platform='" + platform + '\'' +
                ", genre='" + genre + '\'' +
                ", description='" + description + '\'' +
                ", release='" + release + '\'' +
                ", startedDate='" + startedDate + '\'' +
                ", passedDate='" + passedDate + '\'' +
                '}';
    }
}
