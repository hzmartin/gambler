package gambler.commons.advmap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * Advanced map extends <code>HashMap</code><br/>
 * 
 * It provides the variable replacement feature<br/>
 * 
 * More description:<br/>
 * <p>
 * If one value in the map is "dkkj#{ns1.key1.en_US}df", and it will still
 * search the value whose full path key is "ns1.key1.en_US" and replace it. the
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
 * 	</code> value1, value2 and value3 all have the same value.
 * </p>
 * <p>
 * 2. put key/value pairs <br/>
 * <code>
 * 	advmap.put(new AdvancedKey("ns1","key1",Locale.US),"value");
 * 	advmap.setProperty("ns1.key1.en_US","value");
 * 	</code>
 * </p>
 * 
 * @auther Martin
 */

public abstract class AbstractAdvancedMap extends HashMap<AdvancedKey, String> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5248960418733175757L;

	private static final Logger log = Logger
			.getLogger(AbstractAdvancedMap.class);

	/**
	 * Start symbol of variables in property's value
	 */
	private static String VARIABLE_START_SYMBOL = "#{";

	/**
	 * Regular expression for start symbol of variables
	 */
	private static String VARIABLE_START_SYMBOL_RE = "#\\{";

	/**
	 * End symbol of variables in property's value
	 */
	private static String VARIABLE_END_SYMBOL = "}";

	/**
	 * Regular expression for end symbol of variables
	 */
	private static String VARIABLE_END_SYMBOL_RE = "\\}";

	public abstract void load();

	private boolean varReplaceEnable = true;

	private Set<String> recursiveKeys = new HashSet<String>();

	public boolean isVarReplaceEnable() {
		return varReplaceEnable;
	}

	public void setVarReplaceEnable(boolean varReplaceEnable) {
		this.varReplaceEnable = varReplaceEnable;
	}

	/**
	 * get variables in the given string. <br/>
	 * for example:<br/>
	 * <code>String str = helodl${kdlw.dkf}jekf${keolf.ekfl} </code><br/>
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
			int endSymbol = str.indexOf(VARIABLE_END_SYMBOL, startSymbol + 2);
			if (startSymbol + 2 < endSymbol) {
				variables.add(str.substring(startSymbol + 2, endSymbol).trim());
			}
			startSymbol = str.indexOf(VARIABLE_START_SYMBOL, endSymbol);
		}
		return variables;
	}

	/**
	 * <p>
	 * find the property by the given key if the target value contains
	 * #{xxx.xxx}, this method will replace it by the existing value in the
	 * properties once <code>varReplaceEnable</code> is true.
	 * </p>
	 * <p>
	 * if recursive reference found, it will throw an <code>IllegalStateException</code>,
	 * but if there're more recursive references, the map maybe will not follow
	 * this rule, please check your references carefully.
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
				recursiveKeys.add(advkey.toString());
				for (Iterator<String> iter = varnames.iterator(); iter
						.hasNext();) {
					String fullPathKey = iter.next();
					if (recursiveKeys.contains(fullPathKey)) {
						throw new IllegalStateException(
								"recursive key "
										+ fullPathKey
										+ " found while variable repalcement, please resolve it first!");
					}
					// recursive call
					String varvalue = get(fullPathKey);
					value = value.replaceAll(VARIABLE_START_SYMBOL_RE
							+ fullPathKey + VARIABLE_END_SYMBOL_RE,
							varvalue == null ? "" : varvalue);

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
	public String getProperty(String fullPathKey) {
		return getProperty(fullPathKey, null);
	}

	public String getProperty(String fullPathKey, String defaultValue) {
		String value = get(fullPathKey);
		if (value == null || value.trim().equals("")) {
			return defaultValue;
		} else {
			return value;
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
	public String setProperty(String fullPathKey, String value) {
		log.info("Invoking setProperty(" + fullPathKey + ", " + value + ")");
		AdvancedKey key = AdvancedKey.buildAdvancedKey(fullPathKey);
		return put(key, value);
	}

}
