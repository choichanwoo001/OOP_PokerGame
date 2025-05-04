package Practice_Ex_Code;

import java.util.*;

public class Main1 {
    public static void main(String[] args) {
        PokerGame game = new PokerGame();
        game.start();
    }
}

// * 포커 게임 클래스 - 게임 진행을 관리
class PokerGame {
    private static final int MAX_PLAYERS = 4;
    private static final int NUMBER_OF_ROUNDS = 100;
    private final ArrayList<Player> players;
    private final Dealer dealer;
    private final Scanner scanner;

    public PokerGame() {
        this.players = new ArrayList<>();
        this.dealer = new Dealer();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        registerPlayers();
        playRounds();
        displayResults();
        scanner.close();
    }

    private void registerPlayers() {
        while (true) {
            if (players.size() >= MAX_PLAYERS) {
                System.out.println("최대 플레이어 인원은 " + MAX_PLAYERS + "명입니다. 등록을 종료하고 게임을 시작합니다.");
                break;
            }

            System.out.println("플레이어 등록을 하시려면 1번, 등록을 끝내시려면 2번을 눌러주세요. ");
            System.out.print("입력 : ");
            int option = scanner.nextInt();
            scanner.nextLine(); // 버퍼 비우기

            if (option == 1) {
                registerSinglePlayer();
            } else if(option == 2){
                System.out.println("플레이어 등록을 종료하고 게임을 시작합니다.");
                break;
            } else{
                System.out.println("잘못 입력하셨습니다. 다시 입력해주세요");
            }
        }
    }

    private void registerSinglePlayer() {
        System.out.print("참여할 플레이어 이름을 입력해주세요 : ");
        String nickName = scanner.nextLine();
        
        if (nickName.length() > 20) {
            System.out.println("이름은 20자를 넘을 수 없습니다");
        } else {
            players.add(new Player(nickName));
        }
    }

    private void playRounds() {
        Deck deck = new Deck();
        for (int round = 0; round < NUMBER_OF_ROUNDS; round++) {
            deck.shuffle();
            dealCardsToPlayers(deck);
            dealer.calPoint(players);
            System.out.println();
        }
    }

    private void dealCardsToPlayers(Deck deck) {
        ArrayList<Card> usedCards = new ArrayList<>();
        
        for (Player player : players) {
            Card[] playerCards = new Card[5];
            for (int i = 0; i < 5; i++) {
                Card card = getUniqueCard(deck, usedCards);
                playerCards[i] = card;
                usedCards.add(card);
            }
            player.setCards(playerCards);
        }
    }

    private Card getUniqueCard(Deck deck, List<Card> usedCards) {
        while (true) {
            Card card = deck.pick();
            if (!isCardDuplicate(card, usedCards)) {
                return card;
            }
        }
    }

    private boolean isCardDuplicate(Card card, List<Card> usedCards) {
        for (Card usedCard : usedCards) {
            if (card.getNumber() == usedCard.getNumber() && card.getKind() == usedCard.getKind()) {
                return true;
            }
        }
        return false;
    }

    private void displayResults() {
        players.sort(new PointDescending());
        System.out.println("--------------result--------------");
        for (Player player : players) {
            System.out.println(player);
        }
    }
}

