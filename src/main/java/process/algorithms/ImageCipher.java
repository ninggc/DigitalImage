/**
 * @ImageCipher.java
 * @Version 1.0 2010.03.06
 * @Author Xie-Hua Sun  
 */

package process.algorithms;

import java.awt.image.ColorModel;
import process.algorithms.imageTrans.*;
import process.common.Common;

public class ImageCipher
{	
	//空域加密---------------------------------------
	  
	//n阶Arnold变换矩阵算法, pm = 1 为正变换, pm = -1 为反变换
	public int[] arnold_mtr(int[] pix, int iw, int key, int pm)
	{
		int u, v, p;
		int ku = 123 + key % 100;
		int kv = (int)(key / 10);			
						
		int rpix[] = new int[iw*iw];
		
		int k1 = (int)(key / 100);
    	int k2 = (int)((key % 100) / 10);
    	int k3 = key % 10;	
    	int num = k1 + k2 + k3;
    	        
		//计算Fibonacci系数f[2n-1],f[2n],f[2n+1],n=num
		int f = 1, f1 = 1, f2 = 1;
		for(int k = 0; k < 2*num-1; k++)
		{
			f1 = f2;
			f2 = f;
			f  = (f1 + f2) % iw;				
		}									
		//输出f1=f[2n-1],f2=f[2n],f=f[2n+1]
		
		//一步实现num次Arnold变换
		if(pm == 1)
		{			
			for(int j = 0; j < iw; j++)	
			{
				for(int i = 0; i < iw; i++)
				{
					u = (i*f1 + j*f2 + ku) % iw;       
	                v = (i*f2 + j*f  + kv) % iw;		                                                   
	                rpix[u+v*iw] = pix[i+j*iw]; //设置(u,v)处的ARGB值		                                               
				}					
			}
		}
		
		//一步实现num次Arnold反变换
		else if(pm == -1)
		{
			for(int j = 0; j < iw; j++)	
			{
				for(int i = 0; i < iw; i++)
				{						
					u = ((i-ku)*f - (j-kv)*f2) % iw;       
					while(u < 0) 
					    u = u + iw;
	                v = ((j-kv)*f1 - (i-ku)*f2) % iw;
	                while(v < 0) 
	                    v = v + iw;	          		                
	                rpix[u+v*iw] = pix[i+j*iw]; 	                                             
				}					
			}				
		}
		return rpix;				
	}    
        
    //Logistic混沌变换
    public int[] logistic(int[] pix, int iw, int ih, int key, int pm)
    {
    	int u, v;
    	double x = key / 1000.0;
    	int wh = iw * ih;
    	int[] flag = new int[wh];         //flag[i]=0表示i处未被占有
    	int[] rpix = new int[wh];
    	
    	//初始化，所有各处都未被占有
    	for (int i = 0; i < wh; i++)
            flag[i] = 0;
                    	                              
        //用Logistic方程迭代100次，产生混沌序列
        for (int i = 0; i < 100; i++)
            x = 4 * x * (1 - x);
        	    	
		for(int j = 0;j < ih; j++)	
		{
			for(int i = 0; i < iw; i++)
			{				
				u = 0; v = 0;
				
                //用Logistic方程产生整数混沌序列m (0 <= m <M)
                x = 4 * x * (1 - x);
                int m = (int)(wh * x);
                
                if (flag[m] == 0)
                {
                    u = m / iw;                    
                    v = m % iw;                    
                    flag[m] = 1;                 //(u,v)处被占有
                }
                else
                {
                    int flagForWhile = 1;        //While循环标志
                    while (flagForWhile == 1)
                    {
                        x = 4 * x * (1 - x);
                        m = (int)(wh * x);
                        if (flag[m] == 0)
                        {
                            u = m / iw;                            
                            v = m % iw;                            
                            flagForWhile = 0;    //While循环结束
                            flag[m] = 1;         //(u,v)处被占有
                        }
                    }
                }
                if(pm == 1)                      //正向Logistic变换
                    rpix[u+v*iw] = pix[i+j*iw];  //设置(u,v)处的ARGB值             
                else if(pm == -1)                //逆向Logistic变换
                    rpix[i+j*iw] = pix[u+v*iw];  //设置(i,j)处的ARGB值
            }            					
		}
		return rpix;		
    }      
    
