package lotto;

public enum LottoPrizeMoneyRank {
    FIRST(6, 2000000000), SECOND(5, 30000000), THIRD(5, 1500000),
    FOURTH(4, 50000), FIFTH(3, 5000);

    private final int prizeMoney;
    private final int matchCount;

    LottoPrizeMoneyRank(int matchCount, int prizeMoney) {
        this.prizeMoney = prizeMoney;
        this.matchCount = matchCount;
    }

    public int getPrizeMoney() {
        return this.prizeMoney;
    }

    public int getMatchCount() {
        return this.matchCount;
    }
}
