package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;

public class Game extends Group {

    private ArrayList<String> map;

    // Samotny hrac
    private Pacman pacman;
    private int maxPoints = 0;

    // Blok kory sa vytvori ked hrac pozbiera vsetky gulicky
    private EndBlock endBlock;

    // Vsetci duchovia
    private ArrayList<Ghost> ghosts = new ArrayList<>();

    // Hlavny casovac ktory kazdych 10 milisekund aktualizuje hru
    private Timeline mainTimeline;

    // Hra si pamata dve zmacknute tlacitka
    private int[] firstInput = new int[]{0, 0};
    private int[] secondInput = new int[]{0, 0};

    public Game(Level level) {

        this.map = level.getMap();

        this.setFocusTraversable(true);
        this.setFocused(true);

        this.initializeMap();

        // Text na zobrazovanie skore
        Text score = new Text(380, 400, "Score: ");
        score.setStroke(Color.WHITE);
        score.setFont(Font.font("Serif", 30));
        this.getChildren().add(score);

        this.pacman.setScore(score);

        // Event handler na ziskanie tlacitka ktore sa stlacilo
        this.setOnKeyPressed(this::setMovement);
        this.mainTimeline = new Timeline(new KeyFrame(Duration.millis(10), e -> this.update()));
        this.mainTimeline.setCycleCount(Animation.INDEFINITE);
        this.mainTimeline.play();

    }

    /**
     *  Pri kazdom stlaceni tlacitka sa vzdy prepise 'secondInput'
     *  {0, -2} reprezentuje : 0 po x-ovej a -2 po y-ovej
     * @param e -> Tlacitko ktore sa zmacklo
     */

    private void setMovement(KeyEvent e) {
        this.secondInput = (e.getCode() == KeyCode.UP) ? new int[] {0, -2} :
                (e.getCode() == KeyCode.DOWN) ? new int[] {0, 2} :
                (e.getCode() == KeyCode.LEFT) ? new int[] {-2, 0} :
                (e.getCode() == KeyCode.RIGHT) ? new int[] {2, 0} : new int[] {0, 0};

    }

    /**
     * firstInput je hlavny pohyb ale vzdy sa kontroluje ci sa moze odbocit pomocou secondInput
     * Ak firstInput neobsahuje ziadny pohyb ([0, 0]) tak sa do neho vlozi secondInput
     */

    private void updateInputs() {
        if (this.firstInput[0] == 0 && this.firstInput[1] == 0) {
            this.firstInput[0] = this.secondInput[0];
            this.firstInput[1] = this.secondInput[1];

            this.secondInput[0] = 0;
            this.secondInput[1] = 0;
        }

    }

    /***
     *  Aktualizacia hry
     */

    public void update() {
        this.updateInputs();

        // Zmeni sa smer Pacmana
        this.pacman.move(this.firstInput, this.secondInput, this.getChildren());

        // aktualizacia celej logiky duchov
        for(Ghost ghost : this.ghosts) {
            // Ci narazili na Pacmana
            if (ghost.getBoundsInParent().intersects(this.pacman.getBoundsInParent()) && !this.pacman.gotBuff()) {
                this.mainTimeline.stop();
                return;
            }

            // Prepocitava sa trasa -> viac info v class Ghost
            if (ghost.isStackEmpty()) {
                ghost.setPacCoor((int)(this.pacman.getLayoutX() / 30), (int)(this.pacman.getLayoutY() / 30));
                ghost.initializeStack();
            }
            ghost.move();
        }

        // Ci sa pozbierali vsetky gulicky
        if (this.maxPoints == this.pacman.getPoints()) {
            this.endBlock.setVisible(true);
            this.pacman.setAnotherLevel();
        }

        // Pripraveny na novy level
        // Novy level bohuzial neni vytvoreny nakolko som nestihal :D
        if (this.pacman.getNewLevel()) {
            this.endBlock.stopTimeline();
            this.mainTimeline.stop();
        }

    }

    /**
     * Prechadza sa cela mapa z txt suboru a vykresluje sa na sccenu
     */

    private void initializeMap() {
        double yPac = 0;
        double xPac = 0;
        ArrayList<Integer> ghosts = new ArrayList<>();
        // Mapa ma fixnu velkost 31 X 28
        for (int y = 0; y < 31; y++) {
            for (int x = 0; x < 28; x++) {
                // '#' znaci stenu
                char c = this.map.get(y).charAt(x);
                if (c == '#' ) {
                    this.addObject(x * 30, y * 30, 1);
                // '*' znaci Bod
                }else if (c == '*' ){
                    this.addObject(x * 30, y * 30, 2);
                    this.maxPoints++;
                // '@' znaci Buff
                }else if (c == '@') {
                    this.addObject(x * 30, y * 30, 3);
                // 'H' znaci Hrdinu
                }else if (c == 'H') {
                    xPac = x * 30;
                    yPac = y * 30;
                // 'G' znaci duchov
                }else if (c == 'G') {
                    ghosts.add(x * 30);
                    ghosts.add(y * 30);
                // 'E' znaci blok na dokoncenie hry
                }else if (c == 'E') {
                    this.endBlock = new EndBlock(x *30, y *30);
                    this.getChildren().add(this.endBlock);
                }
            }
        }


        // Duchovia sa pridavaju na scenu predposleny lebo boli prekryvany bodmi
        for(int i = 0; i < ghosts.size(); i++) {
            addObject(ghosts.get(i++), ghosts.get(i), 4);
        }

        // Duchom sa nastavi pozicia pacmana.
        for(Node n : this.getChildren()) {
            if (n instanceof Ghost) {

                /* Prepocitavam na poziciu v this.map
                    Prikald: Pacman sa nachadza na pozici X: 390px a Y: 480px
                    ale na mape sa nachadza 390 / 30 = 13 a 480 / 30 = 16
                    Bere sa len cela cast
                 */

                ((Ghost) n).setPacCoor((int)(xPac / 30), (int)(yPac / 30));
            }
        }

        // Prida sa na scenu Pacman
        this.pacman = new Pacman(xPac, yPac);
        this.getChildren().add(this.pacman);
    }


    /**
     *
     * @param x -> X pozicia na scene v pixeloch
     * @param y -> Y pozicia na scene v pixeloch
     * @param objectNum -> jednoznacny identifikator
     */
    private void addObject(double x, double y, int objectNum) {
        Shape object = null;
        Ghost ghost = null;
        switch (objectNum) {
            case 1:
                object = new Wall(x, y);
                break;
            case 2:
                object = new Point(x, y);
                break;
            case 3:
                object = new Buff(x, y);
                break;
            case 4:
                ghost = new Ghost(x, y, this.map);
            case 5:
                object = new EndBlock(x ,y);
        }
        if (ghost != null) {
            this.getChildren().add(ghost);
            this.ghosts.add(ghost);
        }

        if (object != null) {
            this.getChildren().add(object);
        }
    }
}
