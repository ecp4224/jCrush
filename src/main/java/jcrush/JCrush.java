package jcrush;

import static jcrush.system.Constants.*;
import static jcrush.system.Utils.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.xml.internal.ws.util.ByteArrayBuffer;
import jcrush.io.ConnectionType;
import jcrush.io.Requester;
import jcrush.model.CrushedFile;
import jcrush.model.FileStatus;
import jcrush.model.MediaCrushFile;
import jcrush.system.Validator;

import javax.imageio.ImageIO;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Static methods that expose the MediaCrush API
 */
public class JCrush {
    private static final Gson GSON;

    static {
        GSON = new Gson();
    }

    public static void setSystemProperties() {
        System.setProperty("http.agent", DEFAULT_USER_AGENT);
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
        requester.disconnect();
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
        requester.disconnect();
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

    public static boolean doesExists(String hash) throws IOException {
        Validator.validateNotNull(hash, "hash");

        URL uri = new URL(MEDIA_CRUSH_URL + API_DIRECTORY + hash + "/exists");
        Requester requester = new Requester(ConnectionType.HEAD, uri);
        requester.setRecieve(true);
        try {
            requester.connect();
        } catch (FileNotFoundException ignored) {
            requester.disconnect();
            return false;
        }
        requester.disconnect();
        return true;
    }

    public static void uploadFile(String filePath) throws IOException {
        Validator.validateNotNull(filePath, "filePath");
        File file = new File(filePath);
        if (!file.exists())
            throw new FileNotFoundException("The file could not be found!");
        else if (file.isDirectory())
            throw new IOException("The filePath specified is a directory!");
        else
            uploadFile(file);
    }

    public static String uploadFile(File file) throws IOException {
        Validator.validateNotNull(file, "file");

        if (!file.exists())
            throw new FileNotFoundException();
        if (file.isDirectory())
            throw new IOException("This file is a directory!");

        //Get content type of file
        String contentType = toContentType(file);
        if (contentType == null)
            throw new IOException("Unknown file type!");

        //Read file into byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        copy(new FileInputStream(file), baos, 1024);
        byte[] bytes = baos.toByteArray();

        //Prepare form data to send
        String header = "\r\n--" + CONTENT_DIVIDER + "\r\n" +
                "Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"" + "\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Content-Transfer-Encoding: binary\r\n" +
                "\r\n";
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.write(header.getBytes("ASCII"));
        bos.write(bytes);
        String footer = "\r\n--" + CONTENT_DIVIDER + "--";
        bos.write(footer.getBytes("ASCII"));
        byte[] tosend = bos.toByteArray();

        //Prepare the requester with form data
        URL uri = new URL(MEDIA_CRUSH_URL + API_DIRECTORY + "upload/file");
        Requester requester = new Requester(ConnectionType.POST, uri);
        requester.setPostData(tosend);
        requester.addHeader("Content-Length", "" + tosend.length);
        requester.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        requester.addHeader("Accept-Encoding", "gzip, deflate");
        requester.addHeader("X-Requested-With", "XMLHttpRequest");
        requester.addHeader("Content-Type", "multipart/form-data; boundary=" + CONTENT_DIVIDER);
        requester.setRecieve(true);
        try {
            requester.connect(); //Connect
        } catch (IOException e) {
            BufferedReader read = new BufferedReader(new InputStreamReader(
                    requester.getErrorStream()));
            StringBuilder builder = new StringBuilder(100);
            String line;
            while ((line = read.readLine()) != null)
                builder.append(line);
            read.close();
            String reason = builder.toString();

            int code = requester.getResponseCode();
            switch (code) {
                case 409:
                    throw new IOException("This file was already uploaded!");
                case 420:
                    throw new IOException("The rate limit was exceeded. Enhance your calm.");
                case 415:
                    throw new IOException("The file extension is not acceptable.");
                default:
                    throw new IOException("The server responded with an unknown error code! (" + code + ")");
            }
        }

        //Parse results
        int code = requester.getResponseCode();
        String json = requester.getResponse();
        requester.disconnect(); //Disconnect
        Map map = GSON.fromJson(json, Map.class);
        if (code == 200 && !map.containsKey("error")) {
            return (String)map.get("hash");
        } else {
            if (code == 200) {
                try {
                    code = Integer.parseInt((String)map.get("error"));
                } catch (Throwable t) {
                    throw new IOException("The server responded with an unknown error (" + (map.get("error") == null ? "null" : map.get("error")) + ")", t);
                }
            }
            switch (code) {
                case 409:
                    throw new IOException("This file was already uploaded!");
                case 420:
                    throw new IOException("The rate limit was exceeded. Enhance your calm.");
                case 415:
                    throw new IOException("The file extension is not acceptable.");
                default:
                    throw new IOException("The server responded with an unknown error code! (" + code + ")");
            }
        }
    }

    public static void delete(String hash) throws IOException {
        Validator.validateNotNull(hash, "hash");

        URL uri = new URL(MEDIA_CRUSH_URL + API_DIRECTORY + hash + "/delete");
        Requester requester = new Requester(ConnectionType.GET, uri);
        requester.setRecieve(true);
        try {
            requester.connect();
        } catch (FileNotFoundException e) {
            requester.disconnect();
            throw new IOException("There is no file with that hash!");
        }

        int code = requester.getResponseCode();
        requester.disconnect();

        if (code != 200) {
            if (code == 404)
                throw new IOException("There is no file with that hash!");
            else if (code == 401)
                throw new IOException("The IP does not match the stored hash!");
            else
                throw new IOException("The server responded with an unknown code! (" + code + ")");
        }
    }

    public static void delete(MediaCrushFile file) throws IOException {
        Validator.validateNotNull(file, "file");
        delete(file.getHash());
    }

    public static MediaCrushFile getFileStatus(String hash) throws IOException {
        Validator.validateNotNull(hash, "hash");

        URL uri = new URL(MEDIA_CRUSH_URL + API_DIRECTORY + hash + "/status");
        Requester requester = new Requester(ConnectionType.GET, uri);
        requester.setRecieve(true);
        requester.connect();

        String json = requester.getResponse();
        requester.disconnect();
        Validator.validateNot404(json);

        Map map = GSON.fromJson(json, Map.class);

        String statusString = (String) map.get("status");

        try {
            MediaCrushFile file = convertMapToFile((Map) map.get(hash));
            setHash(file, hash);
            setStatus(file, FileStatus.toFileStatus(statusString));

            return file;
        } catch (NoSuchFieldException e) {
            throw new IOException("Error creating MediaCrushFile", e);
        } catch (IllegalAccessException e) {
            throw new IOException("Error creating MediaCrushFile", e);
        } catch (InstantiationException e) {
            throw new IOException("Error creating MediaCrushFile", e);
        } catch (NoSuchMethodException e) {
            throw new IOException("Error creating MediaCrushFile", e);
        } catch (InvocationTargetException e) {
            throw new IOException("Error creating MediaCrushFile", e);
        }
    }
}
