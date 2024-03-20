package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class World {
    private TETile[][] world;
    private int width;
    private int height;
    private long seed;
    private int widthOffset;//TODO remove offset and modify the world to multiple size.
    private int heightOffset;
    private Entity avator;
    public World(int width, int height, long seed, int widthOffset, int heightOffset){
        this.width = width;
        this.height = height;
        this.seed = seed;
        this.widthOffset = widthOffset;
        this.heightOffset = heightOffset;
        this.avator = null;
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
        avator = null;
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
                neighbourRooms.add(leftRoom);
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

    private void setWall(int x, int y, TETile[][] world) {
        if (world[x][y] != Tileset.NOTHING) {
            return;
        }
        world[x][y] = Tileset.WALL;
    }
    private void setRoads(List<Road> roads) {
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
                setWall(x - 1, y1 - 1, world);
                setWall(x + 1, y1 + 1, world);
                setWall(x - 1, y2 - 1, world);
                setWall(x + 1, y2 + 1, world);
                while (y2 >= y1) {
                    world[x][y1] = Tileset.FLOOR;
                    setWall(x - 1, y1, world);
                    setWall(x + 1, y1, world);
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
                setWall(x1 - 1, y - 1, world);
                setWall(x1 - 1, y + 1, world);
                setWall(x2 + 1, y - 1, world);
                setWall(x2 + 1, y + 1, world);
                while (x2 >= x1) {
                    world[x1][y] = Tileset.FLOOR;
                    setWall(x1, y - 1, world);
                    setWall(x1, y + 1, world);
                    x1 += 1;
                }
            }
        }
    }
    private void setAvator() {
        for (int i = 0; i < width; i += 1) {
            for (int j = 0; j < height; j += 1) {
                if (world[i][j] == Tileset.FLOOR) {
                    avator =  new Avator(new Position(i, j), Tileset.FLOOR);
                    world[i][j] = Tileset.AVATAR;
                    return;
                }
            }
        }

    }


    private boolean tryMove(int deltaX, int deltaY, Entity entity) {
        int x = entity.getCurrentPosition().getX();
        int y = entity.getCurrentPosition().getY();
        if (world[x + deltaX][y + deltaY] != Tileset.FLOOR) return false; //judge if it is movable.
        world[x][y] = entity.getTileBeneath();
        entity.moveTo(x + deltaX, y + deltaY, world[x + deltaX][y + deltaY]);
        world[x + deltaX][y + deltaY] = entity.getImage();
        return true;
    }

    private TETile charToTile(char c) {
        switch (c) {
            case '_' : return Tileset.NOTHING;
            case '#' : return Tileset.WALL;
            case '·' : return Tileset.FLOOR;
            case '@' : return Tileset.AVATAR;
            default : return Tileset.NOTHING;
        }
    }
    private char tileToChar(TETile tile) {
        if (tile.equals(Tileset.NOTHING)) {
            return '_';
        } else {
            return tile.character();
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
        setRooms(rooms);
        List<Road> roads = constructRoads(rooms);
        setRoads(roads);
        setAvator();
    }
    public void avatorMoveUp() {
        tryMove(0, 1, avator);
    }
    public void avatorMoveDown() {
        tryMove(0, -1, avator);

    }
    public void avatorMoveLeft() {
        tryMove(-1, 0, avator);
    }
    public void avatorMoveRight() {
        tryMove(1, 0, avator);
    }

    public void saveWorld(String savingName) throws IOException {
        File saving = new File(savingName);
        saving.createNewFile();
        FileWriter fileWriter = new FileWriter(saving);
        fileWriter.write(width+" " + height + " " + seed + " " + widthOffset + " " + heightOffset + "\n");
        fileWriter.write(avator.getCurrentPosition().getX() + " " + avator.getCurrentPosition().getY() + " " + avator.getTileBeneath().character() + "\n");
        for (int j = height - 1; j >= 0; j -= 1) {
            for (int i = 0 ;i < width; i += 1) {
                fileWriter.write(tileToChar(world[i][j]));
            }
            fileWriter.write("\n");
        }
        fileWriter.close();
//        System.out.println("Status: world saved.");
    }
    public void loadWorld(String savingName) throws FileNotFoundException {
        File saving = new File(savingName);
        Scanner scanner = new Scanner(saving);
        String basicInfo = scanner.nextLine();
        String[] basicInformation = basicInfo.split(" ");
        this.width = Integer.parseInt(basicInformation[0]);
        this.height = Integer.parseInt(basicInformation[1]);
        this.seed = Long.parseLong(basicInformation[2]);
        this.widthOffset = Integer.parseInt(basicInformation[3]);
        this.heightOffset = Integer.parseInt(basicInformation[4]);
        basicInfo = scanner.nextLine();
        String[] avatorInformation = basicInfo.split(" ");
        initialize();
        clear();
        this.avator = new Avator(new Position(Integer.parseInt(avatorInformation[0]), Integer.parseInt(avatorInformation[1])), charToTile(avatorInformation[2].charAt(0)));
        for (int j = height - 1; j >= 0; j -= 1) {
            String tileString = scanner.nextLine();
            for (int i = 0 ;i < width; i += 1) {
                world[i][j] = charToTile(tileString.charAt(i));
            }
        }
    }

}
