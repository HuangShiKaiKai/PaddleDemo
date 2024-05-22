package com.hsk.entity.CoCo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: huangshikai
 * @Date: 2024/5/2210:54
 * @Description: 识别字段定义
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Categories {
    // category_id
    @JsonProperty("id")
    private int id;
    // 识别字段名称
    @JsonProperty("name")
    private String name;

}
