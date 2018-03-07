package com.ninggc; /**
 * @Ch6ImageEnhance.java
 * @Version 1.0 2010.02.17
 * @Author Xie-Hua Sun 
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import process.algorithms.ImageEnhance;
import process.param.ParametersI;
import process.common.*;

public class Ch6ImageEnhance extends JFrame implements ActionListener
{        
    Image iImage, iImage2, oImage;
    BufferedImage bImage; 
    
    int   iw, ih;
    int[] pixels;          
             
    boolean loadflag  = false,
            loadflag2 = false,    //第2幅图像载入标志
            runflag   = false,    //图像处理执行标志 
            seeflag   = false;    //预览标志 
    
    //参数选择面板
    ParametersI p;
    JButton okButton, seeButton;
	JDialog dialog;  
      
    Common common;
    ImageEnhance enhance;
    
    public Ch6ImageEnhance()
    {    
        setTitle("数字图像处理-Java编程与实验 第6章 图像增强");
        this.setBackground(Color.lightGray);        
              
        //菜单界面
        setMenu();
        
        common  = new Common();
        enhance = new ImageEnhance();
                
        //关闭窗口
        closeWin();
                
        setSize(530, 330);
        setLocation(700, 10);
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
            
            if(r == JFileChooser.APPROVE_OPTION) 
            {  
                String name = chooser.getSelectedFile().getAbsolutePath();
                if(runflag)
                { 
                    loadflag  = false;
                    runflag   = false;
                }   
			    if(!loadflag)
			    {
	                //装载图像
				    iImage = common.openImage(name, tracker);    
				    //取载入图像的宽和高
				    iw = iImage.getWidth(null);
				    ih = iImage.getHeight(null);				    
				    loadflag = true;
				    repaint();				    
			    }			               
            }            
        }
        else if (evt.getSource() == stretchItem)
        {
        	setTitle("第6章 图像增强 对比度扩展 作者 孙燮华");
        	if(loadflag)        	
        	{
        		p = new ParametersI();
        	    setPanelI(p, "对比度扩展", 1);
        	    showSpaceEnhance(graph, 0, "对比度扩展");        	    					
			}
        }
        else if (evt.getSource() == balanceItem)
        {
        	setTitle("第6章 图像增强 直方图均匀化  作者 孙燮华");
        	if(loadflag)        	
        	    showSpaceEnhance(graph, 1, "直方图均匀化");    	    						
		}
        else if (evt.getSource() == histItem)
        {        	
        	if(loadflag)
        	{        		
	            pixels = common.grabber(iImage, iw, ih);
	            
				//显示图像的直方图
				Hist h = new Hist("均匀化前");
				
				//传送数据
				h.getData(pixels, iw, ih);
				h.setLocation(10, 320);								
			}
			if(runflag)
        	{
        		pixels = common.grabber(oImage, iw, ih);
	            
				//显示图像的直方图
				Hist h = new Hist("均匀化后");
				
				//传送数据
				h.getData(pixels, iw, ih);
				h.setLocation(310, 320);
        	}					
        }
        else if(evt.getSource() == seeButton)//预览
        {        	
			p = new ParametersI();
			common.draw(graph, p.getx1(), p.gety1(), p.getx2(), p.gety2(), 
			            "对比度扩展可视化预览");           	
        }
        else if (evt.getSource() == threshItem)
        {
        	setTitle("第6章 图像增强 阈值滤波 作者 孙燮华");
        	if(loadflag)       	
        	    showSpaceEnhance(graph, 2, "阈值滤波");        	    
        }
        else if (evt.getSource() == averItem)
        {
        	setTitle("第6章 图像增强 均值滤波 作者 孙燮华");
        	if(loadflag)       	
        	    showSpaceEnhance(graph, 3, "均值滤波");        	    
        }
        else if (evt.getSource() == medianItem)
        {
        	setTitle("第6章 图像增强 中值滤波 作者 孙燮华");
        	if(loadflag)        	
        	{
        		p = new ParametersI("窗口选择", "3X3", "1X5", "5X1", "5X5");
        	    setPanelI(p, "中值滤波", 0);
        	    showSpaceEnhance(graph, 4, "中值滤波");        	    	   	
        	}
        }
        else if (evt.getSource() == lowItem)
        {
        	setTitle("第6章 图像增强 低通模板滤波 作者 孙燮华");
        	if(loadflag)
        	{
        		p = new ParametersI("模板选择", "h1", "h2", "h3");
        	    setPanelI(p, "低通模板滤波", 0);
        	    showSpaceEnhance(graph, 5, "低通模板滤波");        	    
        	}
        }
        else if (evt.getSource() == highItem)
        {
        	setTitle("第6章 图像增强 高通模板滤波 作者 孙燮华");
        	if(loadflag)       	
        	{
        	    p = new ParametersI("模板选择", "H1", "H2", "H3", "H4", "H5");
        	    setPanelI(p, "高通模板滤波", 0);
        	    showSpaceEnhance(graph, 6, "高通模板滤波");        	    
        	}
        }
        else if (evt.getSource() == blpfItem)
        {
        	setTitle("第6章 图像增强 Butterworth低通滤波 作者 孙燮华");
        	if(loadflag)        	
        	{ 
        	    if(iw == 256&&ih==256)//0:BHPF低通滤波
        		    showFreqFilter(graph, 0, "输入参数(整数0~255)", 
        		                   "100", "BLPF滤波");	        		
				else
				{				
				    JOptionPane.showMessageDialog(null, "仅适用于256X256图像!");
				    loadflag = false;
				}
        	}
        }
        else if (evt.getSource() == bhpfItem)
        {
        	setTitle("第6章 图像增强 Butterworth高通滤波 作者 孙燮华");
        	if(loadflag)        	
        	{
        		if(iw == 256&&ih==256)//1:BHPF高通滤波
        		    showFreqFilter(graph, 1, "输入参数(整数0~255)", 
        		                   "150", "BHPF滤波");	        		
				else
				{				
				    JOptionPane.showMessageDialog(null, "仅适用于256X256图像!");
				    loadflag = false;
				}	
        	}
        }
        else if (evt.getSource() == elpfItem)
        {
        	setTitle("第6章 图像增强 指数低通滤波 作者 孙燮华");
        	if(loadflag)        	
        	{
        		if(iw == 256&&ih==256)//2:ELPF低通滤波
        		    showFreqFilter(graph, 2, "输入参数(整数0~255)", 
        		                   "150", "ELPF滤波");	        		
				else
				{				
				    JOptionPane.showMessageDialog(null, "仅适用于256X256图像!");
				    loadflag = false;
				}	
        	}
        }
        else if (evt.getSource() == ehpfItem)
        {
        	setTitle("第6章 图像增强 指数高通滤波 作者 孙燮华");
        	if(loadflag)        	
        	{
        		if(iw == 256&&ih==256)//3:BHPF高通滤波
        		    showFreqFilter(graph, 3, "输入参数(整数0~255)", 
        		                   "150", "EHPF滤波");	        		
				else
				{				
				    JOptionPane.showMessageDialog(null, "仅适用于256X256图像!");
				    loadflag = false;
				}	
        	}
        }
        else if (evt.getSource() == kirItem)
        {
        	setTitle("第6章 图像增强 Kirsch锐化 作者 孙燮华");
        	if(loadflag)       	
        	    showSharp(graph, 1, "Kirsch锐化");        		
        }
        else if (evt.getSource() == lapItem)
        {
        	setTitle("第6章 图像增强 Laplace锐化 作者 孙燮华");
        	if(loadflag)        	
        	    showSharp(graph, 2, "Laplace锐化");        		
        }
        else if (evt.getSource() == preItem)
        {
        	setTitle("第6章 图像增强 Prewitt锐化 作者 孙燮华");
        	if(loadflag)        	
        	    showSharp(graph, 3, "Prewitt锐化");        		
        }
        else if (evt.getSource() == robItem)
        {
        	setTitle("第6章 图像增强 Roberts锐化 作者 孙燮华");
        	if(loadflag)        	
        	    showSharp(graph, 0, "Roberts锐化");        		
        }
        else if (evt.getSource() == sobItem)
        {
        	setTitle("第6章 图像增强 Sobel锐化 作者 孙燮华");
        	if(loadflag)        	
        	    showSharp(graph, 5, "Sobel锐化");        		
        }						
        else if (evt.getSource() == okButton)
           	dialog.dispose(); 
        else if (evt.getSource() == exitItem) 
            System.exit(0);       
    }
        
    public void paint(Graphics g) 
    {    	  
        if (loadflag)
        {
        	g.clearRect(0, 0, 260, 350); 
        	g.drawImage(iImage, 5, 50, null);
            g.drawString("原图", 120, 320);
            setBackground(Color.white);
        }             
    }
    
    /*******************************************************
     * type -- 0:仅有一个okButton; 1:有二个Button
     *******************************************************/
    private void setPanelI(ParametersI p, String s, int type)
    {
    	JPanel buttonsPanel = new JPanel();
    	dialog = new JDialog(this, s + " 参数选择", true);     
        Container contentPane = getContentPane();
		Container dialogContentPane = dialog.getContentPane();

		dialogContentPane.add(p, BorderLayout.CENTER);
		dialogContentPane.add(buttonsPanel, BorderLayout.SOUTH);
		
		if(type == 1)
		{
		    seeButton    = new JButton("预览");  
	        seeButton.addActionListener(this);		
            buttonsPanel.add(seeButton);
        }
        okButton     = new JButton("确定");				
        okButton.addActionListener(this);
        buttonsPanel.add(okButton);
        dialog.pack();
		dialog.setLocation(0,320);     //设置对话框在屏幕上坐标
        dialog.show();	        
    }
    
    /*************************************************
     * type - 型号. 0:BLPF 1:BHPF 2:ELPF 3:EHPF
     * name - 输出图像标题字符串
     *************************************************/    
    public void showSpaceEnhance(Graphics graph, int type, String name)
    {
    	pixels = common.grabber(iImage, iw, ih);
		switch(type)
		{
			case 0: //对比度扩展
			        int[] pixMap = enhance.pixelsMap(p.getx1(), p.gety1(), 
			                                         p.getx2(), p.gety2()); 
			        pixels = enhance.stretch(pixels, pixMap, iw, ih);
			        break;
			case 1: //直方图均匀化
				    int[] histogram = common.getHist(pixels, iw, ih);//取直方图
				    pixels = enhance.histequal(pixels, histogram, iw, ih);
				    break;		
	        case 2: pixels = enhance.threshold(pixels, iw, ih);
	                break;
	        case 3: pixels = enhance.average(pixels, iw, ih);
	                break;
	        case 4: pixels = enhance.median(pixels, iw, ih, p.getRadioState4());
	                break;
	        case 5: pixels = enhance.lowpass(pixels, iw, ih, p.getRadioState3());
	                break;
	        case 6: pixels = enhance.highpass(pixels, iw, ih, p.getRadioState3());
	                break;
	    }    
	    //将数组中的象素产生一个图像
		ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
		oImage = createImage(ip);
		common.draw(graph, iImage, "原图", oImage, name);
		runflag = true;		
    }
    
    /*************************************************
     * type - 型号. 0:BLPF 1:BHPF 2:ELPF 3:EHPF
     * str  - 对话框说明字符串
     * val  - 对话框参数默认值字符串
     * name - 输出图像标题字符串
     *************************************************/    
    public void showFreqFilter(Graphics graph, int type, String str, 
                               String val, String name)
    {
    	pixels = common.grabber(iImage, iw, ih);
	    pixels = enhance.BEfilter(pixels, iw, ih, type, 
		         common.getParam(str, val));		
						
		//将数组中的象素产生一个图像
		ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
		oImage = createImage(ip);
	    common.draw(graph, iImage, "原图", oImage, name);
		runflag = true;	
    }
    
    /*************************************************
     * type - 算子型号. 0:Roberts 1:Kirsch  2:Laplace 
     *                  3:Prewitt 5:Sobel
     * name - 输出图像标题字符串
     *************************************************/    
    public void showSharp(Graphics graph, int type, String name)
    {
    	pixels = common.grabber(iImage, iw, ih);
		if(type != 0)
		    pixels = enhance.detect(pixels, iw, ih, type, 0, false);		    
		else if(type == 0)
		    pixels = enhance.robert(pixels, iw, ih, type, false);		    	    	    
		//将数组中的象素产生一个图像
		ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
		oImage = createImage(ip);
		common.draw(graph, iImage, "原图", oImage, name);
		runflag = true;
    }
    
    public static void main(String[] args) 
    {  
        new Ch6ImageEnhance();        
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
    
    private void setMenu()
    {
    	//菜单界面
        Menu fileMenu = new Menu("文件");
        openItem = new MenuItem("打开");
        openItem.addActionListener(this);
        fileMenu.add(openItem);

        exitItem = new MenuItem("退出");
        exitItem.addActionListener(this);
        fileMenu.add(exitItem);
        
        //空域增强---------------------------    
        Menu spaceMenu = new Menu("空域增强");
        stretchItem = new MenuItem("对比度扩展");
        stretchItem.addActionListener(this);
        spaceMenu.add(stretchItem);
        
        spaceMenu.addSeparator();        
        balanceItem = new MenuItem("直方图均匀化");
        balanceItem.addActionListener(this);
        spaceMenu.add(balanceItem);
               
        histItem = new MenuItem("显示直方图");
        histItem.addActionListener(this);
        spaceMenu.add(histItem);
        
        spaceMenu.addSeparator();        
        threshItem = new MenuItem("阈值滤波");
        threshItem.addActionListener(this);
        spaceMenu.add(threshItem);
        
        averItem = new MenuItem("均值滤波");
        averItem.addActionListener(this);
        spaceMenu.add(averItem);
        
        spaceMenu.addSeparator();        
        medianItem = new MenuItem("中值滤波");
        medianItem.addActionListener(this);
        spaceMenu.add(medianItem);
        
        spaceMenu.addSeparator();        
        lowItem = new MenuItem("低通模板滤波");
        lowItem.addActionListener(this);
        spaceMenu.add(lowItem);
        
        highItem = new MenuItem("高通模板滤波");
        highItem.addActionListener(this);
        spaceMenu.add(highItem);
        
        //频域增强-------------------------
        Menu freqMenu = new Menu("频域增强");
        blpfItem = new MenuItem("BLPF(低)");
        blpfItem.addActionListener(this);
        freqMenu.add(blpfItem);
        
        bhpfItem = new MenuItem("BHPF(高)");
        bhpfItem.addActionListener(this);
        freqMenu.add(bhpfItem);
        
        freqMenu.addSeparator();
        elpfItem = new MenuItem("ELPF(低)");
        elpfItem.addActionListener(this);
        freqMenu.add(elpfItem);
        
        ehpfItem = new MenuItem("EHPF(高)");
        ehpfItem.addActionListener(this);
        freqMenu.add(ehpfItem);
                       
        //--------------锐化---------------
        Menu sharpMenu = new Menu("图像锐化");
        kirItem = new MenuItem("Kirsch");
        kirItem.addActionListener(this);
        sharpMenu.add(kirItem);
        
        sharpMenu.addSeparator();
        lapItem = new MenuItem("Laplace");
        lapItem.addActionListener(this);
        sharpMenu.add(lapItem);
        
        sharpMenu.addSeparator();          
        preItem = new MenuItem("Prewitt");
        preItem.addActionListener(this);
        sharpMenu.add(preItem);
        
        sharpMenu.addSeparator();
        robItem = new MenuItem("Roberts");
        robItem.addActionListener(this);
        sharpMenu.add(robItem);
        
        sharpMenu.addSeparator();
        sobItem = new MenuItem("Sobel");
        sobItem.addActionListener(this);
        sharpMenu.add(sobItem);
                
        MenuBar menuBar = new MenuBar();
        menuBar.add(fileMenu);
        menuBar.add(spaceMenu);
        menuBar.add(freqMenu);
        menuBar.add(sharpMenu);
        setMenuBar(menuBar);      
    }
    
    MenuItem openItem;
    MenuItem exitItem;
    MenuItem stretchItem;
    MenuItem balanceItem; //均匀化
    MenuItem histItem;    //显示直方图
    
    MenuItem threshItem;
    MenuItem averItem;
    MenuItem medianItem;
    MenuItem lowItem;     //低通模板滤波
    MenuItem highItem;    //高通模板滤波
    MenuItem blpfItem;    //Butterworth低通滤波
    MenuItem bhpfItem;    //Butterworth高通滤波 
    MenuItem elpfItem;    //指数低通滤波
    MenuItem ehpfItem;    //指数高通滤波 
    
    MenuItem kirItem;     //kirsch算子
    MenuItem lapItem;     //laplace算子
    MenuItem preItem;     //prewitt算子
    MenuItem robItem;     //roberts算子
    MenuItem sobItem;     //sobel算子
}
