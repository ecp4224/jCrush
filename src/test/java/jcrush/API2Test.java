package jcrush;

import jcrush.model.Album;
import jcrush.model.MediaCrushFile;
import jcrush.system.Versions;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class API2Test {
    @Test
    public void createAlbumTest() {
        Album album = new Album(); //Create a new local album
        try {
            album.add("lS_aaVyuaAjn"); //Add the file with the hash "lS_aaVyuaAjn"
        } catch (IOException e) {
            e.printStackTrace(); //If the file was not found
        }

        try {
            MediaCrushFile file = JCrush.getFile("uLImPZMgdrT_"); //Search for the file with the has "uLImPZMgdrT_"
            album.add(file); //And add it
        } catch (IOException e) {
            e.printStackTrace(); //If the file was not found
        }

        try {
            album.commit(); //Upload the album
        } catch (IOException e) {
            e.printStackTrace(); //If upload failed
        }

        System.out.println(album.isAlbumLocal() ? "null" : album.getHash());
    }

    @Test
    public void failedLookup() throws IOException {
        JCrush.setAPIVersion(Versions.VERSION_1); //TODO Remove, temp code

        MediaCrushFile file = JCrush.getFile("aaa");
        if (file != null) throw new RuntimeException("File found..it wasn't suppose to exist!");
    }

    @Test
    public void lookupFile() throws IOException {
        JCrush.setAPIVersion(Versions.VERSION_1); //TODO Remove, temp code

        MediaCrushFile m = JCrush.getFile("lS_aaVyuaAjn");
        System.out.println("Compression: " + m.getCompression());
        System.out.println("== FILES ==");
        for (int i = 0; i < m.getFiles().length; i++) {
            System.out.println((i + 1) + "). ");
            System.out.println("  - " + m.getFiles()[i].getFile());
            System.out.println("  - " + m.getFiles()[i].getFileType());
        }
        System.out.println();
    }

    //API v2 needs to be public in order for this test to work
    public void uploadAndDelete() throws IOException {
        System.out.println("Uploading file..");
        File testFile = new File("src/test/test2.gif");
        String hash = JCrush.uploadFile(testFile);
        System.out.println("DONE: " + hash);
        System.out.println();
        System.out.println("Waiting 3 seconds..");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Deleting file..");
        JCrush.delete(hash);
        System.out.println("DONE");
    }
}
