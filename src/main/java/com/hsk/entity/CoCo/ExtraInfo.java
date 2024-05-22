package com.hsk.entity.CoCo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: huangshikai
 * @Date: 2024/5/2210:53
 * @Description: 标注信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExtraInfo {
    // 标注类型是key还是value
    @JsonProperty("label_type")
    private String labelType;
    // 标注框
    @JsonProperty("label_value")
    private List<LabelValue> labelValue;
}
