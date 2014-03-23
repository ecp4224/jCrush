package jcrush.model;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;
import jcrush.JCrush;
import jcrush.system.Versions;

/**
 * {@link jcrush.model.MediaCrushFile} objects represent a file that has been crushed by MediaCrush. The object
 * contains the resulting files, along with flags, metadata, and other info about the file.
 *
 * @since API v1
 */
public class MediaCrushFile {
    @Since(Versions.VERSION_1)     private String hash;
    @Since(Versions.VERSION_1)     private FileStatus status;
    @Since(Versions.VERSION_1)     private double compression;
    @Since(Versions.VERSION_1)     private String original;
    @Since(Versions.VERSION_1)     private String type;
    @Since(Versions.VERSION_1)     private CrushedFile[] files;

    @Since(Versions.VERSION_2)     private String blob_type;
    @Since(Versions.VERSION_2)     private CrushedFile[] extras;

    private BlobType blobType;
    private CrushedFile orginalFile;

    private MediaCrushFile() { }

    /**
     * Returns the compression ratio achieved by hosting the file on MediaCrush.
     * @return the compression ratio
     * @since API v1
     */
    public double getCompression() {
        return compression;
    }

    /**
     * Returns the original file as a {@link jcrush.model.CrushedFile}
     * @return The orginal file
     * @since API v1
     * @deprecated This method no longer works, as MediaCrush does not provide the type of the original file.
     */
    @Deprecated
    public CrushedFile getOriginalFile() {
        if (orginalFile == null) {
            orginalFile = new CrushedFile(original, JCrush.getAPIVersion() == Versions.VERSION_1 ? type : blob_type);
        }

        return orginalFile;
    }

    /**
     * Returns the resulting compressed files as {@link jcrush.model.CrushedFile} object array
     * @return The resulting compressed files
     * @since API v1
     */
    public CrushedFile[] getFiles() {
        return files;
    }

    /**
     * Returns the hash for this file.
     * @return The hash
     * @since API v1
     */
    public String getHash() {
        return hash;
    }

    /**
     * Get the upload status of this {@link jcrush.model.MediaCrushFile} object
     * @return The upload status
     * @since API v1
     */
    public FileStatus getStatus() {
        return status == null ? FileStatus.DONE : status;
    }

    /**
     * Get the {@link jcrush.model.BlobType} of this file.
     * @return The {@link jcrush.model.BlobType} of this file.
     * @since API v2
     */
    public BlobType getBlobType() {
        if (blobType == null)
            blobType = BlobType.fromString(blob_type);
        return blobType;
    }

    /**
     * Return any auxiliary files, such as a thumbnail or subtitles.
     * @return Any auxiliary files, such as a thumbnail or subtitles.
     * @since API v2
     */
    public CrushedFile[] getExtraFiles() {
        return extras;
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
