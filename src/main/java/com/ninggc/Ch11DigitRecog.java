/*
 * @Ch11DigitRecog.java
 * @Version 1.0 2010.02.21
 * @Author Xie-Hua Sun
 */

package com.ninggc;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

class Ch11DigitRecog extends JFrame implements ActionListener
{
    int model,       //数字代号
        type,        //算法型号,0:模板匹配和Bayes,1:LMSE和奖惩算法
        m_num = 0;
    int count;  
        
    int[][][] sample;
    double[][][] Dsample;
    boolean isChooseAlg, //算法选择标志
            isSaved;     //数据文件存储标志
	String  algName;
	
    public Ch11DigitRecog() 
    {
    	setTitle("数字图像处理-Java编程与实验 第11章 模式识别");
	    
	    sample  = new int[10][2][25];
	    Dsample = new double[10][2][25];
	    count  = 0;
        model  = 0;
	   	isChooseAlg = false;
	   	
	   	setMenu(); 	  
	                  
	    addWindowListener(new WindowAdapter() 
	    {
		    public void windowClosing(WindowEvent e) 
		    {			   
		        if(isSaved == false)
		            if(check() == 0)	                  
	                    save();           
	            System.exit(0);
		    }
        });	
        
        setSize(368,430);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e)
	{
	    if(e.getSource() == recognize)        //识别
	    {	    		    	
	    	if(type == 0)   
	    	    result.setText(" "+ recognize());
	    	else if(type == 1) 
	    	    result.setText(" "+ TwoValueBayes());
	    	else if(type == 2) 
	    	    result.setText(" "+ LMSE());
	    	else if(type == 3)
	    	    result.setText(" "+ Reward_Punish());
		    train.setEnabled(false);		    
            num.setEnabled(false);  	           
            inform.setText("使用按钮清除可擦除数字");            
		} 
		else if(e.getSource() == clear)
	        clearall();            
        else if(e.getSource() == train)       //训练
        {
        	if(type == 0 ||type == 1)
        	{        	
		        int[] tem = character();
		        
		        for(int j = 0; j < 25; j++)
		            sample[model][m_num][j] = tem[j];
		        inform.setText("数字 "+model+" 的第 "+(m_num+1)+
	                       " 个样本已完成训练.\n"+
	                       "每个数字输入两个样本.");	        
		        m_num++;
		        m_num = m_num%2;        
		        
		        if(model == 9)
		            recognize.setEnabled(true);
		                
		        clearall();
	        }
	        else
	        {
	        	double[] dtem = CHARACTER();
		        
		        for(int j = 0; j < 25; j++)	
		            Dsample[model][m_num][j] = dtem[j];
	        
		        inform.setText("数字 "+model+" 的第 "+(m_num+1)+
		                       " 个样本已完成训练.\n"+
		                       "每个数字输入两个样本.");	        
		        m_num++;
		        m_num = m_num%2;        
		        
		        if(model == 9)
		            recognize.setEnabled(true);
		                
		        clearall(); 
	        }               
        }
        else if(e.getSource() == news)        //新建样本
        {
        	if(isChooseAlg)
	    	{
		        inform.setText("按下鼠标左键并拖动, 在右边输入板中手写数字,"+
		                       "\n"+"依次输入0 - 9数字样本");
		        recognize.setEnabled(false);
		        alg.setEditable(false);	        
		        train.setEnabled(true);
		        num.setEnabled(true);
		        clear.setEnabled(true);
		        isSaved = false;
	        }
            else
                JOptionPane.showMessageDialog(null, "请先选择识别算法!"); 
        }
        else if(e.getSource() == save)        //保存样本
           	save();         	
        else if(e.getSource() == load)        //载入样本
        {
        	if(isChooseAlg)
	    	{
	        	load();
	        	isSaved = true;
	        	alg.setEnabled(false);
	        	num.setEnabled(false);
	        	inform.setText("按下鼠标左键并拖动, 在右边输入板中手写数字");
        	}
            else
                JOptionPane.showMessageDialog(null, "请先选择识别算法!");         
        }
        else if(e.getSource() == renew)      //更改算法,初始化
        {
        	count  = 0;
            model  = 0;
        	isChooseAlg = false;        
            alg.setEnabled(true);
            
            recognize.setEnabled(false);
		    clear.setEnabled(false);
		    train.setEnabled(false);
		    num.setEnabled(false);
		    
        }
        else if(e.getSource() == alg)         //算法选择框
        {   	
            algName = (String)alg.getSelectedItem();
            if(algName.equals("模板匹配算法"))
                type = 0;
            else if(algName.equals("Bayes分类器"))
                type = 1;
            else if(algName.equals("LMSE"))
                type = 2;
            else
                type = 3;
            isChooseAlg = true; 
        }              
        else if(e.getSource() == num)         //训练选择框
           	model = Integer.parseInt((String)num.getSelectedItem());	        	        	        	         
        else if(e.getSource() == quit)
	    {
            if(isSaved == false)
                if(check() == 0)
                   	save();                	
	               
            System.exit(0);
        }
    }
    
