/**
 * @Ch15ImageWatermark.java
 * @Version 1.0 2010.03.07
 * @Author Xie-Hua Sun
 */

package com.ninggc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;

import process.algorithms.ImageWatermark;
import process.rw.RAW;
import process.common.Common;

@SuppressWarnings("ALL")
public class Ch15ImageWatermark extends JFrame implements ActionListener {
    Image iImage, oImage;
    BufferedImage bImage;

    int iw, ih;
    int[] pixels,
            cphpix;             //����ͼ������

    boolean loadflag = false,
            runflag = false; //ͼ����ִ�б�־

    String imn,               //ͼ���ļ���
            imh,               //����ͼ���ʶ"c_"
            wtr,
            title;             //ͼ�����

    ImageWatermark watermark;
    RAW reader;
    Common common;

    //    ���ڱ�������ļ����ϼ�Ŀ¼
    String path = "./pic/";

    public Ch15ImageWatermark() {
        setTitle("����ͼ����-Java�����ʵ�� ��15�� ͼ��ˮӡ");
        this.setBackground(Color.lightGray);

        //�˵�����
        setMenu();

        watermark = new ImageWatermark();
        reader = new RAW();
        common = new Common();

        //�رմ���
        closeWin();

        setSize(530, 330);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent evt) {
        Graphics graph = getGraphics();
        MediaTracker tracker = new MediaTracker(this);

        if (evt.getSource() == openItem) {
            JFileChooser chooser = new JFileChooser();
            common.chooseFile(chooser, "./images/ch15", 0);
            int r = chooser.showOpenDialog(null);

            if (r == JFileChooser.APPROVE_OPTION) {
                if (runflag) {
                    loadflag = false;
                    runflag = false;
                }

                if (!loadflag) {
                    String name = chooser.getSelectedFile().getAbsolutePath();

                    //ȡ�ļ���
                    String filename = chooser.getSelectedFile().getName();

                    int len = filename.length();

                    //ȡ��չ��
                    String exname = filename.substring(len - 3, len);

                    //ȡ�ļ���imn
                    imn = filename.substring(0, len - 4);

                    //ȡ���ܱ�ʶimh
                    imh = filename.substring(0, 2);

                    if (exname.equalsIgnoreCase("raw")) {
                        pixels = reader.readRAW1D(name, iw, ih);
                        ImageProducer ip = new MemoryImageSource(iw, ih,
                                pixels, 0, iw);
                        iImage = createImage(ip);
                    } else {
                        //װ��ͼ��
                        iImage = common.openImage(name, tracker);
                        //ȡ����ͼ��Ŀ�͸�
                        iw = iImage.getWidth(null);
                        ih = iImage.getHeight(null);
                        repaint();
                    }
                    if (!imh.equals("j_")) title = "ԭͼ";
                    else title = "�ܹ���";
                    loadflag = true;
                }
            }
        }
        //LSB����ˮӡǶ��
        else if (evt.getSource() == wlsbchaosItem) {
            if (loadflag) {
                setTitle("��15�� ͼ��ˮӡ LSB����ˮӡ�㷨 ���� ���ƻ�");

                //������Կ
                double x = (common.getParam("������Կ(111-999)", "888")) / 1000.0;
                pixels = common.grabber(iImage, iw, ih);

                //���ɻ���ˮӡ��Ϣ
                int[] water = watermark.chaos(x, iw, ih);

                //Ƕ��ˮӡ
                pixels = watermark.chaosEmbed(pixels, water, iw, ih);

                oImage = showPix(graph, pixels, iw, ih, "ԭͼ", "��ˮӡͼ");
                runflag = true;

                //д��.raw��ˮӡ�ļ�
                reader.writeRAW(path + "w_" + imn + ".raw", pixels, iw, ih);

                //�洢��ˮӡͼ��ΪJPEG
                File file = new File(path + "/j_" + imn + ".jpg");
                bImage = (BufferedImage) this.createImage(iw, ih);
                bImage.createGraphics().drawImage(oImage, 0, 0, this);
                common.saveImageAsJPEG(bImage, file);
                JOptionPane.showMessageDialog(null, "��Ŀ¼images/ch15/jpg�洢\n"
                        + "j_" + imn + ".jpg����ͼ�ɹ�!\n\n"
                        + "��Ŀ¼images/ch15/water,\n"
                        + "���溬ˮӡͼw_" + imn + ".raw�ɹ�!");
            }

        }
        //LSB����ˮӡ���
        else if (evt.getSource() == dlsbchaosItem) {
            if (!imh.equals("j_"))                //δ��JPEG����
                pixels = reader.readRAW1D(path + "w_"
                        + imn + ".raw", iw, ih);
            else {                                    //��JPEG����
                pixels = common.grabber(iImage, iw, ih);
                for (int i = 0; i < iw * ih; i++)
                    pixels[i] = pixels[i] & 0xff;
            }
            //������Կ
            double x = (common.getParam("������Կ(111-999)", "888")) / 1000.0;

            //���ɻ���ˮӡ��Ϣ
            int[] water = watermark.chaos(x, iw, ih);

            //���ˮӡ, ��ʾ���
            double thresh = 0.265;
            double d = watermark.chaosDetect(pixels, water, iw, ih);
            if (d > thresh)
                JOptionPane.showMessageDialog(null, "��⵽ˮӡ,\n d = " + d);
            else
                JOptionPane.showMessageDialog(null, "��ⲻ��ˮӡ!!\n d = " + d);
            loadflag = false;
        }
        //patchworkˮӡǶ��
        else if (evt.getSource() == wpatchworkItem) {
            if (loadflag) {
                setTitle("��15�� ͼ��ˮӡ Patchworkˮӡ�㷨 ���� ���ƻ�");

                pixels = common.grabber(iImage, iw, ih);

                //Ƕ��ˮӡ
                pixels = watermark.patchworkEmbed(pixels, iw, ih, 3);
                showPix(graph, pixels, iw, ih, "ԭͼ", "��ˮӡͼ");
                runflag = true;

                //д��.raw��ˮӡ�ļ�
//                reader.writeRAW(path + "w_" + imn +
//                        ".raw", pixels, iw, ih);
                String name = "./pic/w_" + imn + ".raw";
                reader.writeRAW(name, pixels, iw, ih);


//                �鿴Ŀ¼
//                File directory = new File("");//�趨Ϊ��ǰ�ļ���
//                try{
//                    System.out.println(directory.getCanonicalPath());//��ȡ��׼��·��
//                    System.out.println(directory.getAbsolutePath());//��ȡ����·��
//                }catch (IOException e) {
//                    e.printStackTrace();
//                }


                JOptionPane.showMessageDialog(null,
                        "���溬ˮӡͼ��" + name + ".raw�ɹ�!");
            } else
                JOptionPane.showMessageDialog(null, "���ȴ�һ��ͼ!");

        }
        //patchworkˮӡ���
        else if (evt.getSource() == dpatchworkItem) {
            String name = "./pic/w_" + imn + ".raw";
            pixels = reader.readRAW1D(name, iw, ih);
            ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
            oImage = createImage(ip);
            pixels = common.grabber(oImage, iw, ih);

            //���ˮӡ, ��ʾ���
            double thresh = 1.8 * 3;
            double d = watermark.patchworkDetect(pixels, iw, ih);
            if (d > thresh)
                JOptionPane.showMessageDialog(null, "��⵽ˮӡ,\n d = " + d);
            else
                JOptionPane.showMessageDialog(null, "��ⲻ��ˮӡ!!\n d = " + d);
        }
        //lsbͼ��ˮӡǶ��
        else if (evt.getSource() == wlsbimageItem) {
            if (loadflag) {
                setTitle("��15�� ͼ��ˮӡ LSBͼ��ˮӡ ���� ���ƻ�");

                String watername = "Bigwater.jpg";
//                String watername = "Bigwater.png";
                pixels = common.grabber(common.openImage(
                        path + watername, tracker), iw, ih);

                //����ˮӡͼ��
                int[] water = watermark.readWater(pixels, iw, ih);

                pixels = common.grabber(iImage, iw, ih);

                //Ƕ��ˮӡ
                pixels = watermark.chaosEmbed(pixels, water, iw, ih);
                showPix(graph, pixels, iw, ih, "ԭͼ", "��ˮӡͼ");
                runflag = true;

                //д��.raw��ˮӡ�ļ�
                reader.writeRAW(path + "w_" + imn +
                        ".raw", pixels, iw, ih);

                JOptionPane.showMessageDialog(null, "��Ŀ¼images/ch15/water,\n"
                        + "���溬ˮӡͼ��w_" + imn + ".raw�ɹ�!");
            } else
                JOptionPane.showMessageDialog(null, "���ȴ�һ��ͼ!");

        }
        //LSBͼ��ˮӡ��ȡ
        else if (evt.getSource() == dlsbimageItem) {
            pixels = reader.readRAW1D(path + "w_" + imn + ".raw", iw, ih);
            ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
            oImage = createImage(ip);

            pixels = common.grabber(oImage, iw, ih);

            //��ȡˮӡͼ��
            pixels = watermark.getWater(pixels, iw, ih);
            showPix(graph, pixels, iw, ih, "ԭͼ", "ȡ��ˮӡ");
            runflag = true;
        }



        //��ƵˮӡǶ��
        else if (evt.getSource() == walshItem) {
            if (loadflag) {
                setTitle("��15�� ͼ��ˮӡ ��Ƶˮӡ�㷨 ���� ���ƻ�");
                pixels = common.grabber(iImage, iw, ih);

//                Ƕ��ˮӡ
                pixels = watermark.embedWatermark(pixels, iw, ih);

                oImage = showPix(graph, pixels, iw, ih, "ԭͼ", "��ˮӡͼ");

                //д��.raw��ˮӡ�ļ�
                reader.writeRAW(path + "w_" + imn +
                        ".raw", pixels, iw, ih);

                JOptionPane.showMessageDialog(null,
                        "��Ŀ¼images/ch15/water,\n" +
                                "���溬ˮӡͼ��w_" + imn + ".raw�ɹ�!");
            } else
                JOptionPane.showMessageDialog(null, "���ȴ�һ��ͼ!");
        }
        //��Ƶˮӡ���
        else if (evt.getSource() == dwalshItem) {
            pixels = reader.readRAW1D(path + "w_"
                    + imn + ".raw", iw, ih);

//            �������ƶ�
            double sim = watermark.getSimValue(iImage, pixels, iw, ih);
            JOptionPane.showMessageDialog(null, "���ƶ� sim = " + sim);
        }


//        ��������DM��Dither Modulation��
//        ������������Quantization Index Modulation
        //DMͼ��ˮӡǶ��
        else if (evt.getSource() == dmItem) {
            if (loadflag) {
                setTitle("��15�� ͼ��ˮӡ DMˮӡ�㷨 ���� ���ƻ�");
                pixels = common.grabber(iImage, iw, ih);

                double[][] outIm;

                //����ˮӡͼ��
                String waterimage = "water.jpg";
                String waterimage2 = "Bigwater_1.png";
                pixels = common.grabber(common.openImage(
                        path + waterimage, tracker), 32, 32);
                int[] water = watermark.readImWater(pixels, 32, 32);

                pixels = common.grabber(iImage, iw, ih);

                //Ƕ��ˮӡ
                outIm = watermark.dmEmbed(pixels, water, iw, ih);

                for (int j = 0; j < ih; j++) {
                    for (int i = 0; i < iw; i++) {
                        int tem = (int) outIm[i][j];
                        if (tem > 255) tem = 255;
                        if (tem < 0) tem = 0;
                        pixels[i + j * iw] = (255 << 24) | (tem << 16) | (tem << 8) | tem;
                    }
                }
                oImage = showPix(graph, pixels, iw, ih, "ԭͼ", "��ˮӡͼ");

                reader.writeRAW(path + "w_" + imn +
                        ".raw", pixels, iw, ih);

                //�洢��ˮӡͼ��ΪJPEG
                File file = new File(path + "/j_" + imn + ".jpg");
                bImage = (BufferedImage) this.createImage(iw, ih);
                bImage.createGraphics().drawImage(oImage, 0, 0, this);
                common.saveImageAsJPEG(bImage, file);
                JOptionPane.showMessageDialog(null, "��Ŀ¼images/ch15/jpg�洢\n"
                        + "j_" + imn + ".jpg����ͼ�ɹ�!\n\n"
                        + "��Ŀ¼images/ch15/water,\n"
                        + "���溬ˮӡͼw_" + imn + ".raw�ɹ�!");
            }
        }
        //DMͼ��ˮӡ��ȡ
        else if (evt.getSource() == ddmItem) {
            if (!imh.equals("j_"))                //δ��JPEG����
            {
                pixels = reader.readRAW1D(path + "w_"
                        + imn + ".raw", iw, ih);
                int[] water = watermark.getImageWater(pixels, iw, ih);
                showPix(graph, water, 32, 32, "��ˮӡͼ", "ȡ��ˮӡ");
            } else {                                    //��JPEG����
                pixels = common.grabber(iImage, iw, ih);
                for (int i = 0; i < iw * ih; i++)
                    pixels[i] = pixels[i] & 0xff;
                int[] water = watermark.getImageWater(pixels, iw, ih);
                showPix(graph, water, 32, 32, "�ܹ���", "ȡ��ˮӡ");
            }
        } else if (evt.getSource() == exitItem)
            System.exit(0);
    }

