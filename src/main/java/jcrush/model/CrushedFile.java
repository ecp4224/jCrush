package jcrush.model;

import java.net.URL;

/**
 * A {@link jcrush.model.CrushedFile} is the result file from a file being crushed by MediaCrush <br></br>
 * This file can by any file type listed in {@link jcrush.model.FileType}. To determine the file type, invoke {@link CrushedFile#getFileType()}
 * @since apiv1
 */
public class CrushedFile {
    private String file;
    private String type;
    private String url;
    private FileType fileType;

    private CrushedFile() {}

    /**
     * Create a new local {@link jcrush.model.CrushedFile} object.
     * @param file The file path for this {@link jcrush.model.CrushedFile}
     * @param type The file type as a {@link String}
     * @deprecated This method should <b>NOT</b> be used, as a {@link jcrush.model.CrushedFile} object should only be created by
     * {@link jcrush.model.MediaCrushFile} objects.
     * @since apiv1
     */
    @Deprecated
    public CrushedFile(String file, String type) {
        this.type = type;
        this.file = file;
        this.fileType = FileType.toFileType(type);
    }

    /**
     * Returns the file path of this {@link jcrush.model.CrushedFile}
     * @return The file path
     * @since apiv1
     */
    public String getFile() {
        return file;
    }

    /**
     * Get the URL this {@link jcrush.model.CrushedFile} resides at.
     * @return Gets the URL of this {@link jcrush.model.CrushedFile} as a {@link String}
     * @since apiv2
     */
    public String getURL() {
        return url;
    }

    /**
     * Get the file type this {@link jcrush.model.CrushedFile} is
     * @return The file type as a {@link jcrush.model.FileType} enum
     * @since apiv1
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