    //标准化,将输入的数字缩放到5X5标准大小
	int[] character()
	{
		int minx = 1000, miny = 1000, 
	        maxx = 0,    maxy = 0;
	    int ti, tj;
        int[] chr = new int[25];
        
        int[][] temp = can.getXY();       
        
        //分别计算最小和最大的x[i],y[i]
	    for(int i = 0; i < count; i++)
	    {
	        if(temp[0][i] == -1)  continue;
	        if(temp[0][i] < minx) minx = temp[0][i];
		    if(temp[1][i] < miny) miny = temp[1][i];
		    if(temp[0][i] > maxx) maxx = temp[0][i];
		    if(temp[1][i] > maxy) maxy = temp[1][i];		    
		}
	    int xlen = maxx - minx;
	    int ylen = maxy - miny;
	    	    
	    //按比例缩放到标准大小.并计算row[i],col[i] 　
	    for(int i = 0; i < count; i++)
	    {
	         if(temp[0][i] == -1) continue;
	         
	         ti = (temp[1][i]-miny)*5/ylen;
	         tj = (temp[0][i]-minx)*5/xlen;
	         if(ti == 5) ti = 4;
		     if(tj == 5) tj = 4;
		     
		     chr[ti*5+tj]++;		                      
		}		
	    return chr;	    
    }
    
    //标准化,将输入的数字缩放到5X5标准大小
	public double[] CHARACTER()
	{
		int minx = 1000, miny = 1000, 
	        maxx = 0,    maxy = 0;
	    int ti, tj;
        int[] chr  = new int[25];
        double[] t = new double[25];
        
        int[][] temp = can.getXY();       
        
        //分别计算最小和最大的x[i],y[i]
	    for(int i = 0; i < count; i++)
	    {
	        if(temp[0][i] == -1)  continue;
	        if(temp[0][i] < minx) minx = temp[0][i];
		    if(temp[1][i] < miny) miny = temp[1][i];
		    if(temp[0][i] > maxx) maxx = temp[0][i];
		    if(temp[1][i] > maxy) maxy = temp[1][i];		    
		}
	    int xlen = maxx - minx;
	    int ylen = maxy - miny;
	    	    
	    //按比例缩放到标准大小.并计算row[i],col[i] 　
	    for(int i = 0; i < count; i++)
	    {
	         if(temp[0][i] == -1) continue;
	         
	         ti = (temp[1][i]-miny)*5/ylen;
	         tj = (temp[0][i]-minx)*5/xlen;
	         if(ti == 5) ti = 4;
		     if(tj == 5) tj = 4;
		     
		     chr[ti*5+tj]++;		                      
		}
		int w = xlen/5;
		int h = ylen/5;
		for(int i = 0; i < 5; i++)
		    for(int j = 0; j < 5; j++)
		        t[i*5+j] = chr[i*5+j]*7.0/(w*h);		
	    return t;	    
    }
   
