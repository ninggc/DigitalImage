//WHT2.java

package process.algorithms.imageTrans;

//沃尔什-哈达玛变换算法

/**
 * 沃尔什-哈达玛变换（Walsh-Hadmard Transform,WHT），
 * 是一种典型的非正弦函数变换，
 * 采用正交直角函数作为基函数，
 * 具有与傅里叶函数类似的性质，
 * 图像数据越是均匀分布，
 * 经过沃尔什-哈达玛变换后的数据越是集中于矩阵的边角上，
 * 因此沃尔什变换具有能量集中的性质，
 * 可以用于压缩图像信息。
 * 参考 https://wenku.baidu.com/view/dc3f21e9524de518964b7df5.html
 * 参考 https://blog.csdn.net/geekmanong/article/details/50038611
 */
public class WHT2 {
    int iw, ih;

    public WHT2(int w, int h) {
        iw = w;
        ih = h;
    }

    //Walsh-Hadamard变换

    /**
     * @param pix1 输入的时域序列
     * @param n    2的幂
     * @return 输出的频域序列
     */
    public double[] WALSH(double[] pix1, int n) {
        //中间变量
        int bfsize, t;

//        计算变换点数
//        频谱精度 = 采样率/点数
//        00000000 00000000 00000000 00000001
//        00000000 00000001 00000000 00000000
//        count = 2^8 * 2^8 = 2^16
        double count = 1 << n;

        //分配运算所需的数组
        double[] pix2 = new double[iw * ih];
        double[] temp = new double[iw * ih];

        // 蝶形运算
//        k = 0; k < 16
        for (int k = 0; k < n; k++) {
//            j = 0; j < 2^k
            for (int j = 0; j < 1 << k; j++) {
//                bfsize = 2^(16 - k)
                bfsize = 1 << (n - k);
//                i = 0; i < 2^(16 - k - 1)
                for (int i = 0; i < bfsize / 2; i++) {
                    t = j * bfsize;
                    pix2[i + t] = pix1[i + t] + pix1[i + t + bfsize / 2];
                    pix2[i + t + bfsize / 2] = pix1[i + t]
                            - pix1[i + t + bfsize / 2];
                }
            }

            //交换pis1和pis2  
            temp = pix1;
            pix1 = pix2;
            pix2 = temp;
        }

        // 调整系数
        for (int j = 0; j < count; j++) {
            t = 0;
            for (int i = 0; i < n; i++)
                if ((j & (1 << i)) != 0)
                    t += 1 << (n - i - 1);

            temp[j] = pix1[t] / count;
        }
        return temp;
    }

    //Walsh-Hadamard逆变换
    public double[] IWALSH(double[] pix, int n) {
        //计算变换点数
        double count = 1 << n;
        double[] f = WALSH(pix, n);

        //调整系数
        for (int j = 0; j < count; j++)
            f[j] *= count;

        return f;
    }
}