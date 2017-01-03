import java.util.ArrayList;

/**
 * Created by Xuhao Chen on 2016/12/29.
 */
public class ChessRule {

    public ChessRule(){
        Board board = new Board();

        try {
//            board.moveChess('N',1,0,0,2);
//            board.moveChess('P',3,1,3,3);
//            board.moveChess('B',2,0,6,4);
//            board.moveChess('Q',3,0,3,2);
//
//            board.moveChess('P',6,1,6,3);
//            board.moveChess('B',5,0,7,2);
//            board.moveChess('N',6,0,5,2);

//            board.moveChess('R',0,0,1,0);
//            board.moveChess('R',1,0,0,0);

            Board.printBoard(board);
            ArrayList<Location> list = validMove(board,'P',new Location(4,1));
            printLocationList(list);

            board.moveChess('P',new Location(4,1),list.get(1));

//            board.moveChess('K',4,0,2,0);  //long castling
//            board.moveChess('K',4,0,6,0);  //short castling

            Board.printBoard(board);

//
//            board.moveChess('B', 2,0,5,3);
//            Board.printBoard(board);
//            list = validMove(board,'B',new Location(5,3));
//            printLocationList(list);

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
                return kingMove(isUpperCase,currentBoard,location,true);

            case 'q':
            case 'Q':
                return queenMove(isUpperCase,currentBoard,location,true);

            case 'b':
            case 'B':
                return bishopMove(isUpperCase,currentBoard,location,true);

            case 'n':
            case 'N':
                return knightMove(isUpperCase,currentBoard,location,true);

            case 'r':
            case 'R':
                return rookMove(isUpperCase,currentBoard,location, true);

            case 'p':
            case 'P':
                return pawnMove(isUpperCase,currentBoard,location,true);

            default:
                throw new CustomException.WrongChessException();
        }

    }

    /**
     * King can move to the location which is next to the current one
     * King has a special move with Rook, Castling
     * @param isUpperCase upper case means black, lower case means white
     * @param currentBoard current state of the board
     * @param location location of the King
     * @param checkValid check if there is a King
     * @return the list of valid moves
     * @throws CustomException.WrongChessException when there is no King in this location
     */
    private static ArrayList<Location> kingMove(boolean isUpperCase, Board currentBoard, Location location, boolean checkValid) throws CustomException.WrongChessException{
        ArrayList<Location> validMove = new ArrayList<>();

        //check validation
        if(checkValid) {
            if (isUpperCase) {  //black
                if (!checkValidation('K', currentBoard, location)) throw new CustomException.WrongChessException();
            } else {  //white
                if (!checkValidation('k', currentBoard, location)) throw new CustomException.WrongChessException();
            }
        }

        Location tmp;
        //check each location
        for(int dx = -1; dx <= 1; dx++){
            for(int dy = -1; dy <= 1; dy++){
                if(dx==0 && dy==0) continue;
                tmp = new Location(location.x + dx,location.y + dy);
                if(!Board.isOutOfBoard(tmp)){  //not outside
                    if(!Board.hasChess(currentBoard.getBoard(),tmp)){  //no chess
                        validMove.add(tmp);
                    }else{  //has chess
                        if(Character.isUpperCase(currentBoard.getBoard()[tmp.x][tmp.y]) != isUpperCase) {  //opponent's chess
                            validMove.add(tmp);
                        }
                    }
                }
            }
        }

        //castling
        if(isUpperCase){  //black
            if(!currentBoard.hasBlackKingMoved()) {  //black king never moved
                //left rook never moved
                if(!currentBoard.hasBlackLeftRookMoved()){
                    boolean hasChess = false;
                    //check nothing b/t them
                    for(int i=1;i<4;i++){
                        if(Board.hasChess(currentBoard.getBoard(),i,0)){
                            hasChess = true;
                            break;
                        }
                    }
                    if(!hasChess){  //no chess b/t them
                       validMove.add(new Location(2,0)); //long castling
                    }
                }
                //right rook never moved
                if(!currentBoard.hasBlackRightRookMoved()){
                    if(!currentBoard.hasBlackRightRookMoved()){  //right rook never moved
                        boolean hasChess = false;
                        //check nothing b/t them
                        for(int i=5;i<7;i++){
                            if(Board.hasChess(currentBoard.getBoard(),i,0)){
                                hasChess = true;
                                break;
                            }
                        }
                        if(!hasChess){  //no chess b/t them
                            validMove.add(new Location(6,0));  //short castling
                        }
                    }
                }
            }
        }else{  //white
            if(!currentBoard.hasWhiteKingMoved()){  //white king never moved
                //left rook never moved
                if(!currentBoard.hasWhiteLeftRookMoved()){
                    boolean hasChess = false;
                    //check nothing b/t them
                    for(int i=1;i<4;i++){
                        if(Board.hasChess(currentBoard.getBoard(),i,7)){
                            hasChess = true;
                            break;
                        }
                    }
                    if(!hasChess){  //no chess b/t them
                        validMove.add(new Location(2,7));  //long castling
                    }
                }
                //right rook never moved
                if(!currentBoard.hasWhiteRightRookMoved()){
                    boolean hasChess = false;
                    //check nothing b/t them
                    for(int i=5;i<7;i++){
                        if(Board.hasChess(currentBoard.getBoard(),i,7)){
                            hasChess = true;
                            break;
                        }
                    }
                    if(!hasChess){//no chess b/t them
                        validMove.add(new Location(6,7));  //short castling
                    }
                }
            }
        }

        return validMove;
    }