    //Logistic混沌序列变换
    public int[] logisticXor(int[] pix, int iw, int ih, int key)
    {
    	int p, r, g, b;
    	double x = key / 1000.0;
    	int wh = iw * ih;
    	int[] chaos = new int[3];
    		                              
        //用Logistic方程迭代100次，产生混沌序列
        for (int i = 0; i < 100; i++)
            x = 4 * x * (1 - x);
        	    
	    ColorModel cm = ColorModel.getRGBdefault();		
		
		for(int i = 0; i < wh; i++)
		{			
			for(int k = 0; k < 3; k++) 
			    chaos[k] = 0;
			
			//产生8位2进制8位混沌序列chaos
			for(int k = 0; k < 3; k++)
			{				
				for (int w = 0; w < 8; w++)
                {
                    x = 4 * x * (1 - x);
                    if (x >= 0.5)
                        p = 1;
                    else
                        p = 0;
                    chaos[k] = ((chaos[k] << 1) | p);
                }
            }
            
            r = cm.getRed(pix[i])^chaos[0];
            g = cm.getGreen(pix[i])^chaos[1];
            b = cm.getBlue(pix[i])^chaos[2];            
            pix[i] = 255 << 24|r << 16|g << 8|b;
        }    					
		return pix;		
    }
    
    //Fibonacci序列变换
    public int[] fibonacciXor(int[] pix, int iw, int ih, int key)
    {
    	int wh = iw * ih;
    	int mod = iw*20;
    	int[] fib = new int[3];
	    int p, x, y, z, r, g, b;
	    
	    x = key %100; y=(int)(key/100)+7;//初始值，即密钥
                                          
	    ColorModel cm = ColorModel.getRGBdefault();		
			
		for(int i = 0; i < wh; i++)
		{
			for(int k = 0; k < 3; k++) 
			    fib[k] = 0;
			
			//产生Fibonacci序列fib[]
			for(int k = 0; k < 3; k++)
			{	
			    z = (x + y) % mod;                 //Fibonacciy变换
                x = y;
                y = z;
                fib[k] = z;					
            }
            r = cm.getRed(pix[i])^fib[0];
            g = cm.getGreen(pix[i])^fib[1];
            b = cm.getBlue(pix[i])^fib[2];
            pix[i] = 255 << 24|r << 16|g << 8|b;
        }   					
		return pix;		
    }
    
    //频域加密---------------------------------------
	
	//DCT加密
    public int[] dctCipher(int[] pixels, int iw, int ih, int size)
    {    	
	    double[][] inIm = new double[iw][ih];
	    DCT2 dct = new DCT2();
	    
	    //取出pixels[]中的blue分量组成2维数组inIm[][]		     		    
	    for(int j = 0; j < ih; j++)
	        for(int i = 0; i < iw; i++)	        	        
	            inIm[i][j] = pixels[i+j*iw] & 0xff;	        		            
	       
	    //DCT变换      
	    double[][] dctIm = dct.dctTrans(inIm, iw, ih, size, 1);
	    
        //设置密钥
        int key = (new Common()).getParam("输入密钥(111-999)","888");
        
        //DCT Arnold置乱
        dctIm = arnold2(dctIm, iw, key, 1);//dctImScr
                       
        //DCT逆变换
        double[][]outIm = dct.dctTrans(dctIm, iw, ih, size, -1); //dctImScr 
        pixels = outImage(outIm, iw, true);	    
		return pixels;		
	}	
			
    //Arnold变换(2维double数组)
    public double[][] arnold2(double[][] pix, int iw, int key, int pm)
    {
    	int u, v;
    	int ku = 173 + key % 100;
    	int kv = (int)(key / 10);
    	    			
		double rpix[][] = new double[iw][iw];
		
		for(int i = 0; i < iw; i++)	
		{
			for(int j = 0; j < iw; j++){
				u = (i + j + ku) % iw;             //2维Anold变换
                v = (i + 2 * j + kv) % iw;
                if(pm == 1)		                   //正向Arnold变换		                                       
                    rpix[u][v] = pix[i][j];        //设置(u,v)处的ARGB值
                else if(pm == -1)                  //逆向Arnold变换
                    rpix[i][j] = pix[u][v];                                
			}					
		}
		return rpix;		
    }    
    
    //DCT解密
    public int[] dctDecipher(int[] pixels, int iw, int ih, int size)
    { 			
       	double[][] inIm = new double[iw][ih];
       	
       	for(int j = 0; j < ih; j++)
       	    for(int i = 0; i < iw; i++)	        
	            inIm[i][j] = 4 * (pixels[i+j*iw]&0xff) - 512;	                              	            
	     
	    DCT2 dct = new DCT2();
	     	
    	//DCT变换        
	    double[][] dctIm = dct.dctTrans(inIm, iw, ih, size, 1);
	    
        //设置密钥
		int key = (new Common()).getParam("输入密钥(111-999)","888");           
        	    		          
        //DCT 反Arnold置乱
        dctIm = arnold2(dctIm, iw, key, -1);
	    
    	//反DCT变换
        double[][] outIm = dct.dctTrans(dctIm, iw, ih, size, -1);    					
		pixels = outImage(outIm, iw, false);	
		return pixels;	
	}	
	   
