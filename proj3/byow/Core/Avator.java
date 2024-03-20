package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Avator extends Entity {
    public Avator(Position position, TETile tile) {
        super(position, tile);
        this.Type = Avator;
        this.image = Tileset.AVATAR;
    }
}
