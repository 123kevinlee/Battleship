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

/**
 * GUI for Battleship
 *
 * @KLee
 * @1.5.20
 */
public class BattleshipGUI extends Application
{   
    private BorderPane border = new BorderPane();
    private Label title = new Label("Battleship");
    private Label message = new Label("Welcome!");

    private String[][] torpedoBoardData = new String[10][10];
    private String[][] shipBoardData = new String[10][10];
    private Button[][] torpedoBoard = new Button[10][10];
    private Button[][] placeShipsBoard = new Button[10][10];
    private Button confirmPlacement = new Button("Place Ship");

    private String currentShipPlacementSymbol = "C";

    @Override
    public void start(Stage stage)
    {
        setupGame(stage);
        setupShips();
        // for(int r = 0; r<torpedoBoardData.length; r++)
        // {
        // for(int c = 0; c<torpedoBoardData[0].length; c++)
        // {
        // torpedoBoardData[r][c] = "   ";
        // }
        // }

        // for(int r = 0; r<torpedoBoard.length; r++)
        // {
        // for(int c = 0; c<torpedoBoard.length; c++)
        // {
        // torpedoBoard[r][c] = new Button(torpedoBoardData[r][c]);
        // torpedoBoard[r][c].setPrefSize(50,50);
        // torpedoBoard[r][c].setStyle("-fx-background-color: #038cfc");
        // torpedoBoard[r][c].setOnAction(this::fireClick);
        // }
        // }        

        // border.setCenter(addTorpedoGridPane());
    }

    private void setupGame(Stage stage)
    {
        Scene scene = new Scene(border, 750,700);
        stage.setTitle("Battleship");
        stage.setScene(scene);
        title.setFont(new Font("Arial", 24));
        message.setFont(new Font("Arial", 18));
        border.setTop(title);
        border.setBottom(message);
        BorderPane.setAlignment(title, Pos.CENTER);
        BorderPane.setAlignment(message, Pos.CENTER);

        stage.show();  

        Board.setBlankBoard(shipBoardData);
        Board.setBlankBoard(torpedoBoardData);  
    }

    private void setupShips()
    {
        confirmPlacement.setOnAction(this::confirmClick);
        border.setRight(confirmPlacement);
        BorderPane.setAlignment(confirmPlacement, Pos.CENTER);
        BorderPane.setMargin(confirmPlacement, new Insets(0,25,0,0));
        confirmPlacement.setDisable(true);

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

        border.setCenter(addShipPlacementGridPane());     

        message.setText("Choose four blocks for the Cruiser to be placed");
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

    private void fireClick(ActionEvent event)
    {
        Button temp = (Button)(event.getSource());
        temp.setText("X");
        updateDataArray();
        //add miss stuff
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
        else if (checkValidPlacement(5))
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
                        placeShipsBoard[r][c].setStyle("-fx-background-color: #2a2b2e");
                    }
                    else if(currentShipPlacementSymbol.equals("B"))
                    {
                        placeShipsBoard[r][c].setStyle("-fx-background-color: #131414");
                    }
                    placeShipsBoard[r][c].setDisable(true);
                }
            }
        }
        if(currentShipPlacementSymbol.equals("C"))
        {
            currentShipPlacementSymbol = "D";
            message.setText("Choose four blocks for the Battleship to be placed");
        }
        else if(currentShipPlacementSymbol.equals("D"))
        {
            currentShipPlacementSymbol = "B";
            message.setText("Choose five blocks for the Destroyer to be placed");
        }
        else if(currentShipPlacementSymbol.equals("B"))
        {
            currentShipPlacementSymbol = "complete";
            for(int r = 0; r<placeShipsBoard.length; r++)
            {
                for(int c = 0; c<placeShipsBoard.length; c++)
                {
                    placeShipsBoard[r][c].setDisable(true);
                }
            }
            message.setText("Please for your opponent to finish setting up their board...");
        }
        temp.setDisable(true);
    }

    private boolean checkValidPlacement(int length)
    {
        int linedUp = 0;
        int[][] selectedButtons = new int[length][2];
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
            if((selectedButtons[0][0] == selectedButtons[1][0] && selectedButtons[1][0] == selectedButtons[2][0] && selectedButtons[2][0] == selectedButtons[3][0]) && (selectedButtons[0][1] == selectedButtons[1][1] - 1 && selectedButtons[1][1] == selectedButtons[2][1] - 1 && selectedButtons[2][1] == selectedButtons[3][1] - 1))
            {
                return true;
            }
            if((selectedButtons[0][0] == selectedButtons[1][0] - 1 && selectedButtons[1][0] == selectedButtons[2][0] - 1 && selectedButtons[2][0] == selectedButtons[3][0] - 1) && (selectedButtons[0][1] == selectedButtons[1][1] && selectedButtons[1][1] == selectedButtons[2][1] && selectedButtons[2][1] == selectedButtons[3][1]))
            {
                return true;
            }
        }
        return false;
    }

    private void updateShipArray()
    {
        for(int r = 0; r<placeShipsBoard.length; r++)
        {
            for(int c = 0; c<placeShipsBoard.length; c++)
            {
                if(placeShipsBoard[r][c].getText() != shipBoardData[r][c])
                {
                    shipBoardData[r][c] = placeShipsBoard[r][c].getText();
                }
            }
        }
    }

    private void updateDataArray()
    {
        for(int r = 0; r<torpedoBoard.length; r++)
        {
            for(int c = 0; c<torpedoBoard.length; c++)
            {
                if(torpedoBoard[r][c].getText() != torpedoBoardData[r][c])
                {
                    torpedoBoardData[r][c] = torpedoBoard[r][c].getText();
                }
            }
        }
    }
}
