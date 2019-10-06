package DarkBunny.linear_algebra;

import java.util.List;

import static java.lang.Math.*;

public class Math {
    /**
     * Clipping a value x between the minimum and maximum
     * @param x the input value
     * @param minimum the top limit
     * @param maximum the bottom limit
     * @return return the clipped value
     */
    public static float clip(float x, float minimum, float maximum)
    {
        return max(min(x,maximum),minimum);
    }

    /**
     * Returns 1, 0 or -1 depending of the sign of the given number x
     * @param x
     * @return returns sign of x
     */
    public static float sgn(float x)
    {
        return ((0.0f < x?1f:0f) - (x < 0.0f?1:0));
    }

    /**
     * Return angle between two Vector3 objects
     * @param a
     * @param b
     * @return
     */
    public static float angle_between(final Vec3 a, final Vec3 b)
    {
        return (float)acos(clip((float)a.normalized().dotProduct(b.normalized()),-1.0f,1.0f));
    }

    /**
     * Return angle between two Vector2 objects
     * @param a
     * @param b
     * @return
     */
    public static float angle_between(final Vec2 a, final Vec2 b)
    {
        return (float)acos(clip((float)a.normalized().dotProduct(b.normalized()),-1.0f,1.0f));
    }

    public static float angle_between(Mat A,Mat B)
    {
        if(A.N0 == 3 && A.N1 == 3 && B.N0 == 3 && B.N1 == 3)
        {
            return (float)acos(0.5f * (A.dot(B.transpose())).tr() -1.0f);
        }
        return 0;
    }

    public static Vec3 xy (Vec3 vec)
    {
        return new Vec3(vec.x,vec.y,0);
    }

    public static Mat rotation(float theta)
    {
        Mat a = new Mat(2,2);
        a.setValue(0,0,(float)cos(theta));
        a.setValue(1,0,(float)-sin(theta));
        a.setValue(0,1,(float)sin(theta));
        a.setValue(1,1,(float)cos(theta));
        return a;
    }

    public static Mat axis_to_rotation(Vec3 vec)
    {
        float norm_omega = vec.norm();

        if(abs(norm_omega) < 0.000001f)
        {
            return Mat.eye(3);
        }else
        {
            /*Vector3 axis = vec.normalized();
            Mat K = new Mat(3,3);
            K.setValue(0,0.0f);
            K.setValue(1,-axis.z);
            K.setValue(2,axis.y);
            K.setValue(3,axis.z);
            K.setValue(4,0.0f);
            K.setValue(5,-axis.x);
            K.setValue(6,-axis.y);
            K.setValue(7,axis.x);
            K.setValue(8,0.0f);

            return Mat.eye(3).plus(K.scale((float)sin(norm_omega)).plus(K.dot(K).scale(1.0f-(float)cos(norm_omega))));*/

            Vec3 u = vec.scaled(1/norm_omega);
            float c = (float) cos(norm_omega);
            float s = (float) sin(norm_omega);
            Mat a = new Mat(3,3);

            a.setValue(0, (float) (u.x*u.x*(1.0f-c)+c));
            a.setValue(1, (float) (u.x*u.y*(1.0f-c)-u.z*s));
            a.setValue(2, (float) (u.x*u.z*(1.0f-c)+u.y*s));

            a.setValue(3, (float) (u.y*u.x*(1.0f-c)+u.z*s));
            a.setValue(4, (float) (u.y*u.y*(1.0f-c)+c));
            a.setValue(5, (float) (u.y*u.z*(1.0f-c)-u.x*s));

            a.setValue(6, (float) (u.z*u.x*(1.0f-c)-u.y*s));
            a.setValue(7, (float) (u.z*u.y*(1.0f-c)+u.x*s));
            a.setValue(8, (float) (u.z*u.z*(1.0f-c)+c));
            return a;
        }
    }

