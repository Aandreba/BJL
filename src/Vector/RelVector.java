package Vector;

public abstract class RelVector extends Vector {
    public RelVector (int length) {
        super(length);
    }

    public abstract void set (int pos, double value);
}
