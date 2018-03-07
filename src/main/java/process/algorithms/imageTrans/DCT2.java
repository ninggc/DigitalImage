//DCT2.java

package process.algorithms.imageTrans;

public class DCT2
{
    double[][] dct_image;                 //DCT image;
    double[][] dct_coef;                  //DCTϵ������
    double[][] dct_coeft;                 //ת��DCTϵ������
    double[][] image;                     //bsizeXbsizeͼ���

    /*------------------------------------------------------------*
     * in_image  ����ͼ�����
     * iw, ih    ����ͼ����
     * bsize     bsizeXbsizeͼ���DCT�任
     * type      type = 1Ϊ��DCT�任, type =-1Ϊ��任
     *------------------------------------------------------------*/
    public double[][] dctTrans(double[][] img, int iw, int ih, int bsize, int type)
    {
        int iter_num = 256 / bsize;
        dct_image = new double[iw][ih];
        dct_coef  = new double[bsize][bsize];
        dct_coeft = new double[bsize][bsize];
        image = new double[bsize][bsize];
        
        coeff(dct_coef, bsize);
        
        //����ת�þ���ϵ��
        for (int i = 0; i < bsize; i++)
            for (int j = 0; j < bsize; j++)
                dct_coeft[i][j] = dct_coef[j][i];

        if (type == 1)
        {        	
            for (int j = 0; j < iter_num; j++)
            {
                for (int i = 0; i < iter_num; i++)
                {
                    //ȡbsizeXbsizeͼ���image[][]
                    for (int k = 0; k < bsize; k++)
                        for (int l = 0; l < bsize; l++)                            
                            image[k][l] = img[i * bsize + k][j * bsize + l];
                     
                    //bsizeXbsize��DCT�任
                    dct(image, dct_coeft, dct_coef, bsize);//���任
                    
                    //Output dct image
                    for (int k = 0; k < bsize; k++)
                        for (int l = 0; l < bsize; l++)
                            dct_image[i * bsize + k][j * bsize + l] = image[k][l];
                }
            }
        }
        else 
        {
            for (int j = 0; j < iter_num; j++)
            {
                for (int i = 0; i < iter_num; i++)
                {
                    //ȡbsizeXbsizeͼ���image[,]
                    for (int k = 0; k < bsize; k++)
                        for (int l = 0; l < bsize; l++)
                            image[k][l] = img[i * bsize + k][j * bsize + l];

                    //bsizeXbsize��IDCT�任
                    dct(image, dct_coef, dct_coeft, bsize);//��任
                    
                    //Output dct image
                    for (int k = 0; k < bsize; k++)
                        for (int l = 0; l < bsize; l++)
                            dct_image[i * bsize + k][j * bsize + l] = image[k][l];
                }
            }                
        }
        return dct_image;
    }

    public void coeff(double[][] dct_coef, int n)
    {
        double sqrt_1 = 1.0 / Math.sqrt(2.0);

        for (int i = 0; i < n; i++)
            dct_coef[0][i] = sqrt_1;

        //��ʼ��DCTϵ��
        for (int i = 1; i < n; i++)
            for (int j = 0; j < n; j++)
                dct_coef[i][j] = Math.cos(i * Math.PI * (j + 0.5) / ((double)n));
    }

    public void dct(double[][] a, double[][] b, double[][] c, int n)
    {
        double x;
        double[][] af = new double[n][n];

        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                x = 0.0;
                for (int k = 0; k < n; k++)
                    x += a[i][k] * b[k][j];
                af[i][j] = x;
            }
        }
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                x = 0.0;
                for (int k = 0; k < n; k++)
                    x += c[i][k] * af[k][j];
                a[i][j] = 2.0 * x / ((double)n);
            }
        }
    }
}