package gambler.commons.advmap;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import org.apache.log4j.Logger;

/**
 * Advanced map extends <code>TreeMap</code><br/>
 * 
 * It provides the variable replacement feature<br/>
 * 
 * More description:<br/>
 * <p>
 * If one value in the map is "dkkj#ns1.key1.en_US#df", and it will still search
 * the value whose full path key is "ns1.key1.en_US" and replace it. the
 * variable replacement feature can be turned off by switch field
 * <code>varReplaceEnable</code>(default: true)
 * </p>
 * <p>
 * Full path key is the string whose style likes the return value of
 * <code>AdvancedKey.toString()</code> eg: "ns1.key1.en_US", here '.' is the
 * sperator for each part
 * </p>
 * 
 * Usage:<br/>
 * <p>
 * 1. find the value of "ns1.key1.en_US"<br/>
 * <code>
 * 	String value1 = advmap.getProperty("ns1.key1.en_US");
 * 	String value2 = advmap.get("ns1.key1.en_US");
 * 	String value3 = advmap.get(new AdvancedKey("ns1","key1",Locale.US));
 * </code> <br/>
 * value1, value2 and value3 all have the same value.
 * </p>
 * <p>
 * 2. put key/value pairs <br/>
 * <code>
 * 	advmap.put(new AdvancedKey("ns1","key1",Locale.US),"value");
 * 	advmap.setProperty("ns1.key1.en_US","value");
 * </code>
 * </p>
 * <p>
 * 3. refresh map enable if interval greater than 0<br/>
 * </p>
 * 
 * @auther Martin
 */
