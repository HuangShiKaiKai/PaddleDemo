package com.hsk.utils;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author: huangshikai
 * @Date: 2024/4/1915:52
 * @Description:
 */
@Slf4j
public class PaddleServingUtil {
    private static final String GALLERY_PATH = "F:\\飞浆\\aibackend_data\\gallery\\";

    /*
     * @return void
     * @Author HuangShiKai
     * @Description //创建数据集文件夹
     * @Date 2024/4/23 9:35
     * @Param [start, end]
     **/
    public static void mkdirGallery(Integer start, Integer end) {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = start; i <= end; i++) {
            final int index = i;
            executor.submit(() -> {
                try {
                    String path = GALLERY_PATH + index;
                    Path directory = Path.of(path);
                    // 判断文件夹是否存在，若不存在则创建
                    if (!Files.exists(directory)) {
                        Files.createDirectories(directory);
                        log.info("创建文件夹成功: {}", path);
                    }
                } catch (IOException e) {
                    log.error("创建文件夹失败: {}", index, e);
                }
            });
        }
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            log.error("线程池等待终止时被打断", e);
            Thread.currentThread().interrupt(); // 重新设置中断状态
        }
    }

    /*
     * @return void
     * @Author HuangShiKai
     * @Description //旋转图片
     * @Date 2024/4/23 9:30
     * @Param []
     **/
    public static void rotateImages() {
        File gallery = new File(GALLERY_PATH);
        File[] files = gallery.listFiles();
        for (File file : Objects.requireNonNull(files)) {
            if (file.isDirectory()) {
                String fileName = file.getAbsolutePath() + "\\" + "0.png";
                try {
                    ImgUtil.write(ImgUtil.rotate(ImageIO.read(FileUtil.file(fileName)), 90), FileUtil.file(file.getAbsolutePath() + "\\" + "1.png"));
                    ImgUtil.write(ImgUtil.rotate(ImageIO.read(FileUtil.file(fileName)), 180), FileUtil.file(file.getAbsolutePath() + "\\" + "2.png"));
                    ImgUtil.write(ImgUtil.rotate(ImageIO.read(FileUtil.file(fileName)), 270), FileUtil.file(file.getAbsolutePath() + "\\" + "3.png"));
                } catch (IOException e) {
                    log.error("旋转图片失败:{}{}", e, fileName);
                }

            }
        }
    }

    /*
     * @return void
     * @Author HuangShiKai
     * @Description //创建标签文件
     * @Date 2024/4/23 9:35
     * @Param []
     **/
    public static void createLabel() {
        List<String> list = new ArrayList<>();
        File gallery = new File(GALLERY_PATH);
        File[] files = gallery.listFiles();
        try {
            for (File file : Objects.requireNonNull(files)) {
                String fatherDir = gallery.getAbsolutePath() + "\\";
                if (file.isDirectory()) {
                    File[] subFiles = file.listFiles();
                    int count = 0;
                    for (File subFile : Objects.requireNonNull(subFiles)) {
                        String fileName = subFile.getAbsolutePath().replace(fatherDir, "");
                        count++;
                        String father = fileName.substring(0, fileName.lastIndexOf("\\"));
                        String label = "gallery/" + fileName + " " + father + " " + count;
                        if (label.contains("\\")) {
                            label = label.replace("\\", "/");
                        }
                        list.add(label);
                    }
                }
            }
            FileUtil.writeLines(list, GALLERY_PATH.replace("gallery", "") + "aibackend_label.txt", "UTF-8");
        } catch (Exception e) {
            log.error("创建标签文件失败", e);
        }
    }
}
