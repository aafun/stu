package cn.gdut;

import sun.awt.image.BufferedImageDevice;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by lenovo on 2015/11/25.
 */
public class StuTool {
    private BufferedImage bi = null;

    public StuTool() {
    }

    public StuTool(String filename) {
        try {
            this.bi = ImageIO.read(new File(filename));
        } catch (Exception e) {
            System.out.println("Image File Not Found!");
            e.printStackTrace();
        }
    }

    public void setImage(String filename) {
        try {
            this.bi = ImageIO.read(new File(filename));
            // testRead();
        } catch (Exception e) {
            System.out.println("Image File Not Found!");
            e.printStackTrace();
        }
    }

    public BufferedImage getImage(){
        return bi;
    }

    private void testRead() throws Exception{
        ImageIO.read(new File("pic\\2.png"));
    }


    private void train(){

    }

    public String stu() {
        if (this.bi == null) {
            System.out.println("Image File Not Found!");
            return "";
        }
        try {
            BufferedImage img = removeBackground(this.bi);
            //ImageIO.write(img, "PNG", new File("result_" + filename));
            List<BufferedImage> lbi = splitImage(img);
            return "";
        } catch (Exception e) {
            System.out.println("Failed to identify the image!");
            e.printStackTrace();
            return "";
        }
    }

    private List<BufferedImage> splitImage(BufferedImage img) {
        List<BufferedImage> limag = new ArrayList<BufferedImage>();
        int width = img.getWidth();
        int height = img.getHeight();
        List<Integer> xlist = new ArrayList<Integer>();
        for (int i = 0; i < width; ++i) {
            int count = 0;
            for (int j = 0; j < height; ++j) {
                if (img.getRGB(i, j) == Color.BLACK.getRGB()) {
                    count++;
                }
            }
            xlist.add(count);
        }
        /*for (int i = 0; i < xlist.size(); ++i) {
            System.out.print(xlist.get(i) + " ");
        }
        System.out.println();*/

        return null;
    }

    private int isBlack(int pixRGB) {
        if (pixRGB == Color.black.getRGB()) {
            return 1;
        }
        return 0;
    }

    class CleanedBufferedImage {
        public BufferedImage cbi = null;
        public int count = 0;
        public CleanedBufferedImage(BufferedImage bi, int count) {
            this.cbi = bi;
            this.count = count;
        }
        public BufferedImage getCbi() {
            return this.cbi;
        }
        public int getCount() {
            return this.count;
        }
    }

