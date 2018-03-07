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
            runflag   = false;    //ͼ����ִ�б�־ 
            
    Digitization digit;
    Common common;
    
    public Ch2Digitization()
    {    
        setTitle("����ͼ����-Java�����ʵ�� ��2�� ͼ�����ֻ�");
        this.setBackground(Color.lightGray);        
              
        //�˵�����
        setMenu();
        
        digit  = new Digitization();
        common = new Common();
        
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
            common.chooseFile(chooser, "./images/ch2", 0);//����Ĭ��Ŀ¼,�����ļ�
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
	                //װ��ͼ��
				    iImage = common.openImage(name,tracker);    
				    //ȡ����ͼ��Ŀ�͸�
				    iw = iImage.getWidth(null);
				    ih = iImage.getHeight(null);				    
				    repaint();
				    loadflag = true;
			    }			    			               
            }            
        }
        else if (evt.getSource() == samplItem)
        {
        	setTitle("��2�� ͼ�����ֻ� ͼ����� ���� ���ƻ�");
        	if(loadflag)        	
        	{
        		int gray = 64; 
        	    pixels = common.grabber(iImage, iw, ih);
				boolean flag = false;
				do{				
				    gray = common.getParam("��������(256/128/64/32/16):","128");
					
					//�������
					switch(gray)
				    {
						case 256:flag = true;break;
						case 128:flag = true;break;
						case 64: flag = true;break;
						case 32: flag = true;break;
						case 16: flag = true;break;
						default: flag = false;
						         JOptionPane.showMessageDialog(null,
						                "�������ֲ���ȷ������������!");
						         break;
				    }			
			    }while(!flag);				
				
				//ת��Ϊ�Ҷ�ͼ��
				pixels = digit.sample(pixels, iw, ih, gray);				
				showPix(graph, pixels, "�������");											
			}
        }
        else if (evt.getSource() == quantItem)
        { 
            setTitle("��2�� ͼ�����ֻ� ͼ������ ���� ���ƻ�");       	
        	if(loadflag)        	
        	{
        		pixels = common.grabber(iImage, iw, ih);				
				
				int level = common.getParam("������(256/128/64/"
				                            + "32/16/8/4/2):","128");
				
				//���͵����������	
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
					                "��������<2���ѵ���Ϊ2");						        
			    }			
			    else if(level > 256)
			    {
			    	level = 256;
			    	JOptionPane.showMessageDialog(null,
					                "��������>256���ѵ���Ϊ256");						        
			    }
	    		//��ͼ�������ֵ�任
	    		pixels = digit.quantize(pixels, iw, ih, level);
						
				//�������е����ز���һ��ͼ��
				showPix(graph, pixels, "�������");								
			}
			else
			 	JOptionPane.showMessageDialog(null, "���ȴ�ͼ��!");             	                     
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
            g.drawString("ԭͼ", 120, 320);
        }        
    }
    
    public void showPix(Graphics graph, int[] pixels, String str)
    {    
		//�������е����ز���һ��ͼ��
		ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
		Image oImage = createImage(ip);
		common.draw(graph, iImage, "ԭͼ", oImage, str);
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
        
        Menu processMenu = new Menu("ͼ����");
        samplItem = new MenuItem("����");
        samplItem.addActionListener(this);
        processMenu.add(samplItem);
        
        processMenu.addSeparator();        
        quantItem = new MenuItem("����");
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
