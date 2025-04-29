

import java.util.ArrayList;


class Dealer{
    PokerHandChecker checker = new PokerHandChecker();
    void checkWinner(ArrayList<Player> players) {
        for (int i = 0; i < players.size(); i++) {
            if (checker.isRoyalFlush(players.get(i).cards)) {
                players.get(i).points = 11000000;
            } else if (checker.isRoyalFlush(players.get(i).cards)) {
                players.get(i).points = 10900000;
            } else if (checker.isStraightFlush(players.get(i).cards)) {
                players.get(i).points = 10800000;
            } else if (checker.isFourOfAKind(players.get(i).cards)) {
                players.get(i).points = 10700000;
            } else if (checker.isFullHouse(players.get(i).cards)) {
                players.get(i).points = 10600000;
            } else if (checker.isFlush(players.get(i).cards)) {
                players.get(i).points = 10500000;
            } else if (checker.isStraight(players.get(i).cards)) {
                players.get(i).points = 10400000;
            } else if (checker.isThreeOfAKind(players.get(i).cards)) {
                players.get(i).points = 10300000;
            } else if (checker.isTwoPair(players.get(i).cards)) {
                players.get(i).points = 10200000;
            } else if (checker.isOnePair(players.get(i).cards)) {
                players.get(i).points = 10100000;
            } else // highCard 최고는 1400000
                players.get(i).points = checker.getHighCardScore(players.get(i).cards);
        }

        int maxIdx = 0; int max = players.get(0).points;
        for (int i = 0; i < players.size(); i++) { // 최대값 찾기
            if(max < players.get(i).points){
                max = players.get(i).points;
                players.get(i).points = 0; // 다음 포인트 게산을 위한 초기화
                maxIdx = i;
            }
        }

        for (int i = 0; i < players.size(); i++) {
            if(i == maxIdx){ // 이기면 100원, 승리 포인트 1
                players.get(i).winGames += 1;
                players.get(i).gameMoney += 100;
            }
            else // 지면 0원, 패배 포인트 1
                players.get(i).loseGame += 1;
        }
    }
}
