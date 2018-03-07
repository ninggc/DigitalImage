/**
 * @ImageMorph.java
 * @Version 1.0 2010.02.21
 * @Author Xie-Hua Sun 
 * 腐蚀,膨胀,开启和闭合
 * 滤波,边缘提取,区域填充,计算欧拉数,HMT变换
 */

package process.algorithms;

public class ImageMorph
{   
    /*-------------------------------------------------------
     *膨胀
     *im[][]   -- 输入二值图像
     *w, h     -- 输入图像宽高
     *byte[][] -- 输出目标图像
     *type     -- 1表示3X3正方形结构
     *         -- 2表示菱形结构
     *         -- 3表示5X5八角结构
     *-------------------------------------------------------*/  
    public byte[] dilate(byte[] im, int w, int h, int type)
    {
    	int i,j;
    	byte[] out = new byte[w*h];
    	
    	//正方形结构
    	if(type == 21)
    	{
    		for(j = 1; j < h-1; j++)
    		{
    			for(i = 1; i < w-1; i++)
    			{
    				byte s = im[i+j*w];
    				if(s == 1){
    					out[i+j*w] = s;
    					continue;
    				}  
    				if(im[i-1+(j-1)*w]==1){out[i+j*w] = 1; continue;}
    				if(im[i-1+j*w  ]==1){out[i+j*w] = 1; continue;}    				
    				if(im[i-1+(j+1)*w]==1){out[i+j*w] = 1; continue;}
    				if(im[i+(j-1)*w]==1){out[i+j*w] = 1; continue;} 
    				if(im[i+(j+1)*w]==1){out[i+j*w] = 1; continue;}
    				if(im[i+1+(j-1)*w]==1){out[i+j*w] = 1; continue;}
    				if(im[i+1+j*w]==1){out[i+j*w] = 1; continue;}
    				if(im[i+1+(j+1)*w]==1){out[i+j*w] = 1; continue;}  				            
    			}
    		}
    	}
    	//菱形结构
    	if(type == 22)
    	{
    		for(j = 1; j < h-1; j++)
    		{
    			for(i = 1; i < w-1; i++)
    			{
    				byte s = im[i+j*w];
    				if(s == 1){
    					out[i+j*w] = s;
    					continue;
    				} 
    				if(im[i-1+j*w]==1){out[i+j*w] = 1; continue;}
    				if(im[i+1+j*w]==1){out[i+j*w] = 1; continue;}
    				if(im[i+(j-1)*w]==1){out[i+j*w] = 1; continue;}
    				if(im[i+(j+1)*w]==1){out[i+j*w] = 1; continue;}   			  				            
    			}
    		}
    	}
    	//5X5八角结构
    	if(type == 23)
    	{
    		for(j = 2; j < h-2; j++)
    		{
    			for(i = 2; i < w-2; i++)
    			{
    				byte s = im[i+j*w];
    				if(s == 1){
    					out[i+j*w] = s;
    					continue;
    				}
    				if(im[i-2+(j-1)*w]==1) {out[i+j*w] = 1;continue;}
    				if(im[i-2+j*w]==1)     {out[i+j*w] = 1;continue;}
    				if(im[i-2+(j+1)*w]==1) {out[i+j*w] = 1;continue;}
    				
    				if(im[i-1+(j-2)*w]==1) {out[i+j*w] = 1;continue;}
    				if(im[i-1+(j-1)*w]==1) {out[i+j*w] = 1;continue;}
    				if(im[i-1+j*w]==1)     {out[i+j*w] = 1;continue;}
    				if(im[i-1+(j+1)*w]==1) {out[i+j*w] = 1;continue;}
    				if(im[i-1+(j+2)*w]==1) {out[i+j*w] = 1;continue;}
    				
    				if(im[i+(j-2)*w]==1) {out[i+j*w] = 1;continue;}
    				if(im[i+(j-1)*w]==1) {out[i+j*w] = 1;continue;}
    				if(im[i+(j+1)*w]==1) {out[i+j*w] = 1;continue;}
    				if(im[i+(j+2)*w]==1) {out[i+j*w] = 1;continue;}
    				
    				if(im[i+1+(j-2)*w]==1) {out[i+j*w] = 1;continue;}
    				if(im[i+1+(j-1)*w]==1) {out[i+j*w] = 1;continue;}
    				if(im[i+1+j*w]==1)     {out[i+j*w] = 1;continue;}
    				if(im[i+1+(j+1)*w]==1) {out[i+j*w] = 1;continue;}
    				if(im[i+1+(j+2)*w]==1) {out[i+j*w] = 1;continue;}
    				
    				if(im[i+2+(j-1)*w]==1) {out[i+j*w] = 1;continue;}
    				if(im[i+2+j*w]==1)     {out[i+j*w] = 1;continue;}
    				if(im[i+2+(j+1)*w]==1) {out[i+j*w] = 1;continue;}     							            
    			}
    		}
    	}
    	return out;
    }     
	    