    int recognize()
    {
    	int[][] distance = new int[10][2];
    	int[]   tem = character();
        int n = 0;
        
        for(int k = 0; k < 10; k++)
        {
        	for(int i = 0; i < 2; i++)
        	{        	
        	    distance[k][i] = 0;
	            for(int j = 0; j < 25; j++)
	                distance[k][i] += Math.abs(sample[k][i][j]-tem[j]);	
	        }            
	    }
	     
	    //计算distance[k][i]的最小者和相应的序号k
	    for(int k = 1; k < 10; k++)
	      	for(int i = 0; i < 2; i++)
	            if(distance[k][i] < distance[n][i])
	                n = k;		        
	    return n;		     
    }
    
    int TwoValueBayes()
	{
		double Pw[]  = new double[10];    //先验概率P(wj)=Nj/N
		double P[][] = new double[10][25];//Pj(wi) (wi:wi类, j:第j个特征)
		double PXw[] = new double[10];    //类条件概率P(X|wj)
		double PwX[] = new double[10];    //后验概率P(wj|X)
		
	    int[] testsample; 
		
		int i,j,k;
	
		//求先验概率
		int n[] = new int[10];            //各类样品数
				
        testsample = character();
        
		for(i = 0; i < 10; i++)
			Pw[i] = 1.0 / 10.0;           //先验概率. see Book, p.53, (4.5.1)
			
		//求类条件概率
		for(k = 0; k < 10; k++)
		{
			for(j = 0; j < 25; j++)
			{
				int numof1  = 0;         //二值数据中1的个数
				for(i = 0; i < 2; i++)
					numof1 += sample[k][i][j] > 0 ? 1 : 0;
				P[k][j] = (double)(numof1+1);  //see Book, p.53, (4.5.2)
								
			}
		}
	
	    //求类条件概率
		for(i = 0; i < 10; i++)
		{
			double p = 1;
			for(j = 0; j < 25; j++)           //see Book, p.53, (4.5.3)
				p *= (testsample[j] > 0) ? P[i][j] : 1;
			
			PXw[i] = p;
		}
	
		//求后验概率
		double PX  = 0.0, maxP = 0.0;
		int number = 0;
		
		for(i = 0; i < 10; i++)
			PX += PXw[i];
		   	    
		for(i = 0; i < 10; i++)
		{
			PwX[i] = PXw[i] / PX;
			if(PwX[i] > maxP)
			{
				maxP   = PwX[i];
				number = i;
			}
		}
		return number;
	}   
    
    /******************************************************************
	 *   函数名称：LMSE()
	 *   函数功能：LMSE算法 ,返回手写数字的类别
	 ******************************************************************/
	int LMSE()
	{
		double w[][] = new double[10][26];  //权值
		double d[]   = new double[10];      //迭代计算结果权矢量
		double x[]   = new double[26];      //增1样品
		boolean flag;
		int n, i, j, k;
	
	    double[] testsample = CHARACTER();
	    
		//权值初值为0
		for(n = 0; n < 10; n++)
			for(i = 0; i < 26; i++)
				w[n][i] = 0;
		int c = 0, cc = 1;
		do
		{
			flag = true;
			for(n = 0; n < 10; n++)
			{
				for(i = 0; i < 2; i++)
				{
					//取已知样品
					for(j = 0; j < 25; j++)
						x[j] = Dsample[n][i][j];
						
					x[25] = 1;            //末位置1
					//计算结果权矢量
					for(j = 0; j < 10; j++)
						d[j] = 0;
						
					for(j = 0; j < 10; j++)
						for(k = 0; k < 26; k++)
							d[j] += w[j][k]*x[k];
							
					boolean f = true;
					//d[n]是否最大值？
					for(j = 0; j < 10; j++)
						if(j != n)
							f &= (d[n]>d[j])?true:false;
						
					if(f)//若d[n]为最大值
						flag &= true;
					else
						flag &= false;
	
					for(j = 0;j < 10; j++)//改变权值，10个
					{
						double rX;
						if(j == n)     //是当前类别
							rX = 1.0;  
						else
							rX = 0.0;  
						
						for(k = 0; k < 26; k++)
							w[j][k] += x[k]*(rX-d[j])/cc;						
					}
					++cc;
				}
			}
			if(++c>2000) break;
		}while(!flag);//到此为止得到线性判别函数权矢量
		
		double hx[]  = new double[10];		
		double num[] = new double[26];
		//取未知样品
		for(i = 0;i < 25;i++)
			num[i] = testsample[i];
		num[25] = 1;//末位置1
		//计算判别函数
		for(n = 0;n < 10; n++)
		{
			hx[n] = 0;
			for(i = 0;i < 26; i++)
				hx[n] += w[n][i]*num[i];
		}
		//判别函数最大值
		double maxval = hx[0];
		int number = 0;
		for(n = 1; n < 10; n++)
		{
			if(hx[n] > maxval)
			{
				maxval = hx[n];
				number = n;
			}
		}
		return number;
	}   
    
