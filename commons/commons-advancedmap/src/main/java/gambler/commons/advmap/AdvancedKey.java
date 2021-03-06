package gambler.commons.advmap;

import java.util.Locale;

import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Key object for advanced map.<br/>
 * 
 * It contains:<br/>
 * <ol>
 * <li>namespace</li>
 * <li>key</li>
 * <li>revision</li>
 * <li>locale</li>
 * </ol>
 * 
 * the string key has five styles<br/>
 * <ol>
 * <li>key</li>
 * <li>namespace.key</li>
 * <li>namespace.key.revision</li>
 * <li>namespace.key.locale</li>
 * <li>namespace.key.revision.locale</li>
 * </ol>
 * 
 * @auther Martin
 */

public class AdvancedKey implements java.io.Serializable,
		Comparable<AdvancedKey> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -783623282743111304L;

	/**
	 * Separator between namespace, key name and locale. It's value is "."
	 */
	public static final String SEPARATOR = ".";

	/**
	 * Regular expression for the key separator
	 */
	public static final String SEPARATOR_RE = "\\.";

	/**
	 * default locale
	 */
	public static final Locale DEFAULT_LOCALE = Locale.getDefault();

	/**
	 * default revision number: 1
	 */
	public static final Integer DEFAULT_REVISION = 1;

	/**
	 * default namespace: gambler
	 */
	public static final String DEFAULT_NAMESPACE = "gambler";

	private String namespace;

	private String key;

	private Integer revision;

	private Locale locale;

	public AdvancedKey() {
		this.namespace = DEFAULT_NAMESPACE;
		this.revision = DEFAULT_REVISION;
		this.locale = DEFAULT_LOCALE;
	}

	public AdvancedKey(String key) {
		this(DEFAULT_NAMESPACE, key, DEFAULT_REVISION, DEFAULT_LOCALE);
	}

	public AdvancedKey(String namespace, String key) {
		this(namespace, key, DEFAULT_REVISION, DEFAULT_LOCALE);
	}

	public AdvancedKey(String key, Locale locale) {
		this(DEFAULT_NAMESPACE, key, DEFAULT_REVISION, locale);
	}

	public AdvancedKey(String key, Integer revision) {
		this(DEFAULT_NAMESPACE, key, revision, DEFAULT_LOCALE);
	}

	public AdvancedKey(String namespace, String key, Integer revision) {
		this(namespace, key, revision, DEFAULT_LOCALE);
	}

	public AdvancedKey(String namespace, String key, Locale locale) {
		this(namespace, key, DEFAULT_REVISION, locale);
	}

	public AdvancedKey(String key, Integer revision, Locale locale) {
		this(DEFAULT_NAMESPACE, key, revision, locale);
	}

	public AdvancedKey(String namespace, String key, Integer revision,
			Locale locale) {
		if (namespace.contains(SEPARATOR)) {
			throw new IllegalArgumentException(
					"period sign is not allowed in namespace " + namespace);
		}
		if (key.contains(SEPARATOR)) {
			throw new IllegalArgumentException(
					"period sign is not allowed in key " + key);
		}
		this.namespace = namespace;
		this.key = key;
		this.revision = revision;
		this.locale = locale;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Integer getRevision() {
		return revision;
	}

	public void setRevision(Integer revision) {
		this.revision = revision;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * the output is also a full path key for this advanced key.
	 * <code>namespace + SEPARATOR + key + SEPARATOR + revision + SEPARATOR + locale</code>
	 */
	@Override
	public String toString() {
		return buildFullPathKey(namespace, key, revision, locale);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((locale == null) ? 0 : locale.hashCode());
		result = prime * result
				+ ((namespace == null) ? 0 : namespace.hashCode());
		result = prime * result + ((revision == null) ? 0 : revision.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final AdvancedKey other = (AdvancedKey) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (locale == null) {
			if (other.locale != null)
				return false;
		} else if (!locale.equals(other.locale))
			return false;
		if (namespace == null) {
			if (other.namespace != null)
				return false;
		} else if (!namespace.equals(other.namespace))
			return false;
		if (revision == null) {
			if (other.revision != null)
				return false;
		} else if (!revision.equals(other.revision))
			return false;
		return true;
	}

	/**
	 * useful virtual nsKey property for <code>AdvancedKey</code>
	 * 
	 * @return
	 */
	public String getNsKey() {
		return namespace + SEPARATOR + key;
	}

	/**
	 * @param nsKey
	 *            - valid format [ns.key]
	 */
	public void setNsKey(String nsKey) {
		String[] part = nsKey.split(SEPARATOR_RE);
		if (part.length != 2) {
			throw new IllegalArgumentException("Invalid key definition "
					+ nsKey);
		}
		namespace = part[0];
		key = part[1];
	}

	@Override
	public int compareTo(AdvancedKey o) {
		return this.toString().compareTo(o.toString());
	}

	/**
	 * build a full path key whoes style as the string output of
	 * <code>AdvancedKey</code>
	 * 
	 * @param namespace
	 * @param key
	 * @param locale
	 * @param revision
	 * @return full path key string
	 */
	public static final String buildFullPathKey(String namespace, String key,
			Integer revision, Locale locale) {

		if (StringUtils.isBlank(key)) {
			throw new IllegalArgumentException(
					"key required in advanced key definition!");
		}

		if (StringUtils.isBlank(namespace)) {
			namespace = AdvancedKey.DEFAULT_NAMESPACE;
		}

		if (revision == null) {
			revision = AdvancedKey.DEFAULT_REVISION;
		}

		if (locale == null) {
			locale = AdvancedKey.DEFAULT_LOCALE;
		}

		return namespace + AdvancedKey.SEPARATOR + key + AdvancedKey.SEPARATOR
				+ revision + AdvancedKey.SEPARATOR + locale;
	}

	/**
	 * build AdvancedKey instance by the given full path key<br/>
	 * the fullPathKey has five styles<br/>
	 * <ol>
	 * <li>key</li>
	 * <li>namespace.key</li>
	 * <li>namespace.key.revision</li>
	 * <li>namespace.key.locale</li>
	 * <li>namespace.key.revision.locale</li>
	 * </ol>
	 * all omitted paramters will refer to default settings<br/>
	 * 
	 * @param fullPathKey
	 *            - default style as output of
	 *            <code>AdvancedKey{@link #toString()}</code>
	 * @return AdvancedKey instance
	 */
	public static final AdvancedKey buildAdvancedKey(String fullPathKey) {
		try {
			String[] part = fullPathKey.split(AdvancedKey.SEPARATOR_RE);
			AdvancedKey resultKey = null;
			switch (part.length) {
			case 1:
				resultKey = new AdvancedKey(fullPathKey);
				break;
			case 2:
				resultKey = new AdvancedKey(part[0], part[1]);
				break;
			case 3:
				resultKey = new AdvancedKey(part[0], part[1]);
				try {
					resultKey.setRevision(Integer.parseInt(part[2]));
				} catch (Exception e) {
					resultKey.setLocale(LocaleUtils.toLocale(part[2]));
				}
				break;
			default:
				resultKey = new AdvancedKey(part[0], part[1],
						Integer.parseInt(part[2]),
						LocaleUtils.toLocale(part[3]));
				break;
			}
			return resultKey;
		} catch (Exception e) {
			throw new IllegalArgumentException("invalid key definition "
					+ fullPathKey, e);
		}
	}
}