    /*-------------------------------------------------------
     *腐蚀
     *im[][]   -- 输入二值图像
     *w, h     -- 输入图像宽高
     *byte[][] -- 输出目标图像
     *type     -- 1表示3X3正方形结构
     *         -- 2表示菱形结构
     *         -- 3表示5X5八角结构
     *         -- 4空心的3X3结构,用于HMT变换
     *-------------------------------------------------------*/   
    
    public byte[] erode(byte[] im, int w, int h, int type)
    {
    	int i,j;
    	byte[] out = new byte[w*h];
        	
    	//正方形结构
    	if(type == 11)
    	{
    		for(j = 1; j < h-1; j++)
    		{
    			for(i = 1; i < w-1; i++)
    			{
    				byte s = im[i+j*w];
    				if(s == 0)	continue;
    							
    				if(im[i-1+(j-1)*w] == 0) continue;
    				if(im[i-1+j*w] == 0) continue;
    				if(im[i-1+(j+1)*w] == 0) continue;
    				if(im[i+(j-1)*w] == 0) continue;
    				if(im[i-1+(j+1)*w] == 0) continue;
    				if(im[i+(j-1)*w] == 0) continue;
    				if(im[i+1+j*w] == 0) continue;
    				if(im[i+1+(j+1)*w] == 0) continue;
    				out[i+j*w] = 1;    				            
    			}
    		}
    	}
    	//菱形结构
    	else if(type == 12)
    	{
    		for(j = 1; j < h-1; j++)
    		{
    			for(i = 1; i < w-1; i++)
    			{
    				byte s = im[i+j*w];
    				if(s == 0)    			
    					continue;
    				
    				if(im[i-1+j*w]==0)
    				     continue;
    				if(im[i+1+j*w]==0)
    				     continue;
    				if(im[i+(j-1)*w]==0)
    				     continue;
    				if(im[i+(j+1)*w]==0)
    				     continue;    
    				out[i+j*w] = 1;    				            
    			}
    		}
    	}
    	//5X5八角结构
    	else if(type == 13)
    	{
    		for(i = 2; i < h-2; i++)
    		{
    			for(j = 2; j < w-2; j++)
    			{
    				byte s = im[i+j*w];
    				if(s == 0)	continue;
    							
    				if(im[i-2+(j-1)*w]==0) continue;
    				if(im[i-2+j*w]==0)     continue;
    				if(im[i-2+(j+1)*w]==0) continue;
    				
    				if(im[i-1+(j-2)*w]==0) continue;
    				if(im[i-1+(j-1)*w]==0) continue;
    				if(im[i-1+j*w]==0)     continue;
    				if(im[i-1+(j+1)*w]==0) continue;
    				if(im[i-1+(j+2)*w]==0) continue;
    				
    				if(im[i+(j-2)*w]==0) continue;
    				if(im[i+(j-1)*w]==0) continue;
    				if(im[i+(j+1)*w]==0) continue;
    				if(im[i+(j+2)*w]==0) continue;
    				
    				if(im[i+1+(j-2)*w]==0) continue;
    				if(im[i+1+(j-1)*w]==0) continue;
    				if(im[i+1+j*w  ]==0)   continue;
    				if(im[i+1+(j+1)*w]==0) continue;
    				if(im[i+1+(j+2)*w]==0) continue;
    				
    				if(im[i+2+(j-1)*w]==0) continue;
    				if(im[i+2+j*w]==0)     continue;
    				if(im[i+2+(j+1)*w]==0) continue;
    				
    				out[i+j*w] = 1;    				            
    			}
    		}
    	}
    	/*-----------------------------------------*
    	 * type = 15: 15X15 1-矩阵
    	 *-----------------------------------------*/
    	else if(type == 15)
    	{
    		for(j = 7; j < h-7; j++)
    		{
    			for(i = 7; i < w-7; i++)
    			{
    				boolean bf = true;
    				if(im[i+j*w] == 0) continue;				
    				for(int k = -7; k < 8; k++)
    				    for(int l = -7; l < 8; l++)
    				        if(im[i+k+(j+l)*w] == 0) {bf = false; break;}
    				if(bf) out[i+j*w] = 1;    				            
    			}
    		}
    	}
    	/*-----------------------------------------------*
    	 * type = 19: "回"字型
    	 * 19X19-15X15 1-矩阵(中心15X15 0-矩阵)
    	 *-----------------------------------------------*/
    	else if(type == 19)
    	{
    		for(j = 0; j < h; j++)
    		{
    			for(i = 0; i < w; i++)
    			{
    			 	boolean bf = true;
    				for(int k = -9; k < -7; k++)//"回"字型上部2行
    				    for(int l = -9; l < 10; l++)
    				        if(i+k<0||i+k>=h||j+l<0||j+l>=w) out[i+j*w] = 1;
    				        else if(im[i+k+(j+l)*w] == 0) {bf = false; break;}
    				        
    				for(int k = 8; k < 10; k++)//"回"字型下部2行
    				    for(int l = -9; l < 10; l++)
    				        if(i+k<0||i+k>=h||j+l<0||j+l>=w) out[i+j*w] = 1;
    				        else if(im[i+k+(j+l)*w] == 0) {bf = false; break;}
    				        
    				for(int k = -7; k < 8; k++)//"回"字型左边2列
    				    for(int l = -9; l < -7; l++)
    				        if(i+k<0||i+k>=h||j+l<0||j+l>=w) out[i+j*w] = 1;
    				        else  if(im[i+k+(j+l)*w] == 0) {bf = false; break;} 
    				         
    				for(int k = -7; k < 8; k++)//"回"字型右边2列
    				    for(int l = 8; l < 10; l++)
    				        if(i+k<0||i+k>=h||j+l<0||j+l>=w) out[i+j*w] = 1;
    				        else if(im[i+k+(j+l)*w] == 0) {bf = false; break;}                
    				if(bf) out[i+j*w] = 1;    				            
    			}
    		}
    	}
    	return out;
    }
    