    public static Vec3 rotation_to_axis(Mat a)
    {
        float theta = (float) acos(clip((0.5f*(a.tr()-1.0f)),-1.0f,1.0f));

        float scale;

        if(abs(theta) < 0.00001f){
            scale = 0.5f + theta*theta/12.0f;
        }else {
            scale = (float)(0.5f * theta / sin(theta));
        }

        return new Vec3(a.value(2,1)-a.value(1,2),a.value(0,2)-a.value(2,0),a.value(1,0)-a.value(0,1)).scaled(scale);
    }

    public static Mat antisym(Vec3 w)
    {
        Mat a = new Mat(3,3);
        a.setValue(0,0.0f);
        a.setValue(1, (float) -w.z);
        a.setValue(2, (float) w.y);
        a.setValue(3, (float) w.z);
        a.setValue(4,0.0f);
        a.setValue(5, (float) -w.x);
        a.setValue(6, (float) -w.y);
        a.setValue(7, (float) w.x);
        a.setValue(8,0.0f);

        return a;
    }

    public static Mat euler_to_rotation(Vec3 pyr)
    {
        float CP = (float) cos(pyr.x);
        float SP = (float) sin(pyr.x);
        float CY = (float) cos(pyr.y);
        float SY = (float) sin(pyr.y);
        float CR = (float) cos(pyr.z);
        float SR = (float) sin(pyr.z);

        Mat theta = new Mat(3,3);

        //front direction
        theta.setValue(0,0,CP*CY);
        theta.setValue(1,0,CP*SY);
        theta.setValue(2,0,SP);

        //left direction
        theta.setValue(0,1,CY*SP*SR-CR*SY);
        theta.setValue(1,1,SY*SP*SR+CR*CY);
        theta.setValue(2,1,-CP*SR);

        //up direction
        theta.setValue(0,2,-CR *CY *SP-SR*SY);
        theta.setValue(1,2,-CR*SY*SP+SR*CY);
        theta.setValue(2,2,CP*CR);

        return  theta;
    }

    public static Vec3 rotation_to_euler(Mat theta)
    {
        return new Vec3(
                atan2(theta.value(2,0),new Vec2(theta.value(0,0),theta.value(1,0)).norm()),
                atan2(theta.value(1,0),theta.value(0,0)),
                atan2(-theta.value(2,1),theta.value(2,2)));
    }

    public static Mat quaternion_to_rotation(Vec4 q)
    {
        float s = (float) (1.0f / q.dotProduct(q));

        Mat theta = new Mat(3,3);

        //front
        theta.setValue(0,0, (float) (1.0f-2.0f*s*(q.z*q.z+q.w*q.w)));
        theta.setValue(1,0, (float) (2.0f*s*(q.y*q.z+q.w*q.x)));
        theta.setValue(2,0, (float) (2.0f*s*(q.y*q.w-q.z*q.x)));

        //left direction
        theta.setValue(0,1, (float) (2.0f*s*(q.y*q.z-q.w*q.x)));
        theta.setValue(1,1, (float) (1.0f-2.0f*s*(q.y*q.y+q.w*q.w)));
        theta.setValue(2,1, (float) (2.0f*s*(q.z*q.w+q.y*q.x)));

        //up direction
        theta.setValue(0,2, (float) (2.0f * s*(q.x*q.w + q.z*q.x)));
        theta.setValue(1,2, (float) (2.0f*s*(q.z*q.w-q.y*q.x)));
        theta.setValue(2,2, (float) (1.0f-2.0f*s*(q.y*q.y+q.z*q.z)));

        return  theta;
    }