    /**
     * Queen can move likes a combination of Bishop and Rook
     * @param isUpperCase upper case means black, lower case means white
     * @param currentBoard current state of the board
     * @param location location of the Queen
     * @param checkValid check if there is a Queen
     * @return the list of valid moves
     * @throws CustomException.WrongChessException when there is no Queen in this location
     */
    private static ArrayList<Location> queenMove(boolean isUpperCase, Board currentBoard, Location location, boolean checkValid) throws CustomException.WrongChessException{
        ArrayList<Location> validMove = new ArrayList<>();

        //check validation
        if(checkValid) {
            if (isUpperCase) {  //black
                if (!checkValidation('Q', currentBoard, location)) throw new CustomException.WrongChessException();
            } else {  //white
                if (!checkValidation('q', currentBoard, location)) throw new CustomException.WrongChessException();
            }
        }

        //move like a rook
        ArrayList<Location> validRook = rookMove(isUpperCase,currentBoard,location,false);
        //move like a bishop
        ArrayList<Location> validBishop = bishopMove(isUpperCase,currentBoard,location, false);

        //add them up
        for(Location l : validRook)
            validMove.add(l);
        for(Location l : validBishop)
            validMove.add(l);

        return validMove;
    }

    /**
     * Bishop can move along the diagonal line unless a chess in the route will stop its moving
     * @param isUpperCase upper case means black, lower case means white
     * @param currentBoard current state of the board
     * @param location location of the Bishop
     * @param checkValid check if there is a Bishop
     * @return the list of valid moves
     * @throws CustomException.WrongChessException when there is no Bishop in this location
     */
    private static ArrayList<Location> bishopMove(boolean isUpperCase, Board currentBoard, Location location, boolean checkValid) throws CustomException.WrongChessException{
        ArrayList<Location> validMove = new ArrayList<>();
        Location tmp = new Location(0,0);

        //check validation
        if(checkValid) {
            if (isUpperCase) {  //black
                if (!checkValidation('B', currentBoard, location)) throw new CustomException.WrongChessException();
            } else {  //white
                if (!checkValidation('b', currentBoard, location)) throw new CustomException.WrongChessException();
            }
        }

        //(+, +)
        for(int i=1;i<8;i++){
            tmp = new Location(location.x+i, location.y+i);
            if(Board.hasChess(currentBoard.getBoard(),tmp) || Board.isOutOfBoard(tmp)) break;  //if reach a chess or outside of the board, then break
            validMove.add(tmp);  //else add to valid move
        }
        if(!Board.isOutOfBoard(tmp)) {  //if tmp is not outside
            if (Character.isUpperCase(currentBoard.getBoard()[tmp.x][tmp.y]) != isUpperCase) {  //if it is opponent's chess
                validMove.add(tmp);
            }
        }

        //(-, -)
        for(int i=1;i<8;i++){
            tmp = new Location(location.x-i, location.y-i);
            if(Board.hasChess(currentBoard.getBoard(),tmp) || Board.isOutOfBoard(tmp)) break;  //if reach a chess or outside of the board, then break
            validMove.add(tmp);  //else add to valid move
        }
        if(!Board.isOutOfBoard(tmp)) {  //if tmp is not outside
            if (Character.isUpperCase(currentBoard.getBoard()[tmp.x][tmp.y]) != isUpperCase) {  //if it is opponent's chess
                validMove.add(tmp);
            }
        }

        //(+, -)
        for(int i=1;i<8;i++){
            tmp = new Location(location.x+i, location.y-i);
            if(Board.hasChess(currentBoard.getBoard(),tmp) || Board.isOutOfBoard(tmp)) break;  //if reach a chess or outside of the board, then break
            validMove.add(tmp);  //else add to valid move
        }
        if(!Board.isOutOfBoard(tmp)) {  //if tmp is not outside
            if (Character.isUpperCase(currentBoard.getBoard()[tmp.x][tmp.y]) != isUpperCase) {  //if it is opponent's chess
                validMove.add(tmp);
            }
        }

        //(-, +)
        for(int i=1;i<8;i++){
            tmp = new Location(location.x-i, location.y+i);
            if(Board.hasChess(currentBoard.getBoard(),tmp) || Board.isOutOfBoard(tmp)) break;  //if reach a chess or outside of the board, then break
            validMove.add(tmp);  //else add to valid move
        }
        if(!Board.isOutOfBoard(tmp)) {  //if tmp is not outside
            if (Character.isUpperCase(currentBoard.getBoard()[tmp.x][tmp.y]) != isUpperCase) {  //if it is opponent's chess
                validMove.add(tmp);
            }
        }

        return validMove;
    }

