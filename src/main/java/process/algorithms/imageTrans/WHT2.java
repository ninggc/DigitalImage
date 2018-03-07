//WHT2.java

package process.algorithms.imageTrans;

public class WHT2
{
    int iw, ih;

    public WHT2(int w, int h)
    {
        iw = w; ih = h;
    }

    //Walsh-Hadamard�任
    public double[] WALSH(double[] pix1, int n)
    {
        //�м����
        int bfsize, t;

        //����任����
        double count = 1 << n;

        //�����������������
        double[] pix2 = new double[iw * ih];
        double[] temp = new double[iw * ih];

        // ��������
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

            //����pis1��pis2  
            temp = pix1;
            pix1 = pix2;
            pix2 = temp;
        }

        // ����ϵ��
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

    //Walsh-Hadamard��任
    public double[] IWALSH(double[] pix, int n)
    {
        //����任����
        double count = 1 << n;
        double[] f = WALSH(pix, n);

        //����ϵ��
        for (int j = 0; j < count; j++)
            f[j] *= count;

        return f;
    }
}