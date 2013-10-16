package gambler.commons.util.io;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

/**
 * 
 * simple file processor
 * 
 * @author Martin
 * 
 */
public class FileProcessor {

	private File file;

	private ILineProcessor processor;

	public FileProcessor(File file) {
		super();
		this.file = file;
	}

	public ILineProcessor getProcessor() {
		return processor;
	}

	public void setProcessor(ILineProcessor processor) {
		this.processor = processor;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public void processLines() throws IOException {
		LineNumberReader lineNumberReader = new LineNumberReader(
				new FileReader(file));
		String line = null;
		while ((line = lineNumberReader.readLine()) != null) {
			processor.process(lineNumberReader.getLineNumber(), line);
		}
	}

}
