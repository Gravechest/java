import java.util.Scanner;

//gare manier om kleuren te krijgen
class ConsoleColor{
    static final String RESET = "\u001B[0m";
    static final String BLACK = "\u001B[30m";
    static final String RED = "\u001B[31m";
    static final String GREEN = "\u001B[32m";
    static final String YELLOW = "\u001B[33m";
    static final String BLUE = "\u001B[34m";
    static final String PURPLE = "\u001B[35m";
    static final String CYAN = "\u001B[36m";
    static final String WHITE = "\u001B[37m";
}

class Player{
    static final String[] playerColors = {"rood","groen","geel","blauw","zwart","wit"};
    static final String[] playerConsoleColors = {ConsoleColor.RED,ConsoleColor.GREEN,ConsoleColor.YELLOW,ConsoleColor.BLUE,ConsoleColor.BLACK,ConsoleColor.WHITE};

    static int Count;
    static int turn;

    int consoleColorId;
    int playersBehind;
    int pos;

    boolean beurtOverslaan;

    String kleur;
    //beweegt de speler naar de nieuwe positie als er geen speler al op die positie staat
    void movePlayer(int pos){
        for(int i = 0;i < Player.Count;i++){
            if(i != Player.turn && Main.player[i].pos == pos){
                return;
            }
        }
        this.pos = pos;
    }
}
//getallen verwoorden om vakjes een naam te geven
class Vak{
    static final byte NORMAL = 0;
    static final byte HERHAAL = 1;
    static final byte BRUG = 2;
    static final byte HERBERG = 3;
    static final byte PUT = 4;
    static final byte VERDWAALD = 5;
    static final byte GEVANGENIS = 6;
    static final byte DOOD = 7;
    static final byte FINISH = 8;
    static final byte TE_VER = 9;
    static final byte PLAYER = (byte)0x80;
}

