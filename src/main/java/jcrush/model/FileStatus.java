package jcrush.model;

/**
 * The file upload status of a {@link jcrush.model.MediaCrushFile} as an enum
 */
public enum FileStatus {
    /**
     * The file has been processed.
     * @since API v1
     */
    DONE("done"),
    /**
     * The file is being processed or in the processing queue.
     * @since API v1
     */
    PROCESSING("processing"),
    /**
     * The processing step finished early with an abnormal return code.
     * @since API v1
     */
    ERROR("error"),
    /**
     * The file took too long to process.
     * @since API v1
     */
    TIMEOUT("timeout");

    String type;
    FileStatus(String type) { this.type = type; }

    public static FileStatus toFileStatus(String type) {
        for (FileStatus f : values()) {
            if (f.type.equals(type))
                return f;
        }

        return ERROR;
    }

    public String toString() {
        return type;
    }
}
