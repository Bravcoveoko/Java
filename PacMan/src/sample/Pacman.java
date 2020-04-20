package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Iterator;

public class Pacman extends ImageView {

    private Text score;

    private int points = 0;
    private boolean gotBuff = false;

    private Image[] allImages = new Image[4];
    private Timeline timeline;

    private boolean newLevel = false;
    private boolean anotherLevel = false;

    public Pacman(double x, double y) {
        super();

        // Nacitanie obrazkov
        for(int i = 1; i < 5; i++) {
            this.allImages[i - 1] = new Image("pacman" + i + ".png", 27, 28, false, false);
        }
        Image image = new Image("pacman1.png", 27, 28, false, false);
        this.setImage(image);
        this.setLayoutX(x);
        this.setLayoutY(y + 0.5);

        // Timeline na Buff ktory trva 5 sekund
        this.timeline = new Timeline(new KeyFrame(Duration.seconds(5)));
        this.timeline.setOnFinished(e -> this.gotBuff = false);
        this.timeline.setCycleCount(1);

    }

    /**
     * Pacman sa na zaklade 'movement' pohne a az potom sa skontroluje ci sa narazil do niecoho
     * @param movement dany pohyb
     * @param observableList Skontroluje sa kazdy jeden Node s pacmanom
     * @param mv1-> ak sa jedna o firstInput tak sa na konci vynuluje
     * @return ci sa pacman moze pohnut alebo nie
     */
    private boolean canMove(int[] movement, ObservableList<Node> observableList, boolean mv1) {
        this.setNewImage(movement);

        // Prvotny pohyb
        this.setLayoutX(this.getLayoutX() + movement[0]);
        this.setLayoutY(this.getLayoutY() + movement[1]);

        boolean returnValue = true;

        // Iterujem cez vsetky Node
        Iterator<Node> iterator = observableList.iterator();
        while(iterator.hasNext()) {
            Node obj = iterator.next();

            // Kolizia so stenou
            if (obj instanceof Wall) {
                if (this.getBoundsInParent().intersects(obj.getBoundsInParent())) {
                    returnValue = false;
                    break;
                }

            // Kolizia s bodom
            }else if (obj instanceof Point) {
                if (this.getBoundsInParent().intersects(obj.getBoundsInParent())) {
                    this.points++;
                    this.score.setText("Score " + this.points);
                    iterator.remove();
                    returnValue = true;
                    break;
                }
            // Kolizia s buff objektom
            }else if (obj instanceof Buff) {
                if (this.getBoundsInParent().intersects(obj.getBoundsInParent())) {
                    this.gotBuff = true;
                    this.timeline.play();
                    iterator.remove();
                    returnValue = true;
                    break;
                }
            // Kolizia s koncvym blokom
            }else if (obj instanceof EndBlock) {
                if (this.getBoundsInParent().intersects(obj.getBoundsInParent()) && this.anotherLevel) {
                    System.out.println("Konec hry");
                    returnValue = false;
                    this.newLevel = true;
                    break;
                }
            }
        }

        // Nasledne sa pohne naspat
        this.setLayoutX(this.getLayoutX() + (movement[0] * -1));
        this.setLayoutY(this.getLayoutY() + (movement[1] * -1));

        if (!returnValue && mv1) {
            movement[0] = 0;
            movement[1] = 0;
        }

        return returnValue;
    }

    /**
     * Na zaklade pohybu sa zmeni aj jeho obrazok
     * @param movement pohyb
     */

    private void setNewImage(int[] movement) {
        if (movement[0] == 0 && movement[1] == 2) {
            this.setImage(this.allImages[1]);
        }else if (movement[0] == 0 && movement[1] == -2) {
            this.setImage(this.allImages[3]);
        }else if (movement[0] == 2 && movement[1] == 0) {
            this.setImage(this.allImages[0]);
        }else {
            this.setImage(this.allImages[2]);
        }
    }

    /**
     * Ci sa moze ist do dalsieho levelu
     */

    public void setAnotherLevel() {
        this.anotherLevel = true;
    }

    /**
     * Ci sa dostal do koncoveho bloku
     * @return boolean hodnota
     */

    public boolean getNewLevel() {
        return this.newLevel;
    }

    /**
     *
     * @return Aktualny pocet bodov
     */

    public int getPoints() {
        return this.points;
    }

    /**
     *
     * @return Je pacman zranitelny
     */

    public boolean gotBuff() {
        return this.gotBuff;
    }

    /**
     *
     * @param movement1 hlavny pohyb
     * @param movement2 sekundarny pohyb -> Neustale sa kontroluje ci je mozne pohybovat aj ked firstInput je nastaveny
     * @param observableList Vsetky Node prvky
     */

    public void move(int[] movement1, int[] movement2, ObservableList<Node> observableList) {
        if ((movement2[0] != 0 || movement2[1] != 0) && this.canMove(movement2, observableList, false)) {
            this.setLayoutX(this.getLayoutX() + movement2[0]);
            this.setLayoutY(this.getLayoutY() + movement2[1]);

            movement1[0] = 0;
            movement1[1] = 0;
            return;
        }

        if ((movement1[0] != 0 || movement1[1] != 0) && this.canMove(movement1, observableList, true)) {
            this.setLayoutX(this.getLayoutX() + movement1[0]);
            this.setLayoutY(this.getLayoutY() + movement1[1]);
        }



    }

    /**
     *
     * @param score aktualizovat skore
     */
    public void setScore(Text score) {
        this.score = score;
    }
}
