/*
 * @(#) FileWriter.java 2013-11-6
 * 
 * Copyright 2010 NetEase.com, Inc. All rights reserved.
 */
package gambler.commons.util.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

/**
 * Class SimpleFileWriter
 *
 * @author Martin
 * @version 2013-11-6
 */
public class SimpleFileWriter {

    private BufferedWriter writer;

    public SimpleFileWriter(File outFile) throws IOException {
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
