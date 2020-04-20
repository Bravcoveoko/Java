package sample;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.*;

public class Ghost extends ImageView {

    // Sluzia na spravny pohyb ghosta
    private double oldValueY;
    private double oldValueX;

    /**
     * Enum pre BFS algorimus
     */
    private enum Movement {
        UP(0, -1),
        DOWN(0,1),
        RIGHT(1, 0),
        LEFT(-1, 0),
        NONE(0, 0);

        int xCor, yCor;

        Movement(int x, int y) {
            this.xCor = x;
            this.yCor = y;
        }
    }

    private Image[] allImages = new Image[4];

    // Aktualny pohyb
    private int[] movement = new int[] {0, 0};
    private ArrayList<String> map;

    // Pozicia Pacmana na mape
    private int xPacCoor = 0;
    private int yPacCoor = 0;

    // Na prepocitavanie dalsieho pohybu
    private Stack<int[]> movementStack;


    public Ghost(double x, double y, ArrayList<String> map) {
        super();

        this.movementStack = new Stack<>();
        this.map = map;

        // Nastavia sa obrazky
        for(int i = 1; i < 5; i++) {
            this.allImages[i - 1] = new Image("ghost" + i + ".png", 27, 28, false, false);
        }

        Image image = new Image("ghost1.png", 27, 28, false, false);
        this.setImage(image);

        this.setLayoutX(x);
        this.setLayoutY(y + 0.5);

        // Nastavi sa stara pozicia -> sluzia na prepocitavanie dalsieho smeru
        this.oldValueX = x;
        this.oldValueY = y + 0.5;

    }

    /**
     *  Ziska sa aktualna hodnota a prepocita sa do suradnice na mape
     * @param x X pacmana na mape
     * @param y Y pacmana na mape
     */

    public void setPacCoor(int x, int y) {
        this.xPacCoor = x;
        this.yPacCoor = y;
    }

    /**
     * Podla smeru sa nastavi obrazok
     */

    private void setNewImage() {
        if (this.movement[0] == 0 && this.movement[1] == -1) {
            this.setImage(this.allImages[1]);
        }else if (this.movement[0] == 0 && this.movement[1] == 1) {
            this.setImage(this.allImages[2]);
        }else if (this.movement[0] == -1 && this.movement[1] == 0) {
            this.setImage(this.allImages[3]);
        }else if(this.movement[0] == 1 && this.movement[1] == 0){
            this.setImage(this.allImages[0]);
        }
    }

    /**
     * BFS (Breadth-first search) algoritmus alebo aj 'Prehladavanie do sirky' -> Grafovy algoritmus ktory ma zlozitost O(m x n)
     * kde 'm' a 'n' reprezentuju velkost mapy (alebo pocet vrcholov a hran ked sa jedna o graf)
     *
     * Hlada najkratsiu cestu od konkretneho ducha(this) ku Pacmanovi
     *
     *
     * @return Vrati sa class MyNode ktora je obsahuje informacie o X a Y pozicii na mape a predchadzjuci MyNode ktorym bol objaveny
     */

    private MyNode BFS() {
        int ghostY = (int)(this.getLayoutY() / 30);
        int ghostX = (int)(this.getLayoutX() / 30);

        // Booleanovska reprezentacia mpay
        // Najprv je vsetko nenastivene
        boolean[][] visited = new boolean[31][28];

        /* BFS algoritmus pracuje s frontou ktora sa postupne naplna MyNode a kzdy jeden MyNode sa dalej spracuvava */
        Queue<MyNode> queue = new LinkedList<>();

        // Pozicia ghosta je uz navstivena
        visited[ghostY][ghostX] = true;

        // Vytvorime si novy MyNode (pozicia ghosta)
        MyNode s = new MyNode(ghostX, ghostY, null);

        queue.add(s);


        while(!queue.isEmpty()) {
            MyNode currentNode = queue.remove();

            // Ak sa najde pozicia kde sa vyskytoval Pacman tak sa vrati tento MyNode
            if (currentNode.getX() == this.xPacCoor && currentNode.getY() == this.yPacCoor) {
                return currentNode;
            }

            // Okontroluju sa vsetky 4 pohyby
            for(Movement move : Movement.values()) {
                if (move.xCor == 0 && move.yCor == 0) continue;
                int newX = currentNode.getX() + (move.xCor);
                int newY = currentNode.getY() + (move.yCor);

                // Ak bol dana pozicia navstivena alebo sa jedna o stenu (#) tak sa preskoci
                if (this.map.get(newY).charAt(newX) != '#' && !visited[newY][newX]) {
                    visited[newY][newX] = true;
                    MyNode nextNode = new MyNode(newX, newY, currentNode);
                    queue.add(nextNode);
                }
            }

        }

        // Inak sa vrati NULL -> avsak tato hodnota by nemala nikdy nastat
        return null;
    }

