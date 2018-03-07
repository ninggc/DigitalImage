/**
 * @Ch14ImageCipher.java
 * @Version 1.0 2010.03.06
 * @Author Xie-Hua Sun 
 */

package com.ninggc;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;

import process.common.Common;
import process.param.Parameters;
import process.algorithms.ImageCipher;


public class Ch14ImageCipher extends JFrame implements ActionListener
{
    Image iImage, oImage;
    BufferedImage bImage;
    
    int   iw, ih;
    int[] pixels,
          cphpix;                //加密图像数组 
     
    boolean loadflag = false,
            runflag  = false;    //图像处理执行标志 
    String imn,                  //图像文件名
           imh; 
                            //加密图像标识"c_"
    //参数选择面板
    JButton okButton;
	JDialog dialog;
	        
    ImageCipher cipher;
    Common common;
    
    public Ch14ImageCipher()
    {    
        setTitle("数字图像处理-Java编程与实验 第14章 图像加密");
        this.setBackground(Color.lightGray);        
              
        //菜单界面
        setMenu();
                
        cipher = new ImageCipher();
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
            common.chooseFile(chooser, "./images/ch14", 0);//设置默认目录,过滤文件
            int r = chooser.showOpenDialog(null);
                        
            MediaTracker tracker = new MediaTracker(this);
            
            if(r == JFileChooser.APPROVE_OPTION) 
            {  
                //路径与文件全名
                String name = chooser.getSelectedFile().getAbsolutePath();
                
                //取文件名及其长度 
                String fname = chooser.getSelectedFile().getName(); 
                int len = fname.length();
                
                //取出文件名imn和加密标识imh
                imn = fname.substring(0, len-4);
                imh = fname.substring(0, 2);
                
                if(runflag)
                {
                	loadflag = false;   
                    runflag  = false; 
                }
                
			    if(!loadflag)
			    {
	                //装载图像
				    iImage = common.openImage(name, tracker);    
				    //取载入图像的宽和高
				    iw = iImage.getWidth(null);
				    ih = iImage.getHeight(null);				    
				    repaint();
				    loadflag = true;
			    }			    			               
            }            
        }
        else if (evt.getSource() == arnoldItem)
        {
        	if(loadflag)        	
        	{
        		setTitle("第14章 图像加密 Arnold变换置乱 作者 孙燮华");
        		if(iw != ih)
        		{
        			JOptionPane.showMessageDialog(null, "仅适用于宽高相等图像!");
        			loadflag = false;
        			return;        			
        		}
        		pixels = common.grabber(iImage, iw, ih);
				
				//设定密钥
				int key = common.getParam("输入密钥(111-999)","888");	
				pixels = cipher.arnold_mtr(pixels, iw, key, 1);
			    oImage = showPix(graph, pixels, "加密图像");
			    //存储JPEG图像
			    File file = new File("./images/ch14/jpg/c_"+imn+".jpg");
	            bImage = (BufferedImage)this.createImage(iw, ih);
			    bImage.createGraphics().drawImage(oImage, 0, 0, this);
			    common.saveImageAsJPEG(bImage, file);
			    JOptionPane.showMessageDialog(null, "在目录images/ch14/jpg存储\n"
			                                  +"c_"+imn+".jpg加密图像成功!");			   											
			}
        }
        else if (evt.getSource() == iarnoldItem)
        {        	
        	if(loadflag)        	
        	{   
        	    if(!imh.equals("c_"))      //未受JPEG攻击
        	    {   	    
        	        cphpix = pixels;
					//设定密钥				
					int key = common.getParam("输入密钥(111-999)","888");				 
					pixels = cipher.arnold_mtr(cphpix, iw, key, -1);
				    showPix(graph, pixels, "解密图像");	
			    }
			    else                       //受JPEG攻击
			    {
			        cphpix = common.grabber(iImage, iw, ih);
					//设定密钥				
					int key = common.getParam("输入密钥(111-999)","888");				 
					pixels = cipher.arnold_mtr(cphpix, iw, key, -1);
				    showPix(graph, pixels, "解密攻击图");		
			    }		    								
			}
			else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");            	                     
	    }
	    else if (evt.getSource() == logisticItem)
        {
        	if(loadflag)        	
        	{
        		setTitle("第14章 图像加密 Logistic混沌置乱 作者 孙燮华");       		
        		pixels = common.grabber(iImage, iw, ih);
				
				//设定密钥
				int key = common.getParam("输入密钥(111-999)","888");				 				  		
				pixels = cipher.logistic(pixels, iw, ih, key, 1);    	        					
			    oImage = showPix(graph, pixels, "加密图像");
			    
			    File file = new File("./images/ch14/jpg/c_"+imn+".jpg");
	            bImage = (BufferedImage)this.createImage(iw, ih);
			    bImage.createGraphics().drawImage(oImage, 0, 0, this);
			    common.saveImageAsJPEG(bImage, file);
			    JOptionPane.showMessageDialog(null, "在目录images/ch14/jpg存储\n"
			                                  +"c_"+imn+".jpg加密图像成功!");			   											
			}
        }
        else if (evt.getSource() == ilogisticItem)
        {        	
        	if(loadflag)        	
        	{
        		if(!imh.equals("c_"))      //未受JPEG攻击
        	    {        					
					cphpix = pixels;
					//设定密钥
					int key = common.getParam("输入密钥(111-999)","888");						
					pixels = cipher.logistic(cphpix, iw, ih, key, -1);
				    showPix(graph, pixels, "解密图像");
			    }
			    else                       //受JPEG攻击
			    {
			        cphpix = common.grabber(iImage, iw, ih);;
					//设定密钥				
					int key = common.getParam("输入密钥(111-999)","888");				 
					pixels = cipher.logistic(cphpix, iw, ih, key, -1);
				    showPix(graph, pixels, "解密攻击图");		
			    }			    								
			}
			else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");            	                     
	    }
	    else if (evt.getSource() == logisXorItem)
        {
        	if(loadflag)        	
        	{
        		setTitle("第14章 图像加密 Logistic序列加密 作者 孙燮华");        		
        		pixels = common.grabber(iImage, iw, ih);
				
				//设定密钥
				int key = common.getParam("输入密钥(111-999)","888");				 				  		
				pixels = cipher.logisticXor(pixels, iw, ih, key);    	        					
			    showPix(graph, pixels, "加密图像");			   											
			}
        }
        else if (evt.getSource() == ilogisXorItem)
        {        	
        	if(loadflag)        	
        	{        					
				//设定密钥
				cphpix = pixels;				
				int key = common.getParam("输入密钥(111-999)","888");
				pixels = cipher.logisticXor(cphpix, iw, ih, key);
			    showPix(graph, pixels, "解密图像");			    								
			}
			else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");            	                     
	    }
	    else if (evt.getSource() == fibonXorItem)
        {
        	if(loadflag)        	
        	{
        		setTitle("第14章 图像加密 Fibonacci序列加密 作者 孙燮华");        		
        		pixels = common.grabber(iImage, iw, ih);
				
				//设定密钥
				int key = common.getParam("输入密钥(111-999)","888");				 				  		
				pixels = cipher.fibonacciXor(pixels, iw, ih, key);    	        					
			    showPix(graph, pixels, "加密图像");			   											
			}
        }
        else if (evt.getSource() == ifibonXorItem)
        {        	
        	if(loadflag)        	
        	{        					
				//设定密钥
				cphpix = pixels;				
				int key = common.getParam("输入密钥(111-999)","888");
				pixels = cipher.fibonacciXor(cphpix, iw, ih, key);
			    showPix(graph, pixels, "解密图像");			    								
			}
			else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");            	                     
	    }	    
	    else if(evt.getSource() == dctItem)              //DCT频域加密
	    {	    	 	
	    	//必须先加载图像,然后才可以进行DCT变换
			if(loadflag)			
			{
				setTitle("第14章 图像加密 DCT域加密 作者 孙燮华");
				if(iw != 256||ih != 256)
				{
				    JOptionPane.showMessageDialog(null, "仅适用于宽高相等图像!");
        			loadflag = false;
        			return;	
				}
				pixels = common.grabber(iImage, iw, ih);
				//DCT域加密
				pixels = cipher.dctCipher(pixels, iw, ih, 8);
				showPix(graph, pixels, "加密图");		
		    }				    	
	    }
	    //DCT频域解密
	    else if(evt.getSource() == idctItem)
	    {	    	
	    	if(loadflag)
	        {
		 		//DCT域解密
				pixels = cipher.dctDecipher(pixels, iw, ih, 8);								
				showPix(graph, pixels, "解密图");  
			}			
	    }
	    //Walsh-Hadamard变换域加密
	    else if(evt.getSource() == walshItem)
	    {	    		    	
	    	//必须先加载图像,然后才可以进行W-H变换
			if(loadflag)			
			{	
			    setTitle("第14章 图像加密 Walsh-Hadamard域加密 作者 孙燮华");
			    pixels = common.grabber(iImage, iw, ih);
			    //W-H域加密
			    pixels = cipher.whCipher(pixels, iw, ih);
			    showPix(graph, pixels, "加密图");			    					            					
			}				    	
	    }	    
	    //Walsh-Hadamard解密
	    else if(evt.getSource() == iwalshItem)
	    {	    		    	
	    	//必须先加载图像, 然后才可以进行FWT变换
			if(loadflag)			
			{
				//W-H域解密
				pixels = cipher.whDecipher(pixels, iw, ih);							
				showPix(graph, pixels, "解密图");
			}				    	
	    }
	    //小波频域混沌加密
	    else if(evt.getSource() == wavItem)
	    {	    	
	    	//必须先加载图像,然后才可以进行DWT变换
			if(loadflag)			
			{
				setTitle("第14章 图像加密 DWT域混沌置乱加密 作者 孙燮华");		    
			    pixels = common.grabber(iImage, iw, ih);
			    //加密
			    pixels = cipher.dwtCipher(pixels, iw, ih);				
				showPix(graph, pixels, "加密图");
			}				    	
	    }	    
	    //小波频域解密
	    else if(evt.getSource() == iwavItem)
	    {	    	
	    	if(loadflag)
	        {
	        	pixels = cipher.dwtDecipher(pixels, iw, ih);				
				showPix(graph, pixels, "解密图");
			}		
	    }
	    else if(evt.getSource() == logItem)
	    {
	    	setTitle("第14章 图像加密 Logistic混沌 作者 孙燮华");
	    	byte[][] bpix = cipher.chaos(520, 300);
	    	pixels = common.byte2ARGB(bpix, 520, 300);
	        ImageProducer ip = new MemoryImageSource(520, 300, pixels, 0, 520);
		    oImage  = createImage(ip);
		    runflag = true;
		    repaint();		      
	    }
	    else if(evt.getSource() == atrItem)
	    {
	    	setTitle("第14章 图像加密 混沌吸引子 作者 孙燮华");
	    	Parameters pp = new Parameters("参数", "mu: ", "x: ","3.8", "0.235");
	    	setPanel(pp, "");
	    	float mu = pp.getPadx();
	    	float t  = pp.getPady();
	    	
	    	common.draw(graph, mu, t);	    	
	    }
	    else if (evt.getSource() == okButton)
           	dialog.dispose(); 	    	
	    else if (evt.getSource() == exitItem) 
            System.exit(0);       
    }   
	
    public void paint(Graphics g) 
    {    	  
        if (iImage != null && (!runflag))
        {
        	g.clearRect(0, 0, 530, 350);        	
            g.drawImage(iImage, 5, 50, null);
            g.drawString("原图", 120, 320);
        }
        else if(runflag)
        {
            g.clearRect(0, 0, 530, 330);
		    g.drawImage(oImage, 0, 30, null);	        
		    g.drawLine(5, 310, 525, 310);
	        for(int i = 1; i < 7; i++)
	            g.drawLine((int)(i*86.5), 310, (int)(i*86.5), 305);
	        g.drawString("3.0",80, 322);
	        g.drawString("3.2",165,322);
	        g.drawString("3.4",250,322);
	        g.drawString("3.6",338,322);
	        g.drawString("3.8",423,322);  	
        }        
    }
    
    public Image showPix(Graphics graph, int[] pixels, String str)
    {    
		//将数组中的象素产生一个图像
		ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
		Image oImage = createImage(ip);
		common.draw(graph, iImage, "原图", oImage, str);
		runflag = true;
		return oImage;
	}
	
	public void setPanel(Parameters pp, String s)
    {
    	JPanel buttonsPanel = new JPanel();  
	    okButton = new JButton("确定");				
        okButton.addActionListener(this);
        
    	dialog = new JDialog(this, s+ " 参数选择", true);     
        
        Container contentPane = getContentPane();
		Container dialogContentPane = dialog.getContentPane();

		dialogContentPane.add(pp, BorderLayout.CENTER);
		dialogContentPane.add(buttonsPanel, BorderLayout.SOUTH);
		dialog.pack();		        
        buttonsPanel.add(okButton);			
       	dialog.setLocation(50,330);
    	dialog.show();
    }
    
    public static void main(String[] args) 
    {  
        new Ch14ImageCipher();        
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
        
        exitItem = new MenuItem("退出");
        exitItem.addActionListener(this);
        fileMenu.add(exitItem);       
        
        Menu spaceCiphMenu = new Menu("空域加密");
        arnoldItem = new MenuItem("Arnold置乱加密");
        arnoldItem.addActionListener(this);
        spaceCiphMenu.add(arnoldItem);
        
        iarnoldItem = new MenuItem("Arnold置乱解密");
        iarnoldItem.addActionListener(this);
        spaceCiphMenu.add(iarnoldItem);
        spaceCiphMenu.addSeparator();
        
        logisticItem = new MenuItem("Logistic置乱加密");
        logisticItem.addActionListener(this);
        spaceCiphMenu.add(logisticItem);
        
        ilogisticItem = new MenuItem("Logistic置乱解密");
        ilogisticItem.addActionListener(this);
        spaceCiphMenu.add(ilogisticItem);
        spaceCiphMenu.addSeparator();
         
        logisXorItem = new MenuItem("Logistic序列加密");
        logisXorItem.addActionListener(this);
        spaceCiphMenu.add(logisXorItem);
        
        ilogisXorItem = new MenuItem("Logistic序列解密");
        ilogisXorItem.addActionListener(this);
        spaceCiphMenu.add(ilogisXorItem);
        spaceCiphMenu.addSeparator();
        
        fibonXorItem = new MenuItem("Fibonacci序列加密");
        fibonXorItem.addActionListener(this);
        spaceCiphMenu.add(fibonXorItem);
        
        ifibonXorItem = new MenuItem("Fibonacci序列解密");
        ifibonXorItem.addActionListener(this);
        spaceCiphMenu.add(ifibonXorItem);
        
        Menu freqCiphMenu = new Menu("频域加密");
        dctItem = new MenuItem("DCT加密");
        dctItem.addActionListener(this);
        freqCiphMenu.add(dctItem);
        
        idctItem = new MenuItem("DCT解密");
        idctItem.addActionListener(this);
        freqCiphMenu.add(idctItem);
        freqCiphMenu.addSeparator();
        
        walshItem = new MenuItem("Walsh加密");
        walshItem.addActionListener(this);
        freqCiphMenu.add(walshItem);
        
        iwalshItem = new MenuItem("Walsh解密");
        iwalshItem.addActionListener(this);
        freqCiphMenu.add(iwalshItem);
        freqCiphMenu.addSeparator();
        
        wavItem = new MenuItem("DWT加密");
        wavItem.addActionListener(this);
        freqCiphMenu.add(wavItem);        
        
        iwavItem = new MenuItem("DWT解密");
        iwavItem.addActionListener(this);
        freqCiphMenu.add(iwavItem);
        
        Menu chaosMenu = new Menu("混沌演示");
        logItem = new MenuItem("Logistic");
        logItem.addActionListener(this);
        chaosMenu.add(logItem);
        
        atrItem = new MenuItem("吸引子演示");
        atrItem.addActionListener(this);
        chaosMenu.add(atrItem);
               
        MenuBar menuBar = new MenuBar();
        menuBar.add(fileMenu);
        menuBar.add(spaceCiphMenu);
        menuBar.add(freqCiphMenu);
        menuBar.add(chaosMenu);       
        setMenuBar(menuBar);
    }
    
    MenuItem openItem;
    MenuItem exitItem;
    
    MenuItem arnoldItem;   //Arnold变换加密
    MenuItem iarnoldItem;  //逆Arnold变换解密
    MenuItem logisticItem; //Logistic变换加密
    MenuItem ilogisticItem;//逆Logistic变换解密
    MenuItem logisXorItem; //Logistic序列加密
    MenuItem ilogisXorItem;//Logistic序列解密
    MenuItem fibonXorItem; //Fibonacci序列加密
    MenuItem ifibonXorItem;//Fibonacci序列解密
    
    MenuItem dctItem;      //DCT加密
    MenuItem idctItem;     //DCT解密
    MenuItem walshItem;    //Walsh加密
    MenuItem iwalshItem;   //Walsh解密
    MenuItem wavItem;      //Wavelet加密
    MenuItem iwavItem;     //Wavelet解密
    MenuItem logItem;      //Logistic演示
    MenuItem atrItem;      //吸引子演示
}
