/**
 *@FracCompress.java
 *@Version 1.0  2010.02.24
 *@Author Xie-Hua Sun         
 */

package process.algorithms;

import java.io.*;
import javax.swing.*;
import java.awt.*;     
import java.util.Calendar;
  
public class FracCompress
{
	int iw, ih;
	int BLOCKSIZE;
    int sq_BLOCKSIZE;
    int IMAGESIZE;
    int DOMAINSIZE;
    int CODENUMBER;
                   
    int[]   pixels;
    int[][] iImage;   
    int[][] oImage;   
    int[][] Domain;   
    int[][] TransDom; 
	
	public FracCompress()
	{
	    BLOCKSIZE    = 8;
        sq_BLOCKSIZE = 64;
        IMAGESIZE    = 256;
        DOMAINSIZE   = 128;
        CODENUMBER   = 1024;
        iw = IMAGESIZE; 
        ih = IMAGESIZE;
               
        iImage   = new int[IMAGESIZE][IMAGESIZE];  //ԭͼ�����   
        oImage   = new int[IMAGESIZE][IMAGESIZE];  //���ͼ�����   
        Domain   = new int[DOMAINSIZE][DOMAINSIZE];//��������Сͼ�����
        TransDom = new int[BLOCKSIZE][BLOCKSIZE];  //�任��Ķ������	
	}
	
	FRACTALCODEPARAMETER[] Parameter;
	    
    //���ο���
    class FRACTALCODEPARAMETER
    {
        public int RangeX,  RangeY;
        public int DomainX, DomainY;
        public int TransformNo;
        public float Scale, Offset;
    }

    //����������
    class DOMAINLIB
    {
        public int   SumDomPixel;
        public float ave_DomPixel;
        public long  sq_SumDomPixel;            
    }
    
