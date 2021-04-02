package OpenGL.Primitives.Objects;

import Extras.Rand;
import Extras.Image;
import OpenGL.GameObject;
import OpenGL.Primitives.Circle;
import OpenGL.Texture;
import Vector.StatVector;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

public class Pie<T> extends GameObject implements Map<T,Double> {
    class PieEntry implements Entry<T, Double> {
        int i;

        public PieEntry(int i) {
            this.i = i;
        }

        @Override
        public T getKey() {
            return objects.get(i);
        }

        @Override
        public Double getValue() {
            return values.get(i);
        }

        @Override
        public Double setValue (Double value) {
            return Pie.this.put(getKey(), value);
        }
    }

    protected ArrayList<T> objects;
    protected ArrayList<Color> colors;
    protected StatVector values;

    public Pie () {
        super(new Circle(100), Color.WHITE);
        this.objects = new ArrayList<>();
        this.colors = new ArrayList<>();
        this.values = new StatVector();
    }

    public Pie (Collection<T> objects, double... values) {
        this();
        this.objects = new ArrayList<>(objects);
        this.values = new StatVector(values);
        this.values = this.values.div(this.values.getSum()).toStatic();

        for (int i=0;i<size();i++) {
            this.colors.set(i, Rand.getColorRGB());
        }

        try {
            this.generateTexture();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public Pie (T[] objects, double... values) {
        this();
        this.values = new StatVector(values);
        this.values = this.values.div(this.values.getSum()).toStatic();
        Collections.addAll(this.objects, objects);

        for (int i=0;i<size();i++) {
            this.colors.add(i, Rand.getColorRGB());
        }

        try {
            this.generateTexture();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public Pie (Collection<T> objects) {
        this();
        this.objects = new ArrayList<>(objects);
        this.values = new StatVector(objects.size());

        for (int i=0;i<size();i++) {
            this.colors.add(i, Rand.getColorRGB());
        }
    }

    public Pie (T... objects) {
        this();
        this.values = new StatVector(objects.length);
        Collections.addAll(this.objects, objects);

        for (int i=0;i<size();i++) {
            this.colors.add(i, Rand.getColorRGB());
        }
    }

    @Override
    public int size() {
        return objects.size();
    }

    @Override
    public boolean isEmpty() {
        return objects.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return objects.contains(key);
    }

    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof Number)) {
            return false;
        }

        double number = ((Number) value).doubleValue();
        for (int i=0;i<values.getLength();i++) {
            if (get(i) == number) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Double get (Object key) {
        return values.get(objects.indexOf(key));
    }

    @Override
    public Double put(T key, Double value) {
        int index = objects.indexOf(key);
        if (index == -1) {
            objects.add(key);
            colors.add(Rand.getColorRGB());

            index = objects.size() - 1;
            double div = values.getSum() + value;
            StatVector newValues = new StatVector(objects.size());

            for (int i=0;i<values.getLength();i++) {
                newValues.set(i, values.get(i) / div);
            }

            newValues.set(index, value / div);
            values = newValues;

            try {
                generateTexture();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return values.get(index);
        }

        double div = values.getSum() - values.get(index) + value;
        for (int i=0;i<values.getLength();i++) {
            values.set(i, values.get(i) / div);
        }

        values.set(index, value / div);

        try {
            generateTexture();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return values.get(index);
    }

    public Double put (T key, Number number) {
        return put(key, number.doubleValue());
    }

    @Override
    public Double remove (Object key) {
        int index = objects.indexOf(key);
        if (index == -1) {
            return null;
        }

        double div = values.getSum() - values.get(index);
        StatVector lastValues = new StatVector(objects.size() - 1);
        for (int i=0;i<lastValues.getLength();i++) {
            if (i < index) {
                lastValues.set(i, values.get(i) / div);
            } else {
                lastValues.set(i-1, values.get(i) / div);
            }
        }

        objects.remove(index);
        try {
            generateTexture();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void putAll (Map<? extends T, ? extends Double> m) {
        for (Map.Entry<? extends T, ? extends Double> entry: m.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        objects.clear();
        values = new StatVector(0);

        try {
            generateTexture();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<T> keySet() {
        return new HashSet<>(objects);
    }

    @Override
    public Collection<Double> values() {
        ArrayList<Double> values = new ArrayList<>();
        for (int i = 0;i<size();i++) {
            values.add(i, this.values.get(i));
        }

        return values;
    }

    @Override
    public Set<Entry<T, Double>> entrySet() {
        HashSet<Entry<T, Double>> set = new HashSet<>();
        for (int i=0;i<size();i++) {
            set.add(new PieEntry(i));
        }

        return set;
    }

    public Double add (T key, Number value) {
        return put(key, get(key) + value.doubleValue());
    }

    public Color getColor (T key) {
        return colors.get(objects.indexOf(key));
    }

    public Color setColor (T key, Color color) {
        return colors.set(objects.indexOf(key), color);
    }

    public StatVector getVector () {
        return values.toStatic();
    }

    private void generateTexture () throws Exception {
        Image img = new Image(1000, 1000);
        BufferedImage buffer = img.getBuffer();
        Graphics2D g = buffer.createGraphics();

        double curValue = 0;
        int startAngle = 0;
        for (int i=0;i<size();i++) {
            startAngle = (int) (curValue * 360);
            int arcAngle = (int) (values.get(i) * 360);

            g.setColor(colors.get(i));
            g.fillArc(0, 0, 1000, 1000, startAngle, arcAngle);
            curValue += values.get(i);
        }

        int id = 0;
        do {
            id = Rand.getInt();
        } while (new File(id+".png").exists());

        img.write(id+".png", "png");
        this.material.texture = new Texture(id+".png");
        new File(id+".png").delete();
    }
}
