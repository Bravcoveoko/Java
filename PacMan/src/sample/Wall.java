package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Stena
 */
public class Wall extends Rectangle {

    public Wall(double x, double y) {
        super(30, 30);

        this.setFill(Color.BLACK);
        this.setLayoutX(x);
        this.setLayoutY(y);

    }
}
