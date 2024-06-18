package com.hsk;

import com.hsk.utils.DataSetUtil;
import com.hsk.utils.PaddleServingUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;


@SpringBootTest
class PaddleDemoApplicationTests {

    /*
     * @return
     * @Author HuangShiKai
     * @Description //创建数据集文件夹
     * @Date 2024/4/23 9:34
     * @Param []
     **/
    @Test
    void contextLoads() {
        PaddleServingUtil.mkdirGallery(265, 300);
    }

    /*
     * @return
     * @Author HuangShiKai
     * @Description //旋转图片
     * @Date 2024/4/23 9:34
     * @Param []
     **/
    @Test
    void test() {
        PaddleServingUtil.rotateImages();
    }

    /*
     * @return
     * @Author HuangShiKai
     * @Description //创建标签文件
     * @Date 2024/4/23 9:34
     * @Param []
     **/
    @Test
    void test2() {
        PaddleServingUtil.createLabel();
    }


    /*
     * @return
     * @Author HuangShiKai
     * @Description //voc转换coco数据集
     * @Date 2024/4/23 9:34
     * @Param []
     **/
    @Test
    void ocrDataSetToCoco() {
        // PPOCRLabel标签文件路径
        String labelTxtPath = "F:\\角标ocr\\Label.txt";
        // 目标路径
        String targetPath = "F:\\ocrCOCO";
        // 转换
        DataSetUtil.ocrDataSetToCoco(labelTxtPath, targetPath);
    }

    @Test
    void test3() {
        String labelParentPath = "F:\\product0\\Label.txt";
        String targetPathRoot = "F:\\productAll";
        DataSetUtil.copyLabelImage(labelParentPath, targetPathRoot);
    }
    @Test
    void test4() {
        String dir = "F:\\飞浆\\out\\img";
        String dir1 = "F:\\productAll";
        String dir2 = "F:\\labelOcr";
        //读取dir所有文件名。如果dir1和dir2存在同名文件，则删除dir1和dir2的同名文件
        File dirFile = new File(dir);
        if (dirFile.isDirectory()) {
            File[] files = dirFile.listFiles();
            for (File file : files) {
                if (file.isFile()) {
                    String fileName = file.getName();
                    File file1 = new File(dir1, fileName);
                    File file2 = new File(dir2, fileName);

                    // 检查dir1中是否存在同名文件，如果存在则删除
                    if (file1.exists()) {
                        file1.delete();
                        System.out.println("Deleted: " + file1.getAbsolutePath());
                    }

                    // 检查dir2中是否存在同名文件，如果存在则删除
                    if (file2.exists()) {
                        file2.delete();
                        System.out.println("Deleted: " + file2.getAbsolutePath());
                    }
                }
            }
        }
    }
}
