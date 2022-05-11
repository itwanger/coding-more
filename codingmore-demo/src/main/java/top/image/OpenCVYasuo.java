package top.image;

import nu.pattern.OpenCV;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.imgcodecs.Imgcodecs;

public class OpenCVYasuo {
    private final static String docPath = "/Users/maweiqing/Documents/GitHub/toBeBetterJavaer/docs/";

    public static void main(String[] args) {
        OpenCV.loadShared();

//        Mat src = Imgcodecs.imread(imagePath);
        MatOfInt dstImage = new MatOfInt(Imgcodecs.IMWRITE_JPEG_QUALITY, 1);
//        Imgcodecs.imwrite("resized_image.jpg", sourceImage, dstImage);

    }
}
