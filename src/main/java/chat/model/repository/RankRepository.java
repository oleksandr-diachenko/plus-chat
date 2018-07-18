package chat.model.repository;

import chat.model.entity.Rank;

/**
 * @author Alexander Diachenko.
 */
public interface RankRepository extends CRUDRepository<Rank>{

    Rank getRankByExp(final int exp);
}
