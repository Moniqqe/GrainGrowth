package sample;

import com.sun.javafx.css.Rule;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import sun.print.DocumentPropertiesUI;

import javax.management.relation.Role;
import javax.xml.crypto.dom.DOMCryptoContext;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    ArrayList clickedList = new ArrayList();
    int[][] IDTab;
    int[][] weightTab;
    String[] colors = {"FF089B", "#ffa771", "#ffdb71", "#d6ff71", "#71ff8b", "#71ffe7", "#71caff", "#8f71ff", "#e771ff", "#ff7171", "E6FFE5"};
    boolean bounds;
    int[][] tab = new int[3][3];
    int sizeX = 0, sizeY = 0, sizeN = 0, choice = 0;

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
    private MenuItem withRNItem;

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
        gridPane.getChildren().clear();
    }

    @FXML
    void runClick(ActionEvent event) {

    }

    @FXML
    void scrolling(ScrollEvent event) {
        scrol(choice, bounds);
        fillLabels();
    }

    @FXML
    void startClick(ActionEvent event) {
        Random r = new Random();
        sizeX = Integer.parseInt(xRange.getText());
        sizeY = Integer.parseInt(yRange.getText());
        sizeN = Integer.parseInt(nRange.getText()) + 1;
        labelTab = new Label[sizeX][sizeY];
        IDTab = new int[sizeX][sizeY];
        weightTab = new int[sizeX][sizeY];
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                labelTab[i][j] = new Label();
                labelTab[i][j].setMinSize(1, 1);
                labelTab[i][j].setPrefSize(5, 5);
                labelTab[i][j].setStyle("-fx-background-color:#72adff;");
                gridPane.add(labelTab[i][j], i, j);
                weightTab[i][j] = r.nextInt(9);
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
        for (int[] row : IDTab)
            Arrays.fill(row, 0);
        Random g = new Random(); //zmiennna do losowania
        int a = 0, b = 0, c = 0; //zmienne pomocnicze
        int counter = 0;
        if (Integer.parseInt(nRange.getText()) > 0) {
            int n = Integer.parseInt(nRange.getText());
            a = (int) Math.sqrt(n);
            b = Integer.parseInt(xRange.getText()) / (a * 2);
            c = Integer.parseInt(yRange.getText()) / (a * 2);
            for (int i = b; i < Integer.parseInt(xRange.getText()); i = i + 2 * b) {
                for (int j = c; j < Integer.parseInt(yRange.getText()); j = j + 2 * c) {
                    if (counter >= 10)
                        counter -= 10;
                    IDTab[i][j] = counter;
                    labelTab[i][j].setStyle("-fx-background-color:" + colors[counter] + ";");
                    counter++;
                }
            }

        }
    }


    @FXML
    void randomClick(ActionEvent event) {
        for (int[] row : IDTab)
            Arrays.fill(row, 0);
        Random g = new Random(); //zmiennna do losowania
        int a = 0, b = 0, col = 0; //zmienne pomocnicze
        if (Integer.parseInt(nRange.getText()) > 0) {
            int n = Integer.parseInt(nRange.getText());
            for (int i = 1; i <= n; i++) {
                a = g.nextInt(Integer.parseInt(xRange.getText()));
                b = g.nextInt(Integer.parseInt(yRange.getText()));
                if (IDTab[a][b] == 0) {
                    IDTab[a][b] = i;
                    if (col >= 10)
                        col -= 10;
                    labelTab[a][b].setStyle("-fx-background-color:" + colors[col] + ";");
                    col++;
                } else
                    i--;
            }
        }
    }


    @FXML
    void withRClick(ActionEvent event) {
        for (int[] row : IDTab)
            Arrays.fill(row, 0);
        Random g = new Random(); //zmiennna do losowania
        int a = 0, b = 0, j = 0, k = 0, l = 0, m = 0; //zmienne pomocnicze
        int counter = 0, col = 0;
        if (Integer.parseInt(nRange.getText()) > 0 && Integer.parseInt(rRange.getText()) > 0) {
            int n = Integer.parseInt(nRange.getText());
            int r = Integer.parseInt(rRange.getText());
            for (int i = 1; i <= n; i++) {
                a = g.nextInt(Integer.parseInt(xRange.getText()));
                b = g.nextInt(Integer.parseInt(yRange.getText()));
                j = a - r;
                k = b + r; //kbr
                l = a + r;
                m = b - r;
                if (j < 0) {
                    j = 0;
                }
                if (l >= Integer.parseInt(xRange.getText())) {
                    l = Integer.parseInt(xRange.getText()) - 1;
                }
                if (m < 0) {
                    m = 0;
                }
                if (k >= Integer.parseInt(yRange.getText())) {
                    k = Integer.parseInt(yRange.getText()) - 1;
                }
                counter = 0;
                for (int o = j; o <= l; o++) {
                    for (int p = k; p >= m; p--) {
                        if (IDTab[o][p] > 0) {
                            counter++;
                        }
                    }
                }
                if (counter == 0) {
                    IDTab[a][b] = i;
                    if (col >= 10)
                        col -= 10;
                    labelTab[a][b].setStyle("-fx-background-color:" + colors[col] + ";");
                    col++;
                } else
                    i--;
            }
        }

    }

    @FXML
    void nonPeriodicClick(ActionEvent event) {
        bounds = false;
    }

    @FXML
    void periodicClick(ActionEvent event) {
        bounds = true;
    }

    @FXML
    void hexClick(ActionEvent event) {
        choice = 5;
    }

    @FXML
    void hexRClick(ActionEvent event) {
        choice = 4;
    }

    @FXML
    void hexLClick(ActionEvent event) {
        choice = 3;
    }

    @FXML
    void mooreClick(ActionEvent event) {
        choice = 1;
    }

    @FXML
    void nWithRClick(ActionEvent event) {
        choice = 7;
    }

    @FXML
    void neumannClick(ActionEvent event) {
        choice = 2;
    }


    @FXML
    void pentClick(ActionEvent event) {
        choice = 6;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nRange.setEditable(true);
        rRange.setEditable(true);
        nRange.setText("0");
        rRange.setText("0");
        xRange.setText("0");
        yRange.setText("0");
        bounds = false;

        gridPane.setAlignment(Pos.CENTER);
        while (gridPane.getRowConstraints().size() > 0) {
            gridPane.getRowConstraints().remove(0);
        }

        while (gridPane.getColumnConstraints().size() > 0) {
            gridPane.getColumnConstraints().remove(0);
        }
    }

    void scrol(int a, boolean b) {
        int[][] apt = new int[sizeX][sizeY];
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                if (IDTab[i][j] == 0) {
                    if (a == 1) {
                        apt[i][j] = moore(i, j, b);
                    } else if (a == 2) {
                        apt[i][j] = neumann(i, j, b);
                    } else if (a == 3) {
                        apt[i][j] = hexleft(i, j, b);
                    } else if (a == 4) {
                        apt[i][j] = hexright(i, j, b);
                    } else if (a == 5) {
                        apt[i][j] = hexrand(i, j, b);
                    } else if (a == 6) {
                        apt[i][j] = pentrand(i, j, b);
                    } else if (a == 7) {
                        apt[i][j] = weightR(i, j, b);
                    }
                } else {
                    apt[i][j] = IDTab[i][j];
                }

            }
        }
        for (
                int i = 0;
                i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                IDTab[i][j] = apt[i][j];
            }
        }

    }

    public int[] neighbours(int[][] tab) {
        int[] pomid = new int[sizeN];
        for (int i = 0; i < sizeN; i++) {
            pomid[i] = 0;
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i == 1 && j == 1) {
                    //
                } else {
                    for (int k = 0; k < sizeN; k++) {
                        if (tab[i][j] == k) {
                            pomid[k]++;
                        }
                    }
                }
            }
        }
        return pomid;
    }

    int[][] periodic(int x, int y) {
        if (x == 0 && y == 0) {
            tab[0][0] = IDTab[sizeX - 1][sizeY - 1];
            tab[0][1] = IDTab[sizeX - 1][y];
            tab[0][2] = IDTab[sizeX - 1][y + 1];
            tab[1][0] = IDTab[x][sizeY - 1];
            tab[1][1] = IDTab[x][y];
            tab[1][2] = IDTab[x][y + 1];
            tab[2][0] = IDTab[x + 1][sizeY - 1];
            tab[2][1] = IDTab[x + 1][y];
            tab[2][2] = IDTab[x + 1][y + 1];
        } else if (x == 0 && y == sizeY - 1) {
            tab[0][0] = IDTab[sizeX - 1][y - 1];
            tab[0][1] = IDTab[sizeX - 1][y];
            tab[0][2] = IDTab[sizeX - 1][0];
            tab[1][0] = IDTab[x][y - 1];
            tab[1][1] = IDTab[x][y];
            tab[1][2] = IDTab[x][0];
            tab[2][0] = IDTab[x + 1][y - 1];
            tab[2][1] = IDTab[x + 1][y];
            tab[2][2] = IDTab[x + 1][0];
        } else if (x == sizeX - 1 && y == 0) {
            tab[0][0] = IDTab[x - 1][sizeY - 1];
            tab[0][1] = IDTab[x - 1][y];
            tab[0][2] = IDTab[x - 1][y + 1];
            tab[1][0] = IDTab[x][sizeY - 1];
            tab[1][1] = IDTab[x][y];
            tab[1][2] = IDTab[x][y + 1];
            tab[2][0] = IDTab[0][sizeY - 1];
            tab[2][1] = IDTab[0][y];
            tab[2][2] = IDTab[0][y + 1];
        } else if (x == sizeX - 1 && y == sizeY - 1) {
            tab[0][0] = IDTab[x - 1][y - 1];
            tab[0][1] = IDTab[x - 1][y];
            tab[0][2] = IDTab[x - 1][0];
            tab[1][0] = IDTab[x][y - 1];
            tab[1][1] = IDTab[x][y];
            tab[1][2] = IDTab[x][0];
            tab[2][0] = IDTab[0][y - 1];
            tab[2][1] = IDTab[0][y];
            tab[2][2] = IDTab[0][0];
        } else if (x == 0 && y > 0 && y < sizeY - 1) {
            tab[0][0] = IDTab[sizeX - 1][y - 1];
            tab[0][1] = IDTab[sizeX - 1][y];
            tab[0][2] = IDTab[sizeX - 1][y + 1];
            ;
            tab[1][0] = IDTab[x][y - 1];
            tab[1][1] = IDTab[x][y];
            tab[1][2] = IDTab[x][y + 1];
            tab[2][0] = IDTab[x + 1][y - 1];
            tab[2][1] = IDTab[x + 1][y];
            tab[2][2] = IDTab[x + 1][y + 1];
        } else if (x > 0 && x < sizeX - 1 && y == 0) {
            tab[0][0] = IDTab[x - 1][sizeY - 1];
            tab[0][1] = IDTab[x - 1][y];
            tab[0][2] = IDTab[x - 1][y + 1];
            tab[1][0] = IDTab[x][sizeY - 1];
            tab[1][1] = IDTab[x][y];
            tab[1][2] = IDTab[x][y + 1];
            tab[2][0] = IDTab[x + 1][sizeY - 1];
            tab[2][1] = IDTab[x + 1][y];
            tab[2][2] = IDTab[x + 1][y + 1];
        } else if (x == sizeX - 1 && y > 0 && y < sizeY - 1) {
            tab[0][0] = IDTab[x - 1][y - 1];
            tab[0][1] = IDTab[x - 1][y];
            tab[0][2] = IDTab[x - 1][y + 1];
            tab[1][0] = IDTab[x][y - 1];
            tab[1][1] = IDTab[x][y];
            tab[1][2] = IDTab[x][y + 1];
            tab[2][0] = IDTab[0][y - 1];
            tab[2][1] = IDTab[0][y];
            tab[2][2] = IDTab[0][y + 1];
        } else if (y == sizeY - 1 && x > 0 && x < sizeX - 1) {
            tab[0][0] = IDTab[x - 1][y - 1];
            tab[0][1] = IDTab[x - 1][y];
            tab[0][2] = IDTab[x - 1][0];
            tab[1][0] = IDTab[x][y - 1];
            tab[1][1] = IDTab[x][y];
            tab[1][2] = IDTab[x][0];
            tab[2][0] = IDTab[x + 1][y - 1];
            tab[2][1] = IDTab[x + 1][y];
            tab[2][2] = IDTab[x + 1][0];
        } else {
            tab[0][0] = IDTab[x - 1][y - 1];
            tab[0][1] = IDTab[x - 1][y];
            tab[0][2] = IDTab[x - 1][y + 1];
            tab[1][0] = IDTab[x][y - 1];
            tab[1][1] = IDTab[x][y];
            tab[1][2] = IDTab[x][y + 1];
            tab[2][0] = IDTab[x + 1][y - 1];
            tab[2][1] = IDTab[x + 1][y];
            tab[2][2] = IDTab[x + 1][y + 1];
        }
        return tab;
    }

    int[][] nonPeriodic(int x, int y) {
        if (x == 0 && y == 0) {
            tab[0][0] = 0;
            tab[0][1] = 0;
            tab[0][2] = 0;
            tab[1][0] = 0;
            tab[1][1] = IDTab[x][y];
            tab[1][2] = IDTab[x][y + 1];
            tab[2][0] = 0;
            tab[2][1] = IDTab[x + 1][y];
            tab[2][2] = IDTab[x + 1][y + 1];
        } else if (x == 0 && y == sizeY - 1) {
            tab[0][0] = 0;
            tab[0][1] = 0;
            tab[0][2] = 0;
            tab[1][0] = IDTab[x][y - 1];
            tab[1][1] = IDTab[x][y];
            tab[1][2] = 0;
            tab[2][0] = IDTab[x + 1][y - 1];
            tab[2][1] = IDTab[x + 1][y];
            tab[2][2] = 0;
        } else if (x == sizeX - 1 && y == 0) {
            tab[0][0] = 0;
            tab[0][1] = IDTab[x - 1][y];
            tab[0][2] = IDTab[x - 1][y + 1];
            tab[1][0] = 0;
            tab[1][1] = IDTab[x][y];
            tab[1][2] = IDTab[x][y + 1];
            tab[2][0] = 0;
            tab[2][1] = 0;
            tab[2][2] = 0;
        } else if (x == sizeX - 1 && y == sizeY - 1) {
            tab[0][0] = IDTab[x - 1][y - 1];
            tab[0][1] = IDTab[x - 1][y];
            tab[0][2] = 0;
            tab[1][0] = IDTab[x][y - 1];
            tab[1][1] = IDTab[x][y];
            tab[1][2] = 0;
            tab[2][0] = 0;
            tab[2][1] = 0;
            tab[2][2] = 0;
        } else if (x == 0 && y > 0 && y < sizeY - 1) {
            tab[0][0] = 0;
            tab[0][1] = 0;
            tab[0][2] = 0;
            tab[1][0] = IDTab[x][y - 1];
            tab[1][1] = IDTab[x][y];
            tab[1][2] = IDTab[x][y + 1];
            tab[2][0] = IDTab[x + 1][y - 1];
            tab[2][1] = IDTab[x + 1][y];
            tab[2][2] = IDTab[x + 1][y + 1];
        } else if (x > 0 && x < sizeX - 1 && y == 0) {
            tab[0][0] = 0;
            tab[0][1] = IDTab[x - 1][y];
            tab[0][2] = IDTab[x - 1][y + 1];
            tab[1][0] = 0;
            tab[1][1] = IDTab[x][y];
            tab[1][2] = IDTab[x][y + 1];
            tab[2][0] = 0;
            tab[2][1] = IDTab[x + 1][y];
            tab[2][2] = IDTab[x + 1][y + 1];
        } else if (x == sizeX - 1 && y > 0 && y < sizeY - 1) {
            tab[0][0] = IDTab[x - 1][y - 1];
            tab[0][1] = IDTab[x - 1][y];
            tab[0][2] = IDTab[x - 1][y + 1];
            tab[1][0] = IDTab[x][y - 1];
            tab[1][1] = IDTab[x][y];
            tab[1][2] = IDTab[x][y + 1];
            tab[2][0] = 0;
            tab[2][1] = 0;
            tab[2][2] = 0;
        } else if (y == sizeY - 1 && x > 0 && x < sizeX - 1) {
            tab[0][0] = IDTab[x - 1][y - 1];
            tab[0][1] = IDTab[x - 1][y];
            tab[0][2] = 0;
            tab[1][0] = IDTab[x][y - 1];
            tab[1][1] = IDTab[x][y];
            tab[1][2] = 0;
            tab[2][0] = IDTab[x + 1][y - 1];
            tab[2][1] = IDTab[x + 1][y];
            tab[2][2] = 0;
        } else {
            tab[0][0] = IDTab[x - 1][y - 1];
            tab[0][1] = IDTab[x - 1][y];
            tab[0][2] = IDTab[x - 1][y + 1];
            tab[1][0] = IDTab[x][y - 1];
            tab[1][1] = IDTab[x][y];
            tab[1][2] = IDTab[x][y + 1];
            tab[2][0] = IDTab[x + 1][y - 1];
            tab[2][1] = IDTab[x + 1][y];
            tab[2][2] = IDTab[x + 1][y + 1];
        }

        return tab;
    }

    int weightR(int x, int y, boolean w) {
        int r = Integer.parseInt(rRange.getText());
        int[][] tmpTab = new int [2*r+1][2*r+1];
        int max = 0, max2 = 0;

        return max;
    }


    int pentrand(int x, int y, boolean w) {
        if (w) {
            tab = periodic(x, y);
        } else {
            tab = nonPeriodic(x, y);
        }
        Random generator = new Random();
        int a = generator.nextInt(4);
        if (a == 0) {
            tab[0][0] = 0;
            tab[1][0] = 0;
            tab[2][0] = 0;
        } else if (a == 1) {
            tab[0][2] = 0;
            tab[1][2] = 0;
            tab[2][2] = 0;
        } else if (a == 2) {
            tab[0][2] = 0;
            tab[0][1] = 0;
            tab[0][2] = 0;
        } else if (a == 3) {
            tab[2][0] = 0;
            tab[2][1] = 0;
            tab[2][2] = 0;
        }
        int[] pom = neighbours(tab);
        int max = 0;
        int max2 = 0;
        for (int i = 0; i < sizeN; i++) {
            if (pom[i] > max && i != 0) {
                max = pom[i];
                max2 = i;
            }
        }
        return max2;
    }

    int hexrand(int x, int y, boolean w) {
        if (w) {
            tab = periodic(x, y);
        } else {
            tab = nonPeriodic(x, y);
        }
        Random generator = new Random();
        int a = generator.nextInt(2);
        if (a == 0) {
            tab[0][2] = 0;
            tab[2][0] = 0;
        } else if (a == 1) {
            tab[0][0] = 0;
            tab[2][2] = 0;
        }
        int[] pom = neighbours(tab);
        int max = 0;
        int max2 = 0;
        for (int i = 0; i < sizeN; i++) {
            if (pom[i] > max && i != 0) {
                max = pom[i];
                max2 = i;
            }
        }
        return max2;
    }

    int hexleft(int x, int y, boolean w) {
        if (w) {
            tab = periodic(x, y);
        } else {
            tab = nonPeriodic(x, y);
        }
        tab[0][2] = 0;
        tab[2][0] = 0;
        int[] pom = neighbours(tab);
        int max = 0;
        int max2 = 0;
        for (int i = 0; i < sizeN; i++) {
            if (pom[i] > max && i != 0) {
                max = pom[i];
                max2 = i;
            }
        }
        return max2;
    }

    int hexright(int x, int y, boolean w) {
        if (w) {
            tab = periodic(x, y);
        } else {
            tab = nonPeriodic(x, y);
        }
        tab[0][0] = 0;
        tab[2][2] = 0;
        int[] pom = neighbours(tab);
        int max = 0;
        int max2 = 0;
        for (int i = 0; i < sizeN; i++) {
            if (pom[i] > max && i != 0) {
                max = pom[i];
                max2 = i;
            }
        }
        return max2;
    }

    int neumann(int x, int y, boolean w) {
        if (w) {
            tab = periodic(x, y);
        } else {
            tab = nonPeriodic(x, y);
        }
        tab[0][0] = 0;
        tab[0][2] = 0;
        tab[2][0] = 0;
        tab[2][2] = 0;
        int[] pom = neighbours(tab);
        int max = 0;
        int max2 = 0;
        for (int i = 0; i < sizeN; i++) {
            if (pom[i] > max && i != 0) {
                max = pom[i];
                max2 = i;
            }
        }
        return max2;
    }

    int moore(int x, int y, boolean w) {
        if (w) {
            tab = periodic(x, y);
        } else {
            tab = nonPeriodic(x, y);
        }
        int[] pom = neighbours(tab);
        int max = 0;
        int max2 = 0;
        for (int i = 0; i < sizeN; i++) {
            if (pom[i] > max && i != 0) {
                max = pom[i];
                max2 = i;
            }
        }
        return max2;
    }

    void fillLabels() {
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {

                if ((IDTab[i][j]) > 0) {
                    if ((IDTab[i][j]) < 10) {
                        labelTab[i][j].setStyle("-fx-background-color:" + colors[IDTab[i][j] - 1] + ";");
                    } else if (((IDTab[i][j]) >= 10) && ((IDTab[i][j]) < 20)) {
                        labelTab[i][j].setStyle("-fx-background-color:" + colors[(IDTab[i][j] - 10)] + ";");
                    } else {
                        labelTab[i][j].setStyle("-fx-background-color:" + colors[(IDTab[i][j] - 20)] + ";");
                    }
                }
            }
        }
    }
}