    //ѹ��
    public void fracCompress(int[][] iImage)
    {    
		int i, j, k, l, m, n, t;
        int SumRanPixel;      //��ǰֵ���ҶȺ� 
        int   SumDomPixel;    //��ǰ�������ҶȺ�
        float ave_RanPixel;   //��ǰֵ���Ҷ�ƽ��         
        float ave_DomPixel;   //��ǰ�������Ҷ�ƽ��
        long  sq_SumRanPixel; //��ǰֵ���Ҷ�ƽ����
        long  sq_SumDomPixel, //��ǰ�������Ҷ�ƽ����
              SumRanDom;      //��ǰֵ����붨�����ҶȻ�֮��
        float Scale,  Offset;
        float CurDrms,Drms;
        
        int curr_min,    //ʱ����ʾ"��"
            curr_sec,        //ʱ����ʾ"��"
            //curr_mil,        //��ǰʱ��"����"
            start_min,
            start_sec;       //��ʱ��ʼ"��"
            //start_mil;       //��ʱ��ʼ"����"
        
        //���һ��Parameter[CODENUMBER]����ʱ�ṹ��ʹ��
        Parameter = new FRACTALCODEPARAMETER[CODENUMBER + 1];
        for(i = 0; i < CODENUMBER+1; i++)
            Parameter[i] = new FRACTALCODEPARAMETER();//�����ڴ� 
                                                            
        DOMAINLIB[][] Domainlib = new DOMAINLIB[DOMAINSIZE-BLOCKSIZE+1]
                                               [DOMAINSIZE-BLOCKSIZE+1];                    
        
        //��ʱ��ʼʱ��=============================
	    Calendar ctime = Calendar.getInstance();
        start_min = ctime.get(Calendar.MINUTE);
        start_sec = ctime.get(Calendar.SECOND);       
  	    //start_mil = ctime.get(Calendar.MILLISECOND); 
  	    
        //��ԭͼ��С1/2������������ͼ��
        for (i = 0; i < DOMAINSIZE; i++)
            for (j = 0; j < DOMAINSIZE; j++)
                Domain[i][j] =(int)(iImage[i*2][j*2]   + 
                                    iImage[i*2+1][j*2] + 
                                    iImage[i*2][j*2+1] + 
                                    iImage[i*2+1][j*2+1]) / 4;
            
        //����Domainlib[][]
        for (m = 0; m < DOMAINSIZE - BLOCKSIZE + 1; m++)
        {
            for (n = 0; n < DOMAINSIZE - BLOCKSIZE + 1; n++)
            {
                //���㶨�����ҶȺ͡�ƽ���ͼ�ƽ��ֵ
                SumDomPixel = 0;
                sq_SumDomPixel = 0;
                for (k = 0; k < BLOCKSIZE; k++)
                {
                    for (l = 0; l < BLOCKSIZE; l++)
                    {
                        SumDomPixel    += Domain[m+k][n+l]; //�ҶȺ�
                        sq_SumDomPixel += Domain[m+k][n+l] * 
                                          Domain[m+k][n+l]; //�Ҷ�ƽ����
                    }
                }
                Domainlib[m][n] = new DOMAINLIB();          //�����ڴ�
                Domainlib[m][n].SumDomPixel = SumDomPixel;
                Domainlib[m][n].ave_DomPixel = SumDomPixel / sq_BLOCKSIZE;
                Domainlib[m][n].sq_SumDomPixel = sq_SumDomPixel;
            }
        }
                    
        int num = 0;
        RandomAccessFile rf = null;
        
        try
        {
		    rf = new RandomAccessFile("./images/compressed/compressed.frc","rw");
		} 
		catch(IOException e2)
		{
            e2.printStackTrace();

            System.err.println(e2);
		}     
        for (i = 0; i < IMAGESIZE / BLOCKSIZE; i++)
        {
            for (j = 0; j < IMAGESIZE / BLOCKSIZE; j++)
            {
                Drms = 1000000;
                
                //��ǰֵ�������
                Parameter[num].RangeY = i;
                Parameter[num].RangeX = j;

                //���㵱ǰֵ���ҶȺ�,ƽ����,ƽ��ֵ
                SumRanPixel = 0;
                sq_SumRanPixel = 0;
                for (k = 0; k < BLOCKSIZE; k++)
                {
                    for (l = 0; l < BLOCKSIZE; l++)
                    {
                        SumRanPixel    += iImage[i*BLOCKSIZE+k][j*BLOCKSIZE+l];
                        sq_SumRanPixel += iImage[i*BLOCKSIZE+k][j*BLOCKSIZE+l] * 
                                          iImage[i*BLOCKSIZE+k][j*BLOCKSIZE+l];
                    }
                }
                ave_RanPixel = SumRanPixel / sq_BLOCKSIZE; //��ǰֵ���Ҷ�ƽ��ֵ
                
                //����С������ͼ��Domain[DOMAINSIZE,DOMAINSIZE]����ȡ8X8
                //�������.�������֮������ص������غ�, ���ڿ�֮�����
                //һ������, ����(DOMAINSIZE - BLOCKSIZE + 1)^2
                for (m = 0; m < DOMAINSIZE - BLOCKSIZE + 1; m++)
                {
                    for (n = 0; n < DOMAINSIZE - BLOCKSIZE + 1; n++)
                    {                                             
                        SumDomPixel    = Domainlib[m][n].SumDomPixel;   //�ҶȺ�
                        ave_DomPixel   = Domainlib[m][n].ave_DomPixel;  //ƽ��ֵ
                        sq_SumDomPixel = Domainlib[m][n].sq_SumDomPixel;//ƽ����

                        //8�ֱ任
                        for (t = 0; t < 8; t++)
                        {
                            if (t == 0)
                            {
                                for (k = 0; k < BLOCKSIZE; k++)
                                    for (l = 0; l < BLOCKSIZE; l++)
                                        TransDom[k][l] = Domain[m+k][n+l];
                            }
                            else if (t == 1)
                            {
                                for (k = 0; k < BLOCKSIZE; k++)
                                    for (l = 0; l < BLOCKSIZE; l++)
                                        TransDom[k][l] = Domain[m+k][n+BLOCKSIZE-l-1];
                            }
                            else if (t == 2)
                            {
                                for (k = 0; k < BLOCKSIZE; k++)
                                    for (l = 0; l < BLOCKSIZE; l++)
                                        TransDom[k][l] = Domain[m+BLOCKSIZE-k-1][n+l];
                            }
                            else if (t == 3)
                            {
                                for (k = 0; k < BLOCKSIZE; k++)
                                    for (l = 0; l < BLOCKSIZE; l++)
                                        TransDom[k][l] = Domain[m+l][n+k];
                            }
                            else if (t == 4)
                            {
                                for (k = 0; k < BLOCKSIZE; k++)
                                    for (l = 0; l < BLOCKSIZE; l++)
                                        TransDom[k][l] = Domain[m+l][n+BLOCKSIZE-k-1];
                            }
                            else if (t == 5)
                            {
                                for (k = 0; k < BLOCKSIZE; k++)
                                    for (l = 0; l < BLOCKSIZE; l++)
                                        TransDom[k][l] = Domain[m+BLOCKSIZE-l-1]
                                                               [n+BLOCKSIZE-k-1];
                            }
                            else if (t == 6)
                            {
                                for (k = 0; k < BLOCKSIZE; k++)
                                    for (l = 0; l < BLOCKSIZE; l++)
                                        TransDom[k][l] = Domain[m+BLOCKSIZE-k-1]
                                                               [n+BLOCKSIZE-l-1];
                            }
                            else if (t == 7)
                            {
                                for (k = 0; k < BLOCKSIZE; k++)
                                    for (l = 0; l < BLOCKSIZE; l++)
                                        TransDom[k][l] = Domain[m+BLOCKSIZE-l-1][n+k];
                            }
                            
                            //���㵱ǰֵ���Ͷ������Ҷȳ˻��ܺ�
                            SumRanDom = 0;
                            for (k = 0; k < BLOCKSIZE; k++)
                                for (l = 0; l < BLOCKSIZE; l++)
                                    SumRanDom += iImage[i*BLOCKSIZE+k][j*BLOCKSIZE+l] * 
                                                 TransDom[k][l];
                            
                            //���㵱ǰ�������Ӻ�ƽ������
                            double facter = sq_BLOCKSIZE * sq_SumDomPixel - 
                                            SumDomPixel * SumDomPixel;
                            if (facter > -0.00001 && facter < 0.00001)
                                Scale = 0;
                            else
                                Scale = (float)((sq_BLOCKSIZE * SumRanDom - 
                                        SumRanPixel * SumDomPixel) / facter);

                            Offset = ave_RanPixel - Scale * ave_DomPixel;
	   /************************************************************************
	    * ���ټ������CurDrms
	    * CurDrms 
	    *  = \sum_{k=0}^7\sum_{l=0}^7 (Scale * TransDom[k][l] + Offset - 
	    *                              Image[i*8+k][j*8+l])^2
	    *  = (Scale)^2*\sum_{k=0}^7\sum_{l=0}^7(TransDom[k][l])^2
	    *    + \sum_{k=0}^7\sum_{l=0}^7 (Offset)^2
	    *    + \sum_{k=0}^7\sum_{l=0}^7 (Image[i*8+k][j*8+l])^2
	    *    + 2*Scale*Offset*\sum_{k=0}^7\sum_{l=0}^7 TransDom[k][l]
	    *    - 2*Scale*\sum_{k=0}^7\sum_{l=0}^7 TransDom[k][l]*Image[i*8+k][j*8+l]
	    *    - 2*Offset*\sum_{k=0}^7\sum_{l=0}^7 Image[i*8+k][j*8+l]
	    *  = (Scale)^2*sq_SumDomPixel
	    *    + 64*(Offset)^2
	    *    + sq_SumRanPixel
	    *    + 2*Scale*Offset*SumDomPixel
	    *    - 2*Scale*SumRanDom
	    *    - 2*Offset*SumRanPixel
	    ************************************************************************/
	                                
                            CurDrms = Scale * Scale * sq_SumDomPixel
                                            + 64 * Offset * Offset          
                                            + sq_SumRanPixel
                                            + 2 * Scale * Offset * SumDomPixel
                                            - 2 * Scale * SumRanDom
                                            - 2 * Offset * SumRanPixel;
                                                                   
                            CurDrms = CurDrms / sq_BLOCKSIZE;

                            if (CurDrms < Drms)
                            {
                                Drms = CurDrms;
                                Parameter[CODENUMBER].DomainX = n;
                                Parameter[CODENUMBER].DomainY = m;
                                Parameter[CODENUMBER].TransformNo = t;
                                Parameter[CODENUMBER].Scale   = Scale;
                                Parameter[CODENUMBER].Offset  = Offset;	                                    
                            }
                        }
                    }
                }
                //����ʱ��(�ṹ��)Parameter[CODENUMBER]д���ļ�
                int p;
                float q;
                try
                {                    
                    rf.writeByte(i);
                    rf.writeByte(j);
                    p = Parameter[CODENUMBER].DomainY;
                    rf.writeByte(p);
                    p = Parameter[CODENUMBER].DomainX;
                    rf.writeByte(p);	                    
                    p = Parameter[CODENUMBER].TransformNo;
                    rf.writeByte(p);
                    q = Parameter[CODENUMBER].Scale;
                    rf.writeFloat(q);
                    q = Parameter[CODENUMBER].Offset;
                    rf.writeFloat(q);
                }
                catch(FileNotFoundException e1)
                {
                    e1.printStackTrace();

                    System.err.println(e1);
                }
                catch(IOException e2)
                {
                    e2.printStackTrace();

                    System.err.println(e2);
                }    
                num++;
                System.out.println((int)(num*100/1024)+"% completed");                    
            }
        }
        //������ʱ    
        ctime = Calendar.getInstance();
        curr_min = ctime.get(Calendar.MINUTE);
        curr_sec = ctime.get(Calendar.SECOND);
        //curr_mil = ctime.get(Calendar.MILLISECOND);
        
        int min = curr_min-start_min;
        int sec = curr_sec-start_sec;
        sec = sec >= 0 ? sec: 60+sec;
        //int mil = curr_mil-start_mil;
        //mil = mil >= 0 ? mil: 1000+mil;
        //System.out.println(min+"��"+sec+"��");
        JOptionPane.showMessageDialog(null,"ѹ��ʱ��:"+
	                     min+"��"+sec+"��");   
    }
    