    /**
     * Knight(x,y) can move to (x+1,y+2) (x+1,y-2) (x-1,y+2) (x-1,y-2) (x+2,y+1) (x+2,y-1) (x-2,y+1) (x-2,y-1)
     * @param isUpperCase upper case means black, lower case means white
     * @param currentBoard current state of the board
     * @param location location of the Knight
     * @param checkValid check if there is a Knight
     * @return the list of valid moves
     * @throws CustomException.WrongChessException when there is no Knight in this location
     */
    private static ArrayList<Location> knightMove(boolean isUpperCase, Board currentBoard, Location location, boolean checkValid) throws CustomException.WrongChessException{
        ArrayList<Location> validMove = new ArrayList<>();
        Location tmp = new Location(0,0);

        //check validation
        if(checkValid) {
            if (isUpperCase) {  //black
                if (!checkValidation('N', currentBoard, location)) throw new CustomException.WrongChessException();
            } else {  //white
                if (!checkValidation('n', currentBoard, location)) throw new CustomException.WrongChessException();
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
     * @param checkValid check if there is a Rook
     * @return the list of valid moves
     * @throws CustomException.WrongChessException when there is no Rook in this location
     */
    private static ArrayList<Location> rookMove(boolean isUpperCase, Board currentBoard, Location location, boolean checkValid) throws CustomException.WrongChessException {
        ArrayList<Location> validMove = new ArrayList<>();
        Location tmp = new Location(0,0);

        //check validation
        if(checkValid) {
            if (isUpperCase) {  //black
                if(!checkValidation('R', currentBoard, location)) throw new CustomException.WrongChessException();
            } else {  //white
                if(!checkValidation('r', currentBoard, location)) throw new CustomException.WrongChessException();
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
     * @param checkValid check if there is a Pawn
     * @return the list of valid moves
     * @throws CustomException.WrongChessException when there is no pawn in this location
     */
    private static ArrayList<Location> pawnMove(boolean isUpperCase, Board currentBoard, Location location, boolean checkValid) throws CustomException.WrongChessException {
        ArrayList<Location> validMove = new ArrayList<>();
        boolean isNeverMove;
        int nextDeltaY;

        char[][] currentTable = currentBoard.getBoard();

        if(isUpperCase){  //black
            //check if it has moved
            isNeverMove = (location.y == 1);
            nextDeltaY = 1;  //move down

            //check validation
            if(checkValid)
                if (!checkValidation('P', currentBoard, location)) throw new CustomException.WrongChessException();
        }else{  //white
            //check if it has moved
            isNeverMove = (location.y == 6);
            nextDeltaY = -1;  //move up

            //check validation
            if(checkValid)
                if (!checkValidation('p', currentBoard, location)) throw new CustomException.WrongChessException();
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

    private static boolean checkValidation(char chess, Board currentBoard, Location location){
        if(currentBoard.getBoard()[location.x][location.y] != chess){
            System.err.println("No "+ chess +" in " + location.x + ", " + location.y);
            return false;
        }
        return true;
    }


}
