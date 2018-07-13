package twitch;

import chat.model.entity.Rank;
import chat.model.repository.JSONRankRepository;
import chat.model.repository.RankRepository;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Alexander Diachenko.
 */
public class RankRepositoryTest {

    private RankRepository rankRepository = new JSONRankRepository(getResource("/json/ranks.json"));

    @Test
    public void getAllRanksTest() {
        final Set<Rank> ranks = rankRepository.getRanks();
        assertTrue(!ranks.isEmpty());
    }

    @Test
    public void getRankByExpTest() {
        final Rank rankByExp = rankRepository.getRankByExp(10);
        assertEquals("Pro", rankByExp.getName());
    }

    @Test
    public void getRankByNegativeExpTest() {
        final Rank rankByExp = rankRepository.getRankByExp(-1);
        assertNull(rankByExp.getName());
    }

    private String getResource(String path) {
        return getClass().getResource(path).getPath();
    }
}
