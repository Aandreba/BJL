package Relativity;

import OpenGL.GameObject;
import OpenGL.Primitives.Sphere;
import Units.Mass;

import java.awt.*;

public class Test {
    public static void main (String[] args) {
        GameObject planet = new GameObject(null, Color.BLUE);
        planet.createRigidbody(new Mass(70));
        planet.rb.setVelocity(0.99999 * Universe.c, 0, 0);

        System.out.println(Universe.stressEnergyTensor(planet.rb));
        System.out.println(Universe.einsteinTensor(planet.rb));
    }
}
