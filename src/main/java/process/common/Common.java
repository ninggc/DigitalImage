 /**
 * @Common.java
 * @Version 1.0 2010.03.05
 * @Author Xie-Hua Sun 
 */
 
package process.common;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import com.sun.image.codec.jpeg.*;

public class Common extends Frame
{
	//����ͼ���ļ�������--------------------------    
    public int[] grabber(Image im, int iw, int ih)
	{
		int [] pix = new int[iw * ih];
		try
		{
//			file:///D:/Something/Manual/java%201.6%20api/index.html
		    PixelGrabber pg = new PixelGrabber(im, 0, 0, iw,  ih, pix, 0, iw);
		    pg.grabPixels();
		}
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}	
		return pix;
	}
	
	//����Ĭ��Ŀ¼,�����ļ�
	public void chooseFile(JFileChooser jfc, String str, int n)
    {
    	final int type = n;    
        //����Ĭ���ļ�Ŀ¼
        jfc.setCurrentDirectory(new File(str));
        
        jfc.setFileFilter(new javax.swing.filechooser.FileFilter()
        {  
            public boolean accept(File f)
            {  
                String name = f.getName().toLowerCase();
                
                if(type == 0)                  //Java֧�ָ�ʽ
                {
                    return name.endsWith(".gif")
	                || name.endsWith(".jpg")                
	                || name.endsWith(".png")
	                || f.isDirectory();	
                }
                else if(type == 1)             //��RAW
                {
                	return name.endsWith(".raw")
	                || f.isDirectory();
                } 
                else if(type == 2)             //��PGM,RAW
                {
                	return name.endsWith(".pgm")
	                || name.endsWith(".raw")
	                || f.isDirectory();
                }
                else if(type == 3)             //����RAW�����÷���
                {                
	                return name.endsWith(".gif")
	                || name.endsWith(".jpg")                
	                || name.endsWith(".png")
	                || name.endsWith(".raw")
	                || f.isDirectory();
                } 
                else                          //����BMP,PGM�����÷���
                {                
	                return name.endsWith(".bmp")
	                || name.endsWith(".gif")
	                || name.endsWith(".jpg")
	                || name.endsWith(".pgm")                
	                || name.endsWith(".png")
	                || name.endsWith(".ppm")
	                || name.endsWith(".raw")
	                || f.isDirectory();
                }               
            }
            
            public String getDescription() 
            {       	
            	if(type == 0)
                    return "ͼ��(*.gif, *.jpg, *.png)";
            	else if(type == 1)
                    return "ͼ��(*.raw)";
                else if(type == 2)
                    return "ͼ��(*.pgm, *.raw)";
            	else if(type == 3)
                    return "ͼ��(*.gif, *.jpg, *.png, *.raw)";
                else 
                    return "ͼ��(*.bmp, *.gif, *.jpg, *.pgm, *.png, *.ppm, *.raw)";                
            }
        });        
    }
    
    //��ͼ��
	public Image openImage(String is, MediaTracker tracker )
	{
		Image im = null;
				
		//��MediaTracker����ͼ�����
		im = Toolkit.getDefaultToolkit().getImage(is);
		tracker.addImage(im,0);
	
		//�ȴ�ͼ����ɼ���
		try	{ tracker.waitForID(0);	}
 		catch(InterruptedException e){ e.printStackTrace();}
		return im;
	}
    
    public int[] toPixels(int[][] pix, int iw, int ih)
    {
    	int[] pi = new int[iw*ih];
    	    	 
    	for(int j = 0; j < ih; j++)
    	{
    		for(int i = 0; i < iw; i++)
    		{
    			int c = pix[i][j];
    			if(c < 0) c = c + 256;
    			pi[i+j*iw] = (255<<24)|(c<<16)|(c<<8)|c;                			
    		}            
        } 
        return pi;
    }
    
    
    public int[] toPixels(double[][] inIm, int iw, int ih)
    {
    	int r;
        int[] pix = new int[iw*ih];
        for (int j = 0; j < ih; j++)
        {
            for (int i = 0; i < iw; i++)
            {
                r = (int)inIm[i][j];
                if (r < 0) r = 0;
                else if (r > 255) r = 255;
                pix[i + j * iw] = 255 << 24|r << 16|r << 8|r;                
            }
        }
        return pix;
    }
    
    //һάdouble����ת��Ϊͼ������
    public int[] toPixels(double[] inIm, int iw, int ih)
    {
    	int r;
        int[] pix = new int[iw*ih];
        for (int j = 0; j < ih; j++)
        {
            for (int i = 0; i < iw; i++)
            {
                r = (int)inIm[i+j*iw];
                if (r < 0) r = 0;
                else if (r > 255) r = 255;
                pix[i + j * iw] = 255 << 24|r << 16|r << 8|r;                
            }
        }
        return pix;
    }
    
    //ͼ������pix��ֵ�ָ�	
	public int[] thSegment(int[] pix, int iw, int ih, int th)
	{						
		int[] im = new int[iw*ih];
		int t;
		for(int i = 0; i < iw*ih; i++)	
		{
			t = pix[i]&0xff;
								
			if(t > th) 
				im[i] = (255<<24)|(255<<16)|(255<<8)|255;//����ɫ
			else
			    im[i] = (255<<24)|(0<<16)|(0<<8)|0;      //ǰ��ɫΪ		
		}
		return im;
	}
	
	//��RGBͼ������pix��Ϊ2ֵͼ������im
	public byte[] rgb2Bin(int[] pix, int iw, int ih)
	{
		byte[] im = new byte[iw*ih];
		
		for(int i = 0; i < iw*ih; i++)			
			if((pix[i]&0xff) > 128) 
			    im[i] = 0;   //����ɫΪ0
			else
			    im[i] = 1;   //ǰ��ɫΪ1	
		return im;
	}
	
	//��ARGBͼ������pixels��Ϊ2ֵͼ������im
	public byte[] toBinary(int[] pix, int iw, int ih)
	{
		byte[] im = new byte[iw*ih];
		for(int i = 0; i < iw*ih; i++)							
			if((pix[i]&0xff) > 128) 
				im[i] = 0;   //����ɫΪ0
			else
			    im[i] = 1;   //ǰ��ɫΪ1			
		return im;
	}
    
    //��2ֵͼ������bw��ΪRGBͼ������pix
	public int[] bin2Rgb(byte[] bw, int iw, int ih)
	{
		int r, g, b;
		int[] pix = new int[iw * ih];
		
	    for(int i = 0; i < iw*ih; i++) 
		{		
			if(bw[i] == 0)	    {r = 255; g= 255;b = 255;}
			else if(bw[i] == 1)	{r = 0;   g = 0; b = 0;}
			else				{r = 255; g = 0; b = 0;}				
			pix[i] = (255<<24)|(r<<16)|(g<<8)|b;			
		}
		return pix;	
	}
	
	//���ֽ�����pixת��Ϊͼ������pixels
    public int[] byte2int(byte[][] pix, int iw, int ih)
    {
    	int[] p = new int[iw*ih];
    	    	 
    	for(int j = 0; j < ih; j++)
    	{
    		for(int i = 0; i < iw; i++)
    		{
    			int c = pix[i][j];
    			if(c < 0) c = c + 256;
    			p[i+j*iw] = (255<<24)|(c<<16)|(c<<8)|c;                			
    		}            
        } 
        return p;
    }
	
	//��2ֵͼ�����image2��ΪARGBͼ������pixels
	public int[] toARGB(int[] bw, int iw, int ih)
	{
		int r;
		int[] pix = new int[iw * ih];
		
	    for(int i = 0;i < iw*ih; i++) 
		{						
			if(bw[i] == 0)	r = 255;				
			else			r = 0;								
			pix[i] = (255<<24)|(r<<16)|(r<<8)|r;			
		}
		
		int x = bw[iw*ih+2];
		int y = bw[iw*ih+3];
		
		//�ú�ɫС�����ע������ʼ��
		if(x!=-1 && y!=-1)
		{		
			for(int i=-2; i<3; i++)
			   for(int j=-2; j<3; j++)
			       pix[(x+i)+(y+j)*iw] = (255<<24)|(255<<16)|(0<<8)|0;
		}
		return pix;	
	}
	
	//��2ֵͼ�����image2��ΪARGBͼ������pixels
	public int[] toARGB(byte[] bw, int x, int y, int iw, int ih)
	{
		int r;
		int[] pix = new int[iw * ih];
		
	    for(int i = 0;i < iw*ih; i++) 
		{						
			if(bw[i] == 0)	r = 255;				
			else			r = 0;								
			pix[i] = (255<<24)|(r<<16)|(r<<8)|r;			
		}
		
		//�ú�ɫС�����ע������ʼ��
		if(x!=-1 && y!=-1)
		{		
			for(int i=-2; i<3; i++)
			   for(int j=-2; j<3; j++)
			       pix[(x+i)+(y+j)*iw] = (255<<24)|(255<<16)|(0<<8)|0;
		}
		return pix;	
	}
	
	public int[] byte2ARGB(byte[][] bw, int iw, int ih)
	{
		int[] pix = new int[iw * ih];
		int r, g, b;
		
	    for(int j = 0;j < ih; j++) 
		{
			for(int i = 0; i < iw; i++)
			{	
				if(bw[i][j] == 0)
				{					
					r = 255;
					g = 255;
					b = 255;
				}
				else
				{					
					r = 255;
					g = 0;
					b = 0;
				}				
				pix[i+j*iw] = (255<<24)|(r<<16)|(g<<8)|b;
			}
		}
		return pix;	
	}
	
	//��������------------------------------------
	
	//��������d���ֵ
    public int maximum(int[] d)
    {
    	int m = 0;
    	for(int i = 0;i < 256; i++)
    	    if(d[i]> m) m = d[i];
    	return m;
    }
    
    //����h�淶��Ϊ[0,255]
    public int[] normalize(int[] h, int max)
    {
    	for(int i = 0; i < 255; i++)
    	    h[i] = (int)(h[i] * 255 / max);
    	return h;
    } 
    
    public int[] getHist(int[] pix, int iw, int ih)
	{	
		int[] hist = new int[256];		
		for(int i = 0; i < iw*ih; i++) 
		{
			int grey = pix[i]&0xff;
			hist[grey]++;			
		}
		return hist;
	}
	
	/*---------------------------------------------*
	 *�����ֵ�����
	 *pin1[] -- ԭͼ�����
	 *pix2[] -- ���ͼ�����
	 *---------------------------------------------*/	 
    public double PSNR(int[] pix1, int[] pix2, int iw, int ih)
    {
		double err, temp, psnr;
	
		err = 0.0;
		for(int i = 0; i < iw*ih; i++)
		{				
				temp = (pix1[i]&0xff) - (pix2[i]&0xff);
				err += temp * temp;	
		}
	    err = Math.sqrt(err);
	    System.out.print(Math.log(10));
	    psnr = 20*Math.log(255*255/err) / Math.log(10);
		return psnr;		
    }	 
		
    //��ʾͼ�������-----------------------------------
        
    //��ʾһ��ͼ�ͱ���
    public void draw(Graphics g, Image om, int x, int y, String str)
    { 	
		g.clearRect(0, 0, 530, 330);
	    g.drawImage(om, x, y, null);	        
	    g.setColor(Color.red);
	    g.setFont(new Font("",Font.BOLD, 12));
	    g.drawString(str, x+100, 320);	    
    }
    
    //��ʾһ��ͼ�ͱ��������(int)
    public void draw(Graphics g, Image om, String str, int n)
    {    	
		g.clearRect(0, 0, 530, 330);
	    g.drawImage(om, 5, 50, null);	        
	    g.setColor(Color.red);
	    g.setFont(new Font("",Font.BOLD, 18));
	    g.drawString(str + n, 320, 160); 
    }
    
    //��ʾһ��ͼ�ͱ��������(float)
    public void draw(Graphics g, Image om, String str, float p)
    {    	
		g.clearRect(0, 0, 530, 330);
	    g.drawImage(om, 5, 50, null);	        
	    g.setColor(Color.red);
	    g.drawString(str+"(p = "+ p + ")", 20, 320); 
    }
    
    public void draw(Graphics g, Image im, char a0, char a1, 
                     int iw, int ih, int maxpix)
    {
    	g.clearRect(0,  0, 530, 350);    	
		g.drawImage(im, 5, 50, null);
		g.setColor(Color.blue);
		g.setFont(new Font("",Font.BOLD, 16));
		g.drawString("����:             " + a0 + a1, 300, 120);
    	g.drawString("���:             " + iw, 300, 140);
    	g.drawString("�߶�:             " + ih, 300, 160);
    	g.drawString("���Ҷ�ֵ: " + maxpix, 300, 180);
    }
    
    //��ʾһ��ͼ������ͱ���
    public void draw(Graphics g, Image im, double[] inv, String title)
    {
    	g.clearRect(0, 0, 530, 350);    	
		g.drawImage(im, 5, 50, null);
	    g.setColor(Color.blue);
    	g.drawString("phi0 = "+inv[0], 300, 120);
    	g.drawString("phi1 = "+inv[1], 300, 140);
    	g.drawString("phi2 = "+inv[2], 300, 160);
    	g.drawString("phi3 = "+inv[3], 300, 180);
    	g.drawString("phi4 = "+inv[4], 300, 200);
    	g.drawString("phi5 = "+inv[5], 300, 220);
    	g.drawString("phi6 = "+inv[6], 300, 240);  
	    g.drawString(title, 360, 320); 
    }
    
    //��ʾһ��ͼ������
    public boolean draw(Graphics g, Image iImage, String ents, double entr, 
                         String avrs, double aver, String meds, double medn, 
                         String sqss, double sqsm) 
    {  	  
        g.clearRect(0, 0, 530, 350);
    	g.setColor(Color.blue);
    	g.setFont(new Font("",Font.BOLD, 18));
    	g.drawString(ents, 270, 80);
    	g.drawString(""+entr, 270, 100); 
    	g.drawString(avrs, 270, 140);
    	g.drawString(""+aver, 270, 160);  
    	g.drawString(meds, 270, 200);
    	g.drawString(""+medn, 270, 220); 
    	g.drawString(sqss, 270, 260);
    	g.drawString(""+sqsm, 270, 280); 
    	g.drawImage(iImage, 5, 50, null);
    	return true;         	
    }
    
    public void draw(Graphics g, float mu, float t)
    {
    	g.clearRect(0, 0, 530, 350);
    	g.drawLine(30,280,230,280);
        g.drawLine(30,280,30,80);
        g.drawLine(30,80 ,230, 80);
        g.drawLine(230,280,230,80);
        double x, y;
        
        double u = 0.0; 
        double v = 0.0;
        
        //double p = mu;
        
        //draw curve y = 2.8 * x * (1-x)
        for(int i = 1; i < 1000; i++)
        {
        	x = i / 1000.0;
        	y = mu * x * (1 - x);
        	
        	u = x; v = y;
        	g.drawLine((int)(200*u+30),(int)(280-200*v),
        	           (int)(30+200*x),(int)(280-200*y));
        }
        
        //draw line y = x
        g.drawLine(30, 280, 230, 80);
        
        //*** TESTING ATRACTOR ***
        u = t;
        v = 0.0;
        x = t;
        
        for(int i = 0; i< 20;i++)
        {
        	y = mu * x * (1 - x);
        	g.drawLine((int)(200*u+30), (int)(280-200*v), 
        	           (int)(30+200*x), (int)(280-200*y));
        	g.drawLine((int)(30+200*x), (int)(280-200*y), 
        	           (int)(200*y+30), (int)(280-200*y));
        	u = y; v = y;
        	x = y;
        }
        g.setColor(Color.red);
        g.drawString("0", 25, 300);
        g.drawString("1", 225, 300);
        g.drawString(""+t, 15+(int)(200*t), 300); 
    }
    
    /*************************************
     * ��ʾ����ͼ���2��ͼ�����
     * im   -- ����ͼ
     * istr -- ����ͼ����
     * om   -- ���ͼ
     * ostr -- ���ͼ����
     *************************************/    
    public void draw(Graphics g, Image im, String istr, Image om, String ostr)
    {    	  	
		g.clearRect(0, 0, 530, 350);        	
        g.drawImage(im, 5,   50, null);
        g.drawImage(om, 270, 50, null);
    	g.drawString(istr, 120, 320);
    	g.drawString(ostr, 380, 320);    	
    }
    
    //��ʾ����ͼ,��������type
    public void draw(Graphics g, Image im, Image om, 
                     int iw, int ih, int owh, int type)
    {    	  	
		if(type == 4)
    	{
    		g.clearRect(0, 0, 530, 330);
    		g.drawImage(im, 175-iw/2, 175-ih/2, null);
    		g.translate(175-owh/2, 175-owh/2); 
    		g.drawImage(om, 0, 0, null);
    		g.translate(-175+owh/2, -175+owh/2);
    		g.setColor(Color.red);
    		g.drawOval(173, 173, 4, 4);	
    		g.drawOval(172, 172, 6, 6);	
    		g.drawString("��ɫԲ�Ĵ�Ϊ��ת����", 20,320);    
    	}
    	else if(type == 5)
    	{
    	    g.clearRect(0, 0, 530, 330);
            g.drawImage(om, 50, 80, null);	
    	}
    	else if(type == 6)
    	{
    		g.clearRect(0, 0, 530, 330);
    		g.drawImage(im, 50, 80, null);	
    	    g.drawImage(om, 50, 80, null);
		    g.setColor(Color.red);
    		g.drawOval(48, 78, 4, 4);
    		g.drawOval(47, 77, 6, 6);
    		g.drawString("��ɫԲ�Ĵ�Ϊ���л���", 20,320);
    	} 
    	else if(type == 7)
    	{
    		g.clearRect(0, 0, 530, 330);  
    	    g.drawImage(im, 50, 80, null);
    	    g.drawImage(om, 50, 80, null);
    	} 	
    }       
    
    //��ʾ����ͼ�����ݺ�˵��
    public void drawIm(Graphics g, Image image, Image oImage, 
                       int startX, int startY, int sc1, int sc0)
    {    	
    	g.drawImage(image, 5, 50, null);
    	g.drawImage(oImage, 270, 50, null);
    	g.setColor(Color.red);
       	g.fillOval(3+startX, 48+ startY, 4, 4);
       	           	
	    g.drawString("��ʼ��: ("+startX+","+startY+")",10,280);
	    g.drawString("�����DOS��Ļ",10, 300);
	    g.drawString("�����: ("+sc1+","+sc0+")",280,280);
	    g.drawString("�淶�����DOS��Ļ",280, 300);    	        
    }
    
    /*----------------------------------------*
     * x, y  -- ˮƽ�ʹ�ֱ����任����
     * type  -- ��ת:0, ����:1, ����:2, ƽ��:3 
     *----------------------------------------*/
    public void draw(Graphics g, Image iImage, BufferedImage bImage, 
                     float x, float y, int type) 
    {  	  
       	g.clearRect(0, 0, 530, 330);
    	
    	Graphics2D g2D = (Graphics2D)g;               	       	
    	g2D.translate(50, 80);           //����ͼ�����Ͻ�Ϊ��ǰ��
    	g.drawImage(iImage, 0, 0, null); //������ͼ
    	
    	if(type == 0)
    	    g2D.rotate(x * Math.PI/180);//��ǰ��Ϊ����˳ʱ����תt��        	 
    	if(type == 1)
    	    g2D.scale(x, y);            //���� 
    	if(type == 2)
   	        g2D.shear(x, y);            //����
    	if(type == 3)        	    
    	    g2D.translate(x, y);        //ƽ��
    	       	
    	g2D.drawImage(bImage, 0, 0, null);//�����ͼ��  
    }
    
    //��ʾֱ��ͼ
    public void draw(Graphics g, int[] h, int max)
    {
    	g.clearRect(270, 0, 530, 350);    	
    	g.drawLine(270, 306, 525, 306); //x��
    	g.drawLine(270, 50,  270, 306); //y��
    	for(int i = 0; i < 256; i++)
    	    g.drawLine(270+i, 306, 270+i, 306-h[i]);
    	g.drawString(""+max, 275, 60);
       	g.drawString("ֱ��ͼ", 370, 320);
    }  
    
    //��ʾ����
    public void draw(Graphics g, int x1, int y1, int x2, int y2, String str)
    {
    	g.clearRect(270,50,526,306);
    	g.setColor(Color.red);
    	g.drawLine(270,305,525,305);            //x��
	    g.drawLine(270,305,270,50);             //y��
	    g.drawLine(270,305,270+x1,305-y1);      //(0,0)-(x1,y1) 
	    g.drawLine(270+x1,305-y1,270+x2,305-y2);//(x1,y1)-(x2,y2)
	    g.drawLine(270+x2,305-y2,525,50);       //(x2,y2)-(255,255)
	    g.setColor(Color.BLUE);
	    g.drawString("(x1,y1)",255+x1,290-y1);
	    g.drawString("(x2,y2)",255+x2,325-y2);
	    g.drawString(str,340,70);
    }
    
    //����======================================
    
    //�洢JPEGͼ�� 
    public static void saveImageAsJPEG(BufferedImage image, File file)
    {  	   
        try 
        {   
	        OutputStream out = new FileOutputStream(file);   
	        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);   
	        JPEGEncodeParam  param   = encoder.getDefaultJPEGEncodeParam(image);   
	        param.setQuality(1.0f, false);   
	        encoder.encode(image, param);   
	        out.flush();   
	        out.close();   
        }   
        catch(IOException e)
        {
        	e.printStackTrace();
            System.out.println(e);   
        }   
    }
    
    /*******************************************
     * ��ȡ����
     * str -- ����
     * tmp -- Ĭ��ֵ�ַ���
     *******************************************/
	public int getParam(String str, Object tmp)
	{
		int param = 150;
		String st = JOptionPane.showInputDialog(null, str, tmp);
		if(st != null)
			param = Integer.parseInt(st); //���ַ���ת��Ϊ����
		return param;		
	}
	
	/*******************************************
     * ��ȡ����
     * str -- ����
     * tmp -- Ĭ��ֵ�ַ���
     *******************************************/
	public int getKey(String str, Object tmp)
	{
		int num = 150;
		String st = JOptionPane.showInputDialog(null, str, tmp);
		if(st != null)
			num = Integer.parseInt(st); //���ַ���ת��Ϊ����
		
		
		int k1 = (int)(num / 100);
        int k2 = (int)((num % 100) / 10);
        int k3 = num % 10;	
        int key = k1 + k2 + k3;
		return key;		
	}
	
	//��ȡ��ʼֵ
	public double getStartValue()
	{
		double dValue = 0.777;
		Object tmpValue = "0.235";             //Ĭ��ֵΪ888
		String aValue = JOptionPane.showInputDialog(null, 
		                     "�����ʼֵ(0.001-0.999)", tmpValue);
		//���ַ���ת��Ϊ����
		if(aValue != null)
			dValue = (Double.valueOf(aValue)).doubleValue(); 		
		return dValue;		
	}	
}