    /*-------------------------------------------------------
     *边界提取
     *im[][]   -- 输入二值图像
     *byte[][] -- 输出目标图像
     *w, h     -- 图像宽高
     *type     -- 1表示3X3正方形结构
     *         -- 2表示菱形结构
     *         -- 3表示5X5八角结构
     *-------------------------------------------------------*/   
    public byte[] edge(byte[] im, int w, int h, int type)
    {
    	byte[] out = erode(im, w, h, type);
        for(int i = 0; i < h; i++)
    		for(int j = 0; j < w; j++)
    		    out[i+j*w] = (byte)(im[i+j*w] - out[i+j*w]);
    	return out;    	
    }
    
    /*-------------------------------------------------------*
     *种子填充
     *img[][]  -- 输入二值图像
     *byte[][] -- 输出目标图像
     *ix, iy   -- 种子点坐标, 由鼠标点击选择
     *-------------------------------------------------------*/
    boolean fillflag = false;
    
    public byte[] fill(byte[] img, int w, int h, int ix, int iy)
    {
    	    	
    	byte[] out = new byte[w*h];
    	out[ix+iy*w] = 1;              //种子点  
    	
    	do
    	{
    	    fillflag = false;
    	    out = dilate(out, w, h, (byte)1, (byte)1, img, (byte)1);        
        }while(fillflag);
        
        //out+img
        for(int j = 0; j < h; j++)
            for(int i = 0; i < w; i++)            
                if(out[i+j*w] == 1) continue;
                else if(img[i+j*w] == 1) out[i+j*w] = 1;
    	return out;
    }
    
