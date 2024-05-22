package com.hsk;

import com.hsk.utils.DataSetUtil;
import com.hsk.utils.PaddleServingUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


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
}
