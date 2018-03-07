package com.ninggc; /**
 * @Ch4GeomTrans.java
 * @Version 1.0 2010.02.14
 * @Author Xie-Hua Sun 
 */


import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import process.param.Parameters;
import process.common.Common;
import process.algorithms.GeomTrans;

public class Ch4GeomTrans extends JFrame implements ActionListener
{     
    Image iImage, oImage;
    BufferedImage bImage;
    
    boolean loadflag   = false,
            runflag    = false;
            
    int iw, ih,         //图像宽高
        ow, oh;         //镜象、错切、平移,输出图像宽高
        
    int[] pix, pix2,
          opix;         //输入图像序列; 
               
    //参数选择面板
    JButton okButton;
	JDialog dialog; 
     
    Common common;
    GeomTrans geom;
            
    public Ch4GeomTrans()
    {    
        setTitle("数字图像处理-Java编程与实验 第4章 图像几何变换");
        this.setBackground(Color.lightGray);
        
        //菜单界面
        setMenu();
        
        //关闭窗口
        closeWin();
        
        common = new Common();
        geom   = new GeomTrans();
            
        setSize(530, 330);
        setVisible(true);              
    }

    public void actionPerformed(ActionEvent evt)
    {
    	Graphics g = getGraphics();   	  
        if (evt.getSource() == openItem) 
        {  
            JFileChooser chooser = new JFileChooser();
            common.chooseFile(chooser, "./images", 0);//设置默认目录,过滤文件
            int r = chooser.showOpenDialog(null);
            
            if(r == JFileChooser.APPROVE_OPTION) 
            {  
                String name = chooser.getSelectedFile().getAbsolutePath();
                
                //装载图像
			    iImage = common.openImage(name, new MediaTracker(this));       
			    
			    //取载入图像的宽和高
			    iw = iImage.getWidth(null);
			    ih = iImage.getHeight(null);
			    bImage = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_RGB);
			    Graphics2D g2 = bImage.createGraphics();
			    g2.drawImage(iImage, 0, 0, null);			    
			    loadflag = true;
			    repaint();			               
            }
        }
        else if (evt.getSource() == rotateItem)//内置旋转
        {
        	setTitle("第4章 图像几何变换 内置旋转 作者 孙燮华");       	
        	common.draw(g, iImage, bImage, 
        	            common.getParam("旋转角(度):","30"), 0, 0);                  	
        }
        else if (evt.getSource() == scaleItem)//内置缩放
        {       	
        	setTitle("第4章 图像几何变换 内置缩放 作者 孙燮华");
        	//参数选择面板
        	Parameters pp = new Parameters("参数","x方向:",
        	                               "y方向:", "1.5", "1.5");
        	setPanel(pp, "内置缩放");
        	float x = pp.getPadx();
        	float y = pp.getPady(); 
        	common.draw(g, iImage, bImage, x, y, 1);  	
        }
        else if (evt.getSource() == shearItem)//内置错切
        {
        	setTitle("第4章 图像几何变换 内置错切 作者 孙燮华");
        	Parameters pp = new Parameters("参数","x方向:",
        	                               "y方向:", "0.5", "0.5");
        	setPanel(pp, "内置错切");
            float x = pp.getPadx();
        	float y = pp.getPady();
        	common.draw(g, iImage, bImage, x, y, 2);    	
        }
        else if (evt.getSource() == transItem)//内置平移
        {
        	setTitle("第4章 图像几何变换 内置平移 作者 孙燮华");
        	Parameters pp = new Parameters("参数","x方向:",
        	                               "y方向:", "100", "50");
        	setPanel(pp, "内置平移");                                         
        	float x = pp.getPadx();
        	float y = pp.getPady();
        	common.draw(g, iImage, bImage, x, y, 3);         	       	
        }
        else if (evt.getSource() == rotItem)//旋转算法
        {
        	setTitle("第4章 图像几何变换 旋转算法 作者 孙燮华");       	
        	pix = common.grabber(iImage, iw, ih);
			
			//旋转,输出图像宽高 
			int owh = (int)(Math.sqrt(iw * iw + ih * ih + 0.5));   
			opix = geom.imRotate(pix, common.getParam("旋转角(度):","30"), 
			                     iw, ih, owh);
			
			//将数组中的象素产生一个图像
			MemoryImageSource memoryImage  = new MemoryImageSource(owh, owh,
		                                         ColorModel.getRGBdefault(),
		                                                      opix, 0, owh);
		    oImage = createImage(memoryImage);
			common.draw(g, iImage, oImage, iw, ih, owh, 4);                  	
        }
        else if (evt.getSource() == mirItem)//镜象算法(type:5)
        {
        	setTitle("第4章 图像几何变换 镜象算法 作者 孙燮华");
        	Parameters pp = new Parameters("选择镜象类型", 
        	                               "水平", "垂直");
        	setPanel(pp, "镜象算法");
        	
        	pix  = common.grabber(iImage, iw, ih);
        	opix = geom.imMirror(pix, iw, ih, pp.getRadioState());
        	ImageProducer ip = new MemoryImageSource(iw, ih, opix, 0, iw);
		    oImage = createImage(ip); 
		    common.draw(g, iImage, oImage, iw, ih, 0, 5);  
        }
        else if (evt.getSource() == shrItem)//错切算法(type:6)
        {
        	setTitle("第4章 图像几何变换 错切算法 作者 孙燮华");
        	Parameters pp = new Parameters("参数","x方向:",
        	                               "y方向:", "0.5", "0.5");
        	setPanel(pp, "错切算法");
        	
        	pix  = common.grabber(iImage, iw, ih);
        	
        	float shx = pp.getPadx();
            float shy = pp.getPady();
            
            //计算包围盒的宽和高
            int ow = (int)(iw+(ih-1)*shx);
    	    int oh = (int)((iw-1)*shy+ih);
    	    
        	if(shx > 0 && shy > 0)
        	{        	
    	        opix = geom.imShear(pix, shx, shy, iw, ih, ow, oh);
    	        ImageProducer ip = new MemoryImageSource(ow, oh, opix, 0, ow);
		        oImage = createImage(ip);
		        common.draw(g, iImage, oImage, iw, ih, 0, 6); 
    	    }
        	else
    	        JOptionPane.showMessageDialog(null, "参数必须为正数!");               	             
    	}
    	else if (evt.getSource() == trnItem)
        {
        	setTitle("第4章 图像几何变换 平移算法 作者 孙燮华");
        	Parameters pp = new Parameters("参数","x方向:",
        	                               "y方向:", "100", "50");
        	setPanel(pp, "平移算法");
        	pix  = common.grabber(iImage, iw, ih);        	                     
        	int tx = (int)pp.getPadx();
            int ty = (int)pp.getPady();
            
            if(tx > 0 && ty > 0)
        	{        	
	            int ow = iw + tx;
	            int oh = ih + ty;      	
		        opix = geom.imTrans(pix, tx, ty, iw, ih, ow, oh);
		        ImageProducer ip = new MemoryImageSource(ow, oh, opix, 0, ow);
		        oImage = createImage(ip);
		        common.draw(g, iImage, oImage, iw, ih, 0, 7); 
	        }
        	else
    	        JOptionPane.showMessageDialog(null, "参数必须为正数!");    	                  	             
    	}
    	else if (evt.getSource() == nearItem)
        {
        	setTitle("第4章 图像几何变换 最邻近插值算法 作者 孙燮华");
            pix  = common.grabber(iImage, iw, ih); 
		    
		    float p = (Float.valueOf(JOptionPane.showInputDialog(null,
		               "输入缩放参数(0.1-3.0)", "1.50"))).floatValue();
    		int ow = (int) (p * iw);          //计算目标图宽高
	        int oh = (int) (p * ih);
		    opix = geom.nearNeighbor(pix, iw, ih, ow, oh, p);
		    ImageProducer ip = new MemoryImageSource(ow, oh, opix, 0, ow);
		    oImage = createImage(ip); 
            common.draw(g, oImage, "最邻近插值", p);       		
        }
        else if (evt.getSource() == linrItem)
        {
        	setTitle("第4章 图像几何变换 双线性插值算法 作者 孙燮华");
        	pix  = common.grabber(iImage, iw, ih); 
		    
		    float p = (Float.valueOf(JOptionPane.showInputDialog(null,
		               "输入缩放参数(0.1-3.0)", "1.50"))).floatValue();
    		int ow = (int) (p * iw);          //计算目标图宽高
	        int oh = (int) (p * ih);
		    opix = geom.bilinear(pix, iw, ih, ow, oh, p);
		    ImageProducer ip = new MemoryImageSource(ow, oh, opix, 0, ow);
		    oImage = createImage(ip); 
            common.draw(g, oImage, "双线性插值", p);               		
        }
        else if (evt.getSource() == cubicItem)
        {
        	setTitle("第4章 图像几何变换 三次卷积插值算法 作者 孙燮华");
        	pix  = common.grabber(iImage, iw, ih); 
		    
		    float p = (Float.valueOf(JOptionPane.showInputDialog(null,
		               "输入缩放参数(1.1-3.0)", "1.50"))).floatValue();
		    if(p < 1)
		    {		    
		        JOptionPane.showMessageDialog(null, "参数p必须大于1!");
		        return; 
		    }     
    		int ow = (int) (p * iw);          //计算目标图宽高
	        int oh = (int) (p * ih);
		    opix = geom.scale(pix, iw, ih, ow, oh, p, p);
		    ImageProducer ip = new MemoryImageSource(ow, oh, opix, 0, ow);
		    oImage = createImage(ip); 
            common.draw(g, oImage, "三次卷积插值", p);               		
        }           	  
        else if (evt.getSource() == okButton)
           	dialog.dispose(); 
        else if (evt.getSource() == exitItem)
            System.exit(0);                 
    }
        
    public void paint(Graphics g) 
    {    	  
        if (loadflag && (!runflag))
        {        	
        	g.clearRect(0,0,530,330);        	
        	Graphics2D g2D = (Graphics2D)g;               	       	
        	g2D.translate(0, 50);           //设置图像左上角为当前点
        	g.drawImage(iImage,0,0,null);   //画输入图        		
        }
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
        new Ch4GeomTrans();        
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
    
    public void setMenu()
    {
    	//菜单界面
        Menu fileMenu = new Menu("文件");
        openItem = new MenuItem("打开");
        openItem.addActionListener(this);
        fileMenu.add(openItem);

        exitItem = new MenuItem("退出");
        exitItem.addActionListener(this);
        fileMenu.add(exitItem);
    
        Menu affineMenu = new Menu("内置变换");        
        rotateItem = new MenuItem("内置旋转");
        rotateItem.addActionListener(this);
        affineMenu.add(rotateItem);        
        
        affineMenu.addSeparator();
        scaleItem = new MenuItem("内置缩放");
        scaleItem.addActionListener(this);
        affineMenu.add(scaleItem);
        
        affineMenu.addSeparator();
        shearItem = new MenuItem("内置错切");
        shearItem.addActionListener(this);
        affineMenu.add(shearItem);
        
        affineMenu.addSeparator();
        transItem = new MenuItem("内置平移");
        transItem.addActionListener(this);
        affineMenu.add(transItem);
        
        Menu affin2Menu = new Menu("仿射变换");
        rotItem = new MenuItem("旋转算法");
        rotItem.addActionListener(this);
        affin2Menu.add(rotItem);        
        
        affin2Menu.addSeparator();
        mirItem = new MenuItem("镜象算法");
        mirItem.addActionListener(this);
        affin2Menu.add(mirItem);
        
        affin2Menu.addSeparator();
        shrItem = new MenuItem("错切算法");
        shrItem.addActionListener(this);
        affin2Menu.add(shrItem);
        
        affin2Menu.addSeparator();
        trnItem = new MenuItem("平移算法");
        trnItem.addActionListener(this);
        affin2Menu.add(trnItem);
        
        Menu interpMenu = new Menu("图像插值");
        nearItem = new MenuItem("最邻近插值");
        nearItem.addActionListener(this);
        interpMenu.add(nearItem);
        
        interpMenu.addSeparator();
        linrItem = new MenuItem("双线性插值");
        linrItem.addActionListener(this);
        interpMenu.add(linrItem);
        
        interpMenu.addSeparator();
        cubicItem = new MenuItem("三次卷积插值");
        cubicItem.addActionListener(this);
        interpMenu.add(cubicItem);
                
        MenuBar menuBar = new MenuBar();
        menuBar.add(fileMenu);
        menuBar.add(affineMenu);
        menuBar.add(affin2Menu); 
        menuBar.add(interpMenu);              
        setMenuBar(menuBar);
    }
    
    MenuItem openItem;
    MenuItem exitItem;
    MenuItem rotateItem;//内置旋转(type:0)
    MenuItem scaleItem; //内置缩放(type:1)
    MenuItem shearItem; //内置错切(type:2)
    MenuItem transItem; //内置平移(typa:3)
     
    MenuItem rotItem;   //旋转算法(type:4)
    MenuItem mirItem;   //镜象算法(type:5)
    MenuItem shrItem;   //错切算法(type:6)
    MenuItem trnItem;   //平移算法(typa:7)
     
    MenuItem nearItem;  //最邻近插值算法
    MenuItem linrItem;  //双线性插值算法
    MenuItem cubicItem; //三次卷积插值算法
}
