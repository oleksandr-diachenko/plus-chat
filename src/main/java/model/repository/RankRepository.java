package model.repository;

import model.entity.Rank;

import java.util.Set;

/**
 * @author Alexander Diachenko.
 */
public interface RankRepository {

    Set<Rank> getRanks();

    Rank getRankByExp(int exp);
}
