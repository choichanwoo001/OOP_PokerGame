

import java.util.Comparator;

public class Player{
    String nickName;
    int winGames = 0;
    int loseGame = 0;
    int points = 0;
    int gameMoney = 10000;
    Card[] cards = new Card[5];

    Player(String nickName){
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return nickName + " | gameMoney : " + gameMoney + " | Wins : "
                + winGames + " | Loses : " + loseGame;
    }
}

class PointDescending implements Comparator<Player> {
    public int compare(Player o1, Player o2){
        return (o1.winGames - o2.winGames)*-1;
    }
}
