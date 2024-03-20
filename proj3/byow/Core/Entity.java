package byow.Core;

import byow.TileEngine.TETile;

public class Entity {
    static final int Avator = 1;
    static final int Monster = 2;
    protected int Type;
    private Position currentPosition;
    private TETile tileBeneath;
    protected TETile image;

    public Entity(Position position, TETile tile) {
        this.currentPosition = position;
        this.tileBeneath = tile;
        this.image = null;
    }

    public void moveTo(int x, int y, TETile tile) {
        currentPosition = new Position(x, y);
        tileBeneath = tile;
    }
    public int getType(){
        return Type;
    }
    public Position getCurrentPosition(){
        return currentPosition;
    }

    public TETile getTileBeneath() {
        return tileBeneath;
    }

    public TETile getImage(){
        return image;
    }
}