    /*-------------------------------------------------------*
	 *具有条件限制的膨胀
	 *Algorithm and Program Author Xie-Hua Sun
	 *im[][]   -- 输入膨胀源图像
	 *img[][]  -- 膨胀限制图像
	 *byte[][] -- 输出目标图像
	 *byte n_i -- 膨胀前,代表像素的代码,比如0
	 *byte n_o -- 膨胀后,代表像素的代码,比如1
	 *byte n_r -- 代表限制图像像素的代码
	 *--------------------------------------------------------*/
	public byte[] dilate(byte[] im, int w, int h, byte n_i, byte n_o, byte[] img, byte n_r)
	{
		byte[] out = new byte[w*h];//输出图像
		
		//菱形结构
		for(int j = 1; j < h-1; j++)
		{
			for(int i = 1; i < w-1; i++)
			{				
				if(im[i+j*w] == n_i){
					out[i+j*w] = n_o;
					continue;
				}
						                    
				if(img[i+j*w] == n_r)//设置限制条件
					continue;
				if(im[i-1+j*w] == n_i)  {out[i+j*w] = n_o; fillflag = true; continue;}
				if(im[i+1+j*w] == n_i)  {out[i+j*w] = n_o; fillflag = true; continue;}
				if(im[i+(j-1)*w] == n_i){out[i+j*w] = n_o; fillflag = true; continue;}
				if(im[i+(j+1)*w] == n_i){out[i+j*w] = n_o; fillflag = true; continue;} 				 			  				            
			}
		}    	    	
		return out;
	} 
	
	/*----------------------------------------------*
	 *计算区域欧拉数
	 *Algorithm and Program Author Xie-Hua Sun
	 *img[][] -- 输入二值图像
	 *int     -- 输出欧拉数
	 *----------------------------------------------*/
	int areaN;                 //区域数目
	byte[] outIm2;
	boolean fillon    = true; 
             
	public int euler(byte[] img, int w, int h)
    {
    	outIm2 = areas(img, w, h);
	    int tem = areaN;
	    areas(outIm2, w, h);
	    return (tem - areaN);   	
    } 
    
	/*----------------------------------------------*
	 *计算区域数numC, 
	 *Algorithm and Program Author Xie-Hua Sun
	 *img[][] -- 输入二值图像
	 *byte[][]-- 输出区域数和由原图像孔洞构成的新区域　
	 *           代表孔洞像素点的值已转变成1,
	 *           孔洞外部像素点的值已转变成0
	 *----------------------------------------------*/ 
    public byte[] areas(byte[] img, int w, int h)
    {
    	fillon = true;
    	int num = 0;        //连通区域数目
   
    	byte[] out = new byte[w*h];
    	    	   	
    	//1.边界检测
    	out = edge(img, w, h, 12);//菱形结构
    	
    	//2.标记边界像素点为-1
    	for(int j = 0;j < h; j++)
    	    for(int i = 0; i < w; i++)            
                if(out[i+j*w]==1) img[i+j*w] = -1;
        /*-------------------------------------
         *现在区域img[] 内部 --  1
         *        　　　　外部 --  0
         *            　　边界 -- -1
         *-------------------------------------*/
    	
        for(int j = 0; j < h; j++)
            for(int i = 0; i < w; i++)            
                out[i+j*w] = 0;
        
        out[2+2*w] = 2;
        
    	//3.用2标记区域外部点
    	do
    	{
    	    fillflag = false;
    	    out = dilate(out, w, h, (byte)2, (byte)2, img, (byte)-1);
    	}while(fillflag);
    	
    	//4.除去外轮廓边界,即将外轮廓边界和区域外部全部标记-1
       	for(int j = 0; j < h; j++)
            for(int i = 0; i < w; i++)
                if(out[i+j*w] == 2) img[i+j*w] = -1;
        
        boolean findflag = false;
        
        //5.搜索区域内部种子点
        for(int j = 0; j < h; j++)
            for(int i = 0; i < w; i++)
                out[i+j*w] = 0;
        for(int j = 0; j < h; j++)
            for(int i = 0; i < w; i++)
                if(img[i+j*w] == 1)
                {
                	out[i+j*w] = 1;//种子点
                	i = w; j = h; //结束循环
                	findflag = true;
                }
        
        //6.计算num
        do
        {         
	        do
	    	{
	    	    fillflag = false;
	    	    out = dilate(out, w, h, (byte)1, (byte)1, img, (byte)-1);
	    	    if(fillflag&&fillon)
	    	    {
	    	    	fillon = false;
	    	    	num++;
	    	    }
	    	}while(fillflag);
	    	
	    	//清除已标记区域
	    	for(int j = 0; j < h; j++)
                for(int i = 0; i < w; i++)
	                if(out[i+j*w] == 1) img[i+j*w] = -1;
	        
	        findflag = false;
        
	        //找一区域内部种子点
	        for(int j = 0; j < h; j++)
                for(int i = 0; i < w; i++)
	                out[i+j*w] = 0;
	        for(int j = 0; j < h; j++)
                for(int i = 0; i < w; i++)
	                if(img[i+j*w] == 1)
	                {
	                	out[i+j*w] = 1;//种子点
	                	i = w; j = h; //结束循环
	                	findflag = true;
	                	fillon   = true;
	                }
       
        }while(findflag);        
        
        for(int j = 0; j < h; j++)
            for(int i = 0; i < w; i++)
                if(img[i+j*w] == -1) out[i+j*w] = 0;
                else if(img[i+j*w] == 0) out[i+j*w] = 1; 
        /*-------------------------------*
         *现在区域img[][]  外部 -- -1
         *                 孔洞 --  0
         *-------------------------------*/      
        areaN = num;             //区域数目
              
    	return out;
    }
    
