package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import java.util.Arrays;
import java.util.Collections;


import java.awt.*;
import java.net.URL;
import java.util.*;
import java.util.List;

public class Controller implements Initializable {


    private int[][] IDTab;
    private int[][] energyTab;
    private boolean bounds;
    private int[][] tab = new int[3][3];
    private int sizeX = 0, sizeY = 0, sizeN = 0, choice = 0;
    private boolean[] genX;
    private boolean[] genY;
    private double ktParameter;
    private boolean[] colRTab;
    private boolean[] colGTab;
    private boolean[] colBTab;
    private int[][] col;
    private double stateDRX[][];
    private double densityDRX[][];
    double aA, bB, tT, criticRo;
    double[][] tabRo = new double[3][3];

    @FXML
    private GridPane gridPane;

    @FXML
    private TextField xRange;

    @FXML
    private TextField yRange;

    @FXML
    private TextField mcRange;

    @FXML
    private TextField nRange;

    @FXML
    private TextField rRange;

    @FXML
    private TextField ktRange;

    @FXML
    private Label[][] labelTab;

    @FXML
    void clearcCick() {
        gridPane.getChildren().clear();
    }

    @FXML
    void monteClick() {
        if (Double.parseDouble(ktRange.getText()) > 0) {
            ktParameter = Double.parseDouble(ktRange.getText());
        } else
            ktParameter = 1.0;
        if ((Integer.parseInt(mcRange.getText()) > 0) && (Integer.parseInt(mcRange.getText()) <= 1000)) {
            for (int i = 0; i < Integer.parseInt(mcRange.getText()); i++) {
                bounds = false;
                monteCarlo();
                fillLabels();
            }

        }
    }

    @FXML
    void energyClick() {
        fillEnergy();
    }

    @FXML
    void densClick(){
        fillDensity();
    }

    @FXML
    void scrolling() {
        scrol(choice, bounds);
        fillLabels();
    }

