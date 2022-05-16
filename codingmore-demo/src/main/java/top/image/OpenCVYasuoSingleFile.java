package top.image;

import nu.pattern.OpenCV;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.imgcodecs.Imgcodecs;

public class OpenCVYasuoSingleFile {
    private final static String [] docPaths = {
            "/Users/maweiqing/Documents/GitHub/toBeBetterJavaer/docs/",
            "/Users/itwanger/Documents/Github/toBeBetterJavaer/images/itwanger/",
    };

    public static void main(String[] args) {
        OpenCV.loadShared();
        String filename = "xingbiaogongzhonghao.png";
        Mat sourceImage = Imgcodecs.imread(docPaths[1]+filename);

        MatOfInt dstImageParam = new MatOfInt(Imgcodecs.IMWRITE_JPEG_QUALITY, 50);
        dstImageParam = new MatOfInt(Imgcodecs.IMWRITE_PNG_COMPRESSION, 50);
        Imgcodecs.imwrite(docPaths[1]+filename, sourceImage, dstImageParam);

    }
}
