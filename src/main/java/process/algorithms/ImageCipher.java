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
	//�������---------------------------------------
	  
	//n��Arnold�任�����㷨, pm = 1 Ϊ���任, pm = -1 Ϊ���任
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
    	        
		//����Fibonacciϵ��f[2n-1],f[2n],f[2n+1],n=num
		int f = 1, f1 = 1, f2 = 1;
		for(int k = 0; k < 2*num-1; k++)
		{
			f1 = f2;
			f2 = f;
			f  = (f1 + f2) % iw;				
		}									
		//���f1=f[2n-1],f2=f[2n],f=f[2n+1]
		
		//һ��ʵ��num��Arnold�任
		if(pm == 1)
		{			
			for(int j = 0; j < iw; j++)	
			{
				for(int i = 0; i < iw; i++)
				{
					u = (i*f1 + j*f2 + ku) % iw;       
	                v = (i*f2 + j*f  + kv) % iw;		                                                   
	                rpix[u+v*iw] = pix[i+j*iw]; //����(u,v)����ARGBֵ		                                               
				}					
			}
		}
		
		//һ��ʵ��num��Arnold���任
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
        
    //Logistic����任
    public int[] logistic(int[] pix, int iw, int ih, int key, int pm)
    {
    	int u, v;
    	double x = key / 1000.0;
    	int wh = iw * ih;
    	int[] flag = new int[wh];         //flag[i]=0��ʾi��δ��ռ��
    	int[] rpix = new int[wh];
    	
    	//��ʼ�������и�����δ��ռ��
    	for (int i = 0; i < wh; i++)
            flag[i] = 0;
                    	                              
        //��Logistic���̵���100�Σ�������������
        for (int i = 0; i < 100; i++)
            x = 4 * x * (1 - x);
        	    	
		for(int j = 0;j < ih; j++)	
		{
			for(int i = 0; i < iw; i++)
			{				
				u = 0; v = 0;
				
                //��Logistic���̲���������������m (0 <= m <M)
                x = 4 * x * (1 - x);
                int m = (int)(wh * x);
                
                if (flag[m] == 0)
                {
                    u = m / iw;                    
                    v = m % iw;                    
                    flag[m] = 1;                 //(u,v)����ռ��
                }
                else
                {
                    int flagForWhile = 1;        //Whileѭ����־
                    while (flagForWhile == 1)
                    {
                        x = 4 * x * (1 - x);
                        m = (int)(wh * x);
                        if (flag[m] == 0)
                        {
                            u = m / iw;                            
                            v = m % iw;                            
                            flagForWhile = 0;    //Whileѭ������
                            flag[m] = 1;         //(u,v)����ռ��
                        }
                    }
                }
                if(pm == 1)                      //����Logistic�任
                    rpix[u+v*iw] = pix[i+j*iw];  //����(u,v)����ARGBֵ             
                else if(pm == -1)                //����Logistic�任
                    rpix[i+j*iw] = pix[u+v*iw];  //����(i,j)����ARGBֵ
            }            					
		}
		return rpix;		
    }      
    
    //Logistic�������б任
    public int[] logisticXor(int[] pix, int iw, int ih, int key)
    {
    	int p, r, g, b;
    	double x = key / 1000.0;
    	int wh = iw * ih;
    	int[] chaos = new int[3];
    		                              
        //��Logistic���̵���100�Σ�������������
        for (int i = 0; i < 100; i++)
            x = 4 * x * (1 - x);
        	    
	    ColorModel cm = ColorModel.getRGBdefault();		
		
		for(int i = 0; i < wh; i++)
		{			
			for(int k = 0; k < 3; k++) 
			    chaos[k] = 0;
			
			//����8λ2����8λ��������chaos
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
    
    //Fibonacci���б任
    public int[] fibonacciXor(int[] pix, int iw, int ih, int key)
    {
    	int wh = iw * ih;
    	int mod = iw*20;
    	int[] fib = new int[3];
	    int p, x, y, z, r, g, b;
	    
	    x = key %100; y=(int)(key/100)+7;//��ʼֵ������Կ
                                          
	    ColorModel cm = ColorModel.getRGBdefault();		
			
		for(int i = 0; i < wh; i++)
		{
			for(int k = 0; k < 3; k++) 
			    fib[k] = 0;
			
			//����Fibonacci����fib[]
			for(int k = 0; k < 3; k++)
			{	
			    z = (x + y) % mod;                 //Fibonacciy�任
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
    
    //Ƶ�����---------------------------------------
	
	//DCT����
    public int[] dctCipher(int[] pixels, int iw, int ih, int size)
    {    	
	    double[][] inIm = new double[iw][ih];
	    DCT2 dct = new DCT2();
	    
	    //ȡ��pixels[]�е�blue�������2ά����inIm[][]		     		    
	    for(int j = 0; j < ih; j++)
	        for(int i = 0; i < iw; i++)	        	        
	            inIm[i][j] = pixels[i+j*iw] & 0xff;	        		            
	       
	    //DCT�任      
	    double[][] dctIm = dct.dctTrans(inIm, iw, ih, size, 1);
	    
        //������Կ
        int key = (new Common()).getParam("������Կ(111-999)","888");
        
        //DCT Arnold����
        dctIm = arnold2(dctIm, iw, key, 1);//dctImScr
                       
        //DCT��任
        double[][]outIm = dct.dctTrans(dctIm, iw, ih, size, -1); //dctImScr 
        pixels = outImage(outIm, iw, true);	    
		return pixels;		
	}	
			
    //Arnold�任(2άdouble����)
    public double[][] arnold2(double[][] pix, int iw, int key, int pm)
    {
    	int u, v;
    	int ku = 173 + key % 100;
    	int kv = (int)(key / 10);
    	    			
		double rpix[][] = new double[iw][iw];
		
		for(int i = 0; i < iw; i++)	
		{
			for(int j = 0; j < iw; j++){
				u = (i + j + ku) % iw;             //2άAnold�任
                v = (i + 2 * j + kv) % iw;
                if(pm == 1)		                   //����Arnold�任		                                       
                    rpix[u][v] = pix[i][j];        //����(u,v)����ARGBֵ
                else if(pm == -1)                  //����Arnold�任
                    rpix[i][j] = pix[u][v];                                
			}					
		}
		return rpix;		
    }    
    
    //DCT����
    public int[] dctDecipher(int[] pixels, int iw, int ih, int size)
    { 			
       	double[][] inIm = new double[iw][ih];
       	
       	for(int j = 0; j < ih; j++)
       	    for(int i = 0; i < iw; i++)	        
	            inIm[i][j] = 4 * (pixels[i+j*iw]&0xff) - 512;	                              	            
	     
	    DCT2 dct = new DCT2();
	     	
    	//DCT�任        
	    double[][] dctIm = dct.dctTrans(inIm, iw, ih, size, 1);
	    
        //������Կ
		int key = (new Common()).getParam("������Կ(111-999)","888");           
        	    		          
        //DCT ��Arnold����
        dctIm = arnold2(dctIm, iw, key, -1);
	    
    	//��DCT�任
        double[][] outIm = dct.dctTrans(dctIm, iw, ih, size, -1);    					
		pixels = outImage(outIm, iw, false);	
		return pixels;	
	}	
	   
	public int[] whCipher(int[] pixels, int iw, int ih)
	{		    
	    double[] in     = new double[iw*ih];			    		     		    
	    for(int i = 0; i < ih*iw; i++)
	        in[i] = pixels[i] & 0xff;			        
	    		    
        //Walsh-Hadamard�任, ʱ�� => Ƶ��
        double[] tW = (new WHT2(iw,ih)).WALSH(in, 16);			    
        			    
	    //������Կ
		int key = (new Common()).getParam("������Կ(111-999)","888");
			    		
        //Ƶ��, Logistic��������		    			    		    
	    double[] sW = logistic(tW, iw, ih, key, 1);
	    
	    //Walsh-Hadamard��任, Ƶ�� => ʱ��
	    double[] oW = (new WHT2(iw,ih)).IWALSH(sW,16);			    
	    
	    //���outpix[]
	    for(int i = 0; i < ih*iw; i++)
		{
			//��Ϊ-255<=oW[i]<=255
			int t = (int)((oW[i]+255)/2.0);
												
		    pixels[i] = 255 << 24|t << 16|t << 8|t;					
		}		
		return pixels;
	}
	
    //Walsh-Hadamard����	
	public int[] whDecipher(int[] pixels, int iw, int ih)
	{
		double[] oW = new double[iw*ih];			
	    //ȡ��pixels[]�е�blue�������1ά����oW[]		     		    
	    for(int i = 0; i < iw*ih; i++)
	        oW[i] = 2*(pixels[i] & 0xff)-255;	
			
		//Walsh-Hadamard�任, ʱ�� => Ƶ��
		double[] sW = (new WHT2(iw,ih)).WALSH(oW,16);
		
		//������Կ
		int key = (new Common()).getParam("������Կ(111-999)","888");
					              
	    //Ƶ��, ��Logistic��������		    			    		    
	    double[] tW = logistic(sW, iw, ih, key, -1);
	    
	    //Walsh-Hadamard��任, Ƶ�� => ʱ��
        oW = (new WHT2(iw,ih)).IWALSH(tW, 16);			    
        
		for(int i = 0; i < ih*iw; i++)
		{
			int t = (int)oW[i];
			 
			// �ж��Ƿ񳬹�255
			if (t > 255)   t = 255;
			else if(t < 0) t = 0;

		    pixels[i] = 255 << 24|t << 16|t << 8|t;					
		}	
		return pixels;
	}	
	
	//Logistic����任��pm = 1 ��ʾ���任��pm = -1 Ϊ���任
    public double[] logistic(double[] pix, int iw, int ih, int key, int pm)
    {
    	int u, v;
    	double x = key / 1000.0;
    	int wh = iw * ih;
    	int[] flag = new int[wh];         //flag[i]=0��ʾi��δ��ռ��
    	double[] rpix = new double[wh];
    	
    	//��ʼ�������и�����δ��ռ��
    	for (int i = 0; i < wh; i++)
             flag[i] = 0;
                    	                              
        //��Logistic���̵���100�Σ�������������
        for (int i = 0; i < 100; i++)
             x = 4 * x * (1 - x);
        	    		
		for(int i = 0; i < ih; i++)	
		{
			for(int j = 0;j < iw; j++)
			{				
				u = 0; v = 0;
				
                //��Logistic���̲���������������m (0 <= m <M)
                x = 4 * x * (1 - x);
                int m = (int)(wh * x);
                
                if (flag[m] == 0)
                {
                    u = m / iw;                    
                    v = m % iw;                    
                    flag[m] = 1;                      //(u,v)����ռ��
                }
                else
                {
                    int flagForWhile = 1;             //Whileѭ����־
                    while (flagForWhile == 1)
                    {
                        x = 4 * x * (1 - x);
                        m = (int)(wh * x);
                        if (flag[m] == 0)
                        {
                            u = m / iw;                            
                            v = m % iw;                            
                            flagForWhile = 0;         //Whileѭ������
                            flag[m] = 1;              //(u,v)����ռ��
                        }
                    }
                }
                if(pm == 1)                           //����Logistic�任
                    rpix[u*iw+v] = pix[i*iw+j];       //����(u,v)����ARGBֵ             
                else if(pm == -1)                     //����Logistic�任
                    rpix[i*iw+j] = pix[u*iw+v];       //����(i,j)����ARGBֵ
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
                   
    //DWT����
	public int[] dwtCipher(int[] pixels, int iw, int ih)
	{
		double[] wavIm = new double[iw*ih];
		DWT2 dwt = new DWT2(iw,ih);
				    
	    for(int i = 0; i < iw*ih; i++)	        
	        wavIm[i] = pixels[i]&0xff;			            
	    		    
	    //С���任
	    wavIm = dwt.wavelet2D(wavIm, h, g, 1);
	    
	    //������Կ
		int key = (new Common()).getParam("������Կ(111-999)","888");
			    		
        //Logistic��������		    			    		    
	    wavIm = logistic(wavIm, iw, ih, key, 1);
	    			    
	    //��С���任
	    wavIm = dwt.iwavelet2D(wavIm, h, g, 1);		    	
    	
		pixels = outImage(wavIm, iw, true);
		return pixels;
	}
	
	//DWT����
	public int[] dwtDecipher(int[] pixels, int iw, int ih)	        
	{	 	   
	    double[] wavIm = new double[iw*ih];
	    DWT2 dwt = new DWT2(iw,ih);
	    
	    for(int i = 0; i < iw*ih; i++)	        
	        wavIm[i] = 4 * (pixels[i]&0xff) - 512;		                              	            
	     
	    //С���任
	    wavIm = dwt.wavelet2D(wavIm, h, g, 1);
	    
    	//������Կ
		int key = (new Common()).getParam("������Կ(111-999)","888");                   
			    		
    	//Logistic�������ҷ��任
    	wavIm = logistic(wavIm, iw, ih, key, -1);
    	
		//��С���任
	    wavIm = dwt.iwavelet2D(wavIm, h, g, 1); 	    	
    	
		pixels = outImage(wavIm, iw, false);
		return pixels;
	}
	
	//���ARGBͼ������
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
		    
		    //����һ�ξ���Խ��, �ܼ���ʧ��
		    if(x > 255) x = 255;
		    else if(x < 0) x = 0; 
		     			    
		    pix[i] = (255 << 24)|(x << 16)|(x << 8)|x;				    
	    }		
		return pix;
	}
	
	//���ARGBͼ������
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
		    
		    //����һ�ξ���Խ��, �ܼ���ʧ��
		    if(x > 255) x = 255;
		    else if(x < 0) x = 0; 
		     			    
		    pix[i] = (255 << 24)|(x << 16)|(x << 8)|x;				    
	    }		
		return pix;
	}
	
	//--------------------������ʾ-------------------
	
	public byte[][] chaos(int iw, int ih)
	{
	    byte[][] pix = new byte[iw][ih];
	    
	    for(int i = 0; i < iw; i++)
	    {
	        double A = 2.8 + 1.2 * i / iw;  //2.8<= A <=4.0
	        double X = 0.7;                 //��ʼֵ
	
	        int j;
	        for(int k = 0; k <= 120; k++){
	            X = A * X * (1 - X);
	          
	            //Ϊ��ʾ�������ԣ�ѡ��k>100
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
