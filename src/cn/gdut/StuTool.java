package cn.gdut;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 验证识别主工具类
 */
public class StuTool {

    /** 待识别的验证码图像 */
    private BufferedImage bi = null;
    /** 图形部分模板 */
    private Map<String, int[]> tuxiang_mould = null;
    /** 选项部分模板 */
    private Map<String, int[]> xuanxiang_mould = null;
    /** 文字部分模板 */
    private Map<String, int[]> wenzi_mould = null;
    /** 所有模板，包括图形文字选项 */
    private List<Map<String, int[]>> moulds = null;
    /** 默认生成的图像宽度 */
    public static int IMAGE_WIDTH = 75;
    /** 默认生成的图像高度 */
    public static int IMAGE_HEIGHT = 75;


    /**
     * 默认首先读取本地模板文件 - mould.file
     * 如果没有本地模板文件，则重新训练样本，需要耗时较长，训练后会生成mould.file
     */
    public StuTool() {
        File mould_file = new File("mould.file");
        if (!mould_file.exists()) {
            try {
                moulds = gen_moulds();
            } catch (Exception e) {
                System.out.println("Mould Build Failed!!!");
                e.printStackTrace();
            }
            ObjectFileConvert.object2File(moulds, "mould.file");
        } else {
            moulds = (List<Map<String, int[]>>)ObjectFileConvert.file2Object("mould.file");
        }
        this.tuxiang_mould = moulds.get(0);
        this.xuanxiang_mould = moulds.get(1);
        this.wenzi_mould = moulds.get(2);
    }

    /**
     * 可直接传入待识别的验证码图像，其余同StuTool()
     * @param filename 待识别的验证码图像文件路径
     * @see StuTool
     */
    public StuTool(String filename) {
        File mould_file = new File("mould.file");
        if (!mould_file.exists()) {
            try {
                moulds = gen_moulds();
            } catch (Exception e) {
                System.out.println("Mould Build Failed!!!");
                e.printStackTrace();
            }
            ObjectFileConvert.object2File(moulds, "mould.file");
        } else {
            moulds = (List<Map<String, int[]>>)ObjectFileConvert.file2Object("mould.file");
        }
        try {
            this.bi = ImageIO.read(new File(filename));
            this.tuxiang_mould = moulds.get(0);
            this.xuanxiang_mould = moulds.get(1);
            this.wenzi_mould = moulds.get(2);
        } catch (Exception e) {
            System.out.println("Image File Not Found or Mould Build FailedSS!");
            e.printStackTrace();
        }
    }

    /**
     * 设置待识别图像
     * @param filename 文件名
     */
    public void setImage(String filename) {
        try {
            this.bi = ImageIO.read(new File(filename));
            // testRead();
        } catch (Exception e) {
            System.out.println("Image File Not Found!");
            e.printStackTrace();
        }
    }

    /**
     * 获取待识别验证码图像
     * @return 验证码图像
     */
    public BufferedImage getImage() {
        return bi;
    }

