package gambler.commons.advmap;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import junit.framework.TestCase;

/**
 * unit test for advanced properties
 * 
 * @auther Jack
 */
public class AdvancedMapTest extends TestCase {

	private AdvancedMap global = null;

	private XMLMap xml1 = null;

	private XMLMap xml2 = null;

	protected void setUp() throws Exception {
		xml1 = new XMLMap("xmlmap1", 1, "xmlmap1.xml");
		xml2 = new XMLMap("xmlmap2", 0, "xmlmap2.xml");
		List<AdvancedMap> maps = new ArrayList<AdvancedMap>();
		maps.add(xml1);
		maps.add(xml2);
		global = new ListMap("listmap", 0, maps);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		global = null;
	}

	public void testRecur() {
		try {
			XMLMap xmlMap = new XMLMap("xmlmap3", 1, "xmlmap3.xml");
			xmlMap.get("recur");
			fail("recur exception expected");
		} catch (IllegalStateException e) {
			assertTrue(true);
		}
	}

	public void testIntegerArray() {
		Integer[] integerArray = global.getIntegerArray("testIntArray");
		assertTrue(1 == integerArray[0]);
		assertTrue(2 == integerArray[1]);
	}

	public void testGetProperty() {
		assertEquals("Welcome to Beijing",
				xml1.getString("base.jenny." + Locale.getDefault()));
		assertEquals("北京欢迎您", xml1.getString("base.jack.zh_CN"));
		assertEquals("Welcome to Beijing 北京欢迎您",
				xml1.getString("base.third.zh_CN"));
	}

	public void testSetProperty() {
		global.setString("base.welcome.zh_TW", "test info");
		assertEquals("test info", global.getString("base.welcome.zh_TW"));
	}

	public void testMapRefresh() throws InterruptedException {
		assertEquals("Welcome to Beijing",
				xml1.getString("base.jenny." + Locale.getDefault()));
		xml1.setString("base.jenny." + Locale.getDefault(), "updated");
		assertEquals("updated",
				xml1.getString("base.jenny." + Locale.getDefault()));
		Thread.sleep(1000 * 5L);
		assertEquals("Welcome to Beijing",
				xml1.getString("base.jenny." + Locale.getDefault()));

	}

	public void testOverrideProperty() {
		assertEquals("Welcome to Beijing",
				global.getString("base.jenny." + Locale.getDefault()));
		assertEquals("Nice Work", global.getString("base.jack.zh_CN"));
		assertEquals("Welcome to Beijing Nice Work",
				global.getString("base.third.zh_CN"));
	}

	public void testRevision() {

		assertEquals("Nice",
				global.getString(AdvancedKey.DEFAULT_NAMESPACE + ".hello.1"));
		assertEquals("Nice2", global.getString(AdvancedKey.DEFAULT_NAMESPACE + ".hello.2"));
	}
}
