package com.hsk.entity.CoCo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: huangshikai
 * @Date: 2024/5/2210:55
 * @Description: 采用类coco的标注方式，整体标注格式基本与Coco数据集相似，根目录下分为两个文件夹，其中annotations中存放标注文件、images中存放原始图片（注：所有图像的标注均放在annotations目录中的同一json文件中）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataSet {
    // 图片
    @JsonProperty("images")
    private List<Image> images;

    // 标注文件
    @JsonProperty("annotations")
    private List<Annotation> annotations;

    // 类别
    @JsonProperty("categories")
    private List<Categories> categories;
}
