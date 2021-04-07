package Relativity;

import Extras.Mathx;
import Matrix.Matrix;
import Matrix.StatMatrix;
import OpenGL.Extras.Matrix.Matrix4;
import OpenGL.Extras.Matrix.StatMatrix4;
import OpenGL.Extras.Vector.StatVector3;
import OpenGL.Extras.Vector.Vector3;
import OpenGL.Rigidbody;
import Units.Time;
import Vector.StatVector;
import java.util.ArrayList;

public class Universe extends ArrayList<Rigidbody> {
    final public static double c = 299792458;
    final public static double c2 = Math.pow(c, 2);

    final public static double G = 6.674e-11;
    final public static double k = 8 * Math.PI * G / (c2 * c2);

    final public static double cc = 1; // Cosmological constant

    public static double density (StatVector3 scale) {
        return scale.x() * scale.y() * scale.y();
    }

    public static double lorentzFactor (double speed) {
        return 1 / Math.sqrt(1 - Math.pow(speed, 2) / c2);
    }

    public static double lorentzFactor (Rigidbody rb) {
        return lorentzFactor(rb.velocity.getSqrtMagnitude());
    }

    public static double relativisticMass (Rigidbody rb) {
        return rb.mass.getValue() * lorentzFactor(rb);
    }

    public static double energy (Rigidbody rb) {
        return relativisticMass(rb) * c2;
    }

    public static double energyDensity (Rigidbody rb) {
        return energy(rb) / density(rb.object.transform.scale);
    }

    public static StatVector fourVelocity (Rigidbody rb) {
        double lorentz = lorentzFactor(rb);

        StatVector vector = new StatVector(4);
        vector.set(0, lorentz);
        for (int i=0;i<3;i++) {
            vector.set(i+1, lorentz * rb.velocity.get(i));
        }

        return vector;
    }

    public static Matrix4 stressEnergyTensor (Rigidbody rb) {
        double p0 = energyDensity(rb) / c2;
        StatVector velocity = fourVelocity(rb);

        return new Matrix4() {
            @Override
            public double get(int row, int col) {
                double v = p0 * velocity.get(row) * velocity.get(col);
                return row == 0 ? -v : v;
            }
        };
    }

    public static Matrix4 einsteinTensor (Rigidbody rb) {
        return stressEnergyTensor(rb).scalarMul(k);
    }

    public static Matrix4 metricTensor (Rigidbody rb) {
        return einsteinTensor(rb);
    }
}
