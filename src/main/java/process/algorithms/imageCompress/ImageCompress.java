/**
 * @ImageComprss.java
 * @Version 1.0 2010.02.23
 * @Author Xie-Hua Sun 
 */

package process.algorithms.imageCompress;

import java.io.*;
import java.awt.*;
import process.algorithms.imageTrans.DCT2;

public class ImageCompress
{	
	int numColors;            //��ɫ��Ŀ
    byte[] search,            //����������
           colors;            //��ɫ������
    
    //����ɫ��ѹ��-----------------------
            
	//����ͼ���colornum, �õ�ͼ�����ɫ��colors[]��������search[]
    public void ToIndexedColor(byte b[][], int iw, int ih) throws AWTException 
    {
		search = new byte[iw * ih];//���������ݽ�����ѹ��
		byte[] temclr = new byte[256];    //��ʱcolor��
		int colornum = 0;
		 
		for (int y = 0; y < ih; y++)
		{
		    for (int x = 0; x < iw; x++)
		    {   
		        int num;
			    for (num = 0; num < colornum; num++)
			        if (temclr[num] == b[x][y])
				        break;
			
			    if (num > 255)
			        throw new AWTException("Too many colors.");
	            
				search[x + y*iw] = (byte)num;
				
				if (num == colornum) {
				    temclr[num] = b[x][y];				    
				    colornum++;
				}				
		    }
		}
		
		//������ɫ��Ŀ
		numColors = 1 << BitsNeeded(colornum);
		/*---------------------------------------------------
		 *ͨ����temclr[256]��colors[numColors], 
		 *��numColors <256ʱ����С����ɫ��ʹ�õĿռ�		  
		 ----------------------------------------------------*/			
		colors = new byte[numColors];//��numColors����ɫ����������ڴ�		
		colors = temclr;		 
    }
    
    public byte BitsNeeded(int n) 
    {
	    byte ret = 1;
	
	    if (n-- == 0)
	        return 0;
	
	    while ((n >>= 1) != 0)
	        ++ret;
		
	    return ret;
    }
    
    //ѹ��
    public void compress(int iw, int ih)
    {
    	try
	    {
			RandomAccessFile rf = new RandomAccessFile(
				                  "./images/compressed/clock.cmp","rw");
			rf.writeByte(colors[0]);
			rf.writeByte(colors[1]);
			for(int i = 0; i < iw*ih; i = i+8)
			{
				byte c = 0;
			    for(int k = 0; k < 8; k++)
			        c = (byte)(c | search[i+k]<<k);
			    rf.writeByte(c); 	
		    }
		    rf.close();				
		}			
	    catch(FileNotFoundException e1)
	    {
			e1.printStackTrace();

			System.err.println(e1);
	    }
	    catch(IOException e2)
	    {
			e2.printStackTrace();

			System.err.println(e2);
	    }
    }
    
    //��ѹ��
    public int[] uncompress(String name, int iw, int ih)
    {    	
    	int[] pix = new int[ih*iw]; //ARGBͼ������
    	
    	int[] c = new int[2];       //��ɫ��
    	int r;
    	
    	try
	  	{
	  	    FileInputStream fin = new FileInputStream(name);			  	    
	  	    DataInputStream  in = new DataInputStream(fin);
	  	    
	  	    //ȡ����ͷ2�ֽ�, �����ɫ��
	  	    byte b0 = in.readByte();
	  	    
	  	    //���ֽ�ת��Ϊ�Ҷ�(0-255)��ʾ
	  	    c[0] = b0;
	  	    if(b0 < 0) c[0] = b0 + 256;
	  	    
	  	    byte b1 = in.readByte();
	  	    c[1] = b1;
	  	    if(b1 < 0) c[1] = b1 + 256;
	  	    
	  	    //ȡ��ѹ�����ݣ���ת����ͼ������
            for(int i = 0; i < iw*ih; i = i+8)
            {                     
               byte b = in.readByte();
               
               for(int k = 0; k < 8; k++)
               {
               	   r = c[b&0x01];  //ȡ���ֽ����λ���ݣ���ת���ɻҶȱ�ʾ               
                   pix[i+k] = (255<<24)|(r<<16)|(r<<8)|r; //����ARGB
                   b >>= 1;               	  
               }               
            }    	  	   
	  	}
	  	catch(IOException e1){System.out.println("Exception!" + e1.getStackTrace());e1.printStackTrace();}
	  	
	  	return pix; 
    }
    
