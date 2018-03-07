/**
 * @Ch12ImageCompress.java
 * @Version 1.0 2010.02.23
 * @Author Xie-Hua Sun 
 */

package com.ninggc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import process.algorithms.imageCompress.*;
import process.common.*;
import process.rw.*;

public class Ch12ImageCompress extends JFrame implements ActionListener
{
    Image iImage, oImage;
    
    int[] pixels, opix;          
    byte[][] bpix;
    
    int iw, ih;
    int algnum;                      //算法代号 
 		      
    boolean loadflag = false,
            runflag  = false,        //图像处理执行标志
            algflag  = false;        //算法选择标志     
                              
    ImageCompress compress;
    Common common;
    
    public Ch12ImageCompress()
    {    
        setTitle("数字图像处理-Java编程与实验 第12章 图像压缩");
        this.setBackground(Color.lightGray);        
              
        //菜单界面
        setMenu();
        
        compress = new ImageCompress();
        common   = new Common();
        
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
	    	if(!algflag) 
    	        JOptionPane.showMessageDialog(null, "请先选择压缩算法!"); 
    	    else
    	    {  
	            if(algnum == 0||algnum == 2||algnum ==3)                	
	            {
	            	RAW raw = new RAW();
			        				        	
		        	if(algnum == 0)
		        	    //读取RAW图像
		                bpix = raw.readRAW_DAT("./images/ch12/clock.raw", iw, ih);
		            else if(algnum == 2)
		        	    //读取RAW图像
		                bpix = raw.readRAW_DAT("./images/ch12/rleTest.raw", iw, ih);
		            else if(algnum == 3)
		        	    //读取RAW图像
		                bpix = raw.readRAW_DAT("./images/ch12/lzwTest.raw", iw, ih); 
		                   
		        	//将字节数组pix转化为图像序列pixels
		        	pixels = common.byte2int(bpix, iw, ih);
		        	
		        	//将数组中的象素产生一个图像
				    ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
				    iImage = createImage(ip);  
				
		    	    loadflag = true;  
		            repaint();	
			    }
			    else
			    {
			    	//文件选择对话框
		            JFileChooser chooser = new JFileChooser();            
		            common.chooseFile(chooser, "./images/ch12", 3);//设置默认目录,过滤文件
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
			            	runflag   = false;	            	
			            }
			            
			            if(exn.equalsIgnoreCase("raw"))//RAW图像
		                {
		                    RAW raw = new RAW();
				        			        	
				        	iw = 256;
				        	ih = 256;
				        	
				        	//读取RAW图像
				            bpix = raw.readRAW_DAT(name, iw, ih);
				            
				        	//将字节数组pix转化为图像序列pixels
				        	pixels = common.byte2int(bpix, iw, ih);
				        	
				        	//将数组中的象素产生一个图像
						    ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
						    iImage = createImage(ip); 
						
				    	    loadflag = true;  
				            repaint();	
						}                
		                else                          //GIF,JPG,PNG
		                {                       
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
		        }              
	        }             
        }
        //压缩图像, 保存为clock_compressed.raw
        else if (evt.getSource() == compItem)
        {        	
        	if(loadflag)
	    	{
	    		setTitle("第12章 图像压缩 利用颜色表 作者 孙燮华");
	        	try
	        	{
	        		//得到颜色表colors_和搜索表search_
	        	   	compress.ToIndexedColor(bpix, iw, ih);
	        	}
	        	catch(AWTException ae){}
	        	compress.compress(iw, ih);
	            JOptionPane.showMessageDialog(null,"在目录images/compressed,\n"+
	                                          "保存图像clock.cmp成功!");
	        }
            else			
			{
				iw = 100; ih = 100;
				algflag = true;
				algnum  = 0;
			}		    	
        }
        else if (evt.getSource() == uncmItem)
        {        	
        	pixels = compress.uncompress("./images/compressed/clock.cmp", iw, ih);
		    showPix(graph, pixels, "解压缩");
		    algflag = false;
		    loadflag = false;		                          	                     
	    }
	    else if (evt.getSource() == huffItem)
	    {
	    	setTitle("第12章 图像压缩 Huffman编码 作者 孙燮华");
	        if(loadflag)
	    	{				
				pixels = common.grabber(iImage, iw, ih);				
				compress.huffCode(pixels, iw, ih);				
				algflag = false;
				loadflag = false;
			}
			else			
			{
				iw = 256; ih = 256;
				algflag = true;
				algnum  = 1;
			}	
	    }
	    else if (evt.getSource() == rleItem)
	    {
	        if(loadflag)
	    	{
	    		setTitle("第12章 图像压缩 RLE算法 作者 孙燮华");
	    		try
	        	{
	        		//得到颜色表colors_和搜索表search_
	        	   	compress.ToIndexedColor(bpix, iw, ih);
	        	}
	        	catch(AWTException ae){}
	    		compress.rleCompress(iw, ih);
            
                JOptionPane.showMessageDialog(null,"在目录images/compressed,\n"+
                                              "保存图像rleTest.rle成功!");                                        	
	    	}
	    	else			
			{
				iw = 256; ih = 256;
				algflag = true;
				algnum  = 2;
			}
	    }
	    else if (evt.getSource() == unrleItem)
	    {
	    	pixels = compress.rleUncomp("./images/compressed/rleTest.rle", iw, ih);
		    
		    //将数组中的象素产生一个图像
		    ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
		    oImage = createImage(ip);  
		    showPix(graph, pixels, "RLE解压缩"); 
		    algflag  = false;
		    loadflag = false;   	    
	    }
	    else if (evt.getSource() == lzwItem)
	    {
	        if(loadflag)
	    	{
	    		setTitle("第12章 图像压缩 LZW算法 作者 孙燮华");
	    		try
	        	{
	        		//得到颜色表colors_和搜索表search_
	        	   	compress.ToIndexedColor(bpix, iw, ih);
	        	}
	        	catch(AWTException ae){}
	    		compress.lzwCompress(iw, ih);
            
                JOptionPane.showMessageDialog(null,"在目录images/compressed,\n"+
                                              "保存图像lzwTest.lzw成功!");                                        	
	    	}
	    	else			
			{
				iw = 128; ih = 128;
				algflag = true;
				algnum  = 3;
			}
	    }
	    else if (evt.getSource() == unlzwItem)
	    {
	    	pixels = compress.lzwUncomp("./images/compressed/lzwTest.lzw", iw, ih);
	    	showPix(graph, pixels,"LZW解压缩");
	    	algflag  = false;
		    loadflag = false;
	    }
	    else if (evt.getSource() == dctItem)
	    {
	        if(loadflag)
	    	{
	    		if(iw != 256 || ih != 256)
				{
					JOptionPane.showMessageDialog(null,"目前只适用256X256图像压缩!");
					return;
				}
				setTitle("第12章 图像压缩 DCT压缩 作者 孙燮华"); 				
				pixels = common.grabber(iImage, iw, ih);
				
				compress.dctCompress(pixels, iw, ih);
				
                JOptionPane.showMessageDialog(null,"在目录images/compressed,\n"+
                                                   "保存图像test.dct成功!");                    
            }
	    	else			
			{
				algflag = true;
				algnum  = 4;
			}
	    }
	    //解压缩
	    else if(evt.getSource() == undctItem)
	    {
	    	opix = compress.dctUncomp(iw, ih, 8);
	    	showPix(graph, opix,"DCT解压缩");	
	    	algflag  = false;
		    loadflag = false;		
			JOptionPane.showMessageDialog(null,"峰值信噪比PSNR = "+ 
			                              common.PSNR(pixels, opix, iw, ih));
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
        new Ch12ImageCompress();        
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
        
        Menu compressMenu = new Menu("图像压缩");
        compItem = new MenuItem("用颜色表压缩");
        compItem.addActionListener(this);
        compressMenu.add(compItem);
        
        uncmItem = new MenuItem("用颜色表解压");
        uncmItem.addActionListener(this);
        compressMenu.add(uncmItem);
        compressMenu.addSeparator();
        
        huffItem = new MenuItem("Huffman编码");
        huffItem.addActionListener(this);
        compressMenu.add(huffItem);
        compressMenu.addSeparator();
         
        rleItem = new MenuItem("RLE压缩");
        rleItem.addActionListener(this);
        compressMenu.add(rleItem);
        
        unrleItem = new MenuItem("RLE解压");
        unrleItem.addActionListener(this);
        compressMenu.add(unrleItem); 
        compressMenu.addSeparator();
         
        lzwItem = new MenuItem("LZW压缩");
        lzwItem.addActionListener(this);
        compressMenu.add(lzwItem);
        
        unlzwItem = new MenuItem("LZW解压");
        unlzwItem.addActionListener(this);
        compressMenu.add(unlzwItem);
        compressMenu.addSeparator();
         
        dctItem = new MenuItem("DCT压缩");
        dctItem.addActionListener(this);
        compressMenu.add(dctItem);
        
        undctItem = new MenuItem("DCT解压");
        undctItem.addActionListener(this);
        compressMenu.add(undctItem);
                       
        MenuBar menuBar = new MenuBar();
        menuBar.add(fileMenu);
        menuBar.add(compressMenu);       
        setMenuBar(menuBar);
    }
    
    MenuItem openItem;
    MenuItem exitItem;
    
    MenuItem compItem;
    MenuItem uncmItem;
    MenuItem huffItem;
    MenuItem rleItem;
    MenuItem unrleItem;
    MenuItem lzwItem;
    MenuItem unlzwItem;
    MenuItem dctItem;
    MenuItem undctItem;
}
