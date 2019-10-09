package DarkBunny.Brain.Data.linear_algebra;

import DarkBunny.vector.Vector3;

/**
 * A simple 3d vector class with the most essential operations.
 *
 * This class is here for your convenience, it is NOT part of the framework. You can add to it as much
 * as you want, or delete it.
 */
public class Vec3 {

    public double x;
    public double y;
    public double z;

    public Vec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3(Vector3 vec)
    {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
    }

    public Vec3() {
        this(0, 0, 0);
    }

    public Vec3(rlbot.flat.Vector3 vec) {
        // Invert the X value so that the axes make more sense.
        this(vec.x(), vec.y(), vec.z());
    }

    public Vec3 plus(Vec3 other) {
        return new Vec3(x + other.x, y + other.y, z + other.z);
    }

    public Vec3 minus(Vec3 other) {
        return new Vec3(x - other.x, y - other.y, z - other.z);
    }

    public Vec3 scaled(double scale) {
        return new Vec3(x * scale, y * scale, z * scale);
    }

    /**
     * If magnitude is negative, we will return a vector facing the opposite direction.
     */
    public Vec3 scaledToMagnitude(double magnitude) {
        if (isZero()) {
            throw new IllegalStateException("Cannot scale up a vector with length zero!");
        }
        double scaleRequired = magnitude / magnitude();
        return scaled(scaleRequired);
    }

    public float distance(Vec3 other) {
        double xDiff = x - other.x;
        double yDiff = y - other.y;
        double zDiff = z - other.z;
        return (float)java.lang.Math.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
    }

    public double magnitude() {
        return java.lang.Math.sqrt(magnitudeSquared());
    }

    public double magnitudeSquared() {
        return x * x + y * y + z * z;
    }

    public Vec3 normalized() {

        if (isZero()) {
            throw new IllegalStateException("Cannot normalize a vector with length zero!");
        }
        return this.scaled(1 / magnitude());
    }

    public double dotProduct(Vec3 other) {
        return x * other.x + y * other.y + z * other.z;
    }

    public boolean isZero() {
        return x == 0 && y == 0 && z == 0;
    }

    public Vec2 flatten() {
        return new Vec2(x, y);
    }

    public Vec3 make2D() {
        return new Vec3(x,y,0);
    }

    public double angle(Vec3 v) {
        double mag2 = magnitudeSquared();
        double vmag2 = v.magnitudeSquared();
        double dot = dotProduct(v);
        return java.lang.Math.acos(dot / java.lang.Math.sqrt(mag2 * vmag2));
    }

    public Vec3 crossProduct(Vec3 v) {
        double tx = y * v.z - z * v.y;
        double ty = z * v.x - x * v.z;
        double tz = x * v.y - y * v.x;
        return new Vec3(tx, ty, tz);
    }

    public String toString()
    {
        return "X: " + x + "\tY: " + y + "\tZ: " + z;
    }

    public double angle2D(Vec3 object)
    {
        Vec3 difference = this.minus(object);
        return java.lang.Math.atan2(difference.y,difference.x);
    }
    public float norm()
    {
        return (float)java.lang.Math.sqrt(this.dotProduct(this));
    }

    public double value(int i)
    {
        switch(i)
                {
                    case 0: return x;
                    case 1: return y;
                    default: return z;
                }
    }

    public void setValue(int i,double val)
    {
        switch(i)
        {
            case 0: x=val;
            case 1: y = val;
            default: z = val;
        }
    }

    public Vector3 toVector3(){
        return new Vector3(x,y,z);
    }
}
