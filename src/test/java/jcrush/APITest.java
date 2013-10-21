package jcrush;

import jcrush.model.MediaCrushFile;
import java.io.File;
import org.junit.Test;

import java.io.IOException;

public class APITest {
    private static final String TEST_HASH = "CPvuR5lRhmS0";


    @Test(expected = IOException.class)
    public void failedUploadFileTest() throws IOException {
        System.out.println("=== JCrush.uploadFile(File) ===");

        File testFile = new File("failedTest.gif");
        JCrush.uploadFile(testFile);
        System.out.println("TEST COMPLETE");
    }

    @Test
    public void uploadAndDeleteFile() throws IOException {
        System.out.println("=== JCrush.uploadFile(File) ===");
        System.out.println("=== JCrush.delete(String) ===");

        File testFile = new File("test.gif");
        String hash = JCrush.uploadFile(testFile);
        JCrush.delete(hash);
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

        MediaCrushFile file = JCrush.getFile(TEST_HASH);

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
        System.out.println("=== JCrush.getFile(String) ===");
        MediaCrushFile m = JCrush.getFile(TEST_HASH);
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
        System.out.println("=== JCrush.getFiles(String...) ===");
        MediaCrushFile[] files = JCrush.getFiles(TEST_HASH, "tVWMM_ziA3nm");
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
