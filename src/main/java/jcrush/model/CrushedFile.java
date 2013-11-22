package jcrush.model;

public class CrushedFile {
    private String file;
    private String type;
    private FileType fileType;

    private CrushedFile() {}

    public CrushedFile(String file, String type) {
        this.type = type;
        this.file = file;
        this.fileType = FileType.toFileType(type);
    }

    public String getFile() {
        return file;
    }

    public FileType getFileType() {
        if (fileType == null)
            fileType = FileType.toFileType(type);
        return fileType;
    }

    @Override
    public String toString() {
        return "File: " + file + "\n" +
                "Type: " + type + "\n";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CrushedFile) {
            CrushedFile file = (CrushedFile)obj;
            return file.file.equals(this.file) && file.fileType.equals(fileType);
        }
        return false;
    }
}
