/**
 * @ImageWatermark.java
 * @Version 1.0 2010.02.26
 * @Author Xie-Hua Sun 
 */

package process.algorithms;

import java.awt.Image;
import java.io.*;
import process.algorithms.imageTrans.*;
import process.common.Common;

public class ImageWatermark
{
	Common common = new Common();
	
	//lsb混沌嵌入水印
	public int[] chaosEmbed(int[] pix, int[] water, int iw, int ih)
	{		
		for(int i = 0; i < iw*ih; i++)	
		{
			int emb;                         //含水印灰度 
			int gray;                        //灰度值			      
											
			//取(i,j)处灰度								
			gray = pix[i] & 0xFF;
			
			if(gray%2 == 1) gray = gray - 1; //最低位清零	        
							
			emb = gray + water[i];           //最低位嵌入水印
			pix[i] = 255<<24|emb<<16|emb<<8|emb;				 
		}
		
		return pix;		
	}
	
	//LSB混沌水印检测
	public double chaosDetect(int[] wtpix, int[] water, int iw, int ih)
	{		
		double prod = 0;
		int g;
		for(int i = 0; i < ih*iw; i++)	
		{
			//提取水印
			g = wtpix[i]&0x01;
					                           
		    prod = prod + g*water[i];
		}
		//计算相关性
		prod = prod/(iw*ih);
		return prod;				
	}
	
	public int[] chaos(double x, int iw, int ih)
    {
    	int[] water = new int[iw*ih];
    	
    	//先计算100次,使进入混沌状态
	    for(int i = 0; i < 100; i++)
            x = 4*x*(1-x);
        //生成混沌序列water[]
	    for(int i = 0; i < iw*ih; i++)
	    {
	    	x = 4*x*(1-x);
	    	if(x > 0.5) water[i] = 1;
	    	else        water[i] = 0;
	    }//生成混沌水印信息结束
		return water;	    
    }
    
    //patchwork水印嵌入
	public int[] patchworkEmbed(int[] pixels, int iw, int ih, int d)
	{
		int emb;                  //含水印灰度 
		int gray;                 //灰度值			      
				
		for(int j = 0; j < ih; j++)	
		{
			for(int i = 0; i < iw; i++)	
			{												
				//取灰度								
				gray = pixels[i+j*iw] & 0xFF;
					        
				//最低位嵌入水印
				if((i+j)%2 == 0)
				{
				    emb = gray + d;
				    if(emb > 255)
				        emb = 255;
				}       
				else
				{    
				    emb = gray - d;
				    if(emb < 0)
				        emb = 0;
				}				     
				pixels[i+j*iw] = 255<<24|emb<<16|emb<<8|emb;				 
			}
		}
		return pixels;		
	}
	
	//patchwork水印检测
	public double patchworkDetect(int[] pix, int iw, int ih)
	{
		double tema = 0, temb = 0;
		int gray;
		for(int j = 0; j < ih; j++)
		{
			for(int i = 0; i < iw; i++)	
			{
				gray = pix[i+j*iw]&0xFF;
				if((i+j)%2 == 0)
				    tema = tema + gray;
				else
				    temb = temb + gray;
			}
		}
		return Math.abs(2*(tema-temb)/(iw*ih));		
	}
	
	//读入水印图,并转变的二值图像矩阵
	public int[] readWater(int[] pix, int iw, int ih)
	{   
	    int   gray;                     //灰度值
	    
		for(int i = 0; i < iw*ih; i++)	
		{												
	        gray = pix[i] & 0xFF;       //取(i,j)处的灰度
			if(gray > 128) pix[i] = 0;  //white
			else           pix[i] = 1;  //black
		}		
		return pix;
	}
	
	public int[] getWater(int[] pix, int iw, int ih)
	{
		int g;		
		for(int i = 0; i < iw*ih; i++)	
    	{
    		g = pix[i] & 0xFF;
    		if(g%2 == 0) g = 255;
    		else   		 g = 0; 
    		pix[i] = 255 << 24|g<< 16|g << 8|g;
    	}	    
	    return pix;
	}
    
    //频域水印--------------------------------
    int maxlen;
    
    public int[] embedWatermark(int[] pix, int iw, int ih)
	{			
	    double[] in = new double[iw*ih];
				
	    WHT2 wht = new WHT2(iw, ih);		    
	    		     		    
	    for(int i = 0; i < ih*iw; i++)
	        in[i] = pix[i] & 0xff;        //源图像信息			        
	                   
        double[] tW = wht.WALSH(in, 16);			    
       
	    //设置密钥
		int key = common.getParam("输入密钥(111-999)","888");
			    		
        //嵌入水印		    			    		    
	    double[] sW = exFreq(tW, tW, 0.08, iw, ih, key, 1);
	    
	    //Walsh逆变换
	    double[] oW = wht.IWALSH(sW, 16);			    
	    
		for(int i = 0; i < ih*iw; i++)
		{
			int t = (int)oW[i];
			 
			// 判断是否超出[0,255]
			if(t > 255) t = 255;
			if(t < 0)   t = 0;
		    pix[i] = 255 << 24|t << 16|t << 8|t;					
		}
		return pix;
    }
    
