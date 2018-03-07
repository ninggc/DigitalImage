package com.ninggc; /**
 * @Ch10ImageMorph.java
 * @Version 1.0 2010.02.21
 * @Author Xie-Hua Sun 
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import process.algorithms.ImageMorph;
import process.common.Common;

public class Ch10ImageMorph extends JFrame implements ActionListener,MouseListener
{
    Image iImage, oImage;
     
    boolean loadflag = false,       //输入图像标志
            runflag  = false,       //执行处理标志   
            fillflag = false;
    int   iw, ih;
    int[] pixels;          
             
    ImageMorph morph;
    Common common;
    
    public Ch10ImageMorph()
    {    
        setTitle("数字图像处理-Java编程与实验 第10章 图像形态学");
        this.setBackground(Color.lightGray);        
              
        //菜单界面
        setMenu();
        
        morph = new ImageMorph();
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
            common.chooseFile(chooser, "./images/ch10", 0);//设置默认目录,过滤文件
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
				    repaint();
				    loadflag = true;
			    }			    		    			               
            }                        
        }        
        else if (evt.getSource() == erode1Item)//腐蚀
        {
           	if(loadflag)        	
        	{        					    
        		setTitle("第10章 图像形态学 正方形结构腐蚀 作者 孙燮华");
	            removeMouseListener(this);
	            show(graph, 11, "正方形腐蚀");	             	
        	}
        	else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");				
        } 
        else if (evt.getSource() == erode2Item)
        {
        	if(loadflag)        	
        	{        					    
        		setTitle("第10章 图像形态学 菱形结构腐蚀 作者 孙燮华");
	            removeMouseListener(this);
	            show(graph, 12, "菱形腐蚀");	             	
        	}
        	else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");				
        }
        else if (evt.getSource() == erode3Item)
        {
        	if(loadflag)        	
        	{        			    
        		setTitle("第10章 图像形态学 八角结构腐蚀 作者 孙燮华");
	            removeMouseListener(this);
	            show(graph, 13, "八角腐蚀");	             	
        	}
        	else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");				
        }        
        else if (evt.getSource() == dilate1Item)//膨胀
        {
        	if(loadflag)        	
        	{        					    
        		setTitle("第10章 图像形态学 正方形结构膨胀 作者 孙燮华");
	            removeMouseListener(this);
	            show(graph, 21, "正方膨胀");	             	
        	}
        	else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");				
        }
        else if (evt.getSource() == dilate2Item)
        {
        	if(loadflag)        	
        	{        					    
        		setTitle("第10章 图像形态学 菱形结构膨胀 作者 孙燮华");
	            removeMouseListener(this);
	            show(graph, 22, "菱形膨胀");	             	
        	}
        	else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");				
        }
        else if (evt.getSource() == dilate3Item)
        {
        	if(loadflag)        	
        	{        					    
        		setTitle("第10章 图像形态学 八角结构膨胀 作者 孙燮华");
	            removeMouseListener(this);
	            show(graph, 23, "八角膨胀");	             	
        	}
        	else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");				
        }        
        else if (evt.getSource() == OPEN1Item)//开启
        {
        	if(loadflag)        	
        	{        					    
        		setTitle("第10章 图像形态学 正方形结构开启 作者 孙燮华");
	            removeMouseListener(this);
	            show(graph, 31, "正方开启");	             	
        	}
        	else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");				
        }
        else if (evt.getSource() == CLOSE1Item)//闭合
        {
        	if(loadflag)        	
        	{        					    
        		setTitle("第10章 图像形态学 正方结构闭合 作者 孙燮华");
	            removeMouseListener(this);
	            show(graph, 41, "正方结构闭合");	             	
        	}
        	else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");				
        }
        else if (evt.getSource() == filter1Item)//滤波1,正方结构
        {                	        	
        	if(loadflag)
        	{
        		setTitle("第10章 图像形态学 正方结构噪声滤波 作者 孙燮华");
	            removeMouseListener(this);
	            show(graph, 51, "正方滤波"); 
	        }		
			else
			 	JOptionPane.showMessageDialog(null, "请先打开图像!");
									    	
        }
        else if (evt.getSource() == edge1Item)//正方结构边界提取
        {                	        	
        	if(loadflag)
        	{
        		setTitle("第10章 图像形态学 正方结构边界提取 作者 孙燮华");
	            removeMouseListener(this);	                       
	            show(graph, 61, "边界提取");								
			}		
			else
				JOptionPane.showMessageDialog(null, "请先打开图像!");					    	
        }
        else if (evt.getSource() == fillItem)//区域填充
        {                	        	
        	if(loadflag)
        	{
        		setTitle("第10章 图像形态学 区域填充 作者 孙燮华");
        		addMouseListener(this);      		 
        		fillflag = true;
        	}		
			else
				JOptionPane.showMessageDialog(null, "请先打开图像!");					    	
        } 
        else if (evt.getSource() == eulerItem)//计算Euler数
        {                	        	
        	if(loadflag)
        	{
        		setTitle("第10章 图像形态学 计算Euler数 作者 孙燮华");
        		removeMouseListener(this);
        		pixels = common.grabber(iImage, iw, ih);						
				
		        //将ARGB图像序列pixels变为2值图像矩阵image2
		        byte[] imb = common.toBinary(pixels, iw, ih);
        	    //计算Euler数
        		int eulerN = morph.euler(imb, iw, ih);
	            common.draw(graph, iImage, "Euler数 = ", eulerN);
	            runflag = true;	            
        	}		
			else
				JOptionPane.showMessageDialog(null, "请先打开图像!");					    	
        }
        else if (evt.getSource() == hmtItem)//击中未击中变换
        {                	        	
        	if(loadflag)
        	{
        		setTitle("第10章 图像形态学 HMT变换 作者 孙燮华");
        		removeMouseListener(this);
        		show(graph, 71, "HMT变换");        	    			
			}		
			else
				JOptionPane.showMessageDialog(null, "请先打开图像!");					    	
        }
        else if (evt.getSource() == erodeItem)//灰度腐蚀
        {                	        	
        	if(loadflag)
        	{
        		setTitle("第10章 图像形态学 灰度腐蚀 作者 孙燮华");
        		SHOW(graph, 81, "灰度腐蚀");        			
			}								    	
        }
        else if (evt.getSource() == dilateItem)//灰度膨胀
        {                	        	
        	if(loadflag)
        	{
        		setTitle("第10章 图像形态学 灰度膨胀 作者 孙燮华");
        		SHOW(graph, 91, "灰度膨胀");        			
			}								    	
        }
        else if (evt.getSource() == OPENItem)//灰度开启
        {                	        	
        	if(loadflag)
        	{
        		setTitle("第10章 图像形态学 灰度开启 作者 孙燮华");
	           	SHOW(graph, 10, "灰度开启");	           					
			}			
        }
        else if (evt.getSource() == CLOSEItem)//灰度闭合
        {                	        	
        	if(loadflag)
        	{
        		setTitle("第10章 图像形态学 灰度闭合 作者 孙燮华");
	           	SHOW(graph, 11, "灰度闭合");	           						
			}			
        }
        else if (evt.getSource() == gradItem)
        {                	        	
        	if(loadflag)
        	{
        		setTitle("第10章 图像形态学 Sobel型锐化 作者 孙燮华");
	            SHOW(graph, 12, "Sobel型锐化");	            											
			}									    	
        }
        else if (evt.getSource() == filt1Item)//滤波1
        {                	        	
        	if(loadflag)
        	{
        		setTitle("第10章 图像形态学 形态学滤波1 作者 孙燮华");
	           	SHOW(graph, 13, "滤波1");	           							
			}									    	
        }
        else if (evt.getSource() == filt2Item)//滤波2
        {                	        	
        	if(loadflag)
        	{
        		setTitle("第10章 图像形态学 形态学滤波2 作者 孙燮华");
	           	SHOW(graph, 14, "滤波2");	           					
			}									    	
        }
        else if (evt.getSource() == filt3Item)//滤波3
        {                	        	
        	if(loadflag)
        	{
        		setTitle("第10章 图像形态学 形态学滤波3 作者 孙燮华");
	           	SHOW(graph, 15, "滤波3");	           					
			}									    	
        }
        else if (evt.getSource() == topHatItem)//高帽变换
        {                	        	
        	if(loadflag)
        	{
        		setTitle("第10章 图像形态学 高帽变换 作者 孙燮华");
        		SHOW(graph, 16, "高帽变换");       	     				
		    }								    	
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
        if(fillflag)
        {
            g.setColor(Color.red);
        	g.drawString("用鼠标选择填充种子点" , 40, 290);
        	fillflag = false;
        }                
    }
   	
   	public void mouseClicked(MouseEvent e)
   	{
   		Graphics g = getGraphics();
   		pixels = common.grabber(iImage, iw, ih);						
				
		//将ARGB图像序列pixels变为2值图像矩阵image2
		byte[] imb = common.toBinary(pixels, iw, ih);
		
    	int ix = e.getX() - 5; 
    	int iy = e.getY() - 50;
    	imb = morph.fill(imb, iw, ih, ix, iy);
				
		pixels = common.bin2Rgb(imb, iw, ih);
				
		//将数组中的象素产生一个图像
        ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
        oImage = createImage(ip);
		common.draw(g, iImage, "原图", oImage, "区域填充");				
        runflag = true;
    } 
    
    public void mousePressed(MouseEvent e){ }
    public void mouseReleased(MouseEvent e){ }
    public void mouseEntered(MouseEvent e){ }
    public void mouseExited(MouseEvent e){ }
    
   	/*************************************************
     * type - 型号. 0:BLPF 1:BHPF 2:ELPF 3:EHPF
     * name - 输出图像标题字符串
     *************************************************/    
    public void show(Graphics graph, int type, String name)
    {  
    	pixels = common.grabber(iImage, iw, ih);						
				
		//将ARGB图像序列pixels变为2值图像矩阵image2
		byte[] imb = common.toBinary(pixels, iw, ih);
		
		switch(type)
		{
			case 11: //正方形结构腐蚀
				    imb = morph.erode(imb, iw, ih, 11);
			        break;
			case 12: //菱形结构腐蚀
				    imb = morph.erode(imb, iw, ih, 12);
			        break;
			case 13: //八角结构腐蚀
				    imb = morph.erode(imb, iw, ih, 13);
			        break;
			case 21: //正方形结构膨胀
				    imb = morph.dilate(imb, iw, ih, 21);
			        break;
			case 22: //菱形结构膨胀
				    imb = morph.dilate(imb, iw, ih, 22);
			        break;
			case 23: //八角结构膨胀
				    imb = morph.dilate(imb, iw, ih, 23);
			        break;
			case 31: //正方形结构开启
			        imb = morph.erode (imb, iw, ih, 11);
			        imb = morph.dilate(imb, iw, ih, 21);
			        break;
			case 41: //正方形结构闭合			        
			        imb = morph.dilate(imb, iw, ih, 21);
			        imb = morph.erode (imb, iw, ih, 11);
			        break;
			case 51: //正方形结构滤波
					imb = morph.erode (imb, iw, ih, 11);
			        imb = morph.dilate(imb, iw, ih, 21);	        
			        imb = morph.dilate(imb, iw, ih, 21);
			        imb = morph.erode (imb, iw, ih, 11);
			        break;
			case 61: //正方形结构边界提取
				    imb = morph.edge(imb, iw, ih, 11);;
			        break;
			case 71: //HMT变换
    	            imb = morph.hmt(imb, iw, ih); 
    	            break;    	          
	    }    
	    pixels = common.bin2Rgb(imb, iw, ih);
				
		//将数组中的象素产生一个图像
        ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
        oImage = createImage(ip);
		common.draw(graph, iImage, "原图", oImage, name);				
        runflag = true; 	
    }
    
    public void SHOW(Graphics graph, int type, String name)
    {  
    	pixels = common.grabber(iImage, iw, ih);						
		
		switch(type)
		{
			case 81://灰度腐蚀
				    pixels = morph.grayErode(pixels, iw, ih, 1);
			        break;
			case 91://灰度膨胀       
				    pixels = morph.grayDilate(pixels, iw, ih, 1); 
				    break;
		    case 10://灰度开启
		            pixels = morph.grayErode(pixels, iw, ih, 1);
				    pixels = morph.grayDilate(pixels, iw, ih, 1);
				    break;
		    case 11://灰度闭合
		            pixels = morph.grayDilate(pixels, iw, ih, 1);
		            pixels = morph.grayErode(pixels, iw, ih, 1);				    
				    break;
		    case 12://Sobel型锐化
		            pixels = morph.Sobel(pixels, iw, ih);			    
				    break;
		    case 13://滤波1
		            pixels = morph.grayErode(pixels, iw, ih, 2); //开启
				    pixels = morph.grayDilate(pixels, iw, ih, 2);				
				    pixels = morph.grayDilate(pixels, iw, ih, 2);//闭合
				    pixels = morph.grayErode(pixels, iw, ih, 2);			    
				    break;
		    case 14://滤波2
		            pixels = morph.grayDilate(pixels, iw, ih, 2);//闭合
				    pixels = morph.grayErode(pixels, iw, ih, 2);
				    pixels = morph.grayErode(pixels, iw, ih, 2); //开启
				    pixels = morph.grayDilate(pixels, iw, ih, 2);				    
				    break;  
		    case 15://滤波3
		            pixels = morph.grayFilter(pixels, iw, ih);			    
				    break;
		    case 16://高帽变换
		            pixels = morph.grayTopHat(pixels, iw, ih);   
				    break;     
	    }           
	    
	    
		//将数组中的象素产生一个图像
        ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
        oImage = createImage(ip);
		common.draw(graph, iImage, "原图", oImage, name);				
        runflag = true; 	
    }
    public static void main(String[] args) 
    {  
        new Ch10ImageMorph();        
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
        
        Menu morphMenu = new Menu("二值形态学");
        erode1Item = new MenuItem("正方腐蚀");
        erode1Item.addActionListener(this);
        morphMenu.add(erode1Item);
        
        erode2Item = new MenuItem("菱形腐蚀");
        erode2Item.addActionListener(this);
        morphMenu.add(erode2Item);
        
        erode3Item = new MenuItem("八角腐蚀");
        erode3Item.addActionListener(this);
        morphMenu.add(erode3Item);
        
        morphMenu.addSeparator();
        dilate1Item = new MenuItem("正方膨胀");
        dilate1Item.addActionListener(this);
        morphMenu.add(dilate1Item);
        
        dilate2Item = new MenuItem("菱形膨胀");
        dilate2Item.addActionListener(this);
        morphMenu.add(dilate2Item);
        
        dilate3Item = new MenuItem("八角膨胀");
        dilate3Item.addActionListener(this);
        morphMenu.add(dilate3Item);
        
        morphMenu.addSeparator();
        OPEN1Item = new MenuItem("正方结构开启");
        OPEN1Item.addActionListener(this);
        morphMenu.add(OPEN1Item);
        
        CLOSE1Item = new MenuItem("正方结构闭合");
        CLOSE1Item.addActionListener(this);
        morphMenu.add(CLOSE1Item);
        
        morphMenu.addSeparator();
        filter1Item = new MenuItem("正方结构滤波");
        filter1Item.addActionListener(this);
        morphMenu.add(filter1Item);
        
        edge1Item = new MenuItem("正方结构边界提取");
        edge1Item.addActionListener(this);
        morphMenu.add(edge1Item);
        
        fillItem = new MenuItem("区域填充");
        fillItem.addActionListener(this);
        morphMenu.add(fillItem);
        
        eulerItem = new MenuItem("计算Euler数");
        eulerItem.addActionListener(this);
        morphMenu.add(eulerItem);
        
        hmtItem = new MenuItem("HMT变换");
        hmtItem.addActionListener(this);
        morphMenu.add(hmtItem);
        
        Menu grayMenu = new Menu("灰度形态学");
        
        erodeItem = new MenuItem("灰度腐蚀");
        erodeItem.addActionListener(this);
        grayMenu.add(erodeItem);
        
        dilateItem = new MenuItem("灰度膨胀");
        dilateItem.addActionListener(this);
        grayMenu.add(dilateItem);
        
        OPENItem = new MenuItem("开启");
        OPENItem.addActionListener(this);
        grayMenu.add(OPENItem);       
        
        CLOSEItem = new MenuItem("闭合");
        CLOSEItem.addActionListener(this);
        grayMenu.add(CLOSEItem);
        
        grayMenu.addSeparator();
        gradItem = new MenuItem("Sobel型锐化");
        gradItem.addActionListener(this);
        grayMenu.add(gradItem);
             
        filt1Item = new MenuItem("形态学滤波1");
        filt1Item.addActionListener(this);
        grayMenu.add(filt1Item);
        
        filt2Item = new MenuItem("形态学滤波2");
        filt2Item.addActionListener(this);
        grayMenu.add(filt2Item);
        
        filt3Item = new MenuItem("形态学滤波3");
        filt3Item.addActionListener(this);
        grayMenu.add(filt3Item);
        
        topHatItem = new MenuItem("高帽变换");
        topHatItem.addActionListener(this);
        grayMenu.add(topHatItem); 
        
        MenuBar menuBar = new MenuBar();
        menuBar.add(fileMenu);
        menuBar.add(morphMenu);
        menuBar.add(grayMenu);
        setMenuBar(menuBar);
    }
    
    MenuItem openItem;
    MenuItem exitItem;
    
    MenuItem erode1Item;      //正方结构腐蚀
    MenuItem erode2Item;      //菱形结构腐蚀
    MenuItem erode3Item;      //八角结构腐蚀
    
    MenuItem dilate1Item;     //正方结构膨胀
    MenuItem dilate2Item;     //菱形结构膨胀
    MenuItem dilate3Item;     //八角结构膨胀
    MenuItem OPEN1Item;       //正方结构开启    
    MenuItem CLOSE1Item;      //正方结构闭合
    
    MenuItem filter1Item;     //正方结构滤波
    MenuItem edge1Item;       //正方结构边缘提取
    MenuItem fillItem;        //区域填充
    MenuItem eulerItem;       //计算Euler数
    MenuItem hmtItem;         //击中未击中变换  
    
    MenuItem erodeItem;       //灰度腐蚀
    MenuItem dilateItem;      //灰度膨胀    
    MenuItem OPENItem;        //灰度开启    
    MenuItem CLOSEItem;       //灰度闭合
    
    MenuItem gradItem;        //Sobel型锐化        
    MenuItem filt1Item;       //形态学滤波1,先开后闭 
    MenuItem filt2Item;       //形态学滤波2,先闭后开
    MenuItem filt3Item;       //形态学滤波3,复合滤波 
    MenuItem topHatItem;      //高帽变换 
}
