
public class TelKnoten {
    int x;
    int y;
    public TelKnoten(int x, int y) {
        this.x = x;
        this.y = y;
    }


    public boolean equals(TelKnoten obj) {
        if (obj.x == x && obj.y == y)
            return true;
        return false;
    }

    @Override
    public String toString() {
        return "x = " + x +  ", " + "y = " + y;
    }
}
