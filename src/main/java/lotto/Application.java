package lotto;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import camp.nextstep.edu.missionutils.Console;
import camp.nextstep.edu.missionutils.Randoms;

public class Application {
    private static Map<LottoPrizeMoneyRank, Integer> resultMap = new HashMap<LottoPrizeMoneyRank, Integer>();

    public static void main(String[] args) {
        try {
            int buyMoney = enterBuyMoney();
            List<List<Integer>> buyLottoList = buyLotto(buyMoney);
            Lotto lotto = enterLotto();
            int bonusNumber = enterBonusNumber();
            runWinningResult(buyLottoList, lotto, bonusNumber);
            System.out.println(getWinningResult(buyMoney));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 구매금액 입력
     * 
     * @return 구매금액
     * @throws IllegalArgumentException: 입력값이 숫자가 아닌 경우 오류
     * @throws IllegalArgumentException: 입력값이 0이하이거나 1,000 단위가 아닌 경우 오류
     */
    private static int enterBuyMoney() {
        System.out.println("구매금액을 입력해 주세요.");
        int line = 0;

        try {
            line = Integer.parseInt(Console.readLine());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR]숫자타입오류");
        }

        if (line <= 0 || line % LottoConstants.moneyPerLotto != 0) {
            throw new IllegalArgumentException("[ERROR]천단위로 입력하세요.");
        }

        return line;
    }

    /**
     * 구입 금액에 해당하는 만큼 로또를 구입한다.
     */
    private static List<List<Integer>> buyLotto(int money) {
        int buyLottoCnt = money / LottoConstants.moneyPerLotto;
        System.out.println(buyLottoCnt + "개를 구매했습니다.");

        List<List<Integer>> lottoNumbersList = new ArrayList<List<Integer>>();
        for (int i = 0; i < buyLottoCnt; i++) {
            List<Integer> lottoRandomNumbers = Randoms
                    .pickUniqueNumbersInRange(1, 45, 6);
//            lottoRandomNumbers.sort(Comparator.naturalOrder());
            lottoNumbersList.add(lottoRandomNumbers);
            System.out.println(lottoRandomNumbers);
        }
        System.out.println();
        return lottoNumbersList;
    }

    /**
     * 당첨번호 입력
     * 
     * @return Lotto 당첨로또번호
     * @throws IllegalArgumentException 당첨번호 입력개수가 틀린 경우 오류
     * @throws IllegalArgumentException 입력값이 숫자가 아닌 경우 오류
     */
    private static Lotto enterLotto() {
        System.out.println("당첨 번호를 입력해 주세요.");
        List<Integer> list = new ArrayList<Integer>();

        String line = Console.readLine();
        String[] numbers = line.split(",");

        for (int i = 0; i < numbers.length; i++) {
            try {
                list.add(Integer.valueOf(numbers[i].trim()));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(
                        LottoConstants.ErrMsgPrefix + e.getMessage());
            }
        }

        return new Lotto(list);
    }

    /**
     * 보너스번호 입력
     * 
     * @return 보너스번호
     * @throws IllegalArgumentException 입력값이 숫자가 아닌 경우 오류
     */
    private static int enterBonusNumber() {
        System.out.println("보너스 번호를 입력해 주세요.");
        int line = 0;

        try {
            line = Integer.parseInt(Console.readLine());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    LottoConstants.ErrMsgPrefix + e.getMessage());
        }

        return line;
    }

    private static void runWinningResult(List<List<Integer>> buyLottoList,
            Lotto lotto, int bonusNumber) {
        for (List<Integer> buyLotto : buyLottoList) {
            unitWinningResult(buyLotto, lotto, bonusNumber);
        }
    }

    private static void unitWinningResult(List<Integer> buyLotto, Lotto lotto,
            int bonusNumber) {
        List<Integer> lottoNumbers = lotto.getNumbers();
        int matchCount = 0;
        boolean existBounsNumber = false;
        for (Integer number : lottoNumbers) {
            if (buyLotto.contains(number)) {
                matchCount++;
            }
        }

        if (matchCount == 5) {
            existBounsNumber = buyLotto.contains(Integer.valueOf(bonusNumber));
        }

        LottoPrizeMoneyRank rank = null;
        if (matchCount >= 3) {
            rank = getLottoPrizeMoneyRank(matchCount, existBounsNumber);
            addwinningResult(rank);
        }
    }

    private static void addwinningResult(LottoPrizeMoneyRank rank) {
        Integer count = resultMap.get(rank);
        if (count == null) {
            count = 0;
        }
        resultMap.put(rank, count + 1);
    }

    private static String getWinningResult(int buyMoney) {
        StringBuilder sb = new StringBuilder();
        int winningMoney = 0;
        getWinStatHeader(sb);
        LottoPrizeMoneyRank[] rankArray = LottoPrizeMoneyRank.values();
        for (int i = 0; i < rankArray.length; i++) {
            LottoPrizeMoneyRank rank = rankArray[rankArray.length - 1 - i];
            winningMoney += getWinStatBody(sb, rank, resultMap.get(rank));
        }
        getWinStatFooter(sb, buyMoney, winningMoney);

        return sb.toString();
    }

    private static void getWinStatHeader(StringBuilder sb) {
        sb.append("당첨 통계").append("\n").append("---").append("\n");
    }

    private static int getWinStatBody(StringBuilder sb,
            LottoPrizeMoneyRank rank, Integer count) {
        int prizeMoney = 0;
        if (count == null) {
            count = 0;
        }
        if (count != 0) {
            prizeMoney = rank.getPrizeMoney() * count;
        }

        String bonusMessage = "";
        if (LottoPrizeMoneyRank.SECOND.equals(rank)) {
            bonusMessage = ", 보너스 볼 일치";
        }
        sb.append(MessageFormat.format("{0}개 일치{1} ({2}원) - {3}개",
                rank.getMatchCount(), bonusMessage,
                NumberFormat.getInstance().format(rank.getPrizeMoney()),
                count));
        sb.append("\n");

        return prizeMoney;
    }

    private static void getWinStatFooter(StringBuilder sb, int buyMoney,
            int winningMoney) {
        float profitRate = ((float) winningMoney * 100) / (float) buyMoney;
        String sProfitRate = String.format("%.1f", profitRate);
        sb.append(MessageFormat.format("총 수익률은 {0}%입니다.", sProfitRate));
    }

    private static LottoPrizeMoneyRank getLottoPrizeMoneyRank(int matchCount,
            boolean existBounsNumber) {
        if (matchCount == 6) {
            return LottoPrizeMoneyRank.FIRST;
        }
        if (matchCount == 5 && existBounsNumber) {
            return LottoPrizeMoneyRank.SECOND;
        }
        if (matchCount == 5) {
            return LottoPrizeMoneyRank.THIRD;
        }
        if (matchCount == 4) {
            return LottoPrizeMoneyRank.FOURTH;
        }
        if (matchCount == 3) {
            return LottoPrizeMoneyRank.FIFTH;
        }
        return null;
    }
}
