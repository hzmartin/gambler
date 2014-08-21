package jobs.utils.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

/**
 * Class FileWriter
 *
 * @author Administrator
 * @version 2013-11-6
 */
public class FileWriter {

    private BufferedWriter writer;

    public FileWriter(File outFile) throws IOException {
        writer = new BufferedWriter(new java.io.FileWriter(outFile));
    }

    public void write(String str) throws IOException {
        writer.write(str);
    }

    public void flushAndClose() throws IOException {
        writer.flush();
        writer.close();
    }

}
