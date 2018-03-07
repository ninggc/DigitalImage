//WHT2.java

package process.algorithms.imageTrans;

public class WHT2
{
    int iw, ih;

    public WHT2(int w, int h)
    {
        iw = w; ih = h;
    }

    //Walsh-Hadamard变换
    public double[] WALSH(double[] pix1, int n)
    {
        //中间变量
        int bfsize, t;

        //计算变换点数
        double count = 1 << n;

        //分配运算所需的数组
        double[] pix2 = new double[iw * ih];
        double[] temp = new double[iw * ih];

        // 蝶形运算
        for (int k = 0; k < n; k++)
        {
            for (int j = 0; j < 1 << k; j++)
            {
                bfsize = 1 << (n - k);
                for (int i = 0; i < bfsize / 2; i++)
                {
                    t = j * bfsize;
                    pix2[i + t] = pix1[i + t] + pix1[i + t + bfsize / 2];
                    pix2[i + t + bfsize / 2] = pix1[i + t] 
                               - pix1[i + t + bfsize / 2];
                }
            }

            //交换pis1和pis2  
            temp = pix1;
            pix1 = pix2;
            pix2 = temp;
        }

        // 调整系数
        for (int j = 0; j < count; j++)
        {
            t = 0;
            for (int i = 0; i < n; i++)
                if ((j & (1 << i)) != 0)
                    t += 1 << (n - i - 1);

            temp[j] = pix1[t] / count;
        }
        return temp;
    }

    //Walsh-Hadamard逆变换
    public double[] IWALSH(double[] pix, int n)
    {
        //计算变换点数
        double count = 1 << n;
        double[] f = WALSH(pix, n);

        //调整系数
        for (int j = 0; j < count; j++)
            f[j] *= count;

        return f;
    }
}