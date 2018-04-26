//WHT2.java

package process.algorithms.imageTrans;

//�ֶ�ʲ-������任�㷨

/**
 * �ֶ�ʲ-������任��Walsh-Hadmard Transform,WHT����
 * ��һ�ֵ��͵ķ����Һ����任��
 * ��������ֱ�Ǻ�����Ϊ��������
 * �����븵��Ҷ�������Ƶ����ʣ�
 * ͼ������Խ�Ǿ��ȷֲ���
 * �����ֶ�ʲ-������任�������Խ�Ǽ����ھ���ı߽��ϣ�
 * ����ֶ�ʲ�任�����������е����ʣ�
 * ��������ѹ��ͼ����Ϣ��
 * �ο� https://wenku.baidu.com/view/dc3f21e9524de518964b7df5.html
 * �ο� https://blog.csdn.net/geekmanong/article/details/50038611
 */
public class WHT2 {
    int iw, ih;

    public WHT2(int w, int h) {
        iw = w;
        ih = h;
    }

    //Walsh-Hadamard�任

    /**
     * @param pix1 �����ʱ������
     * @param n    2����
     * @return �����Ƶ������
     */
    public double[] WALSH(double[] pix1, int n) {
        //�м����
        int bfsize, t;

//        ����任����
//        Ƶ�׾��� = ������/����
//        00000000 00000000 00000000 00000001
//        00000000 00000001 00000000 00000000
//        count = 2^8 * 2^8 = 2^16
        double count = 1 << n;

        //�����������������
        double[] pix2 = new double[iw * ih];
        double[] temp = new double[iw * ih];

        // ��������
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

            //����pis1��pis2  
            temp = pix1;
            pix1 = pix2;
            pix2 = temp;
        }

        // ����ϵ��
        for (int j = 0; j < count; j++) {
            t = 0;
            for (int i = 0; i < n; i++)
                if ((j & (1 << i)) != 0)
                    t += 1 << (n - i - 1);

            temp[j] = pix1[t] / count;
        }
        return temp;
    }

    //Walsh-Hadamard��任
    public double[] IWALSH(double[] pix, int n) {
        //����任����
        double count = 1 << n;
        double[] f = WALSH(pix, n);

        //����ϵ��
        for (int j = 0; j < count; j++)
            f[j] *= count;

        return f;
    }
}