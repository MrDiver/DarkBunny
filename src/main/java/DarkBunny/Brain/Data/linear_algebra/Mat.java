package DarkBunny.Brain.Data.linear_algebra;

public class Mat {
    final int N0, N1;
    float data[];

    public Mat(int N0, int N1) {
        this.N0 = N0;
        this.N1 = N1;
        data = new float[N0 * N1];
    }

    public Mat(int N0, int N1, float value) {
        this.N0 = N0;
        this.N1 = N1;
        data = new float[N0 * N1];
        for (int i = 0; i < N0 * N1; i++) {
            data[i] = value;
        }
    }

    public Mat(int N0, int N1, float value[]) {
        this.N0 = N0;
        this.N1 = N1;
        data = new float[N0 * N1];
        for (int i = 0; i < N0 * N1; i++) {
            data[i] = value[i];
        }
    }

    public float value(int i) {
        return data[i];
    }

    public void setValue(int i, float value) {
        data[i] = value;
    }

    public float value(int x, int y) {
        return data[x + y * N0];
    }

    public void setValue(int x, int y, float value) {
        data[x + y * N0] = value;
    }

    public Mat transpose() {
        Mat AT = new Mat(N0, N1);
        for (int i = 0; i < N0; i++) {
            for (int j = 0; j < N1; j++) {
                AT.setValue(j, i, this.value(i, j));
            }
        }
        return AT;
    }

    public Mat plus(Mat b) {
        Mat c = new Mat(N0, N1);
        for (int i = 0; i < N0; i++) {
            for (int j = 0; j < N1; j++) {
                c.setValue(i, j, this.value(i, j) + b.value(i, j));
            }
        }
        return c;
    }

    public Mat minus(Mat b) {
        Mat c = new Mat(N0, N1);
        for (int i = 0; i < N0; i++) {
            for (int j = 0; j < N1; j++) {
                c.setValue(i, j, this.value(i, j) - b.value(i, j));
            }
        }
        return c;
    }

    public Mat scale(float b) {
        Mat c = new Mat(N0, N1);
        for (int i = 0; i < N0; i++) {
            for (int j = 0; j < N1; j++) {
                c.setValue(i, j, this.value(i, j) * b);
            }
        }
        return c;
    }

    public Mat mult(Mat b) {
        Mat c = new Mat(N0, N1);
        for (int i = 0; i < N0; i++) {
            for (int j = 0; j < N1; j++) {
                c.setValue(i, j, this.value(i, j) * b.value(i, j));
            }
        }
        return c;
    }

    public Mat divide(float b)
    {
        Mat c = new Mat(N0,N1);
        for(int i = 0; i < N0; i++)
        {
            for (int j = 0; j < N1;j++)
            {
                c.setValue(i,j,this.value(i,j)/b);
            }
        }
        return c;
    }

    public static Mat eye(int x)
    {
        Mat c = new Mat(x,x);
        for(int i = 0; i < x; i++)
        {
            for (int j = 0; j < x;j++)
            {
                c.setValue(i,j,i==j?1:0);
            }
        }
        return c;
    }

    public float fnorm()
    {
        float sum = 0.0f;
        for(int i = 0; i < N0; i++)
        {
            for (int j = 0; j < N1;j++)
            {
                sum += value(i,j);
            }
        }
        return sum;
    }

    public float tr()
    {
        float sum = 0.0f;
        for(int i = 0; i < N0; i++)
        {
                sum += value(i,i);
        }
        return sum;
    }

