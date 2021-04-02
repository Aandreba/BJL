package Vector;

public abstract class RelVector extends Vector {
    public RelVector (int length) {
        super(length);
    }

    public abstract void set (int pos, double value);

    public void set (Vector vector) {
        int k = Math.min(length, vector.getLength());

        for (int i=0;i<k;i++) {
            set(i, vector.get(i));
        }
    }

    public void add (Vector b) {
        for (int i=0;i<length;i++) {
            set(i, get(i) + b.get(i));
        }
    }

    public void add (double b) {
        for (int i=0;i<length;i++) {
            set(i, get(i) + b);
        }
    }

    public <T extends Number> void add (T b) {
        for (int i=0;i<length;i++) {
            set(i, get(i) + b.doubleValue());
        }
    }
}
