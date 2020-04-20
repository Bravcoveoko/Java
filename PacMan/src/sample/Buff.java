package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Specialna gulicka ktora vam dava schopnost byt na 5 sekund nezranitelny
 */
public class Buff extends Circle {

    public Buff(double x, double y) {
        super();
        this.setRadius(10);
        this.setLayoutX(x + 14);
        this.setLayoutY(y + 14);
        this.setFill(Color.BLACK);
    }
}
