package com.hsk.entity.voc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: huangshikai
 * @Date: 2024/5/2213:04
 * @Description: ocr标注实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VocImage {
    // 图片文件名
    private String fileName;
    // 图片id
    private Integer id;
    // 图片中所有识别出的字符或符号
    private List<VocItem> items;
}
