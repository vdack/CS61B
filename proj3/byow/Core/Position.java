package byow.Core;

public class Position {
    private int x;
    private int y;
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getX(){
        return this.x;
    }
    public int getY() {
        return this.y;
    }
    public Position getDeltaPosition(Position anotherPosition) {
        //return the position to this position.
        return new Position(anotherPosition.x - this.x, anotherPosition.y - this.y);
    }

    @Override
    public boolean equals(Object obj) {
        Position another = (Position) obj;
        return this.x == another.x && this.y == another.y;
    }
}
