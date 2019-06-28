package team.educoin.transaction.util;

import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.Arrays;

/**
 * @description: 水印模块：添加和解析
 * @author: PandaClark
 * @create: 2019-06-24
 */
public class WatermarkUtil {

    // 添加水印
    public static String pdfWaterMarkEmbedTool;
    public static String imageWaterMarkEmbedTool;
    // PDF 添加水印过程中生成的临时文档（必须）
    public static String tmpFile;
    // 解析水印
    public static String pdfWaterMarkExtractTool;
    public static String imageWaterMarkExtractTool;

    static {
        try {
            pdfWaterMarkEmbedTool = ResourceUtils.getURL("resources/watermark/pdf_watermark_embed.py").getPath();
            imageWaterMarkEmbedTool = ResourceUtils.getURL("resources/watermark/image_watermark_embed.py").getPath();
            tmpFile = ResourceUtils.getURL("resources/watermark/tmp.pdf").getPath();  // PDF水印使用
            pdfWaterMarkExtractTool = ResourceUtils.getURL("classpath:static/watermark/pdf_watermark_extract.py").getPath();
            imageWaterMarkExtractTool = ResourceUtils.getURL("classpath:static/watermark/image_watermark_extract.py").getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String embedWatermark(String filename, String owner, String buyer) throws IOException, InterruptedException {
        // 获取文件类型(后缀名)
        String[] allowImageTypes = new String[]{"jpg", "jpeg", "png", "bmp", "gif"};
        String type = filename.substring(filename.lastIndexOf(".") + 1);
        boolean imageContain = Arrays.asList(allowImageTypes).contains(type);

        // 初始化参数
        String fileEmbed = FileUtil.UPLOAD_DIR + "/" + filename;
        String waterMarkInfo = "owner:" + owner + " -- buyer:" + buyer;
        // waterMarkInfo = email1 + "-" + email2 + "-" + id;  // 当前资源所有者email+当前资源下载者email+资源id
        String fileEmbedOut = FileUtil.DOWNLOAD_DIR + "/" + filename;
        // String tmpFile = ResourceUtils.getURL("classpath:static/watermark/tmp.pdf").getPath();  // PDF水印使用

        String command;

        // 调用嵌入水印脚本
        if (type.equals("pdf")){
            // 调用python脚本
            command = String.format("python3 %s %s %s %s %s", pdfWaterMarkEmbedTool, tmpFile, fileEmbed, waterMarkInfo, fileEmbedOut);
        } else if (imageContain){
            // 调用python脚本
            command = String.format("python3 %s %s %s %s", imageWaterMarkEmbedTool, fileEmbed, waterMarkInfo, fileEmbedOut);
        } else {
            return "暂时只支持下载图片和PDF文档并添加水印";
        }

        // 开始嵌入水印
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
        System.out.println("资源嵌入水印：" + result);
        return result;
    }

    public static String extractWatermark(String filename) throws InterruptedException, IOException {

        // 获取文件类型(后缀名)
        String[] allowImageTypes = new String[]{"jpg", "jpeg", "png", "bmp", "gif"};
        String type = filename.substring(filename.lastIndexOf(".") + 1);
        boolean imageContain = Arrays.asList(allowImageTypes).contains(type);

        // 设置水印提取脚本路径
        String fileInfringed = FileUtil.DOWNLOAD_DIR + "/" + filename;
        String toolUrl;
        if (imageContain){
            toolUrl = imageWaterMarkExtractTool;
        } else if (type.equals("pdf")){
            toolUrl = pdfWaterMarkExtractTool;
        } else {
            return "暂时只支持图片和PDF文档资源的水印提取";
        }

        // 调用python脚本提取水印信息
        String command = String.format("python %s %s", toolUrl, fileInfringed);
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
        System.out.println("资源嵌入水印：" + result);
        return result;
    }
}
