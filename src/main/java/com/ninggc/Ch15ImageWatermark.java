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
            cphpix;             //加密图像数组

    boolean loadflag = false,
            runflag = false; //图像处理执行标志

    String imn,               //图像文件名
            imh,               //加密图像标识"c_"
            wtr,
            title;             //图像标题

    ImageWatermark watermark;
    RAW reader;
    Common common;

    //    用于保存输出文件的上级目录
    String path = "./pic/";

    public Ch15ImageWatermark() {
        setTitle("数字图像处理-Java编程与实验 第15章 图像水印");
        this.setBackground(Color.lightGray);

        //菜单界面
        setMenu();

        watermark = new ImageWatermark();
        reader = new RAW();
        common = new Common();

        //关闭窗口
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

                    //取文件名
                    String filename = chooser.getSelectedFile().getName();

                    int len = filename.length();

                    //取扩展名
                    String exname = filename.substring(len - 3, len);

                    //取文件名imn
                    imn = filename.substring(0, len - 4);

                    //取加密标识imh
                    imh = filename.substring(0, 2);

                    if (exname.equalsIgnoreCase("raw")) {
                        pixels = reader.readRAW1D(name, iw, ih);
                        ImageProducer ip = new MemoryImageSource(iw, ih,
                                pixels, 0, iw);
                        iImage = createImage(ip);
                    } else {
                        //装载图像
                        iImage = common.openImage(name, tracker);
                        //取载入图像的宽和高
                        iw = iImage.getWidth(null);
                        ih = iImage.getHeight(null);
                        repaint();
                    }
                    if (!imh.equals("j_")) title = "原图";
                    else title = "受攻击";
                    loadflag = true;
                }
            }
        }
        //LSB混沌水印嵌入
        else if (evt.getSource() == wlsbchaosItem) {
            if (loadflag) {
                setTitle("第15章 图像水印 LSB混沌水印算法 作者 孙燮华");

                //设置密钥
                double x = (common.getParam("输入密钥(111-999)", "888")) / 1000.0;
                pixels = common.grabber(iImage, iw, ih);

                //生成混沌水印信息
                int[] water = watermark.chaos(x, iw, ih);

                //嵌入水印
                pixels = watermark.chaosEmbed(pixels, water, iw, ih);

                oImage = showPix(graph, pixels, iw, ih, "原图", "含水印图");
                runflag = true;

                //写入.raw含水印文件
                reader.writeRAW(path + "w_" + imn + ".raw", pixels, iw, ih);

                //存储含水印图像为JPEG
                File file = new File(path + "/j_" + imn + ".jpg");
                bImage = (BufferedImage) this.createImage(iw, ih);
                bImage.createGraphics().drawImage(oImage, 0, 0, this);
                common.saveImageAsJPEG(bImage, file);
                JOptionPane.showMessageDialog(null, "在目录images/ch15/jpg存储\n"
                        + "j_" + imn + ".jpg加密图成功!\n\n"
                        + "在目录images/ch15/water,\n"
                        + "保存含水印图w_" + imn + ".raw成功!");
            }

        }
        //LSB混沌水印检测
        else if (evt.getSource() == dlsbchaosItem) {
            if (!imh.equals("j_"))                //未受JPEG攻击
                pixels = reader.readRAW1D(path + "w_"
                        + imn + ".raw", iw, ih);
            else {                                    //受JPEG攻击
                pixels = common.grabber(iImage, iw, ih);
                for (int i = 0; i < iw * ih; i++)
                    pixels[i] = pixels[i] & 0xff;
            }
            //设置密钥
            double x = (common.getParam("输入密钥(111-999)", "888")) / 1000.0;

            //生成混沌水印信息
            int[] water = watermark.chaos(x, iw, ih);

            //检测水印, 显示结果
            double thresh = 0.265;
            double d = watermark.chaosDetect(pixels, water, iw, ih);
            if (d > thresh)
                JOptionPane.showMessageDialog(null, "检测到水印,\n d = " + d);
            else
                JOptionPane.showMessageDialog(null, "检测不到水印!!\n d = " + d);
            loadflag = false;
        }
        //patchwork水印嵌入
        else if (evt.getSource() == wpatchworkItem) {
            if (loadflag) {
                setTitle("第15章 图像水印 Patchwork水印算法 作者 孙燮华");

                pixels = common.grabber(iImage, iw, ih);

                //嵌入水印
                pixels = watermark.patchworkEmbed(pixels, iw, ih, 3);
                showPix(graph, pixels, iw, ih, "原图", "含水印图");
                runflag = true;

                //写入.raw含水印文件
//                reader.writeRAW(path + "w_" + imn +
//                        ".raw", pixels, iw, ih);
                String name = "./pic/w_" + imn + ".raw";
                reader.writeRAW(name, pixels, iw, ih);


//                查看目录
//                File directory = new File("");//设定为当前文件夹
//                try{
//                    System.out.println(directory.getCanonicalPath());//获取标准的路径
//                    System.out.println(directory.getAbsolutePath());//获取绝对路径
//                }catch (IOException e) {
//                    e.printStackTrace();
//                }


                JOptionPane.showMessageDialog(null,
                        "保存含水印图像" + name + ".raw成功!");
            } else
                JOptionPane.showMessageDialog(null, "请先打开一幅图!");

        }
        //patchwork水印检测
        else if (evt.getSource() == dpatchworkItem) {
            String name = "./pic/w_" + imn + ".raw";
            pixels = reader.readRAW1D(name, iw, ih);
            ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
            oImage = createImage(ip);
            pixels = common.grabber(oImage, iw, ih);

            //检测水印, 显示结果
            double thresh = 1.8 * 3;
            double d = watermark.patchworkDetect(pixels, iw, ih);
            if (d > thresh)
                JOptionPane.showMessageDialog(null, "检测到水印,\n d = " + d);
            else
                JOptionPane.showMessageDialog(null, "检测不到水印!!\n d = " + d);
        }
        //lsb图像水印嵌入
        else if (evt.getSource() == wlsbimageItem) {
            if (loadflag) {
                setTitle("第15章 图像水印 LSB图像水印 作者 孙燮华");

                String watername = "Bigwater.jpg";
//                String watername = "Bigwater.png";
                pixels = common.grabber(common.openImage(
                        path + watername, tracker), iw, ih);

                //读入水印图像
                int[] water = watermark.readWater(pixels, iw, ih);

                pixels = common.grabber(iImage, iw, ih);

                //嵌入水印
                pixels = watermark.chaosEmbed(pixels, water, iw, ih);
                showPix(graph, pixels, iw, ih, "原图", "含水印图");
                runflag = true;

                //写入.raw含水印文件
                reader.writeRAW(path + "w_" + imn +
                        ".raw", pixels, iw, ih);

                JOptionPane.showMessageDialog(null, "在目录images/ch15/water,\n"
                        + "保存含水印图像w_" + imn + ".raw成功!");
            } else
                JOptionPane.showMessageDialog(null, "请先打开一幅图!");

        }
        //LSB图像水印提取
        else if (evt.getSource() == dlsbimageItem) {
            pixels = reader.readRAW1D(path + "w_" + imn + ".raw", iw, ih);
            ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
            oImage = createImage(ip);

            pixels = common.grabber(oImage, iw, ih);

            //提取水印图像
            pixels = watermark.getWater(pixels, iw, ih);
            showPix(graph, pixels, iw, ih, "原图", "取出水印");
            runflag = true;
        }



        //扩频水印嵌入
        else if (evt.getSource() == walshItem) {
            if (loadflag) {
                setTitle("第15章 图像水印 扩频水印算法 作者 孙燮华");
                pixels = common.grabber(iImage, iw, ih);

//                嵌入水印
                pixels = watermark.embedWatermark(pixels, iw, ih);

                oImage = showPix(graph, pixels, iw, ih, "原图", "含水印图");

                //写入.raw含水印文件
                reader.writeRAW(path + "w_" + imn +
                        ".raw", pixels, iw, ih);

                JOptionPane.showMessageDialog(null,
                        "在目录images/ch15/water,\n" +
                                "保存含水印图像w_" + imn + ".raw成功!");
            } else
                JOptionPane.showMessageDialog(null, "请先打开一幅图!");
        }
        //扩频水印检测
        else if (evt.getSource() == dwalshItem) {
            pixels = reader.readRAW1D(path + "w_"
                    + imn + ".raw", iw, ih);

//            计算相似度
            double sim = watermark.getSimValue(iImage, pixels, iw, ih);
            JOptionPane.showMessageDialog(null, "相似度 sim = " + sim);
        }


