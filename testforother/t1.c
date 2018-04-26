/*************************************************************************
	*
	* 函数名称：
	*   WALSH()
	*
	* 参数:
	*   double* f			- 输入的时域序列
	*   double* F			- 输出的频域序列
	*   int r				- 2的幂数		
	*
	* 返回值:
	*   BOOL               - 成功返回TRUE，否则返回FALSE。
	*
	* 说明:
	*   该函数进行一维快速沃尔什——哈达马变换。
	*
	************************************************************************/
void WALSH(double *f, double *F, int r);
/*************************************************************************
	*
	* 函数名称：
	*   IWALSH()
	*
	* 参数:
	*   double* f			- 输入的时域序列
	*   double* F			- 输出的频域序列
	*   int r				- 2的幂数		
	*
	* 返回值:
	*   BOOL               - 成功返回TRUE，否则返回FALSE。
	*
	* 说明:
	*   该函数进行一维快速沃尔什——哈达马逆变换。
	*
	************************************************************************/
void IWALSH(double *F, double *f, int r);
/*************************************************************************
	*
	* 函数名称：
	*   FreqWALSH()
	*
	* 参数:
	*   double* f			- 输入的时域序列
	*   double* F			- 输出的频域序列
	*	 LONG width			- 图象宽度
	*	 LONG height		- 图象高度
	*
	* 返回值:
	*   BOOL               - 成功返回TRUE，否则返回FALSE。
	*
	* 说明:
	*   该函数进行二维快速沃尔什——哈达玛变换。
	*
	************************************************************************/
BOOL FreqWALSH(double *f, double *F, LONG width, LONG height);
/*************************************************************************
	*
	* 函数名称：
	*   IFreqWALSH()
	*
	* 参数:
	*   double* f			- 输入的时域序列
	*   double* F			- 输出的频域序列
	*	 LONG width		- 图象宽度
	*	 LONG height		- 图象高度
	*
	* 返回值:
	*   BOOL               - 成功返回TRUE，否则返回FALSE。
	*
	* 说明:
	*   该函数进行二维快速沃尔什——哈达玛逆变换。
	*
	************************************************************************/
BOOL IFreqWALSH(double *f, double *F, LONG width, LONG height);
/*************************************************************************
	*
	* 函数名称：
	*   BmpWalsh()
	*
	* 参数:
	*   BYTE* bmp,LONG width,LONG height
	*
	* 返回值:
	*   BOOL               - 成功返回TRUE，否则返回FALSE。
	*
	* 说明:
	*   该函数对图象进行二维快速沃尔什——哈达马变换。
	*
	************************************************************************/
BOOL BmpWalsh(BYTE *bmp, LONG width, LONG height);

voidMyProcess::WALSH(double *f, double *F, int r)
{
    // 循环变量
    LONG i;
    LONG j;
    LONG k;
    // 中间变量
    int p;
    double *X;
    // 计算快速沃尔什变换点数
    LONG N = 1 << r;
    // 分配运算所需的数组
    double *X1 = newdouble[N];
    double *X2 = newdouble[N];
    // 将时域点写入数组X1
    memcpy(X1, f, sizeof(double) * N);
    // 蝶形运算
    for (k = 0; k < r; k++)
    {
        for (j = 0; j < 1 << k; j++)
        {
            for (i = 0; i < 1 << (r - k - 1); i++)
            {
                p = j * (1 << (r - k));
                X2[i + p] = X1[i + p] + X1[i + p + (int)(1 << (r - k - 1))];
                X2[i + p + (int)(1 << (r - k - 1))] = X1[i + p] - X1[i + p + (int)(1 << (r - k - 1))];
            }
        }
        // 互换X1和X2
        X = X1;
        X1 = X2;
        X2 = X;
    }
    // 调整系数
    for (j = 0; j < N; j++)
    {
        p = 0;
        for (i = 0; i < r; i++)
        {
            if (j & (1 << i))
            {
                p += 1 << (r - i - 1);
            }
        }
        F[j] = X1[p] / N;
    }
    // 释放内存
    delete X1;
    delete X2;
}

voidMyProcess::IWALSH(double *F, double *f, int r)
{
    // 循环变量
    int i;
    // 计算变换点数
    LONG N = 1 << r;
    // 调用快速沃尔什－哈达玛变换进行反变换
    WALSH(F, f, r);
    // 调整系数
    for (i = 0; i < N; i++)
        f[i] *= N;
}

