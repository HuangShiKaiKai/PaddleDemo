package com.hsk.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: huangshikai
 * @Date: 2024/5/2218:32
 * @Description: 每个标注
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VocItemVo {
    // 图片文件名
    private String fileName;
    // 图片id
    private Integer id;
    // 识别出的文本
    private String transcription;
    // 字符的四个顶点坐标，格式为[x1, y1, x2, y2, x3, y3, x4, y4]
    private List<Double[]> points;
    // 是否困难的识别任务
    private boolean difficult;
    // 识别出的文本类别id
    private Integer keyClsId;

}
