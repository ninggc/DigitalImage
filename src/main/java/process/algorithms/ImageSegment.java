/**
 * @ImageSegment.java
 * @Version 1.0 2010.02.18
 * Author Xie-Hua Sun
 */

package process.algorithms;

import java.awt.image.ColorModel;

public class ImageSegment
{
    //一维最大熵分割算法
    public int segment(int[] pix, int w, int h)
	{
		int i, j, t;
        double a1, a2, max, pt;
        double[] p = new double[256];
        double[] num = new double[256];	    
	       
  	    int[][] im = new int[w][h];
		
		for(j = 0; j < h; j++)
		    for(i = 0; i < w; i++)		    
				im[i][j] = pix[i+j*w]&0xff;
  	    
        for (i = 0; i < 256; i++)
            p[i] = 0;
        
        //统计各灰度级出现的次数
        for (j = 0; j < h; j++)
            for (i = 0; i < w; i++)            
                p[im[i][j]]++;
        
        /**
         *关于计算各灰度级出现的概率
         *1.因为(p[j]/(w*h)) / (pt/(w*h)) = p[j] / pt
         *  所以计算p[j] / pt不必计算概率
         *2.当p[j]=0时，计算Math.log(p[j] / pt)将出现无穷大.但
         *  此时p[j] / pt) * Math.log(p[j] / pt)=0
         *  所以在计算a1时,不必计算这一项       
         */
        int hw =  h*w;               
        for (i = 0; i < 256; i++)
        {
            a1 = a2 = 0.0;
            pt = 0.0;
            for (j = 0; j <= i; j++)
                pt += p[j];
            
            for (j = 0; j <= i; j++)
            
            	if(p[j]>0)
                    a1 += (p[j]/pt) * Math.log(pt/p[j]);
                        
            for (j = i+1; j <256; j++)
              	if(p[j]>0)
                    a2 += (p[j] /(hw-pt))* Math.log((hw - pt)/p[j]);
            
            num[i] = a1 + a2;
        }
        
        max = 0.0; t = 0;
        for (i = 0; i < 256; i++)
        {
            if (max < num[i])
            {
                max = num[i];
                t = i;
            }
        }        
        return t;
	}
	
	//二维最大熵分割算法, 使用递推算法
    public int segment2(int[] pix, int w, int h)
	{
		int i, j, u, v, t;
        double a1, a2, max, pa, pb, pa2, pb2, sum;
        double[][] p = new double[256][256];
        double[][] num = new double[256][256];
	    
	    int[][] im = new int[w][h];
		
		for(j = 0; j < h; j++)
		    for(i = 0; i < w; i++)		    
				im[i][j] = pix[i+j*w]&0xff;
				
        for(i = 0; i < 256; i++)
            for(j = 0; j < 256; j++)
                p[i][j] = 0;
        
        //统计2维直方图p[i][j]
        for(j = 1; j < h-1; j++)
        {
            for(i = 1; i < w-1; i++)
            {
            	t = (int)((im[i-1][j]+im[i+1][j]+im[i][j-1]
            	  +im[i][j+1]+im[i][j])/5);//4-邻域均值
                p[im[i][j]][t]++;
            }
        }
        
        pa = 0.0; pb = 0.0; max = 0.0; t = 0;
        for(i = 49; i < 200; i=i+2)
        {   
            System.out.println((int)(i*100/199)+" %");
            for(j = 0; j < 256; j++)
        	{
        		a1 = 0.0; a2 = 0.0; 	            
	            pb = 0.0;
	            
	            //递推算法计算pa
	            if(j != 0)
	            {
	            	for(u = 0; u <= i; u++) 
	                    pa += p[u][j];
	            }
	            else
	            {   
	                pa = 0.0;
	            	for( u = 0; u <= i; u++)
	            	    pa += p[u][0];
	            }
	            	            
	            //递推算法计算pb
	            if(j != 0)        
	            {
	            	for(u = i+1;u < 256;u++)
	            	    pb -= p[u][j];
	            }
	            else
	            {
	            	pb = 0;
	            	for(u = i+1;u < 256;u++)
	            	    for(v = j+1; v < 256; v++)
	            	        pb += p[u][v];
	            }
	           
	            for(u = 0; u <= i; u++)
	               	for(v = 0; v <= j; v++)
	               	    if(p[u][v] > 0)
	                        a1 += (double)(-p[u][v]/pa)* Math.log(p[u][v]/pa);
	           
	            for(u = i+1; u < 256; u++)
	               	for(v = j+1; v < 256; v++)
	               	    if(p[u][v] > 0)
	               	        a2 += (double)(-p[u][v]/pb)* Math.log(p[u][v]/pb);  
	           
	            num[i][j] = a1 + a2;	                        
            }
        }
        
        max = 0.0; t = 0;
        for (i = 0; i < 256; i++)
        {
        	for(j = 0; j < 256; j++)
        	{
                if (max < num[i][j])
                {
                    max = num[i][j];
                    t = i; 
                }
            }
        }    
        return t;
	}
	
