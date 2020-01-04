/**
 * Player Class
 *
 * @KLee
 * @1.3.20
 */
import java.util.Scanner;
public class Player
{
    private String name;
    private String[][] shipBoard = new String[10][10];
    public String[][] getShipBoard(){return shipBoard;}
    private String[][] torpedoBoard = new String[10][10];
    private Player opponent;
    public void setOpponent(Player opponent){this.opponent = opponent;}
    public Player(String name)
    {
        this.name = name;
        Board.setBlankBoard(shipBoard);
        Board.setBlankBoard(torpedoBoard);  
        setupShips();
    }

    private void setupShips()
    {    
        Scanner input = new Scanner(System.in);
        String positionInputRegex = "^(([A-J]{1})||([a-j]{1}))+,+([1-9]{1}||10)$";

        for(int ships = 0; ships<3; ships++)
        {
            System.out.println("\f" + name + " Board Setup");
            Board.display(shipBoard);
            int shipLength = 0;
            String symbol = "";
            switch (ships) {
                case 0:  
                    shipLength = 4;
                    symbol = "C";
                    break;
                case 1:  
                    shipLength = 4; 
                    symbol = "D";
                    break;
                case 2:  
                    shipLength = 5; 
                    symbol = "B";
                    break;
                default: 
                    shipLength = 3; 
                    symbol = "U";
                    break;
            }

            boolean validStartPosition = false;
            while(!validStartPosition)
            {
                switch (ships) {
                    case 0:  System.out.print("Starting coordinate for the Cruiser Ex. letter,number: ");
                    break;
                    case 1:  System.out.print("Starting coordinate for the Destroyer Ex. letter,number: ");
                    break;
                    case 2:  System.out.print("Starting coordinate for the Battleship Ex. letter,number: ");
                    break;
                    default: System.out.print("Starting coordinate for ship number " + ships + " Ex. letter,number: ");
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
                //Ask for start pos and error check
                //ask for direction and error check
                //calculate positions and set them to respective ship pos arrays
                //create a ship for each one
            }
        }
        System.out.println("\fHere is your final ship board");
        Board.display(shipBoard);
    }

    private boolean checkValidShipPos(String[] firstPosCoords, int length, String direction, String[][]board)
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
    
    public boolean turn()
    {
        String positionInputRegex = "^(([A-J]{1})||([a-j]{1}))+,+([1-9]{1}||10)$";
        Scanner input = new Scanner(System.in);
        
        System.out.println("\f" + name + "'s turn");
        System.out.println("Torpedo Board");
        Board.display(torpedoBoard);
        System.out.println("Ship Board");
        Board.display(shipBoard);
        
        boolean validCoord = false;
            while(!validCoord)
            {
                System.out.print("Where would you like to fire Ex. letter,number:  ");
                String position = input.nextLine();
                if(position.matches(positionInputRegex))
                {
                    String[] coords = position.split(",");
                    String temp = coords[1];
                    coords[1] = Integer.toString((int)(coords[0].toUpperCase().charAt(0))-64);
                    coords[0] = temp;

                    if(!Board.checkIndex(torpedoBoard,Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), "X") || !Board.checkIndex(torpedoBoard,Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), "O"))
                    {
                        validCoord = true;
                        if(Board.checkIndex(opponent.getShipBoard(),Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), "▢"))
                        {
                            Board.changeIndex(torpedoBoard,Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), "O");
                            System.out.println("\f");
                            Board.display(torpedoBoard);
                            System.out.println("You have missed...");
                        }
                        else
                        {
                            Board.changeIndex(torpedoBoard,Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), "X");
                            Board.changeIndex(opponent.getShipBoard(),Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), "F");      
                            System.out.println("\f");
                            Board.display(torpedoBoard);
                            System.out.println("You have hit a ship!");
                        }
                        
                    }
                    else
                    {
                        System.out.print("*You have already fired there - Please try again* ");
                    }
                }
                else
                {
                    System.out.print("*Invalid coordinate input - Please try again* ");
                }
            }
        return checkLoss();
    }

    private boolean checkLoss()
    {
       boolean hasShips = false;
       for(int r = 0; r<shipBoard.length; r++)
        {
            for(int c = 0; c<shipBoard[r].length; c++)
            {
                if(shipBoard[r][c].equals("C") || !shipBoard[r][c].equals("D") || shipBoard[r][c].equals("B") || shipBoard[r][c].equals("U"))
                {
                    hasShips = true;
                }
            }
        }
        return !hasShips;
    }
}