    private CleanedBufferedImage clean(BufferedImage bi, int threshold) throws Exception {
        BufferedImage img_cache = new BufferedImage(bi.getWidth(), bi.getHeight(), bi.getType());
        img_cache.setData(bi.getData());
        int width = bi.getWidth();
        int height = bi.getHeight();
        int count = 0;
        if (isBlack(img_cache.getRGB(0, 0)) == 1 && (isBlack(img_cache.getRGB(0, 1)) + isBlack(img_cache.getRGB(1, 1)) + isBlack(img_cache.getRGB(1, 0)) <=threshold)) {
            bi.setRGB(0, 0, Color.WHITE.getRGB());
            count++;
        }
        if (isBlack(img_cache.getRGB(0, height - 1)) == 1 && (isBlack(img_cache.getRGB(0, height - 2)) + isBlack(img_cache.getRGB(1, height - 1)) + isBlack(img_cache.getRGB(1, height - 2)) <=threshold)) {
            bi.setRGB(0, height - 1, Color.WHITE.getRGB());
            count++;
        }
        if (isBlack(img_cache.getRGB(width - 1, 0)) == 1 && (isBlack(img_cache.getRGB(width - 2, 0)) + isBlack(img_cache.getRGB(width - 2, 1)) + isBlack(img_cache.getRGB(width - 1, 1)) <=threshold)) {
            bi.setRGB(width - 1, 0, Color.WHITE.getRGB());
            count++;
        }
        if (isBlack(img_cache.getRGB(width - 1, height - 1)) == 1 &&
                (isBlack(img_cache.getRGB(width - 2, height - 1)) +
                        isBlack(img_cache.getRGB(width - 2, height - 2)) +
                        isBlack(img_cache.getRGB(width - 1, height - 2)) <= threshold
                )) {
            bi.setRGB(width - 1, height - 1, Color.WHITE.getRGB());
            count++;
        }

        for (int y = 1; y < height - 1; ++y) {
            if (isBlack(img_cache.getRGB(0, y)) == 1) {
                int blackCount = isBlack(img_cache.getRGB(0, y - 1)) + isBlack(img_cache.getRGB(0, y + 1)) +
                        isBlack(img_cache.getRGB(1, y - 1)) + isBlack(img_cache.getRGB(1, y)) + isBlack(img_cache.getRGB(1, y + 1));
                if (blackCount <= threshold) {
                    count++;
                    bi.setRGB(0, y, Color.WHITE.getRGB());
                }
            }
        }
        for (int y = 1; y < height - 1; ++y) {
            if (isBlack(img_cache.getRGB(width - 1, y)) == 1) {
                int blackCount = isBlack(img_cache.getRGB(width - 1, y - 1)) + isBlack(img_cache.getRGB(width - 1, y + 1)) +
                        isBlack(img_cache.getRGB(width - 2, y - 1)) + isBlack(img_cache.getRGB(width - 2, y)) + isBlack(img_cache.getRGB(width - 2, y + 1));
                if (blackCount <= threshold) {
                    count++;
                    bi.setRGB(width - 1, y, Color.WHITE.getRGB());
                }
            }
        }
        for (int x = 1; x < width - 1; ++x) {
            if (isBlack(img_cache.getRGB(x, 0)) == 1) {
                int blackCount = isBlack(img_cache.getRGB(x - 1, 0)) + isBlack(img_cache.getRGB(x + 1, 0)) +
                        isBlack(img_cache.getRGB(x, 1)) + isBlack(img_cache.getRGB(x - 1, 1)) + isBlack(img_cache.getRGB(x + 1, 1));
                if (blackCount <= threshold) {
                    count++;
                    bi.setRGB(x, 0, Color.WHITE.getRGB());
                }
            }
        }
        for (int x = 1; x < width - 1; ++x) {
            if (isBlack(img_cache.getRGB(x, height - 1)) == 1) {
                int blackCount = isBlack(img_cache.getRGB(x - 1, height - 1)) + isBlack(img_cache.getRGB(x + 1, height - 1)) +
                        isBlack(img_cache.getRGB(x, height - 2)) + isBlack(img_cache.getRGB(x - 1, height - 2)) + isBlack(img_cache.getRGB(x + 1, height - 2));
                if (blackCount <= threshold) {
                    count++;
                    bi.setRGB(x, height - 1, Color.WHITE.getRGB());
                }
            }
        }


        for (int x = 1; x < width - 1; ++x) {
            for (int y = 1; y < height - 1; ++y) {
                if (img_cache.getRGB(x, y) == Color.BLACK.getRGB()) {
                    if (isBlack(img_cache.getRGB(x - 1, y)) + isBlack(img_cache.getRGB(x - 1, y - 1)) +
                            isBlack(img_cache.getRGB(x, y - 1)) + isBlack(img_cache.getRGB(x + 1, y - 1)) +
                            isBlack(img_cache.getRGB(x + 1, y)) + isBlack(img_cache.getRGB(x + 1, y + 1)) +
                            isBlack(img_cache.getRGB(x, y + 1)) + isBlack(img_cache.getRGB(x - 1, y + 1)) <= threshold) {
                        bi.setRGB(x, y, Color.WHITE.getRGB());
                        count++;
                    }
                }
            }
        }
        CleanedBufferedImage result = new CleanedBufferedImage(bi, count);
        return result;
    }

    private int[] getEdge(BufferedImage bi) {
        int w = bi.getWidth();
        int h = bi.getHeight();
        int x1 = w - 1, x2 = 0, y1 = h - 1, y2 = 0;
        for (int x = 0; x < w; ++x) {
            for (int y = 0; y < h; ++y) {
                if (isBlack(bi.getRGB(x, y)) == 1) {
                    if (x < x1) x1 = x;
                    if (x > x2) x2 = x;
                    if (y < y1) y1 = y;
                    if (y > y2) y2 = y;
                }
            }
        }
        int res[] = {x1, x2, y1, y2};
        return res;
    }

    private BufferedImage rotateClipScale(BufferedImage bi, boolean rotate, int width, int height) throws Exception {
        int res[] = getEdge(bi);
        int x1 = res[0];
        int x2 = res[1];
        int y1 = res[2];
        int y2 = res[3];
        BufferedImage subBi = bi.getSubimage(x1, y1, x2 - x1 + 1, y2 - y1 + 1);
        ImageIO.write(subBi, "PNG", new File("data\\" + "_bi.png"));
        RotateImage.Rotate(subBi, 15, width, height);
//        BufferedImage rotBi = RotateImage.Rotate(subBi, 0, width, height);
//        if (rotate) {
//            for (int i=90; i >-90; --i) {
//                BufferedImage _bi = RotateImage.Rotate(subBi, i, width, height);
//                int _res[] = getEdge(_bi);
//                if (_res[1] - _res[0] < x2 - x1) {
//                    rotBi = _bi;
//                    x2 = _res[1];
//                    x1 = _res[0];
//                }
//            }
//            ImageIO.write(rotBi, "PNG", new File("data\\" + "_bi_.png"));
//        }

        //return rotBi;
        return null;
    }


