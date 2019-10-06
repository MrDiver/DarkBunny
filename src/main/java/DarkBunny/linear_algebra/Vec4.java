package DarkBunny.linear_algebra;

public class Vec4 {
    public double x,y,z,w;

    public Vec4(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vec4() {
        this(0, 0, 0, 0);
    }

    public Vec4 plus(Vec4 other) {
        return new Vec4(x + other.x, y + other.y, z + other.z,w+other.w);
    }

    public Vec4 minus(Vec4 other) {
        return new Vec4(x - other.x, y - other.y, z - other.z,w-other.w);
    }

    public Vec4 scaled(double scale) {
        return new Vec4(x * scale, y * scale, z * scale,w*scale);
    }

    /**
     * If magnitude is negative, we will return a vector facing the opposite direction.
     */
    public Vec4 scaledToMagnitude(double magnitude) {
        if (isZero()) {
            throw new IllegalStateException("Cannot scale up a vector with length zero!");
        }
        double scaleRequired = magnitude / magnitude();
        return scaled(scaleRequired);
    }

    public float distance(Vec4 other) {
        double xDiff = x - other.x;
        double yDiff = y - other.y;
        double zDiff = z - other.z;
        double wDiff = w - other.w;
        return (float)java.lang.Math.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff+ wDiff*wDiff);
    }

    public double magnitude() {
        return java.lang.Math.sqrt(magnitudeSquared());
    }

    public double magnitudeSquared() {
        return x * x + y * y + z * z + w*w;
    }

    public Vec4 normalized() {

        if (isZero()) {
            throw new IllegalStateException("Cannot normalize a vector with length zero!");
        }
        return this.scaled(1 / magnitude());
    }

    public double dotProduct(Vec4 other) {
        return x * other.x + y * other.y + z * other.z + w*other.w;
    }

    public boolean isZero() {
        return x == 0 && y == 0 && z == 0;
    }

    public double angle(Vec4 v) {
        double mag2 = magnitudeSquared();
        double vmag2 = v.magnitudeSquared();
        double dot = dotProduct(v);
        return java.lang.Math.acos(dot / java.lang.Math.sqrt(mag2 * vmag2));
    }

    public String toString()
    {
        return "X: " + x + "\tY: " + y + "\tZ: " + z;
    }

    public double angle2D(Vec4 object)
    {
        Vec4 difference = this.minus(object);
        return java.lang.Math.atan2(difference.y,difference.x);
    }
    public float norm()
    {
        return (float)java.lang.Math.sqrt(this.dotProduct(this));
    }
}
