package jcrush;

import static jcrush.system.Constants.MEDIA_CRUSH_URL;
import static jcrush.system.Constants.API_DIRECTORY;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jcrush.io.ConnectionType;
import jcrush.io.Requester;
import jcrush.model.MediaCrushFile;
import jcrush.system.Validator;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.HashMap;

public class JCrush {
    private static final Gson GSON;

    static {
        GSON = new Gson();
    }

    /**
     * Returns information about the file whose hash is <b>hash</b>
     * @param hash
     *            <b>NOT NULLABLE</b>
     *            <br></br>
     *            - The hash of the file to retrieve.
     * @return
     *        The file represented as a {@link MediaCrushFile} object
     * @throws IOException
     *                    An {@link IOException} can be thrown for the following reasons: <br></br>
     *                    * There was an error invoking {@link jcrush.io.Requester#connect()} <br></br>
     *                    * The json returned contained a 404 error
     */
    public static MediaCrushFile getFile(String hash) throws IOException {
        Validator.validateNotNull(hash, "hash");

        URL uri = new URL(MEDIA_CRUSH_URL + API_DIRECTORY + hash);
        Requester requester = new Requester(ConnectionType.GET, uri);
        requester.setRecieve(true);
        requester.connect();

        String json = requester.getResponse();
        Validator.validateNot404(json);

        MediaCrushFile toreturn = GSON.fromJson(json, MediaCrushFile.class);

        try {
            setHash(toreturn, hash);
        } catch (NoSuchFieldException e) {
            throw new IOException("Hash could not be set for MediaCrushFile!", e);
        } catch (IllegalAccessException e) {
            throw new IOException("Hash could not be set for MediaCrushFile!", e);
        }

        return toreturn;
    }

    /**
     * Returns an array of {@link MediaCrushFile} containing information about the file whose hash is <b>hash</b>
     * @param hash
     *           <b>NOT NULLABLE</b>
     *           <br></br>
     *           - An array of hash's to lookup
     * @return
     *        An array of files represented as a {@link MediaCrushFile} object
     * @throws IOException
     *                    An {@link IOException} can be thrown for the following reasons: <br></br>
     *                    * There was an error invoking {@link jcrush.io.Requester#connect()} <br></br>
     *                    * The json returned contained a 404 error
     */
    public static MediaCrushFile[] getFiles(String... hash) throws IOException {
        Validator.validateNotNull(hash, "hash");

        String list = "";
        for (int i = 0; i < hash.length; i++) {
            if (i == 0)
                list = hash[i];
            else
                list += "," + hash[i];
        }

        URL uri = new URL(MEDIA_CRUSH_URL + API_DIRECTORY + "info?list=" + list);
        Requester requester = new Requester(ConnectionType.GET, uri);
        requester.setRecieve(true);
        requester.connect();

        String json = requester.getResponse();
        Validator.validateNot404(json);

        Type mapType = new TypeToken<HashMap<String, MediaCrushFile>>(){}.getType();
        HashMap<String, MediaCrushFile> map = GSON.fromJson(json, mapType);

        MediaCrushFile[] array = new MediaCrushFile[map.size()];
        int i = 0;
        for (String key : map.keySet()) {
            try {
                setHash(map.get(key), key);
            } catch (NoSuchFieldException e) {
                throw new IOException("Hash could not be set for MediaCrushFile \"" + key + "\"", e);
            } catch (IllegalAccessException e) {
                throw new IOException("Hash could not be set for MediaCrushFile \"" + key + "\"", e);
            }
            array[i] = map.get(key);
            i++;
        }

        return array;
    }

    public static void setHash(MediaCrushFile file, String hash) throws NoSuchFieldException, IllegalAccessException {
        Field f = file.getClass().getDeclaredField("hash");
        f.setAccessible(true);
        f.set(file, hash);
    }
}
