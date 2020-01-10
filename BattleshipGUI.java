import java.util.Scanner;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.lang.String;

import javafx.application.Application; 
import javafx.scene.Scene; 
import javafx.scene.control.*; 
import javafx.scene.layout.*; 
import javafx.stage.Stage; 
import javafx.scene.layout.*; 
import javafx.scene.paint.*; 
import javafx.scene.text.*; 
import javafx.geometry.*; 
import javafx.scene.layout.*; 
import javafx.scene.shape.*; 
import javafx.event.*;
import javafx.application.*;

/**
 * GUI for Battleship
 *
 * @KLee
 * @1.5.20
 */
public class BattleshipGUI extends Application
{   
    private Stage window;
    private Scene getIpScene, placeShipsScene, fireScene;

    private Socket socket;
    private Scanner serverIn;
    private PrintWriter clientOut;

    private Label title = new Label("Battleship");
    private Label message = new Label("Welcome!");
    private Label title1 = new Label("Battleship");
    private Label message1 = new Label("Welcome!");
    private TextField textField = new TextField ();
    private Button confirmPlacement = new Button("Place Ship");
    private Button fireButton = new Button("Fire");

    private String[][] torpedoBoardData = new String[10][10];
    private String[][] shipBoardData = new String[10][10];
    private String[][] opShipBoardData = new String[10][10];
    private Button[][] torpedoBoard = new Button[10][10];
    private Button[][] placeShipsBoard = new Button[10][10];
    private  Button[][] placeShipsBoardCopy = new Button[10][10];