    private List<BufferedImage> splitCandidate(BufferedImage bi) {
        List<BufferedImage> res = new ArrayList<BufferedImage>();
        int width = bi.getWidth();
        int height = bi.getHeight();
        int xpixel[] = new int[width];
        boolean first_pixel = true;
        boolean last_pixel = false;
        int x1, x2, y1, y2;
        for (int x = 0; x < width; ++x) {
            xpixel[x] = 0;
            for (int y = 0; y < height; ++y) {
                if (isBlack(bi.getRGB(x, y)) == 1) {
                    xpixel[x]++;
                    if (first_pixel) {
                        x1 = x;
                        y1 = y;
                        first_pixel = false;
                    } else if (!last_pixel) {
                        x2 = x;
                        y2 = y;
                    }
                }
            }
            if (!first_pixel && xpixel[x] == 0) last_pixel = true;
        }

        return null;
    }

    private BufferedImage removeBackground(BufferedImage bi) throws Exception {
        BufferedImage img = bi;
        int width = img.getWidth();
        int height = img.getHeight();
        ImageIO.write(img, "PNG", new File("data\\" + "__1.png"));

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                int rgb = img.getRGB(x, y);
                int r = (rgb & 0xff0000) >> 16;
                int g = (rgb & 0xff00) >> 8;
                int b = (rgb & 0xff);
                if (r < 150 && g < 150 && b < 150) {
                    img.setRGB(x, y, Color.BLACK.getRGB());
                } else {
                    img.setRGB(x, y, Color.WHITE.getRGB());
                }
            }
        }

        ImageIO.write(img, "PNG", new File("data\\" + "1.png"));
        BufferedImage img1 = img.getSubimage(0, 0, 60, 60);
        BufferedImage img2 = img.getSubimage(60, 0, 60, 60);
        BufferedImage img3 = img.getSubimage(120, 0, 60, 60);
        CleanedBufferedImage _cbi1 = clean(img1, 1);
        CleanedBufferedImage _cbi2 = clean(img2, 1);
        CleanedBufferedImage _cbi3 = clean(img3, 1);

        CleanedBufferedImage cbi1 = clean(_cbi1.getCbi(), 0);
        CleanedBufferedImage cbi2 = clean(_cbi2.getCbi(), 0);
        CleanedBufferedImage cbi3 = clean(_cbi3.getCbi(), 0);

        ImageIO.write(cbi1.getCbi(), "PNG", new File("data\\" + "_s1_.png"));
        System.out.println(_cbi1.getCount());
        int tuCount = _cbi1.getCount();
        BufferedImage tu = cbi1.getCbi();
        BufferedImage zi1 = cbi2.getCbi();
        BufferedImage zi2 = cbi3.getCbi();

        ImageIO.write(cbi2.getCbi(), "PNG", new File("data\\" + "_s2_.png"));
        System.out.println(_cbi2.getCount());
        if (tuCount > _cbi2.getCount()) {
            tu = cbi2.getCbi();
            tuCount = cbi2.getCount();
            zi1 = cbi1.getCbi();
            zi2 = cbi3.getCbi();
        }

        ImageIO.write(cbi3.getCbi(), "PNG", new File("data\\" + "_s3_.png"));
        System.out.println(_cbi3.getCount());
        if (tuCount > _cbi3.getCount()) {
            tu = cbi3.getCbi();
            tuCount = cbi3.getCount();
            zi1 = cbi1.getCbi();
            zi2 = cbi2.getCbi();
        }

        BufferedImage tuxiang = rotateClipScale(tu, true, 120, 120);
        //ImageIO.write(tuxiang, "PNG", new File("data\\tmp\\" + "tu.png"));

        BufferedImage zi11 = zi1.getSubimage(0, 0, zi1.getWidth(), zi1.getHeight() / 2);
        BufferedImage zi12 = zi1.getSubimage(0, zi1.getHeight() / 2, zi1.getWidth(), zi1.getHeight() / 2);
        BufferedImage zi21 = zi2.getSubimage(0, 0, zi2.getWidth(), zi2.getHeight() / 2);
        BufferedImage zi22 = zi2.getSubimage(0, zi2.getHeight() / 2, zi2.getWidth(), zi2.getHeight() / 2);



        return img;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("StuTool Main Method!");
        StuTool st = new StuTool();
        st.setImage("F:\\BaiduYunDownload\\pic\\pic\\28.png");
        st.stu();
    }
}
