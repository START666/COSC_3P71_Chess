import java.util.ArrayList;

/**
 * Created by Xuhao Chen on 2016/12/29.
 */
public class ChessRule {

    public ChessRule(){
        Board board = new Board();

        try {

            board.moveChess('p',0,6,0,3);

            Board.printBoard(board);
            ArrayList<Location> list = validMove(board,'N',new Location(1,0));
            printLocationList(list);

            board.moveChess('N',new Location(1,0),list.get(0));
            Board.printBoard(board);
            list = validMove(board,'N',list.get(0));
            printLocationList(list);

        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    //DEBUG TEST ONLY
    private void printLocationList(ArrayList<Location> list){
        for(Location location : list){
            System.out.println("("+location.x+", "+location.y+")");
        }
    }

    public static void main(String[] args){
        new ChessRule();
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
     * @param currentBoard current state of the board
     * @param chess current chosen chess
     * @param location location of current chosen chess
     * @return list of valid locations of next move
     * @throws CustomException.WrongChessException when chess is not either upper or lower case or not a valid chess char
     */
    public static ArrayList<Location> validMove(Board currentBoard, char chess, Location location) throws CustomException.WrongChessException{

        boolean isUpperCase = false;

        //if chess is not either upper or lower case
        if( (!Character.isUpperCase(chess)) && (!Character.isLowerCase(chess)) ){
            throw new CustomException.WrongChessException();
        }

        //determine if chess is upper or lower case
        isUpperCase = Character.isUpperCase(chess);

        //different chess have different moving rules
        switch(chess){
            case 'k':
            case 'K':
                return kingMove(isUpperCase,currentBoard,location);

            case 'q':
            case 'Q':
                return queenMove(isUpperCase,currentBoard,location);

            case 'b':
            case 'B':
                return bishopMove(isUpperCase,currentBoard,location);

            case 'n':
            case 'N':
                return knightMove(isUpperCase,currentBoard,location);

            case 'r':
            case 'R':
                return rookMove(isUpperCase,currentBoard,location);

            case 'p':
            case 'P':
                return pawnMove(isUpperCase,currentBoard,location);

            default:
                throw new CustomException.WrongChessException();
        }

    }

    private static ArrayList<Location> kingMove(boolean isUpperCase, Board currentBoard, Location location) throws CustomException.WrongChessException{

        return null;
    }

    private static ArrayList<Location> queenMove(boolean isUpperCase, Board currentBoard, Location location) throws CustomException.WrongChessException{

        return null;
    }

    private static ArrayList<Location> bishopMove(boolean isUpperCase, Board currentBoard, Location location) throws CustomException.WrongChessException{

        return null;
    }

    /**
     * Knight(x,y) can move to (x+1,y+2) (x+1,y-2) (x-1,y+2) (x-1,y-2) (x+2,y+1) (x+2,y-1) (x-2,y+1) (x-2,y-1)
     * @param isUpperCase upper case means black, lower case means white
     * @param currentBoard current state of the board
     * @param location location of the Knight
     * @return the list of valid moves
     * @throws CustomException.WrongChessException when there is no Knight in this location
     */
    private static ArrayList<Location> knightMove(boolean isUpperCase, Board currentBoard, Location location) throws CustomException.WrongChessException{
        ArrayList<Location> validMove = new ArrayList<>();
        Location tmp = new Location(0,0);

        //check validation
        if(isUpperCase){  //black
            if(currentBoard.getBoard()[location.x][location.y] != 'N'){
                System.err.println("No Black Knight in " + location.x + ", " + location.y);
                throw new CustomException.WrongChessException();
            }
        }else{  //white
            if(currentBoard.getBoard()[location.x][location.y] != 'n'){
                System.err.println("No White Knight in " + location.x + ", " + location.y);
                throw new CustomException.WrongChessException();
            }
        }

        //check each location
        //(x+1, y+2)
        addValidLocationToKnightValidMove(validMove,currentBoard.getBoard(),isUpperCase,new Location(location.x+1,location.y+2));
        //(x+1, y-2)
        addValidLocationToKnightValidMove(validMove,currentBoard.getBoard(),isUpperCase,new Location(location.x+1,location.y-2));
        //(x-1, y+2)
        addValidLocationToKnightValidMove(validMove,currentBoard.getBoard(),isUpperCase,new Location(location.x-1,location.y+2));
        //(x-1, y-2)
        addValidLocationToKnightValidMove(validMove,currentBoard.getBoard(),isUpperCase,new Location(location.x-1,location.y-2));
        //(x+2, y+1)
        addValidLocationToKnightValidMove(validMove,currentBoard.getBoard(),isUpperCase,new Location(location.x+2,location.y+1));
        //(x+2, y-1)
        addValidLocationToKnightValidMove(validMove,currentBoard.getBoard(),isUpperCase,new Location(location.x+2,location.y-1));
        //(x-2, y+1)
        addValidLocationToKnightValidMove(validMove,currentBoard.getBoard(),isUpperCase,new Location(location.x-2,location.y+1));
        //(x-2, y-1)
        addValidLocationToKnightValidMove(validMove,currentBoard.getBoard(),isUpperCase,new Location(location.x-2,location.y-1));

        return validMove;
    }
    /**
     * To support knightMove method
     * Check if a location is valid for Knight to Move
     * @param validMove Pointer to the validMove list
     * @param currentTable current state of the board
     * @param isUpperCase used to check if it is opponent's chess
     * @param l Location which is going to be added
     */
    private static void addValidLocationToKnightValidMove(ArrayList<Location> validMove, char[][] currentTable, boolean isUpperCase, Location l){
        if(!Board.isOutOfBoard(l)){  //it is inside
            if(!Board.hasChess(currentTable,l)){  //no chess
                validMove.add(l);
            }else{  //has chess
                if(Character.isUpperCase(currentTable[l.x][l.y]) != isUpperCase){    //if it is opponent's chess
                    validMove.add(l);
                }
            }
        }
    }

    /**
     * Rook can move through x-axis and y-axis unless there is a chess in the route
     * Rook can take the nearest chess in the moving route
     * @param isUpperCase upper case means black, lower case means white
     * @param currentBoard current state of the board
     * @param location location of the Rook
     * @return the list of valid moves
     * @throws CustomException.WrongChessException when there is no Rook in this location
     */
    private static ArrayList<Location> rookMove(boolean isUpperCase, Board currentBoard, Location location) throws CustomException.WrongChessException {
        ArrayList<Location> validMove = new ArrayList<>();
        Location tmp = new Location(0,0);

        //check validation
        if(isUpperCase) {  //black
            if(currentBoard.getBoard()[location.x][location.y] != 'R'){
                System.err.println("No Black Rook in " + location.x + ", " + location.y);
                throw new CustomException.WrongChessException();
            }
        }else{  //white
            if(currentBoard.getBoard()[location.x][location.y] != 'r'){
                System.err.println("No White Rook in " + location.x + ", " + location.y);
                throw new CustomException.WrongChessException();
            }
        }

        //x-axis move
        for(int i=1;i<=8;i++){
            tmp = new Location(location.x + i,location.y);  //moving x + i
            if(Board.hasChess(currentBoard.getBoard(),tmp) || Board.isOutOfBoard(tmp)) break; //if has chess or outside of the board, then break
            validMove.add(tmp);  //else add to valid move list
        }
        if(!Board.isOutOfBoard(tmp)){
            if( Character.isUpperCase(currentBoard.getBoard()[tmp.x][tmp.y]) != isUpperCase ) validMove.add(tmp);  //if this is opponent's chess, then it can be taken
        }
        for(int i=1;i<=8;i++){
            tmp = new Location(location.x-i,location.y);  //moving x - i
            if(Board.hasChess(currentBoard.getBoard(),tmp) || Board.isOutOfBoard(tmp)) break;  //if has chess or outside of the board, then break
            validMove.add(tmp);
        }
        if(!Board.isOutOfBoard(tmp)){
            if( Character.isUpperCase(currentBoard.getBoard()[tmp.x][tmp.y]) != isUpperCase ) validMove.add(tmp);  //if this is opponent's chess, then it can be taken
        }

        //y-axis move
        for(int i=1;i<=8;i++){
            tmp = new Location(location.x,location.y + i);  //moving y + i
            if(Board.hasChess(currentBoard.getBoard(),tmp) || Board.isOutOfBoard(tmp)) break;  //if has chess or outside of the board, then break
            validMove.add(tmp);  //else add to valid move list
        }
        if(!Board.isOutOfBoard(tmp)){
            if( Character.isUpperCase(currentBoard.getBoard()[tmp.x][tmp.y]) != isUpperCase ) validMove.add(tmp);  //if this is opponent's chess, then it can be taken
        }
        for(int i=1;i<=8;i++){
            tmp = new Location(location.x,location.y - i);  //moving y - i
            if(Board.hasChess(currentBoard.getBoard(),tmp) || Board.isOutOfBoard(tmp)) break;  //if has chess or outside of the board, then break
            validMove.add(tmp);  //else add to valid move list
        }
        if(!Board.isOutOfBoard(tmp)){
            if( Character.isUpperCase(currentBoard.getBoard()[tmp.x][tmp.y]) != isUpperCase ) validMove.add(tmp);  //if this is opponent's chess, then it can be taken
        }

        return validMove;
    }

    /**
     * Pawn can move to front only, first move can be distance of 2, others only 1
     * Pawn can only to take the chess in 2 closet front diagonal locations
     * @param isUpperCase upper case means black, lower case means white
     * @param currentBoard current state of the board
     * @param location location of the Pawn
     * @return the list of valid moves
     * @throws CustomException.WrongChessException when there is no pawn in this location
     */
    private static ArrayList<Location> pawnMove(boolean isUpperCase, Board currentBoard, Location location) throws CustomException.WrongChessException {
        ArrayList<Location> validMove = new ArrayList<>();
        boolean isNeverMove;
        int nextDeltaY;

        char[][] currentTable = currentBoard.getBoard();

        if(isUpperCase){  //black
            //check if it has moved
            isNeverMove = (location.y == 1);
            nextDeltaY = 1;  //move down

            //check validation
            if(currentTable[location.x][location.y] != 'P'){
                System.err.println("No Black Pawn in " + location.x + ", " + location.y);
                throw new CustomException.WrongChessException();
            }
        }else{  //white
            //check if it has moved
            isNeverMove = (location.y == 6);
            nextDeltaY = -1;  //move up

            //check validation
            if(currentTable[location.x][location.y] != 'p'){
                System.err.println("No White Pawn in " + location.x + ", " + location.y);
                throw new CustomException.WrongChessException();
            }
        }

        //normal move, 1 distance move
        Location tmp = new Location(location.x, location.y + nextDeltaY);
        if( (!Board.hasChess(currentTable,tmp)) && (!Board.isOutOfBoard(tmp)) ){
            validMove.add(tmp);  //if no chess in there and not outside of the board, then add to valid move list

            //first move, 2 distance move
            if(isNeverMove){
                tmp = new Location(location.x,location.y + nextDeltaY * 2);
                if( (!Board.hasChess(currentTable,tmp)) && (!Board.isOutOfBoard(tmp)) )  validMove.add(tmp);  //if no chess in there and not outside of the board, then add to valid move list
            }
        }

        //to take, 1 distance diagonal
        //left front
        tmp = new Location(location.x - 1,location.y + nextDeltaY);
        if(Board.hasChess(currentTable,tmp)){  //if has chess at left front
            if( Character.isUpperCase(currentTable[tmp.x][tmp.y]) != isUpperCase )  //if its opponent's chess
                validMove.add(tmp);  //add to valid move list
        }
        //right front
        tmp = new Location(location.x + 1,location.y + nextDeltaY);
        if(Board.hasChess(currentTable,tmp)){  //if has chess at right front
            if( Character.isUpperCase(currentTable[tmp.x][tmp.y]) != isUpperCase )  //if its opponent's chess
                validMove.add(tmp);  //add to valid move list
        }

        return validMove;
    }



}
