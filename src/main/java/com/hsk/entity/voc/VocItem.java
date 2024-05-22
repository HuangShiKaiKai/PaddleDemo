package com.hsk.entity.voc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: huangshikai
 * @Date: 2024/5/2213:09
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VocItem {
    // 识别出的文本
    @JsonProperty("transcription")
    private String transcription;
    // 字符的四个顶点坐标，格式为[x1, y1, x2, y2, x3, y3, x4, y4]
    @JsonProperty("points")
    private List<Double[]> points;
    // 是否困难的识别任务
    @JsonProperty("difficult")
    private boolean difficult;
    // 识别出的文本类别
    @JsonProperty("key_cls")
    private String keyCls;
}
