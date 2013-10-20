package jcrush.model;

public class MediaCrushFile {
    private String hash;
    private float compression;
    private String original;
    private String type;
    private FileType fileType;
    private CrushedFile[] files;
    private CrushedFile orginalFile;

    private MediaCrushFile() { }

    public float getCompression() {
        return compression;
    }

    public CrushedFile getOriginalFile() {
        if (orginalFile == null) {
            orginalFile = new CrushedFile(original, type);
        }

        return orginalFile;
    }

    public CrushedFile[] getFiles() {
        return files;
    }

    public String getHash() {
        return hash;
    }
}
