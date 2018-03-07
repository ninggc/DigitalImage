package com.ninggc; /**
 * @Ch5ImageTrans.java
 * @Version 1.0 2010.02.15
 * @Author Xie-Hua Sun 
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import process.common.Common;
import process.algorithms.imageTrans.*;

public class Ch5ImageTrans extends JFrame implements ActionListener
{
    Image iImage, oImage;
    
    int iw, ih;
    int bsize = 128;              //设置128X128块DCT变换
    int[] pixels;          
    Complex[] fftData;
    double[][] dctData;
    double[] dwtData;
    double[] W;
       
    boolean loadflag  = false,
            runflag   = false;    //图像处理执行标志 
            
    Common common;
    
    public Ch5ImageTrans()
    {    
        setTitle("数字图像处理-Java编程与实验 第5章 图像时频变换");
        this.setBackground(Color.lightGray);        
              
        //菜单界面
        setMenu();
        
        common = new Common();
        
        //关闭窗口
        closeWin();
        
        setSize(530, 330);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent evt)
    {
    	Graphics graph = getGraphics();
    	 
        if (evt.getSource() == openItem) 
        {
        	//文件选择对话框
            JFileChooser chooser = new JFileChooser();
            common.chooseFile(chooser, "./images", 0);//设置默认目录,过滤文件
            int r = chooser.showOpenDialog(null);
                        
            MediaTracker tracker = new MediaTracker(this);
            if(runflag)
            { 
                loadflag = false;
                runflag  = false;
            }
            
            if(r == JFileChooser.APPROVE_OPTION) 
            {  
                String name = chooser.getSelectedFile().getAbsolutePath();
                
			    if(!loadflag)
			    {
	                //装载图像
				    iImage = common.openImage(name,tracker);    
				    //取载入图像的宽和高
				    iw = iImage.getWidth(null);
				    ih = iImage.getHeight(null);				    
				    repaint();
				    loadflag = true;
			    }			    			               
            }            
        }
        else if (evt.getSource() == fftItem)
        {
        	setTitle("第5章 图像时频变换 FFT变换 作者 孙燮华");
        	if(loadflag)        	
        	{
        		if(iw == 256&&ih==256)
        		{
        			double[] iPix = new double[iw * ih];//输入灰度
	                pixels = common.grabber(iImage, iw, ih);
	        	    
	        	    for (int i = 0; i < iw*ih; i++)
	                    iPix[i] = pixels[i]&0xff;                  
	                    
	                //FFT变换
	                FFT2 fft2 = new FFT2();
	                fft2.setData2(iw, ih, iPix);
	                fftData = fft2.getFFT2();
	                
	                //FFT数据可视化
	                pixels = fft2.toPix(fftData, iw, ih);   
	        	    showPix(graph, pixels, "FFT变换");	        	    										
				}
				else
				{				
				    JOptionPane.showMessageDialog(null, "本程序仅适用于256X256图像!");
				    loadflag = false;
				}
			}
			else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");
        }
        else if (evt.getSource() == ifftItem)
        { 
            if(loadflag)        	
        	{
        	    double[] oPix;

                //FFT反变换
                FFT2 fft2 = new FFT2();
                fft2.setData2i(iw, ih, fftData);
                pixels = fft2.getPixels2i();
                showPix(graph, pixels, "逆FFT变换");                          					
				runflag = true;				
			}
			else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");             	                     
	    }
	    else if (evt.getSource() == dctItem)
        { 
            setTitle("第5章 图像时频变换 DCT变换 作者 孙燮华");       	
        	if(loadflag)        	
        	{
        	    if(iw == 256&&ih==256)
        		{        			
        			double[][] iPix = new double[ih][iw];//输入灰度
	                pixels = common.grabber(iImage, iw, ih);
	        	    
	        	    for (int j = 0; j < ih; j++)
	                    for (int i = 0; i < iw; i++)
	                        iPix[i][j] = pixels[i+j*iw]&0xff;                  
	                    
	                //DCT变换
	                DCT2 dct2 = new DCT2();
	                dctData = dct2.dctTrans(iPix, iw, ih, bsize, 1);
	                	                
	                //DCT数据可视化
	                pixels = common.toPixels(dctData, iw, ih);   
	        	    showPix(graph, pixels, "DCT变换");	        	    				
				}
				else
				{				
				    JOptionPane.showMessageDialog(null, "本程序仅适用于256X256图像!");
				    loadflag = false;
				}	
        	}
			else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");             	                     
	    }
	    else if (evt.getSource() == idctItem)
        { 
            if(loadflag)        	
        	{
        	    DCT2 dct2 = new DCT2();
        	    
        	    //DCT逆变换
                double[][] oim = dct2.dctTrans(dctData, iw, ih, bsize, -1);
                pixels = common.toPixels(oim, iw, ih);
                showPix(graph, pixels, "DCT逆变换");
                runflag = true;		
        	}
			else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");             	                     
	    }
	    else if (evt.getSource() == dwtItem)
        { 
            setTitle("第5章 图像时频变换 DWT变换 作者 孙燮华");       	
        	if(loadflag)        	
        	{
        	    if(iw == 256&&ih==256)
        		{        			
        			double[] iPix = new double[iw*ih];//输入灰度
	                pixels = common.grabber(iImage, iw, ih);	        	    
	        	    
	                for (int i = 0; i < iw*ih; i++)
	                    iPix[i] = pixels[i]&0xff;                  
	                
	                double[] h = { 0.23037781330889,  0.71484657055291,
                                   0.63088076792986, -0.02798376941686,
                                  -0.18703481171909,  0.03084138183556,
                                   0.03288301166689, -0.01059740178507 };

	                double[] g = { 0.23037781330889, -0.71484657055291,		
	                               0.63088076792986,  0.02798376941686,		
	                              -0.18703481171909, -0.03084138183556,		
	                               0.03288301166689,  0.01059740178507 }; 
	                                 
	                //DWT变换
	                DWT2 dwt2 = new DWT2(iw,ih);
	                dwtData = dwt2.wavelet2D(iPix, h, g, 1);
	                	                	                
	                //FFT数据可视化
	                pixels = common.toPixels(dwtData, iw, ih);   
	        	    showPix(graph, pixels, "DWT变换");	        	    				
				}
				else
				{				
				    JOptionPane.showMessageDialog(null, "本程序仅适用于256X256图像!");
				    loadflag = false;
				}		
        	}
			else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");             	                     
	    }
	    else if (evt.getSource() == idwtItem)
        { 
            if(loadflag)        	
        	{       		              
	            double[] h = { 0.23037781330889,  0.71484657055291,
                               0.63088076792986, -0.02798376941686,
                              -0.18703481171909,  0.03084138183556,
                               0.03288301166689, -0.01059740178507 };

                double[] g = { 0.23037781330889, -0.71484657055291,		
                               0.63088076792986,  0.02798376941686,		
                              -0.18703481171909, -0.03084138183556,		
                               0.03288301166689,  0.01059740178507 }; 
                                 
                //DWT逆变换
                DWT2 dwt2 = new DWT2(iw,ih);
                double[] iPix = dwt2.iwavelet2D(dwtData, h, g, 1);
                	                	                
                //IDWT数据可视化
                pixels = common.toPixels(iPix, iw, ih);   
        	    showPix(graph, pixels, "DWT逆变换");
        	    runflag = true;			
        	}
			else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");             	                     
	    }	    
	    else if (evt.getSource() == whtItem)
        { 
            setTitle("第5章 图像时频变换 WHT变换 作者 孙燮华");       	
        	if(loadflag)        	
        	{
        	    double[] iPix = new double[iw*ih];
                W = new double[iw * ih];
                
                pixels = common.grabber(iImage, iw, ih);
        	    
        	    for (int i = 0; i < iw*ih; i++)
                    iPix[i] = pixels[i]&0xff;
                        
                WHT2 wht2 = new WHT2(iw, ih);
                W = wht2.WALSH(iPix, 16);
                
                double[] nW = new double[iw * ih];
                
                //WHT数据可视化                
                for (int i = 0; i < iw*ih; i++)
                    nW[i] = (int)Math.abs(W[i] * 1000);                        
                
                pixels = common.toPixels(nW, iw, ih);   
        	    showPix(graph, pixels, "WHT变换");    
			}
			else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");             	                     
	    }
	    else if (evt.getSource() == iwhtItem)
        {                  	
        	if(loadflag)        	
        	{
        	    WHT2 wht2 = new WHT2(iw, ih);
                double[] oW = wht2.IWALSH(W, 16);
                pixels = common.toPixels(oW, iw, ih); 
                showPix(graph, pixels, "WHT逆变换");        	    
				runflag = true;   	
        	}
			else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");             	                     
	    }	    
        else if (evt.getSource() == exitItem) 
            System.exit(0);       
    }
    
    public void paint(Graphics g) 
    {    	  
        if (loadflag)
        {
        	g.clearRect(0,0,530, 350);        	
            g.drawImage(iImage, 5, 50, null);
            g.drawString("原图", 120, 320);
        }        
    }
    
    public void showPix(Graphics graph, int[] pixels, String str)
    {    
		//将数组中的象素产生一个图像
		ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
		Image oImage = createImage(ip);
		common.draw(graph, iImage, "原图", oImage, str);
	}

    public static void main(String[] args) 
    {  
        new Ch5ImageTrans();        
    } 
    
    private void closeWin()
    {
    	addWindowListener(new WindowAdapter()
        {  
            public void windowClosing(WindowEvent e) 
            {  
                System.exit(0);
            }
        });
    }
    
    //菜单界面
    public void setMenu()
    {    	
        Menu fileMenu = new Menu("文件");
        openItem = new MenuItem("打开");
        openItem.addActionListener(this);
        fileMenu.add(openItem);

        saveItem = new MenuItem("保存");
        saveItem.addActionListener(this);
        fileMenu.add(saveItem);
        
        exitItem = new MenuItem("退出");
        exitItem.addActionListener(this);
        fileMenu.add(exitItem);
    
        Menu transMenu = new Menu("时频变换");
        fftItem = new MenuItem("FFT");
        fftItem.addActionListener(this);
        transMenu.add(fftItem);
        
        ifftItem = new MenuItem("IFFT");
        ifftItem.addActionListener(this);
        transMenu.add(ifftItem);
         
        transMenu.addSeparator();
        dctItem = new MenuItem("DCT");
        dctItem.addActionListener(this);
        transMenu.add(dctItem);
        
        idctItem = new MenuItem("IDCT");
        idctItem.addActionListener(this);
        transMenu.add(idctItem);
        
        transMenu.addSeparator();
        dwtItem = new MenuItem("DWT");
        dwtItem.addActionListener(this);
        transMenu.add(dwtItem);
        
        idwtItem = new MenuItem("IDWT");
        idwtItem.addActionListener(this);
        transMenu.add(idwtItem);
        
        transMenu.addSeparator();
        whtItem = new MenuItem("WHT");
        whtItem.addActionListener(this);
        transMenu.add(whtItem);
        
        iwhtItem = new MenuItem("IWHT");
        iwhtItem.addActionListener(this);
        transMenu.add(iwhtItem);
               
        MenuBar menuBar = new MenuBar(); 
        menuBar.add(fileMenu);
        menuBar.add(transMenu);      
        setMenuBar(menuBar);
    }
    
    MenuItem openItem;
    MenuItem saveItem;
    MenuItem exitItem;	
    MenuItem fftItem;
    MenuItem ifftItem;
    MenuItem dctItem;
    MenuItem idctItem;
    MenuItem dwtItem;
    MenuItem idwtItem;
    MenuItem whtItem;
    MenuItem iwhtItem;
}