    /*------------------------------------------------------*
     * HMT变换
     * Algorithm and Program Author Xie-Hua Sun
     * img[][]  -- 输入二值图像
	 * byte[][] -- 输出HMT变换结果
	 * 结构元素E-- 15X15 1-矩阵
	 * 结构元素F-- (19X19 1-矩阵)-(15X15 1-矩阵)"回"字型矩阵
     *------------------------------------------------------*/
    public byte[] hmt(byte[] img, int w, int h)
    {
    	int ix = 10, iy = 10;
    	byte[] out = new byte[w*h];
    	
    	//构造img[]的余集imgc[]
    	byte[] imgc = new byte[w*w];
    	for(int j = 0; j < h; j++)
    	    for(int i = 0; i < w; i++)    		
    			if(img[i+j*w] == 1) imgc[i+j*w] = 0;
    			else                imgc[i+j*w] = 1;
    	//对img[]用E腐蚀	
       	byte[] out1 = erode(img, w, h, 15);
       	
       	//对imgc[][]用F腐蚀
       	byte[] out2 = erode(imgc, w, h, 19); 
       	
       	//求out1与out2的交集
       	for(int j = 0; j < h-1; j++)
       	{       	
    	    for(int i = 0; i < w-1; i++)
    	    {
    		    if(out1[i+j*w] == 1&&out2[i+j*w] == 1)
    		    {
    		    	out[i+j*w] = 1; 
    		    	ix = i; iy = j;  //交集中心坐标
    		    } 
    		}
    	}
    	
    	for(int i=-1; i<2; i++)
		   for(int j=-1; j<2; j++)
		       out[ix+i+(iy+j)*w] = 2;//将标记为红色   	        
    	return out;  	
    }
    
    /*-------------------------------------------------------
     *灰度膨胀
     *im[][]  -- 输入灰度图像
     *w, h    -- 输入图像宽高
     *int[][] -- 输出图像
     *type    -- 1 不平顶结构
     *        -- 2 平顶结构 
     *-------------------------------------------------------*/  
    public int[] grayDilate(int[] pix, int w, int h, int type)
    {
    	int i, j, k, l, n;
    	int m0 = 0, m1 = 0, m2 = 0;
    	int[][] im  = new int[w][h];
    	int[] out = new int[w*h];
    	
    	for(j = 0; j < h; j++)
		    for(i = 0; i < w; i++)		    
				im[i][j] = pix[i+j*w]&0xff;
  	    
    	if(type == 1)
    	{    	
    	    m0 = 80; m1 = 10; m2 = 50;
    	}
    	else if(type == 2){
    		m0 = 1; m1 = 1; m2 = 1;
    	}
    	int[][] b = {{m2, m1, m2},
    	             {m1, m0, m1},
    	             {m2, m1, m2}}; 
    	    	
		for(j = 0; j < h; j++)
		{
			for(i = 0; i < w; i++)
			{
				int max = 0;
				for(k = -1; k < 2; k++)
				{
				    for(l = -1; l < 2; l++)
				    {
				        if(i-k>=0&&j-l>=0&&i-k<h&&j-l<w)
				        {    				        
				            n = im[i-k][j-l]+b[k+1][l+1];
				            if(n > max) max = n;    				            
				        }    				            	
				    }
				}
				if(max > 255) max = 255;
				out[i+j*w] = 255 << 24|max << 16|max << 8|max;				            
			}
		}    	    	
    	return out;
    }     
	    
