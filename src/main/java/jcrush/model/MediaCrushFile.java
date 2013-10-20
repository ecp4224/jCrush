package jcrush.model;

public class MediaCrushFile {
    private String hash;
    private FileStatus status;
    private double compression;
    private String original;
    private String type;
    private FileType fileType;
    private CrushedFile[] files;
    private CrushedFile orginalFile;

    private MediaCrushFile() { }

    public double getCompression() {
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

    public FileStatus getStatus() {
        return status == null ? FileStatus.DONE : status;
    }
}
