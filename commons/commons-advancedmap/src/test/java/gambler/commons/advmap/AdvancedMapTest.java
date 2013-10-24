package gambler.commons.advmap;

import gambler.commons.persistence.PersistenceException;

import java.util.Locale;

import junit.framework.TestCase;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * unit test for advanced properties
 *
 * @auther Jack
 */
public class AdvancedMapTest extends TestCase {

    private static ClassPathXmlApplicationContext ctx = null;

    static {
        ctx = new ClassPathXmlApplicationContext("util-test.xml");
    }

    private AdvancedMap global = null;

    private DBMap db = null;

    private XMLMap xml = null;

    protected void setUp() throws Exception {
        xml = (XMLMap) ctx.getBean("xml-properties");
        db = (DBMap) ctx.getBean("db-properties");
        global = (AdvancedMap) ctx.getBean("global-properties");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        global = null;
    }

    public void testGetProperty() {
        assertEquals("Welcome to Beijing",
                xml.getProperty("base.jenny." + Locale.getDefault()));
        assertEquals("北京欢迎您", xml.getProperty("base.jack.zh_CN"));
        assertEquals("Welcome to Beijing 北京欢迎您",
                xml.getProperty("base.third.zh_CN"));
    }

    public void testSetProperty() {
        global.setProperty("base.welcome.zh_TW", "test info");
        assertEquals("test info", global.getProperty("base.welcome.zh_TW"));
    }

    public void testOverrideProperty() throws PersistenceException {
        assertEquals("Welcome to Beijing",
                global.getProperty("base.jenny." + Locale.getDefault()));
        assertEquals("北京欢迎您", global.getProperty("base.jack.zh_CN"));
        assertEquals("Welcome to Beijing 北京欢迎您",
                global.getProperty("base.third.zh_CN"));
        AdvancedKey key = new AdvancedKey("base", "jack", Locale.CHINA);
        AdvancedEntry prop = db.save(key, "Nice Work");
        global.clear();
        global.load();
        assertEquals("Nice Work", global.getProperty("base.jack.zh_CN"));
        db.getPersistence().delete(prop);
    }

    public void testMapRefresh() throws InterruptedException {
        assertEquals("Welcome to Beijing",
                xml.getProperty("base.jenny." + Locale.getDefault()));
        xml.setProperty("base.jenny." + Locale.getDefault(), "updated");
        assertEquals("updated",
                xml.getProperty("base.jenny." + Locale.getDefault()));
        xml.enableMapRefreshAtIntervals();
        Thread.sleep(1000 * 5L);
        assertEquals("Welcome to Beijing",
                xml.getProperty("base.jenny." + Locale.getDefault()));

    }

}
