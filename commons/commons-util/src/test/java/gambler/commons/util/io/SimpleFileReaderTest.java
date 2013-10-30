package gambler.commons.util.io;

import java.io.File;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SimpleFileReaderTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testProcessLines() throws Exception {
		URL systemResource = ClassLoader.getSystemResource("sample.txt");
		FileProcessor simpleFileReader = new FileProcessor(new File(systemResource.getFile()));
		simpleFileReader.setProcessor(new PrintLineProcessor());
		simpleFileReader.processLines();
	}

}
