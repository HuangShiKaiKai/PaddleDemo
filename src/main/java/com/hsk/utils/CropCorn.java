package com.hsk.utils;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CropCorn {
    static {
        // 初始化OpenCV库
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void cropCorn() {
        // 确保 "output" 文件夹存在
        File outputFolder = new File("C:\\Users\\29085\\Desktop\\新建文件夹");
        if (!outputFolder.exists()) {
            outputFolder.mkdirs();
        }

        // 加载图像
        Mat image = Imgcodecs.imread("C:\\Users\\29085\\Desktop\\新建文件夹\\未标题-1.png");

        // 提取蓝色通道
        Mat blueChannel = new Mat();
        Core.extractChannel(image, blueChannel, 0);

        // 二值化图像
        int threshold = 150;
        Mat binaryImage = new Mat();
        Imgproc.threshold(blueChannel, binaryImage, threshold, 255, Imgproc.THRESH_BINARY);

        // 反转图像像素值
        Mat invertedImage = new Mat();
        Core.bitwise_not(binaryImage, invertedImage);

        // 定义结构元素
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(9, 9));

        // 闭运算
        Mat closedImage = new Mat();
        Imgproc.morphologyEx(invertedImage, closedImage, Imgproc.MORPH_CLOSE, kernel);

        // 开运算
        Imgproc.morphologyEx(closedImage, closedImage, Imgproc.MORPH_OPEN, kernel);

        // 寻找轮廓
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(closedImage, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        // 创建一个空白的二值掩模
        Mat mask = new Mat(blueChannel.size(), CvType.CV_8U, new Scalar(0));

        // 填充轮廓
        Mat filledImage = new Mat();
        Imgproc.drawContours(mask, contours, -1, new Scalar(255, 255, 255), -1);
        Imgcodecs.imwrite("filled_image.jpg", filledImage);

        // 寻找填充后的轮廓
        Imgproc.findContours(filledImage, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        // 根据轮廓进行剪裁
        List<Mat> cornImages = new ArrayList<>();
        for (Mat contour : contours) {
            double area = Imgproc.contourArea(contour);
            if (area > 1000) { // 设置面积阈值，筛选掉小面积的轮廓
                Rect rect = Imgproc.boundingRect(contour);
                Mat cornImage = new Mat(image, rect);
                cornImages.add(cornImage);
            }
        }

        // 保存剪裁下来的玉米颗粒
        for (int i = 0; i < cornImages.size(); i++) {
            Mat cornImage = cornImages.get(i);
            File outputFile = new File(outputFolder, "corn_" + i + ".jpg");
            Imgcodecs.imwrite(outputFile.getAbsolutePath(), cornImage);
        }
    }

    public static void main(String[] args) {
        cropCorn();
    }
}