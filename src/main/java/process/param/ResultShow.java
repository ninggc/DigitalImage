package process.param;

import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import javax.swing.border.*;

public class ResultShow extends JFrame 
{
	Graphics g;
	Image image; 
	String ents, avrs, meds, sqss;
	double entr, aver, medn, sqsm;
    
    public ResultShow(Image iIm)
    {
    	image = iIm;
        setSize(320, 330);
		setVisible(true);
		setLocation(200, 360);	
    } 
                        
	public ResultShow(Graphics g,  String ents, double entr, 
                      String avrs, double aver, String meds, 
                      double medn, String sqss, double sqsm)
	{
		this.g = g;
		this.ents = ents;
		this.avrs = avrs;
		this.meds = meds;
		this.sqss = sqss;
		this.entr = entr;
		this.aver = aver;
		this.medn = medn;
		this.sqsm = sqsm;
		
		setSize(320, 330);
		setVisible(true);
		setLocation(200, 360);
	}
	
	public void paint(Graphics g) 
    {
    	if(image != null)
    	    g.drawImage(image, 30, 50, null);    	
    	else
    	{    	  	  
	       	g.setColor(Color.blue);
	    	g.setFont(new Font("",Font.BOLD,18));
	    	g.drawString(ents, 10, 80);
	    	g.drawString(""+entr, 10,100); 
	    	g.drawString(avrs, 10, 140);
	    	g.drawString(""+aver, 10,160);  
	    	g.drawString(meds, 10, 200);
	    	g.drawString(""+medn, 10,220); 
	    	g.drawString(sqss, 10, 260);
	    	g.drawString(""+sqsm, 10,280);
    	}    	         	
    }   
}