    public void paint(Graphics g) {
        if (iImage != null) {
            g.clearRect(0, 0, 530, 350);
            g.drawImage(iImage, 5, 50, null);
            g.drawString(title, 120, 320);
        }
    }

    public Image showPix(Graphics graph, int[] pixels, int w, int h,
                         String istr, String ostr) {
        //�������е����ز���һ��ͼ��
        ImageProducer ip = new MemoryImageSource(w, h, pixels, 0, w);
        Image oImage = createImage(ip);
        common.draw(graph, iImage, istr, oImage, ostr);
        runflag = true;
        return oImage;
    }

    public static void main(String[] args) {
        new Ch15ImageWatermark();
    }

    private void closeWin() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    //�˵�����
    public void setMenu() {
        Menu fileMenu = new Menu("�ļ�");
        openItem = new MenuItem("��");
        openItem.addActionListener(this);
        fileMenu.add(openItem);

        exitItem = new MenuItem("�˳�");
        exitItem.addActionListener(this);
        fileMenu.add(exitItem);

        Menu spwaterMenu = new Menu("����ˮӡ");

        wlsbchaosItem = new MenuItem("LSB����ˮӡǶ��");
        wlsbchaosItem.addActionListener(this);
        spwaterMenu.add(wlsbchaosItem);

        dlsbchaosItem = new MenuItem("LSB����ˮӡ���");
        dlsbchaosItem.addActionListener(this);
        spwaterMenu.add(dlsbchaosItem);
        spwaterMenu.addSeparator();

        wpatchworkItem = new MenuItem("PatchworkˮӡǶ��");
        wpatchworkItem.addActionListener(this);
        spwaterMenu.add(wpatchworkItem);

        dpatchworkItem = new MenuItem("Patchworkˮӡ���");
        dpatchworkItem.addActionListener(this);
        spwaterMenu.add(dpatchworkItem);
        spwaterMenu.addSeparator();

        wlsbimageItem = new MenuItem("LSBͼ��ˮӡǶ��");
        wlsbimageItem.addActionListener(this);
        spwaterMenu.add(wlsbimageItem);

        dlsbimageItem = new MenuItem("LSBͼ��ˮӡ��ȡ");
        dlsbimageItem.addActionListener(this);
        spwaterMenu.add(dlsbimageItem);

        Menu frwaterMenu = new Menu("Ƶ��ˮӡ");
        walshItem = new MenuItem("��ƵˮӡǶ��");
        walshItem.addActionListener(this);
        frwaterMenu.add(walshItem);

        dwalshItem = new MenuItem("��Ƶˮӡ���");
        dwalshItem.addActionListener(this);
        frwaterMenu.add(dwalshItem);
        frwaterMenu.addSeparator();

        dmItem = new MenuItem("DMͼ��ˮӡǶ��");
        dmItem.addActionListener(this);
        frwaterMenu.add(dmItem);

        ddmItem = new MenuItem("DMͼ��ˮӡ��ȡ");
        ddmItem.addActionListener(this);
        frwaterMenu.add(ddmItem);

        MenuBar menuBar = new MenuBar();
        menuBar.add(fileMenu);
        menuBar.add(spwaterMenu);
        menuBar.add(frwaterMenu);
        setMenuBar(menuBar);
    }

    MenuItem openItem;
    MenuItem exitItem;

    MenuItem wlsbchaosItem;   //lsbǶ�����ˮӡ
    MenuItem dlsbchaosItem;   //lsb������ˮӡ
    MenuItem wpatchworkItem;  //patchworkǶ��ˮӡ
    MenuItem dpatchworkItem;  //patchwork���ˮӡ
    MenuItem wlsbimageItem;   //lsbǶ��ͼ��ˮӡ
    MenuItem dlsbimageItem;   //lsb���ͼ��ˮӡ

    MenuItem walshItem;       //��Ƶ�㷨Ƕ�����ˮӡ
    MenuItem dwalshItem;      //��Ƶ�㷨������ˮӡ    
    MenuItem dmItem;          //DM�㷨Ƕ��ͼ��ˮӡ
    MenuItem ddmItem;         //DM�㷨��ȡͼ��ˮӡ
}
