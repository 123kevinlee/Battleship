import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.Executors;
public class BattleshipS
{
    private SPlayer currentPlayer;

    private static boolean checkWin(String[][] shipBoard)
    {
        boolean hasShips = false;
        for(int r = 0; r<shipBoard.length; r++)
        {
            for(int c = 0; c<shipBoard[r].length; c++)
            {
                if(shipBoard[r][c].equals("C") ||shipBoard[r][c].equals("D") || shipBoard[r][c].equals("B") || shipBoard[r][c].equals("U"))
                {
                    hasShips = true;
                }
            }
        }
        return !hasShips;
    }

    public class SPlayer implements Runnable 
    {
        private String name;
        private String[][] shipBoard = new String[10][10];
        private SPlayer opponent;
        private boolean ready = false, start = false;
        private Socket socket;
        private Scanner input;
        private PrintWriter output;

        public SPlayer(Socket socket, String name)
        {
            this.socket = socket;
            this.name = name;
            System.out.println(name + " has connected...");
        }

        @Override
        public void run()
        {
            try{
                loading();
                processCommands();
            }
            catch (Exception e) {e.printStackTrace();}
            finally {
                if (opponent != null && opponent.output != null && !checkWin(opponent.shipBoard))
                {
                    opponent.output.println("MESSAGE Other player has left the game");
                }
                try {socket.close();} catch (IOException e) {}
            }
        }

        private void loading() throws IOException 
        {
            input = new Scanner(socket.getInputStream());
            output = new PrintWriter(socket.getOutputStream(), true);
            output.println("MESSAGE Welcome to Battleship " + name);
            if (name == "p1")
            {
                currentPlayer = this;
                output.println("MESSAGE Waiting for opponent to connect...");
            }
            else
            {
                opponent = currentPlayer;
                opponent.opponent = this;
                output.println("SETUP");
                opponent.output.println("SETUP");
            }
        }

        private void processCommands()
        {
            while(input.hasNextLine())
            {
                var command = input.nextLine();
                if(command.startsWith("SETUP"))
                {
                    String arrayData = command.substring(5);
                    shipBoard = Board.Arrayify(arrayData);
                    opponent.opponent.shipBoard = Board.Arrayify(arrayData);
                    System.out.println("[" + java.time.LocalTime.now() + "] " + "Recieved setup from " + name + "\n" + arrayData);
                    ready = true;
                }
                if (ready && opponent.ready)
                {
                    if(start == false) //name == "p1" && 
                    {
                        output.println("MESSAGE \f");
                        output.println("TURN" + Arrays.deepToString(opponent.shipBoard) + "&" + Arrays.deepToString(shipBoard));
                        opponent.output.println("WAIT");
                        start = true;
                        opponent.start = true;
                        System.out.println("[" + java.time.LocalTime.now() + "] " + name + "'s turn");
                    }
                    else
                    {
                        if(command.startsWith("QUIT"))
                        {
                            System.out.println("[" + java.time.LocalTime.now() + "] " + name + " has quit");
                            return;
                        }
                        else if(command.startsWith("TURN"))
                        {
                            String arrayData = command.substring(4);
                            String[][] prevShipBoard = opponent.shipBoard;
                            opponent.shipBoard = Board.Arrayify(arrayData);
                            System.out.println("[" + java.time.LocalTime.now() + "] Recieved updated board for" + opponent.name + "\n" + arrayData);
                            int[]hitCoords = Board.checkHitShip(prevShipBoard, opponent.shipBoard);
                            String hitCoordsS = (char)(hitCoords[1]+65) + Integer.toString((hitCoords[0] + 1));                   
                            if(hitCoords[0] != -1)
                            {
                                String hitShipSymbol = prevShipBoard[hitCoords[0]][hitCoords[1]];
                                switch (hitShipSymbol)
                                {
                                    case "C": 
                                    opponent.output.println("MESSAGE \fYour Cruiser was hit at " + hitCoordsS);
                                    System.out.println(opponent.name + "s Cruiser was hit at " + hitCoordsS);
                                    break;
                                    case "D": 
                                    opponent.output.println("MESSAGE \fYour Destroyer was hit at " + hitCoordsS);
                                    System.out.println(opponent.name + "s Destroyer was hit at " + hitCoordsS);
                                    break;
                                    case "B": 
                                    opponent.output.println("MESSAGE \fYour Battleship was hit at " + hitCoordsS);
                                    System.out.println(opponent.name + "s Battleship was hit at " + hitCoordsS);
                                    break;
                                }
                                if(Board.checkOneRemaining(prevShipBoard, hitShipSymbol))
                                {
                                    opponent.output.println("MESSAGE It was sunk");
                                    System.out.println(opponent.name + "s ship was sunk");
                                }                
                            }
                            // else
                            // {
                                // opponent.output.println("MESSAGE " + name + " fired at " + hitCoordsS + " and missed");
                            // }

                            if(checkWin(opponent.shipBoard))
                            {
                                System.out.println(name + " won the game");
                                output.println("MESSAGE You have won!");
                                opponent.output.println("MESSAGE You have lost...");
                                return;
                            }
                            output.println("WAIT");
                            opponent.output.println("TURN" + Arrays.deepToString(shipBoard) + "&" + Arrays.deepToString(opponent.shipBoard));
                            System.out.println("[" + java.time.LocalTime.now() + "] " + opponent.name + "'s turn");
                        }
                    }
                }
            }
        }   
    }
}

