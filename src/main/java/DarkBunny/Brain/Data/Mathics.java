package DarkBunny.Brain.Data;

import DarkBunny.Brain.Data.linear_algebra.Mat;
import DarkBunny.Brain.Data.linear_algebra.Mat3;
import DarkBunny.Brain.Data.linear_algebra.Vec3;
import DarkBunny.vector.Vector3;

public class Mathics {

    public static void init()
    {

    }
    public static float cap(float d,float l,float h) {
        return Math.min(h, Math.max(l, d));
    }

    public static double cap(double d,double l,double h) {
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

    /*
    Aerial handling stuff
     */
    final static double T_r = -36.07956616966136; // torque coefficient for roll
    final static double T_p = -12.14599781908070; // torque coefficient for pitch
    final static double T_y =   8.91962804287785; // torque coefficient for yaw
    final static double D_r =  -4.47166302201591; // drag coefficient for roll
    final static double D_p = -2.798194258050845; // drag coefficient for pitch
    final static double D_y = -1.886491900437232; // drag coefficient for yaw

    public static double sgn(double x){
        return 0.0f<x?1:0 - x<0.0f?1:0;
    }

    private static Vec3 aerialInputsRLU(Vec3 omega_start, Vec3 omega_end, Mat3 theta_start, float dt)
    {
        Vec3 tau = (omega_end.minus(omega_start).scaled(1/dt));
        tau = theta_start.transpose().dot(tau);
        Vec3 omega_local = theta_start.transpose().dot(tau);

        Vec3 rhs = new Vec3(
                tau.x - D_r * omega_local.x,
                tau.y - D_p * omega_local.y,
                tau.z - D_y * omega_local.z
        );

        Vec3 u = new Vec3(
                rhs.x/T_r,
                rhs.y/(T_p+sgn(rhs.y)*omega_local.y*D_p),
                rhs.z/(T_y - sgn(rhs.z)*omega_local.z*D_y)
        );

        u.x = Mathics.cap(u.x,-1,1);
        u.y = Mathics.cap(u.y,-1,1);
        u.z = Mathics.cap(u.z,-1,1);

        return u;
    }

    public static Vector3 aerialInput(Vector3 omegaStart, Vector3 omegaEnd, Car car,float dt)
    {
        Mat3 mat3 = new Mat3();

        for(int i = 0; i < 3; i++)
        {
            mat3.setValue(i,0,car.getMatrix()[i].x);
            mat3.setValue(i,1,car.getMatrix()[i].y);
            mat3.setValue(i,2,car.getMatrix()[i].z);
        }

        return aerialInputsRLU(new Vec3(omegaStart),new Vec3(omegaEnd),mat3,dt).toVector3();
    }
}
