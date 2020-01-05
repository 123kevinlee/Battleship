/**
 * Server to host Battleship games
 *
 * @KLee
 * @1.4.20
 */
import java.io.IOException;
import java.io.PrintWriter;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.Executors;
public class Server
{
    public static void main() throws Exception {
        int port = 58901;
        String ip = InetAddress.getLocalHost().getHostAddress();
        try (var listener = new ServerSocket(port))
        {
            System.out.println("Battleship server is running on " + ip + ":" + port);
            var pool = Executors.newFixedThreadPool(2);
            while(true)
            {
                BattleshipS game = new BattleshipS();
                pool.execute(game.new Player(listener.accept(), "p1"));
                pool.execute(game.new Player(listener.accept(), "p2"));
            }
        }
    }
}
