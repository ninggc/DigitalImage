/**
 * @PGM.java
 * @Version 1.0, 2010.02.26
 * @Author Xie-Hua Sun
 */
 
package process.rw;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;     
  
public class PGM
{
	char ch0,   ch1;
	int  width, height;
	int  maxpix;
	
	DataInputStream in;
	
	public void readPGMHeader(String name)
    {
    	try
	  	{
	    	FileInputStream fin = new FileInputStream(name);			  	    
	  	    in  = new DataInputStream(fin);
	  	    ch0 = (char)in.readByte();
	  	    ch1 = (char)in.readByte();
	  	    if(ch0 != 'P' || ch1 != '5'){
	  	        System.out.print("Not a pgm image!"+" [0]="+ch0+", [1]="+ch1);
	  	        System.exit(0);
	  	    }
	  	    in.readByte();                  //���ո�
	  	    char c = (char)in.readByte();
	  	    
	  	    if(c == '#')                    //��ע����
	  	    {
	  	    	do{
	  	    		c = (char)in.readByte();
	  	    	}while((c != '\n') && (c != '\r'));
	  	    	c = (char)in.readByte();
	  	    }
	  	    
	  	    //�������
	  	    if(c < '0' || c > '9'){
	  	    	System.out.print("Errow!");
	  	        System.exit(1);
	  	    }
	  	    
	  	    int k = 0;
	  	    do{
	  	    	k = k*10+c-'0';
	  	    	c = (char)in.readByte();
	  	    }while(c >= '0' && c <= '9');
	  	    width = k;
	  	    
	  	    //�����߶�
	  	    c = (char)in.readByte();
	  	    if(c < '0' || c > '9'){
	  	    	System.out.print("Errow!");
	  	        System.exit(1);
	  	    }
	  	    	  	    
	  	    k = 0;
	  	    do{
	  	    	k = k*10+c-'0';
	  	    	c = (char)in.readByte();
	  	    }while(c >= '0' && c <= '9');
	  	    height = k;
	  	    
	  	    //�����Ҷ����ֵ(��δʹ��)
	  	    c = (char)in.readByte();
	  	    if(c < '0' || c > '9'){
	  	    	System.out.print("Errow!");
	  	        System.exit(1);
	  	    }
	  	    	  	    
	  	    k = 0;
	  	    do{
	  	    	k = k*10 + c - '0';
	  	    	c = (char)in.readByte();
	  	    }while(c >= '0' && c <= '9');
	  	    maxpix = k;
  	    }
	  	catch(IOException e1){System.out.println("Exception!");}    	
    }
    
    public void readPPMHeader(String name)
    {
    	try
	  	{
	    	FileInputStream fin = new FileInputStream(name);			  	    
	  	    in  = new DataInputStream(fin);
	  	    ch0 = (char)in.readByte();
	  	    ch1 = (char)in.readByte();
	  	    if(ch0 != 'P' || ch1 != '6'){
	  	        System.out.print("Not a pgm image!"+" [0]="+ch0+", [1]="+ch1);
	  	        System.exit(0);
	  	    }
	  	    in.readByte();                  //���ո�
	  	    
	  	    //�������
	  	    char c = (char)in.readByte();
	  	    if(c < '0' || c > '9'){
	  	    	System.out.print("Errow!");
	  	        System.exit(1);
	  	    }
	  	    
	  	    int k = 0;
	  	    do{
	  	    	k = k*10+c-'0';
	  	    	c = (char)in.readByte();
	  	    }while(c >= '0' && c <= '9');
	  	    width = k;
	  	    
	  	    //�����߶�
	  	    c = (char)in.readByte();
	  	    if(c < '0' || c > '9'){
	  	    	System.out.print("Errow!");
	  	        System.exit(1);
	  	    }
	  	    	  	    
	  	    k = 0;
	  	    do{
	  	    	k = k*10+c-'0';
	  	    	c = (char)in.readByte();
	  	    }while(c >= '0' && c <= '9');
	  	    height = k;
	  	    
	  	    //�����Ҷ����ֵ(��δʹ��)
	  	    c = (char)in.readByte();
	  	    if(c < '0' || c > '9'){
	  	    	System.out.print("Errow!");
	  	        System.exit(1);
	  	    }
	  	    	  	    
	  	    k = 0;
	  	    do{
	  	    	k = k*10 + c - '0';
	  	    	c = (char)in.readByte();
	  	    }while(c >= '0' && c <= '9');
	  	    maxpix = k;
  	    }
	  	catch(IOException e1){System.out.println("Exception!");}    	
    }
    
    /***************************************************************
     * ����.pgm��.ppm�ļ�
     * type 5:pgm, 6:ppm
     ***************************************************************/
    public int[] readData(int iw, int ih, int type)
    {
    	int[] pixels = new int[iw*ih];
    	try
		{
	    	if(type == 5)
	    	{		    	
		  		//����ͼ��Ҷ�����, ������ͼ������
		  		for(int i = 0; i < iw*ih; i++)
		  		{     
	               int b = in.readByte();
	               if(b < 0) b = b + 256;
	               pixels[i] = (255<<24)|(b<<16)|(b<<8)|b;
	            }    
			}
		  	else if(type == 6)
	  		{  				  		
		  		for(int i = 0; i < iw*ih; i++)
		  		{     
	               int r = in.readByte();
	               if(r < 0) r = r + 256;
	               int g = in.readByte();
	               if(g < 0) g = g + 256;
	               int b = in.readByte();
	               if(b < 0) b = b + 256;
	               pixels[i] = (255<<24)|(r<<16)|(g<<8)|b;
	            }
	        } 
        }
		catch(IOException e1){System.out.println("Exception!");} 
	  	
		return pixels; 
    }
     
    public char getCh0()
    {
    	return ch0;
    } 
    
    public char getCh1()
    {
    	return ch1;
    }
          
    public int getWidth()
    {
    	return width;
    }    
    
    public int getHeight()
    {
    	return height;
    }
    
    public int getMaxpix()
    {
    	return maxpix;
    }
}