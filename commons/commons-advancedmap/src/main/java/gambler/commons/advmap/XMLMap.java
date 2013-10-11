package gambler.commons.advmap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

/**
 * <p>
 * The class represents a transformer between <tt>Properties</tt> and XML data.<br/>
 * ex:<br/>
 * Sample xml file:<br/>
 * <properties><br/>
 * <property namespace="namespace1" locale="en_US"><br/>
 * <key name="keyname1"><br/>
 * value1<br/>
 * </key><br/>
 * <key name="keyname2">value2</key><br/>
 * </property><br/>
 * <property namespace="namespace2"><br/>
 * <key name="keyname1"> value1 </key><br/>
 * <key name="keyname2"><br/>
 * value2</key><br/>
 * </property><br/>
 * <property><br/>
 * <key name="keyname1"><br/>
 * ${namespace2.keyname1}<br/>
 * </key><br/>
 * <key name="keyname2">value2</key><br/>
 * </property><br/>
 * </properties><br/>
 * <br/>
 * Result Properties:<br/>
 * (namespace1.keyname1.en_US=value1)<br/>
 * (namespace1.keyname2.en_US=value2)<br/>
 * (namespace2.keyname1.zh_CN=value1)<br/>
 * (namespace2.keyname2.zh_CN=value2)<br/>
 * ([XMLMap.DEFAULT_NAMESPACE].keyname1.zh_CN=value1)<br/>
 * ([XMLMap.DEFAULT_NAMESPACE].keyname2.zh_CN=value2)<br/>
 * </p>
 * <p>
 * <b>Key value store rule:</b><br/>
 * When characters('\n', ' ', '\t', '\r') appear at the start or end of the
 * string,<br/>
 * they will be ignored.<br/>
 * value contains character from the first valid one to the last valid one.<br/>
 * The special characters('\n', ' ', '\t', '\r') which appear between the valid
 * ones will be stored too.<br/>
 * While multiline case, it treats every line as a string, apply the rule for
 * each and append the result to the value.<br/>
 * </p>
 * <p>
 * Usage:<br/>
 * XMLMap xmlprop=...<br/>
 * //get keyname1's value in default namespace<br/>
 * xmlprop.get("keyname1")<br/>
 * //get keyname1's value in namespace2<br/>
 * xmlprop.get("namespace2","keyname1")<br/>
 * 
 * </p>
 * 
 * @auther Martin
 */

public final class XMLMap extends AbstractAdvancedMap {

	private static final Logger log = Logger.getLogger(XMLMap.class);

	/**
	 * serial version uid
	 */
	private static final long serialVersionUID = 7486290035316643518L;

	public XMLMap(String xmlFilePath) {
		this(new String[] { xmlFilePath });
	}

	public XMLMap(String[] xmlFilePaths) {
		this.xmlFilePaths = xmlFilePaths;
		this.load();
	}

	private String[] xmlFilePaths;

	/**
	 * Load XML file, parse it and store the properties
	 * 
	 * @throws SAXException
	 *             - parse error
	 * @throws IOException
	 *             - if IO error occurs.
	 */
	public synchronized void load(InputStream in) throws SAXException,
			IOException {
		new XMLParser(in, this);
	}

	/**
	 * Load XML file, parse it and store the properties
	 * 
	 * @param file
	 *            - xml file
	 * @throws IOException
	 * @throws SAXException
	 * @see #load(InputStream)
	 */
	public void load(File file) throws SAXException, IOException {
		if (file.exists()) {
			load(new FileInputStream(file));
		} else {
			log.info("File " + file.getAbsolutePath()
					+ " doesn't exist, try to find " + file.getName()
					+ " in the classpath.");
			load(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream(file.getName()));
		}

	}

	/**
	 * Load XML file, parse it and store the properties
	 * 
	 * @param path
	 *            - xml file path
	 * @throws IOException
	 * @throws SAXException
	 * @see #load(InputStream)
	 */
	public void load(String path) throws SAXException, IOException {
		load(new File(path));
	}

	@Override
	public void load() {
		try {
			for (int i = 0; i < xmlFilePaths.length; i++) {
				load(xmlFilePaths[i]);
			}
		} catch (Exception e) {
			log.warn("Load xml properties failed", e);
		}

	}
}
