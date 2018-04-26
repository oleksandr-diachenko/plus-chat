package model;

/**
 * @author Alexander Diachenko.
 */
public enum Rank {

    ROOKIE(0, 10),
    SQUADDIE(1, 20),
    CORPORAL(2, 30),
    SERGEANT(3, 40),
    LIEUTENANT(4, 50),
    CAPTAIN(5, 60),
    MAJOR(6, 70),
    COLONEL(7,80);

    private int rank;
    private int exp;

    Rank(int rank, int exp) {
        this.rank = rank;
        this.exp = exp;
    }

    public int getRank() {
        return rank;
    }

    public int getExp() {
        return exp;
    }
}
