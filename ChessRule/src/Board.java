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

    private ArrayList<Character> takenChess;

    public Board(){
        board = new char[8][8];
        takenChess = new ArrayList<>();
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
     * move chess from a location to a location, no rule check
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

        if(hasChess(board,toX,toY)){  //has chess there
            if( Character.isUpperCase(board[toX][toY]) == Character.isUpperCase(chess) ) throw new CustomException.CannotTakeOwnChessException();  //has own chess
            takenChess.add(board[toX][toY]);
        }
        board[fromX][fromY] = '-';
        board[toX][toY] = chess;

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

    }

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
