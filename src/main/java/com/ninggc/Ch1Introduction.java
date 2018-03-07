package com.ninggc; /**
 * @Ch1Introduction.java
 * @Version 1.0 2010.02.27
 * @Author Xie-Hua Sun 
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import process.algorithms.Introduction;
import process.param.ResultShow;
import process.common.Common;
import process.rw.*;

public class Ch1Introduction extends JFrame implements ActionListener
{
    Image iImage, iImage2, oImage;
   
    int   iw, ih;
    int[] pix, pix2;
                 
    boolean loadflag = false,
            loadflag2= false,
            runflag  = false;    //图像处理执行标志 
            
    Common common;
    Introduction introduction;
    
    public Ch1Introduction()
    {    
        setTitle("数字图像处理-Java编程与实验 第1章 概论");
        this.setBackground(Color.lightGray);        
              
        //菜单界面
        setMenu();
        
        introduction = new Introduction();
        common = new Common();
                
        //关闭窗口
        closeWin();
        
        setSize(530, 330);
        setLocation(700, 10);
        setVisible(true);
    }

	/**
	 * 菜单响应处理
	 * @param evt
	 */
	public void actionPerformed(ActionEvent evt)
    {
    	Graphics graph = getGraphics();
    	ResultShow result = null;
    	      	  
        if (evt.getSource() == openItem) 
        {
        	//文件选择对话框
            JFileChooser chooser = new JFileChooser();            
            common.chooseFile(chooser, "./images/ch1", 4);//设置默认目录,过滤文件
            int r = chooser.showOpenDialog(null);                                 
            
            MediaTracker tracker = new MediaTracker(this);
                                      
            if(r == JFileChooser.APPROVE_OPTION) 
            {
            	String name = chooser.getSelectedFile().getAbsolutePath();
                
                //取文件名长度 
                String fname = chooser.getSelectedFile().getName(); 
                
                int len = fname.length();
                
                //取文件名的扩展名
                String exn = fname.substring(len-3, len);
                String imn = fname.substring(0,len-4);
                
                if(runflag)//初始化 
	            {            	
	            	loadflag  = false;
	            	loadflag2 = false;
	            	runflag   = false;	            	
	            }
	            
	            if(exn.equalsIgnoreCase("bmp"))//BMP图像
                {
                	MemoryImageSource mis = null;          
                	BMPReader bmp = new BMPReader();
				  	try
				  	{
				  	    FileInputStream fin = new FileInputStream(name);
				  	    mis = bmp.getBMPImage(fin);
				  	}
				  	catch(IOException e1){System.out.println("Exception!");}
				  	if(!loadflag)
				    {
					  	iImage = createImage(mis); 
					  	iw = iImage.getWidth(null);
						ih = iImage.getHeight(null);						
						loadflag = true;				    
						repaint();
					}
					else if(loadflag && (!runflag))
				    {			        
					    iImage2 = createImage(mis);
					    common.draw(graph, iImage, "原图1", iImage2, "原图2");
					    loadflag2 = true;					    	    			    	
				    }	
				}
				else if(exn.equalsIgnoreCase("pgm")) //pgm图像
                {
                	setTitle("第1章 概论 PGM图像显示 作者 孙燮华");
                    PGM pgm = new PGM();
                    pgm.readPGMHeader(name);
                    char a0 = pgm.getCh0();
                    char a1 = pgm.getCh1();
                    iw = pgm.getWidth();
                    ih = pgm.getHeight();
                	int maxpix = pgm.getMaxpix();
                	pix = pgm.readData(iw, ih, 5);   //P5-Gray image
                	ImageProducer ip = new MemoryImageSource(iw, ih, pix, 0, iw);
		            iImage = createImage(ip);
		            common.draw(graph, iImage, a0, a1, iw, ih, maxpix);          
		        }
                else if(exn.equalsIgnoreCase("ppm")) //ppm图像
                {
                	setTitle("第1章 概论 PPM图像显示 作者 孙燮华");
                    PGM pgm = new PGM();
                    pgm.readPPMHeader(name);
                    char a0 = pgm.getCh0();
                    char a1 = pgm.getCh1();
                    iw = pgm.getWidth();
                    ih = pgm.getHeight();
                    int maxpix = pgm.getMaxpix();
                	pix = pgm.readData(iw, ih, 6);       //P6-Color image
                 	ImageProducer ip = new MemoryImageSource(iw, ih, pix, 0, iw);
		            iImage = createImage(ip);
		            common.draw(graph, iImage, a0, a1, iw, ih, maxpix);		            
                }
                else if(exn.equalsIgnoreCase("raw"))     //RAW图像
                {
                	setTitle("第1章 概论 RAW图像显示 作者 孙燮华");
                	iw = 256;
                	ih = 256;
                	int[][] iPix = new int[iw][ih];            	
                                
	                //读取RAW图像
	                RAW reader = new RAW();
	                iPix = reader.readRAW(name, iw, ih);
	                
				   	//将字节数组pix转化为图像序列pixels
	            	pix = common.toPixels(iPix, iw, ih);
	            	
	            	//将数组中的象素产生一个图像
				    ImageProducer ip = new MemoryImageSource(iw, ih, pix, 0, iw);
				    iImage = createImage(ip);  
				    
	  	    	    loadflag = true;  
	  	            repaint(); 
                }                  
                else                                //GIF,JPG,PNG
                {                       
	                if(!loadflag)
				    {
		                //装载图像
					    iImage = common.openImage(name,tracker);    
					    //取载入图像的宽和高
					    iw = iImage.getWidth(null);
					    ih = iImage.getHeight(null);
					    loadflag = true;				    
					    repaint();
				    }
				    else if(loadflag && (!runflag))
				    {			        
					    iImage2 = common.openImage(name,tracker);
					    common.draw(graph, iImage, "原图1", iImage2, "原图2");					    
					    loadflag2 = true;	    			    	
				    }
			    }				               
            } 
        }
        else if (evt.getSource() == analItem)
        {
        	setTitle("第1章 概论 基本统计分析量 作者 孙燮华");
        	if(loadflag)        	
        	{ 
        	    pix = common.grabber(iImage, iw, ih);
				
				//计算熵
				double entropy = introduction.getEntropy(pix, iw, ih);
				String entstr = "图像熵:";
				
				//计算灰度平均值
				double average = introduction.getAverage(pix, iw, ih);
				String avrstr = "灰度平均值:";
				
				//灰度中值
				double median = introduction.getMedian(pix, iw, ih);
				String medstr = "(100,100)5X5方块灰度中值:";
				
				//计算均方差
				double sqsum = introduction.getSqsum(pix, iw, ih);
				String sqstr = "均方差:";
				
				runflag = common.draw(graph, iImage, entstr, entropy, avrstr, average, 
				                      medstr, median, sqstr, sqsum);																											
			}
        }
        else if (evt.getSource() == histItem)
        {
            setTitle("第1章 概论 直方图 作者 孙燮华");       	
        	if(loadflag)        	
        	{
        		pix = common.grabber(iImage, iw, ih);
				
				//计算直方图
				int[] hist = common.getHist(pix, iw, ih);
				int max = common.maximum(hist);
    	        hist = common.normalize(hist, max);//规范化为[0, 255]
				common.draw(graph, hist, max);
				runflag = true;       		
			}
			else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");             	                     
	    }
	    else if (evt.getSource() == distItem)
        {   
            setTitle("第1章 概论 图像距离与图像评价 作者 孙燮华");
            
           	if(loadflag)
        	{        		
				pix = common.grabber(iImage, iw, ih);
				if(loadflag2)
				{
				    pix2 = common.grabber(iImage2, iw, ih);
				    
				    String supstr = "上确界距离";				
				    double supdis = introduction.getSupDis(pix, pix2, iw, ih);
				    
				    String rmsstr = "均方根距离";
				    double rmsdis = introduction.getRmsDis(pix, pix2, iw, ih);
				    
				    String msestr = "均方误差";
				    double mse = introduction.getMSE(pix, pix2, iw, ih);
				    
				    String psnrstr = "峰值信噪比";
				    double psnr = introduction.getPSNR(pix, pix2, iw, ih);
				    				    
				    result = new ResultShow(graph, supstr, supdis, rmsstr, rmsdis, 
				                    msestr, mse, psnrstr, psnr);
				    result.show();
				    runflag = true;				    
				}
				else
				    JOptionPane.showMessageDialog(null,"请打开第2幅图像!");
			}
			else
				JOptionPane.showMessageDialog(null,"请先打开图像!");	        	        	
        }  
	    else if (evt.getSource() == exitItem) 
            System.exit(0);       
    }
    
    public void paint(Graphics g)
    {
    	if(loadflag)
    	{
    		g.clearRect(0, 0, 530, 350);        	
            g.drawImage(iImage,  5,   50, null);        
    	    g.drawString("原图", 120, 320);
    	} 
    }
    
    public static void main(String[] args) 
    {  
        new Ch1Introduction();        
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
    
    void setMenu()
    {
    	//菜单界面
        Menu fileMenu = new Menu("文件");
        openItem = new MenuItem("打开");
        openItem.addActionListener(this);
        fileMenu.add(openItem);

        exitItem = new MenuItem("退出");
        exitItem.addActionListener(this);
        fileMenu.add(exitItem);        
        
        Menu processMenu = new Menu("图像处理");
        analItem = new MenuItem("基本分析量");
        analItem.addActionListener(this);
        processMenu.add(analItem);
        
        processMenu.addSeparator();              
        histItem = new MenuItem("直方图");
        histItem.addActionListener(this);
        processMenu.add(histItem);
        
        processMenu.addSeparator();
        distItem = new MenuItem("距离与评价");
        distItem.addActionListener(this);
        processMenu.add(distItem); 
        
        MenuBar menuBar = new MenuBar();
        menuBar.add(fileMenu);       
        menuBar.add(processMenu);
        setMenuBar(menuBar);   
    }      
    
    MenuItem openItem;
    MenuItem exitItem;
    
    MenuItem analItem;
    MenuItem histItem;
    MenuItem distItem;  
}
