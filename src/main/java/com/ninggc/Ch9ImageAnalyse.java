package com.ninggc; /**
 * @Ch9ImageAnalyse.java
 * @Version 1.0 2010.02.20
 * @Author Xie-Hua Sun 
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import process.algorithms.*;
import process.common.*;

public class Ch9ImageAnalyse extends JFrame implements ActionListener
{
    Image iImage, oImage;
     
    boolean loadflag = false,       //输入图像标志
            runflag  = false;       //执行处理标志   
     
    int   iw, ih;
    int[] pixels;          
             
    ImageAnalyse analyse;
    Common common;
    
    public Ch9ImageAnalyse()
    {    
        setTitle("数字图像处理-Java编程与实验 第9章 图像特征与分析");
        this.setBackground(Color.lightGray);        
              
        //菜单界面
        setMenu();
        
        analyse = new ImageAnalyse();
        common  = new Common();
        
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
            common.chooseFile(chooser, "./images/ch9", 0);//设置默认目录,过滤文件
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
				    iw = iImage.getWidth(null);
				    ih = iImage.getHeight(null);				    
				    repaint();
				    loadflag = true;
			    }			    		    			               
            }                        
        }
        else if (evt.getSource() == outlineItem)
        {        	
        	if(loadflag)        	
        	{ 
        	    setTitle("第9章 图像特征与分析 轮廓跟踪 作者 孙燮华");       					    
        		pixels = common.grabber(iImage, iw, ih);						
				
				//将ARGB图像序列pixels变为二值图像序列imb
				byte[] imb = common.toBinary(pixels, iw, ih);
				
				int[] sc = analyse.Outline(imb, iw, ih);
				
				//将二值图像序列sc变为ARGB图像序列pixels
				pixels = common.toARGB(sc, iw, ih);
				
				ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
		        oImage = createImage(ip);
		        common.drawIm(graph, iImage, oImage, sc[iw*ih], 
		                      sc[iw*ih+1], sc[iw*ih+2], sc[iw*ih+3]);
		        runflag = true;   	
        	}
        	else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");				
        }
        else if (evt.getSource() == boundaryItem)        
        {        	
        	if(loadflag)        	
        	{
        		setTitle("第9章 图像特征与分析 边界检测 作者 孙燮华");
        		pixels = common.grabber(iImage, iw, ih);						
				
				byte[] imb = common.toBinary(pixels, iw, ih);
								
				byte[] bn = analyse.Bound(imb, iw, ih);
								
				pixels = common.bin2Rgb(bn, iw, ih);
				
		        ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
		        oImage = createImage(ip);
		        common.draw(graph, iImage, "原图", oImage, "边界检测");
		        runflag = true;
        	}
        	else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");				
        }
        else if (evt.getSource() == holenumItem)
        {        	
        	if(loadflag)        	
        	{
        		setTitle("第9章 图像特征与分析 消除小洞 作者 孙燮华");
        		pixels = common.grabber(iImage, iw, ih);						
				
				byte[] imb = common.toBinary(pixels, iw, ih);
								
				byte[] bm = analyse.DelHole(imb, iw, ih);
								
				pixels = common.bin2Rgb(bm, iw, ih);
				
		        ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
		        oImage = createImage(ip);
		        common.draw(graph, iImage, "原图", oImage, "消除小洞");
		        runflag = true;		        
        	}
        	else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");				
        }
        else if (evt.getSource() == centerItem)
        {        	
        	if(loadflag)
        	{
        		setTitle("第9章 图像特征与分析 计算形心 作者 孙燮华");
				pixels = common.grabber(iImage, iw, ih);						
				
				byte[] imb = common.toBinary(pixels, iw, ih);
				//计算形心		
				int[] sc = analyse.Center(imb, iw, ih);
				
				pixels = common.toARGB(imb, sc[0], sc[1], iw, ih);				
				
		        ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
		        oImage = createImage(ip);
		        common.draw(graph, iImage, "原图", oImage, "计算形心");
		        runflag = true;   
			}
			else
				JOptionPane.showMessageDialog(null, "请先打开图像!");        	
        }
        else if (evt.getSource() == moment7Item)
        {
        	
        	if(loadflag)
        	{
        		setTitle("第9章 图像特征与分析 计算不变矩 作者 孙燮华");
				pixels = common.grabber(iImage, iw, ih);						
				byte[] imb = common.toBinary(pixels, iw, ih);
				//计算不变矩
				double[] mom = analyse.Moment7(imb, iw, ih);
				common.draw(graph, iImage, mom, "7个不变矩");
		        runflag = true;  
			}
			else
				JOptionPane.showMessageDialog(null, "请先打开图像!");        	
        }
        else if (evt.getSource() == thin1Item)
        {
        	if(loadflag)
        	{
        	    setTitle("第9章 图像特征与分析 细化算法 作者 孙燮华");	
				pixels = common.grabber(iImage, iw, ih);						
				
				byte[] imb = common.toBinary(pixels, iw, ih);
				
				imb = analyse.Thinner1(imb, iw, ih);
				
				pixels = common.bin2Rgb(imb, iw, ih);
				
				//将数组中的象素产生一个图像
				ImageProducer ip = new MemoryImageSource(iw,ih,pixels,0,iw);
				oImage = createImage(ip);
				common.draw(graph, iImage, "原图", oImage, "细化结果");
				runflag = true;
				
			}				        	
        }
        else if (evt.getSource() == thin2Item)
        {
        	if(loadflag)
        	{
        		setTitle("第9章 图像特征与分析 细化算法 作者 孙燮华");
				pixels = common.grabber(iImage, iw, ih);						
				
				byte[] imb = common.toBinary(pixels, iw, ih);
				
				imb = analyse.Thinner2(imb, iw, ih);
				
				pixels = common.bin2Rgb(imb, iw, ih);
				
				//将数组中的象素产生一个图像
				ImageProducer ip = new MemoryImageSource(iw,ih,pixels,0,iw);
				oImage = createImage(ip);
				common.draw(graph, iImage, "原图", oImage, "细化结果");
				runflag = true;
				
			}				        	
        }
        else if (evt.getSource() == exitItem) 
            System.exit(0);       
    }
    
    public void paint(Graphics g) 
    {    	  
        if (loadflag)
        {
        	g.clearRect(0, 0, 530, 350);        	
            g.drawImage(iImage, 5, 50, null);
            g.drawString("原图", 120, 320);
        }        
    }
   	
    public static void main(String[] args) 
    {  
        new Ch9ImageAnalyse();        
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
        
        Menu analMenu = new Menu("图像分析");
        outlineItem = new MenuItem("轮廓跟踪和链码编码");
        outlineItem.addActionListener(this);
        analMenu.add(outlineItem);
        
        analMenu.addSeparator();
        boundaryItem = new MenuItem("边界检测");
        boundaryItem.addActionListener(this);
        analMenu.add(boundaryItem);
        
        analMenu.addSeparator();         
        holenumItem = new MenuItem("消除小洞");
        holenumItem.addActionListener(this);
        analMenu.add(holenumItem);
        
        analMenu.addSeparator();        
        centerItem = new MenuItem("计算形心");
        centerItem.addActionListener(this);
        analMenu.add(centerItem);  
        
        moment7Item = new MenuItem("计算不变矩");
        moment7Item.addActionListener(this);
        analMenu.add(moment7Item);               
        
        Menu thinerMenu = new Menu("图形细化");
        thin1Item = new MenuItem("细化算法1");
        thin1Item.addActionListener(this);
        thinerMenu.add(thin1Item);
        
        thin2Item = new MenuItem("细化算法2");
        thin2Item.addActionListener(this);
        thinerMenu.add(thin2Item);
        
        
        MenuBar menuBar = new MenuBar();
        menuBar.add(fileMenu);
        menuBar.add(analMenu); 
        menuBar.add(thinerMenu);
        setMenuBar(menuBar);
    }
    
    MenuItem openItem;
    MenuItem exitItem;
    
    MenuItem outlineItem;
    MenuItem boundaryItem;
    MenuItem holenumItem;
    MenuItem centerItem;
    MenuItem moment7Item;
    
    MenuItem thin1Item;
    MenuItem thin2Item;
}
