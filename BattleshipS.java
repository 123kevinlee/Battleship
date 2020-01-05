import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.Executors;
public class BattleshipS
{
    private Player currentPlayer;

    public class Player implements Runnable 
    {
        private String name;
        private String[][] shipBoard = new String[10][10];
        private String[][] torpedoBoard = new String[10][10];
        private Player opponent;
        private boolean ready = false, start = false;
        private Socket socket;
        private Scanner input;
        private PrintWriter output;

        public Player(Socket socket, String name)
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
                if (opponent != null && opponent.output != null)
                {
                    opponent.output.println("OTHER_PlAYER_LEFT");
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
                    ready = true;
                }
                if (ready && opponent.ready)
                {
                    if(name == "p1" && start == false)
                    {
                        output.println("TURN");
                        start = true;
                    }
                    if(command == "QUIT")
                    {
                        return;
                    }
                    else if(command.startsWith("TURN"))
                    {

                    }
                }
            }
        }
    }
}
