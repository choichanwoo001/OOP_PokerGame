

public class Card {
    static final int KIND_MAX = 4;	// 카드 무늬의 수
    static final int NUM_MAX  = 13;	// 무늬별 카드 수

    int kind;
    int number;

    Card(int kind, int number) {
        this.kind = kind;
        this.number = number;
    }
}
