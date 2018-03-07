/**
 * @ImageAnalysis.java
 * @Version 1.0 2010.02.20
 * @Author Xie-Hua Sun
 */

package process.algorithms;

public class ImageAnalyse
{ 
    /*----------------------------------------------------
     * �������ٺ͹淶�������
     * ����:
     * bw[]      --��ֵͼ�����
     * iw, ih    --��ֵͼ���,��
     * ���:
     * int[]     --��ֵͼ������,����͹淶������ʼ������
     *----------------------------------------------------*/
    
	public int[] Outline(byte bw[], int iw, int ih)
    {	
		boolean bFindStartPoint,               //�ҵ������·��߽���־
                bFindPoint;
		
		int currDirect;
		
		//�߽�����Ŀ
	    int inum;
		
		//�߽��������б߽������
	    int sx  = 0, sy = 0;         //��ʱ���½Ǳ߽����
	    int[] start  = new int[4];   //(start[0],start[1])Ϊ�������
	                                 //(start[2],start[3])Ϊ�淶���������   
	    int[] nCurrX  = new int[(int)(ih*iw/4)];  
	    int[] nCurrY  = new int[(int)(ih*iw/4)];
		int[] codes   = new int[(int)(ih*iw/4)];//�߽�����	
			
	    int[][] imb = new int[iw][ih];
	    
	    //ǰiw*ih��Ϊimb[][], ���4��Ϊstart[]
	    int[] result = new int[iw*ih+4];
	    
	    //�˸��������ʼɨ�跽��
	    int[][] Director = {{1, 0},{ 1,-1},{0,-1},{-1,-1},
	                        {-1,0},{-1, 1},{0, 1},{ 1, 1}};
        
        //1.���¶���, �����������߽��	    
	    bFindStartPoint = false;
		for (int j = ih-1; j >= 0; j--)
	    {
		    for(int i = 0; i < iw; i++)
		    {
				if(bw[i+j*iw] == 1)
				{
					bFindStartPoint = true;
	
					//����߽���ʼ��
					sx = i;
					sy = j;
					
					//����ѭ��
					i = iw;
					j = -1;					
				}		
			}
		}
			
		//��ʼɨ�谴��ʱ�뷽����С� 
		currDirect = 0;
		
		//���ٱ߽�
		bFindStartPoint = false;
	
		//�ӳ�ʼ�㿪ʼɨ��
		inum = 0;
		nCurrX[0] = sx;
		nCurrY[0] = sy;        
        
        //����ʱ�뷽�������߽��
        while(!bFindStartPoint)
	    {
			bFindPoint = false;
	
			while(!bFindPoint)
			{
				int u = nCurrX[inum] + Director[currDirect][0];
				if(u < 0)   u = 0;
				if(u > 255) u = 255;
				int v = nCurrY[inum]+Director[currDirect][1];
				if(v < 0)   v = 0;
				if(v > 255) v = 255;				
							
				//�ҵ���һ���߽��
				if(bw[u+v*iw] == 1)
				{					
					bFindPoint  = true;
	                
	                //�洢����
	                codes[inum] = currDirect; 
					
					//�洢����
					nCurrX[inum+1] = nCurrX[inum] + Director[currDirect][0];
					nCurrY[inum+1] = nCurrY[inum] + Director[currDirect][1];
					
					inum++;
					
                    //��������������
					if(nCurrY[inum] == sy && nCurrX[inum] == sx)
						bFindStartPoint = true;
						
					//ɨ��ķ�����ʱ����ת90��
					currDirect--;
					currDirect--;
					if(currDirect < 0)
						currDirect += 8;					
				}
				else
				{
					//ɨ�跽��˳ʱ����ת45��
					currDirect++;
					if(currDirect == 8)
						currDirect = 0;
				}	
			}
		}
		
		//д��߽��
		for(int i = 0; i <= inum; i++)
		    imb[nCurrX[i]][nCurrY[i]] = 1;
		
		//����淶��--------------------------    
		int tem_i, max_n = 0, max_i = 0;
		for(int i = 0;i < inum; i++)
        {
        	if(codes[i] == 0){
		    	tem_i = i;
		    	int n = 1;
		    	while(codes[i+1]==0){ n++; i++;}
		    	if(n > max_n)
		    	{	
		    	    max_i = tem_i;
		    	    max_n = n;
		    	}
            } 
		}
		start[0] = sx;
		start[1] = sy;
		start[2] = nCurrX[max_i];
		start[3] = nCurrY[max_i];
		
		System.out.println("\n����߽���������:");
		System.out.println("�߽����: ("+start[0]+", "+start[1]+")");
		for(int i = 0;i < inum; i++)
		    System.out.print(codes[i]+" ");
		      
		System.out.println("\n\n�淶��������"); 
		System.out.println("�߽������: ("+start[2]+", "+start[3]+")");    
	    for(int i = 0;i < inum-max_i; i++)
		    System.out.print(codes[max_i+i]+" ");
		for(int i = 0;i<max_i; i++)
		    System.out.print(codes[i]+" ");
		    
	    //�����ֵͼ������
	    for(int j = 0; j < ih; j++)
	        for(int i = 0; i < iw; i++)
	            result[i+j*iw] = imb[i][j];
	            
	    //��������͹淶�������ʼ������        
	    for(int k= 0; k < 4; k++)
	        result[iw*ih+k] = start[k];
	        	        
	    return result;		
	}
	
