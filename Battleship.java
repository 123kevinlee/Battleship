/**
 * Battleship Game
 *
 * @KLee
 * @1.3.20
 */
import java.util.Scanner;
public class Battleship
{
    public static void main()
    {
        Scanner input = new Scanner(System.in);
        Boolean exit = false;
        while(!exit)
        {
            runGame();
            System.out.println("Do you want to play again[y/n]: ");
            if(!input.nextLine().toLowerCase().equals("y"))
            {
                exit = true;
            }
        }
        System.out.println("\fGoodbye!");
    }

    public static void runGame()
    {
        Scanner input = new Scanner(System.in);
        Player p1 = new Player("p1");
        System.out.print("Press enter to continue to player 2 setup...");
        input.nextLine();
        Player p2 = new Player("p2");
        p1.setOpponent(p2);
        p2.setOpponent(p1);

        boolean gameOver = false;
        boolean playerTurn = true; //true = p1, false = p2
        while(!gameOver)
        {
            System.out.print(playerTurn?"Press enter for player 1's turn":"Press enter for player 2's turn");
            input.nextLine();
            if(playerTurn)
            {
                if(p1.turn())
                {
                    gameOver = true;
                }
                playerTurn = !playerTurn;
            }
            else
            {
                if(p2.turn())
                {
                    gameOver = true;
                }
                playerTurn = !playerTurn;
            }
        }
        System.out.println(!playerTurn?"Player 1 won!":"Player 2 won!");
    }
}
