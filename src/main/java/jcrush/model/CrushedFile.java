package jcrush.model;

import java.net.MalformedURLException;
import java.net.URL;

public class CrushedFile {
    private String file;
    private String type;
    private FileType fileType;
    private String url;

    private CrushedFile() {}

    public CrushedFile(String file, String type) {
        this.type = type;
        this.file = file;
        this.fileType = FileType.toFileType(type);
    }

    public String getFile() {
        return file;
    }

    public URL getURL() {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getURLAsString() {
        return url;
    }

    public FileType getFileType() {
        if (fileType == null)
            fileType = FileType.toFileType(type);
        return fileType;
    }

    @Override
    public String toString() {
        return "File: " + file + "\n" +
                "Type: " + type + "\n";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CrushedFile) {
            CrushedFile file = (CrushedFile)obj;
            return file.file.equals(this.file) && file.fileType.equals(fileType);
        }
        return false;
    }
}
