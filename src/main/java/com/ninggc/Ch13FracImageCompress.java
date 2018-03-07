/**
 * @Ch13FracImageCompress.java
 * @Version 1.0 2010.02.24
 * @Author Xie-Hua Sun 
 */

package com.ninggc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import process.rw.RAW;
import process.algorithms.FracCompress;
import process.common.Common;

public class Ch13FracImageCompress extends JFrame implements ActionListener
{
    Image iImage, oImage;
    int   iw, ih;
    int[][] iPix;   
    int[] pixels;          
            
    boolean loadflag  = false,
            runflag   = false;    //图像处理执行标志 
            
    FracCompress fcomp;
    Common common;
    
    public Ch13FracImageCompress()
    {    
        setTitle("数字图像处理-Java编程与实验 第13章 分形图像压缩");
        this.setBackground(Color.lightGray);        
        
        iw = 256;
        ih = 256;
                     
        //菜单界面
        setMenu();
        
        fcomp  = new FracCompress();
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
            common.chooseFile(chooser, "./images/ch13", 1);//设置默认目录,过滤文件
            int r = chooser.showOpenDialog(null);
                        
            MediaTracker tracker = new MediaTracker(this);
            
            if(r == JFileChooser.APPROVE_OPTION) 
            {
            	iPix = new int[iw][ih];
            	  
                String name = chooser.getSelectedFile().getAbsolutePath();  
                                
                //读取RAW图像
                RAW reader = new RAW();
                iPix = reader.readRAW(name, iw, ih);
                
			   	//将字节数组pix转化为图像序列pixels
            	pixels = common.toPixels(iPix, iw, ih);
            	
            	//将数组中的象素产生一个图像
			    ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
			    iImage = createImage(ip);  
			    
  	    	    loadflag = true;  
  	            repaint();     	
            }            
        }
        else if (evt.getSource() == compItem)
        {
        	if(loadflag)        	
        	{        		
				fcomp.fracCompress(iPix);
				JOptionPane.showMessageDialog(null,"在目录images/compressed,\n"+
	                     "保存图像compressed.frc成功!");	                     										
			}
        }
        else if (evt.getSource() == uncmItem)
        {       	
        	if(loadflag)        	
        	{
        		int[][] om = fcomp.fracDecompress();
        	
			   	//将字节数组pix转化为图像序列pixels
	        	pixels = common.toPixels(om, iw, ih);
	        	
	        	//将数组中的象素产生一个图像
			    ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
			    oImage = createImage(ip);  
			    
	    	    loadflag = true; 
	    	    runflag = true; 
	            repaint();									
			}
			else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");             	                     
	    }
	    else if (evt.getSource() == juliaItem)
        {
        	double p = -0.74543, q = 0.11301;        
            //double p = 0.11, q = 0.66;
        	pixels = fcomp.JuliaSet(300, 230, p, q); 
        	ImageProducer ip = new MemoryImageSource(300, 230, pixels, 0, 300);
			oImage = createImage(ip);
			common.draw(graph, oImage, 240, 50, "Julia分形");	        
        }
        else if (evt.getSource() == kochItem)
        {
        	fcomp.Koch(graph, 25, 100, 275, 100); 
        }
        else if (evt.getSource() == mandItem)
        {
        	pixels = fcomp.Mandelbrot(300, 300); 
        	ImageProducer ip = new MemoryImageSource(300, 300, pixels, 0, 300);
			oImage = createImage(ip);
			common.draw(graph, oImage, 240, 50, "Mendelbrot分形");		
        }
        else if (evt.getSource() == fernItem)
        {
        	pixels = fcomp.bfern(220, 260);        	
        	ImageProducer ip = new MemoryImageSource(220, 260, pixels, 0, 220);
			oImage = createImage(ip);
			common.draw(graph, oImage, 240, 50, "Barnsley枫叶");			
        }
        else if (evt.getSource() == ifsItem)
           	runIFS(200, 200);
        else if (evt.getSource() == exitItem) 
            System.exit(0);       
    }
    
    //调用paint()方法，显示图像信息。
    public void paint(Graphics g)
    {    	
  	    if(loadflag&&(!runflag))
  	    {
  	    	g.clearRect(0, 0, 530, 330);		  	
			g.drawImage(iImage, 5, 50, this);
			g.drawString("源图像", 120, 320);
		} 
		else if(runflag)
		{
			g.clearRect(0, 0, 530, 330);
			g.drawImage(iImage, 5, 50, this);
			g.drawImage(oImage, 270, 50, this);
			g.drawString("源图像", 120, 320);
			g.drawString("解压图像", 380, 320);
		}	
    }    
   	 	
   	public void runIFS(int w, int h)
    {
    	byte[] sier = new byte[w*h];
        ImageProducer ip;
        Graphics graph = getGraphics();
               
        //初始化图像x
	    for(int i = 0; i < w*h; i++)
	    	sier[i] = 1; 
	    	   
	    //==============迭代演示===============
	    for(int i = 0 ; i < 10; i++)
	    {	    
		    pixels = common.bin2Rgb(sier, w, h);
		  
		    ip = new MemoryImageSource(w, h, pixels, 0, w);
			oImage = createImage(ip);
			common.draw(graph, oImage, 260, 50, "迭代"+i+"次");			
			
			JOptionPane.showMessageDialog(null,"点击进入第"+(i+1)+"次迭代");			           
			           
			sier = fcomp.sierpinski(sier, w, h);
		}	            
	    //============迭代演示结束============
	    	     
	    pixels = common.bin2Rgb(sier, w, h);
	    ip = new MemoryImageSource(w, h, pixels, 0, w);
		oImage = createImage(ip);	 
		common.draw(graph, oImage, 260, 50, "迭代10次");					
    }
    
    public static void main(String[] args) 
    {  
        new Ch13FracImageCompress();        
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
        
        Menu processMenu = new Menu("图像编码");
        compItem = new MenuItem("压   缩");
        compItem.addActionListener(this);
        processMenu.add(compItem);
        
        uncmItem = new MenuItem("解压缩");
        uncmItem.addActionListener(this);
        processMenu.add(uncmItem);
        
        Menu fracMenu = new Menu("分形演示");
        juliaItem = new MenuItem("Julia分形");
        juliaItem.addActionListener(this);
        fracMenu.add(juliaItem);
        
        fracMenu.addSeparator();
        kochItem = new MenuItem("Koch曲线");
        kochItem.addActionListener(this);
        fracMenu.add(kochItem);
        
        fracMenu.addSeparator();
        mandItem = new MenuItem("Mandelbrot分形");
        mandItem.addActionListener(this);
        fracMenu.add(mandItem);
        
        fracMenu.addSeparator();
        fernItem = new MenuItem("Barnsley枫叶");
        fernItem.addActionListener(this);
        fracMenu.add(fernItem);
        
        fracMenu.addSeparator();
        ifsItem = new MenuItem("IFS试验");
        ifsItem.addActionListener(this);
        fracMenu.add(ifsItem);
        
        MenuBar menuBar = new MenuBar();
        menuBar.add(fileMenu);
        menuBar.add(processMenu);
        menuBar.add(fracMenu);       
        setMenuBar(menuBar);
    }
    
    MenuItem openItem;
    MenuItem exitItem;
    MenuItem compItem;
    MenuItem uncmItem;
    MenuItem juliaItem;
    MenuItem kochItem;
    MenuItem mandItem;
    MenuItem fernItem;
    MenuItem ifsItem;
}