    public static Vec4 rotation_to_quaternion(Mat m)
    {
        float trace = m.tr();

        Vec4 q = new Vec4();

        if(trace > 0.0f)
        {
            float s = (float)sqrt(trace + 1.0f);
            q.x = s*0.5f;
            s = 0.5f /s;
            q.y = (m.value(2,1) - m.value(1,2))*s;
            q.z = (m.value(0,2) - m.value(2,0))*s;
            q.w = (m.value(1,0) - m.value(0,1))*s;
        }else
        {
            if(m.value(0,0)>=m.value(1,1)&&m.value(0,0)>= m.value(2,2))
            {
                float s = (float) sqrt(1.0f + m.value(0,0) - m.value(1,1) - m.value(2,2));
                float invS = 0.5f / s;
                q.y = 0.5f*s;
                q.z = (m.value(1,0) + m.value(0,1)) * invS;
                q.w = (m.value(2,0) + m.value(0,2)) * invS;
                q.x = (m.value(2,1) - m.value(1,2)) * invS;
            }else if(m.value(1,1)>m.value(2,2))
            {
                float s = (float) sqrt(1.0f + m.value(1, 1) - m.value(0, 0) - m.value(2, 2));
                float invS = 0.5f / s;
                q.y = (m.value(0, 1) + m.value(1, 0)) * invS;
                q.z = 0.5f * s;
                q.w = (m.value(1, 2) + m.value(2, 1)) * invS;
                q.x = (m.value(0, 2) - m.value(2, 0)) * invS;
            }else {
                float s = (float) sqrt(1.0f + m.value(2, 2) - m.value(0, 0) - m.value(1, 1));
                float invS = 0.5f / s;
                q.y= (m.value(0, 2) + m.value(2, 0)) * invS;
                q.z = (m.value(1, 2) + m.value(2, 1)) * invS;
                q.w = 0.5f * s;
                q.x = (m.value(1, 0) - m.value(0, 1)) * invS;
            }
        }
        return q;
    }

    public static Mat look_at(Vec3 direction, Vec3 up)
    {
        Vec3 f = direction.normalized();
        Vec3 u = f.crossProduct(up.crossProduct(f)).normalized();
        Vec3 l = u.crossProduct(f).normalized();

        Mat a = new Mat(3,3);
        a.setValue(0, (float) f.x);
        a.setValue(1, (float) l.x);
        a.setValue(2, (float) u.x);
        a.setValue(3, (float) f.y);
        a.setValue(4, (float) l.y);
        a.setValue(5, (float) u.y);
        a.setValue(6, (float) f.z);
        a.setValue(7, (float) l.z);
        a.setValue(8, (float) u.z);
        return  a;
    }

    public static Mat r3_basis(Vec3 n)
    {
        float sign = (n.z >= 0.0f) ? 1.0f : -1.0f;
        float a = (float) (-1.0f / (sign + n.z));
        float b = (float) (n.x * n.y * a);

        Mat m = new Mat(3,3);
        m.setValue(0, (float) (1.0f+sign*n.x*n.x*a));
        m.setValue(1, (float) (-1.0f/(sign+n.z)));
        m.setValue(2, (float) n.x);
        m.setValue(3,sign*b);
        m.setValue(4, (float) (sign+n.y*n.y*a));
        m.setValue(5, (float) n.y);
        m.setValue(6, (float) (-sign*n.x));
        m.setValue(7, (float) -n.y);
        m.setValue(8, (float) n.z);

        return m;
    }

    public static float lerp(float a, float b,float t)
    {
        return a*(1.0f-t) + b*t;
    }

    public static float standard_deviation(List<Float> values)
    {
        final int n = values.size();

        float E_x = 0.0f;
        float E_xsq = 0.0f;

        for (int i = 0; i < n; i++) {
            E_x += values.get(i);
            E_xsq += values.get(i) * values.get(i);
        }

        E_x /= n;
        E_xsq /= n;

        float bessel_correction = (float) sqrt(n / (n-1));

        return (float) (bessel_correction * sqrt(E_xsq - E_x * E_x));
    }

    public static float mean(List<Float> values)
    {
        final int n = values.size();
        float E_x = 0.0f;

        for (int i = 0; i < n; i++) {
            E_x += values.get(i);
        }

        return E_x / n;
    }

}
