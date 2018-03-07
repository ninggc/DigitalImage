/**
 * @ImageMorph.java
 * @Version 1.0 2010.02.21
 * @Author Xie-Hua Sun 
 * ��ʴ,����,�����ͱպ�
 * �˲�,��Ե��ȡ,�������,����ŷ����,HMT�任
 */

package process.algorithms;

public class ImageMorph
{   
    /*-------------------------------------------------------
     *����
     *im[][]   -- �����ֵͼ��
     *w, h     -- ����ͼ����
     *byte[][] -- ���Ŀ��ͼ��
     *type     -- 1��ʾ3X3�����νṹ
     *         -- 2��ʾ���νṹ
     *         -- 3��ʾ5X5�˽ǽṹ
     *-------------------------------------------------------*/  
    public byte[] dilate(byte[] im, int w, int h, int type)
    {
    	int i,j;
    	byte[] out = new byte[w*h];
    	
    	//�����νṹ
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
    	//���νṹ
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
    	//5X5�˽ǽṹ
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
     *��ʴ
     *im[][]   -- �����ֵͼ��
     *w, h     -- ����ͼ����
     *byte[][] -- ���Ŀ��ͼ��
     *type     -- 1��ʾ3X3�����νṹ
     *         -- 2��ʾ���νṹ
     *         -- 3��ʾ5X5�˽ǽṹ
     *         -- 4���ĵ�3X3�ṹ,����HMT�任
     *-------------------------------------------------------*/   
    
    public byte[] erode(byte[] im, int w, int h, int type)
    {
    	int i,j;
    	byte[] out = new byte[w*h];
        	
    	//�����νṹ
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
    	//���νṹ
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
    	//5X5�˽ǽṹ
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
    	 * type = 15: 15X15 1-����
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
    	 * type = 19: "��"����
    	 * 19X19-15X15 1-����(����15X15 0-����)
    	 *-----------------------------------------------*/
    	else if(type == 19)
    	{
    		for(j = 0; j < h; j++)
    		{
    			for(i = 0; i < w; i++)
    			{
    			 	boolean bf = true;
    				for(int k = -9; k < -7; k++)//"��"�����ϲ�2��
    				    for(int l = -9; l < 10; l++)
    				        if(i+k<0||i+k>=h||j+l<0||j+l>=w) out[i+j*w] = 1;
    				        else if(im[i+k+(j+l)*w] == 0) {bf = false; break;}
    				        
    				for(int k = 8; k < 10; k++)//"��"�����²�2��
    				    for(int l = -9; l < 10; l++)
    				        if(i+k<0||i+k>=h||j+l<0||j+l>=w) out[i+j*w] = 1;
    				        else if(im[i+k+(j+l)*w] == 0) {bf = false; break;}
    				        
    				for(int k = -7; k < 8; k++)//"��"�������2��
    				    for(int l = -9; l < -7; l++)
    				        if(i+k<0||i+k>=h||j+l<0||j+l>=w) out[i+j*w] = 1;
    				        else  if(im[i+k+(j+l)*w] == 0) {bf = false; break;} 
    				         
    				for(int k = -7; k < 8; k++)//"��"�����ұ�2��
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
     *�߽���ȡ
     *im[][]   -- �����ֵͼ��
     *byte[][] -- ���Ŀ��ͼ��
     *w, h     -- ͼ����
     *type     -- 1��ʾ3X3�����νṹ
     *         -- 2��ʾ���νṹ
     *         -- 3��ʾ5X5�˽ǽṹ
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
     *�������
     *img[][]  -- �����ֵͼ��
     *byte[][] -- ���Ŀ��ͼ��
     *ix, iy   -- ���ӵ�����, �������ѡ��
     *-------------------------------------------------------*/
    boolean fillflag = false;
    
    public byte[] fill(byte[] img, int w, int h, int ix, int iy)
    {
    	    	
    	byte[] out = new byte[w*h];
    	out[ix+iy*w] = 1;              //���ӵ�  
    	
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
	 *�����������Ƶ�����
	 *Algorithm and Program Author Xie-Hua Sun
	 *im[][]   -- ��������Դͼ��
	 *img[][]  -- ��������ͼ��
	 *byte[][] -- ���Ŀ��ͼ��
	 *byte n_i -- ����ǰ,�������صĴ���,����0
	 *byte n_o -- ���ͺ�,�������صĴ���,����1
	 *byte n_r -- ��������ͼ�����صĴ���
	 *--------------------------------------------------------*/
	public byte[] dilate(byte[] im, int w, int h, byte n_i, byte n_o, byte[] img, byte n_r)
	{
		byte[] out = new byte[w*h];//���ͼ��
		
		//���νṹ
		for(int j = 1; j < h-1; j++)
		{
			for(int i = 1; i < w-1; i++)
			{				
				if(im[i+j*w] == n_i){
					out[i+j*w] = n_o;
					continue;
				}
						                    
				if(img[i+j*w] == n_r)//������������
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
	 *��������ŷ����
	 *Algorithm and Program Author Xie-Hua Sun
	 *img[][] -- �����ֵͼ��
	 *int     -- ���ŷ����
	 *----------------------------------------------*/
	int areaN;                 //������Ŀ
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
	 *����������numC, 
	 *Algorithm and Program Author Xie-Hua Sun
	 *img[][] -- �����ֵͼ��
	 *byte[][]-- �������������ԭͼ��׶����ɵ�������
	 *           ����׶����ص��ֵ��ת���1,
	 *           �׶��ⲿ���ص��ֵ��ת���0
	 *----------------------------------------------*/ 
    public byte[] areas(byte[] img, int w, int h)
    {
    	fillon = true;
    	int num = 0;        //��ͨ������Ŀ
   
    	byte[] out = new byte[w*h];
    	    	   	
    	//1.�߽���
    	out = edge(img, w, h, 12);//���νṹ
    	
    	//2.��Ǳ߽����ص�Ϊ-1
    	for(int j = 0;j < h; j++)
    	    for(int i = 0; i < w; i++)            
                if(out[i+j*w]==1) img[i+j*w] = -1;
        /*-------------------------------------
         *��������img[] �ڲ� --  1
         *        ���������ⲿ --  0
         *            �����߽� -- -1
         *-------------------------------------*/
    	
        for(int j = 0; j < h; j++)
            for(int i = 0; i < w; i++)            
                out[i+j*w] = 0;
        
        out[2+2*w] = 2;
        
    	//3.��2��������ⲿ��
    	do
    	{
    	    fillflag = false;
    	    out = dilate(out, w, h, (byte)2, (byte)2, img, (byte)-1);
    	}while(fillflag);
    	
    	//4.��ȥ�������߽�,�����������߽�������ⲿȫ�����-1
       	for(int j = 0; j < h; j++)
            for(int i = 0; i < w; i++)
                if(out[i+j*w] == 2) img[i+j*w] = -1;
        
        boolean findflag = false;
        
        //5.���������ڲ����ӵ�
        for(int j = 0; j < h; j++)
            for(int i = 0; i < w; i++)
                out[i+j*w] = 0;
        for(int j = 0; j < h; j++)
            for(int i = 0; i < w; i++)
                if(img[i+j*w] == 1)
                {
                	out[i+j*w] = 1;//���ӵ�
                	i = w; j = h; //����ѭ��
                	findflag = true;
                }
        
        //6.����num
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
	    	
	    	//����ѱ������
	    	for(int j = 0; j < h; j++)
                for(int i = 0; i < w; i++)
	                if(out[i+j*w] == 1) img[i+j*w] = -1;
	        
	        findflag = false;
        
	        //��һ�����ڲ����ӵ�
	        for(int j = 0; j < h; j++)
                for(int i = 0; i < w; i++)
	                out[i+j*w] = 0;
	        for(int j = 0; j < h; j++)
                for(int i = 0; i < w; i++)
	                if(img[i+j*w] == 1)
	                {
	                	out[i+j*w] = 1;//���ӵ�
	                	i = w; j = h; //����ѭ��
	                	findflag = true;
	                	fillon   = true;
	                }
       
        }while(findflag);        
        
        for(int j = 0; j < h; j++)
            for(int i = 0; i < w; i++)
                if(img[i+j*w] == -1) out[i+j*w] = 0;
                else if(img[i+j*w] == 0) out[i+j*w] = 1; 
        /*-------------------------------*
         *��������img[][]  �ⲿ -- -1
         *                 �׶� --  0
         *-------------------------------*/      
        areaN = num;             //������Ŀ
              
    	return out;
    }
    
    /*------------------------------------------------------*
     * HMT�任
     * Algorithm and Program Author Xie-Hua Sun
     * img[][]  -- �����ֵͼ��
	 * byte[][] -- ���HMT�任���
	 * �ṹԪ��E-- 15X15 1-����
	 * �ṹԪ��F-- (19X19 1-����)-(15X15 1-����)"��"���;���
     *------------------------------------------------------*/
    public byte[] hmt(byte[] img, int w, int h)
    {
    	int ix = 10, iy = 10;
    	byte[] out = new byte[w*h];
    	
    	//����img[]���༯imgc[]
    	byte[] imgc = new byte[w*w];
    	for(int j = 0; j < h; j++)
    	    for(int i = 0; i < w; i++)    		
    			if(img[i+j*w] == 1) imgc[i+j*w] = 0;
    			else                imgc[i+j*w] = 1;
    	//��img[]��E��ʴ	
       	byte[] out1 = erode(img, w, h, 15);
       	
       	//��imgc[][]��F��ʴ
       	byte[] out2 = erode(imgc, w, h, 19); 
       	
       	//��out1��out2�Ľ���
       	for(int j = 0; j < h-1; j++)
       	{       	
    	    for(int i = 0; i < w-1; i++)
    	    {
    		    if(out1[i+j*w] == 1&&out2[i+j*w] == 1)
    		    {
    		    	out[i+j*w] = 1; 
    		    	ix = i; iy = j;  //������������
    		    } 
    		}
    	}
    	
    	for(int i=-1; i<2; i++)
		   for(int j=-1; j<2; j++)
		       out[ix+i+(iy+j)*w] = 2;//�����Ϊ��ɫ   	        
    	return out;  	
    }
    
    /*-------------------------------------------------------
     *�Ҷ�����
     *im[][]  -- ����Ҷ�ͼ��
     *w, h    -- ����ͼ����
     *int[][] -- ���ͼ��
     *type    -- 1 ��ƽ���ṹ
     *        -- 2 ƽ���ṹ 
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
     *�Ҷȸ�ʴ
     *pix[]   -- ����ͼ������
     *w, h    -- ����ͼ����
     *int[] -- ���Ŀ��ͼ��
     *type    -- 1 ��ƽ���ṹ
     *        -- 2 ƽ���ṹ           
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
            
    //Soble����
    public int[] Sobel(int[] pix, int iw, int ih)
	{	            
        int r;
        //����
		int[] out1 = grayDilate(pix, iw, ih, 2);
		//��ʴ
		int[] out2 = grayErode(pix,  iw, ih, 2);
		
		//Soble�ʹ���		
		for(int i = 0; i < iw*ih; i++)
		{
			r = Math.abs((out1[i]&0xff)-(out2[i]&0xff));	    
		    pix[i] = 255 << 24|r << 16|r << 8|r;
		}
		return pix;
	}
	
	//�˲�3
	public int[] grayFilter(int[] pix, int iw, int ih)
	{		
       	int r;
       	int[] out1, out2;
       	            
        //����
		out1 = grayErode(pix, iw, ih, 2);
		out1 = grayDilate(out1, iw, ih, 2);
		
		//�պ�
		out1 = grayDilate(out1, iw, ih, 2);
		out1 = grayErode(out1, iw, ih, 2);
		
		//�պ�
		out2 = grayDilate(pix, iw, ih, 2);
		out2 = grayErode(out2, iw, ih, 2);
		            
        //����
		out2 = grayErode(out2, iw, ih, 2);
		out2 = grayDilate(out2, iw, ih, 2);
		for(int i = 0; i < iw*ih; i++)
		{
			r = (int)(((out1[i]&0xff)+  (out2[i]&0xff))/2);				    
		    pix[i] = 255 << 24|r << 16|r << 8|r;
		}
		return pix;
	}
	
	//��ñ�任
	public int[] grayTopHat(int[] pix, int iw, int ih)           
	{
		int r;
		int[] out;
		//����
		out = grayErode(pix, iw, ih, 2);
		out = grayDilate(out, iw, ih, 2);
		
		//��ñ����
		for(int i = 0; i< iw*ih; i++)
		{		   
	        r = pix[i] - out[i];
	        if(r < 0) r = 0;
	        out[i] = 255 << 24|r << 16|r << 8|r;	    
		}
		return out;
	}
}          