    //�߽���ȡ
    public byte[] Bound(byte bw[], int iw, int ih)
    {	
		int p, r;
		
		byte[] tem = new byte[iw*ih];
		for(int j = 0;j < ih; j++)
		    for(int i = 0; i < iw; i++)		    
		        tem[i+j*iw] = bw[i+j*iw];
				
		for(int j = 1; j < ih - 1; j++)
		{
			for (int i = 1; i < iw - 1; i++)
			{	
				p = bw[i+j*iw];
						
				if(p == 0)//�����ǰ�����ǰ�ɫ, ������һ��ѭ��
					continue;
				else//��ǰ�����Ǻ�ɫ
				{
					// ����ܱߵ�8-��ͨ��
					r = 1;
					for(int k =-1;k<2;k++)
					{
					    for(int l=-1;l<2;l++)
					    {					    
					        if(bw[i+k+(j+l)*iw] == 0)
					        {					        
					            r = 0;
					            k = 2; l = 2;//����2��ѭ��
					        }
					    }
					}
										
					//������Ǻڵ�,�ж�Ϊ�ڲ���,�ı���ɫ
					if(r == 1)
						tem[i+j*iw] = 0;					
				}
			}
		}
		return tem;				
	}
	
    //����С��
    public byte[] DelHole(byte[] bw, int iw, int ih)
    {	
		int i, j, s, n;
		
		int nPixelValue;;
		
		// ��Ѩ����Ŀ�Լ������ֵ
	    int nHoleNum, nMinArea;

	    int nBlackPix;

	    // ����ͷ��촫����־
	    int nDir1,nDir2;

        // С�������ֵ���Ϊ20�����ص�
	    nMinArea = 20;
		
		byte[] image2 = new byte[iw*ih];
		int[][] ibw = new int[iw][ih];
		
		for(j = 0; j < ih; j++)
		    for(i = 0; i < iw; i++)		    
		        ibw[i][j] = -bw[i+j*iw];
				
		// ��Ѩ������ֵ
		nHoleNum = 1;
		boolean oflag = true,//���whileѭ����־
		        iflag = true;//�ڲ�whileѭ����־

		while(oflag)
		{			
			s = 0;
			
			//Ѱ��ÿ����Ѩ�ĳ�ʼ����ֵ 
			for(j = 1; j < ih - 1; j++)
			{
				for (i = 1; i < iw - 1; i++)
				{					
					if(ibw[i][j] == -1)// �ҵ���ʼ����
					{
						s = 1;
						// ������ֵ�ĳɵ�ǰ�Ŀ�Ѩ��ֵ
						ibw[i][j] = nHoleNum;
						
						// ����ѭ��
						i = ih;
						j = iw;						
					}
				}
			}
			
	        iflag = true;
	        
			if(s == 0)//û�г�ʼ���أ�����ѭ��
				oflag = false;
			else
			{
				while(iflag)
				{
					// ����ͷ��촫��ϵ������ֵ0
					nDir1 = 0;
					nDir2 = 0;
	
					// ����ɨ��
					for(j = 1; j < ih - 1; j++)
			        {
				        for (i = 1; i < iw - 1; i++)
						{
							nBlackPix = ibw[i][j];
							
							// ��������Ѿ���ɨ�裬�����Ǳ���ɫ��������һ��ѭ��
							if(nBlackPix != -1)
								continue;
	
							// ����ϲ������������ֵ�Ѿ���ɨ�裬
							// �����ڵ�ǰ�Ŀ�Ѩ����ǰ������ֵ�ĳɿ�Ѩ����ֵ
							nBlackPix = ibw[i-1][j];
							if(nBlackPix == nHoleNum)
							{
								ibw[i][j] = nHoleNum;
								nDir1 = 1;								
								continue;
							}
	
							nBlackPix = ibw[i][j-1];						
							if(nBlackPix == nHoleNum)
							{
								ibw[i][j] = nHoleNum;
								nDir1 = 1;
							}						
						}
					}
	
					// ��������ȫ����ɨ�裬����ѭ��
					if(nDir1 == 0)
						iflag = false;
	
					// ����ɨ��
					for(j = ih-2; j >= 1 ; j--)
					{
						for (i = iw-2; i >= 1 ; i--)
						{
							nBlackPix = ibw[i][j];						
							
							//��������Ѿ���ɨ�裬�����Ǳ���ɫ��������һ��ѭ��
							if(nBlackPix != -1)
								continue;
	
							//����²�����Ҳ������ֵ�Ѿ���ɨ�裬�����ڵ�ǰ�Ŀ�Ѩ��
							//��ǰ������ֵ�ĳɿ�Ѩ����ֵ
							nBlackPix = ibw[i+1][j];
							if(nBlackPix == nHoleNum)
							{
								ibw[i][j] = nHoleNum;
								nDir2 = 1;								
								continue;
							}
	
							nBlackPix = ibw[i][j+1];						
							if(nBlackPix == nHoleNum)
							{
								ibw[i][j] = nHoleNum;
								nDir2 = 1;
							}
						}
					}	
					if(nDir2 == 0)
						iflag = false;
				}
			}			
			nHoleNum++;
		}
		nHoleNum -- ;
	
		// Ѱ�����С����ֵ�Ŀ�Ѩ����
		for(n = 1; n <= nHoleNum; n++)
		{
			s = 0;
			
			for(j = 0; j < ih - 1; j++)
			{
				for (i = 0; i < iw - 1; i++)
				{
					if(ibw[i][j] == n)
						s++;
	
					// �����������Ѿ�������ֵ������ѭ��
					if(s > nMinArea)
						break;
				}	
				if(s > nMinArea)
					break;
			}
	
			// С����ֵ�����򣬸����뱳��һ������ɫ��������ȥ
			if(s <= nMinArea)
				for(j = 0; j < ih - 1; j++)
			        for (i = 0; i < iw - 1; i++)
						if(ibw[i][j+1] == n)
							ibw[i][j+1] = 0;	
		}
	
	    for(j = 0; j < ih; j++)
		    for (i = 0; i < iw; i++)					
				if(ibw[i][j] != 0)
					image2[i+j*iw] = 1;
				else
					image2[i+j*iw] = 0;			
	
		return image2;								
	}	
		