	public int[] whCipher(int[] pixels, int iw, int ih)
	{		    
	    double[] in     = new double[iw*ih];			    		     		    
	    for(int i = 0; i < ih*iw; i++)
	        in[i] = pixels[i] & 0xff;			        
	    		    
        //Walsh-Hadamard变换, 时域 => 频域
        double[] tW = (new WHT2(iw,ih)).WALSH(in, 16);			    
        			    
	    //设置密钥
		int key = (new Common()).getParam("输入密钥(111-999)","888");
			    		
        //频域, Logistic混沌置乱		    			    		    
	    double[] sW = logistic(tW, iw, ih, key, 1);
	    
	    //Walsh-Hadamard逆变换, 频域 => 时域
	    double[] oW = (new WHT2(iw,ih)).IWALSH(sW,16);			    
	    
	    //输出outpix[]
	    for(int i = 0; i < ih*iw; i++)
		{
			//因为-255<=oW[i]<=255
			int t = (int)((oW[i]+255)/2.0);
												
		    pixels[i] = 255 << 24|t << 16|t << 8|t;					
		}		
		return pixels;
	}
	
    //Walsh-Hadamard解密	
	public int[] whDecipher(int[] pixels, int iw, int ih)
	{
		double[] oW = new double[iw*ih];			
	    //取出pixels[]中的blue分量组成1维数组oW[]		     		    
	    for(int i = 0; i < iw*ih; i++)
	        oW[i] = 2*(pixels[i] & 0xff)-255;	
			
		//Walsh-Hadamard变换, 时域 => 频域
		double[] sW = (new WHT2(iw,ih)).WALSH(oW,16);
		
		//设置密钥
		int key = (new Common()).getParam("输入密钥(111-999)","888");
					              
	    //频域, 逆Logistic混沌置乱		    			    		    
	    double[] tW = logistic(sW, iw, ih, key, -1);
	    
	    //Walsh-Hadamard逆变换, 频域 => 时域
        oW = (new WHT2(iw,ih)).IWALSH(tW, 16);			    
        
		for(int i = 0; i < ih*iw; i++)
		{
			int t = (int)oW[i];
			 
			// 判断是否超过255
			if (t > 255)   t = 255;
			else if(t < 0) t = 0;

		    pixels[i] = 255 << 24|t << 16|t << 8|t;					
		}	
		return pixels;
	}	
	
	//Logistic混沌变换，pm = 1 表示正变换，pm = -1 为负变换
    public double[] logistic(double[] pix, int iw, int ih, int key, int pm)
    {
    	int u, v;
    	double x = key / 1000.0;
    	int wh = iw * ih;
    	int[] flag = new int[wh];         //flag[i]=0表示i处未被占有
    	double[] rpix = new double[wh];
    	
    	//初始化，所有各处都未被占有
    	for (int i = 0; i < wh; i++)
             flag[i] = 0;
                    	                              
        //用Logistic方程迭代100次，产生混沌序列
        for (int i = 0; i < 100; i++)
             x = 4 * x * (1 - x);
        	    		
		for(int i = 0; i < ih; i++)	
		{
			for(int j = 0;j < iw; j++)
			{				
				u = 0; v = 0;
				
                //用Logistic方程产生整数混沌序列m (0 <= m <M)
                x = 4 * x * (1 - x);
                int m = (int)(wh * x);
                
                if (flag[m] == 0)
                {
                    u = m / iw;                    
                    v = m % iw;                    
                    flag[m] = 1;                      //(u,v)处被占有
                }
                else
                {
                    int flagForWhile = 1;             //While循环标志
                    while (flagForWhile == 1)
                    {
                        x = 4 * x * (1 - x);
                        m = (int)(wh * x);
                        if (flag[m] == 0)
                        {
                            u = m / iw;                            
                            v = m % iw;                            
                            flagForWhile = 0;         //While循环结束
                            flag[m] = 1;              //(u,v)处被占有
                        }
                    }
                }
                if(pm == 1)                           //正向Logistic变换
                    rpix[u*iw+v] = pix[i*iw+j];       //设置(u,v)处的ARGB值             
                else if(pm == -1)                     //逆向Logistic变换
                    rpix[i*iw+j] = pix[u*iw+v];       //设置(i,j)处的ARGB值
            }            					
		}
		return rpix;		
    }
    
    double[] h = { 0.23037781330889,  0.71484657055291,
                   0.63088076792986, -0.02798376941686,
                  -0.18703481171909,  0.03084138183556,
                   0.03288301166689, -0.01059740178507 };

