import java.util.ArrayList;

/**
 * Created by Xuhao Chen on 2016/12/29.
 */
public class Board {
    private char[][] board;
    private boolean[] hasMoved;  // 0 - Black King
                                 // 1 - Black Left Rook
                                 // 2 - Black Right Rook
                                 // 3 - White King
                                 // 4 - White Left Rook
                                 // 5 - White Right Rook
    /**
     * index:
     *      0 - Rook
     *      1 - Knight
     *      2 - Bishop
     *      3 - Queen
     *      4 - King
     *      5 - Bishop
     *      6 - Knight
     *      7 - Rook
     *      8 to 15 - Pawn
     * If a chess has been taken, then mark as -1
     */
    private int[] blackX;
    private int[] blackY;
    private int[] whiteX;
    private int[] whiteY;
    private ArrayList<Character> takenChess;

    public Board(){
        board = new char[8][8];
        takenChess = new ArrayList<>();
        blackX = new int[16];
        blackY = new int[16];
        whiteX = new int[16];
        whiteY = new int[16];
        initBoard();
    }

    /**
     * peek the state of the current board
     * @return the current state of the board,
     *         CANNOT CHANGE THE STATE OF THE BOARD BY CHANGING RETURNED OBJECT
     */
    public char[][] getBoard(){
        return board.clone();
    }

    //has already moved
    public boolean hasBlackKingMoved(){
        return hasMoved[0];
    }
    public boolean hasBlackLeftRookMoved(){
        return hasMoved[1];
    }
    public boolean hasBlackRightRookMoved(){
        return hasMoved[2];
    }
    public boolean hasWhiteKingMoved(){
        return hasMoved[3];
    }
    public boolean hasWhiteLeftRookMoved(){
        return hasMoved[4];
    }
    public boolean hasWhiteRightRookMoved(){
        return hasMoved[5];
    }

