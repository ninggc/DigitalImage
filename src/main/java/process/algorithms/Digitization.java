/** 
 * @Digitization.java
 * @Version 1.0 2010.02.10
 * @Author Xie-Hua Sun 
 */
 
package process.algorithms;

import java.awt.image.ColorModel;

public class Digitization
{	
	public int[] sample(int[] pix, int iw, int ih, int grey)
	{			
		//对图像进行采样
		ColorModel cm = ColorModel.getRGBdefault();
		
		int d  = (int)(256/grey);        //采样间隔
		int dd = d*d;
		for(int i = 0; i < ih; i = i+d)
		{
			for(int j = 0; j < iw; j = j+d)
			{
				int r = 0, g = 0, b = 0;			    
			    for(int k = 0; k < d; k++)
			        for(int l = 0; l < d; l++)
			            r = r + cm.getRed(pix[(i+k)*iw+(j+l)]);
			    for(int k = 0; k < d; k++)
			        for(int l = 0; l < d; l++)
			            g = g + cm.getGreen(pix[(i+k)*iw+(j+l)]);
			    for(int k = 0; k < d; k++)
			        for(int l = 0; l < d; l++)
			            b = b + cm.getBlue(pix[(i+k)*iw+(j+l)]);
			    r = (int)(r/dd);
			    g = (int)(g/dd);
			    b = (int)(b/dd);
			    for(int k = 0; k < d; k++)
			        for(int l = 0; l < d; l++)
			            pix[(i+k)*iw+(j+l)] = 255<<24|r<<16|g<<8|b;		   
			}
		}		
		return pix;
	}
	
	public int[] quantize(int[] pix, int iw, int ih, int level)
	{
		int greyLevel = 256/level;
		int tem, r, g, b;
		//对图像进行量化处理
		ColorModel cm=ColorModel.getRGBdefault();
		for(int i = 0; i< iw*ih; i++)
		{
			r  = cm.getRed(pix[i]);
			tem  = r / greyLevel;
			r = tem*greyLevel;
			g  = cm.getGreen(pix[i]);
			tem  = g / greyLevel;
			g = tem*greyLevel;
			b  = cm.getBlue(pix[i]);  
			tem  = b / greyLevel;
			b = tem*greyLevel;
			pix[i] = 255<<24|r<<16|g<<8|b;
		}
		return pix;
	}	
}