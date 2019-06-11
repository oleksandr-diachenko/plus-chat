package chat.factory;

import chat.model.entity.Rank;

import java.util.Optional;

/**
 * @author Oleksandr_Diachenko
 */
public class RankFactory extends AbstractFactory<Rank> {

    @Override
    public Optional<Rank> create() {
        Rank rank = new Rank();
        rank.setName("Pro");
        rank.setId(1);
        rank.setExp(10);
        rank.setImagePath("");
        return Optional.of(rank);
    }
}