    public float det()
    {
        if(N0==2 && N1 == 2)
            return value(0,0) * value(1,1) - value(0,1) * value(1,0);

        if(N0==3 && N1 == 3)
            return value(0, 0) * value(1, 1) * value(2, 2) + value(0, 1) * value(1, 2) * value(2, 0) +
                    value(0, 2) * value(1, 0) * value(2, 1) - value(0, 0) * value(1, 2) * value(2, 1) -
                    value(0, 1) * value(1, 0) * value(2, 2) - value(0, 2) * value(1, 1) * value(2, 0);

        if(N0==4 && N1 == 4)
            return value(0, 3) * value(1, 2) * value(2, 1) * value(3, 0) -
                value(0, 2) * value(1, 3) * value(2, 1) * value(3, 0) -
                value(0, 3) * value(1, 1) * value(2, 2) * value(3, 0) +
                value(0, 1) * value(1, 3) * value(2, 2) * value(3, 0) +
                value(0, 2) * value(1, 1) * value(2, 3) * value(3, 0) -
                value(0, 1) * value(1, 2) * value(2, 3) * value(3, 0) -
                value(0, 3) * value(1, 2) * value(2, 0) * value(3, 1) +
                value(0, 2) * value(1, 3) * value(2, 0) * value(3, 1) +
                value(0, 3) * value(1, 0) * value(2, 2) * value(3, 1) -
                value(0, 0) * value(1, 3) * value(2, 2) * value(3, 1) -
                value(0, 2) * value(1, 0) * value(2, 3) * value(3, 1) +
                value(0, 0) * value(1, 2) * value(2, 3) * value(3, 1) +
                value(0, 3) * value(1, 1) * value(2, 0) * value(3, 2) -
                value(0, 1) * value(1, 3) * value(2, 0) * value(3, 2) -
                value(0, 3) * value(1, 0) * value(2, 1) * value(3, 2) +
                value(0, 0) * value(1, 3) * value(2, 1) * value(3, 2) +
                value(0, 1) * value(1, 0) * value(2, 3) * value(3, 2) -
                value(0, 0) * value(1, 1) * value(2, 3) * value(3, 2) -
                value(0, 2) * value(1, 1) * value(2, 0) * value(3, 3) +
                value(0, 1) * value(1, 2) * value(2, 0) * value(3, 3) +
                value(0, 2) * value(1, 0) * value(2, 1) * value(3, 3) -
                value(0, 0) * value(1, 2) * value(2, 1) * value(3, 3) -
                value(0, 1) * value(1, 0) * value(2, 2) * value(3, 3) +
                value(0, 0) * value(1, 1) * value(2, 2) * value(3, 3);
        return 0;
    }

    public float I1()
    {
        return tr();
    }

    public float I2() {
        float trA = tr();
        float trAA = 0.0f;
        for (int i = 0; i < N0; i++) {
            for (int j = 0; j < N0; j++) {
                trAA += value(i, j) * value(j, i);
            }
        }
        return 0.5f * (trAA - trA * trA);
    }

    public float I3()
    {
        return det();
    }

    public Mat dev() {
        Mat A_dev = this.clone();
        float trA = tr();
        for (int i = 0; i < N0; i++) {
            A_dev.setValue(i, i,A_dev.value(i,i)-trA / N0);
        }
        return A_dev;
    }

