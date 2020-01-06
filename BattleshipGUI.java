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
 * Write a description of JavaFX class BattleshipGUI here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class BattleshipGUI extends Application
{
    private Label title = new Label("Battleship");

    private String[][] torpedoBoardData = new String[10][10];

    private Button[][] torpedoBoard = new Button[10][10];

    @Override
    public void start(Stage stage)
    {
        for(int r = 0; r<torpedoBoardData.length; r++)
        {
            for(int c = 0; c<torpedoBoardData[0].length; c++)
            {
                torpedoBoardData[r][c] = "   ";
            }
        }

        title.setFont(new Font("Arial", 24));;
        for(int r = 0; r<torpedoBoard.length; r++)
        {
            for(int c = 0; c<torpedoBoard.length; c++)
            {
                torpedoBoard[r][c] = new Button(torpedoBoardData[r][c]);
                torpedoBoard[r][c].setStyle("-fx-background-color: #038cfc");
                torpedoBoard[r][c].setOnAction(this::buttonClick);
            }
        }

        BorderPane border = new BorderPane();
        border.setTop(title);
        BorderPane.setAlignment(title, Pos.CENTER);
        border.setCenter(addGridPane());

        // JavaFX must have a Scene (window content) inside a Stage (window)
        Scene scene = new Scene(border, 450,450);
        stage.setTitle("Battleship");
        stage.setScene(scene);

        // Show the Stage (window)
        stage.show();   
    }

    public GridPane addGridPane()
    {
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setMinSize(300, 300);
        pane.setVgap(10);
        pane.setHgap(10);

        //set an action on the button using method reference
        //myButton.setOnAction(this::buttonClick);

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

    /**
     * This will be executed when the button is clicked
     * It increments the count by 1
     */
    private void buttonClick(ActionEvent event)
    {
        // Counts number of button clicks and shows the result on a label
        //title.setText(torpedoBoard[1][1].toString());
        //torpedoBoard[1][1].setText(event.getSource().toString());
        for(int r = 0; r<torpedoBoard.length; r++)
        {
            for(int c = 0; c<torpedoBoard.length; c++)
            {
                Button temp = (Button)(event.getSource());
                temp.setText("X");
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
