package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private GridPane gridPane;

    @FXML
    private MenuButton neighborhoodMenu;

    @FXML
    private MenuItem neumannItem;

    @FXML
    private MenuItem mooreItem;

    @FXML
    private MenuItem hexagonalItem;

    @FXML
    private MenuItem pentagonalItem;

    @FXML
    private MenuButton boundaryMenu;

    @FXML
    private MenuItem periodicItem;

    @FXML
    private MenuItem nonPeriodicItem;

    @FXML
    private MenuItem evenItem;

    @FXML
    private MenuItem randomItem;

    @FXML
    private MenuItem withRItem;

    @FXML
    private MenuItem clickItem;

    @FXML
    private TextField xRange;

    @FXML
    private TextField yRange;

    @FXML
    private TextField nRange;

    @FXML
    private TextField rRange;

    @FXML
    private Button startButton;

    @FXML
    private Button clearButton;

    @FXML
    private Button[][] buttonTab;

    @FXML
    private Label[][] labelTab;

    @FXML
    void clearcCick(ActionEvent event) {

    }

    @FXML
    void startClick(ActionEvent event) {
        buttonTab = new Button[Integer.parseInt(xRange.getText())][Integer.parseInt(yRange.getText())];
        labelTab = new Label[Integer.parseInt(xRange.getText())][Integer.parseInt(yRange.getText())];

        for (int i = 0; i < Integer.parseInt(xRange.getText()); i++) {
            for (int j = 0; j < Integer.parseInt(yRange.getText()); j++) {
                labelTab[i][j] = new Label();
                labelTab[i][j].setMinSize(1,1);
                labelTab[i][j].setPrefSize(5,5);
                labelTab[i][j].setStyle("-fx-background-color:#18ccff;");
                gridPane.add(labelTab[i][j],i,j);
//                buttonTab[i][j] = new Button();
//                buttonTab[i][j].setMinSize(1,1);
//                buttonTab[i][j].setPrefSize(10, 10);
//                buttonTab[i][j].setStyle("-fx-background-color:#18ccff;");
//                gridPane.add(buttonTab[i][j], i, j);
            }
        }
    }

    @FXML
    void clickClick(ActionEvent event) {
        nRange.setEditable(false);
        rRange.setEditable(false);
    }

    @FXML
    void evenClick(ActionEvent event) {
        nRange.setEditable(true);
        rRange.setEditable(false);
    }


    @FXML
    void randomClick(ActionEvent event) {
        nRange.setEditable(true);
        rRange.setEditable(false);
    }


    @FXML
    void withRClick(ActionEvent event) {
        nRange.setEditable(true);
        rRange.setEditable(true);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nRange.setEditable(false);
        rRange.setEditable(false);


        gridPane.setAlignment(Pos.CENTER);
        while (gridPane.getRowConstraints().size() > 0) {
            gridPane.getRowConstraints().remove(0);
        }

        while (gridPane.getColumnConstraints().size() > 0) {
            gridPane.getColumnConstraints().remove(0);
        }
    }


}