   /******************************************************************
	*   函数名称：Reward_Punish()
	*   函数功能：奖惩算法, 返回手写数字的类别
	******************************************************************/
	int Reward_Punish()
	{
		double w[][]= new double[10][26];//权值
		double d[]  = new double[10];    //迭代计算结果权矢量
		double x[]  = new double[26];    //增1样品
		double hx[] = new double[10];    //判别函数
		boolean flag;
		int n, i, j, k;
		int c=0;         //迭代次数
	
	    double[] testsample = CHARACTER();
	    
		//权值初值为0
		for(n = 0;n < 10; n++)
			for(i = 0;i < 26; i++)
				w[n][i] = 0;
		do
		{
			flag = true;
			for(n = 0;n < 10; n++)
			{
				for(i = 0;i < 2; i++)  
				{
					//取已知样品
					for(j = 0; j < 25; j++)
						x[j] = Dsample[n][i][j];
					x[25] = 1;      //最后一位置1
					//计算结果权矢量
					for(j = 0;j < 10; j++)
						d[j] = 0;
					for(j = 0;j < 10; j++)
						for(k = 0;k < 26; k++)
							d[j] += w[j][k]*x[k];
					boolean f = true;
					//判断d[n]是否为最大值
					for(j = 0; j < 10; j++)
						if(j != n)
							f &= (d[n]>d[j])?true:false;
						
					if(f)            //若d[n]为最大值
						flag &= true;//通过检验
					else             //否则，未通过检验需要修正权值
					{
						for(j = 0; j < 10; j++)
							for(k = 0; k < 26; k++)
								if(j == n)
									w[j][k] += x[k];
								else if(d[j] > d[n])
									w[j][k] -= x[k];							
						
						flag&=false;
					}
				}
			}
			if(++c > 2000) break;
		}while(!flag);   //到此为止得到线性判别函数权矢量
	
		double num[] = new double[26];  //未知样品
		
		for(i = 0; i < 25; i++)
			num[i] = testsample[i];
		
		num[25] = 1;                    //末位置1
		for(n = 0; n < 10; n++)         //计算判别函数
		{
			hx[n] = 0;
			for(i = 0; i < 26; i++)
				hx[n] += w[n][i]*num[i];
		}
	
		double maxval = hx[0];
		int number = 0;
		for(n = 1; n < 10; n++)//取判别函数最大值
		{
			if(hx[n] > maxval)
			{
				maxval = hx[n];
				number = n;
			}
		}
		return number;
	}
	        
