package jcrush.model;

/**
 * All possible file types MediaCrush supports as an enum
 */
public enum FileType {
    UNKNOWN("???", ".dat"),
    MP4("video/mp4", "mp4"),
    OGV("video/ogg", "ogv"),
    GIF("image/gif", "gif"),
    PNG("image/png", "png"),
    JPEG("image/jpg", "jpeg"),
    JPG("image/jpg", "jpg"),
    MP3("audio/mp3", "mp3"),
    OGG("audio/ogg", "ogg"),
    WEBM("video/webm", "");

    String type;
    String ext;
    FileType(String type, String ext) { this.type = type; this.ext = ext; }

    public static FileType toFileType(String type) {
        for (FileType t : values()) {
            if (t.type.equals(type))
                return t;
        }
        return UNKNOWN;
    }

    public String getFileExtension() {
        return ext;
    }

    public String toString() {
        return type;
    }
}
