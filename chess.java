import java.util.Scanner;

class CHESSMOVE{
    public byte src;
    public byte dst;
}

public class Main {


    final static byte DEPTH = 1;
    static CHESSMOVE[] movs = new CHESSMOVE[100];
    static char[] board = {
        'r','n','b','q','k','b','n','r',
        'p','p','p','p','p','p','p','p',
        '*','*','*','*','*','*','*','*',
        '*','*','*','*','*','*','*','*',
        '*','*','*','*','*','*','*','*',
        '*','*','*','*','*','*','*','*',
        'P','P','P','P','P','P','P','P',
        'R','N','B','Q','K','B','N','R',
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
                System.out.print(board[i*8+i2]);
                System.out.print(" ");
            }
            System.out.print('\n');
        }
    }
    static byte blackPcsToScore(char piece){
        switch(piece){
            case 'p':
                return -1;
            case 'b','n':
                return -3;
            case 'r':
                return -5;
            case 'q':
                return -9;
            case 'k':
                return -50;
            default:
                return 0;
        }
    }
    static byte whitePcsToScore(char piece){
        switch(piece){
            case 'P':
                return 1;
            case 'B','N':
                return 3;
            case 'R':
                return 5;
            case 'Q':
                return 9;
            case 'K':
                return 50;
            default:
                return 0;
        }
    }
    static void movePiece(int SX,int SY,int DX,int DY){
        board[SX*8+SY] ^= board[DX*8+DY];
        board[DX*8+DY] ^= board[SX*8+SY];
        board[SX*8+SY] ^= board[DX*8+DY];
    }

    static void capturePiece(int SX,int SY,int DX,int DY){
        board[DX*8+DY] = board[SX*8+SY];
        board[SX*8+SY] = '*';
    }
    static void movePieceD(byte src,byte dst){
        board[src] ^= board[dst];
        board[dst] ^= board[src];
        board[src] ^= board[dst];
    }

    static void capturePieceD(byte src,byte dst){
        board[dst] = board[src];
        board[src] = '*';
    }
    static void movePieceWhite(int SX,int SY,int DX,int DY){
        if(board[DX*8+DY] == '*'){
            movePiece(SX,SY,DX,DY);
        }
        else if(board[DX*8+DY] >= 'a' && board[DX*8+DY] <= 'z'){
            capturePiece(SX,SY,DX,DY);
        }
    }

    static void movePieceBlack(int SX,int SY,int DX,int DY){
        if(board[DX*8+DY] == '*'){
            movePiece(SX,SY,DX,DY);
        }
        else if(board[DX*8+DY] >= 'A' && board[DX*8+DY] <= 'Z'){
            capturePiece(SX,SY,DX,DY);
        }
    }
    public static byte maxAIdiagonal(byte i,byte depth,byte pts){
        if(i > 7){
            if((i & 7) != 0){
                if(depth==0){
                    for(int i2 = i - 9;board[i2] < 'a' || board[i2] > 'z';i2-=9){
                        if(board[i2] > 'A' && board[i2] < 'Z'){
                            pts = (byte)Math.max(pts,whitePcsToScore(board[i2]));
                            break;
                        }
                        if(i2 < 8 || (i2 & 7) == 0){
                            break;
                        }
                    }
                }
                else{
                    for(int i2 = i - 9;board[i2] < 'a' || board[i2] > 'z';i2-=9){
                        if(board[i2] > 'A' && board[i2] < 'Z'){
                            byte lpts = whitePcsToScore(board[i2]);
                            char tpcs = board[i2];
                            capturePieceD(i,(byte)i2);
                            pts = (byte)Math.max(pts,minAI((byte)(depth-1)) + lpts);
                            board[i] = board[i2];
                            board[i2] = tpcs;
                            break;
                        }
                        else{
                            movePieceD(i,(byte)i2);
                            pts = (byte)Math.max(pts,minAI((byte)(depth-1)));
                            movePieceD((byte)i2,i);
                        }
                        if(i2 < 8 || (i2 & 7) == 0){
                            break;
                        }
                    }
                }
            }
            if((i & 7) != 7){
                if(depth==0){
                    for(int i2 = i - 7;board[i2] < 'a' || board[i2] > 'z';i2-=7){
                        if(board[i2] > 'A' && board[i2] < 'Z'){
                            pts = (byte)Math.max(pts,whitePcsToScore(board[i2]));
                            break;
                        }
                        if(i2 < 8 || (i2 & 7) == 7){
                            break;
                        }
                    }
                }
                else{
                    for(int i2 = i - 7;board[i2] < 'a' || board[i2] > 'z';i2-=7){
                        if(board[i2] > 'A' && board[i2] < 'Z'){
                            byte lpts = whitePcsToScore(board[i2]);
                            char tpcs = board[i2];
                            capturePieceD(i,(byte)i2);
                            pts = (byte)Math.max(pts,minAI((byte)(depth-1)) + lpts);
                            board[i] = board[i2];
                            board[i2] = tpcs;
                            break;
                        }
                        else{
                            movePieceD(i,(byte)i2);
                            pts = (byte)Math.max(pts,minAI((byte)(depth-1)));
                            movePieceD((byte)i2,i);
                        }
                        if(i2 < 8 || (i2 & 7) == 7){
                            break;
                        }
                    }
                }
            }
        }
        if(i < 55){
            if((i & 7) != 7){
                if(depth==0){
                    for(int i2 = i + 9;board[i2] < 'a' || board[i2] > 'z';i2+=9){
                        if(board[i2] > 'A' && board[i2] < 'Z'){
                            pts = (byte)Math.max(pts,whitePcsToScore(board[i2]));
                            break;
                        }
                        if(i2 > 55 || (i2 & 7) == 7){
                            break;
                        }
                    }
                }
                else{
                    for(int i2 = i + 9;board[i2] < 'a' || board[i2] > 'z';i2+=9){
                        if(board[i2] > 'A' && board[i2] < 'Z'){
                            byte lpts = whitePcsToScore(board[i2]);
                            char tpcs = board[i2];
                            capturePieceD(i,(byte)i2);
                            pts = (byte)Math.max(pts,minAI((byte)(depth-1)) + lpts);
                            board[i] = board[i2];
                            board[i2] = tpcs;
                            break;
                        }
                        else{
                            movePieceD(i,(byte)i2);
                            pts = (byte)Math.max(pts,minAI((byte)(depth-1)));
                            movePieceD((byte)i2,i);
                        }
                        if(i2 > 55 || (i2 & 7) == 7){
                            break;
                        }
                    }
                }
            }
            if((i & 7) != 0){
                if(depth==0){
                    for(int i2 = i + 7;board[i2] < 'a' || board[i2] > 'z';i2+=7){
                        if(board[i2] > 'A' && board[i2] < 'Z'){
                            pts = (byte)Math.max(pts,whitePcsToScore(board[i2]));
                            break;
                        }
                        if(i2 > 55 || (i2 & 7) == 0){
                            break;
                        }
                    }
                }
                else{
                    for(int i2 = i + 7;board[i2] < 'a' || board[i2] > 'z';i2+=7){
                        if(board[i2] > 'A' && board[i2] < 'Z'){
                            byte lpts = whitePcsToScore(board[i2]);
                            char tpcs = board[i2];
                            capturePieceD(i,(byte)i2);
                            pts = (byte)Math.max(pts,minAI((byte)(depth-1)) + lpts);
                            board[i] = board[i2];
                            board[i2] = tpcs;
                            break;
                        }
                        else{
                            movePieceD(i,(byte)i2);
                            pts = (byte)Math.max(pts,minAI((byte)(depth-1)));
                            movePieceD((byte)i2,i);
                        }
                        if(i2 > 55 || (i2 & 7) == 0){
                            break;
                        }
                    }
                }
            }
        }
        return pts;
    }
    static byte maxAIstraight(byte i,byte depth,byte pts){
        if((i & 7) != 7){
            if(depth==0){
                for(int i2 = i + 1;board[i2] < 'a' || board[i2] > 'z';i2++){
                    if(board[i2] > 'A' && board[i2] < 'Z'){
                        pts = (byte)Math.max(pts,whitePcsToScore(board[i2]));
                        break;
                    }
                    if((i2 & 7) == 7){
                        break;
                    }
                }
            }
            else{
                for(int i2 = i + 1;board[i2] < 'a' || board[i2] > 'z';i2++){
                    if(board[i2] > 'A' && board[i2] < 'Z'){
                        byte lpts = whitePcsToScore(board[i2]);
                        char tpcs = board[i2];
                        capturePieceD(i,(byte)i2);
                        pts = (byte)Math.max(pts,minAI((byte)(depth-1)) + lpts);
                        board[i] = board[i2];
                        board[i2] = tpcs;
                        break;
                    }
                    else{
                        movePieceD(i,(byte)i2);
                        pts = (byte)Math.max(pts,minAI((byte)(depth-1)));
                        movePieceD((byte)i2,i);
                    }
                    if((i2 & 7) == 7){
                        break;
                    }
                }
            }
        }
        if((i & 7) != 0){
            if(depth==0){
                for(int i2 = i - 1;board[i2] < 'a' || board[i2] > 'z';i2--){
                    if(board[i2] > 'A' && board[i2] < 'Z'){
                        pts = (byte)Math.max(pts,whitePcsToScore(board[i2]));
                        break;
                    }
                    if((i2 & 7) == 0){
                        break;
                    }
                }
            }
            else{
                for(int i2 = i - 1;board[i2] < 'a' || board[i2] > 'z';i2--){
                    if(board[i2] > 'A' && board[i2] < 'Z'){
                        byte lpts = whitePcsToScore(board[i2]);
                        char tpcs = board[i2];
                        capturePieceD(i,(byte)i2);
                        pts = (byte)Math.max(pts,minAI((byte)(depth-1)) + lpts);
                        board[i] = board[i2];
                        board[i2] = tpcs;
                        break;
                    }
                    else{
                        movePieceD(i,(byte)i2);
                        pts = (byte)Math.max(pts,minAI((byte)(depth-1)));
                        movePieceD((byte)i2,i);
                    }
                    if((i2 & 7) == 0){
                        break;
                    }
                }
            }
        }
        if(i > 7){
            if(depth==0){
                for(int i2 = i - 8;board[i2] < 'a' || board[i2] > 'z';i2-=8){
                    if(board[i2] > 'A' && board[i2] < 'Z'){
                        pts = (byte)Math.max(pts,whitePcsToScore(board[i2]));
                        break;
                    }
                    if(i2 < 8){
                        break;
                    }
                }
            }
            else{
                for(int i2 = i - 8;board[i2] < 'a' || board[i2] > 'z';i2-=8){
                    if(board[i2] > 'A' && board[i2] < 'Z'){
                        byte lpts = whitePcsToScore(board[i2]);
                        char tpcs = board[i2];
                        capturePieceD(i,(byte)i2);
                        pts = (byte)Math.max(pts,minAI((byte)(depth-1)) + lpts);
                        board[i] = board[i2];
                        board[i2] = tpcs;
                        break;
                    }
                    else{
                        movePieceD(i,(byte)i2);
                        pts = (byte)Math.max(pts,minAI((byte)(depth-1)));
                        movePieceD((byte)i2,i);
                    }
                    if(i2 < 8){
                        break;
                    }
                }
            }
        }
        if(i < 55){
            if(depth==0){
                for(int i2 = i + 8;board[i2] < 'a' || board[i2] > 'z';i2+=8){
                    if(board[i2] > 'A' && board[i2] < 'Z'){
                        pts = (byte)Math.max(pts,whitePcsToScore(board[i2]));
                        break;
                    }
                    if(i2 > 55){
                        break;
                    }
                }
            }
            else{
                for(int i2 = i + 8;board[i2] < 'a' || board[i2] > 'z';i2+=8){
                    if(board[i2] > 'A' && board[i2] < 'Z'){
                        byte lpts = whitePcsToScore(board[i2]);
                        char tpcs = board[i2];
                        capturePieceD(i,(byte)i2);
                        pts = (byte)Math.max(pts,minAI((byte)(depth-1)) + lpts);
                        board[i] = board[i2];
                        board[i2] = tpcs;
                        break;
                    }
                    else{
                        movePieceD(i,(byte)i2);
                        pts = (byte)Math.max(pts,minAI((byte)(depth-1)));
                        movePieceD((byte)i2,i);
                    }
                    if(i2 > 55){
                        break;
                    }
                }
            }
        }
        return pts;
    }
    static byte maxAI(byte depth){
        byte pts = -128;
        for(int i = 0;i < 64;i++){
            switch(board[i]){
                case 'p':
                    if(depth==0){
                        if(board[i+9] > 'a' && board[i+9] < 'z' && (i & 7) != 0){
                            pts = (byte)Math.max(pts,whitePcsToScore(board[i+9]));
                        }
                        if(board[i+7] > 'a' && board[i+7] < 'z' && (i & 7) != 7){
                            pts = (byte)Math.max(pts,whitePcsToScore(board[i+7]));
                        }
                    }
                    else{
                        if(board[i+8] == '*'){
                            movePieceD((byte)i,(byte)(i+8));
                            pts = (byte)Math.max(pts,minAI((byte)(depth-1)));
                            movePieceD((byte)(i+8),(byte)i);
                            if(i > 7 && i < 16 && board[i+16] == '*'){
                                movePieceD((byte)i,(byte)(i+16));
                                pts = (byte)Math.max(pts,minAI((byte)(depth-1)));
                                movePieceD((byte)(i+16),(byte)i);
                            }
                        }
                        if(board[i+9] > 'a' && board[i+9] < 'z' && (i & 7) != 0){
                            byte lpts = whitePcsToScore(board[i+9]);
                            char tpcs = board[i+9];
                            capturePieceD((byte)i,(byte)(i+9));
                            pts = (byte)Math.max(pts,minAI((byte)(depth-1)) + lpts);
                            board[i] = board[i+9];
                            board[i+9] = tpcs;
                        }
                        if(board[i+7] > 'a' && board[i+7] < 'z' && (i & 7) != 7){
                            byte lpts = whitePcsToScore(board[i+7]);
                            char tpcs = board[i+7];
                            capturePieceD((byte)i,(byte)(i+7));
                            pts = (byte)Math.max(pts,minAI((byte)(depth-1)) + lpts);
                            board[i] = board[i+7];
                            board[i+7] = tpcs;
                        }
                    }
                    break;
                case 'n':{
                    byte sx = (byte)(i >> 3);
                    byte sy = (byte)(i & 7);
                    if(sx > 0){
                        if(sy < 6){
                            if(depth==0){
                                pts = (byte)Math.max(pts,whitePcsToScore(board[i-6]));
                            }
                            else{
                                if(board[i-6] > 'Z' && board[i-6] < 'A'){
                                    byte lpts = whitePcsToScore(board[i-6]);
                                    char tpcs = board[i-6];
                                    capturePieceD((byte)i,(byte)(i-6));
                                    pts = (byte)Math.max(pts,minAI((byte)(depth-1)) + lpts);
                                    board[i] = board[i-6];
                                    board[i-6] = tpcs;
                                }
                                else if(board[i-6] == '*'){
                                    movePieceD((byte)i,(byte)(i-6));
                                    pts = (byte)Math.max(pts,minAI((byte)(depth-1)));
                                    movePieceD((byte)(i-6),(byte)i);
                                }
                            }
                        }
                        if(sy > 1){
                            if(depth==0){
                                pts = (byte)Math.max(pts,whitePcsToScore(board[i-10]));
                            }
                            else{
                                if(board[i-10] > 'Z' && board[i-10] < 'A'){
                                    byte lpts = whitePcsToScore(board[i-10]);
                                    char tpcs = board[i-10];
                                    capturePieceD((byte)i,(byte)(i-10));
                                    pts = (byte)Math.max(pts,minAI((byte)(depth-1)) + lpts);
                                    board[i] = board[i-10];
                                    board[i-10] = tpcs;
                                }
                                else if(board[i-10] == '*'){
                                    movePieceD((byte)i,(byte)(i-10));
                                    pts = (byte)Math.max(pts,minAI((byte)(depth-1)));
                                    movePieceD((byte)(i-10),(byte)i);
                                }
                            }
                        }
                        if(sx > 1){
                            if(sy > 0){
                                if(depth==0){
                                    pts = (byte)Math.max(pts,whitePcsToScore(board[i-15]));
                                }
                                else{
                                    if(board[i-15] > 'Z' && board[i-15] < 'A'){
                                        byte lpts = whitePcsToScore(board[i-15]);
                                        char tpcs = board[i-15];
                                        capturePieceD((byte)i,(byte)(i-15));
                                        pts = (byte)Math.max(pts,minAI((byte)(depth-1)) + lpts);
                                        board[i] = board[i-15];
                                        board[i-15] = tpcs;
                                    }
                                    else if(board[i-15] == '*'){
                                        movePieceD((byte)i,(byte)(i-15));
                                        pts = (byte)Math.max(pts,minAI((byte)(depth-1)));
                                        movePieceD((byte)(i-15),(byte)i);
                                    }
                                }
                            }
                            if(sy > 0){
                                if(depth==0){
                                    pts = (byte)Math.max(pts,whitePcsToScore(board[i-17]));
                                }
                                else{
                                    if(board[i-17] > 'Z' && board[i-17] < 'A'){
                                        byte lpts = whitePcsToScore(board[i-17]);
                                        char tpcs = board[i-17];
                                        capturePieceD((byte)i,(byte)(i-17));
                                        pts = (byte)Math.max(pts,minAI((byte)(depth-1)) + lpts);
                                        board[i] = board[i-17];
                                        board[i-17] = tpcs;
                                    }
                                    else if(board[i-17] == '*'){
                                        movePieceD((byte)i,(byte)(i-17));
                                        pts = (byte)Math.max(pts,minAI((byte)(depth-1)));
                                        movePieceD((byte)(i-17),(byte)i);
                                    }
                                }
                            }
                        }
                    }
                    if(sx < 7){
                        if(sy > 1){
                            if(depth==0){
                                pts = (byte)Math.max(pts,whitePcsToScore(board[i+6]));
                            }
                            else{
                                if(board[i+6] > 'Z' && board[i+6] < 'A'){
                                    byte lpts = whitePcsToScore(board[i+6]);
                                    char tpcs = board[i+6];
                                    capturePieceD((byte)i,(byte)(i+6));
                                    pts = (byte)Math.max(pts,minAI((byte)(depth-1)) + lpts);
                                    board[i] = board[i+6];
                                    board[i+6] = tpcs;
                                }
                                else if(board[i+6] == '*'){
                                    movePieceD((byte)i,(byte)(i+6));
                                    pts = (byte)Math.max(pts,minAI((byte)(depth-1)));
                                    movePieceD((byte)(i+6),(byte)i);
                                }
                            }
                        }
                        if(sy < 6){
                            if(depth==0){
                                pts = (byte)Math.max(pts,whitePcsToScore(board[i+10]));
                            }
                            else{
                                if(board[i+10] > 'Z' && board[i+10] < 'A'){
                                    byte lpts = whitePcsToScore(board[i+10]);
                                    char tpcs = board[i+10];
                                    capturePieceD((byte)i,(byte)(i+10));
                                    pts = (byte)Math.max(pts,minAI((byte)(depth-1)) + lpts);
                                    board[i] = board[i+10];
                                    board[i+10] = tpcs;
                                }
                                else if(board[i+10] == '*'){
                                    movePieceD((byte)i,(byte)(i+10));
                                    pts = (byte)Math.max(pts,minAI((byte)(depth-1)));
                                    movePieceD((byte)(i+10),(byte)i);
                                }
                            }
                        }
                        if(sx < 6){
                            if(sy > 0){
                                if(depth==0){
                                    pts = (byte)Math.max(pts,whitePcsToScore(board[i+15]));
                                }
                                else{
                                    if(board[i+15] > 'Z' && board[i+15] < 'A'){
                                        byte lpts = whitePcsToScore(board[i+15]);
                                        char tpcs = board[i+15];
                                        capturePieceD((byte)i,(byte)(i+15));
                                        pts = (byte)Math.max(pts,minAI((byte)(depth-1)) + lpts);
                                        board[i] = board[i+15];
                                        board[i+15] = tpcs;
                                    }
                                    else if(board[i+15] == '*'){
                                        movePieceD((byte)i,(byte)(i+15));
                                        pts = (byte)Math.max(pts,minAI((byte)(depth-1)));
                                        movePieceD((byte)(i+15),(byte)i);
                                    }
                                }
                            }
                            if(sy < 7){
                                if(depth==0){
                                    pts = (byte)Math.max(pts,whitePcsToScore(board[i+17]));
                                }
                                else{
                                    if(board[i+17] > 'Z' && board[i+17] < 'A'){
                                        byte lpts = whitePcsToScore(board[i+17]);
                                        char tpcs = board[i+17];
                                        capturePieceD((byte)i,(byte)(i+17));
                                        pts = (byte)Math.max(pts,minAI((byte)(depth-1)) + lpts);
                                        board[i] = board[i+17];
                                        board[i+17] = tpcs;
                                    }
                                    else if(board[i+17] == '*'){
                                        movePieceD((byte)i,(byte)(i+17));
                                        pts = (byte)Math.max(pts,minAI((byte)(depth-1)));
                                        movePieceD((byte)(i+17),(byte)i);
                                    }
                                }
                            }
                        }
                    }
                    break;
                }
                case 'b':
                    pts = maxAIdiagonal((byte)i,depth,pts);
                    break;
                case 'r':
                    pts = maxAIstraight((byte)i,depth,pts);
                    break;
                case 'q':
                    pts = maxAIstraight((byte)i,depth,pts);
                    pts = maxAIdiagonal((byte)i,depth,pts);
                    break;
            }
        }
        return pts;
    }
    static byte minAIdiagonal(byte i,byte depth,byte pts){
        if(i > 7){
            if((i & 7) != 0){
                if(depth==0){
                    for(int i2 = i - 9;board[i2] < 'A' || board[i2] > 'Z';i2-=9){
                        if(board[i2] > 'a' && board[i2] < 'z'){
                            pts = (byte)Math.min(pts,blackPcsToScore(board[i2]));
                            break;
                        }
                        if(i2 < 8 || (i2 & 7) == 0){
                            break;
                        }
                    }
                }
                else{
                    for(int i2 = i - 9;board[i2] < 'A' || board[i2] > 'Z';i2-=9){
                        if(board[i2] > 'a' && board[i2] < 'z'){
                            byte lpts = blackPcsToScore(board[i2]);
                            char tpcs = board[i2];
                            capturePieceD(i,(byte)i2);
                            pts = (byte)Math.min(pts,maxAI((byte) (depth-1)) + lpts);
                            board[i] = board[i2];
                            board[i2] = tpcs;
                            break;
                        }
                        else{
                            movePieceD(i,(byte)i2);
                            pts = (byte)Math.min(pts,maxAI((byte)(depth-1)));
                            movePieceD((byte)i2,i);
                        }
                        if(i2 < 8 || (i2 & 7) == 0){
                            break;
                        }
                    }
                }
            }
            if((i & 7) != 7){
                if(depth==0){
                    for(int i2 = i - 7;board[i2] < 'A' || board[i2] > 'Z';i2-=7){
                        if(board[i2] > 'a' && board[i2] < 'z'){
                            pts = (byte)Math.min(pts,blackPcsToScore(board[i2]));
                            break;
                        }
                        if(i2 < 8 || (i2 & 7) == 7){
                            break;
                        }
                    }
                }
                else{
                    for(int i2 = i - 7;board[i2] < 'A' || board[i2] > 'Z';i2-=7){
                        if(board[i2] > 'a' && board[i2] < 'z'){
                            byte lpts = blackPcsToScore(board[i2]);
                            char tpcs = board[i2];
                            capturePieceD(i,(byte)i2);
                            pts = (byte)Math.min(pts,maxAI((byte)(depth-1)) + lpts);
                            board[i] = board[i2];
                            board[i2] = tpcs;
                            break;
                        }
                        else{
                            movePieceD(i,(byte)i2);
                            pts = (byte)Math.min(pts,maxAI((byte)(depth-1)));
                            movePieceD((byte)i2,i);
                        }
                        if(i2 < 8 || (i2 & 7) == 7){
                            break;
                        }
                    }
                }
            }
        }
        if(i < 55){
            if((i & 7) != 7){
                if(depth==0){
                    for(int i2 = i + 9;board[i2] < 'A' || board[i2] > 'Z';i2+=9){
                        if(board[i2] > 'a' && board[i2] < 'z'){
                            pts = (byte)Math.min(pts,blackPcsToScore(board[i2]));
                            break;
                        }
                        if(i2 > 55 || (i2 & 7) == 7){
                            break;
                        }
                    }
                }
                else{
                    for(int i2 = i + 9;board[i2] < 'A' || board[i2] > 'Z';i2+=9){
                        if(board[i2] > 'a' && board[i2] < 'z'){
                            byte lpts = blackPcsToScore(board[i2]);
                            char tpcs = board[i2];
                            capturePieceD(i,(byte)i2);
                            pts = (byte)Math.min(pts,maxAI((byte)(depth-1)) + lpts);
                            board[i] = board[i2];
                            board[i2] = tpcs;
                            break;
                        }
                        else{
                            movePieceD(i,(byte)i2);
                            pts = (byte)Math.min(pts,maxAI((byte)(depth-1)));
                            movePieceD((byte)i2,i);
                        }
                        if(i2 > 55 || (i2 & 7) == 7){
                            break;
                        }
                    }
                }
            }
            if((i & 7) != 0){
                if(depth==0){
                    for(int i2 = i + 7;board[i2] < 'A' || board[i2] > 'Z';i2+=7){
                        if(board[i2] > 'a' && board[i2] < 'z'){
                            pts = (byte)Math.min(pts,blackPcsToScore(board[i2]));
                            break;
                        }
                        if(i2 > 55  || (i2 & 7) == 0){
                            break;
                        }
                    }
                }
                else{
                    for(int i2 = i + 7;board[i2] < 'A' || board[i2] > 'Z';i2+=7){
                        if(board[i2] > 'a' && board[i2] < 'z'){
                            byte lpts = blackPcsToScore(board[i2]);
                            char tpcs = board[i2];
                            capturePieceD(i,(byte)i2);
                            pts = (byte)Math.min(pts,maxAI((byte)(depth-1)) + lpts);
                            board[i] = board[i2];
                            board[i2] = tpcs;
                            break;
                        }
                        else{
                            movePieceD(i,(byte)i2);
                            pts = (byte)Math.min(pts,maxAI((byte)(depth-1)));
                            movePieceD((byte)i2,i);
                        }
                        if(i2 > 55 || (i2 & 7) == 0){
                            break;
                        }
                    }
                }
            }
        }
        return pts;
    }
    static byte minAIstraight(byte i,byte depth,byte pts){
        if((i & 7) != 7){
            if(depth==0){
                for(int i2 = i + 1;board[i2] < 'A' || board[i2] > 'Z';i2++){
                    if(board[i2] > 'a' && board[i2] < 'z'){
                        pts = (byte)Math.min(pts,blackPcsToScore(board[i2]));
                        break;
                    }
                    if((i2 & 7) != 7){
                        break;
                    }
                }
            }
            else{
                for(int i2 = i + 1;board[i2] < 'A' || board[i2] > 'Z';i2++){
                    if(board[i2] > 'a' && board[i2] < 'z'){
                        byte lpts = blackPcsToScore(board[i2]);
                        char tpcs = board[i2];
                        capturePieceD(i,(byte)i2);
                        pts = (byte)Math.min(pts,maxAI((byte)(depth-1)) + lpts);
                        board[i] = board[i2];
                        board[i2] = tpcs;
                        break;
                    }
                    else{
                        movePieceD(i,(byte)i2);
                        pts = (byte)Math.min(pts,maxAI((byte)(depth-1)));
                        movePieceD((byte)i2,i);
                    }
                    if((i2 & 7) == 7){
                        break;
                    }
                }
            }
        }
        if((i & 7) != 0){
            if(depth==0){
                for(int i2 = i - 1;board[i2] < 'A' || board[i2] > 'Z';i2--){
                    if(board[i2] > 'a' && board[i2] < 'z'){
                        pts = (byte)Math.min(pts,blackPcsToScore(board[i2]));
                        break;
                    }
                    if((i2 & 7) != 0){
                        break;
                    }
                }
            }
            else{
                for(int i2 = i - 1;board[i2] < 'A' || board[i2] > 'Z';i2--){
                    if(board[i2] > 'a' && board[i2] < 'z'){
                        byte lpts = blackPcsToScore(board[i2]);
                        char tpcs = board[i2];
                        capturePieceD(i,(byte)i2);
                        pts = (byte)Math.min(pts,maxAI((byte)(depth-1)) + lpts);
                        board[i] = board[i2];
                        board[i2] = tpcs;
                        break;
                    }
                    else{
                        movePieceD(i,(byte)i2);
                        pts = (byte)Math.min(pts,maxAI((byte)(depth-1)));
                        movePieceD((byte)i2,i);
                    }
                    if((i2 & 7) != 0){
                        break;
                    }
                }
            }
        }
        if(i > 7){
            if(depth==0){
                for(int i2 = i - 8;board[i2] < 'A' || board[i2] > 'Z';i2-=8){
                    if(board[i2] > 'a' && board[i2] < 'z'){
                        pts = (byte)Math.min(pts,blackPcsToScore(board[i2]));
                        break;
                    }
                    if(i2 > 7){
                        break;
                    }
                }
            }
            else{
                for(int i2 = i - 8;board[i2] < 'A' || board[i2] > 'Z';i2-=8){
                    if(board[i2] > 'a' && board[i2] < 'z'){
                        byte lpts = blackPcsToScore(board[i2]);
                        char tpcs = board[i2];
                        capturePieceD(i,(byte)i2);
                        pts = (byte)Math.min(pts,maxAI((byte)(depth-1)) + lpts);
                        board[i] = board[i2];
                        board[i2] = tpcs;
                        break;
                    }
                    else{
                        movePieceD(i,(byte)i2);
                        pts = (byte)Math.min(pts,maxAI((byte)(depth-1)));
                        movePieceD((byte)i2,i);
                    }
                    if(i2 > 7){
                        break;
                    }
                }
            }
        }
        if(i < 55){
            if(depth==0){
                for(int i2 = i + 7;board[i2] < 'A' || board[i2] > 'Z';i2+=7){
                    if(board[i2] > 'a' && board[i2] < 'z'){
                        pts = (byte)Math.min(pts,blackPcsToScore(board[i2]));
                        break;
                    }
                    if(i2 < 55){
                        break;
                    }
                }
            }
            else{
                for(int i2 = i + 8;board[i2] < 'A' || board[i2] > 'Z';i2+=8){
                    if(board[i2] > 'a' && board[i2] < 'z'){
                        byte lpts = blackPcsToScore(board[i2]);
                        char tpcs = board[i2];
                        capturePieceD(i,(byte)i2);
                        pts = (byte)Math.min(pts,maxAI((byte)(depth-1)) + lpts);
                        board[i] = board[i2];
                        board[i2] = tpcs;
                        break;
                    }
                    else{
                        movePieceD(i,(byte)i2);
                        pts = (byte)Math.min(pts,maxAI((byte)(depth-1)));
                        movePieceD((byte)i2,i);
                    }
                    if(i2 > 55){
                        break;
                    }
                }
            }
        }
        return pts;
    }
    static byte minAI(byte depth){
        byte pts = 127;
        for(int i = 0;i < 64;i++){
            switch(board[i]){
                case 'P':
                    if(depth==0){
                        if(board[i-9] > 'a' && board[i-9] < 'z' && (i & 7) != 0){
                            pts = (byte)Math.min(pts,blackPcsToScore(board[i-9]));
                        }
                        if(board[i-7] > 'a' && board[i-7] < 'z' && (i & 7) != 7){
                            pts = (byte)Math.min(pts,blackPcsToScore(board[i-7]));
                        }
                    }
                    else{
                        if(board[i-8] == '*'){
                            movePieceD((byte)i,(byte)(i-8));
                            pts = (byte)Math.min(pts,maxAI((byte)(depth-1)));
                            movePieceD((byte)(i-8),(byte)i);
                            if(i > 47 && i < 56 && board[i-16] == '*'){
                                movePieceD((byte)i,(byte)(i-16));
                                pts = (byte)Math.min(pts,maxAI((byte)(depth-1)));
                                movePieceD((byte)(i-16),(byte)i);
                            }
                        }
                        if(board[i-9] > 'a' && board[i-9] < 'z' && (i & 7) != 0){
                            byte lpts = blackPcsToScore(board[i-9]);
                            char tpcs = board[i-9];
                            capturePieceD((byte)i,(byte)(i-9));
                            pts = (byte)Math.min(pts,maxAI((byte)(depth-1)) + lpts);
                            board[i] = board[i-9];
                            board[i-9] = tpcs;
                        }
                        if(board[i-7] > 'a' && board[i-7] < 'z' && (i & 7) != 7){
                            byte lpts = blackPcsToScore(board[i-7]);
                            char tpcs = board[i-7];
                            capturePieceD((byte)i,(byte)(i-7));
                            pts = (byte)Math.min(pts,maxAI((byte)(depth-1)) + lpts);
                            board[i] = board[i-7];
                            board[i-7] = tpcs;
                        }
                    }
                    break;
                case 'N':{
                    byte sx = (byte)(i >> 3);
                    byte sy = (byte)(i & 7);
                    if(sx > 0){
                        if(sy > 1){
                            if(depth==0){
                                pts = (byte)Math.min(pts,blackPcsToScore(board[i-6]));
                            }
                            else{
                                if(board[i-6] > 'Z' && board[i-6] < 'A'){
                                    byte lpts = blackPcsToScore(board[i-6]);
                                    char tpcs = board[i-6];
                                    capturePieceD((byte)i,(byte)(i-6));
                                    pts = (byte)Math.min(pts,maxAI((byte)(depth-1)) + lpts);
                                    board[i] = board[i-6];
                                    board[i-6] = tpcs;
                                }
                                else if(board[i-6] == '*'){
                                    movePieceD((byte)i,(byte)(i-6));
                                    pts = (byte)Math.min(pts,maxAI((byte)(depth-1)));
                                    movePieceD((byte)(i-6),(byte)i);
                                }
                            }
                        }
                        if(sy < 6){
                            if(depth==0){
                                pts = (byte)Math.min(pts,blackPcsToScore(board[i-10]));
                            }
                            else{
                                if(board[i-10] > 'Z' && board[i-10] < 'A'){
                                    byte lpts = blackPcsToScore(board[i-10]);
                                    char tpcs = board[i-10];
                                    capturePieceD((byte)i,(byte)(i-10));
                                    pts = (byte)Math.min(pts,maxAI((byte)(depth-1)) + lpts);
                                    board[i] = board[i-10];
                                    board[i-10] = tpcs;
                                }
                                else if(board[i-10] == '*'){
                                    movePieceD((byte)i,(byte)(i-10));
                                    pts = (byte)Math.min(pts,maxAI((byte)(depth-1)));
                                    movePieceD((byte)(i-10),(byte)i);
                                }
                            }
                        }
                        if(sx > 1){
                            if(sy > 0){
                                if(depth==0){
                                    pts = (byte)Math.min(pts,blackPcsToScore(board[i-15]));
                                }
                                else{
                                    if(board[i-15] > 'Z' && board[i-15] < 'A'){
                                        byte lpts = blackPcsToScore(board[i-15]);
                                        char tpcs = board[i-15];
                                        capturePieceD((byte)i,(byte)(i-15));
                                        pts = (byte)Math.min(pts,maxAI((byte)(depth-1)) + lpts);
                                        board[i] = board[i-15];
                                        board[i-15] = tpcs;
                                    }
                                    else if(board[i-15] == '*'){
                                        movePieceD((byte)i,(byte)(i-15));
                                        pts = (byte)Math.min(pts,maxAI((byte)(depth-1)));
                                        movePieceD((byte)(i-15),(byte)i);
                                    }
                                }
                            }
                            if(sy < 7){
                                if(depth==0){
                                    pts = (byte)Math.min(pts,blackPcsToScore(board[i-17]));
                                }
                                else{
                                    if(board[i-17] > 'Z' && board[i-17] < 'A'){
                                        byte lpts = blackPcsToScore(board[i-17]);
                                        char tpcs = board[i-17];
                                        capturePieceD((byte)i,(byte)(i-17));
                                        pts = (byte)Math.min(pts,maxAI((byte)(depth-1)) + lpts);
                                        board[i] = board[i-17];
                                        board[i-17] = tpcs;
                                    }
                                    else if(board[i-17] == '*'){
                                        movePieceD((byte)i,(byte)(i-17));
                                        pts = (byte)Math.min(pts,maxAI((byte)(depth-1)));
                                        movePieceD((byte)(i-17),(byte)i);
                                    }
                                }
                            }
                        }
                    }
                    if(sx < 7){
                        if(sy > 1){
                            if(depth==0){
                                pts = (byte)Math.min(pts,blackPcsToScore(board[i+6]));
                            }
                            else{
                                if(board[i+6] > 'Z' && board[i+6] < 'A'){
                                    byte lpts = blackPcsToScore(board[i+6]);
                                    char tpcs = board[i+6];
                                    capturePieceD((byte)i,(byte)(i+6));
                                    pts = (byte)Math.min(pts,maxAI((byte)(depth-1)) + lpts);
                                    board[i] = board[i+6];
                                    board[i+6] = tpcs;
                                }
                                else if(board[i+6] == '*'){
                                    movePieceD((byte)i,(byte)(i+6));
                                    pts = (byte)Math.min(pts,maxAI((byte)(depth-1)));
                                    movePieceD((byte)(i+6),(byte)i);
                                }
                            }
                        }
                        if(sy < 6){
                            if(depth==0){
                                pts = (byte)Math.min(pts,blackPcsToScore(board[i+10]));
                            }
                            else{
                                if(board[i+10] > 'Z' && board[i+10] < 'A'){
                                    byte lpts = blackPcsToScore(board[i+10]);
                                    char tpcs = board[i+10];
                                    capturePieceD((byte)i,(byte)(i+10));
                                    pts = (byte)Math.min(pts,maxAI((byte)(depth-1)) + lpts);
                                    board[i] = board[i+10];
                                    board[i+10] = tpcs;
                                }
                                else if(board[i+10] == '*'){
                                    movePieceD((byte)i,(byte)(i+10));
                                    pts = (byte)Math.min(pts,maxAI((byte)(depth-1)));
                                    movePieceD((byte)(i+10),(byte)i);
                                }
                            }
                        }
                        if(sx < 6){
                            if(sy > 0){
                                if(depth==0){
                                    pts = (byte)Math.min(pts,blackPcsToScore(board[i+15]));
                                }
                                else{
                                    if(board[i+15] > 'Z' && board[i+15] < 'A'){
                                        byte lpts = blackPcsToScore(board[i-15]);
                                        char tpcs = board[i+15];
                                        capturePieceD((byte)i,(byte)(i+15));
                                        pts = (byte)Math.min(pts,maxAI((byte)(depth-1))+ lpts);
                                        board[i] = board[i+15];
                                        board[i+15] = tpcs;
                                    }
                                    else if(board[i+15] == '*'){
                                        movePieceD((byte)i,(byte)(i+15));
                                        pts = (byte)Math.min(pts,maxAI((byte)(depth-1)));
                                        movePieceD((byte)(i+15),(byte)i);
                                    }
                                }
                            }
                            if(sy < 7){
                                if(depth==0){
                                    pts = (byte)Math.min(pts,blackPcsToScore(board[i+17]));
                                }
                                else{
                                    if(board[i+17] > 'Z' && board[i+17] < 'A'){
                                        byte lpts = blackPcsToScore(board[i+17]);
                                        char tpcs = board[i+17];
                                        capturePieceD((byte)i,(byte)(i+17));
                                        pts = (byte)Math.min(pts,maxAI((byte)(depth-1)) + lpts);
                                        board[i] = board[i+17];
                                        board[i+17] = tpcs;
                                    }
                                    else if(board[i+17] == '*'){
                                        movePieceD((byte)i,(byte)(i+17));
                                        pts = (byte)Math.min(pts,maxAI((byte)(depth-1)));
                                        movePieceD((byte)(i+17),(byte)i);
                                    }
                                }
                            }
                        }
                    }
                    break;
                }
                case 'B':
                    pts = minAIdiagonal((byte)i,depth,pts);
                    break;
                case 'R':
                    pts = minAIstraight((byte)i,depth,pts);
                    break;
                case 'Q':
                    pts = minAIdiagonal((byte)i,depth,pts);
                    pts = minAIstraight((byte)i,depth,pts);
                    break;
            }
        }
        return pts;
    }
    static void printMove(byte src,byte dst,byte pts){
        System.out.print((char)((src&7)+'A'));
        System.out.print((char)((src>>3)+'0'));
        System.out.print('-');
        System.out.print((char)((dst&7)+'A'));
        System.out.print((char)((dst>>3)+'0'));
        System.out.print(" = ");
        System.out.println(pts);
    }
    static void captureAI(byte src,byte dst,byte[] pts,byte[] movC){
        char tpcs = board[dst];
        capturePieceD(src,dst);
        byte tpts = minAI(DEPTH);
        board[src] = board[dst];
        board[dst] = tpcs;
        tpts += whitePcsToScore(tpcs);
        printMove(src,dst,tpts);
        if(pts[0] <= tpts){
            if(pts[0] != tpts){
                pts[0] = tpts;
                movC[0] = 0;
            }
            movs[movC[0]].src = src;
            movs[movC[0]].dst = dst;
            movC[0]+=1;
        }
    }
    static void moveAI(byte src,byte dst,byte[] pts,byte[] movC){
        movePieceD(src,dst);
        byte tpts = minAI(DEPTH);
        movePieceD(dst,src);
        printMove(src,dst,tpts);
        if(pts[0] <= tpts){
            if(pts[0] != tpts){
                pts[0] = tpts;
                movC[0] = 0;
            }
            movs[movC[0]].src = src;
            movs[movC[0]].dst = dst;
            movC[0]+=1;
        }
    }
    static void moveCaptureAI(byte src,byte dst,byte[] pts,byte[] movC){
        if(board[dst] > 'A' && board[dst] < 'Z'){
            captureAI(src,dst,pts,movC);
        }
        else if(board[dst] == '*'){
            moveAI(src,dst,pts,movC);
        }
    }
    static void straightAI(byte i,byte[] pts,byte[] movC){
        if((i & 7) != 7){
            for(int i2 = i + 1;board[i2] < 'a' || board[i2] > 'z';i2++){
                if(board[i2] > 'A' && board[i2] < 'Z'){
                    captureAI(i,(byte)i2,pts,movC);
                    break;
                }
                else{
                    moveAI(i,(byte)i2,pts,movC);
                }
                if((i2 & 7) == 7){
                    break;
                }
            }
        }
        if((i & 7) != 0){
            for(int i2 = i - 1;board[i2] < 'a' || board[i2] > 'z';i2--){
                if(board[i2] > 'A' && board[i2] < 'Z'){
                    captureAI(i,(byte)i2,pts,movC);
                    break;
                }
                else{
                    moveAI(i,(byte)i2,pts,movC);
                }
                if((i2 & 7) == 0){
                    break;
                }
            }
        }
        if(i > 7){
            for(int i2 = i - 8;board[i2] < 'a' || board[i2] > 'z';i2-=8){
                if(board[i2] > 'A' && board[i2] < 'Z'){
                    captureAI(i,(byte)i2,pts,movC);
                    break;
                }
                else{
                    moveAI(i,(byte)i2,pts,movC);
                }
                if(i2 < 8){
                    break;
                }
            }
        }
        if(i < 55){
            for(int i2 = i + 8;board[i2] < 'a' || board[i2] > 'z';i2+=8){
                if(board[i2] > 'A' && board[i2] < 'Z'){
                    captureAI(i,(byte)i2,pts,movC);
                    break;
                }
                else{
                    moveAI(i,(byte)i2,pts,movC);
                }
                if(i2 > 55){
                    break;
                }
            }
        }
    }
    static void diagonalAI(byte i,byte[] pts,byte[] movC){
        if(i > 7){
            if((i & 7) != 7){
                for(int i2 = i - 9;board[i2] < 'a' || board[i2] > 'z';i2-=9){
                    if(board[i2] > 'A' && board[i2] < 'Z'){
                        captureAI(i,(byte)i2,pts,movC);
                        break;
                    }
                    else{
                        moveAI(i,(byte)i2,pts,movC);
                    }
                    if(i2 < 8 || (i2 & 7) == 7){
                        break;
                    }
                }
            }
            if((i & 7) != 0){
                for(int i2 = i - 7;board[i2] < 'a' || board[i2] > 'z';i2-=7){
                    if(board[i2] > 'A' && board[i2] < 'Z'){
                        captureAI(i,(byte)i2,pts,movC);
                        break;
                    }
                    else{
                        moveAI(i,(byte)i2,pts,movC);
                    }
                    if(i2 < 8 || (i2 & 7) == 0){
                        break;
                    }
                }
            }
        }
        if(i < 55){
            if((i & 7) != 7){
                for(int i2 = i + 9;board[i2] < 'a' || board[i2] > 'z';i2+=9){
                    if(board[i2] > 'A' && board[i2] < 'Z'){
                        captureAI(i,(byte)i2,pts,movC);
                        break;
                    }
                    else{
                        moveAI(i,(byte)i2,pts,movC);
                    }
                    if(i2 > 55 || (i2 & 7) == 7){
                        break;
                    }
                }
            }
            if((i & 7) != 0){
                for(int i2 = i + 7;board[i2] < 'a' || board[i2] > 'z';i2+=7){
                    if(board[i2] > 'A' && board[i2] < 'Z'){
                        captureAI(i,(byte)i2,pts,movC);
                        break;
                    }
                    else{
                        moveAI(i,(byte)i2,pts,movC);
                    }
                    if(i2 > 55  || (i2 & 7) == 0){
                        break;
                    }
                }
            }
        }
    }
    static byte checkDiagonal(int SX,int SY,int DX,int DY){
        if(SX-DX==SY-DY){
            if(SX<DX){
                for(int i = 1;i < (DX-SX)+1;i++){
                    if(board[(SX+i)*8+(SY+i)] != '*'){
                        return 0;
                    }
                }
            }
            else{
                for(int i = 0;i < (SX-DX);i++){
                    if(board[(DX+i)*8+(DY+i)] != '*'){
                        return 0;
                    }
                }
            }
            movePiece(SX,SY,DX,DY);
            return 1;
        }
        else if(SX-DX==DY-SY){
            if(SX<DX){
                for(int i = 1;i < (DX-SX)+1;i++){
                    if(board[(SX+i)*8+(SY-i)] != '*'){
                        return 0;
                    }
                }
            }
            else{
                for(int i = 0;i < (SX-DX);i++){
                    if(board[(DX+i)*8+(DY-i)] != '*'){
                        return 0;
                    }
                }
            }
            movePiece(SX,SY,DX,DY);
            return 1;
        }
        return 0;
    }
    static byte checkStraight(int SX,int SY,int DX,int DY){
        if(SX == DX){
            if(SY < DY){
                for(int i = SY+1;i < DY;i++){
                    if(board[SX*8+i] != '*'){
                        return 0;
                    }
                }
            }
            else{
                for(int i = DY+1;i < SY;i++){
                    if(board[SX*8+i] != '*'){
                        return 0;
                    }
                }
            }
            movePiece(SX,SY,DX,DY);
            return 1;
        }
        else if(SY == DY){
            if(SX < DX){
                for(int i = SX+1;i < DX;i++){
                    if(board[i*8+SY] != '*'){
                        return 0;
                    }
                }
            }
            else{
                for(int i = DX+1;i < SX;i++){
                    if(board[i*8+SY] != '*'){
                        return 0;
                    }
                }
            }
            movePiece(SX,SY,DX,DY);
            return 1;
        }
        return 0;
    }
    public static void main(String[] args){
        for(int i = 0;i < 100;i++){
            movs[i] = new CHESSMOVE();
        }
        System.out.println("welcome to chessTM");
        printBoard();
        mainLoop:for(;;) {
            Scanner scan = new Scanner(System.in);
            String inp = scan.nextLine();
            int SX = inp.charAt(1) - '0';
            int SY = inp.charAt(0) - 'A';
            int DX = inp.charAt(4) - '0';
            int DY = inp.charAt(3) - 'A';
            switch (board[SX*8+SY]) {
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
                        if(board[DX*8+i] != '*'){
                            continue mainLoop;
                        }
                    }
                    movePiece(SX,SY,DX,DY);
                    movePiece(7,7,7,5);
                }
                else if(DY == 1 && (castleRights & 0b0010) == 0b0010){
                    for(int i = 3;i > 0;i--){
                        if(board[DX*8+i] != '*'){
                            continue mainLoop;
                        }
                    }
                    movePiece(SX,SY,DX,DY);
                    movePiece(0,0,0,2);

                }
                break;
            }
            case 'Q':
                if(checkDiagonal(SX,SY,DX,DY)==0){continue;}
                if(checkStraight(SX,SY,DX,DY)==0){continue;}
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
                if(checkDiagonal(SX,SY,DX,DY)==0){continue;}
                break;
            case 'R':
                if(checkStraight(SX,SY,DX,DY)==0){continue;}
                break;
            case 'P':
                if (SX == DX + 1) {
                    if (SY == DY) {
                        if (board[DX*8+DY] == '*') {
                            if (DX == 0){
                                System.out.println("promotion");
                                String piece;
                                do{
                                    piece = scan.nextLine();
                                }while(piece.charAt(0)!='Q'&&piece.charAt(0)!='R'&&piece.charAt(0)!='N'&&piece.charAt(0)!='B');
                                board[SX*8+SY] = piece.charAt(0);
                            }
                            movePiece(SX,SY,DX,DY);
                        }
                        else{
                            continue;
                        }
                    }
                    else if (SY == DY + 1 || SY == DY - 1) {
                        if (board[DX*8+DY] >= 'a' && board[DX*8+DY] <= 'z') {
                            if (DX == 0){
                                System.out.println("promotion");
                                String piece;
                                do{
                                    piece = scan.nextLine();
                                }while(piece.charAt(0)!='Q'&&piece.charAt(0)!='R'&&piece.charAt(0)!='N'&&piece.charAt(0)!='B');
                                board[SX*8+SY] = piece.charAt(0);
                            }
                            capturePiece(SX,SY,DX,DY);
                        }
                        else{
                            continue;
                        }
                    }
                }
                else if(SX == DX + 2){
                    if(SX == 6){
                        if (board[(DX-1)*8+DY] == '*' && board[DX*8+DY] == '*') {
                             movePiece(SX,SY,DX,DY);
                        }
                    }
                }
                else{
                    continue;
                }
                break;
            }
            printBoard();
            byte[] movC = {0};
            byte[] pts = {-128};
            for(int i = 0;i < 64;i++){
                switch(board[i]){
                    case 'p':
                        if(board[i+8] == '*'){
                            moveAI((byte)i,(byte)(i+8),pts,movC);
                            if(i > 7 && i < 16){
                                if(board[i+16] == '*'){
                                    moveAI((byte)i,(byte)(i+16),pts,movC);
                                }
                            }
                        }
                        if(board[i+9] > 'A' && board[i+9] < 'Z' && (i & 7) != 7){
                            captureAI((byte)i,(byte)(i+9),pts,movC);
                        }
                        if(board[i+7] > 'A' && board[i+7] < 'Z' && (i & 7) != 0){
                            captureAI((byte)i,(byte)(i+7),pts,movC);
                        }
                        break;
                    case 'n':{
                        byte sx = (byte)(i >> 3);
                        byte sy = (byte)(i & 7);
                        if(sx > 0){
                            if(sy > 1){
                                moveCaptureAI((byte)i,(byte)(i-6),pts,movC);
                            }
                            if(sy < 6){
                                moveCaptureAI((byte)i,(byte)(i-10),pts,movC);
                            }
                            if(sx > 1){
                                if(sy > 0){
                                    moveCaptureAI((byte)i,(byte)(i-15),pts,movC);
                                }
                                if(sy < 7){
                                    moveCaptureAI((byte)i,(byte)(i-17),pts,movC);
                                }
                            }
                        }
                        if(sx < 7){
                            if(sy > 1){
                                moveCaptureAI((byte)i,(byte)(i+6),pts,movC);
                            }
                            if(sy < 6){
                                moveCaptureAI((byte)i,(byte)(i+10),pts,movC);
                            }
                            if(sx < 6){
                                if(sy > 0){
                                    moveCaptureAI((byte)i,(byte)(i+15),pts,movC);
                                }
                                if(sy < 7){
                                    moveCaptureAI((byte)i,(byte)(i+17),pts,movC);
                                }
                            }
                        }
                        break;
                    }
                    case 'b':
                        diagonalAI((byte)i,pts,movC);
                        break;
                    case 'r':
                        straightAI((byte)i,pts,movC);
                        break;
                    case 'q':
                        diagonalAI((byte)i,pts,movC);
                        straightAI((byte)i,pts,movC);
                        break;
                }
            }
            byte mv = (byte)(System.currentTimeMillis()%movC[0]);
            byte mvsrc = movs[mv].src;
            byte mvdst = movs[mv].dst;
            movePieceBlack(mvsrc>>3,mvsrc&7,mvdst>>3,mvdst&7);
            printMove(mvsrc,mvdst,pts[0]);
            printBoard();
        }
    }
}
