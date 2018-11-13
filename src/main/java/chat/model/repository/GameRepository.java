package chat.model.repository;

import chat.model.entity.Game;

import java.util.Optional;

/**
 * @author Alexander Diachenko.
 */
public interface GameRepository extends CRUDRepository<Game> {

    Optional<Game> getGameByName(String name);
}
