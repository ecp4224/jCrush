package jcrush.model;

/**
 * Specifies how this file should be displayed to the user.
 * @since API v2
 */
public enum BlobType {
    /**
     * Represents a video file
     */
    VIDEO("video"),
    /**
     * Represents an image file
     */
    IMAGE("image"),
    /**
     * Represents an audio file
     */
    AUDIO("audio"),
    /**
     * Represents an unknown file
     */
    UNKNOWN("???");

    private String type;
    private BlobType(String type) { this.type = type; }
    public static BlobType fromString(String type) {
        for (BlobType btype : values()) {
            if (btype.type.equals(type)) return btype;
        }
        return UNKNOWN;
    }
    public String getType() { return type; }
    public String toString() { return type; }
}
