package chat.unit.repository;

import chat.model.entity.Rank;
import chat.model.repository.RankRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author Alexander Diachenko.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:testApplicationContext.xml"})
public class RankRepositoryTest {

    @Autowired
    private RankRepository rankRepository;

    @Test
    public void shouldReturnAllRanks() {
        Set<Rank> ranks = rankRepository.getAll();
        assertFalse(ranks.isEmpty());
    }

    @Test
    public void shouldReturnProRankWhenRequestedRanksExpTen() {
        Rank rankByExp = rankRepository.getRankByExp(10);
        assertEquals("Pro", rankByExp.getName());
    }

    @Test
    public void shouldReturnNoobRankWhenRequestedRanksExpNine() {
        Rank rankByExp = rankRepository.getRankByExp(9);
        assertEquals("Noob", rankByExp.getName());
    }

    @Test
    public void shouldReturnNullWhenRequestedRanksExpNegative() {
        Rank rankByExp = rankRepository.getRankByExp(-1);
        assertNull(rankByExp.getName());
    }

    @Test
    public void shouldNewRankWhenRequestedRanksExpTen() {
        boolean newRank = rankRepository.isNewRank(10);
        assertTrue(newRank);
    }

    @Test
    public void shouldNotNewRankWhenRequestedRanksExpFourteen() {
        boolean newRank = rankRepository.isNewRank(14);
        assertFalse(newRank);
    }
}
