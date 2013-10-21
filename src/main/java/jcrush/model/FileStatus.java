package jcrush.model;

public enum FileStatus {
    /**
     * The file has been processed.
     */
    DONE("done"),
    /**
     * The file is being processed or in the processing queue.
     */
    PROCESSING("processing"),
    /**
     * The processing step finished early with an abnormal return code.
     */
    ERROR("error"),
    /**
     * The file took too long to process.
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
}