//        抖动调制DM（Dither Modulation）
//        量化索引调制Quantization Index Modulation
        //DM图像水印嵌入
        else if (evt.getSource() == dmItem) {
            if (loadflag) {
                setTitle("第15章 图像水印 DM水印算法 作者 孙燮华");
                pixels = common.grabber(iImage, iw, ih);

                double[][] outIm;

                //读入水印图像
                String waterimage = "water.jpg";
                String waterimage2 = "Bigwater_1.png";
                pixels = common.grabber(common.openImage(
                        path + waterimage, tracker), 32, 32);
                int[] water = watermark.readImWater(pixels, 32, 32);

                pixels = common.grabber(iImage, iw, ih);

                //嵌入水印
                outIm = watermark.dmEmbed(pixels, water, iw, ih);

                for (int j = 0; j < ih; j++) {
                    for (int i = 0; i < iw; i++) {
                        int tem = (int) outIm[i][j];
                        if (tem > 255) tem = 255;
                        if (tem < 0) tem = 0;
                        pixels[i + j * iw] = (255 << 24) | (tem << 16) | (tem << 8) | tem;
                    }
                }
                oImage = showPix(graph, pixels, iw, ih, "原图", "含水印图");

                reader.writeRAW(path + "w_" + imn +
                        ".raw", pixels, iw, ih);

                //存储含水印图像为JPEG
                File file = new File(path + "/j_" + imn + ".jpg");
                bImage = (BufferedImage) this.createImage(iw, ih);
                bImage.createGraphics().drawImage(oImage, 0, 0, this);
                common.saveImageAsJPEG(bImage, file);
                JOptionPane.showMessageDialog(null, "在目录images/ch15/jpg存储\n"
                        + "j_" + imn + ".jpg加密图成功!\n\n"
                        + "在目录images/ch15/water,\n"
                        + "保存含水印图w_" + imn + ".raw成功!");
            }
        }
        //DM图像水印提取
        else if (evt.getSource() == ddmItem) {
            if (!imh.equals("j_"))                //未受JPEG攻击
            {
                pixels = reader.readRAW1D(path + "w_"
                        + imn + ".raw", iw, ih);
                int[] water = watermark.getImageWater(pixels, iw, ih);
                showPix(graph, water, 32, 32, "含水印图", "取出水印");
            } else {                                    //受JPEG攻击
                pixels = common.grabber(iImage, iw, ih);
                for (int i = 0; i < iw * ih; i++)
                    pixels[i] = pixels[i] & 0xff;
                int[] water = watermark.getImageWater(pixels, iw, ih);
                showPix(graph, water, 32, 32, "受攻击", "取出水印");
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
        //将数组中的象素产生一个图像
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

    //菜单界面
    public void setMenu() {
        Menu fileMenu = new Menu("文件");
        openItem = new MenuItem("打开");
        openItem.addActionListener(this);
        fileMenu.add(openItem);

        exitItem = new MenuItem("退出");
        exitItem.addActionListener(this);
        fileMenu.add(exitItem);

        Menu spwaterMenu = new Menu("空域水印");

        wlsbchaosItem = new MenuItem("LSB混沌水印嵌入");
        wlsbchaosItem.addActionListener(this);
        spwaterMenu.add(wlsbchaosItem);

        dlsbchaosItem = new MenuItem("LSB混沌水印检测");
        dlsbchaosItem.addActionListener(this);
        spwaterMenu.add(dlsbchaosItem);
        spwaterMenu.addSeparator();

        wpatchworkItem = new MenuItem("Patchwork水印嵌入");
        wpatchworkItem.addActionListener(this);
        spwaterMenu.add(wpatchworkItem);

        dpatchworkItem = new MenuItem("Patchwork水印检测");
        dpatchworkItem.addActionListener(this);
        spwaterMenu.add(dpatchworkItem);
        spwaterMenu.addSeparator();

        wlsbimageItem = new MenuItem("LSB图像水印嵌入");
        wlsbimageItem.addActionListener(this);
        spwaterMenu.add(wlsbimageItem);

        dlsbimageItem = new MenuItem("LSB图像水印提取");
        dlsbimageItem.addActionListener(this);
        spwaterMenu.add(dlsbimageItem);

        Menu frwaterMenu = new Menu("频域水印");
        walshItem = new MenuItem("扩频水印嵌入");
        walshItem.addActionListener(this);
        frwaterMenu.add(walshItem);

        dwalshItem = new MenuItem("扩频水印检测");
        dwalshItem.addActionListener(this);
        frwaterMenu.add(dwalshItem);
        frwaterMenu.addSeparator();

        dmItem = new MenuItem("DM图像水印嵌入");
        dmItem.addActionListener(this);
        frwaterMenu.add(dmItem);

        ddmItem = new MenuItem("DM图像水印提取");
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

    MenuItem wlsbchaosItem;   //lsb嵌入混沌水印
    MenuItem dlsbchaosItem;   //lsb检测混沌水印
    MenuItem wpatchworkItem;  //patchwork嵌入水印
    MenuItem dpatchworkItem;  //patchwork检测水印
    MenuItem wlsbimageItem;   //lsb嵌入图像水印
    MenuItem dlsbimageItem;   //lsb检测图像水印

    MenuItem walshItem;       //扩频算法嵌入混沌水印
    MenuItem dwalshItem;      //扩频算法检测混沌水印    
    MenuItem dmItem;          //DM算法嵌入图像水印
    MenuItem ddmItem;         //DM算法提取图像水印
}
