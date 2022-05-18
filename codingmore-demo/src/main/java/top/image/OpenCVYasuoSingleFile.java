package top.image;

import nu.pattern.OpenCV;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.imgcodecs.Imgcodecs;

public class OpenCVYasuoSingleFile {
    private final static String docPath = System.getProperty("user.home")
            + "/Documents/Github/toBeBetterJavaer/images/";


    public static void main(String[] args) {
        OpenCV.loadShared();
        String filename = "logo.png";
        Mat sourceImage = Imgcodecs.imread(docPath+filename);

        MatOfInt dstImageParam = new MatOfInt(Imgcodecs.IMWRITE_JPEG_QUALITY, 50);
        dstImageParam = new MatOfInt(Imgcodecs.IMWRITE_PNG_COMPRESSION, 9);
        Imgcodecs.imwrite(docPath+filename, sourceImage, dstImageParam);

    }
}