	//最佳阈值分割
	public int bestThresh(int[] pix, int w, int h)
	{
		int i, j, t,
		    thresh, 
		    newthresh,
		    gmax, gmin;         //最大,最小灰度值
        double a1, a2, max, pt;
        double[] p = new double[256];
        long[] num = new long[256];
	
	    int[][] im = new int[w][h];
		
		for(j = 0; j < h; j++)
		    for(i = 0; i < w; i++)		    
				im[i][j] = pix[i+j*w]&0xff;
				
        for (i = 0; i < 256; i++)
            p[i] = 0;
        
        //1.统计各灰度级出现的次数、灰度最大和最小值
        gmax = 0;
        gmin =255;
        for (j = 0; j < h; j++)
        {
            for (i = 0; i < w; i++)
            {
            	int g = im[i][j];
                p[g]++;
                if(g > gmax) gmax = g;
                if(g < gmin) gmin = g;
            }
        }
        
        thresh = 0;
        newthresh = (gmax+gmin)/2;
        
        int meangray1,meangray2;
        long p1, p2, s1, s2;
        for(i = 0; (thresh!=newthresh)&&(i<100);i++)
        {
        	thresh = newthresh;
        	p1 = 0; p2 = 0; s1 = 0; s2 = 0;
        	
        	//2. 求两个区域的灰度平均值
        	for(j = gmin; j < thresh;j++)
        	{
        		p1 += p[j]*j;
        		s1 += p[j];        		
        	}
        	meangray1 = (int)(p1/s1);
        	
        	for(j = thresh+1; j < gmax; j++)
        	{
        		p2 += p[j]*j;
        		s2 += p[j];        		
        	}
        	meangray2 = (int)(p2/s2);
        	//3. 计算新阈值
        	newthresh = (meangray1+meangray2)/2; 	
        }
        return newthresh;
	}
	
	//Otsu阈值分割
	public int otsuThresh(int[] pix, int iw, int ih)
	{
		ColorModel cm = ColorModel.getRGBdefault();
        int wh = iw * ih;
        int[][] inIm = new int[iw][ih]; 
 
        int i, j, t;
        int L = 256;
        double[] p = new double[L];
                        
        for (j = 0; j < ih; j++)
            for (i = 0; i < iw; i++)
                inIm[i][j] = pix[i+j*iw]&0xff;               

        for (i = 0; i < L; i++)
            p[i] = 0;

        //计算各灰度出现次数
        for (j = 0; j < ih; j++)
            for (i = 0; i < iw; i++)
                p[inIm[i][j]]++;

        //计算各灰度级出现概率
        for (int m = 0; m < L; m++)
            p[m] = p[m] / wh;

        double[] sigma = new double[L];
        for (t = 0; t < L; t++)
        {
            double w0 = 0;
            for (int m = 0; m < t+1; m++)
                w0 += p[m];
            double w1 = 1 - w0;

            double u0 = 0;
            for (int m = 0; m < t + 1; m++)
                u0 += m * p[m] / w0;

            double u1 = 0;
            for (int m = t; m < L; m++)
                u1 += m * p[m] / w1;

            sigma[t] = w0*w1*(u0-u1)*(u0-u1);
        }
        double max = 0.0;
        int T = 0;
        for (i = 0; i < L-1; i++)
        {
            if (max < sigma[i])
            {
                max = sigma[i];
                T = i;
            }
        }        
        return T;        		
	}
	
