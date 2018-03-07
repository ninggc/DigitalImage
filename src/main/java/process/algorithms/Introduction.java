/**
 * @Introduction.java
 * @Version 1.0 2010.02.09
 * @Author Xie-Hua Sun 
 */

package process.algorithms;

import java.awt.image.ColorModel;
import process.common.Common;

public class Introduction 
{
	public double getEntropy(int[] pix, int w, int h)
    {
        double H = 0.0;
        Common common = new Common();
        int[] r = common.getHist(pix, w, h);//����ֱ��ͼ
        
        /*----------------------------------------------------*
         * log2(C) = log10(C)/log10(2)
         * H = - \Sigma [pix[i]/(w*h)]log2[pix[i]/(w*h)]
         *   = - \Sigma pix[i]*log10(pix[i])/(w*h*log10(2))
         *     + log10(w*h)/log10(2)             
         *----------------------------------------------------*/

        for (int i = 0; i < 256; i++)
            if(r[i] > 0)
                H = H + r[i] * Math.log(r[i]);               
        
        H = Math.log(w * h) / Math.log(2) - H / (w * h * Math.log(2));
        return H;
    } 
        
    //ƽ��ֵ
    public double getAverage(int[] pix, int w, int h)
    {
    	double sum = 0.0;
        ColorModel cm = ColorModel.getRGBdefault();
        for (int j = 0; j < h; j++)        
            for(int i = 0; i < w; i++)            
                sum = sum + cm.getRed(pix[i+j*w]);       
        double av = sum / (w*h);        
        return av;
    }
    
    public int getMedian(int[] pix, int w, int h)
    {
    	ColorModel cm = ColorModel.getRGBdefault();
        int[] p = new int[25];
        
        for (int j = 0; j < 5; j++)
            for (int i = 0; i < 5; i++)
                p[i+j*5] = cm.getRed(pix[100 + i+(100 + j)*w]); 
        //����
        p = sorter(p, 25);             
        return p[12];                                   //��ֵ
    }
    
    //ð������
    public int[] sorter(int[] dt, int m)
    {
        int tem;
        for (int k = m - 1; k >= 1; k--)
            for (int l = 1; l <= k; l++)
                if (dt[l - 1] > dt[l])
                {
                    tem = dt[l];
                    dt[l] = dt[l - 1];
                    dt[l - 1] = tem;
                }
        return dt;
    }
    
    //������
    public double getSqsum(int[] pix, int w, int h)
    {
    	ColorModel cm = ColorModel.getRGBdefault();
        double aver = getAverage(pix, w, h);//����ƽ��ֵ
        
        //���㷽��
        int b;
        double sqsum = 0.0;
        for (int j = 0; j < h; j++)
        {
            for (int i = 0; i < w; i++)
            {
                b = cm.getRed(pix[i+j*w]);
                sqsum = sqsum + (b-aver)*(b-aver);
            }
        }
        sqsum = sqsum / (w * h);              
        return sqsum;
    }
    
    //������ȷ�����
    public int getSupDis(int[] pix1, int[] pix2, int w, int h)
    {
    	ColorModel cm = ColorModel.getRGBdefault();    	
    	
    	int g1, g2, tem, sup = 0;
        for (int j = 0; j < h; j++)
        {
            for (int i = 0; i < w; i++)
            {
                g1 = cm.getRed(pix1[i+j*w]);
                g2 = cm.getRed(pix2[i+j*w]);
                tem = Math.abs(g1-g2);
                if (tem > sup) 
                    sup = tem;
            }
        }
        return sup;
    }
        
    //�������������
    public double getRmsDis(int[] pix1, int[] pix2, int w, int h)
    {
    	return Math.sqrt(getMSE(pix1, pix2, w, h));        
    }
    
    //����������MSE
    public double getMSE(int[] pix1, int[] pix2, int w, int h)
    {
    	ColorModel cm = ColorModel.getRGBdefault();    	
    	
    	int g1, g2;
    	double sum = 0.0;
        for (int j = 0; j < h; j++)
        {
            for (int i = 0; i < w; i++)
            {
                g1 = cm.getRed(pix1[i+j*w]);
                g2 = cm.getRed(pix2[i+j*w]);
                sum = sum + (g1 - g2)*(g1 - g2);
            }
        }
        sum = sum / (w * h);
        return sum;
    }
    
    //�����ֵ�����PSNR
    public double getPSNR(int[] pix1, int[] pix2, int w, int h)
    {
    	double rms = getRmsDis(pix1,pix2,w,h);
    	double psnr = 20 * Math.log(255/rms)/Math.log(10);
    	return psnr;
    }
}
