package jcrush.model;

/**
 * {@link jcrush.model.MediaCrushFile} objects represent a file that has been crushed by MediaCrush. The object
 * contains the resulting files, along with flags, metadata, and other info about the file.
 *
 * @since apiv1
 */
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

    /**
     * Returns the compression ratio achieved by hosting the file on MediaCrush.
     * @return the compression ratio
     * @since apiv1
     */
    public double getCompression() {
        return compression;
    }

    /**
     * Returns the original file as a {@link jcrush.model.CrushedFile}
     * @return The orginal file
     * @since apiv1
     */
    public CrushedFile getOriginalFile() {
        if (orginalFile == null) {
            orginalFile = new CrushedFile(original, type);
        }

        return orginalFile;
    }

    /**
     * Returns the resulting compressed files as {@link jcrush.model.CrushedFile} object array
     * @return The resulting compressed files
     * @since apiv1
     */
    public CrushedFile[] getFiles() {
        return files;
    }

    /**
     * Returns the hash for this file.
     * @return The hash
     * @since apiv1
     */
    public String getHash() {
        return hash;
    }

    /**
     * Get the upload status of this {@link jcrush.model.MediaCrushFile} object
     * @return The upload status
     * @since apiv1
     */
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
