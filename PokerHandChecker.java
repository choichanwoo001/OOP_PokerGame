

import java.util.*;

public class PokerHandChecker {
    boolean isRoyalFlush(Card[] cards) {
        // 무늬가 다 같고, 연속적이며, 이루어진 카드가 다 royal number일때
        return isFlush(cards) && isStraight(cards) && hasRoyalNumbers(cards);
    }

    boolean hasRoyalNumbers(Card[] cards) {
        int[] royal = {1, 10, 11, 12, 13}; // A, 10, J, Q, K
        List<Integer> numList = new ArrayList<>();
        for (Card card : cards) {
            numList.add(card.number);
        }
        for (int r : royal) {
            if (!numList.contains(r)) {
                return false;
            }
        }
        return true;
    }

    boolean isStraightFlush(Card[] cards) {
        // 무늬가 다 같고, 연속적이며, 이루어진 카드중 하나라도 royal number가 아닐때
        return isFlush(cards) && isStraight(cards) && !hasRoyalNumbers(cards);
    }

    boolean isFourOfAKind(Card[] cards) {
        Map<Integer, Integer> count = new HashMap<>();
        for (Card card : cards) {
            count.put(card.number, count.getOrDefault(card.number, 0) + 1);
        }
        return count.containsValue(4);
    }

    boolean isFullHouse(Card[] cards) {
        Map<Integer, Integer> count = new HashMap<>();
        for (Card card : cards) {
            count.put(card.number, count.getOrDefault(card.number, 0) + 1);
        }
        return count.containsValue(3) && count.containsValue(2);
    }

    boolean isFlush(Card[] cards) {
        int kind = cards[0].kind;
        for (int i = 1; i < cards.length; i++) {
            if (cards[i].kind != kind) {
                return false;
            }
        }
        return true;
    }

    boolean isStraight(Card[] cards) {
        int[] numbers = new int[cards.length];
        for (int i = 0; i < cards.length; i++) {
            numbers[i] = cards[i].number;
        }
        Arrays.sort(numbers);

        // lowAce는 여기 포함임
        boolean normalStraight = true;
        for (int i = 0; i < numbers.length - 1; i++) {
            if (numbers[i] + 1 != numbers[i+1]) {
                normalStraight = false;
                break;
            }
        }

        // 10, 11, 12, 13, A(14) (하이 스트레이트) 체크
        boolean HighAceStraight = (numbers[0] == 1 && numbers[1] == 10 && numbers[2] == 11 && numbers[3] == 12 && numbers[4] == 13);

        return normalStraight || HighAceStraight;
    }

    boolean isThreeOfAKind(Card[] cards) {
        Map<Integer, Integer> count = new HashMap<>();
        for (Card card : cards) {
            count.put(card.number, count.getOrDefault(card.number, 0) + 1);
        }
        return count.containsValue(3) && !isFullHouse(cards);
    }

    boolean isTwoPair(Card[] cards) {
        Map<Integer, Integer> count = new HashMap<>();
        for (Card card : cards) {
            count.put(card.number, count.getOrDefault(card.number, 0) + 1);
        }

        int pairs = 0;
        for (int c : count.values()) {
            if (c == 2) pairs++;
        }
        return pairs == 2;
    }

    boolean isOnePair(Card[] cards) {
        Map<Integer, Integer> count = new HashMap<>();
        for (Card card : cards) {
            // 카드 넘버의 키가 존재하면 그 수에 +1, 없으면 초기값 0
            count.put(card.number, count.getOrDefault(card.number, 0) + 1);
        }
        return count.containsValue(2); // pair가 2개인 경우는 상위 else if에 걸러지기 때문에 신경 X
    }

    int getHighCardScore(Card[] cards) {
        int[] numbers = new int[cards.length];
        for (int i = 0; i < cards.length; i++) {
            int num = cards[i].number;
            if (num == 1)
                num = 14; // A는 가장 높은 숫자 취급
            numbers[i] = num;
        }
        Arrays.sort(numbers); // 오름차순 정렬

        int score = 0;
        int multiplier = 100000; // 가장 큰 숫자에 가장 높은 가중치를 주면 합을 비교해도 상관없음
        for (int i = numbers.length - 1; i >= 0; i--) { // 큰 수부터
            score += numbers[i] * multiplier;
            multiplier /= 10;
        }
        return score;
    }
}
