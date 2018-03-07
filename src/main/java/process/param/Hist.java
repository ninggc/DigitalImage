//Hist.java
package process.param;

import java.awt.*;
import java.awt.event.*;

public class Hist extends Frame implements ActionListener
{	
	int maxn, data[];
	int histogram[] = new int[256];
	
	public Hist(String str)
	{
		setTitle(str);//"ͼ��ֱ��ͼ"
		
		Panel pdown = new Panel();
		Button quit = new Button("�ر�");
		quit.addActionListener(this);
		
		add(pdown,BorderLayout.SOUTH);
		pdown.add(quit);
		
		//�رմ���
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});	
		setSize(300, 300);
		setVisible(true);
	}
		
	public void actionPerformed(ActionEvent e)
	{
		hide();
	}
	
    public void getData(int [] data,int iw,int ih)
	{
		this.data = data;
		for(int i = 0; i < iw*ih; i++)
		{
			int grey = data[i]&0xff;
			histogram[grey]++;
		}
		
		//�ҳ�������,���б�׼��.
		maxn = histogram[0];
		for(int i = 1; i < 256; i++)
			if(maxn <= histogram[i])
				maxn = histogram[i];	
			
		for(int i = 0; i < 256; i++)		
			histogram[i] = histogram[i]*200/maxn;					
	}
	
	public void paint(Graphics g)
	{		
		//����ˮƽ�ʹ�ֱ����
		g.drawLine(30, 250, 286, 250);
		g.drawLine(30, 50,  30, 250);
		
		g.drawLine(30, 50, 32, 50);
		g.drawLine(30, 150, 32, 150);
		
		g.drawString("0",   28,  263);
		g.drawString("50",  65, 263);
		g.drawString("100", 123, 263);
		g.drawString("150", 173, 263);
		g.drawString("200", 223, 263);
		g.drawString("250", 273, 263);   
		
		g.drawString(""+(maxn/2), 5, 155);
		g.drawString(""+maxn,   5, 55);
		
		//ֱ��ͼ
		for(int i = 0; i < 256; i++)
			g.drawLine(30+i, 250, 30+i, 250-histogram[i]);					
	}
}