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

    @Override
    public int hashCode() {
        return hash.hashCode();
    }

    @Override
    public String toString() {
        String files = "";
        for (CrushedFile f : getFiles()) {
            files += "=====" + f.getFile() + "====\n";
            files += f.toString();
            files += "==============================";
        }
        return "Hash: " + hash + "\n" +
                "Compression: " + compression + "\n" +
                "File Status: " + getStatus() + "\n" +
                "== Original File ==\n" +
                getOriginalFile().toString() + "\n" +
                "==============================\n" +
                files;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MediaCrushFile) {
            MediaCrushFile mediaCrushFile = (MediaCrushFile)obj;
            return mediaCrushFile.hash.equals(hash);
        }
        return false;
    }
}