    //存储数据
    public void save()
    {
    	FileDialog fd = new FileDialog(this,"保存文件",FileDialog.SAVE);
    	fd.setVisible(true);
    	try
	    {
	    	if(fd.getFile() != null)
            {
			    RandomAccessFile rf = new RandomAccessFile(fd.getFile(), "rw");
			    if(type == 0||type == 1)
				    for(int k = 0; k < 10; k++)
				        for(int i = 0; i < 2; i++)
				       	    for(int j = 0; j < 25; j++)
				       	        rf.writeInt(sample[k][i][j]);                                        
                else
                    for(int k = 0; k < 10; k++)         //10个数字
				        for(int i = 0; i < 2; i++)      //2个样本
				       	    for(int j = 0; j < 25; j++) //5X5=25个块
				       	        rf.writeDouble(Dsample[k][i][j]); 
                rf.close();
                isSaved = true;    	
	            inform.setText("在当前目录存储文件"+fd.getFile()+"成功!");	               
			}						
		}			
	    catch(FileNotFoundException e1)
	    {
	    	e1.printStackTrace();

			System.err.println(e1);
	    }
	    catch(IOException e2)
	    {
	        System.err.println(e2);
	    }	     
    }
    
    //载入数据
    public void load()
    {
    	FileDialog fd = new FileDialog(this, "打开文件", FileDialog.LOAD);
    	fd.setVisible(true);
    	try
	    {
	    	if(fd.getFile() != null)
            {
            	FileInputStream fin = new FileInputStream(fd.getFile());			  	    
	  	        DataInputStream  in = new DataInputStream(fin);
			    if(type == 0||type == 1)
				    for(int k = 0; k < 10; k++)        //10个数字
				        for(int i = 0; i < 2; i++)     //2个样本
				       	    for(int j = 0; j < 25; j++)
				       	        sample[k][i][j] = in.readInt();
				else
				    for(int k = 0; k < 10; k++)         
				        for(int i = 0; i < 2; i++)      
				       	    for(int j = 0; j < 25; j++) 
				       	        Dsample[k][i][j] = in.readDouble();
				       	                                         
                in.close();
                recognize.setEnabled(true);
		        clear.setEnabled(true);
		        train.setEnabled(true);
		        num.setEnabled(true);	               
			}						
		}			
	    catch(FileNotFoundException e1)
	    {
	        System.err.println(e1);
	    }
	    catch(IOException e2)
	    {
	        System.err.println(e2);
	    }	        	
    }
    
    void clearall()
    {
        count = 0;
        can.repaint();
        result.setText(" ");
    }
    
    int check()
	{
		String s = "是否要保存已修改的样本到样本文件?";
        String t = "提示";       	               
        int tem  = JOptionPane.showConfirmDialog(new JFrame(), s, t, 0);
        return tem;
	}	   
    
	class myCanvas extends Canvas implements MouseListener,MouseMotionListener
	{
		int[][] pos = new int[2][500];//pos[0][]=x[],pos[1][]=y[]	  
	    myCanvas()
	    {
	        addMouseListener(this);
	        addMouseMotionListener(this);
	        setBackground(Color.white);
	    }
	  
	    public void paint(Graphics g)
	    {
	        g.setColor(Color.red);
	        for(int i = 0; i < count-1; i++)
	            if(pos[0][i+1] == -1)
	            {
	                i++;
	                continue;
	            }
	            else
	                g.drawLine(pos[0][i], pos[1][i], pos[0][i+1], pos[1][i+1]);        
	    }
	    
	    public int[][] getXY()
	    {
	    	return pos;
	    }
	    
	    public void mouseClicked(MouseEvent me){}
	    
	    public void mousePressed(MouseEvent me)
	    {
	        pos[0][count] = me.getX();
	        pos[1][count] = me.getY();
	        count++;
	    }
	    public void mouseReleased(MouseEvent me)
	    {
	        pos[0][count] = -1;//标记结束
	        pos[1][count] = -1;
	        count++;        
	    }
	    public void mouseEntered(MouseEvent me){}
	    public void mouseExited(MouseEvent me){}
	    public void mouseDragged(MouseEvent me)
	    {
	        pos[0][count] = me.getX();
	        pos[1][count] = me.getY();
	        count++;
	        repaint();
	    }
	    public void mouseMoved(MouseEvent me){}
    }
   
    
    public static void main(String args[]) 
	{
		new Ch11DigitRecog();
	}
	
