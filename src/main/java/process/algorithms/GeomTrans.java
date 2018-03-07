/**
 * @GeomTrans.java
 * @Version 1.0 2010.02.14
 * @Author Xie-Hua Sun 
 * ͼ�����任����ת�����С�ƽ�ơ�����
 */

package process.algorithms;

import java.awt.image.ColorModel;

public class GeomTrans
{        
   	/****************************************************
	 * ͼ����ת
	 * pix�� --ԭͼ����������
	 * opix--���ͼ���������� 
	 * (x,y) --ԭͼ������ 0<=x<=w, 0<=y<=h
	 * (i,j) --���ͼ������0<=i,j<=owh	 
	 * beta  --��ת�Ƕ�
	 ****************************************************/
	public int[] imRotate(int[] pix, float beta, int iw, int ih, int owh)
	{
		//1.��ת�����ͼ�������С��Χ�п��
		int[] opix = new int[owh * owh];
		
		double t = beta / 180;    	     
       		
		float cos_beta =  (float)Math.cos(t * Math.PI);//˳ʱ����ת
		float sin_beta =  (float)Math.sin(t * Math.PI);
		
		//2.����ת�任, �������ͼ���p(i,j)����Ӧ��ԭͼ�������(x,y)
		for(int i = 0;i < owh;i++)    
		{
			for(int j = 0;j < owh;j++)
			{				
				//��ת�任����任
				float u = (j-owh/2)*cos_beta+(i-owh/2)*sin_beta;
				float v = (i-owh/2)*cos_beta-(j-owh/2)*sin_beta;
			
				//���������ԭͼ��ľ�������
				u += iw/2;
				v += ih/2;
			
				int x = (int)u;
				int y = (int)v;

				int index = i * owh + j;
				//3.��������, �����������ĵ�(x,y),��ֵF(i,j)=f(x,y)
				if((x >= 0) && (x <= iw - 1) && (y >= 0) && (y <= ih - 1))
					opix[index] = pix[y * iw + x];							
			}
		} 	 
	 	return opix;
	} 
	  
	//����任 	
    public int[] imMirror(int[] pixs, int w, int h, int n)
    {
    	int[] pix = new int[w * h];
    	    	   	
    	//���ݾ���任��ʽ������(u,v)����ֵF(u,v)=f(i,j)
    	for(int j = 0; j < h; j++)
    	{
    		for(int i = 0; i < w; i++)
    		{
    			if(n == 0)     //ˮƽ
    			{	    			
	    			int u = w - 1 - i;
	    			int v = j;
	    			pix[v*w+u] = pixs[j*w+i]; 
    			}
    			else if(n == 1)//��ֱ
    			{	    			
	    			int u = i;
	    			int v = h - 1 - j;
	    			pix[v*w+u] = pixs[j*w+i]; 
    			}   			
    		}    		
    	}
    	return pix;
    }
    
    /**********************************************
     * ���б任�㷨
     * 1.����ͼ���Ľǵ�����
     * 2.�����Χͼ�����С���Σ�����Χ�еĿ�͸�
     * 3.�������Χ���ڵ�������������Ϊ����ɫ
     * 4.���ݴ��б任��ʽ��
     *   u = (int)(i + j * shx)
     *   v = (int)(i * shy + j)
     * ������(u,v)����ֵ F(u,v) = f(i,j)      
     **********************************************/
     
    public int[] imShear(int[] pixs, double shx, double shy, 
                         int w, int h, int ow, int oh)
    { 
        int[] pix = new int[ow * oh];
    	    	
    	//���ݴ��б任��ʽ������(u,v)����ֵF(u,v)=f(i,j)
    	for(int j = 0; j < h; j++)
    	{
    		for(int i = 0; i < w; i++)
    		{
    			int u = (int)(i + j * shx);
    			int v = (int)(i * shy + j);
    			pix[v*ow+u] = pixs[j*w+i];    			
    		}    		
    	}
    	return pix;
    }
    
    //ƽ�Ʊ任�㷨
    public int[] imTrans(int[] pixs, int tx, int ty, 
                         int w, int h, int ow, int oh)
    { 
        int u, v;
        int[] pix = new int[ow * oh];
    	    	
    	//���ݴ��б任��ʽ������(u,v)����ֵF(u,v)=f(i,j)
    	for(int j = 0; j < h; j++)
    	{
    		for(int i = 0; i < w; i++)
    		{
    			u = i + tx;
    			v = j + ty;
    			pix[v*ow+u] = pixs[j*w+i];    			
    		}    		
    	}
    	return pix;
    }
    
