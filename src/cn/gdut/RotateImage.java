package cn.gdut;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * 图像处理工具，用于图像的旋转，位移，固定大小切割
 */
public class RotateImage {

    /**
     * 画图
     * @param src 源图像
     * @param angel 旋转角度
     * @param width 生成图像宽度
     * @param height 生成图像高度
     * @param del_x 横坐标位移
     * @param del_y 纵坐标位移
     * @return 生成的图像
     */
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

    /**
     * 对模板图像进行旋转，位移，增加容错性
     * @param src 源图像
     * @param isTuxiang true 是源验证码中的图形 false 是文字或选项
     * @param des_width 生成图像宽度
     * @param des_height 生成图像高度
     * @return 处理后的模板图像列表
     * @throws Exception
     */
    public static List<BufferedImage> RotateMouldList(Image src, boolean isTuxiang, int des_width, int des_height) throws Exception {
        int[] rotateAngels;
        if (isTuxiang) {
            rotateAngels = new int[]{0, 90, 180, 270};
        } else {
            rotateAngels = new int[]{0, -3, 3};
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
            resList.add(drawImage(src, angel, des_width, des_height, -3, -3));
            resList.add(drawImage(src, angel, des_width, des_height, -3, -2));
            resList.add(drawImage(src, angel, des_width, des_height, -3, -1));
            resList.add(drawImage(src, angel, des_width, des_height, -3, -0));
            resList.add(drawImage(src, angel, des_width, des_height, -3, 1));
            resList.add(drawImage(src, angel, des_width, des_height, -3, 2));
            resList.add(drawImage(src, angel, des_width, des_height, -3, 3));
            resList.add(drawImage(src, angel, des_width, des_height, -2, -3));
            resList.add(drawImage(src, angel, des_width, des_height, -1, -3));
            resList.add(drawImage(src, angel, des_width, des_height, 0, -3));
            resList.add(drawImage(src, angel, des_width, des_height, 1, -3));
            resList.add(drawImage(src, angel, des_width, des_height, 2, -3));
            resList.add(drawImage(src, angel, des_width, des_height, 3, -3));
            resList.add(drawImage(src, angel, des_width, des_height, -2, 3));
            resList.add(drawImage(src, angel, des_width, des_height, -1, 3));
            resList.add(drawImage(src, angel, des_width, des_height, 0, 3));
            resList.add(drawImage(src, angel, des_width, des_height, 1, 3));
            resList.add(drawImage(src, angel, des_width, des_height, 2, 3));
            resList.add(drawImage(src, angel, des_width, des_height, 3, 3));
            resList.add(drawImage(src, angel, des_width, des_height, 3, -2));
            resList.add(drawImage(src, angel, des_width, des_height, 3, -1));
            resList.add(drawImage(src, angel, des_width, des_height, 3, 0));
            resList.add(drawImage(src, angel, des_width, des_height, 3, 1));
            resList.add(drawImage(src, angel, des_width, des_height, 3, 2));
        }
        return resList;
    }

    /**
     * 旋转图像
     * @param src 源图像
     * @param angel 角度
     * @param des_width 宽
     * @param des_height 高
     * @return 生成的图像
     * @throws Exception
     */
    public static BufferedImage Rotate(Image src, int angel, int des_width, int des_height) throws Exception {
        BufferedImage res0 = drawImage(src, angel, des_width, des_height, 0, 0);
        return res0;
    }
}