	public void setMenu() 	  
	{    
	    JMenuBar Bar = new JMenuBar();
        JMenu file  = new JMenu("文件");
        Bar.add(file);
        setJMenuBar(Bar);
        
        load = new JMenuItem("载入样本");
        load.addActionListener(this);
        file.add(load);
                
        news = new JMenuItem("新建样本");
        news.addActionListener(this);
        file.add(news);
                
        save = new JMenuItem("保存样本");
        save.addActionListener(this);
        file.add(save);
        file.addSeparator();
        
        renew = new JMenuItem("更改算法");
        renew.addActionListener(this);
        file.add(renew);
        file.addSeparator();
              
        quit = new JMenuItem("退出");
        quit.addActionListener(this);
        file.add(quit);
        
        can = new myCanvas();
	    Container con = getContentPane();
	    con.setLayout(null);
	    
	    JPanel canpan = new JPanel();
	    canpan.setLayout(null);
	    canpan.add(can);
	    can.setBounds(20, 30, 160, 200);
	    canpan.setBorder(BorderFactory.createTitledBorder("手写数字输入板"));
	    canpan.setBounds(150, 20, 200, 250);
	    con.add(canpan);
	    
	    //识别面板
	    JPanel recogpan = new JPanel();
        con.add(recogpan);
        recogpan.setLayout(null);
        recogpan.setBounds(10, 20, 130, 120);
        recogpan.setBorder(BorderFactory.createTitledBorder("识别显示"));
            	    
	    result = new JTextArea();           
	    result.setEditable(false);
	    result.setBounds(10, 25, 110, 25);
	    recogpan.add(result);
	  	
	   	String[] s = {"模板匹配算法","Bayes分类器","LMSE","奖惩算法"};
	    alg = new JComboBox(s);
        alg.setBounds(10, 55, 110, 25);
        alg.setEnabled(true);
        alg.addActionListener(this);  
        recogpan.add(alg); 
	  	  	    
	    recognize = new JButton("识 别");
	    recognize.setBounds(10, 85, 110, 25);
	    recognize.setEnabled(false);
        recognize.addActionListener(this);
	    recogpan.add(recognize);
	    	    
	    //训练面板
	    JPanel trainpan = new JPanel();
        con.add(trainpan);
        trainpan.setLayout(null);
        trainpan.setBounds(10, 180, 130, 90);
        trainpan.setBorder(BorderFactory.createTitledBorder("训练数字"));
            	    
	    String[] st = {"0","1","2","3","4","5","6","7","8","9"};
	    num = new JComboBox(st);
        num.setBounds(10, 25, 110, 25);
        num.setEnabled(false);
        num.addActionListener(this);  
        trainpan.add(num);  
	    
	    train = new JButton("训 练");
	    train.setBounds(10, 55, 110, 25);
	    train.setEnabled(false);
        train.addActionListener(this);
	    trainpan.add(train);
	            
        clear = new JButton("清 除");
	    clear.setBounds(10, 148, 130, 25);
	    clear.setEnabled(false);
        clear.addActionListener(this);         
	    con.add(clear);
	    	   
	    JPanel inforpan = new JPanel();          
        inforpan.setBounds(10, 280, 340, 80);
        inforpan.setBorder(BorderFactory.createTitledBorder("信息提示"));
        inform = new JTextArea("先选择识别算法, 然后打开菜单"
               + "[文件]/[载入样本]或[新建样本]", 2, 28);
        inforpan.add(inform);
        con.add(inforpan);
    }
    
    JButton recognize;
    JButton train;
    JButton clear;
      
    JComboBox alg, num;
    myCanvas can;
    
    JMenuItem load;    
    JMenuItem news;  
    JMenuItem save;    
    JMenuItem renew;
    JMenuItem quit;
    
    JTextArea result, inform;	
}  	