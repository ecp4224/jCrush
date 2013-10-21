package jcrush.system;

import jcrush.model.CrushedFile;
import jcrush.model.FileStatus;
import jcrush.model.MediaCrushFile;

import javax.imageio.ImageIO;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;

public class Utils {
    public static void setHash(MediaCrushFile file, String hash) throws NoSuchFieldException, IllegalAccessException {
        Field f = file.getClass().getDeclaredField("hash");
        f.setAccessible(true);
        f.set(file, hash);
    }

    public static void setStatus(MediaCrushFile file, FileStatus status) throws NoSuchFieldException, IllegalAccessException {
        Field f = file.getClass().getDeclaredField("status");
        f.setAccessible(true);
        f.set(file, status);
    }

    //oh sweet jesus
    public static MediaCrushFile convertMapToFile(Map map) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, NoSuchFieldException {
        Constructor<?> _construct = MediaCrushFile.class.getDeclaredConstructors()[0];
        _construct.setAccessible(true);
        MediaCrushFile file = (MediaCrushFile) _construct.newInstance();
        for (Object key : map.keySet()) {
            if (key.equals("files")) {
                ArrayList<Map> files = (ArrayList<Map>) map.get(key);
                ArrayList<CrushedFile> crushedFiles = new ArrayList<CrushedFile>();
                for (Map m : files) {
                    CrushedFile c = new CrushedFile((String)m.get("file"), (String)m.get("type"));
                    crushedFiles.add(c);
                }
                Field f = MediaCrushFile.class.getDeclaredField("files");
                f.setAccessible(true);
                f.set(file, crushedFiles.toArray(new CrushedFile[crushedFiles.size()]));
            } else {
                try {
                    Field f = MediaCrushFile.class.getDeclaredField(key.toString());
                    f.setAccessible(true);
                    f.set(file, map.get(key));
                } catch (NoSuchFieldException ignored) { }
            }
        }

        return file;
    }

    public static void copy(InputStream input, OutputStream output, int bufferSize) throws IOException {
        byte[] buf = new byte[bufferSize];
        int bytesRead = input.read(buf);
        while (bytesRead != -1) {
            output.write(buf, 0, bytesRead);
            bytesRead = input.read(buf);
        }
        output.flush();
    }

    public static String toContentType(File file) {
        String fName = file.getName();
        String[] parts = fName.split("\\.");
        String ext = parts[parts.length - 1].toLowerCase();

        if (ext.equals("png") || ext.equals("jpg") || ext.equals("jpeg") || ext.equals("gif"))
            return "image/" + ext;
        else if (ext.equals("mp4") || ext.equals("ogv"))
            return "video/" + ext;
        else if (ext.equals("mp3") || ext.equals("ogg"))
            return "audio/" + ext;
        else
            return null;
    }
}
