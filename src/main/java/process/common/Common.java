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
	//关于图像文件和像素--------------------------    
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
	
	//设置默认目录,过滤文件
	public void chooseFile(JFileChooser jfc, String str, int n)
    {
    	final int type = n;    
        //设置默认文件目录
        jfc.setCurrentDirectory(new File(str));
        
        jfc.setFileFilter(new javax.swing.filechooser.FileFilter()
        {  
            public boolean accept(File f)
            {  
                String name = f.getName().toLowerCase();
                
                if(type == 0)                  //Java支持格式
                {
                    return name.endsWith(".gif")
	                || name.endsWith(".jpg")                
	                || name.endsWith(".png")
	                || f.isDirectory();	
                }
                else if(type == 1)             //仅RAW
                {
                	return name.endsWith(".raw")
	                || f.isDirectory();
                } 
                else if(type == 2)             //仅PGM,RAW
                {
                	return name.endsWith(".pgm")
	                || name.endsWith(".raw")
	                || f.isDirectory();
                }
                else if(type == 3)             //包含RAW和内置方法
                {                
	                return name.endsWith(".gif")
	                || name.endsWith(".jpg")                
	                || name.endsWith(".png")
	                || name.endsWith(".raw")
	                || f.isDirectory();
                } 
                else                          //包含BMP,PGM和内置方法
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
                    return "图像(*.gif, *.jpg, *.png)";
            	else if(type == 1)
                    return "图像(*.raw)";
                else if(type == 2)
                    return "图像(*.pgm, *.raw)";
            	else if(type == 3)
                    return "图像(*.gif, *.jpg, *.png, *.raw)";
                else 
                    return "图像(*.bmp, *.gif, *.jpg, *.pgm, *.png, *.ppm, *.raw)";                
            }
        });        
    }
    
    //打开图像
	public Image openImage(String is, MediaTracker tracker )
	{
		Image im = null;
				
		//用MediaTracker跟踪图像加载
		im = Toolkit.getDefaultToolkit().getImage(is);
		tracker.addImage(im,0);
	
		//等待图像完成加载
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
    
    //一维double数据转变为图像序列
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
    
    //图像序列pix阈值分割	
	public int[] thSegment(int[] pix, int iw, int ih, int th)
	{						
		int[] im = new int[iw*ih];
		int t;
		for(int i = 0; i < iw*ih; i++)	
		{
			t = pix[i]&0xff;
								
			if(t > th) 
				im[i] = (255<<24)|(255<<16)|(255<<8)|255;//背景色
			else
			    im[i] = (255<<24)|(0<<16)|(0<<8)|0;      //前景色为		
		}
		return im;
	}
	
	//将RGB图像序列pix变为2值图像序列im
	public byte[] rgb2Bin(int[] pix, int iw, int ih)
	{
		byte[] im = new byte[iw*ih];
		
		for(int i = 0; i < iw*ih; i++)			
			if((pix[i]&0xff) > 128) 
			    im[i] = 0;   //背景色为0
			else
			    im[i] = 1;   //前景色为1	
		return im;
	}
	
	//将ARGB图像序列pixels变为2值图像序列im
	public byte[] toBinary(int[] pix, int iw, int ih)
	{
		byte[] im = new byte[iw*ih];
		for(int i = 0; i < iw*ih; i++)							
			if((pix[i]&0xff) > 128) 
				im[i] = 0;   //背景色为0
			else
			    im[i] = 1;   //前景色为1			
		return im;
	}
    
    //将2值图像序列bw变为RGB图像序列pix
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
	
	//将字节数组pix转化为图像序列pixels
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
	
	//将2值图像矩阵image2变为ARGB图像序列pixels
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
		
		//用红色小方块标注链码起始点
		if(x!=-1 && y!=-1)
		{		
			for(int i=-2; i<3; i++)
			   for(int j=-2; j<3; j++)
			       pix[(x+i)+(y+j)*iw] = (255<<24)|(255<<16)|(0<<8)|0;
		}
		return pix;	
	}
	
	//将2值图像矩阵image2变为ARGB图像序列pixels
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
		
		//用红色小方块标注链码起始点
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
	
	//基本计算------------------------------------
	
	//计算数组d最大值
    public int maximum(int[] d)
    {
    	int m = 0;
    	for(int i = 0;i < 256; i++)
    	    if(d[i]> m) m = d[i];
    	return m;
    }
    
    //数组h规范化为[0,255]
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
	 *计算峰值信噪比
	 *pin1[] -- 原图像矩阵
	 *pix2[] -- 输出图像矩阵
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
		
    //显示图像和数据-----------------------------------
        
    //显示一幅图和标题
    public void draw(Graphics g, Image om, int x, int y, String str)
    { 	
		g.clearRect(0, 0, 530, 330);
	    g.drawImage(om, x, y, null);	        
	    g.setColor(Color.red);
	    g.setFont(new Font("",Font.BOLD, 12));
	    g.drawString(str, x+100, 320);	    
    }
    
    //显示一幅图和标题与参数(int)
    public void draw(Graphics g, Image om, String str, int n)
    {    	
		g.clearRect(0, 0, 530, 330);
	    g.drawImage(om, 5, 50, null);	        
	    g.setColor(Color.red);
	    g.setFont(new Font("",Font.BOLD, 18));
	    g.drawString(str + n, 320, 160); 
    }
    
    //显示一幅图和标题与参数(float)
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
		g.drawString("类型:             " + a0 + a1, 300, 120);
    	g.drawString("宽度:             " + iw, 300, 140);
    	g.drawString("高度:             " + ih, 300, 160);
    	g.drawString("最大灰度值: " + maxpix, 300, 180);
    }
    
    //显示一幅图，数组和标题
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
    
    //显示一幅图与数据
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
     * 显示两幅图与第2幅图像标题
     * im   -- 输入图
     * istr -- 输入图标题
     * om   -- 输出图
     * ostr -- 输出图标题
     *************************************/    
    public void draw(Graphics g, Image im, String istr, Image om, String ostr)
    {    	  	
		g.clearRect(0, 0, 530, 350);        	
        g.drawImage(im, 5,   50, null);
        g.drawImage(om, 270, 50, null);
    	g.drawString(istr, 120, 320);
    	g.drawString(ostr, 380, 320);    	
    }
    
    //显示两幅图,带有类型type
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
    		g.drawString("红色圆心处为旋转中心", 20,320);    
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
    		g.drawString("红色圆心处为错切基点", 20,320);
    	} 
    	else if(type == 7)
    	{
    		g.clearRect(0, 0, 530, 330);  
    	    g.drawImage(im, 50, 80, null);
    	    g.drawImage(om, 50, 80, null);
    	} 	
    }       
    
    //显示两幅图、数据和说明
    public void drawIm(Graphics g, Image image, Image oImage, 
                       int startX, int startY, int sc1, int sc0)
    {    	
    	g.drawImage(image, 5, 50, null);
    	g.drawImage(oImage, 270, 50, null);
    	g.setColor(Color.red);
       	g.fillOval(3+startX, 48+ startY, 4, 4);
       	           	
	    g.drawString("起始点: ("+startX+","+startY+")",10,280);
	    g.drawString("链码见DOS屏幕",10, 300);
	    g.drawString("新起点: ("+sc1+","+sc0+")",280,280);
	    g.drawString("规范链码见DOS屏幕",280, 300);    	        
    }
    
    /*----------------------------------------*
     * x, y  -- 水平和垂直方向变换参数
     * type  -- 旋转:0, 缩放:1, 错切:2, 平移:3 
     *----------------------------------------*/
    public void draw(Graphics g, Image iImage, BufferedImage bImage, 
                     float x, float y, int type) 
    {  	  
       	g.clearRect(0, 0, 530, 330);
    	
    	Graphics2D g2D = (Graphics2D)g;               	       	
    	g2D.translate(50, 80);           //设置图像左上角为当前点
    	g.drawImage(iImage, 0, 0, null); //画输入图
    	
    	if(type == 0)
    	    g2D.rotate(x * Math.PI/180);//当前点为中心顺时针旋转t度        	 
    	if(type == 1)
    	    g2D.scale(x, y);            //缩放 
    	if(type == 2)
   	        g2D.shear(x, y);            //错切
    	if(type == 3)        	    
    	    g2D.translate(x, y);        //平移
    	       	
    	g2D.drawImage(bImage, 0, 0, null);//画输出图像  
    }
    
    //显示直方图
    public void draw(Graphics g, int[] h, int max)
    {
    	g.clearRect(270, 0, 530, 350);    	
    	g.drawLine(270, 306, 525, 306); //x轴
    	g.drawLine(270, 50,  270, 306); //y轴
    	for(int i = 0; i < 256; i++)
    	    g.drawLine(270+i, 306, 270+i, 306-h[i]);
    	g.drawString(""+max, 275, 60);
       	g.drawString("直方图", 370, 320);
    }  
    
    //显示数据
    public void draw(Graphics g, int x1, int y1, int x2, int y2, String str)
    {
    	g.clearRect(270,50,526,306);
    	g.setColor(Color.red);
    	g.drawLine(270,305,525,305);            //x轴
	    g.drawLine(270,305,270,50);             //y轴
	    g.drawLine(270,305,270+x1,305-y1);      //(0,0)-(x1,y1) 
	    g.drawLine(270+x1,305-y1,270+x2,305-y2);//(x1,y1)-(x2,y2)
	    g.drawLine(270+x2,305-y2,525,50);       //(x2,y2)-(255,255)
	    g.setColor(Color.BLUE);
	    g.drawString("(x1,y1)",255+x1,290-y1);
	    g.drawString("(x2,y2)",255+x2,325-y2);
	    g.drawString(str,340,70);
    }
    
    //其它======================================
    
    //存储JPEG图像 
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
     * 获取参数
     * str -- 标题
     * tmp -- 默认值字符串
     *******************************************/
	public int getParam(String str, Object tmp)
	{
		int param = 150;
		String st = JOptionPane.showInputDialog(null, str, tmp);
		if(st != null)
			param = Integer.parseInt(st); //将字符串转变为数字
		return param;		
	}
	
	/*******************************************
     * 获取参数
     * str -- 标题
     * tmp -- 默认值字符串
     *******************************************/
	public int getKey(String str, Object tmp)
	{
		int num = 150;
		String st = JOptionPane.showInputDialog(null, str, tmp);
		if(st != null)
			num = Integer.parseInt(st); //将字符串转变为数字
		
		
		int k1 = (int)(num / 100);
        int k2 = (int)((num % 100) / 10);
        int k3 = num % 10;	
        int key = k1 + k2 + k3;
		return key;		
	}
	
	//获取初始值
	public double getStartValue()
	{
		double dValue = 0.777;
		Object tmpValue = "0.235";             //默认值为888
		String aValue = JOptionPane.showInputDialog(null, 
		                     "输入初始值(0.001-0.999)", tmpValue);
		//将字符串转变为数字
		if(aValue != null)
			dValue = (Double.valueOf(aValue)).doubleValue(); 		
		return dValue;		
	}	
}
