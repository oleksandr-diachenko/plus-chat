package model.repository;

import model.entity.Rank;

import java.util.Set;

/**
 * @author Alexander Diachenko.
 */
public interface RankRepository {

    Set<Rank> getRanks();

    Rank getRankByName(String name);

    Rank getRankById(int id);
}
