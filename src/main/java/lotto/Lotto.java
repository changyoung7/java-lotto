package lotto;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Lotto {
    private final List<Integer> numbers;

    public Lotto(List<Integer> numbers) {
        validate(numbers);
        this.numbers = numbers;
    }

    private void validate(List<Integer> numbers) {
        if (numbers.size() != LottoConstants.countInputLotto) {
            throw new IllegalArgumentException(
                    LottoConstants.ErrMsgPrefix + MessageFormat.format(
                            "당첨번호 입력개수가 올바르지 않습니다.({0})", numbers.size()));
        }

        Set<Integer> set = new HashSet<Integer>(numbers);
        if (set.size() != LottoConstants.countInputLotto) {
            throw new IllegalArgumentException(LottoConstants.ErrMsgPrefix
                    + MessageFormat.format("중복된 값이 입력되었습니다.({0})", numbers));
        }
    }

    public List<Integer> getNumbers() {
        return numbers;
    }

    @Override
    public String toString() {
        return this.numbers.toString();
    }
}
