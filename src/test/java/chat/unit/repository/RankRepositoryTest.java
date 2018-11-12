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
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class RankRepositoryTest {

    @Autowired
    private RankRepository rankRepository;

    @Test
    public void getAllRanksTest() {
        final Set<Rank> ranks = this.rankRepository.getAll();
        assertTrue(!ranks.isEmpty());
    }

    @Test
    public void getRankByExpProTest() {
        final Rank rankByExp = this.rankRepository.getRankByExp(10);
        assertEquals("Pro", rankByExp.getName());
    }

    @Test
    public void getRankByExpNoobTest() {
        final Rank rankByExp = this.rankRepository.getRankByExp(9);
        assertEquals("Noob", rankByExp.getName());
    }

    @Test
    public void getRankByNegativeExpTest() {
        final Rank rankByExp = this.rankRepository.getRankByExp(-1);
        assertNull(rankByExp.getName());
    }

    @Test
    public void isNewRankExpTest() {
        boolean newRank = this.rankRepository.isNewRank(10);
        assertTrue(newRank);
    }

    @Test
    public void isNotNewRankExpTest() {
        boolean newRank = this.rankRepository.isNewRank(14);
        assertFalse(newRank);
    }
}
