package services.utils;

import play.api.Play;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zipper {

    public static boolean zipMultipleFiles(List<String> inputFiles, String outputFileName) throws Exception {
        boolean status = false;
        FileOutputStream fos = null;
        ZipOutputStream zipOut = null;
        try {
            fos = new FileOutputStream(Play.current().path().getAbsolutePath() + outputFileName);
            zipOut = new ZipOutputStream(fos);
            for (String srcFile : inputFiles) {
                File fileToZip = null;
                FileInputStream fis = null;
                ZipEntry zipEntry = null;
                try {
                    fileToZip = new File(Play.current().path().getAbsolutePath() + srcFile);
                    fis = new FileInputStream(fileToZip);
                    zipEntry = new ZipEntry(fileToZip.getName());
                    zipOut.putNextEntry(zipEntry);
                    byte[] bytes = new byte[1024];
                    int length;
                    while ((length = fis.read(bytes)) >= 0) {
                        zipOut.write(bytes, 0, length);
                    }
                    fis.close();
                } finally {
                    fileToZip = null;
                    fis = null;
                    zipEntry = null;
                }
            }
            zipOut.close();
            fos.close();
            status = true;
        } finally {
            fos = null;
            zipOut = null;
        }
        return status;
    }
}
