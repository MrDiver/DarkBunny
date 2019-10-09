package DarkBunny.Brain.Data.linear_algebra;

/**
 * A vector that only knows about x and y components.
 *
 * This class is here for your convenience, it is NOT part of the framework. You can add to it as much
 * as you want, or delete it.
 */
public class Vec2 {

    public final double x;
    public final double y;

    public Vec2()
    {
        x = 0;
        y = 0;
    }

    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vec2 plus(Vec2 other) {
        return new Vec2(x + other.x, y + other.y);
    }

    public Vec2 minus(Vec2 other) {
        return new Vec2(x - other.x, y - other.y);
    }

    public Vec2 scaled(double scale) {
        return new Vec2(x * scale, y * scale);
    }

    /**
     * If magnitude is negative, we will return a vector facing the opposite direction.
     */
    public Vec2 scaledToMagnitude(double magnitude) {
        if (isZero()) {
            throw new IllegalStateException("Cannot scale up a vector with length zero!");
        }
        double scaleRequired = magnitude / magnitude();
        return scaled(scaleRequired);
    }

    public double distance(Vec2 other) {
        double xDiff = x - other.x;
        double yDiff = y - other.y;
        return java.lang.Math.sqrt(xDiff * xDiff + yDiff * yDiff);
    }

    /**
     * This is the length of the vector.
     */
    public double magnitude() {
        return java.lang.Math.sqrt(magnitudeSquared());
    }

    public double magnitudeSquared() {
        return x * x + y * y;
    }

    public Vec2 normalized() {

        if (isZero()) {
            throw new IllegalStateException("Cannot normalize a vector with length zero!");
        }
        return this.scaled(1 / magnitude());
    }

    public double dotProduct(Vec2 other) {
        return x * other.x + y * other.y;
    }

    public boolean isZero() {
        return x == 0 && y == 0;
    }

    /**
     * The correction angle is how many radians you need to rotate this vector to make it line up with the "ideal"
     * vector. This is very useful for deciding which direction to steer.
     */
    public double correctionAngle(Vec2 ideal) {
        double currentRad = java.lang.Math.atan2(y, x);
        double idealRad = java.lang.Math.atan2(ideal.y, ideal.x);

        if (java.lang.Math.abs(currentRad - idealRad) > java.lang.Math.PI) {
            if (currentRad < 0) {
                currentRad += java.lang.Math.PI * 2;
            }
            if (idealRad < 0) {
                idealRad += java.lang.Math.PI * 2;
            }
        }

        return idealRad - currentRad;
    }

    public rlbot.vector.Vector3 makeRlBot()
    {
        return new DarkBunny.vector.Vector3(this.x,this.y,0);
    }

    public float norm()
    {
        return (float)java.lang.Math.sqrt(this.dotProduct(this));
    }

    /**
     * Will always return a positive value <= Math.PI
     */
    public static double angle(Vec2 a, Vec2 b) {
        return java.lang.Math.abs(a.correctionAngle(b));
    }
}
