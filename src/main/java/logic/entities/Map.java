package logic.entities;

public class Map {
    private int length; // X
    private int height; // Y
    private int[][] field; // 0 - free, 1 - occupied,

    public void placeUnit(int x, int y) {
        if (isValidPosition(x, y)) {
            field[x][y] = 1;
        }
    }

    public void removeUnit(int x, int y) {
        if (x >= 0 && x < length && y >= 0 && y < height) {
            field[x][y] = 0;
        }
    }

    public Map(int length, int height) {
        if (length <= 0 || height <= 0) {
            throw new IllegalArgumentException("Length and height must be positive");
        }
        this.length = length;
        this.height = height;
        this.field = new int[length][height];
    }

    public int getLength() {
        return length;
    }

    public int getHeight() {
        return height;
    }

    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < length && y >= 0 && y < height && field[x][y] == 0;
    }

    public void setNewMap(int length, int height) {
        if (length > 0 && height > 0) {
            this.length = length;
            this.height = height;
            this.field = new int[length][height];
        }
    }
}