    //��ѹ��
    public int[][] fracDecompress()
    {    	
        int IterateNum = 10;                       //�����������
        int i, j, k, l, m, n, t;
        double Scale, Offset;
        
        //���ͼ��o_Image[][]��ʼ��
        for (i = 0; i < IMAGESIZE; i++)
            for (j = 0; j < IMAGESIZE; j++)
                oImage[i][j] = 255;
                            
        for (int r = 0; r < IterateNum; r++)
        {               
            for (i = 0; i < DOMAINSIZE; i++)
            {
                for (j = 0; j < DOMAINSIZE; j++)
                {
                    Domain[i][j] = (oImage[i*2][j*2]   + 
                                    oImage[i*2+1][j*2] + 
                                    oImage[i*2][j*2+1] + 
                                    oImage[i*2+1][j*2+1]) / 4;
                }
            }
            
            try
  	        {
  	            FileInputStream fin = new FileInputStream(
  	            	                      "./images/compressed/compressed.frc");			  	    
  	            DataInputStream  in = new DataInputStream(fin);
                for (int s = 0; s < CODENUMBER; s++)
                {	                    
                    i = in.readByte();	                    
                    j = in.readByte();	                    
                    m = in.readByte();	                    
                    n = in.readByte();	                    
                    t = in.readByte();	                    
                    Scale  = in.readFloat();	                    
                    Offset = in.readFloat();
                    
                    //8�ֱ任
                    if (t == 0)
                    {
                        for (k = 0; k < BLOCKSIZE; k++)
                            for (l = 0; l < BLOCKSIZE; l++)
                                TransDom[k][l] = Domain[m+k][n+l];
                    }
                    else if (t == 1)
                    {
                        for (k = 0; k < BLOCKSIZE; k++)
                            for (l = 0; l < BLOCKSIZE; l++)
                                TransDom[k][l] = Domain[m+k][n+BLOCKSIZE-l-1];
                    }
                    else if (t == 2)
                    {
                        for (k = 0; k < BLOCKSIZE; k++)
                            for (l = 0; l < BLOCKSIZE; l++)
                                TransDom[k][l] = Domain[m+BLOCKSIZE-k-1][n+l];
                    }
                    else if (t == 3)
                    {
                        for (k = 0; k < BLOCKSIZE; k++)
                            for (l = 0; l < BLOCKSIZE; l++)
                                TransDom[k][l] = Domain[m+l][n+k];
                    }
                    else if (t == 4)
                    {
                        for (k = 0; k < BLOCKSIZE; k++)
                            for (l = 0; l < BLOCKSIZE; l++)
                                TransDom[k][l] = Domain[m+l][n+BLOCKSIZE-k-1];
                    }
                    else if (t == 5)
                    {
                        for (k = 0; k < BLOCKSIZE; k++)
                            for (l = 0; l < BLOCKSIZE; l++)
                                TransDom[k][l] = Domain[m+BLOCKSIZE-l-1]
                                                       [n+BLOCKSIZE-k-1];
                    }
                    else if (t == 6)
                    {
                        for (k = 0; k < BLOCKSIZE; k++)
                            for (l = 0; l < BLOCKSIZE; l++)
                                TransDom[k][l] = Domain[m+BLOCKSIZE-k-1]
                                                       [n+BLOCKSIZE-l-1];
                    }
                    else if (t == 7)
                    {
                        for (k = 0; k < BLOCKSIZE; k++)
                            for (l = 0; l < BLOCKSIZE; l++)
                                TransDom[k][l] = Domain[m+BLOCKSIZE-l-1][n+k];
                    }

                    for (k = 0; k < BLOCKSIZE; k++)
                    {
                        for (l = 0; l < BLOCKSIZE; l++)
                        {
                            int tem = (int)(Scale * TransDom[k][l] + Offset);
                            if (tem < 0) tem = 0;
                            else if (tem > 255) tem = 255;
                            oImage[i*BLOCKSIZE+k][j*BLOCKSIZE+l] = tem;
                        }
                    }
                }
            }
            catch(IOException e1){System.out.println("Exception!" + e1.getStackTrace());e1.printStackTrace();}
        }
        return oImage;	             
    }
    
