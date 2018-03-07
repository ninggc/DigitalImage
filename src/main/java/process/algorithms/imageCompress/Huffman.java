//Huffman.java

package process.algorithms.imageCompress;

public class Huffman 
{
	int ii;
	int ColorNum = 256;                    //�Ҷȼ���
	int iw, ih;                            //ͼ����
	int map[];                             //ӳ���ϵ������
	int imageData[];                       //ͼ��ĻҶ�
	float freq[] = new float[ColorNum];    //�����Ҷȵ�Ƶ��
	float freqTemp[];                      //�м�����
	float entropy = 0;                     //ͼ����
	float avgCode = 0;                     //ƽ���볤
	float efficiency = 0;                  //����Ч��

	String [] sCode = new String[ColorNum];//huffman�����
	
	public 	Huffman(int data[], int iw, int ih)
	{		
		imageData = new int[iw*ih];
		for(int i = 0; i < iw*ih; i++)
			imageData[i] = data[i];
				
		this.iw = iw;
		this.ih = ih;
		
		//��ʼ��
		for(int i = 0; i < ColorNum; i++)
		{
			sCode[i] = "";
			freq[i]  = 0;
		}
		
		//�����Ҷȵļ���
		for(int i = 0; i < iw*ih; i++)
		{
			int temp   = imageData[i];
			freq[temp] = freq[temp]+1;
		}
		
		//�����Ҷȳ��ֵ�Ƶ��
		for(int i = 0; i < ColorNum; i++)
			freq[i] = freq[i] / (iw*ih);
				
		//����ͼ����
		for(int i = 0; i < ColorNum; i++)
			if(freq[i] > 0)
				entropy -= freq[i] * Math.log(freq[i]) / Math.log(2.0);
	}
	
	//huffman����
	public void huff()
	{	
		freqTemp = new float[ColorNum];	
		map = new int[ColorNum];
		
		for(int i = 0; i < ColorNum; i++)
		{
			freqTemp[i] = freq[i];
			map[i] = i;
		}
		
		//ð������
		for(int j = 0; j < ColorNum-1; j++)
		{
			for(int i = 0; i < ColorNum-j-1; i++)
			{
				if(freqTemp[i] > freqTemp[i+1])
				{
					float temp  = freqTemp[i];
					freqTemp[i] = freqTemp[i+1];
					freqTemp[i+1] = temp;
					
					//����ӳ���ϵ
					for(int k = 0; k < ColorNum; k++)
						if(map[k] == i)
							map[k] = i + 1;
						else if(map[k] == i+1)
							map[k] = i;					
				}
			}
		}
		
		//��ʼ����
		for(ii = 0; ii < ColorNum-1; ii++)
			if(freqTemp[ii] > 0)
				break;
			
		for(;ii < ColorNum-1; ii++)
		{
			for(int k = 0; k < ColorNum; k++)
				if(map[k] == ii)
					sCode[k] = "1"+sCode[k];
				else if(map[k] == ii+1)
					sCode[k] = "0"+sCode[k];
					
			//��С���������
			freqTemp[ii+1] += freqTemp[ii];
			//�ı�ӳ���ϵ
			for(int k = 0; k < ColorNum; k++)
				if(map[k] == ii)
					map[k] = ii+1;
			
			//��������
			for(int j = ii+1; j <ColorNum-1; j++)
			{
				if(freqTemp[j] > freqTemp[j+1])
				{
					float temp  = freqTemp[j];
					freqTemp[j] = freqTemp[j+1];
					freqTemp[j+1] = temp;
					//����ӳ���ϵ
					for(int k = 0; k < ColorNum; k++)
					{
						if(map[k] == j)
							map[k] = j+1;
						else if(map[k] == j+1)
							map[k] = j;
					}
				}
				else				
					break;				
			}			
		}
		
		//��������ƽ������		
		avgCode = 0;
		for(int i = 0; i < ColorNum; i++)
			avgCode = avgCode + freq[i]*(sCode[i].length());
				
		//�������Ч��
		efficiency = entropy / avgCode;		
	}
	
	public float getEntropy()
	{
		return entropy;
	}
	
	public float getAvgCode()
	{
		return avgCode;
	}
	
	public float getEfficiency()
	{
		return efficiency;
	}
	
	public float[] getFreq()
	{
		return freq;
	}
	
	public String [] getCode()
	{
		return sCode;
	}
	
	public void test()
	{		
		System.out.println("the iw:"+iw+"the ih"+ih);
	}	
}