	//��������
    public int[] Center(byte bw[], int iw, int ih)
    {	
		int[] centerxy = new int[2];
		
		long m00, m10, m01;
			
		// ѭ������
		int i,j;
	
		m00 = 0; m10 = 0; m01 = 0;
	
		//����0-1�׾�
		for (i = 0; i < ih; i++)
		{
			for(j = 0; j < iw; j++)
	        {
	        	if(bw[i+j*iw]==0) continue;
	        	//����0�׾�
				m00 += bw[i+j*iw];
				//����1�׾�					
				m10 += i*bw[i+j*iw];
				m01 += j*bw[i+j*iw];				
			}
		}
		
		//������������
		centerxy[0] = (int)(m10/m00);
		centerxy[1] = (int)(m01/m00);
		return centerxy;					
	}
	
	//����7�������
    public double[] Moment7(byte bw[], int iw, int ih)
    {
    	int i,j;
		double ic, jc;
		double m00, m10, m01, m20, m11, m02, m30, m21, m12, m03;
		
		double[] invmom = new double[7];
		 	
		m00 = 0; m10 = 0; m01 = 0; m20 = 0; m11 = 0; 
		m02 = 0; m30 = 0; m21 = 0; m12 = 0; m03 = 0;
		
		//����0-3�׾�
		for(j = 0; j < ih; j++)
		{
			for (i = 0; i < iw; i++)
	        {
	        	if(bw[i+j*iw]==0) continue;
	        	
	        	//����0�׾�
				m00 += bw[i+j*iw];
				
				//����1�׾�	
				m10 += i*bw[i+j*iw];
				m01 += j*bw[i+j*iw];
				
				//����2�׾�
				m20 += i*i*bw[i+j*iw];
				m11 += i*j*bw[i+j*iw];
				m02 += j*j*bw[i+j*iw];
				
				//����3�׾�
				m30 += i*i*i*bw[i+j*iw];
				m21 += i*i*j*bw[i+j*iw];
				m12 += i*j*j*bw[i+j*iw];
				m03 += j*j*j*bw[i+j*iw];
			}
		}
				
		//������������
		ic = m10/m00;
		jc = m01/m00;
		
		//����mu
		double mu11 = m11-jc*m10;
		double mu20 = m20-ic*m10;
		double mu02 = m02-jc*m01;
		double mu30 = m30-3*ic*m20+2*ic*ic*m10;
		double mu21 = m21-2*ic*m11-jc*m20+2*ic*ic*m01;
		double mu12 = m12-2*jc*m11-ic*m02+2*jc*jc*m10;
		double mu03 = m03-3*jc*m02+2*jc*jc*m01;
		
		//����eta
		double sqrtm00 = Math.sqrt(m00);
		double eta20 = mu20/(m00*m00);
		double eta11 = mu11/(m00*m00);
		double eta02 = mu02/(m00*m00);
		double eta30 = mu30/(m00*m00*sqrtm00);
		double eta21 = mu21/(m00*m00*sqrtm00);
		double eta12 = mu12/(m00*m00*sqrtm00);
		double eta03 = mu03/(m00*m00*sqrtm00);
		
		//����7�������
		invmom[0] = eta20+eta02;
		invmom[1] = (eta20-eta02)     *(eta20-eta02)
		            + 4*eta11 *eta11;
		invmom[2] = (eta30-3  *eta12) *(eta30-3*eta12)
		            + (3*eta21-eta03) *(3*eta21-eta03);
		invmom[3] = (eta30+eta12)     *(eta30+eta12)
		            + (eta21+eta03)   *(eta21+eta03);
		invmom[4] = (eta30-3*eta12)   *(eta30+eta12) 
		            * ((eta30  +eta12)*(eta30+eta12) 
		            - 3*(eta21 +eta03)*(eta21+eta03))
		            + (3*eta21 -eta03)*(eta21+eta03) 
		            * (3*(eta30+eta12)*(eta30+eta12) 
		            - (eta21   +eta03)*(eta21+eta03));	
		invmom[5] = (eta20-eta02)     *((eta30+eta12)*(eta30+eta12)
		            - (eta21 +eta03)  *(eta21+eta03))
		            + 4*eta11*(eta30+eta12)*(eta21+eta03);
		invmom[6] = (3*eta21   -eta03)*(eta30+eta12)
		            * ((eta30  +eta12)*(eta30+eta12)
		            - 3*(eta21 +eta03)*(eta21+eta03))
		            + (3*eta12 -eta30)*(eta21+eta03)
		            * (3*(eta30+eta12)*(eta30+eta12)
		            - (eta21+eta03)   *(eta21+eta03)); 
		return invmom;
    }
    