    //=========================������ʾ=============================
    boolean kochflag = false;
    
    //Julia����
    public int[] JuliaSet (int iw, int ih, double p, double q)
    {
        double re, im, ze0 = 0.012; 
        int k, iternum = 100;                   //��������       
        
        im = -1.35; 
        int[] pix = new int[iw*ih];
        for (int j = 0; j < ih; j++) 
        {
            re = -1.8; 
            for (int i = 0; i < iw; i++) 
            {
            	//�������֣���Julia������Red
            	k = julia(re, im, iternum, p, q);
                if (k == iternum)                                  
                    pix[i+j*iw]=(255<<24)|(255<<16)|(0<<8)|0;
                //��ɢ���֣���ɫ��16��
                else
                {
                	k = k % 16;
                    pix[i+j*iw]=(255<<24)|(0<<16)|((255-k*16)<<8)|(255-k*16);
                }
                re = re + ze0; 
            }
            im = im + ze0; 
        }
        kochflag = false;
        return pix;
    }
    
    //u+j*v=(x+j*y)^2+p+j*q, j^2=-1
    public int julia(double x, double y, int num, double p, double q) 
    {
        double u, v;
        int i;
        for (i = 0; i < num; i++) 
        {
            u = x*x-y*y+p;
            v = 2*x*y+q;

            if (u*u+v*v > 4) return i;
            x = u;
            y = v;
        }
        return i;
    }
    