    //Huffman---------------------------------------
    
    public void huffCode(int[] pixels, int iw, int ih)
	{
		//����ͼ��ĻҶ�
		int grey[] = new int [iw*ih];
		
		//���ͼ��ĻҶ�ֵ
		for(int i = 0; i < iw*ih; i++)
			grey[i] = pixels[i] & 0xff;
							
		//��ͼ�����Huffman����
		Huffman huffman = new Huffman(grey, iw, ih);
					
		//h.test();
		huffman.huff();
		
		//ͼ����
		float entropy = huffman.getEntropy();
		
		//ƽ���볤
		float avgCode = huffman.getAvgCode();
		
		//����Ч��
		float efficiency = huffman.getEfficiency();
		
		//����Ƶ��
		float freq[] = new float[256];
		
		//Huffman����
		String sCode[] = new String[256];
		
		//����Ƶ��
		freq = huffman.getFreq();
		
		//Huffman����
		sCode = huffman.getCode();
						
		//����HuffmanShow.java������ʾ
		HuffmanShow hs = new HuffmanShow(entropy, avgCode, efficiency);
		hs.setData(freq, sCode);
		hs.showTable();
		hs.setVisible(true);		
    }
    
    //RLE�㷨---------------------------------------
    
    //ѹ��������RLE8�㷨��2�ֽ�:[�ظ���][�ظ��ַ�],��
    //��ʶ�� 0x00 0x00 ��ʾ����
    //       0x00 0xNN �Ժ���NN����ͬ�ַ�
    public void rleCompress(int iw, int ih)
    {
    	try
	    {
			RandomAccessFile rf = new RandomAccessFile("./images/"
			                    + "compressed/rleTest.rle","rw");
			
			//��д����ɫ��, û��ѹ��
			for(int i = 0; i < colors.length; i++)
			    rf.writeByte(colors[i]);
			
			//RLE8ѹ��						
	        int v = 0, j;			
	        int num = iw*ih;
	        int numBytes = 0;
	        while(v < num-1)
			{
				int m = v;                    //������ʼv
				byte b1 = search[v++];
				byte b2 = search[v];				
				
				if(b1 == b2)
				{
				    //�����ַ�b���ظ���Ŀ
				    for(j = v+1; (j<num)&&(b1==search[j])&&(j-v<254); j++);
				    
				    int x = j - v + 1;       //����b���ظ���Ŀ, 1 <= x < 256	    
				   
				    //������д��ѹ���ļ�
				    rf.writeByte((byte)x);   //ǰһ�ֽڱ�ʾ"�ظ���Ŀ"
				    numBytes++;
				    rf.writeByte(b1);        //��һ�ֽڱ�ʾ"�ظ��ַ�"
				    numBytes++;
				    v = j;			
				}	
				else
				{
					for(j  = v+1; (j<num)&&(search[j-1]!=search[j])
					                     &&(j-v<255); j++);
										
					int x = j - v;			
					
					//д��ʶ���룬2�ֽ�
					rf.writeByte(0x00);    numBytes++;              
					rf.writeByte((byte)x); numBytes++;
					for(int i = 0; i < x; i++)        //��ѹ��ֱ��д������
					{
					    rf.writeByte(search[m+i]);
					    numBytes++;
					}					
					v = j-1;					
				}							    	
		    }
		    
		    //д��ʶ����, ��ʾȫ�����ݽ���
		    rf.writeByte(0x00); numBytes++;
		    rf.writeByte(0x00);	numBytes++;	    
		    rf.close();				
		}			
	    catch(FileNotFoundException e1)
	    {
			e1.printStackTrace();

			System.err.println(e1);
	    }
	    catch(IOException e2)
	    {
			e2.printStackTrace();

			System.err.println(e2);
	    }
    }
    
