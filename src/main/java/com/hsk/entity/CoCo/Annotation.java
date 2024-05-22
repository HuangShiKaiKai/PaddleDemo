package com.hsk.entity.CoCo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: huangshikai
 * @Date: 2024/5/2210:39
 * @Description: 标注文件信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Annotation {
    // 标注信息id，依次递增
    @JsonProperty("id")
    private int id;
    // images 中对应图片的id
    @JsonProperty("image_id")
    private int imageId;
    // 标注框对应的 识别字段id，该id在 categories 中定义
    @JsonProperty("category_id")
    private int categoryId;
    //
    @JsonProperty("bbox")
    private Double[] bbox;
    // 目前无需更改
    @JsonProperty("iscrowd")
    private int iscrowd = 0;
    // 目前无需更改
    @JsonProperty("area")
    private Double area = 100.0;
    // coco中分割相关的信息，默认填空即可
    @JsonProperty("segmentation")
    private String[] segmentation;
    // 标注信息
    @JsonProperty("extra_infos")
    private ExtraInfo extraInfos;

}
