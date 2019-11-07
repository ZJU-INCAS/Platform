package team.educoin.transaction.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @description: 文件上传下载目录处理工具
 * @author: PandaClark
 * @create: 2019-05-21
 */
public class FileUtil {

    // 用户上传和下载文件时的保存目录
    public static String FILE_UPLOAD_DIR;
    public static String FILE_DOWNLOAD_DIR;
    // 侵权追踪时，用户上传文件的路径
    public static String EXTRACT_UPLOAD_DIR;
    // 测试侵权追踪脚本时，上传文件和嵌入水印文件的路径
    public static String TEST_EXTRACT_UPLOAD_DIR;
    public static String TEST_EXTRACT_OUT_DIR;

    static {
        Path path = Paths.get("..","asserts").toAbsolutePath().normalize();
        try {
            if (!Files.exists(path)){
                Files.createDirectory(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static {

        Path path1 = Paths.get("..","asserts/upload").toAbsolutePath().normalize();
        Path path2 = Paths.get("..","asserts/download").toAbsolutePath().normalize();
        Path path3 = Paths.get("..","asserts/extract_upload").toAbsolutePath().normalize();
        Path path4 = Paths.get("..","asserts/test_extract_upload").toAbsolutePath().normalize();
        Path path5 = Paths.get("..","asserts/test_extract_out").toAbsolutePath().normalize();
        FILE_UPLOAD_DIR = path1.toString();
        FILE_DOWNLOAD_DIR = path2.toString();
        EXTRACT_UPLOAD_DIR = path3.toString();
        TEST_EXTRACT_UPLOAD_DIR = path4.toString();
        TEST_EXTRACT_OUT_DIR = path5.toString();
        try {
            if (!Files.exists(path1)){
                Files.createDirectory(path1);
            }
            if (!Files.exists(path2)){
                Files.createDirectory(path2);
            }
            if (!Files.exists(path3)){
                Files.createDirectory(path3);
            }
            if (!Files.exists(path4)){
                Files.createDirectory(path4);
            }
            if (!Files.exists(path5)){
                Files.createDirectory(path5);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    // 工具函数：文件大小形式化(例：convert 1024 to 1KB)
    public static String getFormatSize(long size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte(s)";
        }
        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }
        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }
        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

}