    //���ڽ���ֵ
	public int[] nearNeighbor(int pixs[], int iw, int ih, 
	                          int ow, int oh, float p)
    {    	
        int opix[] = new int[ow * oh];//Ŀ��ͼ��������
  	    
	    ColorModel cm = ColorModel.getRGBdefault();
	    for(int i = 0; i < oh; i++) 
	    {
	    	int u = (int)(i/p);
	    	for(int j = 0; j < ow; j++)
	    	{
	    		int r, g, b;    		
	    		int v = (int)(j/p);	            
	           
	            r = cm.getRed(pixs[u*iw+v]);
	            g = cm.getGreen(pixs[u*iw+v]);
	            b = cm.getBlue(pixs[u*iw+v]);
	            
	            opix[i*ow+j] = 255 << 24|r << 16|g << 8|b;
	        }
	    }
	    return opix;     
    }
    
    //˫���Բ�ֵ�㷨
    public int[] bilinear(int pixs[], int iw, int ih, int ow, int oh, float p)
    { 	
    	int pixd[]=new int[ow * oh];//Ŀ��ͼ��������
  	    
	    ColorModel cm = ColorModel.getRGBdefault();
	    for(int i = 0; i < oh-1; i++) 
	    {
	    	float dy = i/p;
	    	int iy = (int)dy;
	    	if(iy > ih-2) iy = ih-2;
	    	float d_y = dy - iy;
	    	
	    	for(int j = 0; j < ow-1; j++) 
	    	{
	    		int a,r,g,b;
	    		float dx = j/p;    		
	    		int ix = (int)dx;
	    		if(ix > iw-2) ix = iw-2;
	    		float d_x= dx - ix;
	    		
	    		//f(i+u,j+v)=(1-u)(1-v)f(i,j)+u(1-v)f(i+1,j)+
	    		//           (1-u)vf(i,j+1)+uvf(i+1,j+1)
	    		r = (int)((1-d_x)*(1-d_y)*cm.getRed(pixs[iy*iw+ix])+
                          d_x*(1-d_y)*cm.getRed(pixs[iy*iw+ix+1])+
                          (1-d_x)*d_y*cm.getRed(pixs[(iy+1)*iw+ix])+
                          d_x*d_y*cm.getRed(pixs[(iy+1)*iw+ix+1]));
            	            
	            g = (int)((1-d_x)*(1-d_y)*cm.getGreen(pixs[iy*iw+ix])+
                          d_x*(1-d_y)*cm.getGreen(pixs[iy*iw+ix+1])+
                          (1-d_x)*d_y*cm.getGreen(pixs[(iy+1)*iw+ix])+
                          d_x*d_y*cm.getGreen(pixs[(iy+1)*iw+ix+1]));
            	            
	            b = (int)((1-d_x)*(1-d_y)*cm.getBlue(pixs[iy*iw+ix])+
                          d_x*(1-d_y)*cm.getBlue(pixs[iy*iw+ix+1])+
                          (1-d_x)*d_y*cm.getBlue(pixs[(iy+1)*iw+ix])+
                          d_x*d_y*cm.getBlue(pixs[(iy+1)*iw+ix+1]));
	            	            	            
	            pixd[i*ow+j] = 255 << 24|r << 16|g << 8|b;
	        }
	    }
	    return pixd;      
    }
    
    //������ͼ�񰴸����Ŀ�Ⱥ͸߶ȱ�����������
	public int[] scale(int[] pix, int iw, int ih, int ow, int oh, 
	                   float scalex, float scaley)
	{		
		int pixelsSrc[]  = new int[iw * ih];
		int pixelsDest[] = new int[ow * oh];
		
		//������,����ͼ��
		this.scale(pix, 0, 0, iw, ih, iw, scalex, scaley, pixelsDest);
	 	return (pixelsDest);
	}
	
	/********************************************************
	 * src ԭͼ�����������,
	 * (x, y) �����ŵ����������Ͻǵ�����,
	 * (w, h) ��������Ŀ�Ⱥ͸߶�
	 * scansize ԭͼ���ɨ����
	 * (scalex, scaley) ˮƽ�ʹ�ֱ�����ϵ����ű�
	 * dst ���ź��ͼ����������
	 ********************************************************/	
	public void scale(int[] src, int x, int y, int w, int h, int scansize,
	                  float scalex, float scaley, int[] dst) 
	{				
		//ԭͼ��Ŀ��
		int srcWidth = scansize;
		
		//ԭͼ�����ɨ���������:��ԭͼ��ĸ߶�
		int srcHeight = src.length / scansize;
		
		//width---height:��������Ŀ�Ⱥ͸߶�,
		//������������ǺϷ���,��ô���Ǿ���w,h
		int width  = w;
		int height = h;

		if((x + w) > scansize)  width  = scansize  - x;
		if((y + h) > srcHeight) height = srcHeight - y;
	
		int dstWidth  = (int)( width * scalex + 0.5f);
		int dstHeight = (int)(height * scaley + 0.5f);

		//���з���任
		//i--����,j--����
		for(int i = 0;i < dstHeight;i++)
		{			
			//������任��,��ȡ��i������Ӧ��ԭͼ���λ��:��:yy
			float y_inverse_map = i / scaley;
			
			int y_lt = (int)y_inverse_map;
			
			//��ֱ����ƫ����
			float v = y_inverse_map - y_lt;
			
			//���Ͻǵ�y����
			y_lt += y;
			
			int indexBase = i * dstWidth;
			for(int j = 0;j < dstWidth;j++)
			{
				float x_inverse_map = j / scalex;
			
				int x_lt = (int)x_inverse_map;
			
				//ˮƽ����ƫ����
				float u = x_inverse_map - x_lt;
			
				//���Ͻǵ�y����
				x_lt += x;
				
				//ͨ�������ȡ�任��ĵ�
				int index  = indexBase + j;
				dst[index] = interpolate(src, x_lt, y_lt, u, v, 
				                         srcWidth, srcHeight);
			}
		}
	}
	
