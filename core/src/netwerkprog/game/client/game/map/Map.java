package netwerkprog.game.client.game.map;

/**
 * Map class to hold a 2d array of tiles which will specify the map
 */
public class Map {
    private char[][] map;

    public Map(int size) {
        this(size,size);
    }

    public Map(String[] strings) {
        if (strings == null) {
            throw new NullPointerException("The array of strings given cannot be null!");
        }

        int width = 0;
        //get max width in string array
        for (String s : strings) {
            if (s.length() > width) {
                width = s.length();
            }
        }
        char[][] res = new char[strings.length][width];
        for (int i = 0; i < strings.length; i++) {
            res[i] = strings[i].toCharArray();
        }
        this.map = res;
    }

    public Map(int width, int height) {
        this.map = new char[height][width];
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < getHeight(); row++) {
            for (int col = 0; col < getWidth(); col++) {
                sb.append(this.map[row][col]);
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