    //��ѹ��
    public int[] rleUncomp(String name, int iw, int ih)
    {    	
    	int   num = ih * iw;
    	int[] pix = new int[num];               //ARGBͼ������
    	    	   	
    	int[] c   = new int[colors.length];     //��ɫ��
    	int index, r;
        	
    	try
	  	{
	  	    FileInputStream fin = new FileInputStream(name);			  	    
	  	    DataInputStream  in = new DataInputStream(fin);
	  	    
	  	    //ȡ����ɫ��
	  	    for(int i = 0; i < colors.length; i++)
	  	    {
	  	        int b = in.readByte();
	  	    
	  	        //���ֽ�ת��Ϊ�Ҷ�(0-255)��ʾ
	  	        if(b < 0) b = b + 256;
	  	        c[i] = b;	  	        
	  	    }  	       
	  	    
	  	    int k = 0;
	  	    boolean wflag = true;             //whileѭ����־
	  	    
	  	    while(wflag)
	  	    {	  	    
	  	        byte b0 = in.readByte();
	  	        byte b1 = in.readByte();	  	        
	  	        
	  	        if(b0 == 0)
	  	        {
	  	        	if(b1 == 0)               //�ļ��Ѷ���,����whileѭ��
	  	        	    wflag = false;
	  	        	else
	  	        	{
	  	        		int n = b1;
	  	        		if(b1 < 0) n = b1 + 256;
	  	        		for(int i = 0; i < n; i++)
	  	        		{
	  	        		    b0 = in.readByte();
	  	        		    index = b0;
	  	        		    if(b0 < 0) index = b0 + 256;
	  	        		    r = c[index];
	  	        		    pix[k++] = (255<<24)|(r<<16)|(r<<8)|r;	  	        		    
	  	        		}
	  	        	}
	  	        }
	  	        else
	  	        {
	  	        	int n = b0;
	  	        	if(b0 < 0) n = b0 + 256;
	  	        	index = b1;
	  	        	if(b1 < 0) index = b1 + 256;
	  	            r = c[index];
	  	        	for(int i = 0; i < n; i++)
	  	        	    pix[k++] = (255<<24)|(r<<16)|(r<<8)|r;   	  	        	    
	  	        } 	  	        	  	    	
	  	    }            	  	   
	  	}
	  	catch(IOException e1){System.out.println("Exception!" + e1.getStackTrace());e1.printStackTrace();}
	  	
	  	return pix; 
    }
    
    //LZW�㷨------------------------------
    
    public void lzwCompress(int iw, int ih)
    {
    	try
	    {
			RandomAccessFile rf = new RandomAccessFile(
				                  "./images/compressed/lzwTest.lzw", "rw");
			
			//��д����ɫ��, û��ѹ��
			for(int i = 0; i < colors.length; i++)
			    rf.writeByte(colors[i]);
									
	        //LZW�㷨
	        compressfile(search, rf, iw, ih);            
	        	    
		    rf.close();				
		}			
	    catch(FileNotFoundException e1)
	    {
			e1.printStackTrace();

			System.err.println(e1);
	    }
	    catch(IOException e2)
	    {
			e2.printStackTrace();

			System.err.println(e2);
	    }
    }
    
    byte[] decode_stack = new byte[5021];
    
