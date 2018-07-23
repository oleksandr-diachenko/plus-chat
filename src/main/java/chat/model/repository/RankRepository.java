package chat.model.repository;

import chat.model.entity.Rank;

import java.util.Optional;

/**
 * @author Alexander Diachenko.
 */
public interface RankRepository extends CRUDRepository<Rank>{

    Optional<Rank> getByName(final String name);

    Rank getRankByExp(final long exp);
}