public class Main {
    static final int BOARDSZ = 64;
    static byte[] board = new byte[BOARDSZ+12];
    static Player[] player;
    //https://en.wikipedia.org/wiki/Xorshift
    static int throwDice(){
        int t = (int)(System.nanoTime());
        t ^= t >> 3;
        t ^= t << 7;
        t ^= t >> 2;
        return t % 6 + 1;
    }
    static void printBoard(){
        //verwisseld de spelerkleur met het bordid & geeft aan dat er een speler op staat
        for(int i = 0;i < Player.Count;i++){
            if((board[player[i].pos]&Vak.PLAYER)==0) {
                board[player[i].pos]     ^= player[i].consoleColorId;
                player[i].consoleColorId ^= board[player[i].pos];
                board[player[i].pos]     ^= player[i].consoleColorId;
                board[player[i].pos]     |= Vak.PLAYER;
            }
        }
        for(int i = 0;i < BOARDSZ;i+=9) {
            for (int i2 = 0;i2 < 9;i2++) {
                System.out.print('[');
                if ((board[i+i2] & Vak.PLAYER) == 0) {
                    switch (board[i+i2]) {
                    case Vak.HERHAAL:
                        System.out.print('G');
                        break;
                    case Vak.BRUG:
                        System.out.print('B');
                        break;
                    case Vak.HERBERG:
                        System.out.print('H');
                        break;
                    case Vak.PUT:
                        System.out.print('P');
                        break;
                    case Vak.VERDWAALD:
                        System.out.print('D');
                        break;
                    case Vak.GEVANGENIS:
                        System.out.print('#');
                        break;
                    case Vak.DOOD:
                        System.out.print('!');
                        break;
                    default:
                        System.out.print(' ');
                        break;
                    }
                }
                else {
                    System.out.print(Player.playerConsoleColors[board[i+i2] & ~Vak.PLAYER]);
                    System.out.print('@');
                    System.out.print(ConsoleColor.RESET);
                }
                System.out.print(']');
            }
            System.out.print("\n");
        }
        //verwisseld de boel weer terug en untoggle de speler flag
        for(int i = 0;i < Player.Count;i++){
            if((board[player[i].pos]&Vak.PLAYER)==Vak.PLAYER) {
                board[player[i].pos]     &= ~Vak.PLAYER;
                board[player[i].pos]     ^= player[i].consoleColorId;
                player[i].consoleColorId ^= board[player[i].pos];
                board[player[i].pos]     ^= player[i].consoleColorId;
            }
        }
    }
    static void playerTrapped(){
        //de manier om te checken of er al een speler langs de put/gevangenis langs is geweest is om
        //bij te houden hoeveel spelers er achter een speler staan
        for(int i = 0;i < Player.Count;i++){
            if(i != Player.turn && player[i].pos < player[Player.turn].pos){
                player[Player.turn].playersBehind++;
            }
        }
        // als er geen spelers achterstaan dan slaat de speler een beurt over
        if (player[Player.turn].playersBehind == 0) {
            player[Player.turn].beurtOverslaan = true;
        }
    }
    static void playerTurn(int id,int posBuf){
        //hier staan alle speciale vakjes (zie ganzebord spelregels)
        switch(board[posBuf]){
        case Vak.HERHAAL:
            System.out.println("Je gaat 2X zo hard");
            posBuf += posBuf - player[Player.turn].pos;
            playerTurn(id,posBuf);
            break;
        case Vak.BRUG:
            System.out.println("Het bruggetje helpt je naar vakje 12");
            posBuf = 12;
            break;
        case Vak.HERBERG:
            System.out.println("Je overnacht in de herberg, je doet een ronde niet meer mee");
            player[Player.turn].beurtOverslaan = true;
            break;
        case Vak.GEVANGENIS:
            System.out.println("Je bent in de gevangenis gekomen!");
            playerTrapped();
            break;
        case Vak.PUT:
            System.out.println("Je bent in de put gekomen!");
            playerTrapped();
            break;
        case Vak.VERDWAALD:
            System.out.println("Je bent verdwaald, terug naar vakje 37");
            posBuf = 37;
            break;
        case Vak.DOOD:
            System.out.println("Je bent dood, begin maar opnieuw");
            posBuf = 0;
            break;
        case Vak.FINISH:
            System.out.println("het spel is afgelopen, de " + player[Player.turn].kleur + " heeft gewonnen");
            System.exit(0);
            break;
        case Vak.TE_VER:
            posBuf -= (64-posBuf)*2;
            break;
        }
        player[Player.turn].movePlayer(posBuf);
    }
    static void playerTurn(Scanner scan){
        scan.next();
        int diceValue = throwDice() + throwDice();
        System.out.println("je hebt " + diceValue + " gegooit");
        playerTurn(Player.turn,player[Player.turn].pos+diceValue);
    }
    public static void main(String[] args){
        //init het bord
        for(int i = 9;i < BOARDSZ;i+=9){
            board[i]   = Vak.HERHAAL;
            board[i-4] = Vak.HERHAAL;
        }

        board[6]  = Vak.BRUG;
        board[19] = Vak.HERBERG;
        board[31] = Vak.PUT;
        board[42] = Vak.VERDWAALD;
        board[52] = Vak.GEVANGENIS;
        board[58] = Vak.DOOD;

        for(int i = BOARDSZ;i < BOARDSZ+12;i++){
            board[i] = Vak.TE_VER;
        }

        Scanner scan = new Scanner(System.in);
        System.out.println("Welkom bij ganzeboard!");
        System.out.println("hoeveel spelers zijn er?");
        Player.Count = scan.nextInt();
        player = new Player[Player.Count];

        for (int i = 0;i < Player.Count;i++){
            player[i] = new Player();
            System.out.println("Welke kleur?");
            System.out.println("rood  = 0");
            System.out.println("groen = 1");
            System.out.println("geel  = 2");
            System.out.println("blauw = 3");
            System.out.println("zwart = 4");
            System.out.println("wit   = 5");
            int colSel = -1;
            while(colSel<0||colSel>5){
                colSel = scan.nextInt();
            }
            player[i].consoleColorId = colSel;
            player[i].kleur = Player.playerColors[colSel];

        }
        int highestThrow = 0;

        for (int i = 0;i < Player.Count;i++) {
            int diceValue = throwDice();
            System.out.println("speler " + player[i].kleur + " heeft " + diceValue + " gegooit");
            if(diceValue > highestThrow){
                Player.turn = i;
                highestThrow = diceValue;
            }
        }

        System.out.println("speler " + player[Player.turn].kleur + " mag beginnen");
        for(int i = 0;i < Player.Count;i++){
            System.out.println("speler " + player[Player.turn].kleur + " is aan de beurt");
            scan.next();
            int dice1 = throwDice();
            int dice2 = throwDice();

            System.out.println("je hebt " + (dice1 + dice2) + " gegooit");
            //als de speler een specifieke combinatie van 9 gooit dan word de speler hiernaar verplaatst
            //is om te voorkomen dat de speler gelijk finished
            if((dice1==4&&dice2==5)||(dice1==5&&dice2==4)){
                player[Player.turn].pos = 53;
            }
            else if((dice1==3&&dice2==6)||(dice1==6&&dice2==3)){
                player[Player.turn].pos = 26;
            }
            else{
                playerTurn(i,dice1+dice2);
            }
            Player.turn++;
            if(Player.turn == Player.Count){
                Player.turn = 0;
            }
            printBoard();
        }
        for(;;){
            for(int i = 0;i < Player.Count;i++) {
                System.out.println("speler " + player[Player.turn].kleur + " is aan de beurt");
                if(player[Player.turn].beurtOverslaan){
                    player[Player.turn].beurtOverslaan = false;
                }
                else if(player[Player.turn].playersBehind > 0){
                    int playersBehind = 0;
                    for(int i2 = 0;i2 < Player.Count;i2++){
                        if(player[Player.turn].pos > player[i2].pos){
                            playersBehind++;
                        }
                    }
                    if(playersBehind != player[Player.turn].playersBehind){
                        player[Player.turn].playersBehind = 0;
                        playerTurn(scan);
                    }
                }
                else{
                    playerTurn(scan);
                }
                Player.turn++;
                if(Player.turn == Player.Count){
                    Player.turn = 0;
                }
                printBoard();
            }
        }
    }
}





















