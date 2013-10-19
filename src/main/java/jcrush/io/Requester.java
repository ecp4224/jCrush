package jcrush.io;

import static jcrush.system.Constants.DEFAULT_USER_AGENT;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Requester {
    private URL url;
    private HashMap<String, String> property = new HashMap<String, String>();
    HttpURLConnection connection;
    private boolean isconnected;
    private boolean output;
    private boolean input;
    private String response;
    private byte[] post;
    private boolean autoredirect;
    private ConnectionType type;

    public Requester(ConnectionType type, URL url) {
        this.setType(type);
        this.setUrl(url);
    }

    public void addHeader(String key, String value) {
        property.put(key, value);
    }

    public void setPostData(String data) {
        data = cleanString(data);
        try {
            this.post = data.getBytes("ASCII");
        }
        catch (UnsupportedEncodingException e) {
            this.post = data.getBytes();
        }
        output = post.length != 0;
    }

    private String cleanString(String s) {
        return s.replace(" ", "+");
    }

    public void setAutoRedirect(boolean value) {
        this.autoredirect = value;
    }

    public boolean isAutoRedirecting() {
        return autoredirect;
    }

    public void setPostData(byte[] data) {
        this.post = data;
        output = post.length != 0;
    }

    public byte[] getPostData() {
        return post;
    }

    public boolean isSendingPost() {
        return output;
    }

    public void setRecieve(boolean input) {
        this.input = input;
    }

    public boolean isRecievingData() {
        return input;
    }

    public String getResponse() {
        if (!isConnected())
            throw new IllegalStateException(
                    "This FlankConnection is not connected!");
        if (!input)
            throw new IllegalStateException(
                    "This FlankConnection is not set to recieve input!");
        return response;
    }

    public ConnectionType getType() {
        return type;
    }

    public void setType(ConnectionType type) {
        this.type = type;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
        if (isConnected()) disconnect();
        connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return connection != null && isconnected;
    }

    public void connect() throws IOException {
        if (connection == null) {
            if (url != null) connection = (HttpURLConnection) url
                    .openConnection();
            else
                throw new IllegalStateException("No URL to connect to!");
        }
        connection.setInstanceFollowRedirects(autoredirect);
        connection.setDoInput(input);
        connection.setDoOutput(output);
        connection.setRequestMethod(type.type);
        connection.setRequestProperty("User-Agent", DEFAULT_USER_AGENT);
        for (String key : property.keySet()) {
            connection.addRequestProperty(key, property.get(key));
        }
        if (output) connection.getOutputStream().write(post);
        if (input) {
            if (connection.getResponseCode() == HttpURLConnection.HTTP_UNAVAILABLE) {
                throw new IOException("The server is unavailable!");
            } else if (connection.getResponseCode() == HttpURLConnection.HTTP_FORBIDDEN) {
                BufferedReader read = new BufferedReader(new InputStreamReader(
                        connection.getErrorStream()));
                StringBuilder builder = new StringBuilder(100);
                String line;
                while ((line = read.readLine()) != null)
                    builder.append(line);
                read.close();
                String reason = builder.toString();
                throw new IOException("ERROR 403: The server responded with \""
                        + reason + "\"");
            }
            BufferedReader read = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            StringBuilder builder = new StringBuilder(100);
            String line;
            while ((line = read.readLine()) != null)
                builder.append(line);
            read.close();
            response = builder.toString();
        }
        isconnected = true;
    }

    public Map<String, List<String>> getHeaderFields() {
        if (!isConnected())
            throw new IllegalStateException(
                    "This FlankConnection is not connected!");
        return connection.getHeaderFields();
    }

    public void disconnect() {
        if (!isConnected()) return;
        connection.disconnect();
        isconnected = false;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        if (isConnected()) disconnect();
    }
}
