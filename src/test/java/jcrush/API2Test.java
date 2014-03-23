package jcrush;

import jcrush.model.Album;
import jcrush.model.MediaCrushFile;
import org.junit.Test;

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
}
