package com.hsk.entity.CoCo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: huangshikai
 * @Date: 2024/5/2210:38
 * @Description: 图片信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    // 图片名
    @JsonProperty("file_name")
    private String fileName;
    // 图片高度
    @JsonProperty("height")
    private int height;
    // 图片宽度
    @JsonProperty("width")
    private int width;
    // 图片的深度，一般固定为3，无需改变
    @JsonProperty("depth")
    private int depth = 3;
    // 图片id，如果有多个图片，依次递增
    @JsonProperty("id")
    private int id;
}