    public Mat inv() {
        Mat invA = new Mat(1,1);

        float inv_detA = 1.0f / det();

        if(N0==2&&N1 ==2)
        {
            invA = new Mat(2,2);
            invA.setValue(0, 0,value(1, 1) * inv_detA);
            invA.setValue(0, 1,-value(0, 1) * inv_detA);
            invA.setValue(1, 0,-value(1, 0) * inv_detA);
            invA.setValue(1, 1,value(0, 0) * inv_detA);
        }

        if(N0==3&&N1==3)
        {
            invA = new Mat(3,3);
            invA.setValue(0, 0,((value(1, 1) * value(2, 2) - value(1, 2) * value(2, 1)) * inv_detA));
            invA.setValue(0, 1,(value(0, 2) * value(2, 1) - value(0, 1) * value(2, 2)) * inv_detA);
            invA.setValue(0, 2,(value(0, 1) * value(1, 2) - value(0, 2) * value(1, 1)) * inv_detA);
            invA.setValue(1, 0,(value(1, 2) * value(2, 0) - value(1, 0) * value(2, 2)) * inv_detA);
            invA.setValue(1, 1,(value(0, 0) * value(2, 2) - value(0, 2) * value(2, 0)) * inv_detA);
            invA.setValue(1, 2,(value(0, 2) * value(1, 0) - value(0, 0) * value(1, 2)) * inv_detA);
            invA.setValue(2, 0,(value(1, 0) * value(2, 1) - value(1, 1) * value(2, 0)) * inv_detA);
            invA.setValue(2, 1,(value(0, 1) * value(2, 0) - value(0, 0) * value(2, 1)) * inv_detA);
            invA.setValue(2, 2,(value(0, 0) * value(1, 1) - value(0, 1) * value(1, 0)) * inv_detA);
        }

        if(N0==4&&N1==4){
            invA = new Mat(4,4);
            invA.setValue(0,0,(value(1, 2) * value(2, 3) * value(3, 1) - value(1, 3) * value(2, 2) * value(3, 1) +
                    value(1, 3) * value(2, 1) * value(3, 2) - value(1, 1) * value(2, 3) * value(3, 2) -
                    value(1, 2) * value(2, 1) * value(3, 3) + value(1, 1) * value(2, 2) * value(3, 3)) *
                    inv_detA);
            invA.setValue(0,1,(value(0, 3) * value(2, 2) * value(3, 1) - value(0, 2) * value(2, 3) * value(3, 1) -
                    value(0, 3) * value(2, 1) * value(3, 2) + value(0, 1) * value(2, 3) * value(3, 2) +
                    value(0, 2) * value(2, 1) * value(3, 3) - value(0, 1) * value(2, 2) * value(3, 3)) *
                    inv_detA);
            invA.setValue(0,2, (value(0, 2) * value(1, 3) * value(3, 1) - value(0, 3) * value(1, 2) * value(3, 1) +
                    value(0, 3) * value(1, 1) * value(3, 2) - value(0, 1) * value(1, 3) * value(3, 2) -
                    value(0, 2) * value(1, 1) * value(3, 3) + value(0, 1) * value(1, 2) * value(3, 3)) *
                    inv_detA);
            invA.setValue(0,3,(value(0, 3) * value(1, 2) * value(2, 1) - value(0, 2) * value(1, 3) * value(2, 1) -
                    value(0, 3) * value(1, 1) * value(2, 2) + value(0, 1) * value(1, 3) * value(2, 2) +
                    value(0, 2) * value(1, 1) * value(2, 3) - value(0, 1) * value(1, 2) * value(2, 3)) *
                    inv_detA);
            invA.setValue(1, 0,value(1, 3) * value(2, 2) * value(3, 0) - value(1, 2) * value(2, 3) * value(3, 0) -
                    value(1, 3) * value(2, 0) * value(3, 2) + value(1, 0) * value(2, 3) * value(3, 2) +
                    value(1, 2) * value(2, 0) * value(3, 3) - value(1, 0) * value(2, 2) * value(3, 3) *
                    inv_detA);
            invA.setValue(1, 1,value(0, 2) * value(2, 3) * value(3, 0) - value(0, 3) * value(2, 2) * value(3, 0) +
                    value(0, 3) * value(2, 0) * value(3, 2) - value(0, 0) * value(2, 3) * value(3, 2) -
                    value(0, 2) * value(2, 0) * value(3, 3) + value(0, 0) * value(2, 2) * value(3, 3) *
                    inv_detA);
            invA.setValue(1, 2,value(0, 3) * value(1, 2) * value(3, 0) - value(0, 2) * value(1, 3) * value(3, 0) -
                    value(0, 3) * value(1, 0) * value(3, 2) + value(0, 0) * value(1, 3) * value(3, 2) +
                    value(0, 2) * value(1, 0) * value(3, 3) - value(0, 0) * value(1, 2) * value(3, 3) *
                    inv_detA);
            invA.setValue(1, 3,value(0, 2) * value(1, 3) * value(2, 0) - value(0, 3) * value(1, 2) * value(2, 0) +
                    value(0, 3) * value(1, 0) * value(2, 2) - value(0, 0) * value(1, 3) * value(2, 2) -
                    value(0, 2) * value(1, 0) * value(2, 3) + value(0, 0) * value(1, 2) * value(2, 3) *
                    inv_detA);
            invA.setValue(2, 0,value(1, 1) * value(2, 3) * value(3, 0) - value(1, 3) * value(2, 1) * value(3, 0) +
                    value(1, 3) * value(2, 0) * value(3, 1) - value(1, 0) * value(2, 3) * value(3, 1) -
                    value(1, 1) * value(2, 0) * value(3, 3) + value(1, 0) * value(2, 1) * value(3, 3) *
                    inv_detA);
            invA.setValue(2, 1,value(0, 3) * value(2, 1) * value(3, 0) - value(0, 1) * value(2, 3) * value(3, 0) -
                    value(0, 3) * value(2, 0) * value(3, 1) + value(0, 0) * value(2, 3) * value(3, 1) +
                    value(0, 1) * value(2, 0) * value(3, 3) - value(0, 0) * value(2, 1) * value(3, 3) *
                    inv_detA);
            invA.setValue(2, 2,value(0, 1) * value(1, 3) * value(3, 0) - value(0, 3) * value(1, 1) * value(3, 0) +
                    value(0, 3) * value(1, 0) * value(3, 1) - value(0, 0) * value(1, 3) * value(3, 1) -
                    value(0, 1) * value(1, 0) * value(3, 3) + value(0, 0) * value(1, 1) * value(3, 3) *
                    inv_detA);
            invA.setValue(2, 3,value(0, 3) * value(1, 1) * value(2, 0) - value(0, 1) * value(1, 3) * value(2, 0) -
                    value(0, 3) * value(1, 0) * value(2, 1) + value(0, 0) * value(1, 3) * value(2, 1) +
                    value(0, 1) * value(1, 0) * value(2, 3) - value(0, 0) * value(1, 1) * value(2, 3) *
                    inv_detA);
            invA.setValue(3, 0,value(1, 2) * value(2, 1) * value(3, 0) - value(1, 1) * value(2, 2) * value(3, 0) -
                    value(1, 2) * value(2, 0) * value(3, 1) + value(1, 0) * value(2, 2) * value(3, 1) +
                    value(1, 1) * value(2, 0) * value(3, 2) - value(1, 0) * value(2, 1) * value(3, 2) *
                    inv_detA);
            invA.setValue(3, 1,value(0, 1) * value(2, 2) * value(3, 0) - value(0, 2) * value(2, 1) * value(3, 0) +
                    value(0, 2) * value(2, 0) * value(3, 1) - value(0, 0) * value(2, 2) * value(3, 1) -
                    value(0, 1) * value(2, 0) * value(3, 2) + value(0, 0) * value(2, 1) * value(3, 2) *
                    inv_detA);
            invA.setValue(3, 2,value(0, 2) * value(1, 1) * value(3, 0) - value(0, 1) * value(1, 2) * value(3, 0) -
                    value(0, 2) * value(1, 0) * value(3, 1) + value(0, 0) * value(1, 2) * value(3, 1) +
                    value(0, 1) * value(1, 0) * value(3, 2) - value(0, 0) * value(1, 1) * value(3, 2) *
                    inv_detA);
            invA.setValue(3, 3,value(0, 1) * value(1, 2) * value(2, 0) - value(0, 2) * value(1, 1) * value(2, 0) +
                    value(0, 2) * value(1, 0) * value(2, 1) - value(0, 0) * value(1, 2) * value(2, 1) -
                    value(0, 1) * value(1, 0) * value(2, 2) + value(0, 0) * value(1, 1) * value(2, 2) *
                    inv_detA);
        }

        return invA;
    }

