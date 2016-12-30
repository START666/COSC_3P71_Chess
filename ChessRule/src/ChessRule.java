import java.util.ArrayList;

/**
 * Created by Xuhao Chen on 2016/12/29.
 */
public class ChessRule {
    public ChessRule(){
        char[][] board = new char[8][8];
        initBoard(board);

//        board[1][6]='-';
//        board[1][2]='p';

        printBoard(board);
//        ArrayList<Location> list = new ArrayList<>();
//        try {
//            list = validMove(board,'p',new Location(2,6));
//        } catch (WrongChessException e) {
//            e.printStackTrace();
//        }
//        printLocationList(list);

    }

    //TEST ONLY
    private void printLocationList(ArrayList<Location> list){
        for(Location location : list){
            System.out.println("("+location.x+", "+location.y+")");
        }
    }

    public static void main(String[] args){
        new ChessRule();
    }

    /**
     * Put all chess on the borad
     * @param board the board which is going to be initialized
     */
    public static void initBoard(char[][] board){
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
     * @param board the board which is going to be printed
     */
    public static void printBoard(char[][] board){
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
     * K - King 王
     * Q - Queen 后
     * B - Bishop 象
     * N - Knight 马
     * R - Rook 车
     * P - Pawn 兵
     * upper case - black
     * lower case - white
     * @param currentTable current state of the board
     * @param chess current chosen chess
     * @param location location of current chosen chess
     * @return list of valid locations of next move
     * @throws WrongChessException when chess is not either upper or lower case or not a valid chess char
     */
    public static ArrayList<Location> validMove(char[][] currentTable, char chess, Location location) throws WrongChessException{

        boolean isUpperCase = false;

        //if chess is not either upper or lower case
        if( (!Character.isUpperCase(chess)) && (!Character.isLowerCase(chess)) ){
            throw new WrongChessException();
        }

        //determine if chess is upper or lower case
        isUpperCase = Character.isUpperCase(chess);

        //different chess have different moving rules
        switch(chess){
            case 'k':
            case 'K':
                return kingMove(isUpperCase,currentTable,location);

            case 'q':
            case 'Q':
                return queenMove(isUpperCase,currentTable,location);

            case 'b':
            case 'B':
                return bishopMove(isUpperCase,currentTable,location);

            case 'n':
            case 'N':
                return knightMove(isUpperCase,currentTable,location);

            case 'r':
            case 'R':
                return rookMove(isUpperCase,currentTable,location);

            case 'p':
            case 'P':
                return pawnMove(isUpperCase,currentTable,location);

            default:
                throw new WrongChessException();
        }

    }
    public static class WrongChessException extends Exception{}

    private static ArrayList<Location> kingMove(boolean isUpperCase, char[][] currentTable, Location location){

        return null;
    }

    private static ArrayList<Location> queenMove(boolean isUpperCase, char[][] currentTable, Location location){

        return null;
    }

    private static ArrayList<Location> bishopMove(boolean isUpperCase, char[][] currentTable, Location location){

        return null;
    }

    private static ArrayList<Location> knightMove(boolean isUpperCase, char[][] currentTable, Location location){

        return null;
    }

    private static ArrayList<Location> rookMove(boolean isUpperCase, char[][] currentTable, Location location){

        return null;
    }

    /**
     * Pawn can move to front only, first move can be distance of 2, others only 1
     * Pawn can only to take the chess in 2 closet front diagonal locations
     * @param isUpperCase upper case means black, lower case means white
     * @param currentTable current state of the board
     * @param location location of the pawn
     * @return the list of valid moves
     * @throws WrongChessException when there is no pawn in this location
     */
    private static ArrayList<Location> pawnMove(boolean isUpperCase, char[][] currentTable, Location location) throws WrongChessException {
        ArrayList<Location> validMove = new ArrayList<>();
        boolean isNeverMove;
        int nextDeltaY;

        if(isUpperCase){  //black
            //check if it has moved
            isNeverMove = (location.y == 1);
            nextDeltaY = 1;  //move down

            //check validation
            if(currentTable[location.x][location.y] != 'P'){
                System.err.println("No Black Pawn in " + location.x + ", " + location.y);
                throw new WrongChessException();
            }
        }else{  //white
            //check if it has moved
            isNeverMove = (location.y == 6);
            nextDeltaY = -1;  //move up

            //check validation
            if(currentTable[location.x][location.y] != 'p'){
                System.err.println("No White Pawn in " + location.x + ", " + location.y);
                throw new WrongChessException();
            }
        }

        //normal move, 1 distance move
        Location tmp = new Location(location.x, location.y + nextDeltaY);
        if( (!hasChess(currentTable,tmp)) && (!isOutOfBoard(tmp)) ){
            validMove.add(tmp);  //if no chess in there and not outside of the board, then add to valid move list

            //first move, 2 distance move
            if(isNeverMove){
                tmp = new Location(location.x,location.y + nextDeltaY * 2);
                if( (!hasChess(currentTable,tmp)) && (!isOutOfBoard(tmp)) )  validMove.add(tmp);  //if no chess in there and not outside of the board, then add to valid move list
            }
        }

        //to take, 1 distance diagonal
        //left front
        tmp = new Location(location.x - 1,location.y + nextDeltaY);
        if(hasChess(currentTable,tmp)){  //if has chess at left front
            if( Character.isUpperCase(currentTable[tmp.x][tmp.y]) != isUpperCase )  //if its opponent's chess
                validMove.add(tmp);  //add to valid move list
        }
        //right front
        tmp = new Location(location.x + 1,location.y + nextDeltaY);
        if(hasChess(currentTable,tmp)){  //if has chess at right front
            if( Character.isUpperCase(currentTable[tmp.x][tmp.y]) != isUpperCase )  //if its opponent's chess
                validMove.add(tmp);  //add to valid move list
        }

        return validMove;
    }

    /**
     * Check if a location has chess
     * @param currentTable current state of the board
     * @param x location x
     * @param y location y
     * @return true if has chess, false if not or out of the board
     */
    private static boolean hasChess(char[][] currentTable, int x, int y){
        if(isOutOfBoard(x,y)) return false;
        return currentTable[x][y] != '-';
    }
    private static boolean hasChess(char[][] currentTable, Location location){
        return hasChess(currentTable,location.x,location.y);
    }

    /**
     * Check if a location is out of the board
     * @param x location x
     * @param y location y
     * @return true if outside of the board, false if not
     */
    private static boolean isOutOfBoard(int x, int y){
        return (x<0 || y<0 || x>7 || y>7);
    }
    private static boolean isOutOfBoard(Location l){
        return isOutOfBoard(l.x, l.y);
    }

}
