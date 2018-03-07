/**
 * @ImageSegment.java
 * @Version 1.0 2010.02.18
 * Author Xie-Hua Sun
 */

package process.algorithms;

import java.awt.image.ColorModel;

public class ImageSegment
{
    //һά����طָ��㷨
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
        
        //ͳ�Ƹ��Ҷȼ����ֵĴ���
        for (j = 0; j < h; j++)
            for (i = 0; i < w; i++)            
                p[im[i][j]]++;
        
        /**
         *���ڼ�����Ҷȼ����ֵĸ���
         *1.��Ϊ(p[j]/(w*h)) / (pt/(w*h)) = p[j] / pt
         *  ���Լ���p[j] / pt���ؼ������
         *2.��p[j]=0ʱ������Math.log(p[j] / pt)�����������.��
         *  ��ʱp[j] / pt) * Math.log(p[j] / pt)=0
         *  �����ڼ���a1ʱ,���ؼ�����һ��       
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
	
	//��ά����طָ��㷨, ʹ�õ����㷨
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
        
        //ͳ��2άֱ��ͼp[i][j]
        for(j = 1; j < h-1; j++)
        {
            for(i = 1; i < w-1; i++)
            {
            	t = (int)((im[i-1][j]+im[i+1][j]+im[i][j-1]
            	  +im[i][j+1]+im[i][j])/5);//4-�����ֵ
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
	            
	            //�����㷨����pa
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
	            	            
	            //�����㷨����pb
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
	
	//�����ֵ�ָ�
	public int bestThresh(int[] pix, int w, int h)
	{
		int i, j, t,
		    thresh, 
		    newthresh,
		    gmax, gmin;         //���,��С�Ҷ�ֵ
        double a1, a2, max, pt;
        double[] p = new double[256];
        long[] num = new long[256];
	
	    int[][] im = new int[w][h];
		
		for(j = 0; j < h; j++)
		    for(i = 0; i < w; i++)		    
				im[i][j] = pix[i+j*w]&0xff;
				
        for (i = 0; i < 256; i++)
            p[i] = 0;
        
        //1.ͳ�Ƹ��Ҷȼ����ֵĴ������Ҷ�������Сֵ
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
        	
        	//2. ����������ĻҶ�ƽ��ֵ
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
        	//3. ��������ֵ
        	newthresh = (meangray1+meangray2)/2; 	
        }
        return newthresh;
	}
	
	//Otsu��ֵ�ָ�
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

        //������Ҷȳ��ִ���
        for (j = 0; j < ih; j++)
            for (i = 0; i < iw; i++)
                p[inIm[i][j]]++;

        //������Ҷȼ����ָ���
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
	
	//Hough�任----------------------------------------
	
	//Hough�任���ֱ��
    public byte[] detectLine(byte bm[], int iw, int ih)
    {	 
        //����任��ĳߴ�
        int tMaxDist = (int)Math.sqrt(iw*iw + ih*ih);//������
        
		int tMaxAngle = 90;                          //�Ƕȴ�0-180��ÿ��2��
          
	    byte[] obm = new byte[iw*ih];		 
	   	int [][] ta = new int[tMaxDist][tMaxAngle];
			
		//�任�������
		int tDist, tAngle;	
	
		//����ֵ
		int pixel;			
			
	    //��ʼ����ʱͼ�����		
		for(int i = 0; i < ih; i++)
		    for(int j = 0; j < iw; j++)
		        obm[i+j*iw] = 0;
		        
		//��ʼ���任�����       
	    for(tDist =0;tDist<tMaxDist;tDist++)
	        for(tAngle=0; tAngle<tMaxAngle; tAngle++)
		    	ta[tDist][tAngle] = 0;
			
		for(int j = 0; j < ih; j++)
		{
			for(int i = 0;i < iw; i++)
			{					
				//ȡ�õ�ǰָ�봦������ֵ
				pixel = bm[i+j*iw];
		
				//����Ǻڵ㣬���ڱ任��Ķ�Ӧ�����ϼ�1
				if(pixel == 1)
				{
					//ע�ⲽ����2��
					for(tAngle=0; tAngle<tMaxAngle; tAngle++)
					{
						tDist = (int)Math.abs(i*Math.cos(tAngle*2*Math.PI/180.0) 
						      + j*Math.sin(tAngle*2*Math.PI/180.0));
					
						ta[tDist][tAngle] = ta[tDist][tAngle] + 1;
					}
				}			
			}
		}
					
		//�ҵ��任���е��������ֵ��
		int maxValue1 = 0;
		int maxDist1  = 0;
		int maxAngle1 = 0;		
		
		int maxValue2 = 0;
		int maxDist2  = 0;
		int maxAngle2 = 0;
				
		//�ҵ���һ�����ֵ��
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
	
		//����һ�����ֵ��(maxDist1, maxAngle1)Ϊ���ĵ���������
		for (tDist = -9; tDist < 10; tDist++)
			for(tAngle = -1; tAngle < 2; tAngle++)
				if(tDist+maxDist1 >= 0 && tDist+maxDist1 < tMaxDist && 
				   tAngle+maxAngle1 >= 0 && tAngle+maxAngle1 <= tMaxAngle)
				    ta[tDist+maxDist1][tAngle+maxAngle1] = 0;	
	
		//�ҵ��ڶ������ֵ��
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
		
		//��עֱ��
		for(int j = 0; j < ih; j++)
		{
			for(int i = 0; i < iw; i++)
			{
				//��һ��ֱ����
				tDist = (int)Math.abs(i*Math.cos(maxAngle1*2*Math.PI/180.0) 
				      + j*Math.sin(maxAngle1*2*Math.PI/180.0));
				if (tDist == maxDist1)
					obm[i+j*iw] = 1;
								
				//�ڶ���ֱ����
				tDist = (int)Math.abs(i*Math.cos(maxAngle2*2*Math.PI/180.0) 
				      + j*Math.sin(maxAngle2*2*Math.PI/180.0));
				if (tDist == maxDist2)
				    obm[i+j*iw] = 1;
			}
		}		
	    return obm;				
	} 
	    
    //Hough�任���Բ��
    public byte[] detectCirc(byte bm[], int iw, int ih)
    {	         
		int i, j, x, yp, ym, t;
		
		int[] num = new int[iw*ih];     //�任�����
	    byte[] obm = new byte[iw*ih];   //��ʱͼ�����
	      
		//��ʼ���任�����num����ʱͼ�����bw_tem
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
				if(bm[i+j*iw] == 1)          //��ɫ
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

	    int i_max = 0, j_max = 0,  //Ѱ�ҵ�Բ������
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
		
		//������i_max,j_maxΪԲ�ģ��뾶20��Բ
		if(max > 20)
		{
		    for(i = i_max-20; i <= i_max+20; i++)
		    {
			    t = (int)Math.sqrt(20*20-(i_max-i)*(i_max-i));
	            obm[i+(j_max+t)*iw] = 1;  
			    obm[i+(j_max-t)*iw] = 1;
			}
		}
		
		//�����(i_max,j_max)����num[i][j]��ֵ
	    for(i = i_max-10; i < i_max+10; i++)
	        for(j = j_max-10; j < j_max+10; j++)
			    if((i_max-10 >= 0)&&(i_max+10 < ih)&&
			       (j_max-10 >= 0)&&(j_max+10 < iw))  //��ֹ����Խ��
				    num[i+j*iw] = 0;
	  	
	    //����2��Բ
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
		
		//������i_max,j_maxΪԲ�ģ��뾶20�ĵ�2��Բ
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