    /**
     * move chess from a location to a location, only check castling, no other rules checked
     * @param chess which is going to be moved
     * @param from location of the current
     * @param to location where it is going to be
     * @throws CustomException.WrongLocationException if from or to is outside of the board
     * @throws CustomException.WrongChessException if no corresponding chess at from location
     * @throws CustomException.CannotTakeOwnChessException if from and to locations have same case chars
     */
    public void moveChess(char chess,Location from, Location to) throws CustomException.WrongLocationException, CustomException.WrongChessException, CustomException.CannotTakeOwnChessException {
        moveChess(chess,from.x,from.y,to.x,to.y);
    }
    public void moveChess(char chess,int fromX, int fromY, int toX, int toY) throws CustomException.WrongLocationException, CustomException.WrongChessException, CustomException.CannotTakeOwnChessException {
        //if outside of the board
        if(isOutOfBoard(fromX,fromY) || isOutOfBoard(toX,toY)) throw new CustomException.WrongLocationException();
        //check validation
        if(board[fromX][fromY] != chess) throw new CustomException.WrongChessException();

        //check if this move is castling
        if(chess == 'K' && !hasBlackKingMoved()){  //black King
            if(toX == 2 && toY == 0){  //long castling
                if(!hasBlackLeftRookMoved()) moveChess('R',0,0,3,0);
            }
            if(toX == 6 && toY == 0){  //short castling
                if(!hasBlackRightRookMoved()) moveChess('R',7,0,5,0);
            }
        }
        if(chess == 'k'){  //white King
            if(toX == 2 && toY == 7){  //long castling
                if(!hasWhiteLeftRookMoved()) moveChess('r',0,7,3,7);
            }
            if(toX == 6 && toY == 7){  //short castling
                if(!hasWhiteRightRookMoved()) moveChess('r',7,7,5,7);
            }
        }

        if(hasChess(board,toX,toY)){  //has chess there
            if( Character.isUpperCase(board[toX][toY]) == Character.isUpperCase(chess) ) throw new CustomException.CannotTakeOwnChessException();  //has own chess
            takenChess.add(board[toX][toY]);
        }
        board[fromX][fromY] = '-';
        board[toX][toY] = chess;

        //update chess location list
        switch(chess){
            case 'R':
                if(new Location(fromX,fromY).equals(new Location(blackX[0], blackY[0]))){
                    blackX[0] = toX;
                    blackY[0] = toY;
                }else if(new Location(fromX,fromY).equals(new Location(blackX[7], blackY[7]))){
                    blackX[7] = toX;
                    blackY[7] = toY;
                }else{
                    System.err.println("No " + chess + " Found in this location.");
                    throw new CustomException.WrongChessException();
                }
                break;

            case 'N':
                if(new Location(fromX,fromY).equals(new Location(blackX[1],blackY[1]))){
                    blackX[1] = toX;
                    blackY[1] = toY;
                }else if(new Location(fromX,fromY).equals(new Location(blackX[6],blackY[6]))){
                    blackX[6] = toX;
                    blackY[6] = toY;
                }else{
                    System.err.println("No " + chess + " Found in this location.");
                    throw new CustomException.WrongChessException();
                }
                break;

            case 'B':
                if(new Location(fromX,fromY).equals(new Location(blackX[2],blackY[2]))){
                    blackX[2] = toX;
                    blackY[2] = toY;
                }else if(new Location(fromX,fromY).equals(new Location(blackX[5],blackY[5]))){
                    blackX[5] = toX;
                    blackY[5] = toY;
                }else{
                    System.err.println("No " + chess + " Found in this location.");
                    throw new CustomException.WrongChessException();
                }
                break;

            case 'Q':
                if(new Location(fromX,fromY).equals(new Location(blackX[3],blackY[3]))){
                    blackX[3] = toX;
                    blackY[3] = toY;
                }else{
                    System.err.println("No " + chess + " Found in this location.");
                    throw new CustomException.WrongChessException();
                }
                break;

            case 'K':
                if(new Location(fromX,fromY).equals(new Location(blackX[4],blackY[4]))){
                    blackX[4] = toX;
                    blackY[4] = toY;
                }else{
                    System.err.println("No " + chess + " Found in this location.");
                    throw new CustomException.WrongChessException();
                }
                break;

            case 'P':
                boolean found = false;
                for(int i=8;i<16;i++){
                    if(new Location(fromX,fromY).equals(new Location(blackX[i],blackY[i]))){
                        found = true;
                        blackX[i] = toX;
                        blackY[i] = toY;
                        break;
                    }
                }
                if(!found){
                    System.err.println("No " + chess + " Found in this location.");
                    throw new CustomException.WrongChessException();
                }
                break;

            case 'r':
                if(new Location(fromX,fromY).equals(new Location(whiteX[0], whiteY[0]))){
                    whiteX[0] = toX;
                    whiteY[0] = toY;
                }else if(new Location(fromX,fromY).equals(new Location(whiteX[7], whiteY[7]))){
                    whiteX[7] = toX;
                    whiteY[7] = toY;
                }else{
                    System.err.println("No " + chess + " Found in this location.");
                    throw new CustomException.WrongChessException();
                }
                break;

            case 'n':
                if(new Location(fromX,fromY).equals(new Location(whiteX[1],whiteY[1]))){
                    whiteX[1] = toX;
                    whiteY[1] = toY;
                }else if(new Location(fromX,fromY).equals(new Location(whiteX[6],whiteY[6]))){
                    whiteX[6] = toX;
                    whiteY[6] = toY;
                }else{
                    System.err.println("No " + chess + " Found in this location.");
                    throw new CustomException.WrongChessException();
                }
                break;

            case 'b':
                if(new Location(fromX,fromY).equals(new Location(whiteX[2],whiteY[2]))){
                    whiteX[2] = toX;
                    whiteY[2] = toY;
                }else if(new Location(fromX,fromY).equals(new Location(whiteX[5],whiteY[5]))){
                    whiteX[5] = toX;
                    whiteY[5] = toY;
                }else{
                    System.err.println("No " + chess + " Found in this location.");
                    throw new CustomException.WrongChessException();
                }
                break;

            case 'q':
                if(new Location(fromX,fromY).equals(new Location(whiteX[3],whiteY[3]))){
                    whiteX[3] = toX;
                    whiteY[3] = toY;
                }else{
                    System.err.println("No " + chess + " Found in this location.");
                    throw new CustomException.WrongChessException();
                }
                break;

            case 'k':
                if(new Location(fromX,fromY).equals(new Location(whiteX[4],whiteY[4]))){
                    whiteX[4] = toX;
                    whiteY[4] = toY;
                }else{
                    System.err.println("No " + chess + " Found in this location.");
                    throw new CustomException.WrongChessException();
                }
                break;

            case 'p':
                found = false;
                for(int i=8;i<16;i++){
                    if(new Location(fromX,fromY).equals(new Location(whiteX[i],whiteY[i]))){
                        found = true;
                        whiteX[i] = toX;
                        whiteY[i] = toY;
                        break;
                    }
                }
                if(!found){
                    System.err.println("No " + chess + " Found in this location.");
                    throw new CustomException.WrongChessException();
                }
                break;
        }

        //mark some chess as moved if possible
        switch(chess){
            case 'K':
                hasMoved[0] = true;
                break;

            case 'k':
                hasMoved[3] = true;
                break;

            case 'R':
                if(fromX == 0 && fromY == 0) hasMoved[1] = true;  //left black
                if(fromX == 7 && fromY == 0) hasMoved[2] = true;  //right black
                break;

            case 'r':
                if(fromX == 0 && fromY == 7) hasMoved[4] = true;  //left white
                if(fromX == 7 && fromY == 7) hasMoved[5] = true;  //right white
                break;
        }

    }