	/************************************************************** 
	 * src:ԭͼ�������,
	 * (x,y):��������任��,�뷴��任����ӽ���ԭͼ������Ͻǵĵ�
	 * (u,v):����任�����(x,y)���ƫ����.
	 * scanw:ԭͼ���ɨ����,scanhԭͼ��ĸ߶�
	 **************************************************************/
	private int interpolate(int[] src, int x, int y, float u, 
	                        float v, int scanw, int scanh)
	{		
		ColorModel colorModel = ColorModel.getRGBdefault();
		
		int r = 0;
		int g = 0;
		int b = 0;
		
		//�ڽ����������ֵ
		int red[][]   = new int[4][4];
		int green[][] = new int[4][4];
		int blue[][]  = new int[4][4];
		
		//�ڽ����������
		int xx[] = new int[4];
		int yy[] = new int[4];
		
		xx[0] = x - 1; xx[1] = x; xx[2] = x + 1; xx[3] = x + 2;
		yy[0] = y - 1; yy[1] = y; yy[2] = y + 1; yy[3] = y + 2;
		
		if(xx[0] < 0) xx[0] = 0;
		if(yy[0] < 0) yy[0] = 0;
		if(xx[2] > scanw - 1) xx[2] = scanw - 1;
		if(yy[2] > scanh - 1) yy[2] = scanh - 1;
		if(xx[3] > scanw - 1) xx[3] = scanw - 1;
		if(yy[3] > scanh - 1) yy[3] = scanh - 1;
	
		//��ȡ4*4���������ֵ
		int i = 0; int j = 0;
		for(i = 0; i < 4; i++)
		{
			int indexBase = yy[i] * scanw;
			//j����������
			for(j = 0; j < 4; j++)
			{
				int index   = indexBase + xx[j];
				red[i][j]   = colorModel.getRed(src[index]);
				green[i][j] = colorModel.getGreen(src[index]);
				blue[i][j]  = colorModel.getBlue(src[index]);
			}
		}
		
		float su[] = new float[4];
		float sv[] = new float[4];
		
		su[0] = sinx_x(1.0f + u);
		su[1] = sinx_x(u);
		su[2] = sinx_x(1.0f - u);
		su[3] = sinx_x(2.0f - u);

		sv[0] = sinx_x(1.0f + v);
		sv[1] = sinx_x(v);
		sv[2] = sinx_x(1.0f - v);
		sv[3] = sinx_x(2.0f - v);
		
		//������˻�:sv * red,sv * green,sv * blue
		float sv_r[] = new float[4]; 
		float sv_g[] = new float[4];
		float sv_b[] = new float[4];
		
		for(i = 0; i < 4; i++)
		{
			for(j = 0; j < 4; j++)
			{
				sv_r[i] += (sv[j] * red[j][i]);
				sv_g[i] += (sv[j] * green[j][i]);
				sv_b[i] += (sv[j] * blue[j][i]);
			}
		}
		
		r = (int)(su[0] * sv_r[0] + su[1] * sv_r[1] 
		  + su[2] * sv_r[2] + su[3] * sv_r[3]);
		g = (int)(su[0] * sv_g[0] + su[1] * sv_g[1] 
		  + su[2] * sv_g[2] + su[3] * sv_g[3]);
		b = (int)(su[0] * sv_b[0] + su[1] * sv_b[1] 
		  + su[2] * sv_b[2] + su[3] * sv_b[3]);
		
		r = (r < 0) ? 0 : ((r > 255) ? 255 : r);
		g = (g < 0) ? 0 : ((g > 255) ? 255 : g);
		b = (b < 0) ? 0 : ((b > 255) ? 255 : b);
		return (0xFF000000 | (( r << 16 ) | ( g << 8 ) | b ));
	}
	
	//���� sin(x)/x ��ֵ,���ö���ʽչ��
	private float sinx_x(float x)
	{		
		float x_abs = Math.abs(x);
		float x_x   = x_abs * x_abs;
		float x_x_x = x_x * x_abs;
		
		if(x_abs < 1.0f)
			return (1.0f - 2.0f * x_x + x_x_x);
		else if(x_abs < 2.0f)
			return (4.0f - 8.0f * x_abs + 5.0f * x_x - x_x_x);
		else 
			return	0.0f;
	}      
}
