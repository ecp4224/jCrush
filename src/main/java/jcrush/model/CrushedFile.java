package jcrush.model;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;
import jcrush.JCrush;
import jcrush.system.Versions;

import java.net.URL;

/**
 * A {@link jcrush.model.CrushedFile} is the result file from a file being crushed by MediaCrush <br></br>
 * This file can by any file type listed in {@link jcrush.model.FileType}. To determine the file type, invoke {@link CrushedFile#getFileType()}
 * @since API v1
 */
public class CrushedFile {
    @Since(Versions.VERSION_1)                         private String file;
    @Since(Versions.VERSION_1)                         private String type;
    @Since(Versions.VERSION_2)                         private String url;
    @Since(Versions.VERSION_2)                         private String path;
    private FileType fileType;

    private CrushedFile() {}

    /**
     * Create a new local {@link jcrush.model.CrushedFile} object.
     * @param file The file path for this {@link jcrush.model.CrushedFile}
     * @param type The file type as a {@link String}
     * @deprecated This method should <b>NOT</b> be used, as a {@link jcrush.model.CrushedFile} object should only be created by
     * {@link jcrush.model.MediaCrushFile} objects.
     * @since API v1
     */
    @Deprecated
    public CrushedFile(String file, String type) {
        this.type = type;
        this.path = file;
        this.file = file;
        this.fileType = FileType.toFileType(type);
    }

    /**
     * Returns the file path of this {@link jcrush.model.CrushedFile}
     * @return The file path
     * @since API v1
     */
    public String getFile() {
        if (JCrush.getAPIVersion() == Versions.VERSION_1) return file;
        return path;
    }

    /**
     * Get the URL this {@link jcrush.model.CrushedFile} resides at.
     * @return Gets the URL of this {@link jcrush.model.CrushedFile} as a {@link String}
     * @since API v2
     */
    public String getURL() {
        return url;
    }

    /**
     * Get the file type this {@link jcrush.model.CrushedFile} is
     * @return The file type as a {@link jcrush.model.FileType} enum
     * @since API v1
     */
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
