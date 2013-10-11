package gambler.commons.advmap;

import gambler.commons.persistence.IPersistence;
import gambler.commons.persistence.PersistenceException;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * properties load from database
 * 
 * @auther Martin
 */

public final class DBMap extends AbstractAdvancedMap {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -3515739618355690028L;

	private static final Logger log = Logger.getLogger(DBMap.class);

	public DBMap(IPersistence persistence) {
		this.persistence = persistence;
		this.load();
	}

	/**
	 * database operator
	 */
	private IPersistence persistence;

	public IPersistence getPersistence() {
		return persistence;
	}

	public void setPersistence(IPersistence persistence) {
		this.persistence = persistence;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void load() {
		List<AdvancedEntry> properties = null;

		try {
			properties = persistence.loadAll(AdvancedEntry.class);
		} catch (PersistenceException e1) {
			log.warn("Load db properties failed", e1);
			return;
		}

		Iterator<AdvancedEntry> iter = properties.iterator();
		while (iter.hasNext()) {
			AdvancedEntry prop = iter.next();
			try {
				put(prop.getId(), prop.getValue());
			} catch (Exception e) {
				log.warn("Ignore " + prop, e);
			}
		}

	}

	/**
	 * it will update the map and store the property to the database
	 * 
	 * @throws PersistenceException
	 */
	public AdvancedEntry save(AdvancedKey key, String value)
			throws PersistenceException {
		put(key, value);
		AdvancedEntry prop = new AdvancedEntry(key, value);
		persistence.saveOrUpdate(prop);
		return prop;
	}

}