    /**
     * 此方法用于生成训练样本，实质是对验证码进行切割（切分为图形、选项、文字），进而进行降噪、旋转、同高宽处理
     * 会在图像所在目录下生成：
     * tuxiang/存放处理后的图形
     * wenzi/ 存放处理后的文字图片
     * xuanxiang/ 存放处理后的选项图片
     * @param pic_path 必须是验证码存放的目录
     */
    public void genTrainData(String pic_path) {
        File pic_dir = new File(pic_path);
        if (!pic_dir.exists() || !pic_dir.isDirectory()) {
            System.out.println("The training picture files directory is not correct! Please check it!");
            return;
        }
        File[] pic_files = pic_dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isFile();
            }
        });
        if (pic_files.length == 0) {
            System.out.println("No picture file is found under " + pic_path + " ! Please check it!");
            return;
        }
        String tuxiang_path = "";
        String wenzi_path = "";
        String zimushuzi_path = "";
        if (!pic_path.endsWith(File.separator)) {
            tuxiang_path = pic_path + File.separator + "tuxiang" + File.separator;
            wenzi_path = pic_path + File.separator + "wenzi" + File.separator;
            zimushuzi_path = pic_path + File.separator + "xuanxiang" + File.separator;
        } else {
            tuxiang_path = pic_path + "tuxiang" + File.separator;
            wenzi_path = pic_path + "zhongwen" + File.separator;
            zimushuzi_path = pic_path + "zimushuzi" + File.separator;
        }
        if (!(new File(tuxiang_path)).exists() || !(new File(tuxiang_path)).isDirectory()) {
            new File(tuxiang_path).mkdirs();
        }
        if (!(new File(wenzi_path)).exists() || !(new File(tuxiang_path)).isDirectory()) {
            new File(wenzi_path).mkdirs();
        }
        if (!(new File(zimushuzi_path)).exists() || !(new File(tuxiang_path)).isDirectory()) {
            new File(zimushuzi_path).mkdirs();
        }
        for (int i = 0; i < pic_files.length; ++i) {
            File pic_file = pic_files[i];
            String pic_file_name = pic_file.getName();
            BufferedImage img;
            List<List<BufferedImage>> res_imgs;
            System.out.println("Processing: " + pic_file.getAbsolutePath() + " ...");
            try {
                img = ImageIO.read(pic_file);
                res_imgs = regenImages(img);
            } catch (Exception e) {
                System.out.println("Exception appear when read(pic_file) or regenImages(img)");
                e.printStackTrace();
                continue;
            }
            try {
                ImageIO.write(res_imgs.get(0).get(0), "PNG", new File(tuxiang_path + "tu_" + pic_file_name  + ".png"));
                ImageIO.write(res_imgs.get(1).get(0), "PNG", new File(zimushuzi_path + "xx_" + pic_file_name + "_1" + ".png"));
                ImageIO.write(res_imgs.get(2).get(0), "PNG", new File(zimushuzi_path + "xx_" + pic_file_name + "_2" + ".png"));
                ImageIO.write(res_imgs.get(3).get(0), "PNG", new File(zimushuzi_path + "xx_" + pic_file_name + "_3" + ".png"));
                ImageIO.write(res_imgs.get(4).get(0), "PNG", new File(zimushuzi_path + "xx_" + pic_file_name + "_4" + ".png"));
                ImageIO.write(res_imgs.get(1).get(1), "PNG", new File(wenzi_path + "wz_" + pic_file_name + "_1" + ".png"));
                ImageIO.write(res_imgs.get(2).get(1), "PNG", new File(wenzi_path + "wz_" + pic_file_name + "_2" + ".png"));
                ImageIO.write(res_imgs.get(3).get(1), "PNG", new File(wenzi_path + "wz_" + pic_file_name + "_3" + ".png"));
                ImageIO.write(res_imgs.get(4).get(1), "PNG", new File(wenzi_path + "wz_" + pic_file_name + "_4" + ".png"));
            } catch (Exception e) {
                System.out.println("Exception appear when ImageIo.write()");
                e.printStackTrace();
                continue;

            }
        }
    }

    /**
     * 进行识别
     * 注意得先设置待识别的验证码
     * @return 识别结果
     */
    public String stu() {
        if (this.bi == null) {
            System.out.println("Image File Not Found!");
            return "";
        }
        String result = "";
        try {
            List<List<BufferedImage>> img = regenImages(this.bi);
            List<int[]> test_datas = new ArrayList<>();
            BufferedImage tuxiang = img.get(0).get(0);
            test_datas.add(img2arr(tuxiang));
            BufferedImage xuanxiang1 = img.get(1).get(0);
            test_datas.add(img2arr(xuanxiang1));
            BufferedImage wenzi1 = img.get(1).get(1);
            test_datas.add(img2arr(wenzi1));
            BufferedImage xuanxiang2 = img.get(2).get(0);
            test_datas.add(img2arr(xuanxiang2));
            BufferedImage wenzi2 = img.get(2).get(1);
            test_datas.add(img2arr(wenzi2));
            BufferedImage xuanxiang3 = img.get(3).get(0);
            test_datas.add(img2arr(xuanxiang3));
            BufferedImage wenzi3 = img.get(3).get(1);
            test_datas.add(img2arr(wenzi3));
            BufferedImage xuanxiang4 = img.get(4).get(0);
            test_datas.add(img2arr(xuanxiang4));
            BufferedImage wenzi4 = img.get(4).get(1);
            test_datas.add(img2arr(wenzi4));
            List<String[]> result_list = predict(test_datas);
            /*
            System.out.println("predict log：" + result_list.get(0)[1] + "---" + result_list.get(0)[0]);
            System.out.println("predict log：" + result_list.get(1)[1] + "---" + result_list.get(1)[0]);
            System.out.println("predict log：" + result_list.get(2)[1] + "---" + result_list.get(2)[0]);
            System.out.println("predict log：" + result_list.get(3)[1] + "---" + result_list.get(3)[0]);
            System.out.println("predict log：" + result_list.get(4)[1] + "---" + result_list.get(4)[0]);
            System.out.println("predict log：" + result_list.get(5)[1] + "---" + result_list.get(5)[0]);
            System.out.println("predict log：" + result_list.get(6)[1] + "---" + result_list.get(6)[0]);
            System.out.println("predict log：" + result_list.get(7)[1] + "---" + result_list.get(7)[0]);
            System.out.println("predict log：" + result_list.get(8)[1] + "---" + result_list.get(8)[0]);
            */
            if (Float.parseFloat(result_list.get(0)[1]) < 0.4) {
                double max_f = 0.0;
                if (Float.parseFloat(result_list.get(1)[1]) > max_f) {
                    max_f = Float.parseFloat(result_list.get(1)[1]);
                    result = result_list.get(1)[0].replace("_", "");
                }
                if (Float.parseFloat(result_list.get(3)[1]) > max_f) {
                    max_f = Float.parseFloat(result_list.get(3)[1]);
                    result = result_list.get(3)[0].replace("_", "");
                }
                if (Float.parseFloat(result_list.get(5)[1]) > max_f) {
                    max_f = Float.parseFloat(result_list.get(5)[1]);
                    result = result_list.get(5)[0].replace("_", "");
                }
                if (Float.parseFloat(result_list.get(7)[1]) > max_f) {
                    max_f = Float.parseFloat(result_list.get(7)[1]);
                    result = result_list.get(7)[0].replace("_", "");
                }
            } else {
                String tuxiang_result = result_list.get(0)[0].replace(".img", "");
                double max_f = 0.0;
                if (result_list.get(2)[0].equals(tuxiang_result) && Float.parseFloat(result_list.get(2)[1]) > max_f) {
                    result = result_list.get(1)[0].replace("_", "");
                    max_f = Float.parseFloat(result_list.get(2)[1]);
                }
                if (result_list.get(4)[0].equals(tuxiang_result) && Float.parseFloat(result_list.get(4)[1]) > max_f) {
                    result = result_list.get(3)[0].replace("_", "");
                    max_f = Float.parseFloat(result_list.get(4)[1]);
                }
                if (result_list.get(6)[0].equals(tuxiang_result) && Float.parseFloat(result_list.get(6)[1]) > max_f) {
                    result = result_list.get(5)[0].replace("_", "");
                    max_f = Float.parseFloat(result_list.get(6)[1]);
                }
                if (result_list.get(8)[0].equals(tuxiang_result)) {
                    result = result_list.get(7)[0].replace("_", "");
                    max_f = Float.parseFloat(result_list.get(8)[1]);
                }
                if (result.equals("")) {
                    int tuxiangPixelCount = -1;
                    for (Map.Entry<String, int[]> m : this.wenzi_mould.entrySet()) {
                        String class_name = m.getKey();
                        if (class_name.equals(tuxiang_result)) {
                            tuxiangPixelCount = m.getValue()[m.getValue().length - 1];
                            break;
                        }
                    }
                    if (tuxiangPixelCount == -1) {
                        if (Float.parseFloat(result_list.get(1)[1]) > max_f) {
                            max_f = Float.parseFloat(result_list.get(1)[1]);
                            result = result_list.get(1)[0].replace("_", "");
                        }
                        if (Float.parseFloat(result_list.get(3)[1]) > max_f) {
                            max_f = Float.parseFloat(result_list.get(3)[1]);
                            result = result_list.get(3)[0].replace("_", "");
                        }
                        if (Float.parseFloat(result_list.get(5)[1]) > max_f) {
                            max_f = Float.parseFloat(result_list.get(5)[1]);
                            result = result_list.get(5)[0].replace("_", "");
                        }
                        if (Float.parseFloat(result_list.get(7)[1]) > max_f) {
                            max_f = Float.parseFloat(result_list.get(7)[1]);
                            result = result_list.get(7)[0].replace("_", "");
                        }
                    } else  {
                        int minDiff = tuxiangPixelCount;
                        int[] wenziArr = new int[]{2, 4, 6, 8};
                        int simularXuanXiangIndex = 0;
                        for (int wenziI : wenziArr) {
                            int wenziCount = test_datas.get(wenziI)[test_datas.get(wenziI).length - 1];
                            if (Math.abs(wenziCount - tuxiangPixelCount) < minDiff) {
                                simularXuanXiangIndex = wenziI - 1;
                                minDiff = Math.abs(wenziCount - tuxiangPixelCount);
                            }
                        }
                        result = result_list.get(simularXuanXiangIndex)[0].replace("_", "");
                    }
                }
            }
            return result;
        } catch (Exception e) {
            System.out.println("Failed to identify the image!");
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 判断像素是否为黑色
     * @param pixRGB 像素rgb值
     * @return 1 黑色 0 非黑色
     */
    private int isBlack(int pixRGB) {
        if (pixRGB == Color.black.getRGB()) {
            return 1;
        }
        return 0;
    }

    /**
     * 中间类
     */
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

    /**
     * 图像去噪
     * @param bi 源图像
     * @param threshold 去噪粒度
     * @return 去噪后图像以及消除的像素个数
     * @throws Exception
     */
    private CleanedBufferedImage clean(BufferedImage bi, int threshold) throws Exception {
        BufferedImage img_cache = new BufferedImage(bi.getWidth(), bi.getHeight(), bi.getType());
        img_cache.setData(bi.getData());
        int width = bi.getWidth();
        int height = bi.getHeight();
        int count = 0;
        if (isBlack(img_cache.getRGB(0, 0)) == 1 && (isBlack(img_cache.getRGB(0, 1)) + isBlack(img_cache.getRGB(1, 1)) + isBlack(img_cache.getRGB(1, 0)) <= threshold)) {
            bi.setRGB(0, 0, Color.WHITE.getRGB());
            count++;
        }
        if (isBlack(img_cache.getRGB(0, height - 1)) == 1 && (isBlack(img_cache.getRGB(0, height - 2)) + isBlack(img_cache.getRGB(1, height - 1)) + isBlack(img_cache.getRGB(1, height - 2)) <= threshold)) {
            bi.setRGB(0, height - 1, Color.WHITE.getRGB());
            count++;
        }
        if (isBlack(img_cache.getRGB(width - 1, 0)) == 1 && (isBlack(img_cache.getRGB(width - 2, 0)) + isBlack(img_cache.getRGB(width - 2, 1)) + isBlack(img_cache.getRGB(width - 1, 1)) <= threshold)) {
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
        return new CleanedBufferedImage(bi, count);
    }

    /**
     * 获取图像边缘
     * @param bi 源图像
     * @return 边缘的x1, x2, y1, y2
     */
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
        return new int[]{x1, x2, y1, y2};
    }

    /**
     * 旋转切割图像，如果是图形，会做一个旋转处理
     * @param bi 源图像
     * @param rotate true 是图形需旋转 false 不是图形不用旋转
     * @param width 生成的图像宽度
     * @param height 生成的图像高度
     * @return 生成的图像
     * @throws Exception
     */
    private BufferedImage rotateClipScale(BufferedImage bi, boolean rotate, int width, int height) throws Exception {
        int res[] = getEdge(bi);
        int x1 = res[0];
        int x2 = res[1];
        int y1 = res[2];
        int y2 = res[3];
        BufferedImage subBi = bi.getSubimage(x1, y1, x2 - x1 + 1, y2 - y1 + 1);
        BufferedImage rotBi = RotateImage.Rotate(subBi, 0, width, height);
        if (rotate) {
            for (int i = 90; i > -90; --i) {
                BufferedImage _bi = RotateImage.Rotate(subBi, i, width, height);
                int _res[] = getEdge(_bi);
                if (_res[1] - _res[0] < x2 - x1) {
                    rotBi = _bi;
                    x2 = _res[1];
                    x1 = _res[0];
                }
            }
        }
        return rotBi;
    }

    /**
     * 切分候选项与文字
     * @param bi 源图像
     * @return 候选项图像与文字图像
     * @throws Exception
     */
    private List<BufferedImage> splitCandidate(BufferedImage bi) throws Exception {
        List<BufferedImage> res = new ArrayList<>();
        int width = bi.getWidth();
        int height = bi.getHeight();
        int xpixel[] = new int[width];
        boolean first_pixel = true;
        int x1 = 0, x2 = 0, y1 = 0, y2 = 0;
        for (int x = 0; x < width; ++x) {
            xpixel[x] = 0;
            for (int y = 0; y < height; ++y) {
                if (isBlack(bi.getRGB(x, y)) == 1) {
                    xpixel[x]++;
                    if (first_pixel) {
                        x1 = x;
                        x2 = x;
                        y1 = y;
                        y2 = y;
                        first_pixel = false;
                    } else {
                        if (x < x1) x1 = x;
                        if (y < y1) y1 = y;
                        if (x > x2) x2 = x;
                        if (y > y2) y2 = y;
                    }
                }
            }
            if (!first_pixel && xpixel[x] == 0) break;
        }

        if (x2 - x1 > 10) {
            first_pixel = true;
            x1 = 0;
            x2 = 0;
            y1 = 0;
            y2 = 0;
            for (int x = 0; x < x1 + 11; ++x) {
                xpixel[x] = 0;
                for (int y = 0; y < height; ++y) {
                    if (isBlack(bi.getRGB(x, y)) == 1) {
                        xpixel[x]++;
                        if (first_pixel) {
                            x1 = x;
                            x2 = x;
                            y1 = y;
                            y2 = y;
                            first_pixel = false;
                        } else {
                            if (x < x1) x1 = x;
                            if (y < y1) y1 = y;
                            if (x > x2) x2 = x;
                            if (y > y2) y2 = y;
                        }
                    }
                }
                if (!first_pixel && xpixel[x] == 0) break;
            }
        }

        BufferedImage zmsz = bi.getSubimage(x1, y1, x2 - x1 + 1, y2 - y1 + 1);
        res.add(RotateImage.Rotate(zmsz, 0, StuTool.IMAGE_WIDTH, StuTool.IMAGE_HEIGHT));

        first_pixel = true;
        for (int x = x2 + 11; x < width; ++x) {
            xpixel[x] = 0;
            for (int y = 0; y < height; ++y) {
                if (isBlack(bi.getRGB(x, y)) == 1) {
                    xpixel[x]++;
                    if (first_pixel) {
                        x1 = x;
                        x2 = x;
                        y1 = y;
                        y2 = y;
                        first_pixel = false;
                    } else {
                        if (x < x1) x1 = x;
                        if (y < y1) y1 = y;
                        if (x > x2) x2 = x;
                        if (y > y2) y2 = y;
                    }
                }
            }
        }
        BufferedImage wenzi = bi.getSubimage(x1, y1, x2 - x1 + 1, y2 - y1 + 1);
        res.add(RotateImage.Rotate(wenzi, 0, StuTool.IMAGE_WIDTH, StuTool.IMAGE_HEIGHT));

        return res;
    }

    /**
     * 图像切分
     * @param bi 源识别码
     * @return 切分后的图像列表
     * @throws Exception
     */
    private List<List<BufferedImage>> regenImages(BufferedImage bi) throws Exception {
        List<List<BufferedImage>> res_list = new ArrayList<>();
        int width = bi.getWidth();
        int height = bi.getHeight();
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                int rgb = bi.getRGB(x, y);
                int r = (rgb & 0xff0000) >> 16;
                int g = (rgb & 0xff00) >> 8;
                int b = (rgb & 0xff);
                if (r < 150 && g < 150 && b < 150) {
                    bi.setRGB(x, y, Color.BLACK.getRGB());
                } else {
                    bi.setRGB(x, y, Color.WHITE.getRGB());
                }
            }
        }
        BufferedImage img1 = bi.getSubimage(0, 0, 60, 60);
        BufferedImage img2 = bi.getSubimage(60, 0, 60, 60);
        BufferedImage img3 = bi.getSubimage(120, 0, 60, 60);
        CleanedBufferedImage _cbi1 = clean(img1, 1);
        CleanedBufferedImage _cbi2 = clean(img2, 1);
        CleanedBufferedImage _cbi3 = clean(img3, 1);
        CleanedBufferedImage cbi1 = clean(_cbi1.getCbi(), 0);
        CleanedBufferedImage cbi2 = clean(_cbi2.getCbi(), 0);
        CleanedBufferedImage cbi3 = clean(_cbi3.getCbi(), 0);
        BufferedImage tu = cbi1.getCbi();
        BufferedImage zi1 = cbi2.getCbi();
        BufferedImage zi2 = cbi3.getCbi();

        int middle_pixels = get_middel_pixes(cbi1.getCbi());
        if (middle_pixels < get_middel_pixes(cbi2.getCbi())) {
            middle_pixels = get_middel_pixes(cbi2.getCbi());
            tu = cbi2.getCbi();
            zi1 = cbi1.getCbi();
            zi2 = cbi3.getCbi();
        }
        if (middle_pixels < get_middel_pixes(cbi3.getCbi())) {
            tu = cbi3.getCbi();
            zi1 = cbi1.getCbi();
            zi2 = cbi2.getCbi();
        }
        List<BufferedImage> tu_list = new ArrayList<>();
        BufferedImage tuxiang = rotateClipScale(tu, true, StuTool.IMAGE_WIDTH, StuTool.IMAGE_HEIGHT);
        tu_list.add(tuxiang);
        BufferedImage zi11 = zi1.getSubimage(0, 0, zi1.getWidth(), zi1.getHeight() / 2);
        BufferedImage zi12 = zi1.getSubimage(0, zi1.getHeight() / 2, zi1.getWidth(), zi1.getHeight() / 2);
        BufferedImage zi21 = zi2.getSubimage(0, 0, zi2.getWidth(), zi2.getHeight() / 2);
        BufferedImage zi22 = zi2.getSubimage(0, zi2.getHeight() / 2, zi2.getWidth(), zi2.getHeight() / 2);
        List<BufferedImage> ab_wenzi_11 = splitCandidate(zi11);
        List<BufferedImage> ab_wenzi_12 = splitCandidate(zi12);
        List<BufferedImage> ab_wenzi_21 = splitCandidate(zi21);
        List<BufferedImage> ab_wenzi_22 = splitCandidate(zi22);
        res_list.add(tu_list);
        res_list.add(ab_wenzi_11);
        res_list.add(ab_wenzi_12);
        res_list.add(ab_wenzi_21);
        res_list.add(ab_wenzi_22);
        return res_list;
    }

    private int get_middel_pixes(BufferedImage bi) {
        int width = bi.getWidth();
        int height = bi.getHeight();
        int rslt = 0;
        for (int x = 0; x < width; ++x) {
            if (isBlack(bi.getRGB(x, height / 2)) == 1) {
                rslt++;
            }
        }
        return rslt;
    }

    /**
     * 图像转数列
     * @param img 源图像
     * @return 数列
     */
    public int[] img2arr(BufferedImage img) {
        int arr_size = img.getHeight() * img.getWidth() / (Integer.SIZE - 1);
        if (arr_size % (Integer.SIZE - 1) != 0) arr_size += 1;
        arr_size += 1;
        int[] img_data = new int[arr_size];
        int width = img.getWidth();
        int height = img.getHeight();
        int pixel_idx = 0;
        for (int i = 0; i < arr_size; i++) {
            img_data[i] = 0;
        }
        int pixel_count = 0;
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                int arr_index = pixel_idx / (Integer.SIZE - 1);
                int arr_bin_index = pixel_idx % (Integer.SIZE - 1);
                img_data[arr_index] += isBlack(img.getRGB(x, y)) << arr_bin_index;
                pixel_count += isBlack(img.getRGB(x, y));
                pixel_idx++;
            }
        }
        img_data[arr_size - 1] = pixel_count;
        return img_data;
    }

    /**
     * 生成模板方法
     * @return 模板列表，含图形、选项、文字模板
     * @throws Exception
     */
    private List<Map<String, int[]>> gen_moulds() throws Exception{
        String tuxiang_data_path = "train_data"+File.separator+"tuxiang";
        String xuanxiang_data_path = "train_data"+File.separator+"xuanxiang";
        String wenzi_data_path = "train_data"+File.separator+"wenzi";
        File tuxiang_data_dir = new File(tuxiang_data_path);
        File xuanxiang_data_dir = new File(xuanxiang_data_path);
        File wenzi_data_dir = new File(wenzi_data_path);
        List<Map<String, int[]>> moulds = new ArrayList<>();
        if (!tuxiang_data_dir.exists() || !tuxiang_data_dir.isDirectory()) {
            System.out.println("tuxiang_data path is not correct! please check it!");
            return null;
        }
        if (!xuanxiang_data_dir.exists() || !xuanxiang_data_dir.isDirectory()) {
            System.out.println("xuanxiang_data path is not correct! please check it!");
            return null;
        }
        if (!wenzi_data_dir.exists() || !wenzi_data_dir.isDirectory()) {
            System.out.println("wenzi_data path is not correct! please check it!");
            return null;
        }
        File[] data_type_dir_arr = new File[]{tuxiang_data_dir, xuanxiang_data_dir, wenzi_data_dir};
        for (File data_type_dir : data_type_dir_arr) {
            if (!data_type_dir.isDirectory()) {
                System.out.println("train_data path [" + data_type_dir.getAbsolutePath() + "] is not a directory!");
                continue;
            }
            File[] data_type_sub_dir_arr = data_type_dir.listFiles();
            Map<String, int[]> m = new HashMap<>();
            for (File class_name_dir : data_type_sub_dir_arr) {
                if (!class_name_dir.isDirectory()) continue;
                String class_name = class_name_dir.getName();

                if (class_name.contains("_")) {
                    class_name = class_name.toUpperCase().replace("_", "");
                }
                System.out.println(class_name);
                for (File img_file : class_name_dir.listFiles()) {
                    BufferedImage img;
                    try {
                        img = ImageIO.read(img_file);
                    } catch (Exception e) {
                        System.out.println("File [" + img_file.getAbsolutePath() + "] is not image");
                        continue;
                    }
                    if (img != null) {
                        if (class_name.contains(".img")) {
                            img = rotateClipScale(img, true, StuTool.IMAGE_WIDTH, StuTool.IMAGE_HEIGHT);
                        }
                        List<BufferedImage> img_l = RotateImage.RotateMouldList(img, class_name.contains(".img"), StuTool.IMAGE_WIDTH, StuTool.IMAGE_HEIGHT);
                        for (BufferedImage _img : img_l) {
                            int[] img_data = img2arr(_img);
                            while (m.containsKey(class_name)) {
                                class_name += "_";
                            }
                            m.put(class_name, img_data);
                        }

                    }
                }
            }
            moulds.add(m);
        }
        return moulds;
    }

    /**
     * 计算整数二进制后数位为1个个数
     * @param n
     * @return 整数二进制后数位为1个个数
     */
    int bitCount(int n) {
        int c = 0;
        for (c = 0; n != 0; ++c) {
            n &= (n - 1); // 清除最低位的1
        }
        return c;
    }

    /**
     * 预测结果
     * @param predict_list 待预测的列表
     * @return 预测结果
     */
    private List<String[]> predict(List<int[]> predict_list) {
        List<String[]> res = new ArrayList<>();
        if (this.tuxiang_mould == null || this.wenzi_mould == null || this.xuanxiang_mould == null) {
            System.out.println("The mould contains NULL, please check it!");
            return null;
        }
        for (int i = 0; i < predict_list.size(); i++) {
            int[] img_data = predict_list.get(i);
            Map<String, int[]> mould = new HashMap<>();
            if (i == 0) mould = this.tuxiang_mould;
            if (i == 1 || i == 3 || i == 5 || i == 7) mould = this.xuanxiang_mould;
            if (i == 2 || i == 4 || i == 6 || i == 8) mould = this.wenzi_mould;
            double max_matrio = 0.0;
            String res_name = "";
            int img_len = 0;
            for (int j = 0; j < img_data.length - 1; j++) {
                img_len += bitCount(img_data[j]);
            }
            for (Map.Entry<String, int[]> m : mould.entrySet()) {
                int current_len = 0;
                String class_name = m.getKey();
                int[] m_data = m.getValue();
                int m_len = 0;
                for (int j = 0; j < m_data.length - 1; j++) {
                    current_len += bitCount(m_data[j] & img_data[j]);
                    m_len += bitCount(m_data[j]);
                }
                double current_matrio = 2 * current_len / (double) (img_len + m_len);
                if (current_matrio > max_matrio) {
                    max_matrio = current_matrio;
                    res_name = class_name;
                }
            }
            res.add(new String[]{res_name.replace("_", ""), "" + max_matrio});
        }
        return res;
    }

    /**
     * Moulds get方法
     * @return 所有模板
     */
    public List<Map<String, int[]>> getMoulds() {
        return this.moulds;
    }

    /**
     * main方法，简单测试用
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        
        StuTool st = new StuTool();
        System.out.println("------------RESULT----------------");
        long startTime=System.currentTimeMillis();
        st.setImage("C:\\Users\\xiaohei\\git\\tmp\\stu\\pic\\1447915797272.png");
        System.out.println(st.stu());
        long endTime=System.currentTimeMillis();
        System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
        startTime=System.currentTimeMillis();
        st.setImage("C:\\Users\\xiaohei\\git\\tmp\\stu\\pic\\1447915796482.png");
        System.out.println(st.stu());
        endTime=System.currentTimeMillis();
        System.out.println("程序运行时间： "+(endTime-startTime)+"ms");

        st.genTrainData("C:\\Users\\xiaohei\\git\\new_img\\");

    }
}