    public void Koch(Graphics g, double aX, double aY, double bX, double bY)
    {
        double c = 9,
              cX = 0, cY = 0, 
              dX = 0, dY = 0,
              eX = 0, eY = 0,        
              l  = 0, alpha = 0;
               
        if(!kochflag) 
        {
        	g.clearRect(0, 0, 530, 330);
        	g.setColor(Color.white);
        	g.fillRect(240, 50, 260, 250);
        	kochflag = true;
        }
        g.setColor(Color.red);
        if((bX - aX) * (bX - aX) + (bY - aY) * (bY - aY) < c)
            g.drawLine((int)aX+220, 300-(int) aY, (int)bX+220, 300-(int) bY);             
        else
        {
	        cX = aX + (bX - aX) / 3;
	        cY = aY + (bY - aY) / 3;
	        eX = bX - (bX - aX) / 3;
	        eY = bY - (bY - aY) / 3;
	        l  = Math.sqrt((eX - cX) * (eX - cX) + (eY - cY) * (eY - cY));
	        alpha = Math.atan((eY - cY) / (eX - cX));
	        if( (alpha >= 0) && ((eX - cX) < 0) || (alpha <= 0) && ((eX - cX) < 0)){ 
	            alpha = alpha + 3.14159;
	        }
	
	        dY = cY + Math.sin(alpha + 3.14159 / 3) * l;
	        dX = cX + Math.cos(alpha + 3.14159 / 3) * l;
	        
	        Koch(g, aX, aY, cX, cY);
	        Koch(g, eX, eY, bX, bY);
	        Koch(g, cX, cY, dX, dY);
	        Koch(g, dX, dY, eX, eY); 
        }       
    }
    