    //��ѹ��
    public int[] lzwUncomp(String name, int iw, int ih)
    {    	
    	int   num = ih * iw;
    	int[] pix = new int[num];                  //ARGBͼ������
    	int[] search = new int[num];               
    	int[] c      = new int[colors.length];     //��ɫ��
    	        	
    	try
	  	{
	  	    FileInputStream fin = new FileInputStream(name);			  	    
	  	    DataInputStream  in = new DataInputStream(fin);
	  	    
	  	    //ȡ����ɫ��
	  	    for(int i = 0; i < colors.length; i++)
	  	    {
	  	        byte b = in.readByte();
	  	    
	  	        //���ֽ�ת��Ϊ�Ҷ�(0-255)��ʾ
	  	        c[i] = b;
	  	        if(b < 0) c[i] = b + 256;
	  	    }  	       
	  	    
	  	    //��ѹ��ʼ
	  	    int inew, old;
	  	    int next, count;
			int ch, v = 0;
			next = 257;
			old  = in.readShort();
			
			ch = old;
			search[v++] = old;
			
			while((inew = in.readShort()) != 256)
			{
				if(inew >= next)
				{
					decode_stack[0] = (byte)ch;
					count = decord(1,old);
				}
				else count = decord(0,inew);
				
				ch = decode_stack[count-1];
				while(count>0)
				    search[v++] = decode_stack[--count];
				if(next <= max_code)
				{
					parent[next] = old;
					char1[next]  = (byte)ch;
					next++;
				}
				old=inew;
			}	  	        	  	   
	  	}
	  	catch(IOException e1){System.out.println("Exception!" + e1.getStackTrace());e1.printStackTrace();}
	  	
	  	for(int i = 0; i < iw*ih; i++)
	  	{
	  	    int g  = c[search[i]];
	  	    pix[i] = (255<<24)|(g<<16)|(g<<8)|g; 
	  	}    
	  	
	  	return pix; 
    }
    
    int [] code_value = new int[5021];
	int [] parent     = new int[5021];
	byte[] char1      = new byte[5021];
	long max_code     = ((1<<12)-1);
	
    private void compressfile(byte[] search, RandomAccessFile rf, int iw, int ih)
    {
		int next, string;
		int v, index;
		byte bt;
		next = 257;
		
		for(int i = 0; i < 5021; i++) 
		    code_value[i] = -1;
		
		v = 0;
		string = search[v++];
		try
		{		
			while(v < iw*ih)
			{
				bt = search[v++];
				index = find(string,(int)bt);
				if(code_value[index] != -1)
					string = code_value[index];
				else
				{
					if(next <= max_code)
					{
						code_value[index] = next++;
						parent[index]     = string;
						char1[index]      = bt;
					}
					rf.writeShort(string);
					string = bt;
				}
			}
			rf.writeShort(string);
			rf.writeShort(256);
		}
		catch(FileNotFoundException e1)
	    {
			e1.printStackTrace();

			System.err.println(e1);
	    }
	    catch(IOException e2)
	    {
			e2.printStackTrace();

			System.err.println(e2);
	    }
	}
	
    //find_child_node()
    private int find(int par,int child)
    {    
		int index, offset;
		index = (child<<4)^par;
		if(index == 0) offset = 1;
		else offset = 5021-index;
		for(;;)
		{
			if(code_value[index] == -1) 
			    return(index);			
			if(parent[index] == par&&char1[index] == (byte)child) 
			    return(index);
			index -= offset;
			if(index < 0) 
			    index += 5021;
		}
	}	
	
    private int decord(int count,int code)
    {
		while(code > 255)
		{                              //"stack" decode_string()
			decode_stack[count++] = char1[code];
			code = parent[code];
		}
		decode_stack[count++] = (byte)code;
		return(count);
	}
	
	//DCTѹ��--------------------------------------- 
	int DIM = 256;	
	int in [][],              //����ͼ���ά���� 
	    out[][];              //���ͼ���ά����
	int iter_num;             //��������
	
	    
	double image[][],         //8X8��ͼ������ 
	       coef [][],         //���ұ任ϵ������ 	
	       coeft[][],         //���ұ任ϵ��ת�þ���	         
	       dct_image[][];     //DCT image
	       
	//����������
    int[][] quan  =   {{16,11,10,16, 24, 40, 51, 61},
                       {12,12,14,19, 26, 58, 60, 55},
                       {14,13,16,24, 40, 57, 69, 56},
                       {14,17,22,29, 51, 87, 80, 62},
                       {18,22,37,56, 68,109,103, 77},
                       {24,35,55,64, 81,104,113, 92},
                       {49,64,78,87,103,121,120,101},
                       {72,92,95,98,112,100,103, 99}};
                       
