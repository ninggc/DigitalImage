/**
 * @ImageEnhance.java
 * @Version 1.0 2009.02.17
 * @Author Xie-Hua Sun * 
 */

package process.algorithms;

import java.awt.image.ColorModel;
import process.algorithms.imageTrans.FFT2;
import process.algorithms.imageTrans.Complex;

public class ImageEnhance
{
	//对比度扩展======================================
            
    //计算灰度映射表
    public int[] pixelsMap(int x1,int y1,int x2,int y2)
    {
    	int[] pMap = new int[256];    //映射表
    	
    	if(x1 > 0)
    	{    	
    	   	double k1 = y1/x1;        //第1段钭率k1
	    	
	    	//按第1段钭率k1线性变换
	    	for(int i = 0; i <= x1; i++)
	    	    pMap[i] = (int)(k1*i);		
	    }
    	
    	double k2 = (y2-y1)/(x2-x1);  //第2段钭率k2
    	
    	//按第2段钭率k2线性变换
    	for(int i = x1+1; i <= x2; i++)
    		if(x2 != x1)
    		    pMap[i] = y1 + (int)(k2*(i-x1));
    		else
    		    pMap[i] = y1;    		    
    	    	
    	if(x2<255)
    	{    	
	    	double k3 = (255-y2)/(255-x2);//第2段钭率k2
	    	
	    	//按第3段钭率k3线性变换
	    	for(int i = x2+1; i< 256; i++)
	    		pMap[i] = y2 + (int)(k3*(i-x2)); 		    		    
	    }
    	return pMap;
    }
    
    //对比度扩展
    public int[] stretch(int[] pix, int[] map, int iw, int ih)
    {		
		ColorModel cm = ColorModel.getRGBdefault();
		int r, g, b;
		for(int i = 0; i < iw*ih; i++) 
		{
			r = map[cm.getRed(pix[i])];
			g = map[cm.getGreen(pix[i])];
			b = map[cm.getBlue(pix[i])];
			
			if(r >= 255) r = 255;
			if(r < 0)    r = 0;
			if(g >= 255) g = 255;
			if(g < 0)    g = 0;
			if(b >= 255) b = 255;
			if(b < 0)    b = 0;
			
			pix[i] = 255 << 24|r << 16|g << 8|b;
		}
		return pix;
	}    
    
	//直方图均匀化==================================
	
	public int[] histequal(int[] pix, int[] hist, int iw, int ih)
	{	
		double c  = (double)255/(iw*ih);
		double[] sum  = new double[256];
		int[]    outg = new int[256];
		int g,   area = 256*256;
		
		sum[0] = hist[0];
		for(int i = 1; i < 256; i++) 
			sum[i] = sum[i-1] + hist[i];
		
		//灰度变换:i-->outg[i]	
		for(int i = 0; i < 255; i++)
			outg[i]  = (int)(c*sum[i]);
			
		for(int i = 0; i < iw*ih; i++)
		{
			g = pix[i]&0xff;
			pix[i] = 255 << 24|outg[g] <<16|outg[g] << 8|outg[g];			
		}
		return pix;
    }
     
    //滤波=========================================
    
    //3X3阈值滤波
    public int[] threshold(int[] pix, int iw, int ih)
    {
    	int[] opix = new int[iw*ih];
    	int avr,          //灰度平均 
            sum,          //灰度和
            num = 0,      //计数器
            nT  = 4,      //计数器阈值
            T   = 10;     //阈值
        int pij, pkl,     //(i,j),(i+k,j+l)处灰度值
            err;          //误差
            
        for (int j = 1; j < ih - 1; j++)
        {
            for (int i = 1; i < iw - 1; i++)
            {
                //3X3块像素和
                opix[i+j*iw] = pix[i+j*iw];
                sum = 0;
                for (int k = -1; k < 2; k++)
                {
                    for (int l = -1; l < 2; l++)
                    {
                        if ((k != 0) || (l != 0))
                        {
                            pkl = pix[i+k+(j+l)*iw]&0xff; 
                            pij = pix[i+j*iw]&0xff;
                            err = Math.abs(pkl - pij);
                            sum = sum + pkl;
                            if (err > T) num++;
                        }
                    }
                }
                int a = (int)(sum / 8.0f);         //均值
                if (num > nT)
                   opix[i + j * iw] = 255 << 24|a << 16|a << 8|a;                                       
            }
        }
        return opix;
    }
    