	public int[] minusImage(int[] pix1, int[] pix2, int iw, int ih)
	{
		int[] pix = new int[iw*ih];
		
		for(int i = 0;i<iw*ih;i++)
		{
			int r = Math.abs((pix1[i]&0xff)-(pix2[i]&0xff));
			pix[i] = (255<<24)|(r<<16)|(r<<8)|r;
		}
		return pix;
	}
	
	//Hough变换----------------------------------------
	
	//Hough变换检测直线
    public byte[] detectLine(byte bm[], int iw, int ih)
    {	 
        //计算变换域的尺寸
        int tMaxDist = (int)Math.sqrt(iw*iw + ih*ih);//最大距离
        
		int tMaxAngle = 90;                          //角度从0-180，每格2度
          
	    byte[] obm = new byte[iw*ih];		 
	   	int [][] ta = new int[tMaxDist][tMaxAngle];
			
		//变换域的坐标
		int tDist, tAngle;	
	
		//像素值
		int pixel;			
			
	    //初始化临时图像矩阵		
		for(int i = 0; i < ih; i++)
		    for(int j = 0; j < iw; j++)
		        obm[i+j*iw] = 0;
		        
		//初始化变换域矩阵       
	    for(tDist =0;tDist<tMaxDist;tDist++)
	        for(tAngle=0; tAngle<tMaxAngle; tAngle++)
		    	ta[tDist][tAngle] = 0;
			
		for(int j = 0; j < ih; j++)
		{
			for(int i = 0;i < iw; i++)
			{					
				//取得当前指针处的像素值
				pixel = bm[i+j*iw];
		
				//如果是黑点，则在变换域的对应各点上加1
				if(pixel == 1)
				{
					//注意步长是2度
					for(tAngle=0; tAngle<tMaxAngle; tAngle++)
					{
						tDist = (int)Math.abs(i*Math.cos(tAngle*2*Math.PI/180.0) 
						      + j*Math.sin(tAngle*2*Math.PI/180.0));
					
						ta[tDist][tAngle] = ta[tDist][tAngle] + 1;
					}
				}			
			}
		}
					
		//找到变换域中的两个最大值点
		int maxValue1 = 0;
		int maxDist1  = 0;
		int maxAngle1 = 0;		
		
		int maxValue2 = 0;
		int maxDist2  = 0;
		int maxAngle2 = 0;
				
		//找到第一个最大值点
		for (tDist = 0; tDist < tMaxDist; tDist++)
		{
			for(tAngle = 0; tAngle < tMaxAngle; tAngle++)
			{
				if(ta[tDist][tAngle] > maxValue1)
				{
					maxValue1 = ta[tDist][tAngle];
					maxDist1  = tDist;
					maxAngle1 = tAngle;
				}	
			}
		}
	
		//将第一个最大值点(maxDist1, maxAngle1)为中心的区域清零
		for (tDist = -9; tDist < 10; tDist++)
			for(tAngle = -1; tAngle < 2; tAngle++)
				if(tDist+maxDist1 >= 0 && tDist+maxDist1 < tMaxDist && 
				   tAngle+maxAngle1 >= 0 && tAngle+maxAngle1 <= tMaxAngle)
				    ta[tDist+maxDist1][tAngle+maxAngle1] = 0;	
	
		//找到第二个最大值点
		for (tDist = 0; tDist < tMaxDist; tDist++)
		{
			for(tAngle = 0; tAngle < tMaxAngle; tAngle++)
			{
				if(ta[tDist][tAngle] > maxValue2)
				{
					maxValue2 = ta[tDist][tAngle];
					maxDist2  = tDist;
					maxAngle2 = tAngle;
				}	
			}
		}	
		
		//标注直线
		for(int j = 0; j < ih; j++)
		{
			for(int i = 0; i < iw; i++)
			{
				//第一条直线上
				tDist = (int)Math.abs(i*Math.cos(maxAngle1*2*Math.PI/180.0) 
				      + j*Math.sin(maxAngle1*2*Math.PI/180.0));
				if (tDist == maxDist1)
					obm[i+j*iw] = 1;
								
				//第二条直线上
				tDist = (int)Math.abs(i*Math.cos(maxAngle2*2*Math.PI/180.0) 
				      + j*Math.sin(maxAngle2*2*Math.PI/180.0));
				if (tDist == maxDist2)
				    obm[i+j*iw] = 1;
			}
		}		
	    return obm;				
	} 
	    