	//"֮"����ɨ��·�߱�          
    int[][] zig_zag = {{ 0, 1, 5, 6,14,15,27,28},
                       { 2, 4, 7,13,16,26,29,42},
                       { 3, 8,12,17,25,30,41,43},
                       { 9,11,18,24,31,40,44,53},
                       {10,19,23,32,39,45,52,54},
                       {20,22,33,38,46,51,55,60},
                       {21,34,37,47,50,56,59,61},
                       {35,36,48,49,57,58,62,63}};
	
	//ѹ��������RLE8�㷨��2�ֽ�:[�ظ���][�ظ��ַ�],��
    //��ʶ�� 0x00 0x00 ��ʾ����
    //       0x00 0xNN �Ժ���NN����ͬ�ַ�
    public void RLE8(byte[] search, int iw, int ih)
    {
    	try
	    {
			RandomAccessFile rf = new RandomAccessFile(
				                  "./images/compressed/test.dct","rw");
			
			//RLE8ѹ��						
	        int v = 0, j;			
	        int num = iw*ih;
	        while(v < num-1)
			{
				int m = v;                   //������ʼv
				byte b1 = search[v++];
				byte b2 = search[v];				
				
				if(b1 == b2)
				{
				    //�����ַ�b���ظ���Ŀ
				    for(j = v+1; (j<num)&&(b1==search[j])&&(j-v<254); j++);
				    
				    int x = j - v + 1;       //����b���ظ���Ŀ, 1 <= x < 256	    
				   
				    //������д��ѹ���ļ�
				    rf.writeByte((byte)x);   //ǰһ�ֽڱ�ʾ"�ظ���Ŀ"
				    rf.writeByte(b1);        //��һ�ֽڱ�ʾ"�ظ��ַ�"
				    v = j;				
				}	
				else
				{
					for(j = v+1; (j<num)&&(search[j-1]!=search[j])&&(j-v<255); j++);
										
					int x = j - v;			
					
					//д��ʶ���룬2�ֽ�
					rf.writeByte(0x00);               
					rf.writeByte((byte)x);
					
					for(int i = 0; i < x; i++)       //��ѹ��ֱ��д������
					    rf.writeByte(search[m+i]);
										
					v = j-1;					
				}							    	
		    }
		    
		    //д��ʶ����, ��ʾȫ�����ݽ���
		    rf.writeByte(0x00);
		    rf.writeByte(0x00);		    
		    rf.close();				
		}			
	    catch(FileNotFoundException e1) {
			e1.printStackTrace();
			System.err.println(e1); }
	    catch(IOException e2) {
			e2.printStackTrace();
			System.err.println(e2); }
    }
    
    public void dctCompress(int[] pixels, int iw, int ih)
	{
		//�����ڴ�
	    in  = new int[DIM][DIM];
	    out = new int[DIM][DIM];
	    dct_image = new double[DIM][DIM];
	    coef  = new double[8][8]; 
	    coeft = new double[8][8]; 
	    image = new double[8][8];
	    
	    DCT2 dct = new DCT2();			
					     		    
	    for(int j = 0; j < ih; j++)
	        for(int i = 0; i < iw; i++)			        
	            in[i][j] = pixels[i+j*iw] & 0xff;			            
	   
	    //����8X8DCT�任����ϵ��
        dct.coeff(coef, 8);
        
        //����8X8DCT�任ת�þ���
        for(int i = 0; i < 8; i++)
            for(int j = 0; j < 8; j++)
	            coeft[i][j] = coef[j][i];
	            
	    iter_num = DIM / 8;    // compute the iteration number
        
        byte[] bimage = new byte[iw*ih];
        
        //ѹ���㷨��ʼ-----------------
        for(int i = 0; i < iter_num; i++)
        {                
			for(int j = 0; j < iter_num; j++)
			{
				//ȡ8X8��
				for(int k = 0; k < 8; k++)
					for(int l = 0; l < 8; l++)
						image[k][l] = in[i*8+k][j*8+l];
	
				//1.��8X8��DCT�任
				dct.dct(image, coeft, coef, 8);
	            
	            //2.����
	            //3.��"֮"���ͣ��ų�һά����bimage[]
	            for(int k = 0; k < 8; k++)
				    for(int l = 0; l < 8; l++)
					    bimage[i*2048+j*64+zig_zag[k][l]] 
					          =(byte)(1.0*image[k][l]/quan[k][l]+0.5);											                   
			}
        }
        
        //4.��bimage[]����RLE����
	    RLE8(bimage, iw, ih);	    
    }
    
