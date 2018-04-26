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
	int numColors;            //颜色数目
    byte[] search,            //搜索表数组
           colors;            //颜色表数组
    
    //用颜色表压缩-----------------------
            
	//计算图像的colornum, 得到图像的颜色表colors[]和搜索表search[]
    public void ToIndexedColor(byte b[][], int iw, int ih) throws AWTException 
    {
		search = new byte[iw * ih];//搜索表数据将进行压缩
		byte[] temclr = new byte[256];    //临时color表
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
		
		//计算颜色数目
		numColors = 1 << BitsNeeded(colornum);
		/*---------------------------------------------------
		 *通过从temclr[256]到colors[numColors], 
		 *当numColors <256时，缩小了颜色表使用的空间		  
		 ----------------------------------------------------*/			
		colors = new byte[numColors];//用numColors给颜色表数组分配内存		
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
    
    //压缩
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
    
    //解压缩
    public int[] uncompress(String name, int iw, int ih)
    {    	
    	int[] pix = new int[ih*iw]; //ARGB图像序列
    	
    	int[] c = new int[2];       //颜色表
    	int r;
    	
    	try
	  	{
	  	    FileInputStream fin = new FileInputStream(name);			  	    
	  	    DataInputStream  in = new DataInputStream(fin);
	  	    
	  	    //取出开头2字节, 组成颜色表
	  	    byte b0 = in.readByte();
	  	    
	  	    //将字节转换为灰度(0-255)表示
	  	    c[0] = b0;
	  	    if(b0 < 0) c[0] = b0 + 256;
	  	    
	  	    byte b1 = in.readByte();
	  	    c[1] = b1;
	  	    if(b1 < 0) c[1] = b1 + 256;
	  	    
	  	    //取出压缩数据，并转换成图像序列
            for(int i = 0; i < iw*ih; i = i+8)
            {                     
               byte b = in.readByte();
               
               for(int k = 0; k < 8; k++)
               {
               	   r = c[b&0x01];  //取出字节最低位数据，并转换成灰度表示               
                   pix[i+k] = (255<<24)|(r<<16)|(r<<8)|r; //加入ARGB
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
		//保存图像的灰度
		int grey[] = new int [iw*ih];
		
		//获得图像的灰度值
		for(int i = 0; i < iw*ih; i++)
			grey[i] = pixels[i] & 0xff;
							
		//对图像进行Huffman编码
		Huffman huffman = new Huffman(grey, iw, ih);
					
		//h.test();
		huffman.huff();
		
		//图像熵
		float entropy = huffman.getEntropy();
		
		//平均码长
		float avgCode = huffman.getAvgCode();
		
		//编码效率
		float efficiency = huffman.getEfficiency();
		
		//出现频率
		float freq[] = new float[256];
		
		//Huffman编码
		String sCode[] = new String[256];
		
		//出现频率
		freq = huffman.getFreq();
		
		//Huffman编码
		sCode = huffman.getCode();
						
		//调用HuffmanShow.java进行显示
		HuffmanShow hs = new HuffmanShow(entropy, avgCode, efficiency);
		hs.setData(freq, sCode);
		hs.showTable();
		hs.setVisible(true);		
    }
    
    //RLE算法---------------------------------------
    
    //压缩，采用RLE8算法，2字节:[重复数][重复字符],用
    //标识码 0x00 0x00 表示结束
    //       0x00 0xNN 以后有NN个不同字符
    public void rleCompress(int iw, int ih)
    {
    	try
	    {
			RandomAccessFile rf = new RandomAccessFile("./images/"
			                    + "compressed/rleTest.rle","rw");
			
			//先写入颜色表, 没有压缩
			for(int i = 0; i < colors.length; i++)
			    rf.writeByte(colors[i]);
			
			//RLE8压缩						
	        int v = 0, j;			
	        int num = iw*ih;
	        int numBytes = 0;
	        while(v < num-1)
			{
				int m = v;                    //保存起始v
				byte b1 = search[v++];
				byte b2 = search[v];				
				
				if(b1 == b2)
				{
				    //计算字符b的重复数目
				    for(j = v+1; (j<num)&&(b1==search[j])&&(j-v<254); j++);
				    
				    int x = j - v + 1;       //数据b的重复数目, 1 <= x < 256	    
				   
				    //将数据写入压缩文件
				    rf.writeByte((byte)x);   //前一字节表示"重复数目"
				    numBytes++;
				    rf.writeByte(b1);        //后一字节表示"重复字符"
				    numBytes++;
				    v = j;			
				}	
				else
				{
					for(j  = v+1; (j<num)&&(search[j-1]!=search[j])
					                     &&(j-v<255); j++);
										
					int x = j - v;			
					
					//写入识别码，2字节
					rf.writeByte(0x00);    numBytes++;              
					rf.writeByte((byte)x); numBytes++;
					for(int i = 0; i < x; i++)        //不压缩直接写入数据
					{
					    rf.writeByte(search[m+i]);
					    numBytes++;
					}					
					v = j-1;					
				}							    	
		    }
		    
		    //写入识别码, 表示全部数据结束
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
    
    //解压缩
    public int[] rleUncomp(String name, int iw, int ih)
    {    	
    	int   num = ih * iw;
    	int[] pix = new int[num];               //ARGB图像序列
    	    	   	
    	int[] c   = new int[colors.length];     //颜色表
    	int index, r;
        	
    	try
	  	{
	  	    FileInputStream fin = new FileInputStream(name);			  	    
	  	    DataInputStream  in = new DataInputStream(fin);
	  	    
	  	    //取出颜色表
	  	    for(int i = 0; i < colors.length; i++)
	  	    {
	  	        int b = in.readByte();
	  	    
	  	        //将字节转换为灰度(0-255)表示
	  	        if(b < 0) b = b + 256;
	  	        c[i] = b;	  	        
	  	    }  	       
	  	    
	  	    int k = 0;
	  	    boolean wflag = true;             //while循环标志
	  	    
	  	    while(wflag)
	  	    {	  	    
	  	        byte b0 = in.readByte();
	  	        byte b1 = in.readByte();	  	        
	  	        
	  	        if(b0 == 0)
	  	        {
	  	        	if(b1 == 0)               //文件已读完,结束while循环
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
    
    //LZW算法------------------------------
    
    public void lzwCompress(int iw, int ih)
    {
    	try
	    {
			RandomAccessFile rf = new RandomAccessFile(
				                  "./images/compressed/lzwTest.lzw", "rw");
			
			//先写入颜色表, 没有压缩
			for(int i = 0; i < colors.length; i++)
			    rf.writeByte(colors[i]);
									
	        //LZW算法
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
    
    //解压缩
    public int[] lzwUncomp(String name, int iw, int ih)
    {    	
    	int   num = ih * iw;
    	int[] pix = new int[num];                  //ARGB图像序列
    	int[] search = new int[num];               
    	int[] c      = new int[colors.length];     //颜色表
    	        	
    	try
	  	{
	  	    FileInputStream fin = new FileInputStream(name);			  	    
	  	    DataInputStream  in = new DataInputStream(fin);
	  	    
	  	    //取出颜色表
	  	    for(int i = 0; i < colors.length; i++)
	  	    {
	  	        byte b = in.readByte();
	  	    
	  	        //将字节转换为灰度(0-255)表示
	  	        c[i] = b;
	  	        if(b < 0) c[i] = b + 256;
	  	    }  	       
	  	    
	  	    //解压开始
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
	
	//DCT压缩--------------------------------------- 
	int DIM = 256;	
	int in [][],              //输入图像二维数组 
	    out[][];              //输出图像二维数组
	int iter_num;             //迭代次数
	
	    
	double image[][],         //8X8块图像数组 
	       coef [][],         //余弦变换系数矩阵 	
	       coeft[][],         //余弦变换系数转置矩阵	         
	       dct_image[][];     //DCT image
	       
	//亮度量化表
    int[][] quan  =   {{16,11,10,16, 24, 40, 51, 61},
                       {12,12,14,19, 26, 58, 60, 55},
                       {14,13,16,24, 40, 57, 69, 56},
                       {14,17,22,29, 51, 87, 80, 62},
                       {18,22,37,56, 68,109,103, 77},
                       {24,35,55,64, 81,104,113, 92},
                       {49,64,78,87,103,121,120,101},
                       {72,92,95,98,112,100,103, 99}};
                       
	//"之"字型扫描路线表          
    int[][] zig_zag = {{ 0, 1, 5, 6,14,15,27,28},
                       { 2, 4, 7,13,16,26,29,42},
                       { 3, 8,12,17,25,30,41,43},
                       { 9,11,18,24,31,40,44,53},
                       {10,19,23,32,39,45,52,54},
                       {20,22,33,38,46,51,55,60},
                       {21,34,37,47,50,56,59,61},
                       {35,36,48,49,57,58,62,63}};
	
	//压缩，采用RLE8算法，2字节:[重复数][重复字符],用
    //标识码 0x00 0x00 表示结束
    //       0x00 0xNN 以后有NN个不同字符
    public void RLE8(byte[] search, int iw, int ih)
    {
    	try
	    {
			RandomAccessFile rf = new RandomAccessFile(
				                  "./images/compressed/test.dct","rw");
			
			//RLE8压缩						
	        int v = 0, j;			
	        int num = iw*ih;
	        while(v < num-1)
			{
				int m = v;                   //保存起始v
				byte b1 = search[v++];
				byte b2 = search[v];				
				
				if(b1 == b2)
				{
				    //计算字符b的重复数目
				    for(j = v+1; (j<num)&&(b1==search[j])&&(j-v<254); j++);
				    
				    int x = j - v + 1;       //数据b的重复数目, 1 <= x < 256	    
				   
				    //将数据写入压缩文件
				    rf.writeByte((byte)x);   //前一字节表示"重复数目"
				    rf.writeByte(b1);        //后一字节表示"重复字符"
				    v = j;				
				}	
				else
				{
					for(j = v+1; (j<num)&&(search[j-1]!=search[j])&&(j-v<255); j++);
										
					int x = j - v;			
					
					//写入识别码，2字节
					rf.writeByte(0x00);               
					rf.writeByte((byte)x);
					
					for(int i = 0; i < x; i++)       //不压缩直接写入数据
					    rf.writeByte(search[m+i]);
										
					v = j-1;					
				}							    	
		    }
		    
		    //写入识别码, 表示全部数据结束
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
		//分配内存
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
	   
	    //计算8X8DCT变换矩阵系数
        dct.coeff(coef, 8);
        
        //计算8X8DCT变换转置矩阵
        for(int i = 0; i < 8; i++)
            for(int j = 0; j < 8; j++)
	            coeft[i][j] = coef[j][i];
	            
	    iter_num = DIM / 8;    // compute the iteration number
        
        byte[] bimage = new byte[iw*ih];
        
        //压缩算法开始-----------------
        for(int i = 0; i < iter_num; i++)
        {                
			for(int j = 0; j < iter_num; j++)
			{
				//取8X8块
				for(int k = 0; k < 8; k++)
					for(int l = 0; l < 8; l++)
						image[k][l] = in[i*8+k][j*8+l];
	
				//1.对8X8块DCT变换
				dct.dct(image, coeft, coef, 8);
	            
	            //2.量化
	            //3.按"之"字型，排成一维序列bimage[]
	            for(int k = 0; k < 8; k++)
				    for(int l = 0; l < 8; l++)
					    bimage[i*2048+j*64+zig_zag[k][l]] 
					          =(byte)(1.0*image[k][l]/quan[k][l]+0.5);											                   
			}
        }
        
        //4.对bimage[]进行RLE编码
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
	    
    	//解压缩开始----------------------------------
    	//1.RLE解压
    	byte[] temp = readCompFile("./images/compressed/test.dct", iw, ih);    							
		
		//计算8X8DCT变换矩阵系数
        dct.coeff(coef, 8);
            
        //计算8X8DCT变换转置矩阵
        for(int i = 0; i < 8; i++)
	        for(int j = 0; j < 8; j++)
		        coeft[i][j] = coef[j][i];
		
		for(int i = 0; i < iter_num; i++)
        {                
			for(int j = 0; j < iter_num; j++)
			{	
			    //2.排成一维序列temp[]按"之"字型还原成8X8块		
				//3.逆量化
				for(int k = 0; k < 8; k++)
				    for(int l = 0; l < 8; l++)
				        image[k][l] = quan[k][l]*temp[i*2048+j*64+zig_zag[k][l]];
				
				//4.dct逆变换        
				dct.dct(image, coef, coeft, 8); 
	
				for(int k = 0;k < 8; k++)
					for(int l = 0;l < 8; l++)
						out[i*8+k][j*8+l] = (int)image[k][l];					
			}
        }
    	//解压缩结束--------------------------------------
    	
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
    
    //解压缩
    private byte[] readCompFile(String name, int iw, int ih)
    {    	
    	byte[] pix = new byte[ih * iw];           
        	
    	try
	  	{
	  	    FileInputStream fin = new FileInputStream(name);			  	    
	  	    DataInputStream  in = new DataInputStream(fin);
	  	    
	  	    int k = 0;
	  	    boolean wflag = true;             //while循环标志
	  	    
	  	    while(wflag)
	  	    {	  	    
	  	        byte b0 = in.readByte();
	  	        byte b1 = in.readByte();	  	        
	  	        
	  	        if(b0 == 0)
	  	        {
	  	        	if(b1 == 0)               //文件已读完,结束while循环
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