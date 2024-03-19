package byow.Core;

import antlr.StringUtils;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.InputDemo.InputSource;
import byow.InputDemo.StringInputDevice;
import byow.InputDemo.KeyboardInputSource;

import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class
Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 120;
    public static final int HEIGHT = 90;
    public static final int WIDTH_OFFSET = 10;
    public static final int HEIGHT_OFFSET = 5;

    private static final int MENU_MODE = 0;
    private static final int GAME_MODE = 1;
    private static final int SEED_MODE = 2;

    //initial interface
    private TETile[][] menu() {
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        world[30][50] = Tileset.FLOWER;
        world[60][50] = Tileset.MOUNTAIN;
        world[90][50] = Tileset.WATER;
        return world;
    }


    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        TETile[][] finalWorldFrame = null;
        Random r = new Random();
        Long seedNumber = r.nextLong();
        System.out.println("current world seed is :" + seedNumber);

        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        finalWorldFrame = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                finalWorldFrame[x][y] = Tileset.NOTHING;
            }
        }
        Room[][] rooms = constructRooms(seedNumber);
        drawRooms(finalWorldFrame, rooms);
        List<Road> roads = constructRoads(rooms);
        drawRoads(roads, finalWorldFrame);
        ter.renderFrame(finalWorldFrame);


    }


    private Room[][] constructRooms(long seed) {
        Random r = new Random(seed);
        Room[][] rooms = new Room[4][3];
        for (int i = 0; i < 4; i += 1) {
            for (int j = 0; j < 3; j += 1) {
                rooms[i][j] = null;
            }
        }
        rooms[0][0] = new Room(WIDTH_OFFSET,HEIGHT_OFFSET,0, 0, r.nextLong());
        rooms[3][2] = new Room(WIDTH_OFFSET + 75, HEIGHT_OFFSET + 50, 3, 2, r.nextLong());
        for (int i = 0; i < 4; i += 1) {
            for (int j = 0; j < 3; j += 1) {
                if (r.nextInt() % 3 == 0 || i + j == 0 || i + j == 5) {
                    continue;
                }
                rooms[i][j] = new Room(WIDTH_OFFSET + i * 25, HEIGHT_OFFSET + j* 25, i, j, r.nextLong());
            }
        }
        return rooms;
    }

    private void drawRooms(TETile[][] world, Room[][] rooms) {
        int width = world.length;
        int height = world[0].length;
        for (Room[] roomss : rooms) {
            for (Room room : roomss) {
                if (room == null) {
                    continue;
                }
                TETile type = Tileset.FLOOR;
//                switch ((((int)room.getSeed() % 5) + 10) % 5) {
//                    case 0: type = Tileset.FLOOR;
//                        break;
//                    case 1: type = Tileset.GRASS;
//                        break;
//                    case 2: type = Tileset.SAND;
//                        break;
//                    case 3: type = Tileset.FLOWER;
//                        break;
//                    case 4: type = Tileset.WATER;
//                        break;
//                    default:
//                        System.out.println("default type in room floor with room" + room.topLeftPosition.getX() + " " + room.topLeftPosition.getY());
//                        type = Tileset.FLOOR;
//                }
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

    private Room getNeibourghRoom(int indexI, int indexJ, Room[][] rooms) {
        if (indexI < 0 || indexJ < 0 || indexI > 3 || indexJ > 2) {
            return null;
        }
        if (rooms[indexI][indexJ] != null) {
            return rooms[indexI][indexJ];
        }
        Room r1 = getNeibourghRoom(indexI, indexJ + 1, rooms);
        if (r1 != null) {
            return r1;
        }
        Room r2 = getNeibourghRoom(indexI + 1, indexJ, rooms);
        if (r2 != null) {
            return r2;
        }
        return null;
    }
    private List<Road> findRoads(Position origin, Position destination) {
        List<Road> roads = new ArrayList<>();
        if (origin.getX() != destination.getX()) {
            Position middle = new Position(destination.getX(), origin.getY());
            roads.add(new Road(origin, middle));
            origin = middle;
        }
        roads.add(new Road(origin, destination));
        return roads;
    }

    private List<Road> constructRoads(Room[][] rooms) {
        List<Road> roads = new ArrayList<>();
        List<Room> promotedRooms = new ArrayList<>();
        List<Room> toPromoteRooms = new ArrayList<>();
        toPromoteRooms.add(rooms[0][0]);
        while (!toPromoteRooms.isEmpty()) {
            Room currentRoom = toPromoteRooms.remove(0);
            promotedRooms.add(currentRoom);
            Room upRoom = getNeibourghRoom(currentRoom.indexI, currentRoom.indexJ + 1, rooms);
            Room rightRoom = getNeibourghRoom(currentRoom.indexI + 1, currentRoom.indexJ, rooms);
            Room downRoom = getNeibourghRoom(currentRoom.indexI, currentRoom.indexJ - 1, rooms);
            Room leftRoom = getNeibourghRoom(currentRoom.indexI - 1, currentRoom.indexJ, rooms);
            List<Room> neighbourRooms = new ArrayList<>();
            if (upRoom != null) {
                neighbourRooms.add(upRoom);
            }
            if (rightRoom != null && !neighbourRooms.contains(rightRoom)) {
                neighbourRooms.add(rightRoom);
            } else {
                rightRoom = null;
            }
            if (downRoom != null && !neighbourRooms.contains(downRoom)) {
                neighbourRooms.add(downRoom);
            } else {
                downRoom = null;
            }
            if (leftRoom != null && !neighbourRooms.contains(leftRoom)) {
                //
            } else {
                leftRoom = null;
            }

            if (upRoom != null && !promotedRooms.contains(upRoom)) {
                if (!toPromoteRooms.contains(upRoom)) {
                    toPromoteRooms.add(upRoom);
                }
                List<Road> subRoads = findRoads(currentRoom.getTopDoorPosition(), upRoom.getBottomDoorPosition());
                roads.addAll(subRoads);
            }
            if (rightRoom != null && !promotedRooms.contains(rightRoom)) {
                if (!toPromoteRooms.contains(rightRoom)) {
                    toPromoteRooms.add(rightRoom);
                }
                List<Road> subRoads = findRoads(currentRoom.getRightDoorPosition(), rightRoom.getLeftDoorPosition());
                roads.addAll(subRoads);
            }
            if (downRoom != null && !promotedRooms.contains(downRoom)) {
                if (!toPromoteRooms.contains(downRoom)) {
                    toPromoteRooms.add(downRoom);
                }
                List<Road> subRoads = findRoads(currentRoom.getBottomDoorPosition(), downRoom.getTopDoorPosition());
                roads.addAll(subRoads);
            }
            if (leftRoom != null && !promotedRooms.contains(leftRoom)) {
                if (!toPromoteRooms.contains(leftRoom)) {
                    toPromoteRooms.add(leftRoom);
                }
                List<Road> subRoads = findRoads(currentRoom.getLeftDoorPosition(), leftRoom.getRightDoorPosition());
                roads.addAll(subRoads);
            }
        }

        for (int i = 0; i < 4; i += 1) {
            for (int j = 0; j < 3; j += 1) {
                if (rooms[i][j] == null || promotedRooms.contains(rooms[i][j])) {
                    continue;
                }
                roads.addAll(findRoads(rooms[0][0].getRightDoorPosition(), rooms[i][j].getLeftDoorPosition()));
            }
        }
        return roads;
    }

    private void drawWall(int x, int y, TETile[][] world) {
        if (world[x][y] != Tileset.NOTHING) {
            return;
        }
        world[x][y] = Tileset.WALL;
    }
    private void drawRoads(List<Road> roads, TETile[][] world) {
        while (!roads.isEmpty()) {
            Road road = roads.remove(0);
            if (road.face == Face.UpDown) {
                int y1 = road.origin.getY();
                int y2 = road.destination.getY();
                int x  = road.origin.getX();
                if (y2 < y1) {
                    int t = y2;
                    y2 = y1;
                    y1 = t;
                }
                drawWall(x - 1, y1 - 1, world);
                drawWall(x + 1, y1 + 1, world);
                drawWall(x - 1, y2 - 1, world);
                drawWall(x + 1, y2 + 1, world);
                while (y2 >= y1) {
                    world[x][y1] = Tileset.FLOOR;
                    drawWall(x - 1, y1, world);
                    drawWall(x + 1, y1, world);
                    y1 += 1;
                }
            } else if (road.face == Face.LeftRight){
                int x1 = road.origin.getX();
                int x2 = road.destination.getX();
                int y  = road.origin.getY();
                if (x2 < x1) {
                    int t = x2;
                    x2 = x1;
                    x1 = t;
                }
                drawWall(x1 - 1, y - 1, world);
                drawWall(x1 - 1, y + 1, world);
                drawWall(x2 + 1, y - 1, world);
                drawWall(x2 + 1, y + 1, world);
                while (x2 >= x1) {
                    world[x1][y] = Tileset.FLOOR;
                    drawWall(x1, y - 1, world);
                    drawWall(x1, y + 1, world);
                    x1 += 1;
                }
            }
        }

    }
    private void drawEntities(List<Entity> entities, TETile[][] world) {
        for (Entity entity : entities) {
            Position position = entity.getCurrentPosition();
            world[position.getX()][position.getY()] = Tileset.AVATAR;
        }
    }
    private Avator placeAvator(TETile[][] world){
        for (int i = 0; i < WIDTH; i += 1) {
            for (int j = 0; j < HEIGHT; j += 1) {
                if (world[i][j] == Tileset.FLOOR) {
                    return new Avator(new Position(i, j), Tileset.FLOOR);
                }
            }
        }
        return new Avator(new Position(32,32), Tileset.FLOOR);
    }

    private boolean movable(Entity entity, TETile tile) {
        return tile == Tileset.FLOOR;
    }
    private void tryMove(Entity entity,TETile[][] world, int deltaX, int deltaY) {
        Position position = entity.getCurrentPosition();
        int x = position.getX();
        int y = position.getY();
        if (!movable(entity, world[x + deltaX][y + deltaY])) {
            return;
        }
        world[x][y] = entity.getTileBeneath();
        entity.moveTo(x + deltaX, y + deltaY, world[x + deltaX][y + deltaY]);
        System.out.println("move!");
    }

    private void saveAndQuit() {

    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        int controlMode = MENU_MODE;
        TETile[][] finalWorldFrame = menu();
        Entity avator = null;
        List<Entity> entities = null;
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        InputSource inputSource = new StringInputDevice(input.toUpperCase());
        while (inputSource.possibleNextInput()) {
            char opKey = inputSource.getNextKey();
            if (controlMode == MENU_MODE) {
                if (opKey == 'N') {
                    controlMode = SEED_MODE;
                } else if (opKey == 'L') {
                    //TODO load game.
                } else if (opKey == 'Q') {
                    //TODO save and exit game.
                } else{
                    //Nothing
                }
            } else if (controlMode == GAME_MODE) {
                if (opKey == 'W') {
                    tryMove(avator, finalWorldFrame, 0 ,1);
                } else if (opKey == 'S') {
                    tryMove(avator, finalWorldFrame, 0 ,-1);
                } else if (opKey == 'A') {
                    tryMove(avator, finalWorldFrame, -1 ,0);
                } else if (opKey == 'D') {
                    tryMove(avator, finalWorldFrame, 1 ,0);
                } else if (opKey == ':'){
                    controlMode = MENU_MODE;
                } else {
                    //nothing
                }
                drawEntities(entities,finalWorldFrame);

            } else if (controlMode == SEED_MODE) {
                //get seed number
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(opKey);
                while (inputSource.possibleNextInput()) {
                    char possibleNumber = inputSource.getNextKey();
                    if (possibleNumber == 'S') {
                        break;
                    }
                    stringBuilder.append(possibleNumber);
                }
                Long seedNumber = Long.parseLong(stringBuilder.toString(),10);
                System.out.println("current world seed is :" + seedNumber);
                Room[][] rooms = constructRooms(seedNumber);
                drawRooms(finalWorldFrame, rooms);
                List<Road> roads = constructRoads(rooms);
                drawRoads(roads, finalWorldFrame);
                avator = placeAvator(finalWorldFrame);
                entities = new ArrayList<>();
                entities.add(avator);
                drawEntities(entities, finalWorldFrame);
                controlMode = GAME_MODE;
            }
            else {
                continue;
            }
            ter.renderFrame(finalWorldFrame);
        }

        return finalWorldFrame;
    }
}