    public int[] dctUncomp(int iw, int ih, int block)
    {
    	int iter_num = iw/block;
    	
    	int[] outpix = new int[iw*ih];
    	int[][] out = new int[iw][ih];	    
		    
	    double[][] coef  = new double[8][8]; 
	    double[][] coeft = new double[8][8]; 
	    double[][] image = new double[8][8]; 
	    
	    DCT2 dct = new DCT2();
	    
    	//��ѹ����ʼ----------------------------------
    	//1.RLE��ѹ
    	byte[] temp = readCompFile("./images/compressed/test.dct", iw, ih);    							
		
		//����8X8DCT�任����ϵ��
        dct.coeff(coef, 8);
            
        //����8X8DCT�任ת�þ���
        for(int i = 0; i < 8; i++)
	        for(int j = 0; j < 8; j++)
		        coeft[i][j] = coef[j][i];
		
		for(int i = 0; i < iter_num; i++)
        {                
			for(int j = 0; j < iter_num; j++)
			{	
			    //2.�ų�һά����temp[]��"֮"���ͻ�ԭ��8X8��		
				//3.������
				for(int k = 0; k < 8; k++)
				    for(int l = 0; l < 8; l++)
				        image[k][l] = quan[k][l]*temp[i*2048+j*64+zig_zag[k][l]];
				
				//4.dct��任        
				dct.dct(image, coef, coeft, 8); 
	
				for(int k = 0;k < 8; k++)
					for(int l = 0;l < 8; l++)
						out[i*8+k][j*8+l] = (int)image[k][l];					
			}
        }
    	//��ѹ������--------------------------------------
    	
		for(int j = 0; j < ih; j++)
	    {			    
            for(int i = 0; i < iw; i++)
            {
            	if(out[i][j] > 255) out[i][j] = 255;
            	if(out[i][j] < 0)   out[i][j] = 0;	            
	            outpix[i+j*iw] = out[i][j];	            
	        }
	    } 			    
	    
	    for(int i = 0; i < iw*ih; i++)
	    {
		    int x = outpix[i];			    
		    outpix[i] = 255 << 24|x << 16|x << 8|x;
	    }
	    return outpix;			
    }
    
    //��ѹ��
    private byte[] readCompFile(String name, int iw, int ih)
    {    	
    	byte[] pix = new byte[ih * iw];           
        	
    	try
	  	{
	  	    FileInputStream fin = new FileInputStream(name);			  	    
	  	    DataInputStream  in = new DataInputStream(fin);
	  	    
	  	    int k = 0;
	  	    boolean wflag = true;             //whileѭ����־
	  	    
	  	    while(wflag)
	  	    {	  	    
	  	        byte b0 = in.readByte();
	  	        byte b1 = in.readByte();	  	        
	  	        
	  	        if(b0 == 0)
	  	        {
	  	        	if(b1 == 0)               //�ļ��Ѷ���,����whileѭ��
	  	        	{
	  	        	    wflag = false;
	  	        	}
	  	        	else
	  	        	{
	  	        		int n = b1;
	  	        		if(b1 < 0) n = b1 + 256;
	  	        		for(int i = 0; i < n; i++)
	  	        		    pix[k++] = in.readByte();  	        		
	  	        	}
	  	        }
	  	        else
	  	        {
	  	        	int n = b0;
	  	        	if(b0 < 0) n = b0 + 256;
	  	           	for(int i = 0; i < n; i++)
	  	        	    pix[k++] = b1;        	    
	  	        } 	  	        	  	    	
	  	    }            	  	   
	  	}
	  	catch(IOException e1){System.out.println("Exception!" + e1.getStackTrace());e1.printStackTrace();}
	  	return pix; 
    }      
}