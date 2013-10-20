package jcrush.model;

public enum FileType {
    UNKNOWN("???"),
    MP4("video/mp4"),
    OGV("video/ogg"),
    GIF("image/gif"),
    WEBM("video/webm");

    String type;
    FileType(String type) { this.type = type; }

    public static FileType toFileType(String type) {
        for (FileType t : values()) {
            if (t.type.equals(type))
                return t;
        }
        return UNKNOWN;
    }

    public String toString() {
        return name() + " (" + type + ")";
    }
}
