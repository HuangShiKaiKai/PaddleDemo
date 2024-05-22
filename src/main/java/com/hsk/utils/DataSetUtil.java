package com.hsk.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hsk.entity.CoCo.*;
import com.hsk.entity.voc.VocImage;
import com.hsk.entity.voc.VocItem;
import com.hsk.vo.VocItemVo;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author: huangshikai
 * @Date: 2024/5/2210:20
 * @Description:数据集工具类
 */
@Slf4j
public class DataSetUtil {

    private static final String IMAGE_PATH = "images";
    private static final String ANNOTATION_PATH = "annotations";

    /*
     * @return void
     * @Author HuangShiKai
     * @Description ocr数据集转coco数据集
     * @Date 2024/5/22 10:25
     * @Param [labelTxt(标注信息文件), targetPath(输出路径)]
     **/
    public static void ocrDataSetToCoco(String labelTxtPath, String targetPath) {
        Path labelPath = Path.of(labelTxtPath);

        if (!Files.exists(labelPath) || !Files.isRegularFile(labelPath)) {
            log.error("Label file does not exist or is not a regular file: {}", labelTxtPath);
            return;
        }

        DataSet dataSet = new DataSet();
        try {
            // 获取Voc数据集
            List<VocImage> vocImages = getVocDataSet(labelPath.toFile());
            if (CollUtil.isNotEmpty(vocImages)) {
                // 获取分类
                Map<String, Integer> vocKeyCls = getVocKeyCls(vocImages);
                // 解析Image
                String imageParentPath = labelPath.getParent().toString();
                dataSet.setImages(analysisImages(vocImages, imageParentPath));
                dataSet.setAnnotations(analysisAnnotations(vocImages, vocKeyCls));
                dataSet.setCategories(analysisCategories(vocKeyCls));

                //转为json
                ObjectMapper objectMapper = new ObjectMapper();
                String json = objectMapper.writeValueAsString(dataSet);

                // 创建目录
                createDir(targetPath);
                //创建json文件
                String jsonDir = targetPath + File.separator + "annotations";
                Path jsonPath = Path.of(jsonDir);
                Files.write(jsonPath.resolve("coco.json"), json.getBytes());

                // 复制图片
                copyImage(imageParentPath, targetPath, dataSet.getImages());
            }
        } catch (Exception e) {
            log.error("Error parsing label file: {}", labelTxtPath, e);
            return;
        }
    }

