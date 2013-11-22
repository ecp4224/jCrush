package jcrush;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import jcrush.model.MediaCrushFile;

import org.junit.Test;

public class APITest {
    private static final String TEST_HASH = "CPvuR5lRhmS0";

    @Test(expected = IOException.class)
    public void failedUploadFileTest() throws IOException {
        System.out.println("=== JCrush.uploadFile(File) ===");

        File testFile = new File("src/test/failedTest.gif");
        JCrush.uploadFile(testFile);
        System.out.println("TEST COMPLETE");
    }
    
    //Removing test until issue #354 in MediaCrush is fixed
    public void uploadAndDeleteFile() throws IOException {
        System.out.println("=== JCrush.uploadFile(File) ===");
        System.out.println("=== JCrush.delete(String) ===");

        File testFile = new File("src/test/test.gif");
        JCrush.delete("bTVxrDGQ4ks_");
        String hash = JCrush.uploadFile(testFile);
        System.out.println(hash);
        System.out.println("TEST COMPLETE");
    }

    //Removing test until issue #354 in MediaCrush is fixed
    public void uploadViaURL() throws IOException {
        System.out.println("=== JCrush.uploadFileViaURL(URL) ===");
        System.out.println("=== JCrush.uploadFileViaURL(String) ===");
        URL url1 = new URL("http://rmart.org/45341/Src/1ac65c7f45047fb99d58d3c87052fcc1.gif");
        String url2 = "http://25.media.tumblr.com/tumblr_mec3lqCnpg1qce7tgo1_500.gif";

        String hash2 = JCrush.uploadFileViaURL(url1);
        String hash = JCrush.uploadFileViaURL(url2);

        System.out.println(hash);
        System.out.println(hash2);
        System.out.println("TEST COMPLETE");
    }

    @Test(expected = IOException.class)
    public void failedDeleteHashTest() throws IOException {
        System.out.println("=== JCrush.delete(String) ===");

        JCrush.delete(TEST_HASH);
    }

    @Test(expected = IOException.class)
    public void failedDeleteFileTest() throws IOException {
        System.out.println("=== JCrush.delete(MediaCrushFile) ===");

        MediaCrushFile file = JCrush.getFileInfo(TEST_HASH);

        JCrush.delete(file);
    }

    @Test
    public void fileStatusTest() throws Exception {
        System.out.println("=== JCrush.getFileStatus(String) ===");

        MediaCrushFile file = JCrush.getFileStatus(TEST_HASH);

        System.out.println("Status: " + file.getStatus());

        System.out.println("TEST COMPLETE");
    }
    @Test
    public void fileExistsTest() throws Exception {
        System.out.println("=== JCrush.doesExists(String) ===");
        boolean result = JCrush.doesExists("aaaaa");
        if (result)
            throw new Exception("Test returned true! Should of returned false!");

        result = JCrush.doesExists(TEST_HASH);
        if (!result)
            throw new Exception("Test returned false! Should of returned true!");

        System.out.println("TEST COMPLETE");
    }
    @Test
    public void fileTest() throws IOException {
        System.out.println("=== JCrush.getFileInfo(String) ===");
        MediaCrushFile m = JCrush.getFileInfo(TEST_HASH);
        System.out.println("Compression: " + m.getCompression());
        System.out.println("== ORIGINAL FILE ==");
        System.out.println("  - " + m.getOriginalFile().getFile());
        System.out.println("  - " + m.getOriginalFile().getFileType());
        System.out.println("== FILES ==");
        for (int i = 0; i < m.getFiles().length; i++) {
            System.out.println((i + 1) + "). ");
            System.out.println("  - " + m.getFiles()[i].getFile());
            System.out.println("  - " + m.getFiles()[i].getFileType());
        }
        System.out.println();
        System.out.println("TEST COMPLETE");
    }

    @Test
    public void multipleFileTest() throws IOException {
        System.out.println("=== JCrush.getFileInfos(String...) ===");
        MediaCrushFile[] files = JCrush.getFileInfos(TEST_HASH, "tVWMM_ziA3nm");
        for (int i = 0; i < files.length; i++) {
            MediaCrushFile m = files[i];

            System.out.println("== FILE " + (i + 1) + " ==");
            System.out.println("  Compression: " + m.getCompression());
            System.out.println("  == ORIGINAL FILE ==");
            System.out.println("    - " + m.getOriginalFile().getFile());
            System.out.println("    - " + m.getOriginalFile().getFileType());
            System.out.println("  == FILES ==");
            for (int ii = 0; ii < m.getFiles().length; ii++) {
                System.out.println((ii + 1) + "). ");
                System.out.println("    - " + m.getFiles()[ii].getFile());
                System.out.println("    - " + m.getFiles()[ii].getFileType());
            }
            System.out.println();
        }
        System.out.println("TEST COMPLETE");
    }
}
