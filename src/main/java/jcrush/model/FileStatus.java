package jcrush.model;

public enum FileStatus {
    DONE("done"),
    PROCESSING("processing"),
    ERROR("error"),
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
