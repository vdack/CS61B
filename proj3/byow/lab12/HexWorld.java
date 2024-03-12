package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {

    private final static int WIDTH = 30;
    private final static int HEIGHT = 32;
    private final static Random RANDOM = new Random();

    /**
     * get a tuple of x and y.
     */
    private static class Position{
        int x;
        int y;
        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
        private Position getDeltaPosition(int deltaX, int deltaY) {
            //return a position adding delta x and delta y.
            // if negative, return null.
            int x = this.x + deltaX;
            int y = this.y + deltaY;
            if (x < 0 || y < 0) {
                return null;
            }
            return new Position(x, y);

        }
        private Position getDownPosition() {
            return getDeltaPosition(0, -6);
        }
        private Position getLeftDownPosition() {
            return getDeltaPosition(-5, -3);
        }
        private Position getRightDownPosition() {
            return getDeltaPosition(5, -3);
        }
    }



    /**
     *
     * @param initPosition the top subHex position(top&left) of the BigHex.
     * @return a list with all subHex positions(top&left).
     *
     * ignore all the subHex positions with negative number.
     */
    private static List<Position> getHexPositionsOfBigHex(Position initPosition) {
        List<Position> subHexPositions = new ArrayList<>();
        Position position = initPosition;
        subHexPositions.add(position);
        for (int i = 0; i < 4; i += 1) {
            position = position.getDownPosition();
            subHexPositions.add(position);
        }
        position = initPosition.getLeftDownPosition();
        subHexPositions.add(position);
        for (int i = 0; i < 3; i += 1) {
            position = position.getDownPosition();
            subHexPositions.add(position);
        }
        position = initPosition.getLeftDownPosition().getLeftDownPosition();
        subHexPositions.add(position);
        for (int i = 0; i < 2; i += 1) {
            position = position.getDownPosition();
            subHexPositions.add(position);
        }
        position = initPosition.getRightDownPosition();
        subHexPositions.add(position);
        for (int i = 0; i < 3; i += 1) {
            position = position.getDownPosition();
            subHexPositions.add(position);
        }
        position = initPosition.getRightDownPosition().getRightDownPosition();
        subHexPositions.add(position);
        for (int i = 0; i < 2; i += 1) {
            position = position.getDownPosition();
            subHexPositions.add(position);
        }
        return subHexPositions;

    }

    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(6);
        switch (tileNum) {
            case 0: return Tileset.MOUNTAIN;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.WATER;
            case 3: return Tileset.GRASS;
            case 4: return Tileset.SAND;
            case 5: return Tileset.TREE;
            default: return Tileset.NOTHING;
        }
    }

    private static void setTileAt(TETile[][] world, int x, int y, TETile tileType, int maxWidth, int maxHeight) {
        //a safe way to set tile when out of range.
        if (x < 0 || x >= maxWidth || y < 0 || y >= maxHeight) return;
        world[x][y] = tileType;
    }

    /**
     *
     * @param world the world being set.
     * @param position left top x position of the hex.
     * @param tileType the thing fill in the hex.
     *
     * Each hex has six floors, from top to down are 3-5-7-7-5-3.
     * one hex on/beneath another with 0 delta x, 6 delta y
     * one hex beside another with 5 delta x, 3 delta y
     */
    private static void setHexAt(TETile[][] world, Position position, TETile tileType) {
        int maxWidth = world.length;
        int maxHeight = world[0].length;
        int x = position.x;
        int y = position.y;
        for (int offset = 0; offset < 3; offset += 1) {
            setTileAt(world, x + offset, y, tileType, maxWidth, maxHeight);
        }
        for (int offset = -1; offset < 4; offset += 1) {
            setTileAt(world, x + offset, y - 1, tileType, maxWidth, maxHeight);
        }
        for (int offset = -2; offset < 5; offset += 1) {
            setTileAt(world, x + offset, y - 2, tileType, maxWidth, maxHeight);
        }
        for (int offset = -2; offset < 5; offset += 1) {
            setTileAt(world, x + offset, y - 3, tileType, maxWidth, maxHeight);
        }
        for (int offset = -1; offset < 4; offset += 1) {
            setTileAt(world, x + offset, y - 4, tileType, maxWidth, maxHeight);
        }
        for (int offset = 0; offset < 3; offset += 1) {
            setTileAt(world, x + offset, y - 5, tileType, maxWidth, maxHeight);
        }

    }

    public static void main(String[] args){
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int i = 0; i < WIDTH; i += 1) {
            for (int j = 0; j < HEIGHT; j += 1) {
                world[i][j] = Tileset.NOTHING;
            }
        }

        List<Position> hexPositions = getHexPositionsOfBigHex(new Position(WIDTH/2 - 2, HEIGHT - 1));
        for (Position position : hexPositions) {
            if (position == null) {
                continue;
            }
            setHexAt(world, position, randomTile());
        }

        ter.renderFrame(world);

    }
}
