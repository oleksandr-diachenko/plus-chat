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
        return Objects.equals(this.name, game.name) &&
                Objects.equals(this.platform, game.platform) &&
                Objects.equals(this.genre, game.genre) &&
                Objects.equals(this.release, game.release);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.platform, this.genre, this.release);
    }

    @Override
    public String toString() {
        return "Game{" +
                "name='" + this.name + '\'' +
                ", platform='" + this.platform + '\'' +
                ", genre='" + this.genre + '\'' +
                ", description='" + this.description + '\'' +
                ", release='" + this.release + '\'' +
                ", startedDate='" + this.startedDate + '\'' +
                ", passedDate='" + this.passedDate + '\'' +
                '}';
    }
}
