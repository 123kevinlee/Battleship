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

    public static void display(String[][] board, boolean showKey)
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

            if(showKey)
            {
                if(r==3){System.out.print("Symbol Key");}
                if(r==4){System.out.print("▢ = Empty Space");}
                if(r==5){System.out.print("X = Hit / O = Miss");}
                if(r==6){System.out.print("C/D/B = Ships / F = Dead Ships");}
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

    public static void deleteShip(String[][] shipBoard, String shipSymbol)
    {
        for(int r = 0; r<shipBoard.length; r++)
        {
            for(int c = 0; c<shipBoard[0].length; c++)
            {
                if(shipBoard[r][c].equals(shipSymbol))
                {
                    shipBoard[r][c] = "▢";
                }
            }
        }
    }

    public static boolean checkOneRemaining(String[][] shipBoard, String shipSymbol)
    {
        int remainingParts = 0;
        for(int r = 0; r<shipBoard.length; r++)
        {
            for(int c = 0; c<shipBoard[0].length; c++)
            {
                if(shipBoard[r][c].equals(shipSymbol))
                {
                    remainingParts++;
                }
            }
        }
        if(remainingParts == 1)
        {
            return true;
        }
        return false;
    }
    
    public static String[][] Arrayify (String str) {
            int row = 0;
            int col = 0;
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) == '[') {
                    row++;
                }
            }
            row--;
            for (int i = 0;; i++) {
                if (str.charAt(i) == ',') {
                    col++;
                }
                if (str.charAt(i) == ']') {
                    break;
                }
            }
            col++;

            String[][] out = new String[row][col];

            str = str.replaceAll("\\[", "").replaceAll("\\]", "");

            String[] s1 = str.split(", ");

            int j = -1;
            for (int i = 0; i < s1.length; i++) {
                if (i % col == 0) {
                    j++;
                }
                out[j][i % col] = s1[i];
            }
            return out;
        }
        
        public static int[] checkHitShip(String[][] oldShipBoard, String[][] newShipBoard)
        {
            for(int r = 0; r<oldShipBoard.length; r++)
            {
                for(int c = 0; c<oldShipBoard[r].length; c++)
                {
                    if(oldShipBoard[r][c] != newShipBoard[r][c])
                    {
                        return new int[]{r,c};
                    }
                }
            }
            return new int[]{-1,-1};
        }
}
