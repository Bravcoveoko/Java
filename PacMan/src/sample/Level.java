package sample;

import java.io.*;
import java.util.ArrayList;

public class Level {

    private ArrayList<String> map = new ArrayList<>();
    public Level(File file) {
        this.fillMap(file);
    }

    private void fillMap(File file) {

        // Subor sa nacita po riadku a vlozi do ArrayListu -> Maticovy pristup
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String strCurrentLine;

            while ((strCurrentLine = br.readLine()) != null) {
                this.map.add(strCurrentLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Tento atribut sa bude pouzvat v classe Game
     * @return mapa
     */
    public ArrayList<String> getMap() {
        return this.map;
    }
}