    //Hough变换检测圆周
    public byte[] detectCirc(byte bm[], int iw, int ih)
    {	         
		int i, j, x, yp, ym, t;
		
		int[] num = new int[iw*ih];     //变换域矩阵
	    byte[] obm = new byte[iw*ih];   //临时图像矩阵
	      
		//初始化变换域矩阵num和临时图像矩阵bw_tem
		for(j = 0; j < ih; j++)
		{
			for(i = 0; i < iw; i++)
			{
				num[i+j*iw]    = 0;
				obm[i+j*iw] =0;
			}
		}
	
	    //calculate num[][]
		for(j = 0; j < ih; j++)
		{
			for(i = 0; i < iw; i++)
			{
				if(bm[i+j*iw] == 1)          //黑色
				{        
					for(x = i-20; x <= i+20; x++)
					{
						t = (int)Math.sqrt(20*20-(x-i)*(x-i));
						yp = j+t;
						ym = j-t;
						if(x >= 0&&x < ih&&yp >= 0&&yp < iw) 
							num[x+yp*iw]++;
						if(x >= 0&&x < ih&&ym >= 0&&ym < iw) 
							num[x+ym*iw]++;
					}
				}
			}
	    }	

	    int i_max = 0, j_max = 0,  //寻找的圆心坐标
		    max   = 0;

		for(j = 0; j < ih; j++)
		{
			for(i = 0; i < iw; i++)
			{
	            if(num[i+j*iw] > max)
	            {
	                max = num[i+j*iw];
				    i_max = i;
					j_max = j;
				}
			}
		}
		
		//画出以i_max,j_max为圆心，半径20的圆
		if(max > 20)
		{
		    for(i = i_max-20; i <= i_max+20; i++)
		    {
			    t = (int)Math.sqrt(20*20-(i_max-i)*(i_max-i));
	            obm[i+(j_max+t)*iw] = 1;  
			    obm[i+(j_max-t)*iw] = 1;
			}
		}
		
		//清除点(i_max,j_max)附近num[i][j]的值
	    for(i = i_max-10; i < i_max+10; i++)
	        for(j = j_max-10; j < j_max+10; j++)
			    if((i_max-10 >= 0)&&(i_max+10 < ih)&&
			       (j_max-10 >= 0)&&(j_max+10 < iw))  //防止数组越界
				    num[i+j*iw] = 0;
	  	
	    //检测第2个圆
		max = 0;
		for(i = 0; i < ih; i++)
		{
			for(j = 0; j < iw; j++)
			{
	            if(num[i+j*iw] > max)
	            {
	                max = num[i+j*iw];
					i_max = i;
					j_max = j;
				}
			}
		}
		
		//画出以i_max,j_max为圆心，半径20的第2个圆
		if(max > 20)
		{
			for(i = i_max-20; i <= i_max+20; i++)
			{
				t = (int)Math.sqrt(20*20-(i_max-i)*(i_max-i));
				obm[i+(j_max+t)*iw] = 1;  
				obm[i+(j_max-t)*iw] = 1;
			}
		}			 
	    return obm;        	
	}  	
}