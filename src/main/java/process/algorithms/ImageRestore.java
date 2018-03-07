/*
 * ImageRestore.java
 */

package process.algorithms;

import process.algorithms.imageTrans.FFT2;
import process.algorithms.imageTrans.Complex;

public class ImageRestore
{	
    FFT2 fft2;
		
    //对图像进行模糊处理
    public int[] imBlur(int[] pixels, int iw, int ih)
    {    				
		double[] newPixels = new double [iw*ih];
		double[] newKernel = new double [iw*ih];
		
		for(int j = 0;j < ih; j++)
		{
			for(int i = 0; i < iw; i++)
			{
				newPixels[i+j*iw] = pixels[i+j*iw]&0xff;
				if((i < 5) && (j < 5))
					newKernel[i+j*iw] = 1.0/25;
				else 
				    newKernel[i+j*iw] = 0; 
			}
		}
	
		//初始化
		Complex [] complex   = new Complex[iw*ih];
		Complex [] comKernel = new Complex[iw*ih];
		
		for(int i = 0; i < iw*ih; i++)
		{
			complex[i]   = new Complex(0,0);
			comKernel[i] = new Complex(0,0);
		}
		
		//对原图像进行FFT
		fft2 = new FFT2();
		fft2.setData2(iw, ih, newPixels);
		complex = fft2.getFFT2();
		
		//对卷积核进行FFT
		fft2 = new FFT2();
		fft2.setData2(iw, ih, newKernel);		
		comKernel = fft2.getFFT2();
		
		//频域相乘
		for(int i = 0; i < iw*ih; i++)
		{
			double re = complex[i].re*comKernel[i].re 
			          - complex[i].im*comKernel[i].im;
			double im = complex[i].re*comKernel[i].im 
			          + complex[i].im*comKernel[i].re;
			complex[i].re = re;
			complex[i].im = im;	
		}
				
		//进行FFT反变换
		fft2 = new FFT2();
		fft2.setData2i(iw, ih, complex);
		pixels = fft2.getPixels2i();
		return pixels;       	
    }
        
    //图像恢复
    public int[] imRestore(int[] pixels, int iw, int ih) 
    {				
		double[] newPixels = new double [iw*ih];
		double[] newKernel = new double [iw*ih];
		
		//初始化
		for(int j = 0; j < ih; j++)
		{
			for(int i = 0; i < iw; i++)
			{
				newPixels[i+j*iw] = pixels[i+j*iw]&0xff;
				if((i<5) && (j<5))
					newKernel[i+j*iw] = 1.0/25;					
				else 
				    newKernel[i+j*iw] = 0;
			}
		}
		
		//初始化
		Complex[] complex   = new Complex[iw*ih];
		Complex[] comKernel = new Complex[iw*ih];
		for(int i = 0;i < iw*ih; i++)
		{
			complex[i]   = new Complex(0,0);
			comKernel[i] = new Complex(0,0);
		}
	
		//对原图像进行FFT
		fft2 = new FFT2();
		fft2.setData2(iw, ih, newPixels);
		complex = fft2.getFFT2();
		
		//对卷积核进行FFT
		fft2 = new FFT2();
		fft2.setData2(iw, ih, newKernel);
		comKernel = fft2.getFFT2();
		
		//逆滤波复原
		for(int j = 0;j < ih; j++)
		{
			for(int i = 0; i < iw; i++)
			{
				double re = complex[i+j*iw].re;
				double im = complex[i+j*iw].im;
				double reKernel = comKernel[i+j*iw].re;
				double imKernel = comKernel[i+j*iw].im;
				double x = reKernel*reKernel+imKernel*imKernel;
				
				if(x > 1e-3)
				{
					double r = (re*reKernel+im*imKernel)/x;
					double m = (im*reKernel-re*imKernel)/x;	
					complex[i+j*iw].re = r;
					complex[i+j*iw].im = m;
				}				
			}
		}
		
		//进行FFT反变换
		fft2 = new FFT2();
		fft2.setData2i(iw, ih, complex);
		pixels = fft2.getPixels2i();				
	    return pixels;   	
    }    
}