public abstract class AdvancedMap extends TreeMap<AdvancedKey, String> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5248960418733175757L;

	private static final Logger log = Logger.getLogger(AdvancedMap.class);

	/**
	 * Start symbol of variables in property's value
	 */
	private static String VARIABLE_START_SYMBOL = "#{";

	/**
	 * End symbol of variables in property's value
	 */
	private static String VARIABLE_END_SYMBOL = "}";

	public abstract void load();

	private boolean varReplaceEnable = true;

	private Set<AdvancedKey> recursiveKeys = new HashSet<AdvancedKey>();

	private String name;

	/**
	 * @param name
	 *            - map name
	 * @param refreshIntervalInSeconds
	 *            - enable if greater than 0
	 */
	public AdvancedMap(String name, int refreshIntervalInSeconds) {
		super();
		this.name = name;
		if (refreshIntervalInSeconds > 0) {
			enableMapRefreshAtIntervals(refreshIntervalInSeconds);
		}
	}

	/**
	 * refresh map at interval
	 */
	private void enableMapRefreshAtIntervals(int interval) {
		try {
			Timer timer = new Timer("Map(" + getName() + ") Refresh", true);
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					AdvancedMap.this.load();
					log.info("map(" + AdvancedMap.this.getName()
							+ ") refreshed!");
				}
			}, interval * 1000L, interval * 1000L);
		} catch (Exception e) {
			log.error("scheduled map(" + AdvancedMap.this.getName()
					+ ") refresh failed!", e);
		}

	}

	public boolean isVarReplaceEnable() {
		return varReplaceEnable;
	}

	public void setVarReplaceEnable(boolean varReplaceEnable) {
		this.varReplaceEnable = varReplaceEnable;
	}

	/**
	 * get variables in the given string. <br/>
	 * for example:<br/>
	 * <code>String str = helodl#kdlw.dkf#jekf#keolf.ekfl# </code><br/>
	 * this method will extract two variable names, <i>kdlw.dkf</i> and
	 * <i>keolf.ekfl</i>
	 * 
	 * @param str
	 * @return a list of extracted variables
	 */
	private Set<String> getVariableNames(String str) {
		Set<String> variables = new HashSet<String>();
		int startSymbol = str.indexOf(VARIABLE_START_SYMBOL);
		while (startSymbol != -1) {
			int endSymbol = str.indexOf(VARIABLE_END_SYMBOL, startSymbol
					+ VARIABLE_START_SYMBOL.length());
			if (startSymbol + 1 < endSymbol) {
				variables.add(str
						.substring(
								startSymbol + VARIABLE_START_SYMBOL.length(),
								endSymbol).trim());
			}
			startSymbol = str.indexOf(VARIABLE_START_SYMBOL, endSymbol
					+ VARIABLE_END_SYMBOL.length());
		}
		return variables;
	}

	/**
	 * <p>
	 * find the property by the given key if the target value contains
	 * #xxx.xxx#, this method will replace it by the existing value in the
	 * properties once <code>varReplaceEnable</code> is true.
	 * </p>
	 * <p>
	 * if recursive reference found, it will throw an
	 * <code>IllegalStateException</code>, but if there're more recursive
	 * references, the map maybe will not follow this rule, please check your
	 * references carefully.
	 * </p>
	 * 
	 * @param akey
	 *            - AdvancedKey instance or full path key(exactly the style of
	 *            <code>AdvancedKey{@link #toString()}</code>)
	 * @return property's value
	 */
	@Override
	public String get(Object akey) {
		AdvancedKey advkey = null;
		if (akey instanceof String) {
			// support full path key access
			advkey = AdvancedKey.buildAdvancedKey(akey.toString());
		} else if (akey instanceof AdvancedKey) {
			advkey = (AdvancedKey) akey;
		}

		String value = super.get(advkey);

		if (varReplaceEnable && value != null) {
			Set<String> varnames = getVariableNames(value);
			if (varnames.size() > 0) {
				recursiveKeys.add(advkey);
				for (Iterator<String> iter = varnames.iterator(); iter
						.hasNext();) {
					String fullPathKey = iter.next();
					AdvancedKey inclusiveKey = AdvancedKey
							.buildAdvancedKey(fullPathKey);
					if (recursiveKeys.contains(inclusiveKey)) {
						throw new IllegalStateException(
								"recursive key "
										+ fullPathKey
										+ " found while variable repalcement, please resolve it first!");
					}
					// recursive call
					String varvalue = get(fullPathKey);
					String replacement = VARIABLE_START_SYMBOL + fullPathKey
							+ VARIABLE_END_SYMBOL;
					int startSymbolIndex = value.indexOf(replacement);
					if (startSymbolIndex != -1) {
						value = value.substring(0, startSymbolIndex)
								+ (varvalue == null ? "" : varvalue)
								+ value.substring(startSymbolIndex
										+ replacement.length());
					}
					recursiveKeys.clear();
				}
			} else {
				// return value directly as value doesn't contain any variable
				return value;
			}

		}

		return value;
	}

	/**
	 * get the property by the given full path key
	 * 
	 * @param fullPathKey
	 *            - five styles, reference
	 *            <code>AdvancedKey#buildAdvancedKey</code>
	 * @return property value
	 * @see AdvancedKey#buildAdvancedKey(String)
	 */
	public String getString(String fullPathKey) {
		return getString(fullPathKey, null);
	}

	public String getString(String fullPathKey, String defaultValue) {
		String value = get(fullPathKey);
		if (value == null) {
			return defaultValue;
		} else {
			return value;
		}
	}

	public Integer getInteger(String fullPathKey) {
		return getInteger(fullPathKey, null);
	}

	public Integer getInteger(String fullPathKey, Integer defaultValue) {
		String value = getString(fullPathKey);
		if (value == null) {
			return defaultValue;
		}
		try {
			return Integer.valueOf(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public Boolean getBoolean(String fullPathKey) {
		return getBoolean(fullPathKey, null);
	}

	public Boolean getBoolean(String fullPathKey, Boolean defaultValue) {
		String value = getString(fullPathKey);
		if (value == null) {
			return defaultValue;
		}
		try {
			return Boolean.valueOf(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public Long getLong(String fullPathKey) {
		return getLong(fullPathKey, null);
	}

	public Long getLong(String fullPathKey, Long defaultValue) {
		String value = getString(fullPathKey);
		if (value == null) {
			return defaultValue;
		}
		try {
			return Long.valueOf(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public Double getDouble(String fullPathKey) {
		return getDouble(fullPathKey, null);
	}

	public Double getDouble(String fullPathKey, Double defaultValue) {
		String value = getString(fullPathKey);
		if (value == null) {
			return defaultValue;
		}
		try {
			return Double.valueOf(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public Float getFloat(String fullPathKey) {
		return getFloat(fullPathKey, null);
	}

	public Float getFloat(String fullPathKey, Float defaultValue) {
		String value = getString(fullPathKey);
		if (value == null) {
			return defaultValue;
		}
		try {
			return Float.valueOf(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * set property by the given key and value
	 * 
	 * @param fullPathKey
	 *            - four styles, reference
	 *            <code>AdvancedKey#buildAdvancedKey</code>
	 * @param value
	 * @return previous value the key associated or null if there's no mapping
	 *         for the key
	 * @see AdvancedKey#buildAdvancedKey(String)
	 */
	public void setString(String fullPathKey, String value) {
		log.debug("set property: key=" + fullPathKey + ", value=" + value);
		AdvancedKey key = AdvancedKey.buildAdvancedKey(fullPathKey);
		put(key, value);
	}

	public void setInteger(String fullPathKey, Integer value) {
		AdvancedKey key = AdvancedKey.buildAdvancedKey(fullPathKey);
		put(key, value + "");
	}

	public void setBoolean(String fullPathKey, Boolean value) {
		AdvancedKey key = AdvancedKey.buildAdvancedKey(fullPathKey);
		put(key, value + "");
	}

	public void setLong(String fullPathKey, Long value) {
		AdvancedKey key = AdvancedKey.buildAdvancedKey(fullPathKey);
		put(key, value + "");
	}

	public void setDouble(String fullPathKey, Double value) {
		AdvancedKey key = AdvancedKey.buildAdvancedKey(fullPathKey);
		put(key, value + "");
	}

	public void setFloat(String fullPathKey, Float value) {
		AdvancedKey key = AdvancedKey.buildAdvancedKey(fullPathKey);
		put(key, value + "");
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
