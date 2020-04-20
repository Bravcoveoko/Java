package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Group root = new Group();

        // Mapa je nakreslena v txt subore
        File file = new File("map.txt");

        // V classe Level sa txt subor rozdeli
        Level level = new Level(file);

        // Samotna hra
        Game game = new Game(level);

        root.getChildren().add(game);
        primaryStage.setTitle("Pac-man");
        primaryStage.setScene(new Scene(root, 840, 940, Color.WHITE));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