    //3X3均值滤波
    public int[] average(int[] pix, int iw, int ih)
    {
        int[] opix = new int[iw*ih];
    	int a, pkl, sum;          
            
        for (int j = 1; j < ih - 1; j++)
        {
            for (int i = 1; i < iw - 1; i++)
            {                
                opix[i+j*iw] = pix[i+j*iw];
                sum = 0;
                //3X3块像素和
                for (int k = -1; k < 2; k++)
                    for (int l = -1; l < 2; l++){
                        pkl = pix[i+k+(j+l)*iw]&0xff;
                        sum = sum + pkl;
                    }       
                a = (int)(sum / 9.0f);         //均值                
                opix[i + j * iw] = 255 << 24|a << 16|a << 8|a;                                       
            }
        }
        return opix;
    }
    
    //中值滤波===================================
    
    /**************************************************
     * type -- 0: 3X3窗口
     *         1: 1X5窗口
     *         2: 5X1窗口
     *      -- 3: 5X5窗口
     **************************************************/
    public int[] median(int[] pix, int iw, int ih, int type)
    {
        int[] opix = new int[iw*ih];
        
        for (int j = 2; j < ih - 2; j++)
        {
            int[] dt;
            for (int i = 2; i < iw - 2; i++)
            {
            	opix[i+j*iw] = pix[i+j*iw];
                int m = 0, r = 0;
                                    
                if (type == 0)
                {
                    dt = new int[9];

                    //3X3窗口
                    for (int k = -1; k < 2; k++)
                    {
                        for (int l = -1; l < 2; l++)
                        {
                            dt[m] = pix[i+k+(j+l)*iw]&0xff;
                            m++;
                        }
                    }
                    r = median_sorter(dt, 9); //中值                        
                }
                else if (type == 1)
                {
                    dt = new int[5];

                    //1X5窗口
                    dt[0] = pix[i+(j-2)*iw]&0xff;
                    dt[1] = pix[i+(j-1)*iw]&0xff;
                    dt[2] = pix[i+j*iw]&0xff;
                    dt[3] = pix[i+(j+1)*iw]&0xff;
                    dt[4] = pix[i+(j+2)*iw]&0xff;				
                    r = median_sorter(dt, 5);   //中值                        
                }
                else if (type == 2)
                {
                    dt = new int[5];

                    //5X1窗口
                    dt[0] = pix[i-2+j*iw]&0xff;
                    dt[1] = pix[i-1+j*iw]&0xff;
                    dt[2] = pix[i+j*iw]&0xff;
                    dt[3] = pix[i+1+j*iw]&0xff;
                    dt[4] = pix[i+2+j*iw]&0xff;		
                    r = median_sorter(dt, 5);  //中值                      
                }
                else if (type == 3)
                {
                    dt = new int[25];

                    //5X5窗口
                    for (int k = -2; k < 3; k++)
                    {
                        for (int l = -2; l < 3; l++)
                        {
                            dt[m] = pix[i+k+(j+l)*iw]&0xff;
                            m++;
                        }
                    }
                    r = median_sorter(dt, 25); //中值                        
                }
                opix[i + j * iw] = 255 << 24|r << 16|r << 8|r;                 
            }
        }
        return opix;
    }

