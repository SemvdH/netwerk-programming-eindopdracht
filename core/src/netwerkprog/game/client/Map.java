package netwerkprog.game.client;

public class Map {
    private char[][] map;
    private int tileWidth = 16;

    public Map(int size) {
        this(size,size);
    }

    public Map(int width, int height) {
        this.map = new char[height][width];
    }

    public void setTileWidth(int tileWidth) {
        this.tileWidth = tileWidth;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public char[][] get() {
        return this.map;
    }

    /**
     * gets the char at the specified position
     * @param row the row of the element
     * @param col the column of the element
     * @return Character.MIN_VALUE if the element does not exist, the specified element otherwise.
     */
    public char get(int row, int col) {
        return (row < this.map.length && col < this.map[0].length) ? this.map[row][col] : Character.MIN_VALUE;
    }

    public Map(char[][] map) {
        this.map = map;
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
