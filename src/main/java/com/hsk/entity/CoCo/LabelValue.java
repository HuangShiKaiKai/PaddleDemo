package com.hsk.entity.CoCo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: huangshikai
 * @Date: 2024/5/2210:51
 * @Description: 单行标注信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LabelValue {
    // 单行标注【左上(x1,y1), 左下(x2,y2), 右下(x3,y3), 右上(x4,y4)】
    @JsonProperty("location")
    private Double[] location;
    // 单行标注内容
    @JsonProperty("text")
    private String text;
}
