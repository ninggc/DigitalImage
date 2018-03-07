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
     
    boolean loadflag = false,       //����ͼ���־
            runflag  = false,       //ִ�д����־   
            fillflag = false;
    int   iw, ih;
    int[] pixels;          
             
    ImageMorph morph;
    Common common;
    
    public Ch10ImageMorph()
    {    
        setTitle("����ͼ����-Java�����ʵ�� ��10�� ͼ����̬ѧ");
        this.setBackground(Color.lightGray);        
              
        //�˵�����
        setMenu();
        
        morph = new ImageMorph();
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
            common.chooseFile(chooser, "./images/ch10", 0);//����Ĭ��Ŀ¼,�����ļ�
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
				    //ȡ����ͼ��Ŀ�͸�
				    iw = iImage.getWidth(null);
				    ih = iImage.getHeight(null);				    
				    repaint();
				    loadflag = true;
			    }			    		    			               
            }                        
        }        
        else if (evt.getSource() == erode1Item)//��ʴ
        {
           	if(loadflag)        	
        	{        					    
        		setTitle("��10�� ͼ����̬ѧ �����νṹ��ʴ ���� ���ƻ�");
	            removeMouseListener(this);
	            show(graph, 11, "�����θ�ʴ");	             	
        	}
        	else
			 	JOptionPane.showMessageDialog(null, "���ȴ�ͼ��!");				
        } 
        else if (evt.getSource() == erode2Item)
        {
        	if(loadflag)        	
        	{        					    
        		setTitle("��10�� ͼ����̬ѧ ���νṹ��ʴ ���� ���ƻ�");
	            removeMouseListener(this);
	            show(graph, 12, "���θ�ʴ");	             	
        	}
        	else
			 	JOptionPane.showMessageDialog(null, "���ȴ�ͼ��!");				
        }
        else if (evt.getSource() == erode3Item)
        {
        	if(loadflag)        	
        	{        			    
        		setTitle("��10�� ͼ����̬ѧ �˽ǽṹ��ʴ ���� ���ƻ�");
	            removeMouseListener(this);
	            show(graph, 13, "�˽Ǹ�ʴ");	             	
        	}
        	else
			 	JOptionPane.showMessageDialog(null, "���ȴ�ͼ��!");				
        }        
        else if (evt.getSource() == dilate1Item)//����
        {
        	if(loadflag)        	
        	{        					    
        		setTitle("��10�� ͼ����̬ѧ �����νṹ���� ���� ���ƻ�");
	            removeMouseListener(this);
	            show(graph, 21, "��������");	             	
        	}
        	else
			 	JOptionPane.showMessageDialog(null, "���ȴ�ͼ��!");				
        }
        else if (evt.getSource() == dilate2Item)
        {
        	if(loadflag)        	
        	{        					    
        		setTitle("��10�� ͼ����̬ѧ ���νṹ���� ���� ���ƻ�");
	            removeMouseListener(this);
	            show(graph, 22, "��������");	             	
        	}
        	else
			 	JOptionPane.showMessageDialog(null, "���ȴ�ͼ��!");				
        }
        else if (evt.getSource() == dilate3Item)
        {
        	if(loadflag)        	
        	{        					    
        		setTitle("��10�� ͼ����̬ѧ �˽ǽṹ���� ���� ���ƻ�");
	            removeMouseListener(this);
	            show(graph, 23, "�˽�����");	             	
        	}
        	else
			 	JOptionPane.showMessageDialog(null, "���ȴ�ͼ��!");				
        }        
        else if (evt.getSource() == OPEN1Item)//����
        {
        	if(loadflag)        	
        	{        					    
        		setTitle("��10�� ͼ����̬ѧ �����νṹ���� ���� ���ƻ�");
	            removeMouseListener(this);
	            show(graph, 31, "��������");	             	
        	}
        	else
			 	JOptionPane.showMessageDialog(null, "���ȴ�ͼ��!");				
        }
        else if (evt.getSource() == CLOSE1Item)//�պ�
        {
        	if(loadflag)        	
        	{        					    
        		setTitle("��10�� ͼ����̬ѧ �����ṹ�պ� ���� ���ƻ�");
	            removeMouseListener(this);
	            show(graph, 41, "�����ṹ�պ�");	             	
        	}
        	else
			 	JOptionPane.showMessageDialog(null, "���ȴ�ͼ��!");				
        }
        else if (evt.getSource() == filter1Item)//�˲�1,�����ṹ
        {                	        	
        	if(loadflag)
        	{
        		setTitle("��10�� ͼ����̬ѧ �����ṹ�����˲� ���� ���ƻ�");
	            removeMouseListener(this);
	            show(graph, 51, "�����˲�"); 
	        }		
			else
			 	JOptionPane.showMessageDialog(null, "���ȴ�ͼ��!");
									    	
        }
        else if (evt.getSource() == edge1Item)//�����ṹ�߽���ȡ
        {                	        	
        	if(loadflag)
        	{
        		setTitle("��10�� ͼ����̬ѧ �����ṹ�߽���ȡ ���� ���ƻ�");
	            removeMouseListener(this);	                       
	            show(graph, 61, "�߽���ȡ");								
			}		
			else
				JOptionPane.showMessageDialog(null, "���ȴ�ͼ��!");					    	
        }
        else if (evt.getSource() == fillItem)//�������
        {                	        	
        	if(loadflag)
        	{
        		setTitle("��10�� ͼ����̬ѧ ������� ���� ���ƻ�");
        		addMouseListener(this);      		 
        		fillflag = true;
        	}		
			else
				JOptionPane.showMessageDialog(null, "���ȴ�ͼ��!");					    	
        } 
        else if (evt.getSource() == eulerItem)//����Euler��
        {                	        	
        	if(loadflag)
        	{
        		setTitle("��10�� ͼ����̬ѧ ����Euler�� ���� ���ƻ�");
        		removeMouseListener(this);
        		pixels = common.grabber(iImage, iw, ih);						
				
		        //��ARGBͼ������pixels��Ϊ2ֵͼ�����image2
		        byte[] imb = common.toBinary(pixels, iw, ih);
        	    //����Euler��
        		int eulerN = morph.euler(imb, iw, ih);
	            common.draw(graph, iImage, "Euler�� = ", eulerN);
	            runflag = true;	            
        	}		
			else
				JOptionPane.showMessageDialog(null, "���ȴ�ͼ��!");					    	
        }
        else if (evt.getSource() == hmtItem)//����δ���б任
        {                	        	
        	if(loadflag)
        	{
        		setTitle("��10�� ͼ����̬ѧ HMT�任 ���� ���ƻ�");
        		removeMouseListener(this);
        		show(graph, 71, "HMT�任");        	    			
			}		
			else
				JOptionPane.showMessageDialog(null, "���ȴ�ͼ��!");					    	
        }
        else if (evt.getSource() == erodeItem)//�Ҷȸ�ʴ
        {                	        	
        	if(loadflag)
        	{
        		setTitle("��10�� ͼ����̬ѧ �Ҷȸ�ʴ ���� ���ƻ�");
        		SHOW(graph, 81, "�Ҷȸ�ʴ");        			
			}								    	
        }
        else if (evt.getSource() == dilateItem)//�Ҷ�����
        {                	        	
        	if(loadflag)
        	{
        		setTitle("��10�� ͼ����̬ѧ �Ҷ����� ���� ���ƻ�");
        		SHOW(graph, 91, "�Ҷ�����");        			
			}								    	
        }
        else if (evt.getSource() == OPENItem)//�Ҷȿ���
        {                	        	
        	if(loadflag)
        	{
        		setTitle("��10�� ͼ����̬ѧ �Ҷȿ��� ���� ���ƻ�");
	           	SHOW(graph, 10, "�Ҷȿ���");	           					
			}			
        }
        else if (evt.getSource() == CLOSEItem)//�Ҷȱպ�
        {                	        	
        	if(loadflag)
        	{
        		setTitle("��10�� ͼ����̬ѧ �Ҷȱպ� ���� ���ƻ�");
	           	SHOW(graph, 11, "�Ҷȱպ�");	           						
			}			
        }
        else if (evt.getSource() == gradItem)
        {                	        	
        	if(loadflag)
        	{
        		setTitle("��10�� ͼ����̬ѧ Sobel���� ���� ���ƻ�");
	            SHOW(graph, 12, "Sobel����");	            											
			}									    	
        }
        else if (evt.getSource() == filt1Item)//�˲�1
        {                	        	
        	if(loadflag)
        	{
        		setTitle("��10�� ͼ����̬ѧ ��̬ѧ�˲�1 ���� ���ƻ�");
	           	SHOW(graph, 13, "�˲�1");	           							
			}									    	
        }
        else if (evt.getSource() == filt2Item)//�˲�2
        {                	        	
        	if(loadflag)
        	{
        		setTitle("��10�� ͼ����̬ѧ ��̬ѧ�˲�2 ���� ���ƻ�");
	           	SHOW(graph, 14, "�˲�2");	           					
			}									    	
        }
        else if (evt.getSource() == filt3Item)//�˲�3
        {                	        	
        	if(loadflag)
        	{
        		setTitle("��10�� ͼ����̬ѧ ��̬ѧ�˲�3 ���� ���ƻ�");
	           	SHOW(graph, 15, "�˲�3");	           					
			}									    	
        }
        else if (evt.getSource() == topHatItem)//��ñ�任
        {                	        	
        	if(loadflag)
        	{
        		setTitle("��10�� ͼ����̬ѧ ��ñ�任 ���� ���ƻ�");
        		SHOW(graph, 16, "��ñ�任");       	     				
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
            g.drawString("ԭͼ", 120, 320);            
        }
        if(fillflag)
        {
            g.setColor(Color.red);
        	g.drawString("�����ѡ��������ӵ�" , 40, 290);
        	fillflag = false;
        }                
    }
   	
   	public void mouseClicked(MouseEvent e)
   	{
   		Graphics g = getGraphics();
   		pixels = common.grabber(iImage, iw, ih);						
				
		//��ARGBͼ������pixels��Ϊ2ֵͼ�����image2
		byte[] imb = common.toBinary(pixels, iw, ih);
		
    	int ix = e.getX() - 5; 
    	int iy = e.getY() - 50;
    	imb = morph.fill(imb, iw, ih, ix, iy);
				
		pixels = common.bin2Rgb(imb, iw, ih);
				
		//�������е����ز���һ��ͼ��
        ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
        oImage = createImage(ip);
		common.draw(g, iImage, "ԭͼ", oImage, "�������");				
        runflag = true;
    } 
    
    public void mousePressed(MouseEvent e){ }
    public void mouseReleased(MouseEvent e){ }
    public void mouseEntered(MouseEvent e){ }
    public void mouseExited(MouseEvent e){ }
    
   	/*************************************************
     * type - �ͺ�. 0:BLPF 1:BHPF 2:ELPF 3:EHPF
     * name - ���ͼ������ַ���
     *************************************************/    
    public void show(Graphics graph, int type, String name)
    {  
    	pixels = common.grabber(iImage, iw, ih);						
				
		//��ARGBͼ������pixels��Ϊ2ֵͼ�����image2
		byte[] imb = common.toBinary(pixels, iw, ih);
		
		switch(type)
		{
			case 11: //�����νṹ��ʴ
				    imb = morph.erode(imb, iw, ih, 11);
			        break;
			case 12: //���νṹ��ʴ
				    imb = morph.erode(imb, iw, ih, 12);
			        break;
			case 13: //�˽ǽṹ��ʴ
				    imb = morph.erode(imb, iw, ih, 13);
			        break;
			case 21: //�����νṹ����
				    imb = morph.dilate(imb, iw, ih, 21);
			        break;
			case 22: //���νṹ����
				    imb = morph.dilate(imb, iw, ih, 22);
			        break;
			case 23: //�˽ǽṹ����
				    imb = morph.dilate(imb, iw, ih, 23);
			        break;
			case 31: //�����νṹ����
			        imb = morph.erode (imb, iw, ih, 11);
			        imb = morph.dilate(imb, iw, ih, 21);
			        break;
			case 41: //�����νṹ�պ�			        
			        imb = morph.dilate(imb, iw, ih, 21);
			        imb = morph.erode (imb, iw, ih, 11);
			        break;
			case 51: //�����νṹ�˲�
					imb = morph.erode (imb, iw, ih, 11);
			        imb = morph.dilate(imb, iw, ih, 21);	        
			        imb = morph.dilate(imb, iw, ih, 21);
			        imb = morph.erode (imb, iw, ih, 11);
			        break;
			case 61: //�����νṹ�߽���ȡ
				    imb = morph.edge(imb, iw, ih, 11);;
			        break;
			case 71: //HMT�任
    	            imb = morph.hmt(imb, iw, ih); 
    	            break;    	          
	    }    
	    pixels = common.bin2Rgb(imb, iw, ih);
				
		//�������е����ز���һ��ͼ��
        ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
        oImage = createImage(ip);
		common.draw(graph, iImage, "ԭͼ", oImage, name);				
        runflag = true; 	
    }
    
    public void SHOW(Graphics graph, int type, String name)
    {  
    	pixels = common.grabber(iImage, iw, ih);						
		
		switch(type)
		{
			case 81://�Ҷȸ�ʴ
				    pixels = morph.grayErode(pixels, iw, ih, 1);
			        break;
			case 91://�Ҷ�����       
				    pixels = morph.grayDilate(pixels, iw, ih, 1); 
				    break;
		    case 10://�Ҷȿ���
		            pixels = morph.grayErode(pixels, iw, ih, 1);
				    pixels = morph.grayDilate(pixels, iw, ih, 1);
				    break;
		    case 11://�Ҷȱպ�
		            pixels = morph.grayDilate(pixels, iw, ih, 1);
		            pixels = morph.grayErode(pixels, iw, ih, 1);				    
				    break;
		    case 12://Sobel����
		            pixels = morph.Sobel(pixels, iw, ih);			    
				    break;
		    case 13://�˲�1
		            pixels = morph.grayErode(pixels, iw, ih, 2); //����
				    pixels = morph.grayDilate(pixels, iw, ih, 2);				
				    pixels = morph.grayDilate(pixels, iw, ih, 2);//�պ�
				    pixels = morph.grayErode(pixels, iw, ih, 2);			    
				    break;
		    case 14://�˲�2
		            pixels = morph.grayDilate(pixels, iw, ih, 2);//�պ�
				    pixels = morph.grayErode(pixels, iw, ih, 2);
				    pixels = morph.grayErode(pixels, iw, ih, 2); //����
				    pixels = morph.grayDilate(pixels, iw, ih, 2);				    
				    break;  
		    case 15://�˲�3
		            pixels = morph.grayFilter(pixels, iw, ih);			    
				    break;
		    case 16://��ñ�任
		            pixels = morph.grayTopHat(pixels, iw, ih);   
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
        
        Menu morphMenu = new Menu("��ֵ��̬ѧ");
        erode1Item = new MenuItem("������ʴ");
        erode1Item.addActionListener(this);
        morphMenu.add(erode1Item);
        
        erode2Item = new MenuItem("���θ�ʴ");
        erode2Item.addActionListener(this);
        morphMenu.add(erode2Item);
        
        erode3Item = new MenuItem("�˽Ǹ�ʴ");
        erode3Item.addActionListener(this);
        morphMenu.add(erode3Item);
        
        morphMenu.addSeparator();
        dilate1Item = new MenuItem("��������");
        dilate1Item.addActionListener(this);
        morphMenu.add(dilate1Item);
        
        dilate2Item = new MenuItem("��������");
        dilate2Item.addActionListener(this);
        morphMenu.add(dilate2Item);
        
        dilate3Item = new MenuItem("�˽�����");
        dilate3Item.addActionListener(this);
        morphMenu.add(dilate3Item);
        
        morphMenu.addSeparator();
        OPEN1Item = new MenuItem("�����ṹ����");
        OPEN1Item.addActionListener(this);
        morphMenu.add(OPEN1Item);
        
        CLOSE1Item = new MenuItem("�����ṹ�պ�");
        CLOSE1Item.addActionListener(this);
        morphMenu.add(CLOSE1Item);
        
        morphMenu.addSeparator();
        filter1Item = new MenuItem("�����ṹ�˲�");
        filter1Item.addActionListener(this);
        morphMenu.add(filter1Item);
        
        edge1Item = new MenuItem("�����ṹ�߽���ȡ");
        edge1Item.addActionListener(this);
        morphMenu.add(edge1Item);
        
        fillItem = new MenuItem("�������");
        fillItem.addActionListener(this);
        morphMenu.add(fillItem);
        
        eulerItem = new MenuItem("����Euler��");
        eulerItem.addActionListener(this);
        morphMenu.add(eulerItem);
        
        hmtItem = new MenuItem("HMT�任");
        hmtItem.addActionListener(this);
        morphMenu.add(hmtItem);
        
        Menu grayMenu = new Menu("�Ҷ���̬ѧ");
        
        erodeItem = new MenuItem("�Ҷȸ�ʴ");
        erodeItem.addActionListener(this);
        grayMenu.add(erodeItem);
        
        dilateItem = new MenuItem("�Ҷ�����");
        dilateItem.addActionListener(this);
        grayMenu.add(dilateItem);
        
        OPENItem = new MenuItem("����");
        OPENItem.addActionListener(this);
        grayMenu.add(OPENItem);       
        
        CLOSEItem = new MenuItem("�պ�");
        CLOSEItem.addActionListener(this);
        grayMenu.add(CLOSEItem);
        
        grayMenu.addSeparator();
        gradItem = new MenuItem("Sobel����");
        gradItem.addActionListener(this);
        grayMenu.add(gradItem);
             
        filt1Item = new MenuItem("��̬ѧ�˲�1");
        filt1Item.addActionListener(this);
        grayMenu.add(filt1Item);
        
        filt2Item = new MenuItem("��̬ѧ�˲�2");
        filt2Item.addActionListener(this);
        grayMenu.add(filt2Item);
        
        filt3Item = new MenuItem("��̬ѧ�˲�3");
        filt3Item.addActionListener(this);
        grayMenu.add(filt3Item);
        
        topHatItem = new MenuItem("��ñ�任");
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
    
    MenuItem erode1Item;      //�����ṹ��ʴ
    MenuItem erode2Item;      //���νṹ��ʴ
    MenuItem erode3Item;      //�˽ǽṹ��ʴ
    
    MenuItem dilate1Item;     //�����ṹ����
    MenuItem dilate2Item;     //���νṹ����
    MenuItem dilate3Item;     //�˽ǽṹ����
    MenuItem OPEN1Item;       //�����ṹ����    
    MenuItem CLOSE1Item;      //�����ṹ�պ�
    
    MenuItem filter1Item;     //�����ṹ�˲�
    MenuItem edge1Item;       //�����ṹ��Ե��ȡ
    MenuItem fillItem;        //�������
    MenuItem eulerItem;       //����Euler��
    MenuItem hmtItem;         //����δ���б任  
    
    MenuItem erodeItem;       //�Ҷȸ�ʴ
    MenuItem dilateItem;      //�Ҷ�����    
    MenuItem OPENItem;        //�Ҷȿ���    
    MenuItem CLOSEItem;       //�Ҷȱպ�
    
    MenuItem gradItem;        //Sobel����        
    MenuItem filt1Item;       //��̬ѧ�˲�1,�ȿ���� 
    MenuItem filt2Item;       //��̬ѧ�˲�2,�ȱպ�
    MenuItem filt3Item;       //��̬ѧ�˲�3,�����˲� 
    MenuItem topHatItem;      //��ñ�任 
}
