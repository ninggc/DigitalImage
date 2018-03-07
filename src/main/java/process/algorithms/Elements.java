/**
 * @Elements.java
 * @Version 1.0 2010.02.09
 * @Author Xie-Hua Sun 
 */

package process.algorithms;

import java.awt.Image;
import java.awt.image.*;
import process.common.Common;

public class Elements
{    
    //转变为灰度图像
    public int[] toGray(int[] pix, int iw, int ih)
    {    
		ColorModel cm = ColorModel.getRGBdefault();
		int r, g, b, gray;
		
		for(int i = 0; i < iw*ih; i++)	
		{			
			r = cm.getRed(pix[i]);
			g = cm.getGreen(pix[i]);
			b = cm.getBlue(pix[i]);	
			gray =(int)((r + g + b) / 3);
			pix[i] = 255 << 24|gray << 16|gray << 8|gray;
		}		
		return pix;
	}	
				
    //灰度阈值变换
    public int[] thresh(int[] pix, int iw, int ih, int th)
    {    
		ColorModel cm = ColorModel.getRGBdefault();
		int r, g, b, gray;
		
		for(int i = 0; i < iw*ih; i++)	
		{			
			r = cm.getRed(pix[i]);
			g = cm.getGreen(pix[i]);
			b = cm.getBlue(pix[i]);	
			gray =(int)((r + g + b) / 3);
							
			if(gray > th) gray = 255;
			else	      gray = 0;								
			pix[i] = 255 << 24|gray << 16|gray << 8|gray;
		}
		return pix;
    }
    
    //线性变换
    public int[] linetrans(int[] pix, int iw, int ih, float p, int q)
    {
		ColorModel cm = ColorModel.getRGBdefault();
		int r, g, b;
		
		for(int i = 0; i < iw*ih; i++) 
		{
			r = cm.getRed(pix[i]);
			g = cm.getGreen(pix[i]);
			b = cm.getBlue(pix[i]);
			
			//增加图像亮度
			r  = (int)(p * r + q);
			g  = (int)(p * g + q);
			b  = (int)(p * b + q);
			
			if(r >= 255)   r = 255;
			if(g >= 255)   g = 255;
			if(b >= 255)   b = 255;
			
			pix[i] = 255 << 24|r << 16|g << 8|b;
		}
		return pix;
	}
	
	//伪彩色处理
    public int[] falseColor(int[] pix, int iw, int ih, int p, int q)
    {    	
		ColorModel cm = ColorModel.getRGBdefault();
		int r, g, b;
		
		for(int j = 0; j < ih; j++)
		{		 
			for(int i = 0; i < iw; i++)
			{
				r = cm.getRed(pix[j*iw+i]);
				g = cm.getGreen(pix[j*iw+i]);
				b = cm.getBlue(pix[j*iw+i]);
				//R变换
	            if (r < p) r = 0;
	            else if (r >= p && r < q) r = (int)((r - p) * 255 / (q - p));
	            else r = 255;
	
	            //G变换
	            if (g < p) g = (int)(g * 255 / p);
	            else if (g >= p && g < q) g = 255;
	            else g = (int)((255 - g) * 255 / (255 - q));
	
	            //B变换
	            if (b < p) b = 255;
	            else if (b >= p && b < q) b = (int)((q - b) * 255 / (q - p));
	            else b = 0;
				
				pix[j*iw+i] = 255 << 24|r << 16|g << 8|b;
			}
		}
		return pix;
	}
	
    //图像融合
    public int[] combine(int[] pix1,int[] pix2,int iw, int ih, float p1, float p2)
    {
    	ColorModel cm = ColorModel.getRGBdefault();
		int[] mpix = new int[iw * ih];
		int r, g, b;
		
		for(int i = 0; i < ih; i++)
		{
		    for(int j = 0; j < iw; j++)
		    {		       		       
		       r = (int)(p1*cm.getRed(pix1[i*iw+j])+
		                 p2*cm.getRed(pix2[i*iw+j]));		
			   g = (int)(p1*cm.getGreen(pix1[i*iw+j])+
			             p2*cm.getGreen(pix2[i*iw+j]));
			   b = (int)(p1*cm.getBlue(pix1[i*iw+j])+
			             p2*cm.getBlue(pix2[i*iw+j]));
			   mpix[i*iw+j] = 255 << 24|r << 16|g << 8|b;
		    }
		}
		return mpix;
    }
    
    //图像合成
    public int[] merge(Image img, int w, int h)
    {
    	int r, g, b;		
		int[] pix = (new Common()).grabber(img, w, h);
		 
	    ColorModel cm = ColorModel.getRGBdefault();
	    for(int i = 0; i < w*h; i++)
	    {		            
            r = cm.getRed(pix[i]);		
		    g = cm.getGreen(pix[i]);
		    b = cm.getBlue(pix[i]);
		    //背景透明度改为0  
		    if(r > 230 && g > 230 && b > 230)
		        pix[i] = 0 << 24|r << 16|g << 8|b;		    
	    }
	    return pix;
	}	   
}
