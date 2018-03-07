/**
 * @Ch14ImageCipher.java
 * @Version 1.0 2010.03.06
 * @Author Xie-Hua Sun 
 */

package com.ninggc;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;

import process.common.Common;
import process.param.Parameters;
import process.algorithms.ImageCipher;


public class Ch14ImageCipher extends JFrame implements ActionListener
{
    Image iImage, oImage;
    BufferedImage bImage;
    
    int   iw, ih;
    int[] pixels,
          cphpix;                //����ͼ������ 
     
    boolean loadflag = false,
            runflag  = false;    //ͼ����ִ�б�־ 
    String imn,                  //ͼ���ļ���
           imh; 
                            //����ͼ���ʶ"c_"
    //����ѡ�����
    JButton okButton;
	JDialog dialog;
	        
    ImageCipher cipher;
    Common common;
    
    public Ch14ImageCipher()
    {    
        setTitle("����ͼ����-Java�����ʵ�� ��14�� ͼ�����");
        this.setBackground(Color.lightGray);        
              
        //�˵�����
        setMenu();
                
        cipher = new ImageCipher();
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
            common.chooseFile(chooser, "./images/ch14", 0);//����Ĭ��Ŀ¼,�����ļ�
            int r = chooser.showOpenDialog(null);
                        
            MediaTracker tracker = new MediaTracker(this);
            
            if(r == JFileChooser.APPROVE_OPTION) 
            {  
                //·�����ļ�ȫ��
                String name = chooser.getSelectedFile().getAbsolutePath();
                
                //ȡ�ļ������䳤�� 
                String fname = chooser.getSelectedFile().getName(); 
                int len = fname.length();
                
                //ȡ���ļ���imn�ͼ��ܱ�ʶimh
                imn = fname.substring(0, len-4);
                imh = fname.substring(0, 2);
                
                if(runflag)
                {
                	loadflag = false;   
                    runflag  = false; 
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
            }            
        }
        else if (evt.getSource() == arnoldItem)
        {
        	if(loadflag)        	
        	{
        		setTitle("��14�� ͼ����� Arnold�任���� ���� ���ƻ�");
        		if(iw != ih)
        		{
        			JOptionPane.showMessageDialog(null, "�������ڿ�����ͼ��!");
        			loadflag = false;
        			return;        			
        		}
        		pixels = common.grabber(iImage, iw, ih);
				
				//�趨��Կ
				int key = common.getParam("������Կ(111-999)","888");	
				pixels = cipher.arnold_mtr(pixels, iw, key, 1);
			    oImage = showPix(graph, pixels, "����ͼ��");
			    //�洢JPEGͼ��
			    File file = new File("./images/ch14/jpg/c_"+imn+".jpg");
	            bImage = (BufferedImage)this.createImage(iw, ih);
			    bImage.createGraphics().drawImage(oImage, 0, 0, this);
			    common.saveImageAsJPEG(bImage, file);
			    JOptionPane.showMessageDialog(null, "��Ŀ¼images/ch14/jpg�洢\n"
			                                  +"c_"+imn+".jpg����ͼ��ɹ�!");			   											
			}
        }
        else if (evt.getSource() == iarnoldItem)
        {        	
        	if(loadflag)        	
        	{   
        	    if(!imh.equals("c_"))      //δ��JPEG����
        	    {   	    
        	        cphpix = pixels;
					//�趨��Կ				
					int key = common.getParam("������Կ(111-999)","888");				 
					pixels = cipher.arnold_mtr(cphpix, iw, key, -1);
				    showPix(graph, pixels, "����ͼ��");	
			    }
			    else                       //��JPEG����
			    {
			        cphpix = common.grabber(iImage, iw, ih);
					//�趨��Կ				
					int key = common.getParam("������Կ(111-999)","888");				 
					pixels = cipher.arnold_mtr(cphpix, iw, key, -1);
				    showPix(graph, pixels, "���ܹ���ͼ");		
			    }		    								
			}
			else
			 	JOptionPane.showMessageDialog(null, "���ȴ�ͼ��!");            	                     
	    }
	    else if (evt.getSource() == logisticItem)
        {
        	if(loadflag)        	
        	{
        		setTitle("��14�� ͼ����� Logistic�������� ���� ���ƻ�");       		
        		pixels = common.grabber(iImage, iw, ih);
				
				//�趨��Կ
				int key = common.getParam("������Կ(111-999)","888");				 				  		
				pixels = cipher.logistic(pixels, iw, ih, key, 1);    	        					
			    oImage = showPix(graph, pixels, "����ͼ��");
			    
			    File file = new File("./images/ch14/jpg/c_"+imn+".jpg");
	            bImage = (BufferedImage)this.createImage(iw, ih);
			    bImage.createGraphics().drawImage(oImage, 0, 0, this);
			    common.saveImageAsJPEG(bImage, file);
			    JOptionPane.showMessageDialog(null, "��Ŀ¼images/ch14/jpg�洢\n"
			                                  +"c_"+imn+".jpg����ͼ��ɹ�!");			   											
			}
        }
        else if (evt.getSource() == ilogisticItem)
        {        	
        	if(loadflag)        	
        	{
        		if(!imh.equals("c_"))      //δ��JPEG����
        	    {        					
					cphpix = pixels;
					//�趨��Կ
					int key = common.getParam("������Կ(111-999)","888");						
					pixels = cipher.logistic(cphpix, iw, ih, key, -1);
				    showPix(graph, pixels, "����ͼ��");
			    }
			    else                       //��JPEG����
			    {
			        cphpix = common.grabber(iImage, iw, ih);;
					//�趨��Կ				
					int key = common.getParam("������Կ(111-999)","888");				 
					pixels = cipher.logistic(cphpix, iw, ih, key, -1);
				    showPix(graph, pixels, "���ܹ���ͼ");		
			    }			    								
			}
			else
			 	JOptionPane.showMessageDialog(null, "���ȴ�ͼ��!");            	                     
	    }
	    else if (evt.getSource() == logisXorItem)
        {
        	if(loadflag)        	
        	{
        		setTitle("��14�� ͼ����� Logistic���м��� ���� ���ƻ�");        		
        		pixels = common.grabber(iImage, iw, ih);
				
				//�趨��Կ
				int key = common.getParam("������Կ(111-999)","888");				 				  		
				pixels = cipher.logisticXor(pixels, iw, ih, key);    	        					
			    showPix(graph, pixels, "����ͼ��");			   											
			}
        }
        else if (evt.getSource() == ilogisXorItem)
        {        	
        	if(loadflag)        	
        	{        					
				//�趨��Կ
				cphpix = pixels;				
				int key = common.getParam("������Կ(111-999)","888");
				pixels = cipher.logisticXor(cphpix, iw, ih, key);
			    showPix(graph, pixels, "����ͼ��");			    								
			}
			else
			 	JOptionPane.showMessageDialog(null, "���ȴ�ͼ��!");            	                     
	    }
	    else if (evt.getSource() == fibonXorItem)
        {
        	if(loadflag)        	
        	{
        		setTitle("��14�� ͼ����� Fibonacci���м��� ���� ���ƻ�");        		
        		pixels = common.grabber(iImage, iw, ih);
				
				//�趨��Կ
				int key = common.getParam("������Կ(111-999)","888");				 				  		
				pixels = cipher.fibonacciXor(pixels, iw, ih, key);    	        					
			    showPix(graph, pixels, "����ͼ��");			   											
			}
        }
        else if (evt.getSource() == ifibonXorItem)
        {        	
        	if(loadflag)        	
        	{        					
				//�趨��Կ
				cphpix = pixels;				
				int key = common.getParam("������Կ(111-999)","888");
				pixels = cipher.fibonacciXor(cphpix, iw, ih, key);
			    showPix(graph, pixels, "����ͼ��");			    								
			}
			else
			 	JOptionPane.showMessageDialog(null, "���ȴ�ͼ��!");            	                     
	    }	    
	    else if(evt.getSource() == dctItem)              //DCTƵ�����
	    {	    	 	
	    	//�����ȼ���ͼ��,Ȼ��ſ��Խ���DCT�任
			if(loadflag)			
			{
				setTitle("��14�� ͼ����� DCT����� ���� ���ƻ�");
				if(iw != 256||ih != 256)
				{
				    JOptionPane.showMessageDialog(null, "�������ڿ�����ͼ��!");
        			loadflag = false;
        			return;	
				}
				pixels = common.grabber(iImage, iw, ih);
				//DCT�����
				pixels = cipher.dctCipher(pixels, iw, ih, 8);
				showPix(graph, pixels, "����ͼ");		
		    }				    	
	    }
	    //DCTƵ�����
	    else if(evt.getSource() == idctItem)
	    {	    	
	    	if(loadflag)
	        {
		 		//DCT�����
				pixels = cipher.dctDecipher(pixels, iw, ih, 8);								
				showPix(graph, pixels, "����ͼ");  
			}			
	    }
	    //Walsh-Hadamard�任�����
	    else if(evt.getSource() == walshItem)
	    {	    		    	
	    	//�����ȼ���ͼ��,Ȼ��ſ��Խ���W-H�任
			if(loadflag)			
			{	
			    setTitle("��14�� ͼ����� Walsh-Hadamard����� ���� ���ƻ�");
			    pixels = common.grabber(iImage, iw, ih);
			    //W-H�����
			    pixels = cipher.whCipher(pixels, iw, ih);
			    showPix(graph, pixels, "����ͼ");			    					            					
			}				    	
	    }	    
	    //Walsh-Hadamard����
	    else if(evt.getSource() == iwalshItem)
	    {	    		    	
	    	//�����ȼ���ͼ��, Ȼ��ſ��Խ���FWT�任
			if(loadflag)			
			{
				//W-H�����
				pixels = cipher.whDecipher(pixels, iw, ih);							
				showPix(graph, pixels, "����ͼ");
			}				    	
	    }
	    //С��Ƶ��������
	    else if(evt.getSource() == wavItem)
	    {	    	
	    	//�����ȼ���ͼ��,Ȼ��ſ��Խ���DWT�任
			if(loadflag)			
			{
				setTitle("��14�� ͼ����� DWT��������Ҽ��� ���� ���ƻ�");		    
			    pixels = common.grabber(iImage, iw, ih);
			    //����
			    pixels = cipher.dwtCipher(pixels, iw, ih);				
				showPix(graph, pixels, "����ͼ");
			}				    	
	    }	    
	    //С��Ƶ�����
	    else if(evt.getSource() == iwavItem)
	    {	    	
	    	if(loadflag)
	        {
	        	pixels = cipher.dwtDecipher(pixels, iw, ih);				
				showPix(graph, pixels, "����ͼ");
			}		
	    }
	    else if(evt.getSource() == logItem)
	    {
	    	setTitle("��14�� ͼ����� Logistic���� ���� ���ƻ�");
	    	byte[][] bpix = cipher.chaos(520, 300);
	    	pixels = common.byte2ARGB(bpix, 520, 300);
	        ImageProducer ip = new MemoryImageSource(520, 300, pixels, 0, 520);
		    oImage  = createImage(ip);
		    runflag = true;
		    repaint();		      
	    }
	    else if(evt.getSource() == atrItem)
	    {
	    	setTitle("��14�� ͼ����� ���������� ���� ���ƻ�");
	    	Parameters pp = new Parameters("����", "mu: ", "x: ","3.8", "0.235");
	    	setPanel(pp, "");
	    	float mu = pp.getPadx();
	    	float t  = pp.getPady();
	    	
	    	common.draw(graph, mu, t);	    	
	    }
	    else if (evt.getSource() == okButton)
           	dialog.dispose(); 	    	
	    else if (evt.getSource() == exitItem) 
            System.exit(0);       
    }   
	
