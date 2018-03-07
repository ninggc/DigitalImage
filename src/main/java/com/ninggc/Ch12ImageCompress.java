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
    int algnum;                      //�㷨���� 
 		      
    boolean loadflag = false,
            runflag  = false,        //ͼ����ִ�б�־
            algflag  = false;        //�㷨ѡ���־     
                              
    ImageCompress compress;
    Common common;
    
    public Ch12ImageCompress()
    {    
        setTitle("����ͼ����-Java�����ʵ�� ��12�� ͼ��ѹ��");
        this.setBackground(Color.lightGray);        
              
        //�˵�����
        setMenu();
        
        compress = new ImageCompress();
        common   = new Common();
        
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
	    	if(!algflag) 
    	        JOptionPane.showMessageDialog(null, "����ѡ��ѹ���㷨!"); 
    	    else
    	    {  
	            if(algnum == 0||algnum == 2||algnum ==3)                	
	            {
	            	RAW raw = new RAW();
			        				        	
		        	if(algnum == 0)
		        	    //��ȡRAWͼ��
		                bpix = raw.readRAW_DAT("./images/ch12/clock.raw", iw, ih);
		            else if(algnum == 2)
		        	    //��ȡRAWͼ��
		                bpix = raw.readRAW_DAT("./images/ch12/rleTest.raw", iw, ih);
		            else if(algnum == 3)
		        	    //��ȡRAWͼ��
		                bpix = raw.readRAW_DAT("./images/ch12/lzwTest.raw", iw, ih); 
		                   
		        	//���ֽ�����pixת��Ϊͼ������pixels
		        	pixels = common.byte2int(bpix, iw, ih);
		        	
		        	//�������е����ز���һ��ͼ��
				    ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
				    iImage = createImage(ip);  
				
		    	    loadflag = true;  
		            repaint();	
			    }
			    else
			    {
			    	//�ļ�ѡ��Ի���
		            JFileChooser chooser = new JFileChooser();            
		            common.chooseFile(chooser, "./images/ch12", 3);//����Ĭ��Ŀ¼,�����ļ�
		            int r = chooser.showOpenDialog(null);                                 
		            
		            MediaTracker tracker = new MediaTracker(this);
		                                      
		            if(r == JFileChooser.APPROVE_OPTION) 
		            {
		            	String name = chooser.getSelectedFile().getAbsolutePath();
		                
		                //ȡ�ļ������� 
		                String fname = chooser.getSelectedFile().getName(); 
		                
		                int len = fname.length();
		                
		                //ȡ�ļ�������չ��
		                String exn = fname.substring(len-3, len);
		                String imn = fname.substring(0,len-4);
		                
		                if(runflag)//��ʼ�� 
			            {            	
			            	loadflag  = false;
			            	runflag   = false;	            	
			            }
			            
			            if(exn.equalsIgnoreCase("raw"))//RAWͼ��
		                {
		                    RAW raw = new RAW();
				        			        	
				        	iw = 256;
				        	ih = 256;
				        	
				        	//��ȡRAWͼ��
				            bpix = raw.readRAW_DAT(name, iw, ih);
				            
				        	//���ֽ�����pixת��Ϊͼ������pixels
				        	pixels = common.byte2int(bpix, iw, ih);
				        	
				        	//�������е����ز���һ��ͼ��
						    ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
						    iImage = createImage(ip); 
						
				    	    loadflag = true;  
				            repaint();	
						}                
		                else                          //GIF,JPG,PNG
		                {                       
			                if(!loadflag)
						    {
				                //װ��ͼ��
							    iImage = common.openImage(name, tracker);    
							    //ȡ����ͼ��Ŀ�͸�
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
        //ѹ��ͼ��, ����Ϊclock_compressed.raw
        else if (evt.getSource() == compItem)
        {        	
        	if(loadflag)
	    	{
	    		setTitle("��12�� ͼ��ѹ�� ������ɫ�� ���� ���ƻ�");
	        	try
	        	{
	        		//�õ���ɫ��colors_��������search_
	        	   	compress.ToIndexedColor(bpix, iw, ih);
	        	}
	        	catch(AWTException ae){}
	        	compress.compress(iw, ih);
	            JOptionPane.showMessageDialog(null,"��Ŀ¼images/compressed,\n"+
	                                          "����ͼ��clock.cmp�ɹ�!");
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
		    showPix(graph, pixels, "��ѹ��");
		    algflag = false;
		    loadflag = false;		                          	                     
	    }
	    else if (evt.getSource() == huffItem)
	    {
	    	setTitle("��12�� ͼ��ѹ�� Huffman���� ���� ���ƻ�");
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
	    		setTitle("��12�� ͼ��ѹ�� RLE�㷨 ���� ���ƻ�");
	    		try
	        	{
	        		//�õ���ɫ��colors_��������search_
	        	   	compress.ToIndexedColor(bpix, iw, ih);
	        	}
	        	catch(AWTException ae){}
	    		compress.rleCompress(iw, ih);
            
                JOptionPane.showMessageDialog(null,"��Ŀ¼images/compressed,\n"+
                                              "����ͼ��rleTest.rle�ɹ�!");                                        	
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
		    
		    //�������е����ز���һ��ͼ��
		    ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
		    oImage = createImage(ip);  
		    showPix(graph, pixels, "RLE��ѹ��"); 
		    algflag  = false;
		    loadflag = false;   	    
	    }
	    else if (evt.getSource() == lzwItem)
	    {
	        if(loadflag)
	    	{
	    		setTitle("��12�� ͼ��ѹ�� LZW�㷨 ���� ���ƻ�");
	    		try
	        	{
	        		//�õ���ɫ��colors_��������search_
	        	   	compress.ToIndexedColor(bpix, iw, ih);
	        	}
	        	catch(AWTException ae){}
	    		compress.lzwCompress(iw, ih);
            
                JOptionPane.showMessageDialog(null,"��Ŀ¼images/compressed,\n"+
                                              "����ͼ��lzwTest.lzw�ɹ�!");                                        	
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
	    	showPix(graph, pixels,"LZW��ѹ��");
	    	algflag  = false;
		    loadflag = false;
	    }
	    else if (evt.getSource() == dctItem)
	    {
	        if(loadflag)
	    	{
	    		if(iw != 256 || ih != 256)
				{
					JOptionPane.showMessageDialog(null,"Ŀǰֻ����256X256ͼ��ѹ��!");
					return;
				}
				setTitle("��12�� ͼ��ѹ�� DCTѹ�� ���� ���ƻ�"); 				
				pixels = common.grabber(iImage, iw, ih);
				
				compress.dctCompress(pixels, iw, ih);
				
                JOptionPane.showMessageDialog(null,"��Ŀ¼images/compressed,\n"+
                                                   "����ͼ��test.dct�ɹ�!");                    
            }
	    	else			
			{
				algflag = true;
				algnum  = 4;
			}
	    }
	    //��ѹ��
	    else if(evt.getSource() == undctItem)
	    {
	    	opix = compress.dctUncomp(iw, ih, 8);
	    	showPix(graph, opix,"DCT��ѹ��");	
	    	algflag  = false;
		    loadflag = false;		
			JOptionPane.showMessageDialog(null,"��ֵ�����PSNR = "+ 
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
        
        Menu compressMenu = new Menu("ͼ��ѹ��");
        compItem = new MenuItem("����ɫ��ѹ��");
        compItem.addActionListener(this);
        compressMenu.add(compItem);
        
        uncmItem = new MenuItem("����ɫ���ѹ");
        uncmItem.addActionListener(this);
        compressMenu.add(uncmItem);
        compressMenu.addSeparator();
        
        huffItem = new MenuItem("Huffman����");
        huffItem.addActionListener(this);
        compressMenu.add(huffItem);
        compressMenu.addSeparator();
         
        rleItem = new MenuItem("RLEѹ��");
        rleItem.addActionListener(this);
        compressMenu.add(rleItem);
        
        unrleItem = new MenuItem("RLE��ѹ");
        unrleItem.addActionListener(this);
        compressMenu.add(unrleItem); 
        compressMenu.addSeparator();
         
        lzwItem = new MenuItem("LZWѹ��");
        lzwItem.addActionListener(this);
        compressMenu.add(lzwItem);
        
        unlzwItem = new MenuItem("LZW��ѹ");
        unlzwItem.addActionListener(this);
        compressMenu.add(unlzwItem);
        compressMenu.addSeparator();
         
        dctItem = new MenuItem("DCTѹ��");
        dctItem.addActionListener(this);
        compressMenu.add(dctItem);
        
        undctItem = new MenuItem("DCT��ѹ");
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
