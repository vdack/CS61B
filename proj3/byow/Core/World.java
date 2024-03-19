package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.List;
import java.util.Random;

public class World {
    private TETile[][] world;
    private int width;
    private int height;
    private long seed;
    private int widthOffset;
    private int heightOffset;
    public World(int width, int height, long seed, int widthOffset, int heightOffset){
        this.width = width;
        this.height = height;
        this.seed = seed;
        this.widthOffset = widthOffset;
        this.heightOffset = heightOffset;
    }
    //=================public-method-to-get-some-private-variables=================================
    //

    public TETile[][] getWorld() {
        return world;
    }

    //==================private-method-for-easier-programming=======================================
    //
    private void clear() {
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    private Room[][] constructRooms() {
        Random r = new Random(seed);
        Room[][] rooms = new Room[4][3];
        for (int i = 0; i < 4; i += 1) {
            for (int j = 0; j < 3; j += 1) {
                rooms[i][j] = null;
            }
        }
        rooms[0][0] = new Room(widthOffset,heightOffset,0, 0, r.nextLong());
        rooms[3][2] = new Room(widthOffset + 75, heightOffset + 50, 3, 2, r.nextLong());
        for (int i = 0; i < 4; i += 1) {
            for (int j = 0; j < 3; j += 1) {
                if (r.nextInt() % 3 == 0 || i + j == 0 || i + j == 5) {
                    continue;
                }
                rooms[i][j] = new Room(widthOffset + i * 25, heightOffset + j* 25, i, j, r.nextLong());
            }
        }
        return rooms;
    }
    private void setRooms(Room[][] rooms) {
        for (Room[] roomss : rooms) {
            for (Room room : roomss) {
                if (room == null) {
                    continue;
                }
                TETile type = Tileset.FLOOR;
                Position position = room.topLeftPosition;
                int x = position.getX();
                int y = position.getY();
                for (int i = 1; i < room.roomWidth - 1; i += 1) {
                    for (int j = 1; j < room.roomHeight - 1; j += 1) {
                        world[x + i][y + j] = type;
                    }
                }
                for (int i = 0; i < room.roomWidth; i += 1) {
                    world[x + i][y] = Tileset.WALL;
                    world[x + i][y + room.roomHeight - 1] = Tileset.WALL;
                }
                for (int j = 0; j < room.roomHeight; j += 1) {
                    world[x][y + j] = Tileset.WALL;
                    world[x + room.roomWidth - 1] [y + j] = Tileset.WALL;
                }
//                world[room.getTopDoorPosition().getX()][room.getTopDoorPosition().getY()] = Tileset.UNLOCKED_DOOR;
//                world[room.getBottomDoorPosition().getX()][room.getBottomDoorPosition().getY()] = Tileset.UNLOCKED_DOOR;
//                world[room.getLeftDoorPosition().getX()][room.getLeftDoorPosition().getY()] = Tileset.UNLOCKED_DOOR;
//                world[room.getRightDoorPosition().getX()][room.getRightDoorPosition().getY()] = Tileset.UNLOCKED_DOOR;
            }
        }
    }
    //==================public-method-to-change-the-object===========================================

    public void initialize() {
        world = new TETile[width][height];
        clear();
    }
    public void menu(){
        clear();
        world[30][50] = Tileset.FLOWER;
        world[60][50] = Tileset.MOUNTAIN;
        world[90][50] = Tileset.WATER;
    }
    public void worldGenerate() {
        //TODO
        Room[][] rooms = constructRooms();
        setRooms(finalWorldFrame, rooms);
        List<Road> roads = constructRoads(rooms);
        setRoads(roads, finalWorldFrame);
    }

}
