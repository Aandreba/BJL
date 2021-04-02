package OpenGL.Primitives.Objects;

import OpenGL.GameObject;
import OpenGL.Material;
import OpenGL.Mesh;
import OpenGL.Primitives.Square;
import OpenGL.Texture;

public class Terrain extends GameObject {
    final int precision;

    public Terrain (int precision, Material texture) {
        super(new Square(precision), texture);
        this.precision = precision;
    }

    public void applyNoise () {
        // TODO
        //StatVect
    }
}