    public int[] Mandelbrot(int iw, int ih)
    {
    	double a, b;
    	double xmin = -2;
	    double xmax =  2;
	    double ymin = -2;
	    double ymax =  2;
	    int k, iternum = 100;
    	int[] pix   = new int[iw*ih];
    	
    	for (int j = 0; j < ih; j++)	    
	    {
	        for (int i = 0; i < iw; i++)
	        {  
		        a = xmin + i * (xmax - xmin) / iw;
		        b = ymin + j * (ymax - ymin) / ih;
		        k = escapes(a, b);
		        if (iternum == k)
		        	pix[i+j*iw]=(255<<24)|(255<<16)|(0<<8)|0;
		        else
		        {
		        	k = k % 16;
		        	pix[i+j*iw]=(255<<24)|(0<<16)|((255-k*16)<<8)|(255-k*16);		        	
		        }		        
	        }
	    }
	    kochflag = false;
	    return pix;
    }
    
    private int escapes(double a, double b)
    {  
	    double x = 0.0, y = 0.0;
	    int iter = 0;
	    int iternum = 100;
	    do 
	    {  
	        double xnew = x * x - y * y + a;
	        double ynew = 2 * x * y + b;
	        x = xnew;
	        y = ynew;
	        iter++;
	    }
	    while (x*x+y*y<=4&&iter<iternum);
	    return iter;
    }
    
