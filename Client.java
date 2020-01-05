/**
 * Client for Battleship games
 *
 * @KLee
 * @1.4.20
 */
import java.util.Scanner;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
public class Client
{
    public static String[][] shipBoard = new String[10][10];
    public static String[][] torpedoBoard = new String[10][10];
        
    public static void main() throws Exception
    {
        Scanner input = new Scanner(System.in);
        System.out.println("Server IP: ");
        String[] ip = input.nextLine().split(":");
        Socket socket = new Socket(ip[0], Integer.parseInt(ip[1]));
        Scanner serverIn = new Scanner(socket.getInputStream());
        PrintWriter clientOut = new PrintWriter(socket.getOutputStream(), true);
        play(socket, serverIn, clientOut);
    }

    private static void play(Socket socket, Scanner serverIn, PrintWriter clientOut) throws Exception
    {
        Scanner input = new Scanner(System.in);
        
        Board.setBlankBoard(shipBoard);
        Board.setBlankBoard(torpedoBoard);  

        String[][] shipBoardOp = new String[10][10];
        String[][] torpedoBoardOp = new String[10][10];

        try
        {
            while(serverIn.hasNextLine())
            {
                var response = serverIn.nextLine();
                if(response.startsWith("MESSAGE"))
                {
                    System.out.println(response.substring(8));
                }
                else if(response.startsWith("SETUP"))
                {
                    setupShips();
                    System.out.println(Arrays.deepToString(shipBoard));
                }
                else if(response.startsWith("TURN"))
                {
                    System.out.println("Your turn");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            socket.close();
        }
    }

    private static void setupShips()
    {   
        Scanner input = new Scanner(System.in);
        for(int ships = 0; ships<3; ships++)
        {
            setShip(ships);
        }      
        boolean satisfied = false;
        while(!satisfied)
        {
            System.out.println("\fHere is your final layout...");
            Board.display(shipBoard, true);
            System.out.print("Are you satisfied with this layout[y/n]: ");
            if(!input.nextLine().toLowerCase().equals("y"))
            {

                boolean validShip = false;
                while(!validShip)
                {
                    System.out.print("What ship do you want to change [1,2,3]: ");
                    String resp = input.nextLine();
                    if(resp.matches("^[1-3]{1}$"))
                    {
                        validShip = true;
                        switch (Integer.parseInt(resp)) {
                            case 1:  Board.deleteShip(shipBoard, "C");                            
                            break;
                            case 2:  Board.deleteShip(shipBoard, "D");                            
                            break;
                            case 3:  Board.deleteShip(shipBoard, "B");
                            break;
                        }
                        setShip(Integer.parseInt(resp)-1);
                    }
                    else
                    {
                        System.out.print("*That is not a ship - Please try again* ");
                    }
                }
            }
            else
            {
                satisfied = true;
            }
        }
    }

    private static void setShip(int shipNum)
    {
        Scanner input = new Scanner(System.in);
        String positionInputRegex = "^(([A-J]{1})||([a-j]{1}))+,+([1-9]{1}||10)$"; //returns true if first char is A-J or a-j, second char is a comma, and third char is 1-10
        System.out.println("\fBoard Setup");
        Board.display(shipBoard, true);

        int shipLength = 0;
        String symbol = "";
        switch (shipNum) {
            case 0:  //Cruiser
            shipLength = 4;
            symbol = "C";
            break;
            case 1:  //Destroyer
            shipLength = 4; 
            symbol = "D";
            break;
            case 2:  //Battleship
            shipLength = 5; 
            symbol = "B";
            break;
            default: //Rando
            shipLength = 3; 
            symbol = "U";
            break;
        }

        boolean validStartPosition = false;
        while(!validStartPosition)
        {
            switch (shipNum) {
                case 0:  System.out.print("Starting coordinate for the Cruiser Ex. letter,number: ");
                break;
                case 1:  System.out.print("Starting coordinate for the Destroyer Ex. letter,number: ");
                break;
                case 2:  System.out.print("Starting coordinate for the Battleship Ex. letter,number: ");
                break;
                default: System.out.print("Starting coordinate for ship number " + shipNum + " Ex. letter,number: ");
                break;
            }

            String position = input.nextLine();
            if(position.matches(positionInputRegex))
            {
                String[] firstPosCoords = position.split(",");
                String temp = firstPosCoords[1];
                firstPosCoords[1] = Integer.toString((int)(firstPosCoords[0].toUpperCase().charAt(0))-64);
                firstPosCoords[0] = temp;

                if(Board.checkIndex(shipBoard,Integer.parseInt(firstPosCoords[0]), Integer.parseInt(firstPosCoords[1]), "▢"))
                {
                    validStartPosition = true;
                    boolean validDirection = false;
                    while(!validDirection)
                    {
                        System.out.print("What direction do you want the ship to go[U,D,L,R]: ");
                        String direction = input.nextLine().toLowerCase();
                        if(direction.equals("u") || direction.equals("d") || direction.equals("l") || direction.equals("r"))
                        {
                            if(checkValidShipPos(firstPosCoords, shipLength, direction, shipBoard))
                            {
                                validDirection = true;
                                Board.placeShip(firstPosCoords, shipLength, direction, shipBoard, symbol);
                            }
                            else
                            {
                                System.out.print("*Ship cannot be placed that direction* Do you wish to change the start position[y/n]: ");
                                if(input.nextLine().toLowerCase().equals("y"))
                                {
                                    validStartPosition = false;
                                    break;
                                }
                            }
                        }
                        else
                        {
                            System.out.print("*Invalid direction input - Please try again* ");
                        }
                    }
                }
                else
                {
                    System.out.print("*Another ship is at that coordinate - Please try again* ");
                }
            }
            else
            {
                System.out.print("*Invalid coordinate input - Please try again* ");
            }
        }
    }

    private static boolean checkValidShipPos(String[] firstPosCoords, int length, String direction, String[][]board)
    {
        if(direction.equals("u"))
        {
            if(Integer.parseInt(firstPosCoords[0]) - length < 0)
            {
                return false;
            }
            boolean allEmpty = true;
            for(int i = 1; i<length; i++)
            {
                if(!Board.checkIndex(shipBoard,Integer.parseInt(firstPosCoords[0])-i, Integer.parseInt(firstPosCoords[1]), "▢"))
                {
                    allEmpty = false;
                }
            }
            if(allEmpty)
            {
                return true;
            }
        }
        if(direction.equals("d"))
        {
            if(Integer.parseInt(firstPosCoords[0]) + length > 11)
            {
                return false;
            }
            boolean allEmpty = true;
            for(int i = 1; i<length; i++)
            {
                if(!Board.checkIndex(shipBoard,Integer.parseInt(firstPosCoords[0])+i, Integer.parseInt(firstPosCoords[1]), "▢"))
                {
                    allEmpty = false;
                }
            }
            if(allEmpty)
            {
                return true;
            }
        }
        if(direction.equals("l"))
        {
            if(Integer.parseInt(firstPosCoords[1]) - length < 0)
            {
                return false;
            }
            boolean allEmpty = true;
            for(int i = 1; i<length; i++)
            {
                if(!Board.checkIndex(shipBoard,Integer.parseInt(firstPosCoords[0]), Integer.parseInt(firstPosCoords[1]) - i, "▢"))
                {
                    allEmpty = false;
                }
            }
            if(allEmpty)
            {
                return true;
            }
        }
        if(direction.equals("r"))
        {
            if(Integer.parseInt(firstPosCoords[1]) + length > 11)
            {
                return false;
            }
            boolean allEmpty = true;
            for(int i = 1; i<length; i++)
            {
                if(!Board.checkIndex(shipBoard,Integer.parseInt(firstPosCoords[0]), Integer.parseInt(firstPosCoords[1]) + i, "▢"))
                {
                    allEmpty = false;
                }
            }
            if(allEmpty)
            {
                return true;
            }
        }
        return false;
    }
}
