package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Bod
 */
public class Point extends Circle {

    public Point(double x, double y) {
        super();
        this.setRadius(7);
        this.setLayoutX(x + 14);
        this.setLayoutY(y + 14);

        int red = (int) (Math.random() * 256);
        int green = (int) (Math.random() * 256);
        int blue = (int) (Math.random() * 256);

        this.setFill(Color.rgb(red, green, blue));

    }
}
