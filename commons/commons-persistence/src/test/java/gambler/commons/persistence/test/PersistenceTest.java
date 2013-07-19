package gambler.commons.persistence.test;

import gambler.commons.persistence.PersistenceException;
import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PersistenceTest extends TestCase {

	Logger logger = Logger.getLogger(PersistenceTest.class);

	private static ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
			"test.xml");

	AppLevelService levelService = null;

	protected void setUp() throws Exception {
		levelService = (AppLevelService) ctx.getBean("testAppLevelService");
	}

	protected void tearDown() throws Exception {
	}

	public void testSave() throws Exception {
		logger.info("start................");
		long id = 2;
		SysAppLevel level = new SysAppLevel();
		level.setId(id);
		level.setDisplayName("Hello World");
		level.setLevel("hello");
		level.setProductKey("mmmmm");
		level.setPlatform("ios");
		level.setParent(10);
		level.setEnable(1);
		levelService.save(level);
		level = levelService.findById(id);
		assertEquals("hello", level.getLevel());
		assertEquals("mmmmm", level.getProductKey());
		level.setLevel("world");
		levelService.saveOrUpdate(level);
		level = levelService.findById(id);
		assertEquals("world", level.getLevel());
		levelService.delete(level);
		level = levelService.findById(id);
		assertNull(level);
	}

	public void testTransaction() throws PersistenceException {
		SysAppLevel level = new SysAppLevel();
		long id = 2;
		level.setId(id);
		level.setDisplayName("Hello World");
		level.setLevel("hello");
		level.setProductKey("mmmmm");
		level.setPlatform("ios");
		level.setParent(10);
		level.setEnable(1);
		levelService.save(level);
		level = levelService.findById(id);
		assertEquals("hello", level.getLevel());
		assertEquals("mmmmm", level.getProductKey());
		try {
			levelService.moreTasks(level);
		} catch (Exception e) {
			logger.warn(e);
			assertTrue(true);
		}
		level = levelService.findById(id);
		assertEquals(id, level.getId());
		assertEquals(10, level.getParent());
		levelService.delete(level);
		level = levelService.findById(id);
		assertNull(level);
	}
}
