package byow.Core;

import java.util.Random;

public class Room {
    public Position topLeftPosition;
    private long seed;
    public int roomWidth;
    public int roomHeight;
    public int indexI;
    public int indexJ;

    //possible room door when connected.
    private Position topDoorPosition;
    private Position bottomDoorPosition;
    private Position leftDoorPosition;
    private Position rightDoorPosition;

    public Room(int topLeftX, int topLeftY, int i, int j, long seed) {
        this.seed = seed;
        this.indexI = i;
        this.indexJ = j;
        Random r = new Random(seed);
        int offsetX = r.nextInt(4);
        int offsetY = r.nextInt(4);
        topLeftX += offsetX;
        topLeftY += offsetY;
        topLeftPosition = new Position(topLeftX, topLeftY);
        roomWidth = r.nextInt(12) + 8;
        roomHeight = r.nextInt(12) + 8;

        int topDoorOffset = r.nextInt(roomWidth - 2) + 1;
        topDoorPosition = new Position(topLeftX + topDoorOffset, topLeftY + roomHeight - 1);
        int bottomDoorOffset = r.nextInt(roomWidth - 2) + 1;
        bottomDoorPosition = new Position(topLeftX + bottomDoorOffset, topLeftY);
        int leftDoorOffset = r.nextInt(roomHeight - 2) + 1;
        leftDoorPosition = new Position(topLeftX, topLeftY + leftDoorOffset);
        int rightDoorOffset = r.nextInt(roomHeight - 2) + 1;
        rightDoorPosition = new Position(topLeftX + roomWidth - 1, topLeftY + rightDoorOffset);
    }

    public long getSeed() {
        return seed;
    }
    public Position getBottomDoorPosition() {
        return bottomDoorPosition;
    }
    public Position getLeftDoorPosition() {
        return leftDoorPosition;
    }
    public Position getRightDoorPosition() {
        return rightDoorPosition;
    }
    public Position getTopDoorPosition() {
        return topDoorPosition;
    }


}
