package com.ninggc; /**
 * @Ch7ImageRestore.java
 * @Version 1.0 2010.02.17
 * @Author Xie-Hua Sun 
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import process.algorithms.ImageRestore;
import process.common.Common;

public class Ch7ImageRestore extends JFrame implements ActionListener
{
    Image iImage, oImage;
    
    int   iw, ih;
    int[] pixels;          
             
    boolean loadflag  = false,
            runflag   = false;    //图像处理执行标志 
            
    ImageRestore restore;
    Common common;
    
    public Ch7ImageRestore()
    {    
        setTitle("数字图像处理-Java编程与实验 第7章 图像恢复");
        this.setBackground(Color.lightGray);        
              
        //菜单界面
        setMenu();
        
        restore  = new ImageRestore();
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
        else if (evt.getSource() == blurItem)
        {
        	setTitle("第7章 图像恢复");
        	if(loadflag)        	
        	{        		 
        	    pixels = common.grabber(iImage, iw, ih);
				pixels = restore.imBlur(pixels, iw, ih);
				//将数组中的象素产生一个图像
				showPix(graph, pixels, "图像模糊");											
			}
        }
        else if (evt.getSource() == restoreItem)
        { 
            setTitle("第7章 图像恢复");       	
        	if(loadflag)        	
        	{      			
				pixels = restore.imRestore(pixels, iw, ih);						
				//将数组中的象素产生一个图像
				showPix(graph, pixels, "图像恢复");						
			}
			else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");             	                     
	    }
        else if (evt.getSource() == exitItem) 
            System.exit(0);       
    }
    
    public void paint(Graphics g) 
    {    	  
        if (iImage != null)
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
        new Ch7ImageRestore();        
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
        blurItem = new MenuItem("模糊图像");
        blurItem.addActionListener(this);
        processMenu.add(blurItem);
        
        restoreItem = new MenuItem("恢复图像");
        restoreItem.addActionListener(this);
        processMenu.add(restoreItem);
        
        MenuBar menuBar = new MenuBar();
        menuBar.add(fileMenu);
        menuBar.add(processMenu);            
        setMenuBar(menuBar);
    }
    
    MenuItem openItem;
    MenuItem exitItem;
    MenuItem blurItem;     
    MenuItem restoreItem;  
}
