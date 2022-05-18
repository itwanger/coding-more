package top.image;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import nu.pattern.OpenCV;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.util.List;

public class OpenCVYasuoDicFile {
    private final static String docPath = System.getProperty("user.home")
            + "/Documents/Github/toBeBetterJavaer/images/";

    public static void main(String[] args) {
        OpenCV.loadShared();

        List<File> files = FileUtil.loopFiles(docPath);
        for (File file: files) {
            if (!FileNameUtil.isType(file.getName(), "jpg","png","jpeg")) {
                System.out.println(file.getAbsolutePath());
                continue;
            }
            Mat sourceImage = Imgcodecs.imread(file.getAbsolutePath());
            String extName = FileUtil.extName(file);
            MatOfInt dstImageParam = null;
            if ("png".equals(extName)) {
                dstImageParam = new MatOfInt(
                        Imgcodecs.IMWRITE_PNG_COMPRESSION, 9);

            } else if ("jpg".equals(extName) || "jpeg".equals(extName)) {
//                dstImageParam = new MatOfInt(Imgcodecs.IMWRITE_JPEG_QUALITY, 50);
            }

            if (dstImageParam != null) {
                Imgcodecs.imwrite(file.getAbsolutePath(), sourceImage, dstImageParam);
            }
        }
    }
}
