package com.example.modulith.services.organization.utils;

import com.example.modulith.utils.StringUtil;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtil {

    /**
     * csv BOM 头
     */
    public static final String CSV_BOM_HEAD = "\uFEFF";

    private static final Logger log = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 创建文件目录
     * @param dir
     */
    public static File makeDir(String dir) {
        File dirFile = new File(dir);
        dirFile.mkdirs();
        dirFile.getParentFile().setReadable(true, false);
        dirFile.getParentFile().setWritable(true, false);
        dirFile.getParentFile().setExecutable(true, false);
        dirFile.setReadable(true, false);
        dirFile.setWritable(true, false);
        dirFile.setExecutable(true, false);
        return dirFile;
    }

    /**
     * 删除文件目录
     */
    public static void deleteDir(String dir) {
        try {
            File tempDir = new File(dir);
            if (tempDir.exists()) {
                FileUtils.deleteDirectory(new File(dir));
            }
        } catch (IOException e) {
            log.error("Delete dir failed." + dir, e);
        }
    }

    /**
     * 删除文件
     */
    public static void deleteFile(String path) {
        File tempFile = new File(path);
        if (tempFile.exists() && tempFile.isFile()) {
            tempFile.delete();
        }
    }

    /**
     * 在临时目录下随机一个文件名
     * @param ext
     * @return
     */
    public static String randomTempFilePath(String ext) {
        String tempDir = System.getProperty("java.io.tmpdir");
        String path = tempDir + File.separator + UUID.randomUUID();
        if (ext != null)
            path += ext;
        return path;
    }

    /**
     * windows 测试时使用 (window获取java.io.tmpdir的临时路径时会在末尾多出一个'\')
     *                  (Linux和Mac则末尾不存在'\'
     */
    public static String randomTempFilePath2(String ext) {
        String tempDir = System.getProperty("java.io.tmpdir");
        String path = tempDir + UUID.randomUUID();
        if (ext != null)
            path += ext;
        return path;
    }
    public static String createTempFileName(String fileName) {
        String tempDir = System.getProperty("java.io.tmpdir");
        return tempDir + File.separator + fileName;
    }


    /**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }


    /**
     * 比较两个以TB,GB,MB,KB为单位的字符串大小
     * @param size1
     * @param size2
     * @return
     */
    public static int compareSize(String size1, String size2) {
        if (StringUtil.isEmptyOrBlank(size1) && StringUtil.isEmptyOrBlank(size2)) {
            return 0;
        }
        if (StringUtil.isEmptyOrBlank(size1)) {
            return -1;
        }
        if (StringUtil.isEmptyOrBlank(size2)) {
            return 1;
        }
        if (size1.equalsIgnoreCase(size2)) {
            return 0;
        }
        BigDecimal size1Number = new BigDecimal(size1.substring(0, size1.length() - 2));
        String size1Unit = size1.substring(size1.length() - 2);

        BigDecimal size2Number = new BigDecimal(size2.substring(0, size2.length() - 2));
        String size2Unit = size2.substring(size2.length() - 2);

        if (size1Unit.equalsIgnoreCase(size2Unit)) {
            return size1Number.compareTo(size2Number);
        }else {
            if (size1Unit.equalsIgnoreCase("TB")) {
                return 1;
            }
            if (size2Unit.equalsIgnoreCase("TB")) {
                return -1;
            }
            if (size1Unit.equalsIgnoreCase("GB")) {
                return 1;
            }
            if (size2Unit.equalsIgnoreCase("GB")) {
                return -1;
            }
            if (size1Unit.equalsIgnoreCase("MB")) {
                return 1;
            }
            if (size2Unit.equalsIgnoreCase("MB")) {
                return -1;
            }
            return size1.compareToIgnoreCase(size2);
        }
    }


    /**
     * 文件字节单位转换
     * @param fileSize
     * @return
     */
    public static String formetFileSize(long fileSize){
        DecimalFormat df = new DecimalFormat("#.0");
        String fileSizeString = "";
        if (fileSize < 1024) {
            fileSizeString = df.format((double) fileSize) + "B";
        } else if (fileSize < 1048576) {
            fileSizeString = df.format((double) fileSize / 1024) + "K";
        } else if (fileSize < 1073741824) {
            fileSizeString = df.format((double) fileSize / 1048576) + "M";
        }else {
            fileSizeString = df.format((double) fileSize / 1073741824) + "G";
        }
        return fileSizeString;
    }

    public static File createTempFile() throws IOException {
        return createTempFile(".tmp");
    }

    public static File createTempFile(String suffix) throws IOException {
        try {
            return Files.createTempFile(UUID.randomUUID().toString(), suffix).toFile();
        } catch (IOException e) {
            log.error("local temp file cannot be created", e);
            throw e;
        }
    }

    public static List<String> getMediaURLs(Map<String, String> mediaMap, String mediaIds) {
        if (StringUtil.isEmptyOrBlank(mediaIds)) {
            return new ArrayList<>();
        }
        return Stream.of(mediaIds.split(StringUtil.COMMA))
                .map(String::toUpperCase)
                .map(mediaMap::get)
                .filter(url -> !StringUtil.isEmptyOrBlank(url))
                .collect(Collectors.toList());
    }


    /**
     * 压缩图片文件。
     * 创建一个临时文件，将指定图片按指定质量进行压缩后，保存到这个临时文件中。
     *
     * @param imageFile 待压缩的图片文件。
     * @param imageFormat 图片格式，如"jpg"、"png"等。
     * @param quality 压缩质量，取值范围为0.0到1.0，其中0.0为最差质量（最大压缩比），1.0为最好质量（最小压缩比）。
     * @return 压缩后的图片文件，如果压缩失败则返回null。
     */
    public static File compressImage(File imageFile, float quality, String imageFormat) {
        try {
            // 创建一个临时文件，用于存放压缩后的图片
            File compressedImageFile = FileUtil.createTempFile(UUID.randomUUID() + "." + imageFormat);
            // 读取原始图片
            BufferedImage image = ImageIO.read(imageFile);
            // 根据指定质量缩放图片
            java.awt.Image compressedImage = image.getScaledInstance((int) (image.getWidth() * quality), (int) (image.getHeight() * quality), java.awt.Image.SCALE_SMOOTH);
            // 创建一个与缩放后的图片大小相匹配的 BufferedImage 对象
            BufferedImage resultImage = new BufferedImage((int) (image.getWidth() * quality), (int) (image.getHeight() * quality), image.getType());
            // 使用 Graphics2D 绘制缩放后的图片
            Graphics2D graphics2D = resultImage.createGraphics();
            graphics2D.drawImage(compressedImage, 0, 0, null);
            graphics2D.dispose();
            // 将绘制后的图片写入到临时文件中
            ImageIO.write(resultImage, imageFormat, compressedImageFile);
            return compressedImageFile;
        } catch (IOException e) {
            // 图片压缩失败，打印异常信息并返回 null
            e.printStackTrace();
            return null;
        }
    }

    public static void createImageByHtml(String cohortReadinessReportHtmlPath, String cohortReadinessReportImagePath) {
        try {
            // googleslider/bin/wkhtmltoimage.exe
            String cmd = "googleslider/bin/wkhtmltoimage.exe ---crop-h 2048 --crop-w 2048 --crop-x 0 --crop-y 0 --width 1980 --format png " + cohortReadinessReportHtmlPath + " " + cohortReadinessReportImagePath;
            Process process = Runtime.getRuntime().exec(cmd);
            process.waitFor();
        } catch (Exception e) {
            log.error("Create image by html failed.", e);
        }
    }

    /**
     * 移动路径
     *
     * @param newFileName     文件名
     * @param oldFileName 生成文档
     * @param more         文件后缀
     * @return {@link Path}
     * @throws IOException IOException
     */
    public static Path movePath(String newFileName, File oldFileName, String more) throws IOException {
        // 创建新目录
        File newDir = FileUtil.makeDir(oldFileName.getParent() + File.separator + UUID.randomUUID());
        // 定义新路径，临时目录 + uuid 作为子目录，再拼接文件名
        Path movePath = Paths.get(newDir.getPath(), newFileName + more);
        // 生成新文件
        return Files.move(oldFileName.toPath(),
                Files.createFile(movePath),
                StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * 获取 image 的 byte 数组
     *
     * @param image 图片
     * @param format 传递 JPG 或者 PNG
     * @return {@link byte[]} 图片对应的字节数组
     */
    public static byte[] getBufferByteArray(BufferedImage image, String format) {
        return getBufferByteArray(image, format, 5.20 / 3);
    }

    /**
     * 获取 image 的 byte 数组
     *
     * @param image 图片
     * @param format 传递 JPG 或者 PNG
     * @return {@link byte[]} 图片对应的字节数组
     */
    public static byte[] getBufferByteArray(BufferedImage image, String format, double targetAspectRatio) {
        // 如果图片为空，直接返回
        if (null == image) return null;
        // 获取图片的格式名称
        String formatName = (!StringUtil.isEmptyOrBlank(format) && format.charAt(0) == '.') ? format.substring(1) : format;
        // 如果不存在格式，则默认是 png
        if (!StringUtil.isEmptyOrBlank(format)) formatName = "png";
        BufferedImage newImage = image;
        // 创建字节数组读取流
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            // 计算原始图片的宽高比
            double aspectRatio = (double) image.getWidth() / image.getHeight();

            // 如果原始图片的宽高比不接近目标宽高比
            if (Math.abs(aspectRatio - targetAspectRatio) > 0.01) {
                // 计算裁剪后的图片的宽度和高度
                int newWidth = 0;
                int newHeight = 0;
                if (aspectRatio > targetAspectRatio) {
                    newHeight = image.getHeight();
                    newWidth = (int) (newHeight * targetAspectRatio);
                } else {
                    newWidth = image.getWidth();
                    newHeight = (int) (newWidth / targetAspectRatio);
                }

                // 计算裁剪区域的位置，使得裁剪后的图片在水平和垂直方向上都能居中
                int x = (image.getWidth() - newWidth) / 2;
                int y = (image.getHeight() - newHeight) / 2;
                // 每次获取图片只获取其中的一部分
                newImage = image.getSubimage(x, y, newWidth, newHeight);
            }
            // 读取流并返回
            ImageIO.write(newImage, formatName, os);
            return os.toByteArray();
        } catch (Exception e) {
            return null;
        } finally {
            IOUtils.closeQuietly(os);
        }
    }

}
