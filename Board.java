/**
 * Board Class
 *
 * @KLee
 * @1.3.20
 */
public class Board
{
    public static void setBlankBoard(String[][] board)
    {
        for(int r = 0; r<board.length; r++)
        {
            for(int c = 0; c<board[r].length; c++)
            {
                board[r][c] = "▢";
            }
        }
    }

    public static void display(String[][] board)
    {
        System.out.print("\n\t");
        for(int r = 0; r<board.length; r++)
        {
            System.out.print((char)(r+65) + "\t");
        }
        System.out.print("\n -----------------------------------------------------------------------------------");
        for(int r = 0; r<board.length; r++)
        {
            System.out.print("\n\n" + (r+1) + "|\t");
            for(int c = 0; c<board[r].length; c++)
            {
                System.out.print(board[r][c] + "\t");
            }
        }
        System.out.println();
    }

    public static void changeIndex(String[][] board, int r, int c, String symbol)
    {
        board[r-1][c-1] = symbol;
    }

    public static boolean checkIndex(String[][]board, int r, int c, String symbol)
    {
        return board[r-1][c-1].equals(symbol)?true:false;
    }

    public static void placeShip(String[] firstPosCoords, int shipLength, String direction, String[][]shipBoard, String symbol)
    {
        if(direction.equals("u"))
        {
            for(int i = 0; i < shipLength; i++)
            {
                changeIndex(shipBoard,Integer.parseInt(firstPosCoords[0])-i, Integer.parseInt(firstPosCoords[1]), symbol);
            }
        }
        if(direction.equals("d"))
        {
            for(int i = 0; i < shipLength; i++)
            {
                changeIndex(shipBoard,Integer.parseInt(firstPosCoords[0])+i, Integer.parseInt(firstPosCoords[1]), symbol);
            }
        }
        if(direction.equals("l"))
        {
            for(int i = 0; i < shipLength; i++)
            {
                changeIndex(shipBoard,Integer.parseInt(firstPosCoords[0]), Integer.parseInt(firstPosCoords[1]) - i, symbol);
            }
        }
        if(direction.equals("r"))
        {
            for(int i = 0; i < shipLength; i++)
            {
                changeIndex(shipBoard,Integer.parseInt(firstPosCoords[0]), Integer.parseInt(firstPosCoords[1]) + i, symbol);    
            }
        }
    }
}
