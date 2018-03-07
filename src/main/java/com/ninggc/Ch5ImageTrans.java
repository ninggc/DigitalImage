package com.ninggc; /**
 * @Ch5ImageTrans.java
 * @Version 1.0 2010.02.15
 * @Author Xie-Hua Sun 
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import process.common.Common;
import process.algorithms.imageTrans.*;

public class Ch5ImageTrans extends JFrame implements ActionListener
{
    Image iImage, oImage;
    
    int iw, ih;
    int bsize = 128;              //����128X128��DCT�任
    int[] pixels;          
    Complex[] fftData;
    double[][] dctData;
    double[] dwtData;
    double[] W;
       
    boolean loadflag  = false,
            runflag   = false;    //ͼ����ִ�б�־ 
            
    Common common;
    
    public Ch5ImageTrans()
    {    
        setTitle("����ͼ����-Java�����ʵ�� ��5�� ͼ��ʱƵ�任");
        this.setBackground(Color.lightGray);        
              
        //�˵�����
        setMenu();
        
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
            common.chooseFile(chooser, "./images", 0);//����Ĭ��Ŀ¼,�����ļ�
            int r = chooser.showOpenDialog(null);
                        
            MediaTracker tracker = new MediaTracker(this);
            if(runflag)
            { 
                loadflag = false;
                runflag  = false;
            }
            
            if(r == JFileChooser.APPROVE_OPTION) 
            {  
                String name = chooser.getSelectedFile().getAbsolutePath();
                
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
        else if (evt.getSource() == fftItem)
        {
        	setTitle("��5�� ͼ��ʱƵ�任 FFT�任 ���� ���ƻ�");
        	if(loadflag)        	
        	{
        		if(iw == 256&&ih==256)
        		{
        			double[] iPix = new double[iw * ih];//����Ҷ�
	                pixels = common.grabber(iImage, iw, ih);
	        	    
	        	    for (int i = 0; i < iw*ih; i++)
	                    iPix[i] = pixels[i]&0xff;                  
	                    
	                //FFT�任
	                FFT2 fft2 = new FFT2();
	                fft2.setData2(iw, ih, iPix);
	                fftData = fft2.getFFT2();
	                
	                //FFT���ݿ��ӻ�
	                pixels = fft2.toPix(fftData, iw, ih);   
	        	    showPix(graph, pixels, "FFT�任");	        	    										
				}
				else
				{				
				    JOptionPane.showMessageDialog(null, "�������������256X256ͼ��!");
				    loadflag = false;
				}
			}
			else
			 	JOptionPane.showMessageDialog(null, "���ȴ�ͼ��!");
        }
        else if (evt.getSource() == ifftItem)
        { 
            if(loadflag)        	
        	{
        	    double[] oPix;

                //FFT���任
                FFT2 fft2 = new FFT2();
                fft2.setData2i(iw, ih, fftData);
                pixels = fft2.getPixels2i();
                showPix(graph, pixels, "��FFT�任");                          					
				runflag = true;				
			}
			else
			 	JOptionPane.showMessageDialog(null, "���ȴ�ͼ��!");             	                     
	    }
	    else if (evt.getSource() == dctItem)
        { 
            setTitle("��5�� ͼ��ʱƵ�任 DCT�任 ���� ���ƻ�");       	
        	if(loadflag)        	
        	{
        	    if(iw == 256&&ih==256)
        		{        			
        			double[][] iPix = new double[ih][iw];//����Ҷ�
	                pixels = common.grabber(iImage, iw, ih);
	        	    
	        	    for (int j = 0; j < ih; j++)
	                    for (int i = 0; i < iw; i++)
	                        iPix[i][j] = pixels[i+j*iw]&0xff;                  
	                    
	                //DCT�任
	                DCT2 dct2 = new DCT2();
	                dctData = dct2.dctTrans(iPix, iw, ih, bsize, 1);
	                	                
	                //DCT���ݿ��ӻ�
	                pixels = common.toPixels(dctData, iw, ih);   
	        	    showPix(graph, pixels, "DCT�任");	        	    				
				}
				else
				{				
				    JOptionPane.showMessageDialog(null, "�������������256X256ͼ��!");
				    loadflag = false;
				}	
        	}
			else
			 	JOptionPane.showMessageDialog(null, "���ȴ�ͼ��!");             	                     
	    }
	    else if (evt.getSource() == idctItem)
        { 
            if(loadflag)        	
        	{
        	    DCT2 dct2 = new DCT2();
        	    
        	    //DCT��任
                double[][] oim = dct2.dctTrans(dctData, iw, ih, bsize, -1);
                pixels = common.toPixels(oim, iw, ih);
                showPix(graph, pixels, "DCT��任");
                runflag = true;		
        	}
			else
			 	JOptionPane.showMessageDialog(null, "���ȴ�ͼ��!");             	                     
	    }
	    else if (evt.getSource() == dwtItem)
        { 
            setTitle("��5�� ͼ��ʱƵ�任 DWT�任 ���� ���ƻ�");       	
        	if(loadflag)        	
        	{
        	    if(iw == 256&&ih==256)
        		{        			
        			double[] iPix = new double[iw*ih];//����Ҷ�
	                pixels = common.grabber(iImage, iw, ih);	        	    
	        	    
	                for (int i = 0; i < iw*ih; i++)
	                    iPix[i] = pixels[i]&0xff;                  
	                
	                double[] h = { 0.23037781330889,  0.71484657055291,
                                   0.63088076792986, -0.02798376941686,
                                  -0.18703481171909,  0.03084138183556,
                                   0.03288301166689, -0.01059740178507 };

	                double[] g = { 0.23037781330889, -0.71484657055291,		
	                               0.63088076792986,  0.02798376941686,		
	                              -0.18703481171909, -0.03084138183556,		
	                               0.03288301166689,  0.01059740178507 }; 
	                                 
	                //DWT�任
	                DWT2 dwt2 = new DWT2(iw,ih);
	                dwtData = dwt2.wavelet2D(iPix, h, g, 1);
	                	                	                
	                //FFT���ݿ��ӻ�
	                pixels = common.toPixels(dwtData, iw, ih);   
	        	    showPix(graph, pixels, "DWT�任");	        	    				
				}
				else
				{				
				    JOptionPane.showMessageDialog(null, "�������������256X256ͼ��!");
				    loadflag = false;
				}		
        	}
			else
			 	JOptionPane.showMessageDialog(null, "���ȴ�ͼ��!");             	                     
	    }
	    else if (evt.getSource() == idwtItem)
        { 
            if(loadflag)        	
        	{       		              
	            double[] h = { 0.23037781330889,  0.71484657055291,
                               0.63088076792986, -0.02798376941686,
                              -0.18703481171909,  0.03084138183556,
                               0.03288301166689, -0.01059740178507 };

                double[] g = { 0.23037781330889, -0.71484657055291,		
                               0.63088076792986,  0.02798376941686,		
                              -0.18703481171909, -0.03084138183556,		
                               0.03288301166689,  0.01059740178507 }; 
                                 
                //DWT��任
                DWT2 dwt2 = new DWT2(iw,ih);
                double[] iPix = dwt2.iwavelet2D(dwtData, h, g, 1);
                	                	                
                //IDWT���ݿ��ӻ�
                pixels = common.toPixels(iPix, iw, ih);   
        	    showPix(graph, pixels, "DWT��任");
        	    runflag = true;			
        	}
			else
			 	JOptionPane.showMessageDialog(null, "���ȴ�ͼ��!");             	                     
	    }	    
	    else if (evt.getSource() == whtItem)
        { 
            setTitle("��5�� ͼ��ʱƵ�任 WHT�任 ���� ���ƻ�");       	
        	if(loadflag)        	
        	{
        	    double[] iPix = new double[iw*ih];
                W = new double[iw * ih];
                
                pixels = common.grabber(iImage, iw, ih);
        	    
        	    for (int i = 0; i < iw*ih; i++)
                    iPix[i] = pixels[i]&0xff;
                        
                WHT2 wht2 = new WHT2(iw, ih);
                W = wht2.WALSH(iPix, 16);
                
                double[] nW = new double[iw * ih];
                
                //WHT���ݿ��ӻ�                
                for (int i = 0; i < iw*ih; i++)
                    nW[i] = (int)Math.abs(W[i] * 1000);                        
                
                pixels = common.toPixels(nW, iw, ih);   
        	    showPix(graph, pixels, "WHT�任");    
			}
			else
			 	JOptionPane.showMessageDialog(null, "���ȴ�ͼ��!");             	                     
	    }
	    else if (evt.getSource() == iwhtItem)
        {                  	
        	if(loadflag)        	
        	{
        	    WHT2 wht2 = new WHT2(iw, ih);
                double[] oW = wht2.IWALSH(W, 16);
                pixels = common.toPixels(oW, iw, ih); 
                showPix(graph, pixels, "WHT��任");        	    
				runflag = true;   	
        	}
			else
			 	JOptionPane.showMessageDialog(null, "���ȴ�ͼ��!");             	                     
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
    
    public void showPix(Graphics graph, int[] pixels, String str)
    {    
		//�������е����ز���һ��ͼ��
		ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
		Image oImage = createImage(ip);
		common.draw(graph, iImage, "ԭͼ", oImage, str);
	}

    public static void main(String[] args) 
    {  
        new Ch5ImageTrans();        
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

        saveItem = new MenuItem("����");
        saveItem.addActionListener(this);
        fileMenu.add(saveItem);
        
        exitItem = new MenuItem("�˳�");
        exitItem.addActionListener(this);
        fileMenu.add(exitItem);
    
        Menu transMenu = new Menu("ʱƵ�任");
        fftItem = new MenuItem("FFT");
        fftItem.addActionListener(this);
        transMenu.add(fftItem);
        
        ifftItem = new MenuItem("IFFT");
        ifftItem.addActionListener(this);
        transMenu.add(ifftItem);
         
        transMenu.addSeparator();
        dctItem = new MenuItem("DCT");
        dctItem.addActionListener(this);
        transMenu.add(dctItem);
        
        idctItem = new MenuItem("IDCT");
        idctItem.addActionListener(this);
        transMenu.add(idctItem);
        
        transMenu.addSeparator();
        dwtItem = new MenuItem("DWT");
        dwtItem.addActionListener(this);
        transMenu.add(dwtItem);
        
        idwtItem = new MenuItem("IDWT");
        idwtItem.addActionListener(this);
        transMenu.add(idwtItem);
        
        transMenu.addSeparator();
        whtItem = new MenuItem("WHT");
        whtItem.addActionListener(this);
        transMenu.add(whtItem);
        
        iwhtItem = new MenuItem("IWHT");
        iwhtItem.addActionListener(this);
        transMenu.add(iwhtItem);
               
        MenuBar menuBar = new MenuBar(); 
        menuBar.add(fileMenu);
        menuBar.add(transMenu);      
        setMenuBar(menuBar);
    }
    
    MenuItem openItem;
    MenuItem saveItem;
    MenuItem exitItem;	
    MenuItem fftItem;
    MenuItem ifftItem;
    MenuItem dctItem;
    MenuItem idctItem;
    MenuItem dwtItem;
    MenuItem idwtItem;
    MenuItem whtItem;
    MenuItem iwhtItem;
}
