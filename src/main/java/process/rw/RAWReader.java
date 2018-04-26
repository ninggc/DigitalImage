/*
 * @RAW.java
 * @Version 1.0 2010.02.26
 * @Author Xie-Hua Sun
 */
package process.rw;

import java.io.*;

public class RAWReader {
    //��image����ΪRAWͼ��,����byte[][]
    public byte[][] readRAW_DAT(String name, int iw, int ih) {
        byte[][] pix = new byte[iw][ih];

        try {
            FileInputStream fin = new FileInputStream(name);
            DataInputStream in = new DataInputStream(fin);

            //�����ļ���������
            for (int j = 0; j < ih; j++) {
                for (int i = 0; i < iw; i++) {
                    byte c = in.readByte();
                    pix[i][j] = c;
                }
            }
        } catch (IOException e1) {
        }

        return pix;
    }

    //��image����ΪRAWͼ��, ����int[][]
    public int[][] readRAW(String name, int iw, int ih) {
        int c;
        int[][] pix = new int[iw][ih];

        try {
            FileInputStream fin = new FileInputStream(name);
            DataInputStream in = new DataInputStream(fin);

            //�����ļ���������
            for (int j = 0; j < ih; j++) {
                for (int i = 0; i < iw; i++) {
                    c = in.readByte();
                    if (c < 0) c += 256;
                    pix[i][j] = c;
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
            System.out.println("Exception!" + e1.getStackTrace());
        }
        return pix;
    }

    //����.raw�ļ�
    public int[] readRAW1D(String name, int iw, int ih) {
        int[] pix = new int[iw * ih];
        try {
            FileInputStream fin = new FileInputStream(name);
            DataInputStream in = new DataInputStream(fin);

            for (int i = 0; i < iw * ih; i++) {     //�����ļ���������
                int c = in.readByte();
                if (c < 0) c = c + 256;
                pix[i] = c;
            }
        } catch (IOException e1) {
            System.out.println("Exception!" + e1.getStackTrace());e1.printStackTrace();
        }
        return pix;
    }

    //�ڵ�ǰĿ¼д��.raw�ļ�
    public void writeRAW(String name, int[] pix, int iw, int ih) {
        try {
            RandomAccessFile rf = new RandomAccessFile(name, "rw");
            for (int i = 0; i < iw * ih; i++) {
                rf.writeByte((byte) (pix[i] & 0xff));
            }
            rf.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();

        } catch (IOException e2) {
            e2.printStackTrace();

        }
    }
}