    //ϸ��-------------------------------------------
    
    //ϸ��(Deutsch�㷨)
    public byte[] Thinner1(byte bm[], int iw, int ih)
    {
	    boolean bModified;     //��ֵͼ���޸ı�־
	
		boolean bCondition1;   //ϸ������1��־
		boolean bCondition2;   //ϸ������2��־
		boolean bCondition3;   //ϸ������3��־
		boolean bCondition4;   //ϸ������4��־
		
		int nCount;
		
		//5X5���ؿ�
		int neighbour[][] = new int[5][5];
	    
	    byte[][] bw_tem = new byte[iw][ih];
	    
		bModified = true;     //ϸ���޸ı�־, ����ѭ������
	    
	    //ϸ��ѭ����ʼ
		while(bModified)
		{
			bModified = false;
	        
	        //��ʼ����ʱ��ֵͼ��bw_tem
	        for(int j = 0; j < ih; j++)
	            for(int i = 0; i < iw; i++)	            
	                bw_tem[i][j] = 0;
	                
	        for(int j = 2; j < ih - 2; j++)
	        {    
			    for(int i = 2; i < iw - 2; i++)
			    {
	                bCondition1 = false;
				    bCondition2 = false;
				    bCondition3 = false;
				    bCondition4 = false;
				    	                
				    if(bm[i+j*iw] == 0)       //��ͼ��ĵ�ǰ��Ϊ��ɫ��������
					    continue;					
					    
					//ȡ�Ե�ǰ��Ϊ���ĵ�5X5��
					for(int k = 0; k < 5; k++)					    
						for(int l = 0; l < 5; l++)					   
	                        neighbour[k][l] = bm[(i+k-2)+(j+l-2)*iw];					
			    
					//(1)�ж�����2<=n(p)<=6
		            nCount = neighbour[1][1]+neighbour[1][2]+neighbour[1][3]+
		                     neighbour[2][1]                +neighbour[2][3]+
		                     neighbour[3][1]+neighbour[3][2]+neighbour[3][3];
		            if(nCount >= 2 && nCount <= 6)
			            bCondition1 = true;
	                else{
	                    bw_tem[i][j] = 1;
						continue;       //����, �ӿ��ٶ�
					}
					
					//(2)�ж�s(p)=1
					nCount = 0;
					if (neighbour[2][3] == 0 && neighbour[1][3] == 1)
						nCount++;
					if (neighbour[1][3] == 0 && neighbour[1][2] == 1)
						nCount++;
					if (neighbour[1][2] == 0 && neighbour[1][1] == 1)
						nCount++;
					if (neighbour[1][1] == 0 && neighbour[2][1] == 1)
						nCount++;
					if (neighbour[2][1] == 0 && neighbour[3][1] == 1)
						nCount++;
					if (neighbour[3][1] == 0 && neighbour[3][2] == 1)
						nCount++;
					if (neighbour[3][2] == 0 && neighbour[3][3] == 1)
						nCount++;
					if (neighbour[3][3] == 0 && neighbour[2][3] == 1)
						nCount++;					
					if (nCount == 1)
						bCondition2 = true;
	                else
	                {
	                    bw_tem[i][j] = 1;
						continue;
					}
					
					//(3)�ж�p0*p2*p4=0 or s(p2)!=1
					if (neighbour[2][3]*neighbour[1][2]*neighbour[2][1] == 0)
						bCondition3 = true;
					else
					{
						nCount = 0;
						if (neighbour[0][2] == 0 && neighbour[0][1] == 1)
							nCount++;
						if (neighbour[0][1] == 0 && neighbour[1][1] == 1)
							nCount++;
						if (neighbour[1][1] == 0 && neighbour[2][1] == 1)
							nCount++;
						if (neighbour[2][1] == 0 && neighbour[2][2] == 1)
							nCount++;
						if (neighbour[2][2] == 0 && neighbour[2][3] == 1)
							nCount++;
						if (neighbour[2][3] == 0 && neighbour[1][3] == 1)
							nCount++;
						if (neighbour[1][3] == 0 && neighbour[0][3] == 1)
							nCount++;
						if (neighbour[0][3] == 0 && neighbour[0][2] == 1)
							nCount++;
						if (nCount != 1) //s(p2)!=1
							bCondition3 = true;
						else
						{
	                        bw_tem[i][j] = 1;
							continue;
						}
					}
	
					//(4)�ж�p2*p4*p6=0 or s(p4)!=1
					if (neighbour[1][2]*neighbour[2][1]*neighbour[3][2] == 0)
						bCondition4 = true;
					else
					{
						nCount = 0;
						if (neighbour[1][1] == 0 && neighbour[1][0] == 1)
							nCount++;
						if (neighbour[1][0] == 0 && neighbour[2][0] == 1)
							nCount++;
						if (neighbour[2][0] == 0 && neighbour[3][0] == 1)
							nCount++;
						if (neighbour[3][0] == 0 && neighbour[3][1] == 1)
							nCount++;
						if (neighbour[3][1] == 0 && neighbour[3][2] == 1)
							nCount++;
						if (neighbour[3][2] == 0 && neighbour[2][2] == 1)
							nCount++;
						if (neighbour[2][2] == 0 && neighbour[1][2] == 1)
							nCount++;
						if (neighbour[1][2] == 0 && neighbour[1][1] == 1)
							nCount++;
						if (nCount != 1)//s(p4)!=1
							bCondition4 = true;
					}
					if(bCondition1 && bCondition2 && bCondition3 && bCondition4)
					{
						bw_tem[i][j] = 0;
						bModified = true;
					}
					else
						bw_tem[i][j] = 1;									    
				}
			}
	
			//��ϸ���˵���ʱͼ��bw_tem[h][w]copy��bw[h][w],���һ��ϸ��
	        for(int j = 2;j < ih - 2; j++) 
			    for(int i = 2;i < iw - 2; i++)
	                bm[i+j*iw] = bw_tem[i][j];
		}		
	    return bm;				
	}
	