// 딜러 클래스
class Dealer {
    void calPoint(ArrayList<Player> players) {
        PokerHandChecker checker = new PokerHandChecker();
        ArrayList<Integer> onePairIdx = new ArrayList<>();

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            if (checker.isRoyalFlush(player.getCards())) {
                player.setPoints(200000000);
                System.out.println("RoyalFlush");
            } else if (checker.isStraightFlush(player.getCards())) {
                player.setPoints(190000000);
                System.out.println("StraightFlush");
            } else if (checker.isFourOfAKind(player.getCards())) {
                player.setPoints(180000000);
                System.out.println("isFourOfAKind");
            } else if (checker.isFullHouse(player.getCards())) {
                player.setPoints(170000000);
                System.out.println("isFullHouse");
            } else if (checker.isFlush(player.getCards())) {
                player.setPoints(160000000);
                System.out.println("isFlush");
            } else if (checker.isStraight(player.getCards())) {
                player.setPoints(150000000);
                System.out.println("isStraight");
            } else if (checker.isThreeOfAKind(player.getCards())) {
                player.setPoints(140000000);
                System.out.println("isThreeOfAKind");
            } else if (checker.isTwoPair(player.getCards())) {
                player.setPoints(130000000);
                System.out.println("isTwoPair");
            } else if (checker.isOnePair(player.getCards())) {
                player.setPoints(120000000);
                System.out.println("isOnePair");
            } else {
                player.setPoints(checker.getHighCardScore(player.getCards()));
                System.out.println("highCard");
            }
        }
        System.out.println();
        CheckDupleOnePair(players); // 원페어가 여러명일때
        FindWinner(players);
    }

    public void CheckDupleOnePair(ArrayList<Player> players) {
        // 원페어 플레이어들만 찾기
        ArrayList<Player> onePairPlayers = new ArrayList<>();
        for(Player player : players) {
            if(player.getPoints() == 120000000) { // 원페어 점수인 경우
                onePairPlayers.add(player);
            }
        }

        // 원페어 플레이어가 한 명이면 처리할 필요 없음
        if(onePairPlayers.size() <= 1) {
            System.out.println("원페어 플레이어가 없거나 한 명뿐이므로 추가 비교 필요 없음");
            return;
        }

        System.out.println("원페어 플레이어 수: " + onePairPlayers.size() + " - 추가 비교 시작");

        int highestPairValue = 0;
        Map<Player, ArrayList<Integer>> highPairPlayers = new HashMap<>(); // 같은 페어 값을 가진 플레이어들

        // 각 플레이어의 페어 카드와 나머지 카드 찾기 & 가장 높은 페어 찾기
        for(Player player : onePairPlayers) {
            Map<Integer, Integer> cardCount = new HashMap<>();
            for(Card card : player.getCards()) {
                int num = card.getNumber();
                if(num == 1) num = 14; // A는 가장 높은 숫자
                cardCount.put(num, cardCount.getOrDefault(num, 0) + 1);
            }

            // 페어 값과 나머지 카드 찾기
            int pairValue = 0;
            ArrayList<Integer> others = new ArrayList<>();
            for(Integer key : cardCount.keySet()) {
                if(cardCount.get(key) == 2) { // 페어이면
                    pairValue = key;
                } else {
                    others.add(key);
                }
            }
            others.sort(Collections.reverseOrder()); // 내림치순

            System.out.println(player.getNickName() + "의 페어 값: " + pairValue + ", 나머지 카드: " + others);

            // 더 높은 페어를 발견하면 이전 플레이어들 지우기
            if(pairValue > highestPairValue) {
                highestPairValue = pairValue;
                highPairPlayers.clear();
                highPairPlayers.put(player, others);
            }
            // 같은 페어 값이면 나중에 비교를 위해 저장
            else if(pairValue == highestPairValue) {
                highPairPlayers.put(player, others);
            }
        }

        System.out.println("최고 페어 값: " + highestPairValue + ", 동일 페어 플레이어 수: " + highPairPlayers.size());

        // 페어 값이 같은 플레이어가 여러 명이면 나머지 카드 비교
        if(highPairPlayers.size() > 1) {
            ArrayList<Player> sameOnepairPlayer = new ArrayList<>(highPairPlayers.keySet());
            Player finalWinner = sameOnepairPlayer.getFirst();
            ArrayList<Integer> winnerCards = highPairPlayers.get(finalWinner);

            for (int i = 1; i < sameOnepairPlayer.size(); i++) {
                Player currentPlayer = sameOnepairPlayer.get(i);
                ArrayList<Integer> currentCards = highPairPlayers.get(currentPlayer);

                // 페어가 아닌 값들은 내림차순으로 정렬되어 있기에 가장 빠른 순서의 카드의 차이로 판단할 수 있음
                for (int j = 0; j < currentCards.size(); j++) {
                    System.out.println("카드 비교 [" + j + "]: " + currentCards.get(j) + " vs " + winnerCards.get(j));
                    if (currentCards.get(j) > winnerCards.get(j)) {
                        finalWinner = currentPlayer;
                        winnerCards = currentCards;
                        break;
                    } else if (currentCards.get(j) < winnerCards.get(j)) {break;}
                    // 같으면 다음 카드 비교
                }
            }
            finalWinner.setPoints(120000000 + finalWinner.getCards()[0].getNumber()); // 페어 값을 더해 점수 구분
            System.out.println("최종 승자: " + finalWinner.getNickName() + ", 점수: " + finalWinner.getPoints());
        } else if (highPairPlayers.size() == 1) {
            Player winner = highPairPlayers.keySet().iterator().next();
            winner.setPoints(120000000 + highestPairValue);
            System.out.println("단일 원페어 승자: " + winner.getNickName() + ", 점수: " + winner.getPoints());
        }
    }

    public void FindWinner(ArrayList<Player> players){
        int maxIdx = 0;
        int max = players.getFirst().getPoints();
        for (int i = 0; i < players.size(); i++) { // 최대값 찾기
            if (max < players.get(i).getPoints()) {
                max = players.get(i).getPoints();
                maxIdx = i;
            }
            System.out.println(players.get(i).getNickName() + " : " + players.get(i).getPoints());
            players.get(i).setPoints(0); // 다음 포인트 게산을 위한 초기화
        }
        CompensateWinner(players, maxIdx);
    }

    public void CompensateWinner(ArrayList<Player> players, int maxIdx){
        for (int i = 0; i < players.size(); i++) {
            if (i == maxIdx) { // 이기면 100원, 승리 포인트 1
                players.get(i).addWin();
                players.get(i).addMoney(100);
                System.out.println("승자는 " + i + "입니다");
                System.out.println("------------------------------------");
            }
            else // 지면 0원, 패배 포인트 1
                players.get(i).addLoss();
        }
    }
}

// 포커 핸드 체커 클래스
class PokerHandChecker {
    boolean isRoyalFlush(Card[] cards) {
        // 무늬가 다 같고, 연속적이며, 이루어진 카드가 다 royal number일때
        return isFlush(cards) && isStraight(cards) && hasRoyalNumbers(cards);
    }