    private String currentShipPlacementSymbol = "C";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage)
    {
        window = stage;

        title.setFont(new Font("Arial", 24));
        message.setFont(new Font("Arial", 20));
        title1.setFont(new Font("Arial", 24));
        message1.setFont(new Font("Arial", 20));

        //getIpScene
        Label question = new Label("Server IP:");        
        Button enterIP = new Button("Join Server");
        enterIP.setOnAction(this::ipClick); 

        HBox hb = new HBox();
        hb.getChildren().addAll(question, textField, enterIP);
        hb.setSpacing(10);

        getIpScene = new Scene(hb, 325,100);

        //placeShipsScene
        confirmPlacement.setOnAction(this::confirmClick);
        BorderPane bp = new BorderPane();
        bp.setTop(title);
        bp.setBottom(message);
        BorderPane.setAlignment(title, Pos.CENTER);
        BorderPane.setMargin(title, new Insets(20,0,20,0));
        BorderPane.setAlignment(message, Pos.CENTER);
        BorderPane.setMargin(message, new Insets(20,0,40,0));

        bp.setRight(confirmPlacement);
        BorderPane.setAlignment(confirmPlacement, Pos.CENTER);
        BorderPane.setMargin(confirmPlacement, new Insets(0,25,0,0));
        confirmPlacement.setDisable(true);

        //make a configure method
        for(int r = 0; r<shipBoardData.length; r++)
        {
            for(int c = 0; c<shipBoardData[0].length; c++)
            {
                shipBoardData[r][c] = "   ";
            }
        }

        for(int r = 0; r<placeShipsBoard.length; r++)
        {
            for(int c = 0; c<placeShipsBoard.length; c++)
            {
                placeShipsBoard[r][c] = new Button(shipBoardData[r][c]);
                placeShipsBoard[r][c].setPrefSize(50,50);
                placeShipsBoard[r][c].setStyle("-fx-background-color: #038cfc");
                placeShipsBoard[r][c].setOnAction(this::selectClick);
            }
        }

        GridPane gpS = addShipPlacementGridPane();
        bp.setCenter(gpS);     
        BorderPane.setMargin(gpS, new Insets(0,0,0,25));
        message.setText("Choose four blocks for the Cruiser to be placed");

        placeShipsScene = new Scene(bp, 750,700);

        //fireScene
        for(int r = 0; r<torpedoBoardData.length; r++)
        {
            for(int c = 0; c<torpedoBoardData[0].length; c++)
            {
                torpedoBoardData[r][c] = "   ";
            }
        }

        for(int r = 0; r<torpedoBoard.length; r++)
        {
            for(int c = 0; c<torpedoBoard.length; c++)
            {
                torpedoBoard[r][c] = new Button(torpedoBoardData[r][c]);
                torpedoBoard[r][c].setPrefSize(50,50);
                torpedoBoard[r][c].setStyle("-fx-background-color: #038cfc");
                torpedoBoard[r][c].setOnAction(this::targetClick);
            }
        }     

        BorderPane bp2 = new BorderPane();
        bp2.setTop(title1);
        bp2.setBottom(message1);
        BorderPane.setAlignment(title1, Pos.CENTER);
        BorderPane.setMargin(title1, new Insets(20,0,20,0));
        BorderPane.setAlignment(message1, Pos.CENTER);
        BorderPane.setMargin(message1, new Insets(20,0,40,0));

        bp2.setRight(fireButton);
        BorderPane.setAlignment(fireButton, Pos.CENTER);
        BorderPane.setMargin(fireButton, new Insets(0,25,0,0));
        fireButton.setDisable(true);
        fireButton.setOnAction(this::fireClick);
        GridPane gpT = addTorpedoGridPane();
        //GridPane gpS2 = addShipPlacementGridPane();
        bp2.setCenter(gpT);
        //bp2.setLeft(gpS2);

        // GridPane pane = new GridPane();
        // pane.setPadding(new Insets(10, 10, 10, 10));
        // pane.setMinSize(300, 300);
        // pane.setVgap(10);
        // pane.setHgap(10);

        // for(int c = 1; c<=placeShipsBoardCopy.length;c++)
        // {
        // pane.add(new Label("   " + Character.toString((char)(c+64))),c,0);
        // }
        // for(int r = 1; r<=placeShipsBoardCopy.length; r++)
        // {
        // pane.add(new Label(Integer.toString(r)),0,r);
        // for(int c = 1; c<=placeShipsBoardCopy.length; c++)
        // {
        // pane.add(placeShipsBoardCopy[r-1][c-1],c,r);
        // }
        // }
        //bp2.setTop(pane);
        
        // HBox hb1 = new HBox();
        // hb1.getChildren().addAll(gpT, gpS);
        // hb1.setSpacing(10);
        // bp2.setCenter(hb1);

        fireScene = new Scene(bp2,750, 700);

        //Init
        stage.setTitle("Battleship");
        stage.setScene(getIpScene);
        stage.show(); 
    }

    private void ipClick(ActionEvent event)
    {
        Button temp = (Button)(event.getSource());
        new Thread( () -> {
                try {
                    String ip = textField.getText();
                    String[] ipA = new String[2];
                    try
                    {
                        ipA = ip.split(":");
                    }
                    catch(Exception e)
                    {
                        temp.setText("Invalid IP");
                    }
                    Socket socket = new Socket(ipA[0], Integer.parseInt(ipA[1]));
                    temp.setText("Waiting for p2");
                    Scanner serverIn = new Scanner(socket.getInputStream());
                    PrintWriter clientOut = new PrintWriter(socket.getOutputStream(), true);
                    this.clientOut = clientOut;
                    this.serverIn = serverIn;
                    this.socket = socket;

                    while(serverIn.hasNextLine())
                    {
                        var response = serverIn.nextLine();
                        if(response.startsWith("MESSAGE"))
                        {
                            message.setText(response.substring(8));
                            if(response.substring(8).startsWith("You have"))
                            {
                                //end game here
                            }
                        }
                        else if(response.startsWith("SETUP"))
                        {
                            Platform.runLater(
                                () -> {
                                    window.setScene(placeShipsScene);
                                    message.setText("Choose four blocks for the Cruiser to be placed");
                                }
                            );
                        }
                        else if(response.startsWith("TURN"))
                        {
                            Platform.runLater(
                                () -> {
                                    window.setScene(fireScene);
                                    message1.setText("Choose a coordinate to fire at");
                                }
                            );
                            //placeShipsBoardCopy = placeShipsBoard;
                            String arrayDataOp = response.substring(4,response.indexOf("&"));
                            opShipBoardData = Board.Arrayify(arrayDataOp);
                            // String arrayData = response.substring(response.indexOf("&")+1);
                            // shipBoard = Board.Arrayify(arrayData);
                            // turn();
                            // clientOut.println("TURN" + Arrays.deepToString(shipBoardOp));
                        }
                        else if(response.startsWith("WAIT"))
                        {
                            message.setText("Waiting for opponent to finish turn...");
                        }
                    }
                }
                catch(Exception ex) {
                    temp.setText(ex.toString());
                }
            }).start();
    }

    private GridPane addShipPlacementGridPane()
    {
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setMinSize(300, 300);
        pane.setVgap(10);
        pane.setHgap(10);

        for(int c = 1; c<=placeShipsBoard.length;c++)
        {
            pane.add(new Label("   " + Character.toString((char)(c+64))),c,0);
        }
        for(int r = 1; r<=placeShipsBoard.length; r++)
        {
            pane.add(new Label(Integer.toString(r)),0,r);
            for(int c = 1; c<=placeShipsBoard.length; c++)
            {
                pane.add(placeShipsBoard[r-1][c-1],c,r);
            }
        }
        return pane;
    }

    private GridPane addTorpedoGridPane()
    {
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setMinSize(300, 300);
        pane.setVgap(10);
        pane.setHgap(10);

        for(int c = 1; c<=torpedoBoard.length;c++)
        {
            pane.add(new Label("   " + Character.toString((char)(c+64))),c,0);
        }
        for(int r = 1; r<=torpedoBoard.length; r++)
        {
            pane.add(new Label(Integer.toString(r)),0,r);
            for(int c = 1; c<=torpedoBoard.length; c++)
            {
                pane.add(torpedoBoard[r-1][c-1],c,r);
            }
        }
        return pane;
    }

    private void targetClick(ActionEvent event)
    {
        Button temp = (Button)(event.getSource());
        if(!temp.getText().equals("X"))
        {
            temp.setText("X");
            for(int r = 0; r<torpedoBoard.length; r++)
            {
                for(int c = 0; c<torpedoBoard.length; c++)
                {
                    if(!torpedoBoard[r][c].getText().equals("X"))
                    {
                        torpedoBoard[r][c].setDisable(true);
                    }
                    torpedoBoard[r][c].setStyle(torpedoBoard[r][c].getStyle() + "; -fx-opacity: 1");
                }
            }
            fireButton.setDisable(false);
        }
        else
        {
            temp.setText("   ");
            for(int r = 0; r<torpedoBoard.length; r++)
            {
                for(int c = 0; c<torpedoBoard.length; c++)
                {
                    if(!torpedoBoard[r][c].getText().equals("X"))
                    {
                        torpedoBoard[r][c].setDisable(false);
                    }
                    torpedoBoard[r][c].setStyle(torpedoBoard[r][c].getStyle() + "; -fx-opacity: 1");
                }
            }
            fireButton.setDisable(true);
        }
        updateTorpedoDataArray();
        //add miss stuff
    }

    private void fireClick(ActionEvent event)
    {
        for(int r = 0; r < torpedoBoard.length; r++)
        {
            for(int c = 0; c < torpedoBoard[r].length; c++)
            {
                if(torpedoBoard[r][c].getText().equals("X") && !opShipBoardData[r][c].equals("-"))
                {
                    opShipBoardData[r][c].equals("F");
                    message1.setText("You have hit a ship!");
                }
                else if (torpedoBoard[r][c].getText().equals("X") && opShipBoardData[r][c].equals("-"))
                {
                    torpedoBoard[r][c].setText("O");
                    message1.setText("You have missed!");
                }
            }
        }
        for(int r = 0; r<torpedoBoard.length; r++)
        {
            for(int c = 0; c<torpedoBoard.length; c++)
            {
                if(!torpedoBoard[r][c].getText().equals("X"))
                {
                    torpedoBoard[r][c].setDisable(false);
                }
                torpedoBoard[r][c].setStyle(torpedoBoard[r][c].getStyle() + "; -fx-opacity: 1");
            }
        }
        fireButton.setDisable(true);
        clientOut.println("TURN" + Arrays.deepToString(opShipBoardData));
    }

    private void selectClick(ActionEvent event)
    {
        Button temp = (Button)(event.getSource());
        if(temp.getStyle() == "-fx-background-color: #038cfc")
        {
            temp.setStyle("-fx-background-color: #caf218");
        }
        else
        {
            temp.setStyle("-fx-background-color: #038cfc");
        }
        if(!currentShipPlacementSymbol.equals("B") && checkValidPlacement(4))
        {
            confirmPlacement.setDisable(false);
        }
        else if (currentShipPlacementSymbol.equals("B") && checkValidPlacement(5))
        {
            confirmPlacement.setDisable(false);
        }
        else
        {
            confirmPlacement.setDisable(true);
        }
    }

    private void confirmClick(ActionEvent event)
    {
        Button temp = (Button)(event.getSource());
        for(int r = 0; r<placeShipsBoard.length; r++)
        {
            for(int c = 0; c<placeShipsBoard.length; c++)
            {
                if(placeShipsBoard[r][c].getStyle() == "-fx-background-color: #caf218")
                {
                    shipBoardData[r][c] = currentShipPlacementSymbol;
                    if(currentShipPlacementSymbol.equals("C"))
                    {
                        placeShipsBoard[r][c].setStyle("-fx-background-color: #6c6e7a");
                    }
                    else if(currentShipPlacementSymbol.equals("D"))
                    {
                        placeShipsBoard[r][c].setStyle("-fx-background-color: #3b3636");
                    }
                    else if(currentShipPlacementSymbol.equals("B"))
                    {
                        placeShipsBoard[r][c].setStyle("-fx-background-color: #2a2b2e");
                    }
                    placeShipsBoard[r][c].setDisable(true);
                    placeShipsBoard[r][c].setStyle(placeShipsBoard[r][c].getStyle() + "; -fx-opacity: 1");
                }
            }
        }
        if(currentShipPlacementSymbol.equals("C"))
        {
            currentShipPlacementSymbol = "D";
            message.setText("Choose four blocks for the Destroyer to be placed");
        }
        else if(currentShipPlacementSymbol.equals("D"))
        {
            currentShipPlacementSymbol = "B";
            message.setText("Choose five blocks for the Battleship to be placed");
        }
        else if(currentShipPlacementSymbol.equals("B"))
        {
            currentShipPlacementSymbol = "complete";
            for(int r = 0; r<placeShipsBoard.length; r++)
            {
                for(int c = 0; c<placeShipsBoard.length; c++)
                {
                    placeShipsBoard[r][c].setDisable(true);
                    placeShipsBoard[r][c].setStyle(placeShipsBoard[r][c].getStyle() + "; -fx-opacity: 1");
                }
            }
            message.setText("Please for your opponent to finish setting up their board...");
            clientOut.println("SETUP" + Arrays.deepToString(arrayElementTrim(shipBoardData)));
        }
        temp.setDisable(true);
    }

    private boolean checkValidPlacement(int length)
    {
        //fix dis bc it only creates array of desired length not amount of blocks highlighted and also it checks if only 4 are in a row so go back to the for loop method
        int linedUp = 0;
        int[][] selectedButtons = new int[5][2];
        for(int r = 0; r<placeShipsBoard.length; r++)
        {
            for(int c = 0; c<placeShipsBoard.length; c++)
            {
                if(placeShipsBoard[r][c].getStyle() == "-fx-background-color: #caf218")
                {
                    selectedButtons[linedUp][0] = r;
                    selectedButtons[linedUp][1] = c;
                    linedUp++;
                }
            }
        }
        if(linedUp == length)
        {
            if(linedUp == 4)
            {
                if((selectedButtons[0][0] == selectedButtons[1][0] && selectedButtons[1][0] == selectedButtons[2][0] && selectedButtons[2][0] == selectedButtons[3][0]) && (selectedButtons[0][1] == selectedButtons[1][1] - 1 && selectedButtons[1][1] == selectedButtons[2][1] - 1 && selectedButtons[2][1] == selectedButtons[3][1] - 1))
                {
                    return true;
                }
                if((selectedButtons[0][0] == selectedButtons[1][0] - 1 && selectedButtons[1][0] == selectedButtons[2][0] - 1 && selectedButtons[2][0] == selectedButtons[3][0] - 1) && (selectedButtons[0][1] == selectedButtons[1][1] && selectedButtons[1][1] == selectedButtons[2][1] && selectedButtons[2][1] == selectedButtons[3][1]))
                {
                    return true;
                }
            }
            if(linedUp == 5)
            {
                if((selectedButtons[0][0] == selectedButtons[1][0] && selectedButtons[1][0] == selectedButtons[2][0] && selectedButtons[2][0] == selectedButtons[3][0] && selectedButtons[3][0] == selectedButtons[4][0]) && (selectedButtons[0][1] == selectedButtons[1][1] - 1 && selectedButtons[1][1] == selectedButtons[2][1] - 1 && selectedButtons[2][1] == selectedButtons[3][1] - 1 && selectedButtons[3][1] == selectedButtons[4][1] - 1))
                {
                    return true;
                }
                if((selectedButtons[0][0] == selectedButtons[1][0] - 1 && selectedButtons[1][0] == selectedButtons[2][0] - 1 && selectedButtons[2][0] == selectedButtons[3][0] - 1 && selectedButtons[3][0] == selectedButtons[4][0] -1) && (selectedButtons[0][1] == selectedButtons[1][1] && selectedButtons[1][1] == selectedButtons[2][1] && selectedButtons[2][1] == selectedButtons[3][1] && selectedButtons[3][1] == selectedButtons[4][1]))
                {
                    return true;
                }
            }
        }
        return false;
    }

    private void updateTorpedoDataArray()
    {
        for(int r = 0; r<torpedoBoard.length; r++)
        {
            for(int c = 0; c<torpedoBoard[r].length; c++)
            {
                if(torpedoBoard[r][c].getText() != torpedoBoardData[r][c])
                {
                    torpedoBoardData[r][c] = torpedoBoard[r][c].getText();
                }
            }
        }
    }

    private String[][] arrayElementTrim(String[][] data)
    {
        String[][]temp = new String[10][10];
        for(int r = 0; r<data.length; r++)
        {
            for(int c = 0; c<data[r].length; c++)
            {
                temp[r][c] = data[r][c].trim();
                if(temp[r][c].equals(""))
                {
                    temp[r][c] = "-";
                }
            }
        }
        return temp;
    }
}
