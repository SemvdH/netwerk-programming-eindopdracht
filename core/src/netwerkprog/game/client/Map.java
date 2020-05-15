package netwerkprog.game.client;

public class Map {
    private char[][] map;

    public Map(int size) {
        this(size,size);
    }

    public Map(int width, int height) {
        this.map = new char[height][width];
    }

    public char get(int x, int y) {
        return this.map[y][x];
    }

    /**
     * gets the height of this map
     * @return -1 if the map is null or is empty, otherwise the height.
     */
    public int getHeight() {
        return this.map == null || this.map.length == 0 ? -1 : this.map.length;
    }

    /**
     * gets the width of this map
     * @return -1 if the map is null or is empty, otherwise the width
     */
    public int getWidth() {
        return this.map == null || this.map.length == 0 ? -1 : this.map[0].length;
    }


}