    public double getSimValue(Image iImage, int[] pixels, int iw, int ih)			
    {
    	double[] in = new double[iw*ih];
    	for(int i = 0; i < ih*iw; i++)
		    in[i] = (double)pixels[i]; 
		     
		//Common common = new Common();    
		WHT2 wht = new WHT2(iw, ih);
		        
		double[] sW = wht.WALSH(in, 16);
		
		//设置密钥
		int key = common.getParam("输入密钥(111-999)","888");
		
		//用原图和密钥生成原加密序列cx[]----------
		pixels = common.grabber(iImage, iw, ih);
		    		     		    
		for(int i = 0; i < ih*iw; i++)
		    in[i] = pixels[i] & 0xff;  //源图像信息			        
		                   
        double[] tW = wht.WALSH(in, 16);	
	    double[] cx = getCx(tW, 0.08, iw, ih, key);
								              
	    //提取水印		    			    		    
	    double[] oW = exFreq(sW, tW, 0.08, iw, ih, key, -1);
        
	    //计算相似度sim
	    double sim = 0;
	    double ww  = 0;
	    for(int i = 0; i < 1000; i++)
	    {
	    	sim = sim + oW[i]*cx[i];
	    	ww  = ww  + oW[i]*oW[i];			    			    	
	    }
	    ww  = Math.sqrt(ww);
	    sim = sim / Math.sqrt(ww);
		return sim;
    }
    
    //扩频算法, pm = 1 表示嵌入水印, pm = -1 表示提取水印
    //用原图在同样条件下产生混沌序列
	public double[] getCx(double[] pix, double thresh, int iw, int ih, int key)
    {
	    double x = key / 1000.0;
    	double[] cx = new double[1000]; 
    	
    	int k = 0;
	    //用Logistic方程迭代100次，产生混沌序列
        for(int i = 0; i < 100; i++)
            x = 4 * x * (1 - x);            
    
    	for(int i = 0; i < ih*iw; i++)	
        {
            if(Math.abs(pix[i])>thresh&&k<1000&&i>2)
            {
            	//用Logistic方程产生混沌序列cx[]
            	x = 4 * x * (1 - x);
                cx[k] = x;
                k++;
            }
            if(k==1000) break;                                
        }
        return cx;
    } 
	
	//扩频算法, pm = 1 表示嵌入水印, pm = -1 表示提取水印
    public double[] exFreq(double[] pix, double[] tW, double alpha, 
                           int iw, int ih, int key, int pm)
    {
    	double thresh = 0.01;            //嵌入水印阈值
    	double x = key / 1000.0;
    	double[] rpix = new double[iw*ih];
    	
    	//用Logistic方程迭代100次，产生混沌序列
        for(int i = 0; i < 100; i++)
            x = 4 * x * (1 - x);
            
        int k = 0;	    		
							
		if(pm == 1)                      //嵌入
        {
        	for(int i = 0; i < ih*iw; i++)	
	        {
                if(Math.abs(pix[i])>thresh&&k<1000&&i>2)
                {
                	//用Logistic方程产生混沌序列cx[]
                	x = 4 * x * (1 - x);
                    rpix[i] = pix[i]*(1+alpha*x);//嵌入水印
                    k++;
                } 
                else
                   	rpix[i] = pix[i];                 
            } 
            maxlen = k;              
        }          
        else if(pm == -1)                        //提取
        { 
            for(int i = 0; i < ih*iw; i++)	
	        {               
                if(Math.abs(pix[i])>thresh&&k<maxlen&&i>2)
                   	rpix[i] = (pix[i]-tW[i])/(alpha*tW[i]);
                else
                   	rpix[i] = 0;
            }
        }
		return rpix;		
    }
    
    //DM算法-----------------------------------------
    double[][] freq;
	int[][] block;
	int step = 8;
    int[] d0, d1;
          