BOOL MyProcess::FreqWALSH(double *f, double *F, LONG width, LONG height)
{
    // 循环变量
    LONG i;
    LONG j;
    LONG k;
    // 进行离散余弦变换的宽度和高度（2的整数次方）
    LONG w = 1;
    LONG h = 1;
    int wp = 0;
    int hp = 0;
    // 计算进行离散余弦变换的宽度和高度（2的整数次方）
    while (w < width / 3)
    {
        w *= 2;
        wp++;
    }
    while (h < height)
    {
        h *= 2;
        hp++;
    }
    // 分配内存
    double *TempIn = newdouble[h];
    double *TempOut = newdouble[h];
    // 对y方向进行离散余弦变换
    for (i = 0; i < w * 3; i++)
    {
        // 抽取数据
        for (j = 0; j < h; j++)
            TempIn[j] = f[j * w * 3 + i];
        // 一维快速离散余弦变换
        WALSH(TempIn, TempOut, hp);
        // 保存变换结果
        for (j = 0; j < h; j++)
            f[j * w * 3 + i] = TempOut[j];
    }
    // 释放内存
    deleteTempIn;
    deleteTempOut;
    // 分配内存
    TempIn = newdouble[w];
    TempOut = newdouble[w];
    // 对x方向进行快速离散余弦变换
    for (i = 0; i < h; i++)
    {
        for (k = 0; k < 3; k++)
        {
            // 抽取数据
            for (j = 0; j < w; j++)
                TempIn[j] = f[i * w * 3 + j * 3 + k];
            // 一维快速离散余弦变换
            WALSH(TempIn, TempOut, wp);
            // 保存变换结果
            for (j = 0; j < w; j++)
                F[i * w * 3 + j * 3 + k] = TempOut[j];
        }
    }
    // 释放内存
    deleteTempIn;
    deleteTempOut;
    return TRUE;
}

BOOL MyProcess::IFreqWALSH(double *f, double *F, LONG width, LONG height)
{
    // 循环变量
    LONG i;
    LONG j;
    LONG k;
    // 赋初值
    LONG w = 1;
    LONG h = 1;
    int wp = 0;
    int hp = 0;
    // 计算进行付立叶变换的宽度和高度（2的整数次方）
    while (w < width / 3)
    {
        w *= 2;
        wp++;
    }
    while (h < height)
    {
        h *= 2;
        hp++;
    }
    // 分配内存
    double *TempIn = newdouble[w];
    double *TempOut = newdouble[w];
    // 对x方向进行快速付立叶变换
    for (i = 0; i < h; i++)
    {
        for (k = 0; k < 3; k++)
        {
            // 抽取数据
            for (j = 0; j < w; j++)
                TempIn[j] = F[i * w * 3 + j * 3 + k];
            // 一维快速傅立叶变换
            IWALSH(TempIn, TempOut, wp);
            // 保存变换结果
            for (j = 0; j < w; j++)
                F[i * w * 3 + j * 3 + k] = TempOut[j];
        }
    }
    // 释放内存
    deleteTempIn;
    deleteTempOut;
    TempIn = newdouble[h];
    TempOut = newdouble[h];
    // 对y方向进行快速付立叶变换
    for (i = 0; i < w * 3; i++)
    {
        // 抽取数据
        for (j = 0; j < h; j++)
            TempIn[j] = F[j * w * 3 + i];
        // 一维快速傅立叶变换
        IWALSH(TempIn, TempOut, hp);
        // 保存变换结果
        for (j = 0; j < h; j++)
            F[j * w * 3 + i] = TempOut[j];
    }
    // 释放内存
    deleteTempIn;
    deleteTempOut;
    for (i = 0; i < h; i++)
    {
        for (j = 0; j < w * 3; j++)
        {
            if (i < height && j < width)
                *(f + i * width + j) = F[i * w * 3 + j];
        }
    }
    return TRUE;
}

BOOL MyProcess::BmpWalsh(BYTE *bmp, LONG width, LONG height)
{
    // 循环变量
    LONG i;
    LONG j;
    // 进行沃尔什——哈达玛变换的宽度和高度（2的整数次方）
    LONG w = 1;
    LONG h = 1;
    int wp = 0;
    int hp = 0;
    // 计算进行离散余弦变换的宽度和高度（2的整数次方）
    while (w < width / 3)
    {
        w *= 2;
        wp++;
    }
    while (h < height)
    {
        h *= 2;
        hp++;
    }
    // 分配内存
    double *f = newdouble[w * h * 3];
    double *F = newdouble[w * h * 3];
    // 向时域赋值并补零
    for (i = 0; i < h; i++)
    {
        for (j = 0; j < w * 3; j++)
        {
            if (i < height && j < width)
                f[i * w * 3 + j] = bmp[width * i + j];
            else
                f[w * i * 3 + j] = 0.0f;
        }
    }
    // 进行频谱分析
    if (FreqWALSH(f, F, width, height) == FALSE)
        return FALSE;
    // 更新所有象素
    for (i = 0; i < height; i++)
    {
        for (j = 0; j < width; j++)
        {
            // 判断是否超过255
            if (fabs(F[i * w * 3 + j] * 1000) > 255)
            {
                // 对于超过的，直接设置为255
                bmp[width * (height - 1 - i) + j] = 255;
            }
            else
            {
                // 如果没有超过，则按实际计算结果赋值
                bmp[width * (height - 1 - i) + j] = fabs(F[i * w * 3 + j] * 1000);
            }
        }
    }
    //释放内存
    delete[] f;
    delete[] F;
    // 返回
    return TRUE;
}