    //冒泡排序,输出中值
    public int median_sorter(int[] dt, int m)
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
        return dt[(int)(m / 2)];
    }
    
    //模板滤波=====================================
          
    //3X3低通滤波方法
	public int[] lowpass(int[] pix, int iw, int ih, int n)
	{		
		int[] opix = new int[iw*ih];
		
		//定义扩展输入图像矩阵
		int[][] ex_inpix = exinpix(pix, iw, ih);
		
		int r = 0, sum;
		
		//低通模板		
		int[][] h = low_matrix(n);
		
		//低通滤波
		for(int j = 1; j < ih+1; j++)	
		{
			for(int i = 1; i < iw+1; i++)	
			{
				//求3X3窗口9个像素加权和
			    sum = 0;
				for(int k =- 1; k <= 1; k++)
					for(int l =- 1; l <= 1; l++)					
						sum = sum + h[k+1][l+1]*ex_inpix[i+k][j+l];							
			
				if(n == 0)
				    r = (int)(sum/9);       //h1平均值
				else if(n == 1)
				    r = (int)(sum/10);      //h2平均值
				else if(n == 2)
				    r = (int)(sum/16);      //h3平均值   
																			
				opix[(i-1)+(j-1)*iw] = 255 << 24|r << 16|r << 8|r;
			}
		}
		return opix;	
	}
	
	//3X3高通滤波方法
	public int[] highpass(int[] pix, int iw, int ih, int n)
	{		
		int[] opix = new int[iw*ih];
		
		//定义扩展输入图像矩阵
		int[][] ex_inpix = exinpix(pix, iw, ih);
		
		//高通模板				
        int[][] H = high_matrix(n);
                
		//高通滤波
		for(int j = 1; j < ih+1; j++)	
		{
			for(int i = 1; i < iw+1; i++)	
			{
				int r = 0, sum = 0 ;								
				
			    //求3X3窗口9个像素加权和
				for(int k =- 1; k <= 1; k++)
					for(int l =- 1; l <= 1; l++)					
						sum = sum + H[k+1][l+1]*ex_inpix[i+k][j+l];							
				
				if(n == 4)
				    r = (int)(sum/7);      //H4平均值
				else if(n == 5)
				    r = (int)(sum/2);      //H5
				else
				    r = sum;               //H1~H3   
				if(r > 255)     r = 255;
				else if( r < 0) r = 0;												
				opix[(i-1)+(j-1)*iw] = 255 << 24|r << 16|r << 8|r;
			}
		}
		return opix;	
	}
		
	//定义扩展输入图像矩阵
    public int[][] exinpix(int[] pix, int iw, int ih)
    {   
        int[][] ex_inpix = new int[iw+2][ih+2];
        
        //获取非边界灰度值
		for(int j = 0; j < ih; j++)	
			for(int i = 0; i < iw; i++)				
				ex_inpix[i+1][j+1] = pix[i+j*iw]&0xff;
			
		//四角点处理
		ex_inpix[0][0] = ex_inpix[1][1];
		ex_inpix[0][ih+1] = ex_inpix[1][ih];
		ex_inpix[iw+1][0] = ex_inpix[iw][1];
		ex_inpix[iw+1][ih+1] = ex_inpix[iw][ih];
		
		//上下边界处理
		for(int j = 1; j < ih + 1; j++){
			ex_inpix[0][j]    = ex_inpix[1][j]; //上边界 
			ex_inpix[iw+1][j] = ex_inpix[iw][j];//下边界
		}
		  
		//左右边界处理
		for(int i = 1; i < iw + 1; i++){
			ex_inpix[i][0]    = ex_inpix[i][1]; //左边界 
			ex_inpix[i][ih+1] = ex_inpix[i][ih];//右边界
		}
		return ex_inpix;
	}
	
	//低通滤波模板
	public int[][] low_matrix(int n)
	{
		int[][] h = new int[3][3];
	    if(n == 0)//h1
	    {
	    	h[0][0] = 1; h[0][1] = 1; h[0][2] = 1;
	    	h[1][0] = 1; h[1][1] = 1; h[1][2] = 1;
	    	h[2][0] = 1; h[2][1] = 1; h[2][2] = 1;
	    }
	    else if(n == 1)//h2
	    {
	    	h[0][0] = 1; h[0][1] = 1; h[0][2] = 1;
	    	h[1][0] = 1; h[1][1] = 2; h[1][2] = 1;
	    	h[2][0] = 1; h[2][1] = 1; h[2][2] = 1;
	    }	
	    else if(n == 2)//h3
	    {
	    	h[0][0] = 1; h[0][1] = 2; h[0][2] = 1;
	    	h[1][0] = 2; h[1][1] = 4; h[1][2] = 2;
	    	h[2][0] = 1; h[2][1] = 2; h[2][2] = 1;
	    }
	    return h;
	}
	
	//高通滤波模板 
	public int[][] high_matrix(int n)
	{
		int[][] H = new int[3][3];
	    if(n == 0)//H1
	    {
	    	H[0][0] =  0; H[0][1] = -1; H[0][2] =  0;
	    	H[1][0] = -1; H[1][1] =  5; H[1][2] = -1;
	    	H[2][0] =  0; H[2][1] = -1; H[2][2] =  0;
	    }
	    else if(n == 1)//H2
	    {
	    	H[0][0] = -1; H[0][1] = -1; H[0][2] = -1;
	    	H[1][0] = -1; H[1][1] =  9; H[1][2] = -1;
	    	H[2][0] = -1; H[2][1] = -1; H[2][2] = -1;
	    }	
	    else if(n == 2)//H3
	    {
	    	H[0][0] = 1;  H[0][1] = -2; H[0][2] = 1;
	    	H[1][0] = -2; H[1][1] =  5; H[1][2] = -2;
	    	H[2][0] = 1;  H[2][1] = -2; H[2][2] = 1;
	    }
	    else if(n == 3)//H4
	    {
	    	H[0][0] = -1; H[0][1] = -2; H[0][2] = -1;
	    	H[1][0] = -2; H[1][1] = 19; H[1][2] = -2;
	    	H[2][0] = -1; H[2][1] = -2; H[2][2] = -1;
	    }
	    else if(n == 4)//H5
	    {
	    	H[0][0] = -2; H[0][1] = 1; H[0][2] = -2;
	    	H[1][0] =  1; H[1][1] = 6; H[1][2] =  1;
	    	H[2][0] = -2; H[2][1] = 1; H[2][2] = -2;
	    }
	    return H;
	}
	
	//频域滤波=====================================
	
	/***********************************************
	 * Butterworth和指数低/高通滤波
	 * type: 0--巴氏低通, 1--巴氏高通
	 *       2--指数低通, 3--指数高通
	 ***********************************************/
	public int[] BEfilter(int[] pix, int iw, int ih, int type, int d0)           
	{                                       
        //int d0 = 150;//getParam();
        double[] opix = new double [iw*ih];
				
		//初始化
		for(int j = 0; j < ih; j++)
			for(int i = 0;i < iw; i++)
				opix[i+j*iw] = pix[i+j*iw]&0xff;
						
		//初始化
		FFT2 fft2;
		Complex[] complex = new Complex[iw*ih];
				
		for(int i = 0;i < iw*ih; i++)
			complex[i] = new Complex(0,0);
			    
		//对原图像进行FFT
		fft2 = new FFT2();
		fft2.setData2(iw, ih, opix);
		complex = fft2.getFFT2();
		
		Complex Comp = new Complex();
		Complex Bc = new Complex(0,0);
		
		//滤波
		for(int j = 0; j < ih; j++)
		{
			for(int i = 0; i < iw; i++)
			{
				double dd = i*i+j*j;
				if(type == 0)                             //Butterworth低通
				    Bc = new Complex(1/(1+0.414213562*dd/(d0*d0)),0); 
				else if(type == 1)                        //Butterworth高通
				    Bc = new Complex(1/(1+0.414213562*d0*d0/dd),0);
				else if(type == 2)                        //指数低通II型
				    Bc = new Complex(Math.exp(-0.5*Math.log(2)*dd/(d0*d0)),0); 
				else if(type == 3)                        //指数高通II型
				    Bc = new Complex(Math.exp(-0.5*Math.log(2)*d0*d0/dd),0);    
				complex[i+j*iw] = Comp.Mul(complex[i+j*iw], Bc);								
			}
		}
		
		//进行FFT反变换
		fft2 = new FFT2();
		fft2.setData2i(iw, ih, complex);
		pix = fft2.getPixels2i();		
		return pix;			
	}
	
	//锐化--------------------------------------------------
	
	/*******************************************************
     * pix   --待检测图像数组
     * iw, ih--待检测图像宽高
     * num   --算子代号.1:Kirsch算子;2:Laplace算子;
     *                  3:Prewitt算子;5:Sobel算子
     * thresh--边缘检测阈值T
     * flag  --锐化与边缘检测标志, false:锐化; true:边缘检测
     *******************************************************/
    public int[] detect(int[] px, int iw, int ih, int num, 
                        int thresh, boolean flag)
    {
		int i, j, r, g, b;
	    int[][] inr   = new int[iw][ih];//红色分量矩阵
	    int[][] ing   = new int[iw][ih];//绿色分量矩阵
	    int[][] inb   = new int[iw][ih];//蓝色分量矩阵
	    int[][] gray  = new int[iw][ih];//灰度图像矩阵
	    
	    ColorModel cm = ColorModel.getRGBdefault();
	    
	    for(j = 0; j < ih; j++)
	    {	    
	        for(i = 0; i < iw; i++)
	        {
	        	inr[i][j] = cm.getRed(px[i+j*iw]);
	            ing[i][j] = cm.getGreen(px[i+j*iw]);
	            inb[i][j] = cm.getBlue(px[i+j*iw]);
	            
	            //转变为灰度图像矩阵
	            gray[i][j] = (int)((inr[i][j]+ing[i][j]+inb[i][j])/3.0);
	        }
	    }	           
	    
	    if(num == 1)//Kirsch
	    {
	    	byte[][][] kir ={{{ 5, 5, 5},
	                          {-3, 0,-3},
	                          {-3,-3,-3}},//kir0
	                         
		                     {{-3, 5, 5},
	                          {-3, 0, 5},
	                          {-3,-3,-3}},//kir1
	                         
	                         {{-3,-3, 5},
	                          {-3, 0, 5},
	                          {-3,-3, 5}},//kir2
	                         
		                     {{-3,-3,-3},
	                          {-3, 0, 5},
	                          {-3, 5, 5}},//kir3
	                         
	                         {{-3,-3,-3},
	                          {-3, 0,-3},
	                          { 5, 5, 5}},//kir4
	                         
		                     {{-3,-3,-3},
	                          { 5, 0,-3},
	                          { 5, 5,-3}},//kir5
	                                       
	                         {{ 5,-3,-3},
	                          { 5, 0,-3},
	                          { 5,-3,-3}},//kir6
	                         
		                     {{ 5, 5,-3},
	                          { 5, 0,-3},
	                          {-3,-3,-3}}};//kir7	        
	        
	        if(flag)//边缘检测
	        {
	        	int[][][] edge = new int[8][iw][ih];
	        	for(i = 0; i < 8; i++)
		            edge[i] = edge(gray, kir[i], iw, ih);
		        for(int k = 1; k < 8; k++)
					for(j = 0; j < ih; j++)
				        for(i = 0; i < iw; i++)
				           	if(edge[0][i][j] < edge[k][i][j])
				        	    edge[0][i][j] = edge[k][i][j];
				        	    	
				for(j = 0; j < ih; j++)
			    {
			        for(i = 0; i < iw; i++)
			        {		       			        	
		        		if(edge[0][i][j] > thresh) r = 0;
		        		else r = 255;
		        		px[i+j*iw] = (255<<24)|(r<<16)|(r<<8)|r;			        	
			        }
			    }		        
	        }
	        else   //锐化
	        {
	        	int[][][] edger = new int[8][iw][ih];
	            int[][][] edgeg = new int[8][iw][ih];
	            int[][][] edgeb = new int[8][iw][ih];
	            	        
		        for(i = 0; i < 8; i++)
		        {	                    
		            edger[i] = edge(inr, kir[i], iw, ih);
		            edgeg[i] = edge(ing, kir[i], iw, ih);
		            edgeb[i] = edge(inb, kir[i], iw, ih);
		        }
		        
		        for(int k = 1; k < 8; k++)
				{			
					for(j = 0; j < ih; j++)
				    {
				        for(i = 0; i < iw; i++)
				        {
				        	if(edger[0][i][j] < edger[k][i][j])
				        	   edger[0][i][j] = edger[k][i][j];	
				        	if(edgeg[0][i][j] < edgeg[k][i][j])
				        	   edgeg[0][i][j] = edgeg[k][i][j];		        	
				        	if(edgeb[0][i][j] < edgeb[k][i][j])
				        	   edgeb[0][i][j] = edgeb[k][i][j];	
				        }
				    } 
			    }
			    
			    for(j = 0; j < ih; j++)
			    {
			        for(i = 0; i < iw; i++)
			        {
			           	r = edger[0][i][j];
			        	g = edgeg[0][i][j];
			        	b = edgeb[0][i][j];			        			        	
			        	px[i+j*iw] = (255<<24)|(r<<16)|(g<<8)|b;			        	
			        }
			    }
		    } 		     
	    }
	    else if(num == 2)                       //Laplace
	    {
		    byte[][] lap1 = {{ 1, 1, 1},
	                         { 1,-8, 1},
	                         { 1, 1, 1}};
		    /*byte[][] lap2 = {{ 0, 1, 0},
	                           { 1,-4, 1},
	                           { 0, 1, 0}}; */
	        if(flag)//边缘检测
	        {
	        	int[][] edge = edge(gray, lap1, iw, ih);
			    
				for(j = 0; j < ih; j++)
			    {
			        for(i = 0; i < iw; i++)
			        {			        	
			        	if(edge[i][j] > thresh) r = 0;//黑色，边缘点
			        	else r = 255;
			        	px[i+j*iw] = (255<<24)|(r<<16)|(r<<8)|r;
			        	
			        }
			    }
	        }
	        else
	        {	                           
			    int[][] edger = edge(inr, lap1, iw, ih);
			    int[][] edgeg = edge(ing, lap1, iw, ih);
			    int[][] edgeb = edge(inb, lap1, iw, ih);
				
				for(j = 0; j < ih; j++)
			    {
			        for(i = 0; i < iw; i++)
			        {
			           	r = edger[i][j];
			        	g = edgeg[i][j];
			        	b = edgeb[i][j];
			        	px[i+j*iw] = (255<<24)|(r<<16)|(g<<8)|b;			        	
			        }
			    }
		    }
	    }
	    else if(num == 20)                       //LOG
	    {
		    byte[][] log =  { {-2,-4,-4,-4,-2},  //LOG模板
			                  {-4, 0, 8, 0,-4},
			                  {-4, 8,24, 8,-4},
			                  {-4, 0, 8, 0,-4},
			                  {-2,-4,-4,-4,-2} };
		         
		    int[][] ledge = LOGedge(gray, log, iw, ih);
		    
		    for(j = 2; j < ih-2; j++)
		    {
		        for(i = 2; i < iw-2; i++)
		        {
		        	int t0 = ledge[i-1][j];
		        	int t1 = ledge[i+1][j];
		        	int t2 = ledge[i][j-1];
		        	int t3 = ledge[i][j+1];
		        			        	
		        	if(flag)
		        	{
		        		if((t0*t1 < 0 || t2*t3<0)) r = 0;//黑色，边缘点
		        		else r = 255;
		        		px[i+j*iw] = (255<<24)|(r<<16)|(r<<8)|r;
		        	}
		        
		        }
		    }
	    }
	    else if(num == 21)                       //修正LOG
	    {
		    byte[][] log =  { {-2,-4,-4,-4,-2},  //LOG模板
			                  {-4, 0, 8, 0,-4},
			                  {-4, 8,24, 8,-4},
			                  {-4, 0, 8, 0,-4},
			                  {-2,-4,-4,-4,-2} };
		         
		    int[][] ledge = LOGedge(gray, log, iw, ih);
		    
		    byte[][] sobelx = {{ 1, 0,-1},       //Sobel模板
	                           { 2, 0,-2},
	                           { 1, 0,-1}};
		    
		    byte[][] sobely = {{ 1, 2, 1},       //Sobel模板
	                           { 0, 0, 0},
	                           {-1,-2,-1}};
		    
			int[][] sedgex = edge(gray, sobelx, iw, ih);
			int[][] sedgey = edge(gray, sobely, iw, ih);
			
			for(j = 2; j < ih-2; j++)
		    {
		        for(i = 2; i < iw-2; i++)
		        {
		        	int t0 = ledge[i-1][j];
		        	int t1 = ledge[i+1][j];
		        	int t2 = ledge[i][j-1];
		        	int t3 = ledge[i][j+1];
		        	
		        	int sx = sedgex[i][j];
		        	int sy = sedgey[i][j];
		        	
		        	if(flag)
		        	{
		        		if((t0*t1 < 0 || t2*t3<0)&&(sx>80||sy>80)) r = 0;
		        		else r = 255;
		        		px[i+j*iw] = (255<<24)|(r<<16)|(r<<8)|r;
		        	}
		        
		        }
		    }
	    }
	    else if(num == 3)//Prewitt
	    {
	    	//Prewitt算子D_x模板
		    byte[][] pre1 = {{ 1, 0,-1},
	                         { 1, 0,-1},
	                         { 1, 0,-1}}; 
		    
	    	//Prewitt算子D_y模板
		    byte[][] pre2 = {{ 1, 1, 1},
	                         { 0, 0, 0},
	                         {-1,-1,-1}};
		    if(flag)//边缘检测
	        {
	        	int[][] edge1 = edge(gray, pre1, iw, ih);
			    int[][] edge2 = edge(gray, pre2, iw, ih);
				for(j = 0; j < ih; j++)
			    {
			        for(i = 0; i < iw; i++)
			        {
			        	if(Math.max(edge1[i][j],edge2[i][j]) > thresh) r = 0;
			        	else r = 255;
			        	px[i+j*iw] = (255<<24)|(r<<16)|(r<<8)|r;
			        	
			        }
			    }
	        }
	        else
	        {	   
			    int[][] edger1 = edge(inr, pre1, iw, ih);
				int[][] edger2 = edge(inr, pre2, iw, ih);
				int[][] edgeg1 = edge(ing, pre1, iw, ih);
				int[][] edgeg2 = edge(ing, pre2, iw, ih);
				int[][] edgeb1 = edge(inb, pre1, iw, ih);
				int[][] edgeb2 = edge(inb, pre2, iw, ih);
				
				for(j = 0; j < ih; j++)
				{
				    for(i = 0; i < iw; i++)
			        {			        
			        	r = Math.max(edger1[i][j],edger2[i][j]);
			        	g = Math.max(edgeg1[i][j],edgeg2[i][j]); 
			        	b = Math.max(edgeb1[i][j],edgeb2[i][j]); 
			        	px[i+j*iw] = (255<<24)|(r<<16)|(g<<8)|b;			
			        }
			    }
		    }
	    }
	    else if(num == 5)//Sobel
	    {
	    	byte[][] sob1 = {{ 1, 0,-1},
	                         { 2, 0,-2},
	                         { 1, 0,-1}};
		    
		    byte[][] sob2 = {{ 1, 2, 1},
	                         { 0, 0, 0},
	                         {-1,-2,-1}};		     
		    if(flag)//边缘检测
	        {
	        	int[][] edge1 = edge(gray, sob1, iw, ih);
			    int[][] edge2 = edge(gray, sob2, iw, ih);
				for(j = 0; j < ih; j++)
			    {
			        for(i = 0; i < iw; i++)
			        {
			        	if(Math.max(edge1[i][j],edge2[i][j]) > thresh) r = 0;
			        	else r = 255;
			        	px[i+j*iw] = (255<<24)|(r<<16)|(r<<8)|r;			        	
			        }
			    }
	        }
	        else
	        {	
			    int[][] edger1 = edge(inr, sob1, iw, ih);
				int[][] edger2 = edge(inr, sob2, iw, ih);
				int[][] edgeg1 = edge(ing, sob1, iw, ih);
				int[][] edgeg2 = edge(ing, sob2, iw, ih);
				int[][] edgeb1 = edge(inb, sob1, iw, ih);
				int[][] edgeb2 = edge(inb, sob2, iw, ih);
				
				for(j = 0; j < ih; j++)
				{
				    for(i = 0; i < iw; i++)
			        {			        	
			        	r = Math.max(edger1[i][j],edger2[i][j]);
			        	g = Math.max(edgeg1[i][j],edgeg2[i][j]); 
			        	b = Math.max(edgeb1[i][j],edgeb2[i][j]); 
			        	px[i+j*iw] = (255<<24)|(r<<16)|(g<<8)|b;			            
			        }
			    }
		    }
	    }	    
	    return px;
	}
	
	public int[][] edge(int[][] in, byte[][] tmp, int iw, int ih) 
	{
		int[][] ed = new int[iw][ih];
		
		for(int j = 1; j < ih-1; j++)
		{		
			for(int i = 1; i < iw-1; i++)
			{
				ed[i][j] = Math.abs(tmp[0][0]*in[i-1][j-1]+tmp[0][1]*in[i-1][j]+
				                    tmp[0][2]*in[i-1][j+1]+tmp[1][0]*in[i][j-1]+
			                        tmp[1][1]*in[i][j]    +tmp[1][2]*in[i][j+1]+
				                    tmp[2][0]*in[i+1][j-1]+tmp[2][1]*in[i+1][j]+
				                    tmp[2][2]*in[i+1][j+1]);	            						
			}
		}
		return ed;
	}
	
	public int[][] LOGedge(int[][] in, byte[][] tmp, int iw, int ih) 
	{
		int[][] ed = new int[iw][ih];
		
		for(int j = 2; j < ih-2; j++)
		{		
			for(int i = 2; i < iw-2; i++)
			{
				ed[i][j] = tmp[0][0]*in[i-2][j-2] + tmp[0][1]*in[i-2][j-1]
				         + tmp[0][2]*in[i-2][j]   + tmp[0][3]*in[i-2][j+1]
			             + tmp[0][4]*in[i-2][j+2] + tmp[1][0]*in[i-1][j-2]
				         + tmp[1][1]*in[i-1][j-1] + tmp[1][2]*in[i-1][j]  
				         + tmp[1][3]*in[i-1][j+1] + tmp[1][4]*in[i-1][j+2]
				         + tmp[2][0]*in[i][j-2]   + tmp[2][1]*in[i][j-1]  
			             + tmp[2][2]*in[i][j]     + tmp[2][3]*in[i][j+1]  
			             + tmp[2][4]*in[i][j+2]   + tmp[3][0]*in[i+1][j-2]
			             + tmp[3][1]*in[i+1][j-1] + tmp[3][2]*in[i+1][j]
			             + tmp[3][3]*in[i+1][j+1] + tmp[3][4]*in[i+1][j+2]
			             + tmp[4][0]*in[i+2][j-2] + tmp[4][1]*in[i+2][j-1]
				         + tmp[4][2]*in[i+2][j]   + tmp[4][3]*in[i+2][j+1]
				         + tmp[4][4]*in[i+2][j+2];	            						
			}
		}
		return ed;
	}
	
	//Roberts算法
    public int[] robert(int[] px, int iw, int ih, int thresh, boolean flag)
    {    
		ColorModel cm = ColorModel.getRGBdefault();
		
		int r, r0, r1, r2, r3, g, g0, g1, g2, g3, b, b0, b1, b2, b3;
		
		for(int j = 1; j < ih-1; j++)
		{
			for(int i = 1; i < iw-1; i++)
			{							
				r0 = cm.getRed(px[i+j*iw]);
				r1 = cm.getRed(px[i+(j+1)*iw]);
				r2 = cm.getRed(px[i+1+j*iw]);
				r3 = cm.getRed(px[i+1+(j+1)*iw]);
				/*--------------------------------------------*
				 * -------------------------
				 * |r0:(i,j)  |r1:(i, j+1) |
				 * |-----------------------| 交叉
				 * |r2:(i+1,j)|r3:(i+1,j+1)|
				 * -------------------------
				 *--------------------------------------------*/
				r = (int)Math.sqrt((r0-r3)*(r0-r3)+(r1-r2)*(r1-r2));
				
				g0 = cm.getGreen(px[i+j*iw]);
				g1 = cm.getGreen(px[i+(j+1)*iw]);
				g2 = cm.getGreen(px[i+1+j*iw]);
				g3 = cm.getGreen(px[i+1+(j+1)*iw]);
				g = (int)Math.sqrt((g0-g3)*(g0-g3)+(g1-g2)*(g1-g2));
								
			    b0 = cm.getBlue(px[i+j*iw]);
				b1 = cm.getBlue(px[i+(j+1)*iw]);
				b2 = cm.getBlue(px[i+1+j*iw]);
				b3 = cm.getBlue(px[i+1+(j+1)*iw]);
				b = (int)Math.sqrt((b0-b3)*(b0-b3)+(b1-b2)*(b1-b2));
				if(flag)
		        {
		        	if(r > thresh) r = 0;//黑色，边缘点
		        	else r = 255;
		        	px[i+j*iw] = (255<<24)|(r<<16)|(r<<8)|r;
		        }
		        else		        	
				    px[i+j*iw] = (255<<24)|(r<<16)|(g<<8)|b;				
			}
		}
		return px;					   				
	}
	
	//一次平面拟合Robert算法
    public int[] robert1(int[] px, int iw, int ih, int thresh)
    {    
		ColorModel cm = ColorModel.getRGBdefault();
		
		int r, g, b, g0, g1, g2, g3;
		
		int[][] img = new int[iw][ih];
		//转变为灰度图像
		for(int j = 1; j < ih-1; j++)
		{
			for(int i = 1; i < iw-1; i++)
		    {	
		        r = cm.getRed(px[i+j*iw]);
		        g = cm.getGreen(px[i+j*iw]);
		        b = cm.getBlue(px[i+j*iw]);	    
		        img[i][j] = (int)((r+g+b)/3);
		    }
		}
		
		for(int j = 1; j < ih-1; j++)
		{
			for(int i = 1; i < iw-1; i++)
			{				
				g0 = img[i][j];
				g1 = img[i][j+1];
				g2 = img[i+1][j];
				g3 = img[i+1][j+1];
				/*--------------------------------------------*
				 * -------------------------
				 * |g0:(i,j)  |g1:(i, j+1) |
				 * |-----------------------|
				 * |g2:(i+1,j)|g3:(i+1,j+1)|
				 * -------------------------
				 * a = (f(i+1,j)+f(i+1,j+1))/2 -(f(i,j)+f(i,j+1))/2
				 * b = (f(i,j+1)+f(i+1,j+1))/2 -(f(i,j)+f(i+1,j))/2
				 *
				 * r = sqrt(a*a+b*b)
				 *--------------------------------------------*/
				r = (int)Math.sqrt(((g2+g3)/2-(g0+g1)/2)*((g2+g3)/2-(g0+g1)/2)+
				                   ((g1+g3)/2-(g0+g2)/2)*((g1+g3)/2-(g0+g2)/2));
				
				if(r > thresh) r = 0;//黑色，边缘点
		        else           r = 255;
		        px[i+j*iw] = (255<<24)|(r<<16)|(r<<8)|r;		        		       
			}
		}
		return px;					   				
	}
}