package cn.gdut;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2015/11/25.
 */
public class StuTool {
    private BufferedImage bi = null;
    private Map<String, int[]> tuxiang_mould = null;
    private Map<String, int[]> xuanxiang_mould = null;
    private Map<String, int[]> wenzi_mould = null;
    private List<Map<String, int[]>> moulds = null;
    public static int IMAGE_WIDTH = 75;
    public static int IMAGE_HEIGHT = 75;


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

    public void setImage(String filename) {
        try {
            this.bi = ImageIO.read(new File(filename));
            // testRead();
        } catch (Exception e) {
            System.out.println("Image File Not Found!");
            e.printStackTrace();
        }
    }

    public BufferedImage getImage() {
        return bi;
    }

    public void pre_train() {
        String pic_path = "pic\\";
        File pic_dir = new File(pic_path);
        if (!pic_dir.exists() && !pic_dir.isDirectory()) {
            System.out.println("The training picture files directory is not correct! Please check it!");
            return;
        }
        File[] pic_files = pic_dir.listFiles();
        if (pic_files.length == 0) {
            System.out.println("No picture file is found under " + pic_path + " ! Please check it!");
            return;
        }
        int tu_index = 1;
        int wz_index = 1;
        int zmsz_index = 1;
        String tuxiang_path = "source_data\\tuxiang\\";
        String wenzi_path = "source_data\\zhongwen\\";
        String zimushuzi_path = "source_data\\zimushuzi\\";
        for (int i = 0; i < pic_files.length; ++i) {
            File pic_file = pic_files[i];
            BufferedImage img;
            List<List<BufferedImage>> res_imgs;
            System.out.println("---------------");
            try {
                img = ImageIO.read(pic_file);
                res_imgs = regenImages(img);
            } catch (Exception e) {
                System.out.println("Exception appear when read(pic_file) or regenImages(img)");
                e.printStackTrace();
                continue;
            }
            try {
                ImageIO.write(res_imgs.get(0).get(0), "PNG", new File(tuxiang_path + tu_index + ".png"));
                tu_index++;
                ImageIO.write(res_imgs.get(1).get(0), "PNG", new File(zimushuzi_path + zmsz_index + ".png"));
                zmsz_index++;
                ImageIO.write(res_imgs.get(2).get(0), "PNG", new File(zimushuzi_path + zmsz_index + ".png"));
                zmsz_index++;
                ImageIO.write(res_imgs.get(3).get(0), "PNG", new File(zimushuzi_path + zmsz_index + ".png"));
                zmsz_index++;
                ImageIO.write(res_imgs.get(4).get(0), "PNG", new File(zimushuzi_path + zmsz_index + ".png"));
                zmsz_index++;
                ImageIO.write(res_imgs.get(1).get(1), "PNG", new File(wenzi_path + wz_index + ".png"));
                wz_index++;
                ImageIO.write(res_imgs.get(2).get(1), "PNG", new File(wenzi_path + wz_index + ".png"));
                wz_index++;
                ImageIO.write(res_imgs.get(3).get(1), "PNG", new File(wenzi_path + wz_index + ".png"));
                wz_index++;
                ImageIO.write(res_imgs.get(4).get(1), "PNG", new File(wenzi_path + wz_index + ".png"));
                wz_index++;
            } catch (Exception e) {
                System.out.println("Exception appear when ImageIo.write()");
                e.printStackTrace();
                continue;

            }
            System.out.println("++++++++++++++++");

        }
    }

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
            System.out.println("---" + result_list.get(0)[1] + "---" + result_list.get(0)[0]);
            System.out.println("---" + result_list.get(1)[1] + "---" + result_list.get(1)[0]);
            System.out.println("---" + result_list.get(2)[1] + "---" + result_list.get(2)[0]);
            System.out.println("---" + result_list.get(3)[1] + "---" + result_list.get(3)[0]);
            System.out.println("---" + result_list.get(4)[1] + "---" + result_list.get(4)[0]);
            System.out.println("---" + result_list.get(5)[1] + "---" + result_list.get(5)[0]);
            System.out.println("---" + result_list.get(6)[1] + "---" + result_list.get(6)[0]);
            System.out.println("---" + result_list.get(7)[1] + "---" + result_list.get(7)[0]);
            System.out.println("---" + result_list.get(8)[1] + "---" + result_list.get(8)[0]);
            if (Float.parseFloat(result_list.get(0)[1]) < 0.4) {
                double max_f = 0.0;
                if (Float.parseFloat(result_list.get(1)[1]) > max_f) {
                    max_f = Float.parseFloat(result_list.get(1)[1]);
                    result = result_list.get(1)[0].replace("_", "").toUpperCase();
                }
                if (Float.parseFloat(result_list.get(3)[1]) > max_f) {
                    max_f = Float.parseFloat(result_list.get(3)[1]);
                    result = result_list.get(3)[0].replace("_", "").toUpperCase();
                }
                if (Float.parseFloat(result_list.get(5)[1]) > max_f) {
                    max_f = Float.parseFloat(result_list.get(5)[1]);
                    result = result_list.get(5)[0].replace("_", "").toUpperCase();
                }
                if (Float.parseFloat(result_list.get(7)[1]) > max_f) {
                    max_f = Float.parseFloat(result_list.get(7)[1]);
                    result = result_list.get(7)[0].replace("_", "").toUpperCase();
                }
            } else {
                String tuxiang_result = result_list.get(0)[0].replace(".img", "");
                double max_f = 0.0;
                if (result_list.get(2)[0].equals(tuxiang_result) && Float.parseFloat(result_list.get(2)[1]) > max_f) {
                    result = result_list.get(1)[0].replace("_", "").toUpperCase();
                    max_f = Float.parseFloat(result_list.get(2)[1]);
                }
                if (result_list.get(4)[0].equals(tuxiang_result) && Float.parseFloat(result_list.get(4)[1]) > max_f) {
                    result = result_list.get(3)[0].replace("_", "").toUpperCase();
                    max_f = Float.parseFloat(result_list.get(4)[1]);
                }
                if (result_list.get(6)[0].equals(tuxiang_result) && Float.parseFloat(result_list.get(6)[1]) > max_f) {
                    result = result_list.get(5)[0].replace("_", "").toUpperCase();
                    max_f = Float.parseFloat(result_list.get(6)[1]);
                }
                if (result_list.get(8)[0].equals(tuxiang_result)) {
                    result = result_list.get(7)[0].replace("_", "").toUpperCase();
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
                            result = result_list.get(1)[0].replace("_", "").toUpperCase();
                        }
                        if (Float.parseFloat(result_list.get(3)[1]) > max_f) {
                            max_f = Float.parseFloat(result_list.get(3)[1]);
                            result = result_list.get(3)[0].replace("_", "").toUpperCase();
                        }
                        if (Float.parseFloat(result_list.get(5)[1]) > max_f) {
                            max_f = Float.parseFloat(result_list.get(5)[1]);
                            result = result_list.get(5)[0].replace("_", "").toUpperCase();
                        }
                        if (Float.parseFloat(result_list.get(7)[1]) > max_f) {
                            max_f = Float.parseFloat(result_list.get(7)[1]);
                            result = result_list.get(7)[0].replace("_", "").toUpperCase();
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
                        result = result_list.get(simularXuanXiangIndex)[0].replace("_", "").toUpperCase();
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


    private String img2str(BufferedImage img) {
        String res = "";
        int width = img.getWidth();
        int height = img.getHeight();
        int pixel_idx = 1;
        for (int x = 0; x < width; ++x) {
            if (x < 31 || x >= 31 + 13) continue;
            for (int y = 0; y < height; ++y) {
                if (y < 31 || y >= 31 + 13) continue;
                res += "" + pixel_idx + ":" + isBlack(img.getRGB(x, y)) + " ";
                pixel_idx++;
            }
        }
        return res.trim();
    }

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
//      int tuCount = _cbi1.getCount();
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



/*

        if (tuCount > _cbi2.getCount()) {
            tu = cbi2.getCbi();
            tuCount = cbi2.getCount();
            zi1 = cbi1.getCbi();
            zi2 = cbi3.getCbi();
        }
        if (tuCount > _cbi3.getCount()) {
            tu = cbi3.getCbi();
            // tuCount = cbi3.getCount();
            zi1 = cbi1.getCbi();
            zi2 = cbi2.getCbi();
        }
        */
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

    public void gen_train_data() {
        int class_num = 1;
        String train_data_path = "train_data\\";
        File train_data_dir = new File(train_data_path);
        if (!train_data_dir.exists() || !train_data_dir.isDirectory()) {
            System.out.println("train_data path is not correct! please check it!");
            return;
        }
        File[] data_type_dir_arr = train_data_dir.listFiles();
        BufferedWriter fw = null, fw_name = null;
        try {
            fw = new BufferedWriter(new FileWriter(new File("data\\img.train")));
            fw_name = new BufferedWriter(new FileWriter(new File("data\\img.name")));
        } catch (Exception e) {
            System.out.println("Fail to write img.train file.");
            e.printStackTrace();
            return;
        }
        for (File data_type_dir : data_type_dir_arr) {
            if (!data_type_dir.isDirectory()) {
                System.out.println("train_data path [" + data_type_dir.getAbsolutePath() + "] is not a directory!");
                continue;
            }
            File[] data_type_sub_dir_arr = data_type_dir.listFiles();
            boolean first_line = true;
            for (File class_name_dir : data_type_sub_dir_arr) {
                if (!class_name_dir.isDirectory()) continue;
                String class_name = class_name_dir.getName();
                try {
                    fw_name.write(class_num + "=" + class_name);
                    fw_name.newLine();
                    fw_name.flush();
                    fw.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(class_num + "=" + class_name);
                for (File img_file : class_name_dir.listFiles()) {
                    BufferedImage img;
                    try {
                        img = ImageIO.read(img_file);
                    } catch (Exception e) {
                        System.out.println("File [" + img_file.getAbsolutePath() + "] is not image");
                        continue;
                    }
                    if (img != null) {
                        int width = img.getWidth();
                        int height = img.getHeight();
                        String img_data = "" + class_num + "  ";
                        int pixel_idx = 1;
                        if (!first_line) {
                            try {
                                fw.newLine();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        first_line = false;
                        for (int x = 0; x < width; ++x) {
                            if (x < 31 || x >= 31 + 13) continue;
                            for (int y = 0; y < height; ++y) {
                                if (y < 31 || y >= 31 + 13) continue;
                                img_data += "" + pixel_idx + ":" + isBlack(img.getRGB(x, y)) + " ";
                                img_data = img_data.trim();
                                pixel_idx++;
                            }
                        }
                        try {
                            fw.write(img_data);
                        } catch (Exception e) {
                            System.out.println("Fail to write img.gen_train_data with (" +
                                    img_data +
                                    ")");
                            continue;
                        }
                    }
                }
                class_num++;
            }
        }
        try {
            fw.flush();
            fw.close();
            fw_name.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void train() {
        String[] argvTrain = {
                "data\\img.train",
                "data\\img.model"
        };
        svm_train svmt = new svm_train();
        try {
            System.out.println("training...");
            svmt.main(argvTrain);
            System.out.println("done!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void gen_test_data() throws Exception {
        String test_data_path = "source_data\\test\\";
        File test_data_dir = new File(test_data_path);
        BufferedWriter fw = null;
        try {
            fw = new BufferedWriter(new FileWriter(new File("data\\img.test")));
        } catch (Exception e) {
            System.out.println("Fail to write img.test.");
            e.printStackTrace();
            return;
        }
        for (File f : test_data_dir.listFiles()) {
            BufferedImage bi = ImageIO.read(f);
            String test_data = "0  ";
            test_data += img2str(bi);
            fw.write(test_data);
            fw.newLine();
            fw.flush();
        }
        fw.close();
    }


    private List<List<int[]>> gen_test_datas(boolean isTuxiang, String test_data_path) throws Exception {
        File test_location = new File(test_data_path);
        List<List<int[]>> res = new ArrayList<>();

        if (test_location.isDirectory()) {
            for (File f : test_location.listFiles()) {
                BufferedImage bi = ImageIO.read(f);
                List<BufferedImage> bil = RotateImage.RotateTestList(bi, isTuxiang, StuTool.IMAGE_WIDTH, StuTool.IMAGE_HEIGHT);
                List<int[]> r = new ArrayList<>();
                for (BufferedImage _bi : bil) {
                    int[] img_data = img2arr(bi);
                    r.add(img_data);
                }
                res.add(r);
            }
        } else {
            BufferedImage bi = ImageIO.read(test_location);
            List<BufferedImage> bil = RotateImage.RotateTestList(bi, isTuxiang, StuTool.IMAGE_WIDTH, StuTool.IMAGE_HEIGHT);
            List<int[]> r = new ArrayList<>();
            int idx = 1;
            for (BufferedImage _bi : bil) {
                ImageIO.write(_bi, "PNG", new File("tmp\\" + idx + ".png"));
                idx++;
                int[] img_data = img2arr(bi);
                r.add(img_data);
            }
            res.add(r);
        }
        return res;
    }

    private List<int[]> gen_test_datas(boolean isTuxiang, BufferedImage bi) throws Exception {
        List<int[]> res = new ArrayList<>();
        res.add(img2arr(bi));
        return res;
    }

    private List<int[]> gen_test_datas_deprecated(boolean isTuxiang, BufferedImage bi) throws Exception {
        List<int[]> res = new ArrayList<>();
        List<BufferedImage> bil = RotateImage.RotateTestList(bi, isTuxiang, StuTool.IMAGE_WIDTH, StuTool.IMAGE_HEIGHT);
        int idx = 1;
        for (BufferedImage _bi : bil) {
            ImageIO.write(_bi, "PNG", new File("tmp\\" + idx + ".png"));
            idx++;
            int[] img_data = img2arr(bi);
            res.add(img_data);
        }
        return res;
    }


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

    private List<Map<String, int[]>> gen_moulds() throws Exception{
        String tuxiang_data_path = "train_data\\tuxiang";
        String xuanxiang_data_path = "train_data\\xuanxiang";
        String wenzi_data_path = "train_data\\wenzi";
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

    int bitCount(int n) {
        int c = 0;
        for (c = 0; n != 0; ++c) {
            n &= (n - 1); // 清除最低位的1
        }
        return c;
    }

    private List<String[]> predict_deprecated(List<List<int[]>> test_list) {
        List<String[]> res = new ArrayList<>();
        if (this.tuxiang_mould == null || this.wenzi_mould == null || this.xuanxiang_mould == null) {
            System.out.println("The mould contains NULL, please check it!");
            return null;
        }
        for (int i = 0; i < test_list.size(); i++) {
            List<int[]> img_data_list = test_list.get(i);
            Map<String, int[]> mould = new HashMap<>();
            if (i == 0) mould = this.tuxiang_mould;
            if (i == 1 || i == 3 || i == 5 || i == 7) mould = this.xuanxiang_mould;
            if (i == 2 || i == 4 || i == 6 || i == 8) mould = this.wenzi_mould;
            double max_matrio = 0.0;
            String res_name = "";
            for (int[] img_data : img_data_list) {
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
//                    double gap_ratio = 0.0;
//                    if (m_len > img_len) gap_ratio =  Math.log(m_len - img_len) + 10E-10;
//                    else if (img_len > m_len) gap_ratio = Math.log(img_len - m_len) + 10E-10;
//                    else gap_ratio = 10E-10;
                    double current_matrio = 2 * current_len / (double) (img_len + m_len);
                    if (current_matrio > max_matrio) {
                        max_matrio = current_matrio;
                        res_name = class_name;
                    }
                    //System.out.println(current_len);
                    //System.out.println(m_len);
                    //System.out.println(current_matrio);
                }
            }
            res.add(new String[]{res_name.replace("_", ""), "" + max_matrio});
        }
        return res;
    }


    private List<String[]> predict(List<int[]> test_list) {
        List<String[]> res = new ArrayList<>();
        if (this.tuxiang_mould == null || this.wenzi_mould == null || this.xuanxiang_mould == null) {
            System.out.println("The mould contains NULL, please check it!");
            return null;
        }
        for (int i = 0; i < test_list.size(); i++) {
            int[] img_data = test_list.get(i);
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

    public List<Map<String, int[]>> getMoulds() {
        return this.moulds;
    }
    public static void main(String[] args) throws Exception {
        StuTool st = new StuTool();
        System.out.println("------------RESULT----------------");
        long startTime=System.currentTimeMillis();
        st.setImage("pic\\1447915797272.png");
        System.out.println(st.stu());
        long endTime=System.currentTimeMillis();
        System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
        startTime=System.currentTimeMillis();
        st.setImage("pic\\1447915796482.png");
        System.out.println(st.stu());
        endTime=System.currentTimeMillis();
        System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
        //ObjectFileConvert.object2File(st.getMoulds(), "mould.file");
        //st.setImage("F:\\BaiduYunDownload\\pic\\pic\\3.png");
        //st.stu();
        //st.pre_train();
        //st.gen_train_data();
//        st.gen_test_data();
//        st.train();
//        String[] parg = { "data\\img.test", // 这个是存放测试数据
//                "data\\img.model", // 调用的是训练以后的模型
//                "data\\img.result" }; // 生成的结果的文件的路径
//        svm_predict p = new svm_predict();
//        p.main(parg);
        //st.gen_mould();

        //Map<String, int[]> mould = st.gen_mould();
//
//        List<List<int[]>> test_list = st.gen_test_datas(true, "C:\\Program Files\\Git\\workspace\\stu\\source_data\\test\\135.png");
//
//        List<String[]> res_list = st.predict(test_list);
//
//        System.out.println("___RESULT___");
//        for (String[] res_str : res_list) {
//            System.out.println(res_str[0]);
//            System.out.println(res_str[1]);
//        }
////
//        st.rotateClipScale(ImageIO.read(new File("C:\\Program Files\\Git\\workspace\\stu\\source_data\\tmp\\t\\1.png")),true,75, 75);
//        BufferedImage res = st.rotateClipScale(RotateImage.Rotate(ImageIO.read(new File("C:\\Program Files\\Git\\workspace\\stu\\source_data\\tmp\\t\\1.png")), 91, 75, 75), true, 75, 75);
//        BufferedImage res1 = RotateImage.Rotate(ImageIO.read(new File("C:\\Program Files\\Git\\workspace\\stu\\source_data\\tmp\\t\\1.png")), 180, 75, 75);
//        ImageIO.write(res, "PNG", new File("C:\\Program Files\\Git\\workspace\\stu\\source_data\\tmp\\t\\1_.png"));
//        ImageIO.write(res1, "PNG", new File("C:\\Program Files\\Git\\workspace\\stu\\source_data\\tmp\\t\\1__.png"));


//        Map<String, int[]> m = new HashMap<>();
//        m.put("a", new int[]{1, 2, 3});
//        int a = 0x7fffffff;
//        System.out.println(a);
//        int b = 1 << 3;
//        System.out.println(b & a);
//        System.out.println(st.bitCount(a));
//        System.out.println(m.get("a")[2]);
    }
}
