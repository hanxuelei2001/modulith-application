package com.example.modulith.services.department.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
    private static Logger logger = LoggerFactory.getLogger(ZipUtil.class);

    /**
     *
     * @param inputFolder 输入文件夹
     * @param zipFilePath 输出文件名
     * @throws IOException
     */
    public static void zipFolder(File inputFolder, String zipFilePath ) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(zipFilePath);
        try(ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream)) {
            File[] contents = inputFolder.listFiles();
            for (File f : contents) {
                if (f.isFile() && (f.getName().endsWith(".jpg") || f.getName().endsWith(".pdf")  || f.getName().endsWith(".xlsx"))) {
                    zipFile(f, zipOutputStream);
                }
            }
        }
    }

    public static void zipFile(File inputFile,ZipOutputStream zipOutputStream) throws IOException {
       // A ZipEntry represents a file entry in the zip archive
       // We name the ZipEntry after the original file's name
       ZipEntry zipEntry = new ZipEntry(inputFile.getName());
       zipOutputStream.putNextEntry(zipEntry);
       try(FileInputStream fileInputStream = new FileInputStream(inputFile)) {
            byte[] buf = new byte[1024];
            int bytesRead;
            // Read the input file by chucks of 1024 bytes
            // and write the read bytes to the zip stream
            while ((bytesRead = fileInputStream.read(buf)) > 0) {
                zipOutputStream.write(buf, 0, bytesRead);
            }
            // close ZipEntry to store the stream to the file
        }
       zipOutputStream.closeEntry();
       logger.info("Regular file :" + inputFile.getCanonicalPath() + " is zipped to archive :" + inputFile.getName());
    }

    /**
     * 解压.gz文件
     */
  public static void gZFile(GZIPInputStream in, File outdir, String name) throws IOException {
      byte[] buffer = new byte[1024];
      BufferedOutputStream out = new BufferedOutputStream(
              new FileOutputStream(new File(outdir, name)));
      int count = -1;
      while ((count = in.read(buffer)) != -1) {
          out.write(buffer, 0, count);
      }
      out.close();
  }
}
