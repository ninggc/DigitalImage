package com.ninggc; /**
 * @Ch2Digitization.java
 * @Version 1.0 2010.02.10
 * @Author Xie-Hua Sun 
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import process.algorithms.Digitization;
import process.common.Common;

public class Ch2Digitization extends JFrame implements ActionListener
{
    Image iImage, oImage;
    
    int   iw, ih;
    int[] pixels;          
             
    boolean loadflag  = false,
            runflag   = false;    //图像处理执行标志 
            
    Digitization digit;
    Common common;
    
    public Ch2Digitization()
    {    
        setTitle("数字图像处理-Java编程与实验 第2章 图像数字化");
        this.setBackground(Color.lightGray);        
              
        //菜单界面
        setMenu();
        
        digit  = new Digitization();
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
            common.chooseFile(chooser, "./images/ch2", 0);//设置默认目录,过滤文件
            int r = chooser.showOpenDialog(null);
                        
            MediaTracker tracker = new MediaTracker(this);
            
            if(r == JFileChooser.APPROVE_OPTION) 
            {  
                String name = chooser.getSelectedFile().getAbsolutePath();
                if(runflag)
                {
                	loadflag = false;   
                    runflag  = false; 
                }
                
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
        else if (evt.getSource() == samplItem)
        {
        	setTitle("第2章 图像数字化 图像采样 作者 孙燮华");
        	if(loadflag)        	
        	{
        		int gray = 64; 
        	    pixels = common.grabber(iImage, iw, ih);
				boolean flag = false;
				do{				
				    gray = common.getParam("采样级数(256/128/64/32/16):","128");
					
					//检查输入
					switch(gray)
				    {
						case 256:flag = true;break;
						case 128:flag = true;break;
						case 64: flag = true;break;
						case 32: flag = true;break;
						case 16: flag = true;break;
						default: flag = false;
						         JOptionPane.showMessageDialog(null,
						                "输入数字不正确，请重新输入!");
						         break;
				    }			
			    }while(!flag);				
				
				//转变为灰度图像
				pixels = digit.sample(pixels, iw, ih, gray);				
				showPix(graph, pixels, "采样结果");											
			}
        }
        else if (evt.getSource() == quantItem)
        { 
            setTitle("第2章 图像数字化 图像量化 作者 孙燮华");       	
        	if(loadflag)        	
        	{
        		pixels = common.grabber(iImage, iw, ih);				
				
				int level = common.getParam("量化级(256/128/64/"
				                            + "32/16/8/4/2):","128");
				
				//检查和调整输入参数	
				if(level>=2&&level<=256)
				{
				    int t = 1;
				    while(t<=level) { t = t*2;}
				    level = t;         
			    }
			    else if(level < 2)
			    {
			    	level = 2;
			    	JOptionPane.showMessageDialog(null,
					                "输入数字<2，已调整为2");						        
			    }			
			    else if(level > 256)
			    {
			    	level = 256;
			    	JOptionPane.showMessageDialog(null,
					                "输入数字>256，已调整为256");						        
			    }
	    		//对图像进行阈值变换
	    		pixels = digit.quantize(pixels, iw, ih, level);
						
				//将数组中的象素产生一个图像
				showPix(graph, pixels, "量化结果");								
			}
			else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");             	                     
	    }
        else if (evt.getSource() == exitItem) 
            System.exit(0);       
    }
    
    public void paint(Graphics g) 
    {    	  
        if (iImage != null && (!runflag))
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
		runflag = true;
	}
	
    public static void main(String[] args) 
    {  
        new Ch2Digitization();        
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
        
        Menu processMenu = new Menu("图像处理");
        samplItem = new MenuItem("采样");
        samplItem.addActionListener(this);
        processMenu.add(samplItem);
        
        processMenu.addSeparator();        
        quantItem = new MenuItem("量化");
        quantItem.addActionListener(this);
        processMenu.add(quantItem);
        
        MenuBar menuBar = new MenuBar();
        menuBar.add(fileMenu);
        menuBar.add(processMenu);       
        setMenuBar(menuBar);
    }
    
    MenuItem openItem;
    MenuItem exitItem;
    MenuItem samplItem;
    MenuItem quantItem; 
}
