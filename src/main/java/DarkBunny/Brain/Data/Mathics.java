package DarkBunny.Brain.Data;

public class Mathics {

    public static void init()
    {

    }
    public static float cap(float d,float l,float h) {
        return Math.min(h, Math.max(l, d));
    }

    public static double turnRadius(double speed)
    {
        if (speed == 0)
        {
            return 0;
        }
        return 1/curvature(speed);
    }

    public static double curvature(double speed)
    {
        if(speed <= 500f) {
            return 0.006900 - 5.84e-6*speed;
        }else if(speed <= 1000f) {
            return 0.005610 - 3.26e-6*speed;
        }else if(speed <= 1500f) {
            return  0.004300 - 1.95e-6*speed;
        }else if (speed <= 1750f){
            return 0.003025 - 1.10e-6*speed;
        }else if(speed <= 2500f){
            return 0.001800 -0.40e-6*speed;
        }else
            return 0;
    }
}
