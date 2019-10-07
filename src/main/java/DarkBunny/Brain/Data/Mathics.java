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

    public static double toCurvature(double turnRadius)
    {
        return 1/turnRadius;
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

    public static double maxSpeedForCurvature(double curvature)
    {
        if(curvature >= 0.006900)
        {
            return 0;
        }else if(curvature >= 0.005610)
        {
            return (0.006900-curvature)/5.84e-6;
        }
        else if(curvature >= 0.004300)
        {
            return (0.005610-curvature)/3.26e-6;
        }
        else if(curvature >= 0.003025)
        {
            return (0.004300-curvature)/1.95e-6;
        }
        else if(curvature >= 0.001800)
        {
            return (0.003025-curvature)/1.10e-6;
        }else if(curvature >= 0.00088)
        {
            return (0.001800-curvature)/0.40e-6;
        }else
            return 2301;
    }
}