    /*
     * @return java.util.List<com.hsk.entity.voc.VocImage>
     * @Author HuangShiKai
     * @Description 将Voc数据集转为对象
     * @Date 2024/5/22 13:56
     * @Param [labelTxtPath(labelTxt文件路径)]
     **/
    public static List<VocImage> getVocDataSet(File labelTxtPath) {
        List<VocImage> vocImages = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        // 读取label文件
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(labelTxtPath));) {
            String line;
            AtomicInteger id = new AtomicInteger(1);
            while ((line = bufferedReader.readLine()) != null) {
                String fileName = line.split("\t")[0];
                String itemsJson = line.split("\t")[1];
                if (StringUtils.isNotBlank(fileName) && StringUtils.isNotBlank(itemsJson)) {
                    // 处理文件名，只保留文件名部分
                    fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
                    VocImage vocImage = new VocImage();
                    vocImage.setFileName(fileName);
                    vocImage.setId(id.getAndIncrement());
                    // 将标签信息反序列化为对象
                    vocImage.setItems(objectMapper.readValue(itemsJson, new TypeReference<>() {
                    }));
                    vocImages.add(vocImage);
                }
            }
        } catch (Exception e) {
            log.error("读取Voc数据集失败");
        }
        return vocImages;
    }

    /*
     * @return java.util.Map<java.lang.String,java.lang.Integer>
     * @Author HuangShiKai
     * @Description //把分类放入内存
     * @Date 2024/5/22 16:29
     * @Param [vocImages]
     **/
    public static Map<String, Integer> getVocKeyCls(List<VocImage> vocImages) {
        Map<String, Integer> vocKeyCls = new HashMap<>();
        // 用于生成唯一索引，从1开始
        AtomicInteger index = new AtomicInteger(1);
        vocImages.stream()
                .flatMap(vocImage -> vocImage.getItems().stream())
                .forEach(vocItem -> {
                    // 获取分类
                    vocKeyCls.computeIfAbsent(vocItem.getKeyCls(), k -> index.getAndIncrement());
                });
        return vocKeyCls;
    }


    /*
     * @return java.util.List<com.hsk.entity.CoCo.Image>
     * @Author HuangShiKai
     * @Description 解析Images
     * @Date 2024/5/22 16:49
     * @Param [vocImages]
     **/
    public static List<Image> analysisImages(List<VocImage> vocImages, String imageParentPath) {
        if (vocImages == null || vocImages.isEmpty()) {
            return Collections.emptyList();
        }
        List<Image> images = new ArrayList<>();
        try {
            // 获取图片名及id
            images = vocImages.stream()
                    .map(vocImage -> {
                        Image image = new Image();
                        // 从map中获取id
                        image.setId(vocImage.getId());
                        image.setFileName(vocImage.getFileName());
                        BufferedImage bufferedImage = null;
                        try {
                            String imagePath = imageParentPath + File.separator + vocImage.getFileName();
                            bufferedImage = ImageIO.read(new File(imagePath));
                            image.setHeight(bufferedImage.getHeight());
                            image.setWidth(bufferedImage.getWidth());
                        } catch (IOException e) {
                            log.error("读取图片信息失败: {}", vocImage.getFileName(), e);
                        }
                        return image;
                    }).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("解析Images失败:", e);
        }
        return images;
    }

    /*
     * @return java.util.List<com.hsk.entity.CoCo.Annotation>
     * @Author HuangShiKai
     * @Description 解析Annotations
     * @Date 2024/5/22 18:42
     * @Param [vocImages]
     **/
    private static List<Annotation> analysisAnnotations(List<VocImage> vocImages, Map<String, Integer> vocKeyCls) {
        List<Annotation> annotations = new ArrayList<>();
        try {
            AtomicInteger id = new AtomicInteger(1);

            vocImages.forEach(vocImage -> {
                // 根据文件名分组，获取vocimage对象
                Map<String, List<VocItem>> vocItemMap = vocImage.getItems().stream().collect(Collectors.groupingBy(VocItem::getKeyCls));
                vocItemMap.forEach((keyCls, vocItems) -> {
                    Annotation annotation = new Annotation();
                    annotation.setId(id.getAndIncrement());
                    // 图片id
                    annotation.setImageId(vocImage.getId());
                    // 分类id
                    annotation.setCategoryId(vocKeyCls.get(keyCls));
                    ExtraInfo extraInfo = new ExtraInfo();
                    extraInfo.setLabelType("key");
                    List<LabelValue> labelValues = vocItems.stream()
                            .map(vocItem -> {
                                LabelValue labelValue = new LabelValue();
                                // 识别文本
                                labelValue.setText(vocItem.getTranscription());
                                // 获取坐标
                                labelValue.setLocation(getLocation(vocItem.getPoints()));
                                return labelValue;
                            })
                            .collect(Collectors.toList());
                    extraInfo.setLabelValue(labelValues);
                    annotation.setExtraInfos(extraInfo);
                    annotations.add(annotation);
                });
            });
        } catch (Exception e) {
            log.error("解析Annotations失败:", e);
        }
        return annotations;
    }


    /*
     * @return java.lang.Double[]
     * @Author HuangShiKai
     * @Description 获取坐标
     * @Date 2024/5/22 18:43
     * @Param [points]
     **/
    public static Double[] getLocation(List<Double[]> points) {
        Double[] location = new Double[8];
        try {
            // 字符的四个顶点坐标，格式为[x1, y1, x2, y2, x3, y3, x4, y4]
            location[0] = points.get(0)[0];
            location[1] = points.get(0)[1];
            location[2] = points.get(1)[0];
            location[3] = points.get(1)[1];
            location[4] = points.get(2)[0];
            location[5] = points.get(2)[1];
            location[6] = points.get(3)[0];
            location[7] = points.get(3)[1];
        } catch (Exception e) {
            log.error("获取location失败:", e);
        }
        return location;
    }

    /*
     * @return java.util.List<com.hsk.entity.CoCo.Categories>
     * @Author HuangShiKai
     * @Description 解析分类
     * @Date 2024/5/22 19:51
     * @Param [vocKeyCls]
     **/
    public static List<Categories> analysisCategories(Map<String, Integer> vocKeyCls) {
        List<Categories> categories = new ArrayList<>();
        try {
            vocKeyCls.forEach((keyCls, id) -> {
                Categories category = new Categories();
                category.setId(id);
                category.setName(keyCls);
                categories.add(category);
            });
        } catch (Exception e) {
            log.error("解析Categories失败:", e);
        }
        return categories;
    }

    /*
     * @return void
     * @Author HuangShiKai
     * @Description 创建目录
     * @Date 2024/5/22 20:45
     * @Param [targetPath]
     **/
    public static void createDir(String targetPath) {
        try {
            // 创建图片目录
            String imageDir = targetPath + File.separator + IMAGE_PATH;
            Path imagePath = Path.of(imageDir);
            // 创建标签目录
            String labelDir = targetPath + File.separator + ANNOTATION_PATH;
            Path labelPath = Path.of(labelDir);
            // 如果目标路径不存在就创建
            if (!Files.exists(imagePath) || !Files.exists(labelPath)) {
                Files.createDirectories(imagePath);
                Files.createDirectories(labelPath);
            }
        } catch (Exception e) {
            log.error("创建目录失败:", e);
        }
    }

    /*
     * @return void
     * @Author HuangShiKai
     * @Description 复制图片
     * @Date 2024/5/22 20:45
     * @Param [imageParentPath, images]
     **/
    public static void copyImage(String imageParentPath, String targetPathRoot, List<Image> images) {
        try {
            images.forEach(image -> {
                String imagePath = imageParentPath + File.separator + image.getFileName();
                String targetPath = targetPathRoot + File.separator + IMAGE_PATH + File.separator + image.getFileName();
                File sourceFile = new File(imagePath);
                File targetFile = new File(targetPath);
                FileUtil.copyFile(sourceFile, targetFile);
            });
        } catch (Exception e) {
            log.error("复制图片失败:", e);
        }
    }

}
