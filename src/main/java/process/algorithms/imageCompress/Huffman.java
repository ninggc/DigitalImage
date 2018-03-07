//Huffman.java

package process.algorithms.imageCompress;

public class Huffman 
{
	int ii;
	int ColorNum = 256;                    //灰度级数
	int iw, ih;                            //图像宽高
	int map[];                             //映射关系的数组
	int imageData[];                       //图像的灰度
	float freq[] = new float[ColorNum];    //各个灰度的频率
	float freqTemp[];                      //中间数组
	float entropy = 0;                     //图像熵
	float avgCode = 0;                     //平均码长
	float efficiency = 0;                  //编码效率

	String [] sCode = new String[ColorNum];//huffman编码表
	
	public 	Huffman(int data[], int iw, int ih)
	{		
		imageData = new int[iw*ih];
		for(int i = 0; i < iw*ih; i++)
			imageData[i] = data[i];
				
		this.iw = iw;
		this.ih = ih;
		
		//初始化
		for(int i = 0; i < ColorNum; i++)
		{
			sCode[i] = "";
			freq[i]  = 0;
		}
		
		//各个灰度的计数
		for(int i = 0; i < iw*ih; i++)
		{
			int temp   = imageData[i];
			freq[temp] = freq[temp]+1;
		}
		
		//各个灰度出现的频率
		for(int i = 0; i < ColorNum; i++)
			freq[i] = freq[i] / (iw*ih);
				
		//计算图像熵
		for(int i = 0; i < ColorNum; i++)
			if(freq[i] > 0)
				entropy -= freq[i] * Math.log(freq[i]) / Math.log(2.0);
	}
	
	//huffman编码
	public void huff()
	{	
		freqTemp = new float[ColorNum];	
		map = new int[ColorNum];
		
		for(int i = 0; i < ColorNum; i++)
		{
			freqTemp[i] = freq[i];
			map[i] = i;
		}
		
		//冒泡排序
		for(int j = 0; j < ColorNum-1; j++)
		{
			for(int i = 0; i < ColorNum-j-1; i++)
			{
				if(freqTemp[i] > freqTemp[i+1])
				{
					float temp  = freqTemp[i];
					freqTemp[i] = freqTemp[i+1];
					freqTemp[i+1] = temp;
					
					//更新映射关系
					for(int k = 0; k < ColorNum; k++)
						if(map[k] == i)
							map[k] = i + 1;
						else if(map[k] == i+1)
							map[k] = i;					
				}
			}
		}
		
		//开始编码
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
					
			//最小的两个相加
			freqTemp[ii+1] += freqTemp[ii];
			//改变映射关系
			for(int k = 0; k < ColorNum; k++)
				if(map[k] == ii)
					map[k] = ii+1;
			
			//重新排序
			for(int j = ii+1; j <ColorNum-1; j++)
			{
				if(freqTemp[j] > freqTemp[j+1])
				{
					float temp  = freqTemp[j];
					freqTemp[j] = freqTemp[j+1];
					freqTemp[j+1] = temp;
					//更新映射关系
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
		
		//计算编码的平均长度		
		avgCode = 0;
		for(int i = 0; i < ColorNum; i++)
			avgCode = avgCode + freq[i]*(sCode[i].length());
				
		//计算编码效率
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