	//Rosenfeld4-����ϸ���㷨
	public byte[] Thinner2(byte bm[], int iw, int ih)
	{
	    byte g[][];                       //��ʱͼ������
	    int n[] = new int[8];
	    int a[] = {0, -1, 1, 0, 0};
	    int b[] = {0, 0, 0, 1, -1};
	    int nrnd, n02, n04, n06, n24, n26, n46, n123, n345, n567, n701;
	    int i, i1, j, j1;
	    byte m;
	    boolean flag = false;
	    
	    g = new byte[iw][ih];       
	    	
	    for(j = 0; j < ih; j++)
	        for(i = 0; i < iw; i++)	       	
	      	    g[i][j] = bm[i+j*iw];
	   	    
	    do
	    {
	        flag = false;
	        
	        for(m = 1; m <= 4; m++)
	        {
	            for(i = 1; i < iw-1; i++)
	            {
	                i1 = i + a[m];
	
	                for(j = 1; j < ih-1; j++)
	                {
	                    if(bm[i+j*iw] == 0)
	                        continue;
	
	                    j1  = j + b[m];
	                    	
	                    if(bm[i1+j1*iw] > 0)
	                        continue;
	                    
	                    n[0] = bm[(i+1)+j*iw];
	                    n[1] = bm[i+1+(j-1)*iw];
	                    n[2] = bm[i+(j-1)*iw];
	                    n[3] = bm[i-1+(j-1)*iw];
	                    n[4] = bm[i-1+j*iw];
	                    n[5] = bm[i-1+(j+1)*iw];      
	                    n[6] = bm[i+(j+1)*iw];
	                    n[7] = bm[i+1+(j+1)*iw];
	
	                    nrnd = n[0] + n[1] + n[2] + n[3] 
	                         + n[4] + n[5] + n[6] + n[7] ;
	                    
	                    if(nrnd <= 1)
	                        continue;                    
	                    
	                    n02  = n[0] + n[2];
	                    n04  = n[0] + n[4];
	                    n06  = n[0] + n[6];
	                    n24  = n[2] + n[4];
	                    n26  = n[2] + n[6];
	                    n46  = n[4] + n[6];
	                    n123 = n[1] + n[2] + n[3];
	                    n345 = n[3] + n[4] + n[5];
	                    n567 = n[5] + n[6] + n[7];
	                    n701 = n[7] + n[0] + n[1];
	
	                    if(n[0] == 1 && n26 == 0 && n345 > 0)
	                        continue;
	                    if(n[2] == 1 && n04 == 0 && n567 > 0)
	                        continue;
	                    if(n[4] == 1 && n26 == 0 && n701 > 0)
	                        continue;
	                    if(n[6] == 1 && n04 == 0 && n123 > 0)
	                        continue;     	
	                    if(n[1] == 1 && n02 == 0)
	                        continue;
	                    if(n[3] == 1 && n24 == 0)
	                         continue; 
	                    if(n[5] == 1 && n46 == 0)
	                        continue;
	                    if(n[7]==1 && n06==0)
	                        continue;             	
	                    
	                    g[i][j] = 0;
	                    flag = true;
	                }
	            }
	
	            for(j = 0; j < ih; j++)
	                for(i = 0; i < iw; i++)	                
	                    bm[i+j*iw] = g[i][j];	            
	        }	       
	    }while(flag);
	    return bm;	       
	}		
}             