package jcrush.model;

public enum FileType {
    UNKNOWN("???"),
    MP4("video/mp4"),
    OGV("video/ogg"),
    GIF("image/gif"),
    PNG("image/png"),
    JPEG("image/jpg"),
    JPG("image/jpg"),
    MP3("audio/mp3"),
    OGG("audio/ogg"),
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
