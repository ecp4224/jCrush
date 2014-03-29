package jcrush.io;

import java.io.File;
import java.io.FileNotFoundException;

public class UploadJob {
    private File file;

    private UploadJob() { }

    public static UploadJob createJob(File file) {
        UploadJob job = new UploadJob();
        job.file = file;

        return job;
    }

    public static UploadJob createJob(String file) throws FileNotFoundException {
        File fileObj = new File(file);
        if (!fileObj.exists()) throw new FileNotFoundException(file);
        return createJob(file);
    }

    public void start() {

    }

}