    /*-------------------------------------------------------
     *灰度腐蚀
     *pix[]   -- 输入图像序列
     *w, h    -- 输入图像宽高
     *int[] -- 输出目标图像
     *type    -- 1 不平顶结构
     *        -- 2 平顶结构           
     *-------------------------------------------------------*/   
    public int[] grayErode(int[] pix, int w, int h, int type)
    {
    	int i, j, k, l, n;
    	int m0 = 0, m1 = 0, m2 = 0;
    	int[][] im  = new int[w][h];
    	int[] out = new int[w*h];
    	
    	for(j = 0; j < h; j++)
		    for(i = 0; i < w; i++)		    
				im[i][j] = pix[i+j*w]&0xff;
  	    
    	
    	if(type == 1)
    	{    	
    	    m0 = 80; m1 = 10; m2 = 50;
    	}
    	else if(type == 2){
    		m0 = 1; m1 = 1; m2 = 1;
    	}
    	
    	int[][] b = {{m2, m1, m2},
    	             {m1, m0, m1},
    	             {m2, m1, m2}}; 
    	
		for(j = 0; j < h; j++)
		{
			for(i = 0; i < w; i++)
			{
				int min = 255;
				for(k = -1; k < 2; k++)
				{
				    for(l = -1; l < 2; l++)
				    {
				        if(i+k>=0&&j+l>=0&&i+k<h&&j+l<w)
				        {    				        
				            n = im[i+k][j+l]-b[k+1][l+1];
				            if(n < min) min = n;    				            
				        }    				            	
				    }
				}
				if(min < 0) min = 0;
				out[i+j*w] = 255 << 24|min << 16|min << 8|min;				            
			}
		}
		   	    	
    	return out;
    }
            
    //Soble型锐化
    public int[] Sobel(int[] pix, int iw, int ih)
	{	            
        int r;
        //膨胀
		int[] out1 = grayDilate(pix, iw, ih, 2);
		//腐蚀
		int[] out2 = grayErode(pix,  iw, ih, 2);
		
		//Soble型处理		
		for(int i = 0; i < iw*ih; i++)
		{
			r = Math.abs((out1[i]&0xff)-(out2[i]&0xff));	    
		    pix[i] = 255 << 24|r << 16|r << 8|r;
		}
		return pix;
	}
	
	//滤波3
	public int[] grayFilter(int[] pix, int iw, int ih)
	{		
       	int r;
       	int[] out1, out2;
       	            
        //开启
		out1 = grayErode(pix, iw, ih, 2);
		out1 = grayDilate(out1, iw, ih, 2);
		
		//闭合
		out1 = grayDilate(out1, iw, ih, 2);
		out1 = grayErode(out1, iw, ih, 2);
		
		//闭合
		out2 = grayDilate(pix, iw, ih, 2);
		out2 = grayErode(out2, iw, ih, 2);
		            
        //开启
		out2 = grayErode(out2, iw, ih, 2);
		out2 = grayDilate(out2, iw, ih, 2);
		for(int i = 0; i < iw*ih; i++)
		{
			r = (int)(((out1[i]&0xff)+  (out2[i]&0xff))/2);				    
		    pix[i] = 255 << 24|r << 16|r << 8|r;
		}
		return pix;
	}
	
	//高帽变换
	public int[] grayTopHat(int[] pix, int iw, int ih)           
	{
		int r;
		int[] out;
		//开启
		out = grayErode(pix, iw, ih, 2);
		out = grayDilate(out, iw, ih, 2);
		
		//高帽处理
		for(int i = 0; i< iw*ih; i++)
		{		   
	        r = pix[i] - out[i];
	        if(r < 0) r = 0;
	        out[i] = 255 << 24|r << 16|r << 8|r;	    
		}
		return out;
	}
}          