    public void paint(Graphics g) 
    {    	  
        if (iImage != null && (!runflag))
        {
        	g.clearRect(0, 0, 530, 350);        	
            g.drawImage(iImage, 5, 50, null);
            g.drawString("ԭͼ", 120, 320);
        }
        else if(runflag)
        {
            g.clearRect(0, 0, 530, 330);
		    g.drawImage(oImage, 0, 30, null);	        
		    g.drawLine(5, 310, 525, 310);
	        for(int i = 1; i < 7; i++)
	            g.drawLine((int)(i*86.5), 310, (int)(i*86.5), 305);
	        g.drawString("3.0",80, 322);
	        g.drawString("3.2",165,322);
	        g.drawString("3.4",250,322);
	        g.drawString("3.6",338,322);
	        g.drawString("3.8",423,322);  	
        }        
    }
    
    public Image showPix(Graphics graph, int[] pixels, String str)
    {    
		//�������е����ز���һ��ͼ��
		ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
		Image oImage = createImage(ip);
		common.draw(graph, iImage, "ԭͼ", oImage, str);
		runflag = true;
		return oImage;
	}
	
	public void setPanel(Parameters pp, String s)
    {
    	JPanel buttonsPanel = new JPanel();  
	    okButton = new JButton("ȷ��");				
        okButton.addActionListener(this);
        
    	dialog = new JDialog(this, s+ " ����ѡ��", true);     
        
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
        new Ch14ImageCipher();        
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
        
        Menu spaceCiphMenu = new Menu("�������");
        arnoldItem = new MenuItem("Arnold���Ҽ���");
        arnoldItem.addActionListener(this);
        spaceCiphMenu.add(arnoldItem);
        
        iarnoldItem = new MenuItem("Arnold���ҽ���");
        iarnoldItem.addActionListener(this);
        spaceCiphMenu.add(iarnoldItem);
        spaceCiphMenu.addSeparator();
        
        logisticItem = new MenuItem("Logistic���Ҽ���");
        logisticItem.addActionListener(this);
        spaceCiphMenu.add(logisticItem);
        
        ilogisticItem = new MenuItem("Logistic���ҽ���");
        ilogisticItem.addActionListener(this);
        spaceCiphMenu.add(ilogisticItem);
        spaceCiphMenu.addSeparator();
         
        logisXorItem = new MenuItem("Logistic���м���");
        logisXorItem.addActionListener(this);
        spaceCiphMenu.add(logisXorItem);
        
        ilogisXorItem = new MenuItem("Logistic���н���");
        ilogisXorItem.addActionListener(this);
        spaceCiphMenu.add(ilogisXorItem);
        spaceCiphMenu.addSeparator();
        
        fibonXorItem = new MenuItem("Fibonacci���м���");
        fibonXorItem.addActionListener(this);
        spaceCiphMenu.add(fibonXorItem);
        
        ifibonXorItem = new MenuItem("Fibonacci���н���");
        ifibonXorItem.addActionListener(this);
        spaceCiphMenu.add(ifibonXorItem);
        
        Menu freqCiphMenu = new Menu("Ƶ�����");
        dctItem = new MenuItem("DCT����");
        dctItem.addActionListener(this);
        freqCiphMenu.add(dctItem);
        
        idctItem = new MenuItem("DCT����");
        idctItem.addActionListener(this);
        freqCiphMenu.add(idctItem);
        freqCiphMenu.addSeparator();
        
        walshItem = new MenuItem("Walsh����");
        walshItem.addActionListener(this);
        freqCiphMenu.add(walshItem);
        
        iwalshItem = new MenuItem("Walsh����");
        iwalshItem.addActionListener(this);
        freqCiphMenu.add(iwalshItem);
        freqCiphMenu.addSeparator();
        
        wavItem = new MenuItem("DWT����");
        wavItem.addActionListener(this);
        freqCiphMenu.add(wavItem);        
        
        iwavItem = new MenuItem("DWT����");
        iwavItem.addActionListener(this);
        freqCiphMenu.add(iwavItem);
        
        Menu chaosMenu = new Menu("������ʾ");
        logItem = new MenuItem("Logistic");
        logItem.addActionListener(this);
        chaosMenu.add(logItem);
        
        atrItem = new MenuItem("��������ʾ");
        atrItem.addActionListener(this);
        chaosMenu.add(atrItem);
               
        MenuBar menuBar = new MenuBar();
        menuBar.add(fileMenu);
        menuBar.add(spaceCiphMenu);
        menuBar.add(freqCiphMenu);
        menuBar.add(chaosMenu);       
        setMenuBar(menuBar);
    }
    
    MenuItem openItem;
    MenuItem exitItem;
    
    MenuItem arnoldItem;   //Arnold�任����
    MenuItem iarnoldItem;  //��Arnold�任����
    MenuItem logisticItem; //Logistic�任����
    MenuItem ilogisticItem;//��Logistic�任����
    MenuItem logisXorItem; //Logistic���м���
    MenuItem ilogisXorItem;//Logistic���н���
    MenuItem fibonXorItem; //Fibonacci���м���
    MenuItem ifibonXorItem;//Fibonacci���н���
    
    MenuItem dctItem;      //DCT����
    MenuItem idctItem;     //DCT����
    MenuItem walshItem;    //Walsh����
    MenuItem iwalshItem;   //Walsh����
    MenuItem wavItem;      //Wavelet����
    MenuItem iwavItem;     //Wavelet����
    MenuItem logItem;      //Logistic��ʾ
    MenuItem atrItem;      //��������ʾ
}
