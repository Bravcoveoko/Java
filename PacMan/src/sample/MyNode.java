package sample;

public class MyNode {

    private int x;
    private int y;
    private MyNode predecessor;

    public MyNode(int x, int y, MyNode predecessor) {
        this.x = x;
        this.y = y;
        this.predecessor = predecessor;
    }

    /**
     *
     * @return Vrati MyNode ktorym bol objaveny v BFS
     */

    public MyNode getPredecessor() {
        return this.predecessor;
    }

    /**
     *
     * @return X pozicia na mape
     */
    public int getX() {
        return this.x;
    }

    /**
     *
     * @return Y pozicia na mape
     */
    public int getY() {
        return this.y;
    }
}
