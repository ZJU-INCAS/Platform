package team.educoin.transaction.util;

import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * @description: 水印模块：添加和解析
 * @author: PandaClark
 * @create: 2019-06-24
 */
public class WatermarkUtil {

    // 添加水印
    private static String pdfWaterMarkEmbedTool;
    private static String imageWaterMarkEmbedTool;
    // PDF 添加水印过程中生成的临时文档（必须）
    private static String tmpFile;
    // 解析水印
    private static String pdfWaterMarkExtractTool;
    private static String imageWaterMarkExtractTool;

    static {
        Path watermark = Paths.get("watermark").toAbsolutePath().normalize();
        try {
            // 本机部署，jar 包下路径失效
            // pdfWaterMarkEmbedTool = ResourceUtils.getURL("classpath:watermark/pdf_watermark_embed.py").getPath();
            // imageWaterMarkEmbedTool = ResourceUtils.getURL("classpath:watermark/image_watermark_embed.py").getPath();
            // tmpFile = ResourceUtils.getURL("classpath:watermark/tmp.pdf").getPath();  // PDF水印使用
            // pdfWaterMarkExtractTool = ResourceUtils.getURL("classpath:watermark/pdf_watermark_extract.py").getPath();
            // imageWaterMarkExtractTool = ResourceUtils.getURL("classpath:watermark/image_watermark_extract.py").getPath();

            // 服务端 jar 包部署
            pdfWaterMarkEmbedTool = ResourceUtils.getURL(FileUtil.WATERMARK_DIR + "/pdf_watermark_embed.py").getPath();
            imageWaterMarkEmbedTool = ResourceUtils.getURL(FileUtil.WATERMARK_DIR + "/image_watermark_embed.py").getPath();
            tmpFile = ResourceUtils.getURL(FileUtil.WATERMARK_DIR + "/tmp.pdf").getPath();  // PDF水印使用
            pdfWaterMarkExtractTool = ResourceUtils.getURL(FileUtil.WATERMARK_DIR + "/pdf_watermark_extract.py").getPath();
            imageWaterMarkExtractTool = ResourceUtils.getURL(FileUtil.WATERMARK_DIR + "/image_watermark_extract.py").getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * =============================================================
     * @desc
     * @author PandaClark
     * @date 2019/11/4 4:59 PM
     * @param test == 0：普通用户下载资源时下载水印；test == 1：测试添加水印的脚本
     * @return java.lang.String
     * =============================================================
     */
    public static String embedWatermark(String id, String filename, String owner, String buyer, int test) throws IOException, InterruptedException {
        // 获取文件类型(后缀名)
        String[] allowImageTypes = new String[]{"jpg", "jpeg", "png", "bmp", "gif"};
        String type = filename.substring(filename.lastIndexOf(".") + 1);
        boolean imageContain = Arrays.asList(allowImageTypes).contains(type);

        // 初始化参数
        String fileEmbed = test ==  0 ? (FileUtil.FILE_UPLOAD_DIR + "/" + filename) : (FileUtil.TEST_EXTRACT_UPLOAD_DIR + "/" + filename);
        String waterMarkInfo = id + ";" + owner + ";" + buyer;
        String fileEmbedOut = test == 0 ? (FileUtil.FILE_DOWNLOAD_DIR + "/" + filename) : (FileUtil.TEST_EXTRACT_OUT_DIR + "/" + filename);
        // String tmpFile = ResourceUtils.getURL("classpath:static/watermark/tmp.pdf").getPath();  // PDF水印使用

        String[] command;

        // 调用嵌入水印脚本
        if (type.equals("pdf")){
            // 调用python脚本
            // command = String.format("python3 %s %s %s %s %s", pdfWaterMarkEmbedTool, tmpFile, fileEmbed, waterMarkInfo, fileEmbedOut);
            command = new String[]{
                    "python3",
                    pdfWaterMarkEmbedTool,
                    tmpFile,
                    fileEmbed,
                    waterMarkInfo,
                    fileEmbedOut
            };
        } else if (imageContain){
            // 调用python脚本
            // 脚本工具位置 需要嵌入水印文件路径 嵌入的水印信息 嵌入水印后的导出文件路径
            fileEmbedOut = fileEmbedOut.substring(0, fileEmbedOut.lastIndexOf('.')) + ".png";
            // command = String.format("python3 %s %s %s %s", imageWaterMarkEmbedTool, fileEmbed, waterMarkInfo, fileEmbedOut);
            command = new String[]{
                    "python",
                    imageWaterMarkEmbedTool,
                    fileEmbed,
                    waterMarkInfo,
                    fileEmbedOut
            };
        } else {
            return "暂时只支持下载图片和PDF文档并添加水印";
        }

        System.out.println("command: " + Arrays.toString(command));
        Process process = Runtime.getRuntime().exec(command);
        process.waitFor();
        BufferedInputStream in = new BufferedInputStream(process.getInputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;
        String result = "default";
        while ((line = br.readLine()) != null) {
            result = line;
        }
        br.close();
        in.close();
        // 打印嵌入水印结果
        System.out.println("给资源嵌入水印：" + result);
        return result;
    }

    /**
     * =============================================================
     * @desc
     * @author PandaClark
     * @date 2019/11/4 5:04 PM
     * @param test == 0：机构用户上传疑似侵权文件提取水印；test == 1：测试提取水印的脚本
     * @return java.lang.String
     * =============================================================
     */
    public static String extractWatermark(String filename, int test) throws InterruptedException, IOException {

        // 获取文件类型(后缀名)
        String[] allowImageTypes = new String[]{"jpg", "jpeg", "png", "bmp", "gif"};
        String type = filename.substring(filename.lastIndexOf(".") + 1);
        boolean imageContain = Arrays.asList(allowImageTypes).contains(type);

        // 设置水印提取脚本路径
        String fileInfringed = test == 0 ? (FileUtil.EXTRACT_UPLOAD_DIR + "/" + filename) : (FileUtil.TEST_EXTRACT_OUT_DIR + "/" + filename);
        String toolUrl;
        if (imageContain){
            toolUrl = imageWaterMarkExtractTool;
            // 水印提取脚本只支持提取 png 格式的图片
            fileInfringed = fileInfringed.substring(0, fileInfringed.lastIndexOf(".")) + ".png";
        } else if (type.equals("pdf")){
            toolUrl = pdfWaterMarkExtractTool;
        } else {
            return "暂时只支持图片和PDF文档资源的水印提取";
        }

        // 调用python脚本提取水印信息
        String command = String.format("python %s %s", toolUrl, fileInfringed);
        System.out.println("command: " + command);
        Process process = Runtime.getRuntime().exec(command);
        process.waitFor();
        BufferedInputStream in = new BufferedInputStream(process.getInputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;
        String result = null;
        while ((line = br.readLine()) != null) {
            result = line;
        }
        br.close();
        in.close();
        // 打印嵌入水印结果
        System.out.println("提取嵌入资源水印：" + result);
        return result;
    }
}