    /**
     * Tato rekurzivna metoda mi vypise cez ake policka na mape sa dostaneme od Pacmana ku ghostovi ( jedna sa o najkratsiu cestu )
     * Metoda sa vsak nevyuziva.
     * @param node MyNode pacmana
     */

    private void recur(MyNode node) {
        if (node == null)return;
        System.out.println(node.getX() + " -- " + node.getY());
        recur(node.getPredecessor());
    }

    /**
     * BFS mi vrati MyNode pacmana ktory mi cez getPredecessor() metodu vrati jeho predchodcu
     * Avsak cesta je od Pacmana -> Ghostovi. Ja som potreboval opacnu cestu
     *
     * Preto urobim rozdiel pozic aktulneho MyNod a jeho predchodcu a invertujem
     *
     * Na ukladanie smeru pouzijem Zasobnik ( stack ) aby som mal jednoduchsi pristup k poslednemu prvku
     *
     * @param node MyNode Pacmana
     */
    private void getPath(MyNode node) {
        MyNode pr = node.getPredecessor();
        this.movementStack.push(new int[]{0, 0});
        while(node != null && pr != null) {

            int x = pr.getX() - node.getX();
            int y = pr.getY() - node.getY();

            if (x == 0 && y == -1) {
                this.movementStack.push(new int[] {0, 1});
            }else if (x == 0 && y == 1) {
                this.movementStack.push(new int[] {0, -1});
            }else if (x == -1 && y == 0) {
                this.movementStack.push(new int[] {1, 0});
            }else if (x == 1 && y == 0){
                this.movementStack.push(new int[] {-1, 0});
            }else {
                this.movementStack.push(new int[] {0, 0});
            }
            node = pr;
            pr = pr.getPredecessor();
        }
    }

    /**
     * Ci je zasobnik pohybo prazdny
     * Nastane vtedy ked Ghost dojde na poziciu Pacmana ktoru ziskal
     * @return boolean
     */
    public boolean isStackEmpty() {
        return this.movementStack.isEmpty();
    }

    /**
     * Pohyb Ghosta je nasledovny. Ziska sa pozicia Pacmana cez BFS. Prepocita sa trasa k nemu ( vyuziva sa zasobnik )
     * Je to pozicia Pacmana na ktorej bol alebo je. Ghost ale musi tuto poziciu navstivit.
     *
     * Ked sa tato pozicia navstivi tak Zasobnik krokov je prazdny a musi sa opat ziskat pozicia pacmana.
     */

    public void initializeStack() {
        MyNode pacman = this.BFS();
        this.getPath(pacman);
        this.movement = this.movementStack.pop();

    }

    /**
     * Pohym ghosta.
     * Kontrolovat ci narazi do steny neni vobec nutne nakolko pohyb je vypocitany.
     */

    public void move() {

        double newPosX = ((double)((int)(this.oldValueX / 30)) + this.movement[0]) * 30;
        double newPosY = (((double)((int)(this.oldValueY / 30)) + this.movement[1]) * 30) + 0.5;

        if (this.getLayoutX() == newPosX  && this.getLayoutY() == newPosY) {
            if (this.movementStack.size() == 0) return;
            int[] newMove =  this.movementStack.pop();

            this.movement[0] = newMove[0];
            this.movement[1] = newMove[1];

            this.oldValueX = this.getLayoutX();
            this.oldValueY = this.getLayoutY();

            if (this.movementStack.size() == 0) return;
        }
        this.setNewImage();
        this.setLayoutX(this.getLayoutX() + this.movement[0]);
        this.setLayoutY(this.getLayoutY() + this.movement[1]);

    }
}
