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
     
    boolean loadflag = false,       //����ͼ���־
            runflag  = false;       //ִ�д����־   
     
    int   iw, ih;
    int[] pixels;          
             
    ImageAnalyse analyse;
    Common common;
    
    public Ch9ImageAnalyse()
    {    
        setTitle("����ͼ����-Java�����ʵ�� ��9�� ͼ�����������");
        this.setBackground(Color.lightGray);        
              
        //�˵�����
        setMenu();
        
        analyse = new ImageAnalyse();
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
            common.chooseFile(chooser, "./images/ch9", 0);//����Ĭ��Ŀ¼,�����ļ�
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
	                //װ��ͼ��
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
        	    setTitle("��9�� ͼ����������� �������� ���� ���ƻ�");       					    
        		pixels = common.grabber(iImage, iw, ih);						
				
				//��ARGBͼ������pixels��Ϊ��ֵͼ������imb
				byte[] imb = common.toBinary(pixels, iw, ih);
				
				int[] sc = analyse.Outline(imb, iw, ih);
				
				//����ֵͼ������sc��ΪARGBͼ������pixels
				pixels = common.toARGB(sc, iw, ih);
				
				ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
		        oImage = createImage(ip);
		        common.drawIm(graph, iImage, oImage, sc[iw*ih], 
		                      sc[iw*ih+1], sc[iw*ih+2], sc[iw*ih+3]);
		        runflag = true;   	
        	}
        	else
			 	JOptionPane.showMessageDialog(null, "���ȴ�ͼ��!");				
        }
        else if (evt.getSource() == boundaryItem)        
        {        	
        	if(loadflag)        	
        	{
        		setTitle("��9�� ͼ����������� �߽��� ���� ���ƻ�");
        		pixels = common.grabber(iImage, iw, ih);						
				
				byte[] imb = common.toBinary(pixels, iw, ih);
								
				byte[] bn = analyse.Bound(imb, iw, ih);
								
				pixels = common.bin2Rgb(bn, iw, ih);
				
		        ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
		        oImage = createImage(ip);
		        common.draw(graph, iImage, "ԭͼ", oImage, "�߽���");
		        runflag = true;
        	}
        	else
			 	JOptionPane.showMessageDialog(null, "���ȴ�ͼ��!");				
        }
        else if (evt.getSource() == holenumItem)
        {        	
        	if(loadflag)        	
        	{
        		setTitle("��9�� ͼ����������� ����С�� ���� ���ƻ�");
        		pixels = common.grabber(iImage, iw, ih);						
				
				byte[] imb = common.toBinary(pixels, iw, ih);
								
				byte[] bm = analyse.DelHole(imb, iw, ih);
								
				pixels = common.bin2Rgb(bm, iw, ih);
				
		        ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
		        oImage = createImage(ip);
		        common.draw(graph, iImage, "ԭͼ", oImage, "����С��");
		        runflag = true;		        
        	}
        	else
			 	JOptionPane.showMessageDialog(null, "���ȴ�ͼ��!");				
        }
        else if (evt.getSource() == centerItem)
        {        	
        	if(loadflag)
        	{
        		setTitle("��9�� ͼ����������� �������� ���� ���ƻ�");
				pixels = common.grabber(iImage, iw, ih);						
				
				byte[] imb = common.toBinary(pixels, iw, ih);
				//��������		
				int[] sc = analyse.Center(imb, iw, ih);
				
				pixels = common.toARGB(imb, sc[0], sc[1], iw, ih);				
				
		        ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
		        oImage = createImage(ip);
		        common.draw(graph, iImage, "ԭͼ", oImage, "��������");
		        runflag = true;   
			}
			else
				JOptionPane.showMessageDialog(null, "���ȴ�ͼ��!");        	
        }
        else if (evt.getSource() == moment7Item)
        {
        	
        	if(loadflag)
        	{
        		setTitle("��9�� ͼ����������� ���㲻��� ���� ���ƻ�");
				pixels = common.grabber(iImage, iw, ih);						
				byte[] imb = common.toBinary(pixels, iw, ih);
				//���㲻���
				double[] mom = analyse.Moment7(imb, iw, ih);
				common.draw(graph, iImage, mom, "7�������");
		        runflag = true;  
			}
			else
				JOptionPane.showMessageDialog(null, "���ȴ�ͼ��!");        	
        }
        else if (evt.getSource() == thin1Item)
        {
        	if(loadflag)
        	{
        	    setTitle("��9�� ͼ����������� ϸ���㷨 ���� ���ƻ�");	
				pixels = common.grabber(iImage, iw, ih);						
				
				byte[] imb = common.toBinary(pixels, iw, ih);
				
				imb = analyse.Thinner1(imb, iw, ih);
				
				pixels = common.bin2Rgb(imb, iw, ih);
				
				//�������е����ز���һ��ͼ��
				ImageProducer ip = new MemoryImageSource(iw,ih,pixels,0,iw);
				oImage = createImage(ip);
				common.draw(graph, iImage, "ԭͼ", oImage, "ϸ�����");
				runflag = true;
				
			}				        	
        }
        else if (evt.getSource() == thin2Item)
        {
        	if(loadflag)
        	{
        		setTitle("��9�� ͼ����������� ϸ���㷨 ���� ���ƻ�");
				pixels = common.grabber(iImage, iw, ih);						
				
				byte[] imb = common.toBinary(pixels, iw, ih);
				
				imb = analyse.Thinner2(imb, iw, ih);
				
				pixels = common.bin2Rgb(imb, iw, ih);
				
				//�������е����ز���һ��ͼ��
				ImageProducer ip = new MemoryImageSource(iw,ih,pixels,0,iw);
				oImage = createImage(ip);
				common.draw(graph, iImage, "ԭͼ", oImage, "ϸ�����");
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
            g.drawString("ԭͼ", 120, 320);
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
        
        Menu analMenu = new Menu("ͼ�����");
        outlineItem = new MenuItem("�������ٺ��������");
        outlineItem.addActionListener(this);
        analMenu.add(outlineItem);
        
        analMenu.addSeparator();
        boundaryItem = new MenuItem("�߽���");
        boundaryItem.addActionListener(this);
        analMenu.add(boundaryItem);
        
        analMenu.addSeparator();         
        holenumItem = new MenuItem("����С��");
        holenumItem.addActionListener(this);
        analMenu.add(holenumItem);
        
        analMenu.addSeparator();        
        centerItem = new MenuItem("��������");
        centerItem.addActionListener(this);
        analMenu.add(centerItem);  
        
        moment7Item = new MenuItem("���㲻���");
        moment7Item.addActionListener(this);
        analMenu.add(moment7Item);               
        
        Menu thinerMenu = new Menu("ͼ��ϸ��");
        thin1Item = new MenuItem("ϸ���㷨1");
        thin1Item.addActionListener(this);
        thinerMenu.add(thin1Item);
        
        thin2Item = new MenuItem("ϸ���㷨2");
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
