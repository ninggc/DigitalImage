//DWT2.java

package process.algorithms.imageTrans;

public class DWT2
{
    int iw, ih;

    double[] s1;
    double[] w1;
    double[] s;
    
    //¹¹Ôìº¯Êý
    public DWT2(int w, int h) 
    { 
        iw = w;
        ih = h;
    }

    private void wavelet1D(double[] scl0, double[] p, double[] q,
                           double[] scl1, double[] wvl1)
    {
        int temp;
        int sclLen = scl0.length;
        int pLen = p.length;
        s1 = new double[sclLen / 2];
        w1 = new double[sclLen / 2];

        for (int i = 0; i < sclLen / 2; i++)
        {
            s1[i] = 0.0;
            w1[i] = 0.0;
            for (int j = 0; j < pLen; j++)
            {
                temp = (j + i * 2) % sclLen;
                s1[i] += p[j] * scl0[temp];
                w1[i] += q[j] * scl0[temp];
            }
        }
    }

    private void iwavelet1D(double[] s, double[] p, double[] q,
                            double[] scl1, double[] wvl1)
    {
        int temp;
        int sclLen = scl1.length;
        int pLen = p.length;
        
        for (int i = 0; i < sclLen; i++)
        {
            s[2 * i + 1] = 0.0;
            s[2 * i] = 0.0;
            for (int j = 0; j < pLen / 2; j++)
            {
                temp = (i - j + sclLen) % sclLen;
                s[2 * i + 1] += p[2 * j + 1] * scl1[temp]
                              + q[2 * j + 1] * wvl1[temp];
                s[2 * i] += p[2 * j] * scl1[temp] + q[2 * j] * wvl1[temp];
            }
        }
    }

    public double[] wavelet2D(double[] dataImage, double[] p, double[] q, int series)
    {
        double[] s = new double[iw / series];
        s1 = new double[iw / (2 * series)];
        w1 = new double[iw / (2 * series)];
        
        for (int j = 0; j < ih / series; j++)
        {
            for (int i = 0; i < iw / series; i++)
                s[i] = dataImage[j * iw / series + i];

            wavelet1D(s, p, q, s1, w1);

            for (int i = 0; i < iw / series; i++)
            {
                if (i < iw / (2 * series))
                    dataImage[j * iw / series + i] = s1[i];
                else
                    dataImage[j * iw / series + i] =
                           w1[i - iw / (2 * series)];
            }
        }

        for (int i = 0; i < iw / series; i++)
        {
            for (int j = 0; j < ih / series; j++)
                s[j] = dataImage[j * iw / series + i];

            wavelet1D(s, p, q, s1, w1);
            for (int j = 0; j < ih / series; j++)
            {
                if (j < ih / (2 * series))
                    dataImage[j * iw / series + i] = s1[j];
                else
                    dataImage[j * iw / series + i] =
                           w1[j - ih / (2 * series)];
            }
        }
        return dataImage;
    }
        
    public double[] iwavelet2D(double[] dataImage, double[] p,
                               double[] q, int series)
    {
        s  = new double[iw / series];
        s1 = new double[iw / (2 * series)];
        w1 = new double[iw / (2 * series)];
        for (int i = 0; i < iw / series; i++)
        {
            for (int j = 0; j < ih / series; j++)
            {
                if (j < ih / (2 * series))
                    s1[j] = dataImage[j * iw / series + i];
                else
                    w1[j - ih / (2 * series)] =
                              dataImage[j * iw / series + i];
            }
            iwavelet1D(s, p, q, s1, w1);
            for (int j = 0; j < ih / series; j++)
                dataImage[j * iw / series + i] = s[j];
        }
        for (int i = 0; i < ih / series; i++)
        {
            for (int j = 0; j < iw / series; j++)
            {
                if (j < iw / (2 * series))
                    s1[j] = dataImage[i * iw / series + j];
                else
                    w1[j - iw / (2 * series)] =
                             dataImage[i * iw / series + j];
            }
            iwavelet1D(s, p, q, s1, w1);
            for (int j = 0; j < iw / series; j++)
                dataImage[i * iw / series + j] = s[j];
        }
        return dataImage;
    }
}