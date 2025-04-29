

import java.util.*;

public class DeckTest {
    public static void main(String[] args) {
        ArrayList<Player> players = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        Dealer dealer = new Dealer();
        Deck d = new Deck(); // 카드 한 벌(Deck)을 만든다.

        while (true) { // 플레이어 등록
            if (players.size() >= 4) {
                System.out.println("최대 플레이어 인원은 4명입니다. 등록을 종료하고 게임을 시작합니다.");
                break;
            }

            System.out.println("플레이어 등록을 하시려면 1번, 등록을 끝내시려면 2번을 눌러주세요. ");
            System.out.print("입력 : ");
            int option = sc.nextInt();
            sc.nextLine();
            if (option == 1) {
                System.out.print("참여할 플레이어 이름을 입력해주세요 : ");
                String nickName = sc.nextLine();
                if (nickName.length() > 20) {
                    System.out.print("이름은 20자를 넘을 수 없습니다");
                    continue;
                } else {
                    players.add(new Player(nickName));
                }
            } else {
                System.out.println("플레이어 등록을 종료하고 게임을 시작합니다.");
                break;
            }
        }

        Card[] usedCard = new Card[players.size()*5]; // 사용된 카드

        for (int k = 0; k < 100; k++) {
            d.shuffle(); // 매번 셔플
            int usedIdx = 0; // 사용한 카드 초기화
            for (int i = 0; i < players.size(); i++) {
                for (int j = 0; j < 5; j++) { // 각 플레이어마다 5장씩 뽑기
                    usedCard[usedIdx] = d.pick();
                    if(checkDuple(usedCard, usedIdx)) { // 중복이면 다시 뽑기
                        j--;
                    }
                    else { // 중복 아니면 저장
                        players.get(i).cards[j] = usedCard[usedIdx];
                        usedIdx++;
                    }
                }
            }
            dealer.checkWinner(players);
        }

        players.sort(new PointDescending());
        Iterator<Player> it = players.iterator();
        System.out.println("--------------result--------------");
        while (it.hasNext())
            System.out.println(it.next());
    }

    static boolean checkDuple(Card[] cards, int idx){
        for(int i = 0; i<idx; i++){
            if(cards[i].number == cards[idx].number &&
            cards[i].kind == cards[idx].kind)
                return true;
        }
        return false;
    }
}