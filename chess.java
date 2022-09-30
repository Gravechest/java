import java.util.Scanner;

public class Main {
    static char[][] board = {
            {'r','n','b','q','k','b','n','r'},
            {'p','p','p','p','p','p','p','p'},
            {'*','*','*','*','*','*','*','*'},
            {'*','*','*','*','*','*','*','*'},
            {'*','*','*','*','*','*','*','*'},
            {'*','*','*','*','*','*','*','*'},
            {'P','P','P','P','P','P','P','P'},
            {'R','N','B','Q','K','B','N','R'},
    };
    static boolean turn;
    static byte castleRights = 0b00001111;
    public static void printBoard() {
        System.out.print("   ");
        for (int i = 0; i < 8; i++) {
            System.out.print((char) ('A' + i));
            System.out.print(' ');
        }
        System.out.print("\n  ");
        for (int i = 0; i < 16; i++) {
            System.out.print('_');
        }
        System.out.print('\n');
        for (int i = 0; i < 8; i++) {
            System.out.print(i);
            System.out.print("| ");
            for (int i2 = 0; i2 < 8; i2++) {
                System.out.print(board[i][i2]);
                System.out.print(" ");
            }
            System.out.print('\n');
        }
    }
    public static void movePiece(int SX,int SY,int DX,int DY){
        if(board[DX][DY] == '*'){
            board[SX][SY] ^= board[DX][DY];
            board[DX][DY] ^= board[SX][SY];
            board[SX][SY] ^= board[DX][DY];
            printBoard();
        }
        else if(board[DX][DY] >= 'a' && board[DX][DY] <= 'z'){
            board[DX][DY] = board[SX][SY];
            board[SX][SY] = '*';
            printBoard();
        }
    }
    public static void checkDiagonal(int SX,int SY,int DX,int DY){
        if(SX-DX==SY-DY){
            if(SX<DX){
                for(int i = 1;i < (DX-SX)+1;i++){
                    if(board[SX+i][SY+i] != '*'){
                        return;
                    }
                }
            }
            else{
                for(int i = 0;i < (SX-DX);i++){
                    if(board[DX+i][DY+i] != '*'){
                        return;
                    }
                }
            }
            movePiece(SX,SY,DX,DY);
        }
        else if(SX-DX==DY-SY){
            System.out.println('a');
            if(SX<DX){
                for(int i = 1;i < (DX-SX)+1;i++){
                    if(board[SX+i][SY-i] != '*'){
                        return;
                    }
                }
            }
            else{
                for(int i = 0;i < (SX-DX);i++){
                    if(board[DX+i][DY-i] != '*'){
                        return;
                    }
                }
            }
            movePiece(SX,SY,DX,DY);
        }
    }
    public static void checkStraight(int SX,int SY,int DX,int DY){
        if(SX == DX){
            if(SY < DY){
                for(int i = SY+1;i < DY;i++){
                    if(board[SX][i] != '*'){
                        return;
                    }
                }
            }
            else{
                for(int i = DY+1;i < SY;i++){
                    if(board[SX][i] != '*'){
                        return;
                    }
                }
            }
            movePiece(SX,SY,DX,DY);
        }
        else if(SY == DY){
            if(SX < DX){
                for(int i = SX+1;i < DX;i++){
                    if(board[i][SY] != '*'){
                        return;
                    }
                }
            }
            else{
                for(int i = DX+1;i < SX;i++){
                    if(board[i][SY] != '*'){
                        return;
                    }
                }
            }
            movePiece(SX,SY,DX,DY);
        }
    }
    public static void main(String[] args){
        System.out.println("welcome to chessTM");
        printBoard();
        mainLoop:for(;;) {
            Scanner scan = new Scanner(System.in);
            String inp = scan.nextLine();
            int SX = inp.charAt(1) - '0';
            int SY = inp.charAt(0) - 'A';
            int DX = inp.charAt(4) - '0';
            int DY = inp.charAt(3) - 'A';
            switch (board[SX][SY]) {
            case '*':
                break;
            case 'K': {
                int MNX = Math.abs(SX - DX);
                int MNY = Math.abs(SY - DY);
                if (MNX == 1 || MNY == 1) {
                    movePiece(SX, SY, DX, DY);
                    castleRights &= ~0b0011;
                }
                else if(DY == 6 && (castleRights & 0b0001) == 0b0001){
                    for(int i = 5;i < 7;i++){
                        if(board[DX][i] != '*'){
                            continue mainLoop;
                        }
                    }
                    movePiece(SX,SY,DX,DY);
                    movePiece(7,7,7,5);
                }
                else if(DY == 1 && (castleRights & 0b0010) == 0b0010){
                    for(int i = 3;i > 0;i--){
                        if(board[DX][i] != '*'){
                            continue mainLoop;
                        }
                    }
                    movePiece(SX,SY,DX,DY);
                    movePiece(0,0,0,2);

                }
                break;
            }
            case 'Q':
                checkDiagonal(SX,SY,DX,DY);
                checkStraight(SX,SY,DX,DY);
                break;
            case 'N': {
                if(DY == 0){
                    castleRights &= ~0b0010;
                }
                if(DY == 7){
                    castleRights &= ~0b0001;
                }
                int MNX = Math.abs(SX - DX);
                int MNY = Math.abs(SY - DY);
                if ((MNX == 1 && MNY == 2) || (MNX == 2 && MNY == 1)) {
                    movePiece(SX, SY, DX, DY);
                }
                break;
            }
            case 'B':
                checkDiagonal(SX,SY,DX,DY);
                break;
            case 'R':
                checkStraight(SX,SY,DX,DY);
                break;
            case 'P':
                if (SX == DX + 1) {
                    if (SY == DY) {
                        if (board[DX][DY] == '*') {
                            if (DX == 0){
                                System.out.println("promotion");
                                String piece;
                                do{
                                    piece = scan.nextLine();
                                }while(piece.charAt(0)!='Q'&&piece.charAt(0)!='R'&&piece.charAt(0)!='N'&&piece.charAt(0)!='B');
                                board[SX][SY] = piece.charAt(0);
                            }
                            board[SX][SY] ^= board[DX][DY];
                            board[DX][DY] ^= board[SX][SY];
                            board[SX][SY] ^= board[DX][DY];
                            printBoard();
                        }
                    }
                    else if (SY == DY + 1 || SY == DY - 1) {
                        if (board[DX][DY] >= 'a' && board[DX][DY] <= 'z') {
                            if (DX == 0){
                                System.out.println("promotion");
                                String piece;
                                do{
                                    piece = scan.nextLine();
                                }while(piece.charAt(0)!='Q'&&piece.charAt(0)!='R'&&piece.charAt(0)!='N'&&piece.charAt(0)!='B');
                                board[SX][SY] = piece.charAt(0);
                            }
                            board[DX][DY] = board[SX][SY];
                            board[SX][SY] = '*';
                            printBoard();
                        }
                    }
                }
                else if(SX == DX + 2){
                    if(SX == 6){
                        if (board[DX-1][DY] == '*' && board[DX][DY] == '*') {
                            board[SX][SY] ^= board[DX][DY];
                            board[DX][DY] ^= board[SX][SY];
                            board[SX][SY] ^= board[DX][DY];
                            printBoard();
                        }
                    }
                }
                break;
            }
        }
    }
}
