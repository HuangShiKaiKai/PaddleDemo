# Paddle工具集合

## 一、PPOCRLabel数据集转COCO数据集

> PPOCRLabel目录结构

```bash
dataset
	-- iamge-1
	-- iamge-2
	-- iamge-3
	-- ...
	-- Label.txt
```

> COCO数据集结构

```bash
dataset
	annotations
		-- coco.json
	images
        -- iamge-1
        -- iamge-2
        -- iamge-3
        -- ...
```

> 示例

```java
@Test
void ocrDataSetToCoco() {
    // PPOCRLabel标签文件路径
    String labelTxtPath = "F:\\角标ocr\\Label.txt";
    // 目标路径
    String targetPath = "F:\\ocrCOCO";
    // 转换
    DataSetUtil.ocrDataSetToCoco(labelTxtPath, targetPath);
}
```