    /**
     * Put all chess on the board
     */
    private void initBoard(){

        //initial the state of all kings and rooks
        hasMoved = new boolean[6];
        for(int i=0;i<6;i++){
            hasMoved[i]=false;
        }

        //clear taken list
        takenChess.clear();

        //all space
        for(int i=0;i<8;i++)
            for(int j=0;j<8;j++)
                board[i][j] = '-';

        //black
        board[0][0] = 'R';
        board[1][0] = 'N';
        board[2][0] = 'B';
        board[3][0] = 'Q';
        board[4][0] = 'K';
        board[5][0] = 'B';
        board[6][0] = 'N';
        board[7][0] = 'R';
        for(int i=0;i<8;i++)
            board[i][1] = 'P';
        for(int i=0;i<8;i++){
            blackX[i] = i;
            blackY[i] = 0;
            blackX[i+8] = i;  //pawn
            blackY[i+8] = 1;
        }

        //white
        board[0][7] = 'r';
        board[1][7] = 'n';
        board[2][7] = 'b';
        board[3][7] = 'q';
        board[4][7] = 'k';
        board[5][7] = 'b';
        board[6][7] = 'n';
        board[7][7] = 'r';
        for(int i=0;i<8;i++)
            board[i][6] = 'p';
        for(int i=0;i<8;i++){
            whiteX[i] = i;
            whiteY[i] = 7;
            whiteX[i+8] = i;  //pawn
            whiteY[i+8] = 6;
        }
    }

    /**
     * Get the location lists
     * @return corresponding location list
     */
    public int[] getBlackX(){return blackX;}
    public int[] getBlackY(){return blackY;}
    public int[] getWhiteX(){return whiteX;}
    public int[] getWhiteY(){return whiteY;}

    /**
     * Get taken chess list
     * @return taken chess list
     */
    public ArrayList<Character> getTakenChess(){return takenChess;}

    /**
     * Print the board into Console
     * @param b the board which is going to be printed
     */
    public static void printBoard(Board b){
        char[][] board = b.getBoard();
        for(int j=0;j<8;j++){
            String line="";
            for(int i=0;i<8;i++){
                line += board[i][j];
                if(i != 7) line += " ";
            }
            System.out.println(line);
        }
    }

    /**
     * Check if a location has chess
     * @param currentTable current state of the board
     * @param x location x
     * @param y location y
     * @return true if has chess, false if not or out of the board
     */
    public static boolean hasChess(char[][] currentTable, int x, int y){
        if(isOutOfBoard(x,y)) return false;
        return currentTable[x][y] != '-';
    }
    public static boolean hasChess(char[][] currentTable, Location location){
        return hasChess(currentTable,location.x,location.y);
    }

    /**
     * Check if a location is out of the board
     * @param x location x
     * @param y location y
     * @return true if outside of the board, false if not
     */
    public static boolean isOutOfBoard(int x, int y){
        return (x<0 || y<0 || x>7 || y>7);
    }
    public static boolean isOutOfBoard(Location l){
        return isOutOfBoard(l.x, l.y);
    }
}
