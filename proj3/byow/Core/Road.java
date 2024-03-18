package byow.Core;

enum Face {UpDown, LeftRight};
public class Road {
    public Position origin;
    public Position destination;
    public Face face;
    public Road(Position from, Position to){
        this.origin = from;
        this.destination = to;
        if (from.getX() == to.getX()) {
            face = Face.UpDown;
        }
        if (from.getY() == to.getY()) {
            face = Face.LeftRight;
        }
    }
}