    double[] g = { 0.23037781330889, -0.71484657055291,		
                   0.63088076792986,  0.02798376941686,		
                  -0.18703481171909, -0.03084138183556,		
                   0.03288301166689,  0.01059740178507 }; 
                   
    //DWT加密
	public int[] dwtCipher(int[] pixels, int iw, int ih)
	{
		double[] wavIm = new double[iw*ih];
		DWT2 dwt = new DWT2(iw,ih);
				    
	    for(int i = 0; i < iw*ih; i++)	        
	        wavIm[i] = pixels[i]&0xff;			            
	    		    
	    //小波变换
	    wavIm = dwt.wavelet2D(wavIm, h, g, 1);
	    
	    //设置密钥
		int key = (new Common()).getParam("输入密钥(111-999)","888");
			    		
        //Logistic混沌置乱		    			    		    
	    wavIm = logistic(wavIm, iw, ih, key, 1);
	    			    
	    //逆小波变换
	    wavIm = dwt.iwavelet2D(wavIm, h, g, 1);		    	
    	
		pixels = outImage(wavIm, iw, true);
		return pixels;
	}
	
	//DWT解密
	public int[] dwtDecipher(int[] pixels, int iw, int ih)	        
	{	 	   
	    double[] wavIm = new double[iw*ih];
	    DWT2 dwt = new DWT2(iw,ih);
	    
	    for(int i = 0; i < iw*ih; i++)	        
	        wavIm[i] = 4 * (pixels[i]&0xff) - 512;		                              	            
	     
	    //小波变换
	    wavIm = dwt.wavelet2D(wavIm, h, g, 1);
	    
    	//设置密钥
		int key = (new Common()).getParam("输入密钥(111-999)","888");                   
			    		
    	//Logistic混沌置乱反变换
    	wavIm = logistic(wavIm, iw, ih, key, -1);
    	
		//逆小波变换
	    wavIm = dwt.iwavelet2D(wavIm, h, g, 1); 	    	
    	
		pixels = outImage(wavIm, iw, false);
		return pixels;
	}
	
	//输出ARGB图像序列
	public int[] outImage(double[][] im, int iw, boolean bl)
	{	
	    int ddim = iw*iw;
	    int[] npix = new int[ddim];
	    int[] pix = new int[ddim];
	    if(!bl)
	       	for(int i = 0; i < iw; i++)
		        for(int j = 0; j < iw; j++)
	                npix[i+j*iw] = (int)im[i][j];			            		            
	    else	    
		    for(int i = 0; i < iw; i++)
		        for(int j = 0; j < iw; j++)
	                npix[i+j*iw] = (int)((im[i][j] + 512.0) / 4.0); 			            		            
	    	    	
	    for(int i = 0; i < ddim; i++)
	    {
		    int x = npix[i];
		    
		    //下面一段纠正越界, 能减少失真
		    if(x > 255) x = 255;
		    else if(x < 0) x = 0; 
		     			    
		    pix[i] = (255 << 24)|(x << 16)|(x << 8)|x;				    
	    }		
		return pix;
	}
	
	//输出ARGB图像序列
	public int[] outImage(double[] im, int iw, boolean bl)
	{	
	    int ddim = iw*iw;
	    int[] npix = new int[ddim];
	    int[] pix = new int[ddim];
	    if(!bl)
	       	for (int i = 0; i < ddim; i++)
		        npix[i] = (int)im[i];			            		            
	    else	    
		    for (int i = 0; i < ddim; i++)
		        npix[i] = (int)((im[i] + 512.0) / 4.0); 			            		            
	    	    	
	    for(int i = 0; i < ddim; i++)
	    {
		    int x = npix[i];
		    
		    //下面一段纠正越界, 能减少失真
		    if(x > 255) x = 255;
		    else if(x < 0) x = 0; 
		     			    
		    pix[i] = (255 << 24)|(x << 16)|(x << 8)|x;				    
	    }		
		return pix;
	}
	
	//--------------------混沌演示-------------------
	
	public byte[][] chaos(int iw, int ih)
	{
	    byte[][] pix = new byte[iw][ih];
	    
	    for(int i = 0; i < iw; i++)
	    {
	        double A = 2.8 + 1.2 * i / iw;  //2.8<= A <=4.0
	        double X = 0.7;                 //初始值
	
	        int j;
	        for(int k = 0; k <= 120; k++){
	            X = A * X * (1 - X);
	          
	            //为显示其收敛性，选择k>100
	            if (k > 100)
	            {
	                j = (int)(300 - 250 * X);
	                if (j < 0) j = 0;
	                if (j > ih-1) j = ih-1;
	
	                pix[i][j] = 1;     
	            }
	        }
	    }
	    return pix;
	}
}