    boolean hasRoyalNumbers(Card[] cards) {
        int[] royal = {1, 10, 11, 12, 13}; // A, 10, J, Q, K
        ArrayList<Integer> numList = new ArrayList<>();
        for (Card card : cards) {
            numList.add(card.getNumber());
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
            count.put(card.getNumber(), count.getOrDefault(card.getNumber(), 0) + 1);
        }
        return count.containsValue(4);
    }

    boolean isFullHouse(Card[] cards) {
        Map<Integer, Integer> count = new HashMap<>();
        for (Card card : cards) {
            count.put(card.getNumber(), count.getOrDefault(card.getNumber(), 0) + 1);
        }
        return count.containsValue(3) && count.containsValue(2);
    }

    boolean isFlush(Card[] cards) {
        int kind = cards[0].getKind();
        for (int i = 1; i < cards.length; i++) {
            if (cards[i].getKind() != kind) {
                return false;
            }
        }
        return true;
    }

    boolean isStraight(Card[] cards) {
        int[] numbers = new int[cards.length];
        for (int i = 0; i < cards.length; i++) {
            numbers[i] = cards[i].getNumber();
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
            count.put(card.getNumber(), count.getOrDefault(card.getNumber(), 0) + 1);
        }
        return count.containsValue(3) && !isFullHouse(cards);
    }

    boolean isTwoPair(Card[] cards) {
        Map<Integer, Integer> count = new HashMap<>();
        for (Card card : cards) {
            count.put(card.getNumber(), count.getOrDefault(card.getNumber(), 0) + 1);
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
            count.put(card.getNumber(), count.getOrDefault(card.getNumber(), 0) + 1);
        }
        return count.containsValue(2); // pair가 2개인 경우는 상위 else if에 걸러지기 때문에 신경 X
    }

    int getHighCardScore(Card[] cards) {
        int[] numbers = new int[cards.length];
        for (int i = 0; i < cards.length; i++) {
            int num = cards[i].getNumber();
            if (num == 1)
                num = 14; // A는 가장 높은 숫자 취급
            numbers[i] = num;
        }
        Arrays.sort(numbers); // 오름차순 정렬

        return numbers[cards.length-1]; // 가장 큰 숫자
    }
}

// 카드 클래스 - 카드 정보 관리
class Card {
    static final int KIND_MAX = 4;	// 카드 무늬의 수
    static final int NUM_MAX  = 13;	// 무늬별 카드 수

    private final int kind;
    private final int number;

    Card(int kind, int number) {
        this.kind = kind;
        this.number = number;
    }

    public int getKind() {
        return kind;
    }

    public int getNumber() {
        return number;
    }

    public String toString() {
        return String.format("(%-2d, %-2d)", kind, number);
    }
}

// 플레이어 클래스 - 플레이어 정보 관리
class Player {
    private final String nickName;
    private int winGames;
    private int loseGame;
    private int points;
    private int gameMoney;
    private Card[] cards;

    Player(String nickName) {
        this.nickName = nickName;
        this.winGames = 0;
        this.loseGame = 0;
        this.points = 0;
        this.gameMoney = 10000;
        this.cards = new Card[5];
    }

    public String getNickName() {
        return nickName;
    }
    public int getWinGames() {
        return winGames;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void addWin() {
        winGames++;
    }

    public void addLoss() {
        loseGame++;
    }

    public void addMoney(int amount) {
        gameMoney += amount;
    }

    public Card[] getCards() {
        return cards;
    }

    public void setCards(Card[] cards) {
        this.cards = cards;
    }

    @Override
    public String toString() {
        return String.format("%-20s | gameMoney : %-8d | Wins : %-4d | Loses : %-4d", 
        nickName, gameMoney, winGames, loseGame);
    }
}

// 플레이어 랭크 비교기 - 승리 횟수로 정렬
class PointDescending implements Comparator<Player> {
    @Override
    public int compare(Player o1, Player o2) {
        return (o1.getWinGames() - o2.getWinGames()) * -1;
    }
}

// 덱 클래스 - 카드 덱 관리
class Deck {
    private static final int CARD_NUM = 52;	// 카드의 개수
    private final Card[] cardArr;  // Card객체 배열을 포함

    Deck() {	// Deck의 카드를 초기화한다.
        cardArr = new Card[CARD_NUM];
        int i = 0;
        for (int k = Card.KIND_MAX; k > 0; k--)
            for (int n = 0; n < Card.NUM_MAX; n++)
                cardArr[i++] = new Card(k, n+1);
    }

    public Card pick() { // Deck에서 카드 하나를 선택한다.
        int index = (int)(Math.random() * CARD_NUM);
        return cardArr[index];
    }

    public void shuffle() { // 카드의 순서를 섞는다.
        for (int i = 0; i < cardArr.length; i++) {
            int r = (int)(Math.random() * CARD_NUM);

            Card temp = cardArr[i];
            cardArr[i] = cardArr[r];
            cardArr[r] = temp;
        }
    }
}