    //dm嵌入水印
	public double[][] dmEmbed(int[] pixels, int[] water, int iw, int ih)
	{		
		int size = 8;
		double[][] inIm = new double[iw][ih];
		double[][] outIm = new double[iw][ih];
		
		dmData(iw, ih);	
			
		//取出pixels[]中的blue分量组成2维数组inIm[][]		     		    
	    for(int j = 0; j < ih; j++)
	        for(int i = 0; i < iw; i++)	        
	            inIm[i][j] = pixels[i+j*iw] & 0xff;		            
	    
	  	DCT2 dct = new DCT2();
	  	
	  	double[][] dctWt =new double[iw][ih];
	  	   
        //8X8分块DCT变换      
	    double[][] dctIm = dct.dctTrans(inIm, iw, ih, size, 1);
	    	
		for(int j = 0; j < ih; j++)	
		{
			for(int i = 0; i < iw; i++)		
			{
				if(freq[i][j] > 0.25)
			        dctWt[i][j] = dctIm[i][j];
			    else
			    {
			        if(block[i][j]>1024)  //MNm=1024
			            dctWt[i][j] = dctIm[i][j];
			        else
			            if(water[block[i][j]]==255)	
			                dctWt[i][j] = Math.round((dctIm[i][j]
			                            + d1[block[i][j]])/step)
			                            * step-d1[block[i][j]];
			            else
			                dctWt[i][j] = Math.round((dctIm[i][j]
			                            + d0[block[i][j]])/step)
			                            * step-d0[block[i][j]];			        		        	
			    }								 
			}
		}
		//DCT逆变换
		outIm = dct.dctTrans(dctWt, iw, ih, size, -1);
		return outIm; 				
	}
	
	private void dmData(int iw, int ih)
	{		
	    freq = new double[iw][ih];
		block = new int[iw][ih]; 
		d0 = new int[1024];
		d1 = new int[1024];
		
		int im, jm;
		//计算(i,j)处的频率，用于判断在(i,j)处是否进行抖动量化
	    for(int j = 0; j < ih; j++)	
		{
			for(int i = 0; i < iw; i++)	
			{
				im = i%8;
				jm = j%8;
			    if(im==0||jm==0)
			        freq[i][j]=1;
			    else
			    	freq[i][j]= Math.sqrt((im-1)*(im-1)/256+(jm-1)*(jm-1)/256);
			}
		}
		
		//计算[i][j]属于哪个块，用于判断对[i][j]如何抖动量化
	    for(int j = 0; j < ih; j++)
	        for(int i = 0; i < iw; i++)				
		        block[i][j] = (int)(i/8)*32+(int)(j/8);
		 
		//产生抖动序列
		for(int i = 0; i < 1024; i++)
		{
			d0[i] = -step/4;
			d1[i] = d0[i]+step/2;
		}       	  
	}
	
	//提取水印
	public int[] getImageWater(int[] wtpix, int iw, int ih)
	{
		int gray;
		int[] water = new int[32*32];	    
	    
	    double[][] sy0 = new double[iw][ih];
	    double[][] sy1 = new double[iw][ih];
	    double[] t0    = new double[1024];
	    double[] t1    = new double[1024];
	    
	    dmData(iw, ih);
	    
	    for(int i = 0; i < 1024; i++)
	    { 
	        t0[i] = 0; t1[i] = 0;
	    }
	    
	    int size = 8;
		double[][] inIm = new double[iw][ih];
		
		//取出wtpix[]中的blue分量组成2维数组inIm[][]		     		    
	    for(int j = 0; j < ih; j++)
	        for(int i = 0; i < iw; i++)	        
	            inIm[i][j] = wtpix[i+j*iw];		            
	       
	    DCT2 dct = new DCT2();
	  	    
        //8X8分块DCT变换      
	    double[][] dctWt = dct.dctTrans(inIm, iw, ih, size, 1);
	    	    
	    for(int j = 0; j < ih; j++)
	    {
	    	for(int i = 0; i < iw; i++)
	    	{
	    	    if(freq[i][j]<=0.25&&block[i][j]<=1024){
	    	    	sy0[i][j] = Math.round((dctWt[i][j]
	    	    	          + d0[block[i][j]])/step)
	    	    	          * step-d0[block[i][j]];
	    	    	sy1[i][j] = Math.round((dctWt[i][j]
	    	    	          + d1[block[i][j]])/step)*step
	    	    	          - d1[block[i][j]];
	    	    	
	    	    	int k = block[i][j];
	    	    	t1[k] = t1[k] + (dctWt[i][j]-sy1[i][j])
	    	    	      * (dctWt[i][j]-sy1[i][j]);
	    	    	t0[k] = t0[k] + (dctWt[i][j]-sy0[i][j])
	    	    	      * (dctWt[i][j]-sy0[i][j]);
	    	    }	
	    	}
	    }
	    
	    for(int k = 0; k < 1024; k++)
	    {
	        if(t0[k]-t1[k]>0) water[k] = (255<<24)|(255<<16)|(255<<8)|255;
	        else              water[k] = (255<<24)|(0<<16)|(0<<8)|0;	
	    }
	    return water;	    
	}
    
    //读入32X32水印图,并转变的二值图像矩阵
	public int[] readImWater(int[] pix, int iw, int ih)
	{		
	    int gray;                        //灰度值	    
		for(int i = 0; i < iw*ih; i++)	
		{												
	        gray = pix[i] & 0xFF;        //取(i,j)处的灰度
			if(gray > 128) pix[i] = 255; //white
			else           pix[i] = 0;   //black
		}		
		return pix;
	}
}