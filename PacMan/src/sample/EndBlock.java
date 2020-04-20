package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * Blok do noveho levelu -> avsak zadny novy level neni :D
 */
public class EndBlock extends Rectangle {

    private Timeline timeline;
    private int count = 0;
    public EndBlock(double x, double y) {
        super(30, 30);
        this.timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> this.redraw()));
        this.timeline.setCycleCount(Animation.INDEFINITE);
        this.timeline.play();

        this.setVisible(false);

        this.setFill(Color.BLACK);
        this.setLayoutX(x);
        this.setLayoutY(y);

    }

    // Blikanie
    private void redraw() {
        if (this.count++ % 2 == 0) {
            this.count = 1;
            this.setFill(Color.WHITE);
        }else {
            this.setFill(Color.BLACK);
        }
    }

    public void stopTimeline() {
        this.timeline.stop();
    }
}
