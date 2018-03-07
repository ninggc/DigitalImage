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
            
    int iw, ih,         //ͼ����
        ow, oh;         //���󡢴��С�ƽ��,���ͼ����
        
    int[] pix, pix2,
          opix;         //����ͼ������; 
               
    //����ѡ�����
    JButton okButton;
	JDialog dialog; 
     
    Common common;
    GeomTrans geom;
            
    public Ch4GeomTrans()
    {    
        setTitle("����ͼ����-Java�����ʵ�� ��4�� ͼ�񼸺α任");
        this.setBackground(Color.lightGray);
        
        //�˵�����
        setMenu();
        
        //�رմ���
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
            common.chooseFile(chooser, "./images", 0);//����Ĭ��Ŀ¼,�����ļ�
            int r = chooser.showOpenDialog(null);
            
            if(r == JFileChooser.APPROVE_OPTION) 
            {  
                String name = chooser.getSelectedFile().getAbsolutePath();
                
                //װ��ͼ��
			    iImage = common.openImage(name, new MediaTracker(this));       
			    
			    //ȡ����ͼ��Ŀ�͸�
			    iw = iImage.getWidth(null);
			    ih = iImage.getHeight(null);
			    bImage = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_RGB);
			    Graphics2D g2 = bImage.createGraphics();
			    g2.drawImage(iImage, 0, 0, null);			    
			    loadflag = true;
			    repaint();			               
            }
        }
        else if (evt.getSource() == rotateItem)//������ת
        {
        	setTitle("��4�� ͼ�񼸺α任 ������ת ���� ���ƻ�");       	
        	common.draw(g, iImage, bImage, 
        	            common.getParam("��ת��(��):","30"), 0, 0);                  	
        }
        else if (evt.getSource() == scaleItem)//��������
        {       	
        	setTitle("��4�� ͼ�񼸺α任 �������� ���� ���ƻ�");
        	//����ѡ�����
        	Parameters pp = new Parameters("����","x����:",
        	                               "y����:", "1.5", "1.5");
        	setPanel(pp, "��������");
        	float x = pp.getPadx();
        	float y = pp.getPady(); 
        	common.draw(g, iImage, bImage, x, y, 1);  	
        }
        else if (evt.getSource() == shearItem)//���ô���
        {
        	setTitle("��4�� ͼ�񼸺α任 ���ô��� ���� ���ƻ�");
        	Parameters pp = new Parameters("����","x����:",
        	                               "y����:", "0.5", "0.5");
        	setPanel(pp, "���ô���");
            float x = pp.getPadx();
        	float y = pp.getPady();
        	common.draw(g, iImage, bImage, x, y, 2);    	
        }
        else if (evt.getSource() == transItem)//����ƽ��
        {
        	setTitle("��4�� ͼ�񼸺α任 ����ƽ�� ���� ���ƻ�");
        	Parameters pp = new Parameters("����","x����:",
        	                               "y����:", "100", "50");
        	setPanel(pp, "����ƽ��");                                         
        	float x = pp.getPadx();
        	float y = pp.getPady();
        	common.draw(g, iImage, bImage, x, y, 3);         	       	
        }
        else if (evt.getSource() == rotItem)//��ת�㷨
        {
        	setTitle("��4�� ͼ�񼸺α任 ��ת�㷨 ���� ���ƻ�");       	
        	pix = common.grabber(iImage, iw, ih);
			
			//��ת,���ͼ���� 
			int owh = (int)(Math.sqrt(iw * iw + ih * ih + 0.5));   
			opix = geom.imRotate(pix, common.getParam("��ת��(��):","30"), 
			                     iw, ih, owh);
			
			//�������е����ز���һ��ͼ��
			MemoryImageSource memoryImage  = new MemoryImageSource(owh, owh,
		                                         ColorModel.getRGBdefault(),
		                                                      opix, 0, owh);
		    oImage = createImage(memoryImage);
			common.draw(g, iImage, oImage, iw, ih, owh, 4);                  	
        }
        else if (evt.getSource() == mirItem)//�����㷨(type:5)
        {
        	setTitle("��4�� ͼ�񼸺α任 �����㷨 ���� ���ƻ�");
        	Parameters pp = new Parameters("ѡ��������", 
        	                               "ˮƽ", "��ֱ");
        	setPanel(pp, "�����㷨");
        	
        	pix  = common.grabber(iImage, iw, ih);
        	opix = geom.imMirror(pix, iw, ih, pp.getRadioState());
        	ImageProducer ip = new MemoryImageSource(iw, ih, opix, 0, iw);
		    oImage = createImage(ip); 
		    common.draw(g, iImage, oImage, iw, ih, 0, 5);  
        }
        else if (evt.getSource() == shrItem)//�����㷨(type:6)
        {
        	setTitle("��4�� ͼ�񼸺α任 �����㷨 ���� ���ƻ�");
        	Parameters pp = new Parameters("����","x����:",
        	                               "y����:", "0.5", "0.5");
        	setPanel(pp, "�����㷨");
        	
        	pix  = common.grabber(iImage, iw, ih);
        	
        	float shx = pp.getPadx();
            float shy = pp.getPady();
            
            //�����Χ�еĿ�͸�
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
    	        JOptionPane.showMessageDialog(null, "��������Ϊ����!");               	             
    	}
    	else if (evt.getSource() == trnItem)
        {
        	setTitle("��4�� ͼ�񼸺α任 ƽ���㷨 ���� ���ƻ�");
        	Parameters pp = new Parameters("����","x����:",
        	                               "y����:", "100", "50");
        	setPanel(pp, "ƽ���㷨");
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
    	        JOptionPane.showMessageDialog(null, "��������Ϊ����!");    	                  	             
    	}
    	else if (evt.getSource() == nearItem)
        {
        	setTitle("��4�� ͼ�񼸺α任 ���ڽ���ֵ�㷨 ���� ���ƻ�");
            pix  = common.grabber(iImage, iw, ih); 
		    
		    float p = (Float.valueOf(JOptionPane.showInputDialog(null,
		               "�������Ų���(0.1-3.0)", "1.50"))).floatValue();
    		int ow = (int) (p * iw);          //����Ŀ��ͼ���
	        int oh = (int) (p * ih);
		    opix = geom.nearNeighbor(pix, iw, ih, ow, oh, p);
		    ImageProducer ip = new MemoryImageSource(ow, oh, opix, 0, ow);
		    oImage = createImage(ip); 
            common.draw(g, oImage, "���ڽ���ֵ", p);       		
        }
        else if (evt.getSource() == linrItem)
        {
        	setTitle("��4�� ͼ�񼸺α任 ˫���Բ�ֵ�㷨 ���� ���ƻ�");
        	pix  = common.grabber(iImage, iw, ih); 
		    
		    float p = (Float.valueOf(JOptionPane.showInputDialog(null,
		               "�������Ų���(0.1-3.0)", "1.50"))).floatValue();
    		int ow = (int) (p * iw);          //����Ŀ��ͼ���
	        int oh = (int) (p * ih);
		    opix = geom.bilinear(pix, iw, ih, ow, oh, p);
		    ImageProducer ip = new MemoryImageSource(ow, oh, opix, 0, ow);
		    oImage = createImage(ip); 
            common.draw(g, oImage, "˫���Բ�ֵ", p);               		
        }
        else if (evt.getSource() == cubicItem)
        {
        	setTitle("��4�� ͼ�񼸺α任 ���ξ����ֵ�㷨 ���� ���ƻ�");
        	pix  = common.grabber(iImage, iw, ih); 
		    
		    float p = (Float.valueOf(JOptionPane.showInputDialog(null,
		               "�������Ų���(1.1-3.0)", "1.50"))).floatValue();
		    if(p < 1)
		    {		    
		        JOptionPane.showMessageDialog(null, "����p�������1!");
		        return; 
		    }     
    		int ow = (int) (p * iw);          //����Ŀ��ͼ���
	        int oh = (int) (p * ih);
		    opix = geom.scale(pix, iw, ih, ow, oh, p, p);
		    ImageProducer ip = new MemoryImageSource(ow, oh, opix, 0, ow);
		    oImage = createImage(ip); 
            common.draw(g, oImage, "���ξ����ֵ", p);               		
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
        	g2D.translate(0, 50);           //����ͼ�����Ͻ�Ϊ��ǰ��
        	g.drawImage(iImage,0,0,null);   //������ͼ        		
        }
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
    	//�˵�����
        Menu fileMenu = new Menu("�ļ�");
        openItem = new MenuItem("��");
        openItem.addActionListener(this);
        fileMenu.add(openItem);

        exitItem = new MenuItem("�˳�");
        exitItem.addActionListener(this);
        fileMenu.add(exitItem);
    
        Menu affineMenu = new Menu("���ñ任");        
        rotateItem = new MenuItem("������ת");
        rotateItem.addActionListener(this);
        affineMenu.add(rotateItem);        
        
        affineMenu.addSeparator();
        scaleItem = new MenuItem("��������");
        scaleItem.addActionListener(this);
        affineMenu.add(scaleItem);
        
        affineMenu.addSeparator();
        shearItem = new MenuItem("���ô���");
        shearItem.addActionListener(this);
        affineMenu.add(shearItem);
        
        affineMenu.addSeparator();
        transItem = new MenuItem("����ƽ��");
        transItem.addActionListener(this);
        affineMenu.add(transItem);
        
        Menu affin2Menu = new Menu("����任");
        rotItem = new MenuItem("��ת�㷨");
        rotItem.addActionListener(this);
        affin2Menu.add(rotItem);        
        
        affin2Menu.addSeparator();
        mirItem = new MenuItem("�����㷨");
        mirItem.addActionListener(this);
        affin2Menu.add(mirItem);
        
        affin2Menu.addSeparator();
        shrItem = new MenuItem("�����㷨");
        shrItem.addActionListener(this);
        affin2Menu.add(shrItem);
        
        affin2Menu.addSeparator();
        trnItem = new MenuItem("ƽ���㷨");
        trnItem.addActionListener(this);
        affin2Menu.add(trnItem);
        
        Menu interpMenu = new Menu("ͼ���ֵ");
        nearItem = new MenuItem("���ڽ���ֵ");
        nearItem.addActionListener(this);
        interpMenu.add(nearItem);
        
        interpMenu.addSeparator();
        linrItem = new MenuItem("˫���Բ�ֵ");
        linrItem.addActionListener(this);
        interpMenu.add(linrItem);
        
        interpMenu.addSeparator();
        cubicItem = new MenuItem("���ξ����ֵ");
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
    MenuItem rotateItem;//������ת(type:0)
    MenuItem scaleItem; //��������(type:1)
    MenuItem shearItem; //���ô���(type:2)
    MenuItem transItem; //����ƽ��(typa:3)
     
    MenuItem rotItem;   //��ת�㷨(type:4)
    MenuItem mirItem;   //�����㷨(type:5)
    MenuItem shrItem;   //�����㷨(type:6)
    MenuItem trnItem;   //ƽ���㷨(typa:7)
     
    MenuItem nearItem;  //���ڽ���ֵ�㷨
    MenuItem linrItem;  //˫���Բ�ֵ�㷨
    MenuItem cubicItem; //���ξ����ֵ�㷨
}
