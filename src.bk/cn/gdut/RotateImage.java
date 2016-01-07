package cn.gdut;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RotateImage {

    private static BufferedImage drawImage(Image src, int angel, int width, int height, int del_x, int del_y) {
        BufferedImage res = null;
        res = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = res.createGraphics();
        Color old = g2.getColor();
        g2.setPaint(Color.WHITE);
        g2.fillRect(0, 0, width, height);
        g2.setColor(old);
        // transform
        g2.translate((width - src.getWidth(null)) / 2 + del_x,
                (height - src.getHeight(null)) / 2 + del_y);
        g2.rotate(Math.toRadians(angel), src.getWidth(null) / 2, src.getHeight(null) / 2);
        g2.drawImage(src, null, null);
        return res;
    }

    public static List<BufferedImage> RotateTestList(Image src, boolean isTuxiang, int des_width, int des_height) throws Exception {
        int[] rotateAngels;
        if (isTuxiang) {
            rotateAngels = new int[]{0, 90, 180, 270};
//            rotateAngels = new int[359];
//            for(int i=1;i<=359;i++){
//                rotateAngels[i-1] = i;
//            }
        } else {
            rotateAngels = new int[]{0};
        }
        List<BufferedImage> resList = new ArrayList<BufferedImage>();
        for (int angel: rotateAngels) {
            BufferedImage res0 = drawImage(src, angel, des_width, des_height, 0, 0);
            BufferedImage res1 = drawImage(src, angel, des_width, des_height, 0, -1);
            BufferedImage res2 = drawImage(src, angel, des_width, des_height, 0, 1);
            BufferedImage res3 = drawImage(src, angel, des_width, des_height, 1, -1);
            BufferedImage res4 = drawImage(src, angel, des_width, des_height, 1, 0);
            BufferedImage res5 = drawImage(src, angel, des_width, des_height, 1, 1);
            BufferedImage res6 = drawImage(src, angel, des_width, des_height, -1, -1);
            BufferedImage res7 = drawImage(src, angel, des_width, des_height, -1, 0);
            BufferedImage res8 = drawImage(src, angel, des_width, des_height, -1, 1);
            BufferedImage res9 = drawImage(src, angel, des_width, des_height, -2, -2);
            BufferedImage res10 = drawImage(src, angel, des_width, des_height, -1, -2);
            BufferedImage res11 = drawImage(src, angel, des_width, des_height, 0, -2);
            BufferedImage res12 = drawImage(src, angel, des_width, des_height, 1, -2);
            BufferedImage res13 = drawImage(src, angel, des_width, des_height, 2, -2);
            BufferedImage res14 = drawImage(src, angel, des_width, des_height, -2, -1);
            BufferedImage res15 = drawImage(src, angel, des_width, des_height, -2, 0);
            BufferedImage res16 = drawImage(src, angel, des_width, des_height, -2, 1);
            BufferedImage res17 = drawImage(src, angel, des_width, des_height, -2, 2);
            BufferedImage res18 = drawImage(src, angel, des_width, des_height, 2, 2);
            BufferedImage res19 = drawImage(src, angel, des_width, des_height, 2, -1);
            BufferedImage res20 = drawImage(src, angel, des_width, des_height, 2, 0);
            BufferedImage res21 = drawImage(src, angel, des_width, des_height, 2, 1);
            BufferedImage res22 = drawImage(src, angel, des_width, des_height, 2, 2);
            BufferedImage res23 = drawImage(src, angel, des_width, des_height, -1, 2);
            BufferedImage res24 = drawImage(src, angel, des_width, des_height, 0, 2);
            BufferedImage res25 = drawImage(src, angel, des_width, des_height, 1, 2);
            resList.add(res0);
            resList.add(res1);
            resList.add(res2);
            resList.add(res3);
            resList.add(res4);
            resList.add(res5);
            resList.add(res6);
            resList.add(res7);
            resList.add(res8);
            resList.add(res9);
            resList.add(res10);
            resList.add(res11);
            resList.add(res12);
            resList.add(res13);
            resList.add(res14);
            resList.add(res15);
            resList.add(res16);
            resList.add(res17);
            resList.add(res18);
            resList.add(res19);
            resList.add(res20);
            resList.add(res21);
            resList.add(res22);
            resList.add(res23);
            resList.add(res24);
            resList.add(res25);
        }
        return resList;
    }


    public static List<BufferedImage> RotateList(Image src, int angel, int des_width, int des_height) throws Exception {
        //int src_width = src.getWidth(null);
        //int src_height = src.getHeight(null);
        // calculate the new image size
        // Rectangle rect_des = CalcRotatedSize(new Rectangle(new Dimension(
        //         src_width, src_height)), angel);
        //Rectangle rect_des = new java.awt.Rectangle(new Dimension(des_width, des_height));

        List<BufferedImage> resList = new ArrayList<BufferedImage>();
        BufferedImage res0 = drawImage(src, angel, des_width, des_height, 0, 0);
        BufferedImage res1 = drawImage(src, angel, des_width, des_height, 0, -1);
        BufferedImage res2 = drawImage(src, angel, des_width, des_height, 0, 1);
        BufferedImage res3 = drawImage(src, angel, des_width, des_height, 1, -1);
        BufferedImage res4 = drawImage(src, angel, des_width, des_height, 1, 0);
        BufferedImage res5 = drawImage(src, angel, des_width, des_height, 1, 1);
        BufferedImage res6 = drawImage(src, angel, des_width, des_height, -1, -1);
        BufferedImage res7 = drawImage(src, angel, des_width, des_height, -1, 0);
        BufferedImage res8 = drawImage(src, angel, des_width, des_height, -1, 1);
        resList.add(res0);
        resList.add(res1);
        resList.add(res2);
        resList.add(res3);
        resList.add(res4);
        resList.add(res5);
        resList.add(res6);
        resList.add(res7);
        resList.add(res8);

//        ImageIO.write(res0, "PNG", new File("data\\tmp\\tu0.png"));
//        ImageIO.write(res1, "PNG", new File("data\\tmp\\tu1.png"));
//        ImageIO.write(res2, "PNG", new File("data\\tmp\\tu2.png"));
//        ImageIO.write(res3, "PNG", new File("data\\tmp\\tu3.png"));
//        ImageIO.write(res4, "PNG", new File("data\\tmp\\tu4.png"));
//        ImageIO.write(res5, "PNG", new File("data\\tmp\\tu5.png"));
//        ImageIO.write(res6, "PNG", new File("data\\tmp\\tu6.png"));
//        ImageIO.write(res7, "PNG", new File("data\\tmp\\tu7.png"));
//        ImageIO.write(res8, "PNG", new File("data\\tmp\\tu8.png"));
        return resList;
    }

    public static BufferedImage Rotate(Image src, int angel, int des_width, int des_height) throws Exception {
        BufferedImage res0 = drawImage(src, angel, des_width, des_height, 0, 0);
        return res0;
    }

    public static Rectangle CalcRotatedSize(Rectangle src, int angel) {
        // if angel is greater than 90 degree, we need to do some conversion
        if (angel >= 90) {
            if(angel / 90 % 2 == 1){
                int temp = src.height;
                src.height = src.width;
                src.width = temp;
            }
            angel = angel % 90;
        }

        double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
        double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
        double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
        double angel_dalta_width = Math.atan((double) src.height / src.width);
        double angel_dalta_height = Math.atan((double) src.width / src.height);

        int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha
                - angel_dalta_width));
        int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha
                - angel_dalta_height));
        int des_width = src.width + len_dalta_width * 2;
        int des_height = src.height + len_dalta_height * 2;
        return new Rectangle(new Dimension(des_width, des_height));
    }
}