    @FXML
    void startClick() {
        sizeX = Integer.parseInt(xRange.getText());
        sizeY = Integer.parseInt(yRange.getText());
        sizeN = Integer.parseInt(nRange.getText()) + 1;
        labelTab = new Label[sizeX][sizeY];
        IDTab = new int[sizeX][sizeY];
        energyTab = new int[sizeX][sizeY];
        stateDRX = new double[sizeX][sizeY];
        densityDRX = new double[sizeX][sizeY];
        aA = 86710969050178.5;
        bB = 9.41268203527779;
        tT = 0;
        criticRo = 4215840142323.42;
        ktParameter = 0.6;
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                labelTab[i][j] = new Label();
                labelTab[i][j].setMinSize(1, 1);
                labelTab[i][j].setPrefSize(10, 10);
                labelTab[i][j].setStyle("-fx-background-color:#72adff;");
                gridPane.add(labelTab[i][j], i, j);
                energyTab[i][j] = 0;
                stateDRX[i][j] = 0.0;
                densityDRX[i][j] = 0.0;
            }
        }
    }

    @FXML
    void clickClick() {
        nRange.setEditable(false);
        rRange.setEditable(false);
    }

    private void setColors() {
        colRTab = new boolean[255];
        colGTab = new boolean[255];
        colBTab = new boolean[255];
        for (int i = 0; i < 255; i++) {
            colRTab[i] = true;
            colGTab[i] = true;
            colBTab[i] = true;
        }
        col = new int[255][3];
        for (int i = 0; i < sizeN; i++) {
            col[i][0] = getR();
            col[i][1] = getG();
            col[i][2] = getB();
        }
        for (int i = 0; i < 255; i++) {
            colRTab[i] = true;
            colGTab[i] = true;
            colBTab[i] = true;
        }
        for (int i = sizeN; i < 255; i++) {
            col[i][0] = getR();
            col[i][1] = 0;
            col[i][2] = 0;
        }
    }

    @FXML
    void evenClick() {
        for (int[] row : IDTab)
            Arrays.fill(row, 0);
        int a, b, c; //zmienne pomocnicze
        int counter = 0;
        setColors();
        if (Integer.parseInt(nRange.getText()) > 0) {
            int n = Integer.parseInt(nRange.getText());
            a = (int) Math.sqrt(n);
            b = Integer.parseInt(xRange.getText()) / (a * 2);
            c = Integer.parseInt(yRange.getText()) / (a * 2);
            for (int i = b; i < Integer.parseInt(xRange.getText()); i = i + 2 * b) {
                for (int j = c; j < Integer.parseInt(yRange.getText()); j = j + 2 * c) {
                    IDTab[i][j] = counter;
                    labelTab[i][j].setStyle("-fx-background-color: rgb(" + col[IDTab[i][j]][0] + "," + col[IDTab[i][j]][1] + "," + col[IDTab[i][j]][2] + ");");
                    counter++;
                }
            }

        }
    }

    @FXML
    void DRXClick() {
        for (int i = 0; i < 100; i++) {
            recrystallize();
        }
        fillLabels();
    }

    @FXML
    void randomClick() {
        for (int[] row : IDTab)
            Arrays.fill(row, 0);
        Random g = new Random(); //zmiennna do losowania
        int a, b; //zmienne pomocnicze
        setColors();
        if (Integer.parseInt(nRange.getText()) > 0) {
            int n = Integer.parseInt(nRange.getText());
            for (int i = 1; i <= n; i++) {
                a = g.nextInt(Integer.parseInt(xRange.getText()));
                b = g.nextInt(Integer.parseInt(yRange.getText()));
                if (IDTab[a][b] == 0) {
                    IDTab[a][b] = i;
                    labelTab[a][b].setStyle("-fx-background-color: rgb(" + col[IDTab[a][b]][0] + "," + col[IDTab[a][b]][1] + "," + col[IDTab[a][b]][2] + ");");
                } else
                    i--;
            }
        }
    }


    @FXML
    void withRClick() {
        for (int[] row : IDTab)
            Arrays.fill(row, 0);
        Random g = new Random(); //zmiennna do losowania
        int a, b, j, k, l, m; //zmienne pomocnicze
        int counter;
        setColors();
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
                    labelTab[a][b].setStyle("-fx-background-color: rgb(" + col[IDTab[a][b]][0] + "," + col[IDTab[a][b]][1] + "," + col[IDTab[a][b]][2] + ");");
                } else
                    i--;
            }
        }

    }

    @FXML
    void nonPeriodicClick() {
        bounds = false;
    }

    @FXML
    void periodicClick() {
        bounds = true;
    }

    @FXML
    void hexClick() {
        choice = 5;
    }

    @FXML
    void hexRClick() {
        choice = 4;
    }

    @FXML
    void hexLClick() {
        choice = 3;
    }

    @FXML
    void mooreClick() {
        choice = 1;
    }

    @FXML
    void nWithRClick() {
        choice = 7;
    }

    @FXML
    void neumannClick() {
        choice = 2;
    }


    @FXML
    void pentClick() {
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
        mcRange.setText("0");
        ktRange.setText("0");
        bounds = false;

        gridPane.setAlignment(Pos.CENTER);
        while (gridPane.getRowConstraints().size() > 0) {
            gridPane.getRowConstraints().remove(0);
        }

        while (gridPane.getColumnConstraints().size() > 0) {
            gridPane.getColumnConstraints().remove(0);
        }
    }

    private void scrol(int a, boolean b) {
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
                    }
                } else {
                    apt[i][j] = IDTab[i][j];
                }

            }
        }
        for (int i = 0; i < sizeX; i++) {
            if (sizeY >= 0) System.arraycopy(apt[i], 0, IDTab[i], 0, sizeY);
        }

    }


    private int[] neighbours(int[][] tab) {
        int[] pomid = new int[sizeN];
        for (int i = 0; i < sizeN; i++) {
            pomid[i] = 0;
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!(i == 1 && j == 1)) {
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

    private int[][] periodic(int x, int y) {
        if (x == 0 && y == 0) {
            first(x, y, sizeX);
        } else if (x == 0 && y == sizeY - 1) {
            second(x, y, sizeX);
        } else if (x == sizeX - 1 && y == 0) {
            third(x, y, sizeY);
        } else if (x == sizeX - 1 && y == sizeY - 1) {
            fourth(x, y, IDTab[x]);
            tab[2][0] = IDTab[0][y - 1];
            tab[2][1] = IDTab[0][y];
            tab[2][2] = IDTab[0][0];
        } else if (x == 0 && y > 0 && y < sizeY - 1) {
            fifth(x, y, sizeX);
        } else if (x > 0 && x < sizeX - 1 && y == 0) {
            first(x, y, x);
        } else if (x == sizeX - 1 && y > 0 && y < sizeY - 1) {
            third(x, y, y);
        } else if (y == sizeY - 1 && x > 0 && x < sizeX - 1) {
            second(x, y, x);
        } else {
            fifth(x, y, x);
        }
        return tab;
    }

    private void fifth(int x, int y, int sizeX) {
        tab[0][0] = IDTab[sizeX - 1][y - 1];
        tab[0][1] = IDTab[sizeX - 1][y];
        tab[0][2] = IDTab[sizeX - 1][y + 1];
        tab[1][0] = IDTab[x][y - 1];
        tab[1][1] = IDTab[x][y];
        tab[1][2] = IDTab[x][y + 1];
        tab[2][0] = IDTab[x + 1][y - 1];
        tab[2][1] = IDTab[x + 1][y];
        tab[2][2] = IDTab[x + 1][y + 1];
    }

    private void fourth(int x, int y, int[] ints) {
        tab[0][0] = IDTab[x - 1][y - 1];
        tab[0][1] = IDTab[x - 1][y];
        tab[0][2] = IDTab[x - 1][0];
        tab[1][0] = ints[y - 1];
        tab[1][1] = ints[y];
        tab[1][2] = ints[0];
    }

    private void third(int x, int y, int sizeY) {
        tab[0][0] = IDTab[x - 1][sizeY - 1];
        tab[0][1] = IDTab[x - 1][y];
        tab[0][2] = IDTab[x - 1][y + 1];
        tab[1][0] = IDTab[x][sizeY - 1];
        tab[1][1] = IDTab[x][y];
        tab[1][2] = IDTab[x][y + 1];
        tab[2][0] = IDTab[0][sizeY - 1];
        tab[2][1] = IDTab[0][y];
        tab[2][2] = IDTab[0][y + 1];
    }

    private void second(int x, int y, int sizeX) {
        fourth(sizeX, y, IDTab[x]);
        tab[2][0] = IDTab[x + 1][y - 1];
        tab[2][1] = IDTab[x + 1][y];
        tab[2][2] = IDTab[x + 1][0];
    }

    private void first(int x, int y, int sizeX) {
        tab[0][0] = IDTab[sizeX - 1][sizeY - 1];
        tab[0][1] = IDTab[sizeX - 1][y];
        tab[0][2] = IDTab[sizeX - 1][y + 1];
        tab[1][0] = IDTab[x][sizeY - 1];
        tab[1][1] = IDTab[x][y];
        tab[1][2] = IDTab[x][y + 1];
        tab[2][0] = IDTab[x + 1][sizeY - 1];
        tab[2][1] = IDTab[x + 1][y];
        tab[2][2] = IDTab[x + 1][y + 1];
    }

    private int[][] nonPeriodic(int x, int y) {
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
            fifth(x, y, x);
        }

        return tab;
    }

    private int pentrand(int x, int y, boolean w) {
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
            tab[0][0] = 0;
            tab[0][1] = 0;
            tab[0][2] = 0;
        } else if (a == 3) {
            tab[2][0] = 0;
            tab[2][1] = 0;
            tab[2][2] = 0;
        }
        return retMax();
    }

    private int retMax() {
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

    private double[][] nonPeriodicRo(int x, int y) {
        if (x == 0 && y == 0) {
            tabRo[0][0] = 0.0;
            tabRo[0][1] = 0.0;
            tabRo[0][2] = 0.0;
            tabRo[1][0] = 0.0;
            tabRo[1][1] = densityDRX[x][y];
            tabRo[1][2] = densityDRX[x][y + 1];
            tabRo[2][0] = 0.0;
            tabRo[2][1] = densityDRX[x + 1][y];
            tabRo[2][2] = densityDRX[x + 1][y + 1];
        } else if (x == 0 && y == sizeY - 1) {
            tabRo[0][0] = 0.0;
            tabRo[0][1] = 0.0;
            tabRo[0][2] = 0.0;
            tabRo[1][0] = densityDRX[x][y - 1];
            tabRo[1][1] = densityDRX[x][y];
            tabRo[1][2] = 0.0;
            tabRo[2][0] = densityDRX[x + 1][y - 1];
            tabRo[2][1] = densityDRX[x + 1][y];
            tabRo[2][2] = 0.0;
        } else if (x == sizeX - 1 && y == 0) {
            tabRo[0][0] = 0.0;
            tabRo[0][1] = densityDRX[x - 1][y];
            tabRo[0][2] = densityDRX[x - 1][y + 1];
            tabRo[1][0] = 0.0;
            tabRo[1][1] = densityDRX[x][y];
            tabRo[1][2] = densityDRX[x][y + 1];
            tabRo[2][0] = 0.0;
            tabRo[2][1] = 0.0;
            tabRo[2][2] = 0.0;
        } else if (x == sizeX - 1 && y == sizeY - 1) {
            tabRo[0][0] = densityDRX[x - 1][y - 1];
            tabRo[0][1] = densityDRX[x - 1][y];
            tabRo[0][2] = 0.0;
            tabRo[1][0] = densityDRX[x][y - 1];
            tabRo[1][1] = densityDRX[x][y];
            tabRo[1][2] = 0.0;
            tabRo[2][0] = 0.0;
            tabRo[2][1] = 0.0;
            tabRo[2][2] = 0.0;
        } else if (x == 0 && y > 0 && y < sizeY - 1) {
            tabRo[0][0] = 0.0;
            tabRo[0][1] = 0.0;
            tabRo[0][2] = 0.0;
            tabRo[1][0] = densityDRX[x][y - 1];
            tabRo[1][1] = densityDRX[x][y];
            tabRo[1][2] = densityDRX[x][y + 1];
            tabRo[2][0] = densityDRX[x + 1][y - 1];
            tabRo[2][1] = densityDRX[x + 1][y];
            tabRo[2][2] = densityDRX[x + 1][y + 1];
        } else if (x > 0 && x < sizeX - 1 && y == 0) {
            tabRo[0][0] = 0.0;
            tabRo[0][1] = densityDRX[x - 1][y];
            tabRo[0][2] = densityDRX[x - 1][y + 1];
            tabRo[1][0] = 0.0;
            tabRo[1][1] = densityDRX[x][y];
            tabRo[1][2] = densityDRX[x][y + 1];
            tabRo[2][0] = 0.0;
            tabRo[2][1] = densityDRX[x + 1][y];
            tabRo[2][2] = densityDRX[x + 1][y + 1];
        } else if (x == sizeX - 1 && y > 0 && y < sizeY - 1) {
            tabRo[0][0] = densityDRX[x - 1][y - 1];
            tabRo[0][1] = densityDRX[x - 1][y];
            tabRo[0][2] = densityDRX[x - 1][y + 1];
            tabRo[1][0] = densityDRX[x][y - 1];
            tabRo[1][1] = densityDRX[x][y];
            tabRo[1][2] = densityDRX[x][y + 1];
            tabRo[2][0] = 0.0;
            tabRo[2][1] = 0.0;
            tabRo[2][2] = 0.0;
        } else if (y == sizeY - 1 && x > 0 && x < sizeX - 1) {
            tabRo[0][0] = densityDRX[x - 1][y - 1];
            tabRo[0][1] = densityDRX[x - 1][y];
            tabRo[0][2] = 0.0;
            tabRo[1][0] = densityDRX[x][y - 1];
            tabRo[1][1] = densityDRX[x][y];
            tabRo[1][2] = 0.0;
            tabRo[2][0] = densityDRX[x + 1][y - 1];
            tabRo[2][1] = densityDRX[x + 1][y];
            tabRo[2][2] = 0.0;
        } else {
            tabRo[0][0] = densityDRX[sizeX - 1][y - 1];
            tabRo[0][1] = densityDRX[sizeX - 1][y];
            tabRo[0][2] = densityDRX[sizeX - 1][y + 1];
            tabRo[1][0] = densityDRX[x][y - 1];
            tabRo[1][1] = densityDRX[x][y];
            tabRo[1][2] = densityDRX[x][y + 1];
            tabRo[2][0] = densityDRX[x + 1][y - 1];
            tabRo[2][1] = densityDRX[x + 1][y];
            tabRo[2][2] = densityDRX[x + 1][y + 1];
        }

        return tabRo;
    }

    private double retMaxRo(int i, int j) {
        nonPeriodicRo(i, j);
        double max = tabRo[0][0];
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if (tabRo[x][y] > max) {
                    max = tabRo[x][y];
                }
            }
        }
        return max;
    }

    private int hexrand(int x, int y, boolean w) {
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
        return retMax();
    }

    private int hexleft(int x, int y, boolean w) {
        if (w) {
            tab = periodic(x, y);
        } else {
            tab = nonPeriodic(x, y);
        }
        tab[0][2] = 0;
        tab[2][0] = 0;
        return retMax();
    }

    private int hexright(int x, int y, boolean w) {
        if (w) {
            tab = periodic(x, y);
        } else {
            tab = nonPeriodic(x, y);
        }
        tab[0][0] = 0;
        tab[2][2] = 0;
        return retMax();
    }

    private int neumann(int x, int y, boolean w) {
        if (w) {
            tab = periodic(x, y);
        } else {
            tab = nonPeriodic(x, y);
        }
        tab[0][0] = 0;
        tab[0][2] = 0;
        tab[2][0] = 0;
        tab[2][2] = 0;
        return retMax();
    }

    private int moore(int x, int y, boolean w) {
        if (w) {
            tab = periodic(x, y);
        } else {
            tab = nonPeriodic(x, y);
        }
        return retMax();
    }

    private int getX() {
        Random r = new Random();
        int i = r.nextInt(sizeX);
        if (genX[i]) {
            genX[i] = false;
            return i;
        } else {
            return getX();
        }
    }

    private int getR() {
        Random r = new Random();
        int i = r.nextInt(255);
        if (colRTab[i]) {
            colRTab[i] = false;
            return i;
        } else {
            return getR();
        }
    }

    private int getG() {
        Random r = new Random();
        int i = r.nextInt(255);
        if (colGTab[i]) {
            colGTab[i] = false;
            return i;
        } else {
            return getG();
        }
    }

    private int getB() {
        Random r = new Random();
        int i = r.nextInt(255);
        if (colBTab[i]) {
            colBTab[i] = false;
            return i;
        } else {
            return getB();
        }
    }

    private int getY() {
        Random r = new Random();
        int i = r.nextInt(sizeX);
        if (genY[i]) {
            genY[i] = false;
            return i;
        } else {
            return getY();
        }
    }

    private void monteCarlo() {
        genX = new boolean[sizeX];
        genY = new boolean[sizeY];
        for (int o = 0; o < sizeX; o++)
            genX[o] = true;
        for (int o = 0; o < sizeY; o++)
            genY[o] = true;
        Random r = new Random();
        int startEnergy = 0, newEnergy = 0, ID, tempX = getX(), tempY = getY();
        int[] tempEnergyTab;
        int[][] omg;
        for (int i = 0; i < (sizeX * sizeY); i++) {
            tab = nonPeriodic(tempX, tempY);
            omg = tab;
            ID = IDTab[tempX][tempY];
            int[] tempTab = neighbours(tab);
            for (int k = 1; k < sizeN; k++) {
                if ((tempTab[k] > 0) && (k != ID)) {
                    startEnergy += tempTab[k];
                }
            }
            for (int k = 1; k < sizeN; k++) {
                if ((tempTab[k] > 0) && (k != ID)) {
                    omg[1][1] = k;
                    tempEnergyTab = neighbours(omg);
                    for (int m = 1; m < sizeN; m++) {
                        if ((tempEnergyTab[k] > 0) && (m != k)) {
                            newEnergy += tempEnergyTab[m];
                        }
                    }
                    if ((newEnergy - startEnergy) <= 0) {
                        startEnergy = newEnergy;
                        ID = k;
                    } else if ((newEnergy - startEnergy) > 0) {
                        double probability = Math.exp(-(newEnergy - startEnergy) / ktParameter) * 100;
                        double randNumber = r.nextDouble() * 100;

                        if (randNumber < probability) {
                            startEnergy = newEnergy;
                            ID = k;
                        }
                    }
                }
            }
            IDTab[tempX][tempY] = ID;
            energyTab[tempX][tempY] = startEnergy;
            //System.out.println(startEnergy);
        }
    }

    public boolean edge(int i, int j) {
        tab = nonPeriodic(i, j);
        int[] pom = neighbours(tab);
        for (int k = 1; k < sizeN; k++)
            if ((k != IDTab[i][j]) && (pom[k] > 0))
                return true;
        return false;
    }

    private void dislocations() {
        double dis = aA / bB + (1 - aA / bB) * Math.exp(-bB * tT);
        double avgDis = dis / (sizeX * sizeY);
        double eq = 0.3;
        double small = 5E11;
        int id, x, y;
        List<Point> ePoints = new ArrayList<>();
        List<Point> nePoints = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                densityDRX[i][j] = densityDRX[i][j] + eq * avgDis;
                dis -= eq * avgDis;
                if (edge(i, j)) {
                    ePoints.add(new Point(i, j));
                } else {
                    nePoints.add(new Point(i, j));
                }
            }
        }
        //System.out.println(edgeCounter + " " + ePoints.size());
        //System.out.println(notEdgeCounter + " " + nePoints.size());
        while (dis > small) {
            int d = r.nextInt(100);
            if (d > 20) {
                id = r.nextInt(ePoints.size());
                x = ePoints.get(id).x;
                y = ePoints.get(id).y;
            } else {
                id = r.nextInt(nePoints.size());
                x = nePoints.get(id).x;
                y = nePoints.get(id).y;
            }
            densityDRX[x][y] = densityDRX[x][y] + small;
            dis -= small;
            //System.out.println(dis);
        }
    }

    private void DRX() {
        dislocations();

            for (int i = 0; i < sizeX; i++) {
                for (int j = 0; j < sizeY; j++) {
                    //System.out.println(densityDRX[i][j]);
                    if ((densityDRX[i][j] >= criticRo) && (edge(i, j))) {
                        System.out.println("TAK");
                        IDTab[i][j] = sizeN++;
                        System.out.println(sizeN);
                        densityDRX[i][j] = 0.0;
                    }
                }
            }

        if (tT > 0) {
            for (int i = 0; i < sizeX; i++) {
                for (int j = 0; j < sizeY; j++) {
                    tab = nonPeriodic(i, j);
                    int[] pom = neighbours(tab);
                    int tem = 0;
                    for(int m = 1; m <= Integer.parseInt(nRange.getText()); m++)
                        tem+=pom[m];
                    int max = 0;
                    int [] temp = new int[sizeN];
                    for (int m = 0; m < 3; m++) {
                        for (int o = 0; o < 3; o++)
                            temp[tab[m][o]]++;
                    }
                    max = Arrays.stream(temp).max().getAsInt();

//                    System.out.println(max);
//                    System.out.println(tem);
                    if ((tem < 8) && (retMaxRo(i, j) < stateDRX[i][j])) {
                        System.out.println("TUTAJ");
                        IDTab[i][j] = max;
                        densityDRX[i][j] = 0.0;
                    }
                }
            }
        }
        tT = tT + 0.01;
        System.out.println(tT);
    }

    private void recrystallize() {
        DRX();
        stateDRX = densityDRX;
    }

    private void fillEnergy() {
        int max = energyTab[0][0], tempMax;
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                tempMax = energyTab[i][j];
                if (tempMax > max) {
                    max = tempMax;
                }
            }
        }
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                if (energyTab[i][j] < 0) {
                    labelTab[i][j].setStyle("-fx-background-color: rgb(100, 100, 100);");
                } else if (energyTab[i][j] == 0) {
                    labelTab[i][j].setStyle("-fx-background-color: rgb(0, 0, 0);");
                } else if ((energyTab[i][j] * 255 / max) >= 255) {
                    labelTab[i][j].setStyle("-fx-background-color: rgb(0, 255, 0);");
                } else {
                    labelTab[i][j].setStyle("-fx-background-color: rgb(0," + (energyTab[i][j] * 255 / max) + ",0);");
                }
            }
        }
    }

    private void fillDensity() {
        double max = densityDRX[0][0], tempMax;
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                tempMax = densityDRX[i][j];
                if (tempMax > max) {
                    max = tempMax;
                }
            }
        }
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                if (densityDRX[i][j] < 0) {
                    labelTab[i][j].setStyle("-fx-background-color: rgb(100, 100, 100);");
                } else if (densityDRX[i][j] == 0) {
                    labelTab[i][j].setStyle("-fx-background-color: rgb(0, 0, 0);");
                } else if ((densityDRX[i][j] * 255 / max) >= 255) {
                    labelTab[i][j].setStyle("-fx-background-color: rgb(0, 0, 255);");
                } else {
                    labelTab[i][j].setStyle("-fx-background-color: rgb(0, 0," + (densityDRX[i][j] * 255 / max) +");");
                }
            }
        }
    }

    private void fillLabels() {

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {

                if ((IDTab[i][j]) > 0) {
                    labelTab[i][j].setStyle("-fx-background-color: rgb(" + col[IDTab[i][j]][0] + "," + col[IDTab[i][j]][1] + "," + col[IDTab[i][j]][2] + ");");
                }
            }
        }
    }

}