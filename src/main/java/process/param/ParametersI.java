//ParametersI.java

package process.param;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class ParametersI extends JPanel 
{
	JLabel lx1, ly1, lx2, ly2 ;
    Checkbox x1box, y1box, x2box, y2box, x3box;
    JTextField tfx1, tfy1, tfx2, tfy2;
			   
	public ParametersI() 
	{
		lx1 = new JLabel("x1:");
		ly1 = new JLabel("y1:");
		lx2 = new JLabel("x2:");
		ly2 = new JLabel("y2:");
		
		tfx1 = new JTextField("50",5); 
	    tfy1 = new JTextField("30",5); 
        tfx2 = new JTextField("200",5);
		tfy2 = new JTextField("210",5);		
		add(lx1); add(tfx1); add(ly1); add(tfy1);
		add(lx2); add(tfx2); add(ly2); add(tfy2);
		
        setBorder(new CompoundBorder(
			BorderFactory.createTitledBorder("选择两点(x1,y1)和(x2,y2)"),
			BorderFactory.createEmptyBorder(10, 10, 50, 10)));
	}
	
	public ParametersI(String note, String sx1, String sy1, 
	                   String sx2,  String sy2, String sx3)             //5个按钮
	{
		CheckboxGroup cbg = new CheckboxGroup();
		x1box = new Checkbox(sx1, cbg, true);
		y1box = new Checkbox(sy1, cbg, true);
		x2box = new Checkbox(sx2, cbg, true);
		y2box = new Checkbox(sy2, cbg, true);
		x3box = new Checkbox(sx3, cbg, true);		
		add(x1box); add(y1box);
		add(x2box); add(y2box); add(x3box);
				
        setBorder(new CompoundBorder(
			BorderFactory.createTitledBorder(note),
			BorderFactory.createEmptyBorder(10, 10, 50, 10)));
	}
	
	public ParametersI(String note, String sx1, String sy1, 
	                   String sx2, String sy2)                         //4个按钮
	{
		CheckboxGroup cbg = new CheckboxGroup();
		x1box = new Checkbox(sx1, cbg, true);
		y1box = new Checkbox(sy1, cbg, true);
		x2box = new Checkbox(sx2, cbg, true);
		y2box = new Checkbox(sy2, cbg, true);		
		add(x1box); add(y1box);
		add(x2box); add(y2box);
				
        setBorder(new CompoundBorder(
			BorderFactory.createTitledBorder(note),
			BorderFactory.createEmptyBorder(10, 10, 50, 10)));
	}
	
	public ParametersI(String note, String sx1, String sy1, String sx2)//3个按钮 
	{
		CheckboxGroup cbg = new CheckboxGroup();
		x1box = new Checkbox(sx1, cbg, true);
		y1box = new Checkbox(sy1, cbg, true);
		x2box = new Checkbox(sx2, cbg, true);
				
		add(x1box); add(y1box);	add(x2box); 
				
        setBorder(new CompoundBorder(
			BorderFactory.createTitledBorder(note),
			BorderFactory.createEmptyBorder(10, 10, 50, 10)));
	}
	
	public int getx1() 
	{
		return Integer.parseInt(tfx1.getText());
	}
	
	public int gety1() 
	{
		return Integer.parseInt(tfy1.getText());
	}
	
	public int getx2() 
	{
		return Integer.parseInt(tfx2.getText());
	}
	
	public int gety2() 
	{
		return Integer.parseInt(tfy2.getText());
	}
	
	public int getRadioState3()
	{
		if(x1box.getState())      return 0;
		else if(y1box.getState()) return 1;
		else if(x2box.getState()) return 2;
		else return 0;
	}
	
	public int getRadioState4()
	{
		if(x1box.getState())      return 0;
		else if(y1box.getState()) return 1;
		else if(x2box.getState()) return 2;
		else if(y2box.getState()) return 3;
		else return 0;
	}
	
	public int getRadioState5()
	{
		if(x1box.getState())      return 0;
		else if(y1box.getState()) return 1;
		else if(x2box.getState()) return 2;
		else if(y2box.getState()) return 3;
		else if(x2box.getState()) return 4;
		else return 0;
	}
}