    public int[] bfern(int iw, int ih)
	{
		int i, j, k = 0, m = 0;
		int[][] pix = new int[iw][ih];
		int[] pixs  = new int[iw*ih];
				
		double x, y,
		       u = 0.0, v = 0.1;
		double a[] = {0.0,  0.85,  0.20, -0.15};
		double b[] = {0.0,  0.04, -0.26,  0.28};
		double c[] = {0.0, -0.04,  0.23,  0.26};
		double d[] = {0.16, 0.85,  0.22,  0.24};
		double e[] = {0.0,  0.0,   0.0,   0.0};
		double f[] = {0.0,  1.6,   1.6,   0.44};
		int    p[] = {1, 85, 7, 7};
		
		do{
			i = (int)(100*Math.random());
			if((0 <= i)&&(i < p[0]))                     m = 0;
			if((p[0] <= i)&&(i < (p[0]+p[1])))           m = 1;
			if((p[0]+p[1] <= i)&&(i < (p[0]+p[1]+p[2]))) m = 2;
			if((p[0]+p[1]+p[2] <= i)&&( i< 100))         m = 3;			
		
			x = v ;                   
			y = 10 - u;
			v = a[m]*x+b[m]*y+e[m];
			u = c[m]*x+d[m]*y+f[m];
			u = 10 - u;
			k++;
			
			if(k > 20)
				pix[(int)(20*(v+2.2))+50][(int)(20*u)+40] = 1;											
		}while(k < 50000);
		
		for(j = 0; j < ih; j++)
		    for(i = 0; i < iw; i++)
		    	if(pix[i][j] == 1)
		    	    pixs[i+j*iw] = 255 << 24|0 << 16|0 << 8|255;
		    	else
		    	    pixs[i+j*iw] = 255 << 24|255 << 16|255 << 8|255;
		kochflag = false;
		return pixs;	
	}
	
	double map[][];
	
	private void setParam()
    {
	    map = new double[3][7];
	    	
	    map[0][0] = 0.5;   map[1][0] = 0.5;   map[2][0] = 0.5;
	    map[0][1] = 0.0;   map[1][1] = 0.0;   map[2][1] = 0.0;
	    map[0][2] = 0.0;   map[1][2] = 0.0;   map[2][2] = 0.0;
	    map[0][3] = 0.5;   map[1][3] = 0.5;   map[2][3] = 0.5;
	    map[0][4] = 0.0;   map[1][4] = 0;   map[2][4] = 0.5;
	    map[0][5] = 0.0;   map[1][5] = 0.5;   map[2][5] = 0.5;
	    map[0][6] = 0.333; map[1][6] = 0.333; map[2][6] = 0.334;
    }
    
    public byte[] sierpinski(byte[] t, int iw, int ih)
    {
    	int p, q;
    	int[] u = new int[2];
    	double[] v = new double[2];
    	byte[][] z = new byte[iw][ih];
    	
    	setParam();
    	
    	for(int j = 0; j < ih; j++)
	    {
	       for(int i = 0; i < iw; i++)
	       {
	       	   z[i][j] = t[i+j*iw];
	       	   t[i+j*iw] = 0;
	       }
	    }	    
	               
		for(int j = 0; j < ih; j++)
	    {
	       for(int i = 0; i < iw; i++)
	       {	
	            if(z[i][j] == 0) continue;	            
			    
			    u[0] = i; u[1] = j;	            
	            for(int l = 0; l < 3; l++)
	            {
	                v = sier(u, l, iw, ih); 	                
	                p = (int)v[0]; 
	                q = (int)v[1];   
	                t[p+q*iw] = 1;	                	                
		        }		        
	        }
	    }
	    kochflag = false;	    
	    return t;
    }

    private double[] sier(int[] x, int m, int w, int h)
    {
    	double[] y = new double[2];
    	y[0] = map[m][0] * x[0] + map[m][1] * x[1] + map[m][4] * w;
		y[1] = map[m][2] * x[0] + map[m][3] * x[1] + map[m][5] * h;		
		return y;
    }           
}

