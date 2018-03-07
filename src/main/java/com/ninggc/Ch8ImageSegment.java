package com.ninggc; /**
 * @Ch8ImageSegment.java
 * @Version 1.0 2010.02.18
 * @Author Xie-Hua Sun 
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import process.algorithms.ImageEnhance;
import process.algorithms.ImageSegment;
import process.common.Common;

public class Ch8ImageSegment extends JFrame implements ActionListener
{
    Image iImage, iImage2, oImage;
    
    int   iw, ih;
    int[] pixels;          
             
    boolean loadflag  = false,
            loadflag2 = false,
            runflag   = false;    //ͼ����ִ�б�־ 
     
    ImageEnhance enhance;
    ImageSegment segment;
    Common common;
    
    public Ch8ImageSegment()
    {    
        setTitle("����ͼ����-Java�����ʵ�� ��8�� ͼ��ָ�");
        this.setBackground(Color.lightGray);        
              
        //�˵�����
        setMenu();
        
        enhance = new ImageEnhance();
        segment = new ImageSegment();
        common  = new Common();
        
        //�رմ���
        closeWin();
        
        setSize(530, 330);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent evt)
    {
    	Graphics graph = getGraphics();
    	      	  
        if (evt.getSource() == openItem) 
        {
        	//�ļ�ѡ��Ի���
            JFileChooser chooser = new JFileChooser();
            common.chooseFile(chooser, "./images/ch8", 0);//����Ĭ��Ŀ¼,�����ļ�
            int r = chooser.showOpenDialog(null);
                        
            MediaTracker tracker = new MediaTracker(this);
            
            if(r == JFileChooser.APPROVE_OPTION) 
            {  
                String name = chooser.getSelectedFile().getAbsolutePath();
                 
                if(loadflag2||runflag)
                { 
                    loadflag  = false;
                    loadflag2 = false;
                    runflag   = false;
                }                   
			    if(!loadflag)
			    {
	                //װ��ͼ��
				    iImage = common.openImage(name, tracker);    
				    //ȡ����ͼ��Ŀ�͸�
				    iw = iImage.getWidth(null);
				    ih = iImage.getHeight(null);				    
				    repaint();
				    loadflag = true;
			    }
			    else if(loadflag && (!runflag))
			    {			        
				    iImage2 = common.openImage(name, tracker);    
				    common.draw(graph, iImage, "ԭͼ", iImage2, "ԭͼ2");
				    loadflag2 = true;				    			    	
			    }			    			               
            }                      
        }
        else if (evt.getSource() == KirschItem)
        {
        	setTitle("��8�� ͼ��ָ� ��Ե��� Kirsch���� ���� ���ƻ�");
        	if(loadflag)//1:Kirsch        	
        		show(graph, 1, "������ֵ(����400~600)", "500", "Kirsch");
        	else
			 	JOptionPane.showMessageDialog(null, "���ȴ�ͼ��!"); 		
        }
        else if (evt.getSource() == LaplaceItem)
        { 
            setTitle("��8�� ͼ��ָ� ��Ե��� Laplace���� ���� ���ƻ�");       	
        	if(loadflag)//2:Laplace        	
        	    show(graph, 2, "������ֵ(����50~150)", "100", "Laplace");        		
			else
			 	JOptionPane.showMessageDialog(null, "���ȴ�ͼ��!");             	                     
	    }
	    else if (evt.getSource() == LOGItem)
        { 
            setTitle("��8�� ͼ��ָ� ��Ե��� LOG���� ���� ���ƻ�");       	
        	if(loadflag)//20:LOG        	
        	    show(graph, 20, "������ֵ(����50~150)", "100", "LOG");        		
			else
			 	JOptionPane.showMessageDialog(null, "���ȴ�ͼ��!");             	                     
	    }
	    else if (evt.getSource() == LOG1Item)
        { 
            setTitle("��8�� ͼ��ָ� ��Ե��� LOG(����)���� ���� ���ƻ�");       	
        	if(loadflag)//21:LOG(����)        	
        	    show(graph, 21, "������ֵ(����50~150)", "100", "LOG(����)");        		
			else
			 	JOptionPane.showMessageDialog(null, "���ȴ�ͼ��!");             	                     
	    }
	    else if (evt.getSource() == PrewittItem)
        {        	 
            setTitle("��8�� ͼ��ָ� ��Ե��� Prewitt���� ���� ���ƻ�");       	
        	if(loadflag)//3:Prewitt        	
        	    show(graph, 3, "������ֵ(����100~250)", "180", "Prewitt");        						
			else
			 	JOptionPane.showMessageDialog(null, "���ȴ�ͼ��!");             	                     
	    }
	    else if (evt.getSource() == RobertsItem)
        { 
            setTitle("��8�� ͼ��ָ� ��Ե��� Roberts���� ���� ���ƻ�");       	
        	if(loadflag) //0:Roberts�㷨       	
        	    show(graph, 0, "������ֵ(����30~100)", "50", "Roberts");         	
			else
			 	JOptionPane.showMessageDialog(null, "���ȴ�ͼ��!");             	                     
	    }
	    else if (evt.getSource() == Roberts1Item)
        { 
            setTitle("��8�� ͼ��ָ� ��Ե��� ƽ�����Roberts�㷨 ���� ���ƻ�");       	
        	if(loadflag)//0:һ��ƽ�����Roberts�㷨        	
        	    show(graph, 0, "������ֵ(����20~60)", "40", "Roberts1");        		
			else
			 	JOptionPane.showMessageDialog(null, "���ȴ�ͼ��!");             	                     
	    }
	    else if (evt.getSource() == SobelItem)
        { 
            setTitle("��8�� ͼ��ָ� ��Ե��� Sobel���� ���� ���ƻ�");       	
        	if(loadflag)//5:Sobel        	
        	    show(graph, 5, "������ֵ(����100~300)", "200","Sobel");        		
			else
			 	JOptionPane.showMessageDialog(null, "���ȴ�ͼ��!");             	                     
	    }
	    else if (evt.getSource() == thresh1Item)
        {
        	setTitle("��8�� ͼ��ָ� ��ֵ�ָ� ���� ���ƻ�");
        	if(loadflag)//6: 1ά��ֵ�ָ�
        	    show(graph, 6, "1ά��ֵ�ָ�");				
	    }
	    else if (evt.getSource() == thresh2Item)
        {
        	setTitle("��8�� ͼ��ָ� 2ά��ֵ�ָ� ���� ���ƻ�");
        	if(loadflag)//7: 2ά��ֵ�ָ�
        	    show(graph, 7, "2ά��ֵ�ָ�");				
	    }
	    else if (evt.getSource() == bthreshItem)
        {
        	setTitle("��8�� ͼ��ָ� �����ֵ�ָ� ���� ���ƻ�");
        	if(loadflag)//8:�����ֵ�ָ�
        	    show(graph, 8, "�����ֵ�ָ�");				
	    }
	    else if (evt.getSource() == otsuItem)
        {
        	setTitle("��8�� ͼ��ָ� Otsu�㷨 ���� ���ƻ�");
        	if(loadflag)//9: Otsu�㷨
        	    show(graph, 9, "Otsu�㷨");				
	    }
	    else if (evt.getSource() == minusImItem)
        {
        	setTitle("��8�� ͼ��ָ� ��Ӱ�� ���� ���ƻ�");
        	if(loadflag && loadflag2)
        	    show(graph, 10, "��Ӱ��");
        	else
			    JOptionPane.showMessageDialog(null, "���ȴ�����ͼ��!");				
	    }
	    else if (evt.getSource() == lineDetectItem)
        {
        	setTitle("��8�� ͼ��ָ� Hough�任 ���� ���ƻ�");
        	if(loadflag)
        	    show(graph, 11, "ֱ�߼��");
        	else
			    JOptionPane.showMessageDialog(null, "���ȴ�����ͼ��!");				
	    }
	    else if (evt.getSource() == circDetectItem)
        {
        	setTitle("��8�� ͼ��ָ� Hough�任 ���� ���ƻ�");
        	if(loadflag)
        	    show(graph, 12, "Բ�ܼ��");
        	else
			    JOptionPane.showMessageDialog(null, "���ȴ�����ͼ��!");				
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
            g.drawString("ԭͼ", 120, 320);
        }        
    }
    
    /*************************************************
     * str  - �Ի���˵���ַ���
     * type - �����ͺ�. 0:Roberts, Roberts1
     *                  1:Kirsch 
     *                  2:Laplace 20:LOG  21:LOG(����)
     *                  3:Prewitt 5:Sobel
     * val  - �Ի������Ĭ��ֵ�ַ���
     * nam  - ���ͼ������ַ���
     *************************************************/    
    public void show(Graphics graph, int type, String str, String val, String nam)
    {
    	pixels = common.grabber(iImage, iw, ih);
		if(type != 0)
		    pixels = enhance.detect(pixels, iw, ih, type, 
		             common.getParam(str, val), true);
		else if(nam.equals("Roberts"))
		    pixels = enhance.robert(pixels, iw, ih, 
	    		     common.getParam(str, val), true);
	    else if(nam.equals("Roberts1"))
	        pixels = enhance.robert1(pixels, iw, ih, 
	    		     common.getParam(str, val));		    
		//�������е����ز���һ��ͼ��
		ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
		oImage = createImage(ip);
		common.draw(graph, iImage, "ԭͼ", oImage, nam);
		runflag = true;
    }

    /*************************************************
     * type - �ͺ�. 0:BLPF 1:BHPF 2:ELPF 3:EHPF
     * name - ���ͼ������ַ���
     *************************************************/    
    public void show(Graphics graph, int type, String name)
    {  
        int th;
        byte[] imb;
    	pixels = common.grabber(iImage, iw, ih);
		switch(type)
		{
			case 6: //1ά��ֵ�ָ�	            
	                th = segment.segment(pixels, iw, ih);	           
	                pixels = common.thSegment(pixels, iw, ih, th);
			        break;
			case 7: //2ά��ֵ�ָ�	            
	                th = segment.segment2(pixels, iw, ih);
	                pixels = common.thSegment(pixels, iw, ih, th);	  
				    break;		
	        case 8: //�����ֵ�ָ�	            
	                th = segment.bestThresh(pixels, iw, ih);
	                pixels = common.thSegment(pixels, iw, ih, th);	  
				    break;
		    case 9: //Otsu�㷨	            
	                th = segment.otsuThresh(pixels, iw, ih);
	                pixels = common.thSegment(pixels, iw, ih, th);	  
				    break;
		    case 10://��Ӱ��
		            int[] pix2 = common.grabber(iImage2, iw, ih);	            
	                pixels = segment.minusImage(pixels, pix2, iw, ih);	                	  
				    break;
		    case 11://ֱ�߼��
		            imb = common.rgb2Bin(pixels, iw, ih);
				    //Hough�任���ֱ��
				    imb = segment.detectLine(imb, iw, ih);				
				    //��2ֵͼ�����image2��ΪARGBͼ������pixels
				    pixels = common.bin2Rgb(imb, iw, ih);	                	  
				    break;
		    case 12://Բ�ܼ��
		            imb = common.rgb2Bin(pixels, iw, ih);
				    //Hough�任���ֱ��
				    imb = segment.detectCirc(imb, iw, ih);				
				    //��2ֵͼ�����image2��ΪARGBͼ������pixels
				    pixels = common.bin2Rgb(imb, iw, ih);	                	  
				    break;
	    }    
	    //�������е����ز���һ��ͼ��
		ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
		oImage = createImage(ip);
		common.draw(graph, iImage, "ԭͼ", oImage, name);
		runflag = true;		
    }
    
    public static void main(String[] args) 
    {  
        new Ch8ImageSegment();        
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
    
    //�˵�����
    public void setMenu()
    {    	
        Menu fileMenu = new Menu("�ļ�");
        openItem = new MenuItem("��");
        openItem.addActionListener(this);
        fileMenu.add(openItem);

        exitItem = new MenuItem("�˳�");
        exitItem.addActionListener(this);
        fileMenu.add(exitItem);
    
        Menu edgeMenu = new Menu("��Ե���");
        KirschItem = new MenuItem("Kirsch����");
        KirschItem.addActionListener(this);
        edgeMenu.add(KirschItem);
        
        edgeMenu.addSeparator();
        LaplaceItem = new MenuItem("Laplace����");
        LaplaceItem.addActionListener(this);
        edgeMenu.add(LaplaceItem);
        
        LOGItem = new MenuItem("LOG����");
        LOGItem.addActionListener(this);
        edgeMenu.add(LOGItem);
        
        LOG1Item = new MenuItem("LOG����(����)");
        LOG1Item.addActionListener(this);
        edgeMenu.add(LOG1Item);
        
        edgeMenu.addSeparator();         
        PrewittItem = new MenuItem("Prewitt����");
        PrewittItem.addActionListener(this);
        edgeMenu.add(PrewittItem);
        
        edgeMenu.addSeparator();        
        RobertsItem = new MenuItem("Roberts����");
        RobertsItem.addActionListener(this);
        edgeMenu.add(RobertsItem);
        
        Roberts1Item = new MenuItem("Roberts����(ƽ�����)");
        Roberts1Item.addActionListener(this);
        edgeMenu.add(Roberts1Item);
        
        edgeMenu.addSeparator();        
        SobelItem = new MenuItem("Sobel����");
        SobelItem.addActionListener(this);
        edgeMenu.add(SobelItem);
        
        Menu imSegMenu = new Menu("ͼ��ָ�");
        thresh1Item = new MenuItem("1ά��ֵ�ָ�");
        thresh1Item.addActionListener(this);
        imSegMenu.add(thresh1Item);
        
        thresh2Item = new MenuItem("2ά��ֵ�ָ�");
        thresh2Item.addActionListener(this);
        imSegMenu.add(thresh2Item);
        
        imSegMenu.addSeparator();
        bthreshItem = new MenuItem("�����ֵ�ָ�");
        bthreshItem.addActionListener(this);
        imSegMenu.add(bthreshItem);
        
        imSegMenu.addSeparator();
        otsuItem = new MenuItem("Otsu�㷨");
        otsuItem.addActionListener(this);
        imSegMenu.add(otsuItem);
        
        imSegMenu.addSeparator();
        minusImItem = new MenuItem("��Ӱ��");
        minusImItem.addActionListener(this);
        imSegMenu.add(minusImItem);
        
        Menu houghMenu = new Menu("Hough�任");
        lineDetectItem = new MenuItem("���ֱ��");
        lineDetectItem.addActionListener(this);
        houghMenu.add(lineDetectItem);
        
        circDetectItem = new MenuItem("���Բ��");
        circDetectItem.addActionListener(this);
        houghMenu.add(circDetectItem);        
        
        MenuBar menuBar = new MenuBar();
        menuBar.add(fileMenu);
        menuBar.add(edgeMenu);
        menuBar.add(imSegMenu);
        menuBar.add(houghMenu);     
        setMenuBar(menuBar);
    }
    
    MenuItem openItem;
    MenuItem exitItem;
    //��Ե���
    MenuItem KirschItem;
    MenuItem LaplaceItem;
    MenuItem LOGItem;
    MenuItem LOG1Item;
    MenuItem PrewittItem;
    MenuItem RobertsItem;
    MenuItem Roberts1Item;
    MenuItem SobelItem;
    
    MenuItem thresh1Item;       //һά��ֵ�ָ�  
    MenuItem thresh2Item;       //��ά��ֵ�ָ�  
    MenuItem bthreshItem;       //�����ֵ�ָ�
    MenuItem otsuItem;          //Otsu�㷨�ָ�
    MenuItem minusImItem;       //��Ӱ��
    
    MenuItem lineDetectItem;    //ֱ�߼��
    MenuItem circDetectItem;    //Բ�ܼ��
}