    public Mat gram() {
        Mat ATA = new Mat(N0, N0);

        for (int i = 0; i < N0; i++) {
            for (int j = 0; j < N0; j++) {
                ATA.setValue(i, j, 0.0f);
                for (int k = 0; k < N1; k++) {
                    ATA.setValue(i, j, ATA.value(i, j) + value(k, i) * value(k, j));
                }
            }
        }
        return ATA;
    }

    public Mat dot(Mat B)
    {
        Mat C = new Mat(N0,B.N1);

        for (int i = 0; i < C.N0; i++) {
            for (int j = 0; j < C.N1; j++) {
                C.setValue(i, j, 0.0f);
                for (int k = 0; k < this.N1; k++) {
                    C.setValue(i, j,C.value(i,j)+ value(i, k) * B.value(k, j));
                }
            }
        }
        return C;
    }

    public Vec3 dot(Vec3 B)
    {
        Vec3 C = new Vec3();
        for(int y = 0; y < N1; y++)
        {
            double sum = 0;
            for(int x = 0; x < N0; x++)
            {
                sum += this.value(x,y)*B.value(x);
            }
            C.setValue(y,sum);
        }

        return C;
    }

    public Mat clone()
    {
        Mat a = new Mat(N0,N1);
        for (int i = 0 ; i < N0*N1;i++)
        {
            a.setValue(i,this.value(i));
        }
        return a;
    }
}