package gambler.commons.advmap;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang.LocaleUtils;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Simple XML Parser for <tt>XMLMap</tt>
 *
 * @auther Martin
 * @see XMLMap
 */
public class XMLParser extends DefaultHandler {

    private Logger log = Logger.getLogger(XMLParser.class);

    /**
     * It represents tag <properties>.
     */
    private static final String PROPERTIES_TAG = "map";

    /**
     * It represents tag <property>.
     */
    private static final String PROPERTY_TAG = "prop";

    /**
     * It represents attribute <i>namespace</i> of property tag.
     */
    private static final String NAMESPACE_ATTR = "namespace";

    /**
     * It represents key tag <key>
     */
    private static final String KEY_TAG = "key";

    /**
     * It represents attribute <i>name</i> of key tag.
     */
    private static final String NAME_ATTR = "name";

    /**
     * It represents attribute <i>order</i> of key tag
     */
    private static final String ORDER_ATTR = "order";

    /**
     * It represents attribute <i>locale</i> of key tag.
     */
    private static final String LOCALE_ATTR = "locale";

    /**
     * Intial state.
     */
    private static final int IN_NOTHING = 0;

    /**
     * It represents start <properties> element.
     */
    private static final int IN_DOCUMENT = 1;

    /**
     * It represents start <property> element.
     */
    private static final int IN_PROPERTY = 2;

    /**
     * It represents start <key> element.
     */
    private static final int IN_KEY = 3;

    /**
     * Set <code>state</state> to initial state.
     */
    private int state = IN_NOTHING;

    /**
     * It stores the value of attribute name of key tag.
     */
    private String key;

    /**
     * It stores the value of attribute namespace of property tag.
     */
    private String namespace;

    /**
     * It stores the value of attribute order of key tag
     */
    private Integer order;

    /**
     * It store the locale information
     */
    private String locale;

    /**
     * It stores the value between key tag pairs. <br/>
     * ex: &lt;key&gt; a &lt;/key&gt;<br/>
     * <br/>
     * <code>value</code> = a<br/>
     * When characters('\n', ' ', '\t', '\r') appear at the start or end of the
     * string, they will be ignored. <code>value</code> stores character from
     * the first valid one to the last valid one. <br/>
     * The special characters('\n', ' ', '\t', '\r') which appear between the
     * valid ones will be stored too.<br/>
     * While multiline case, it treats every line as a string, <br/>
     * apply the above rule for each and append the result to the
     * <code>value</code><br/>
     */
    private StringBuffer value;

    /**
     * SAX Parser here used.
     */
    private SAXParser parser;

    /**
     * It used to store properties.
     */
    private XMLMap xmlmap;

    /**
     * Initialize the SAX Parser.
     *
     * @return
     */
    private SAXParser getParser() {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp;
        try {
            sp = spf.newSAXParser();
        } catch (Exception e) {
            throw new RuntimeException("Unable to initialize SAXParser");
        }
        return sp;
    }

    /**
     * The constructor initializes the <code>parser</code>, parse the xml file
     * and store the properties to the given <code>xmlmap</code> parameter.
     *
     * @param in - xml file stream which will be parsed.
     * @param xmlmap - used to store properties.
     * @throws SAXException - if parsing xml file failed.
     * @throws IOException - if an IO error occurs.
     */
    public XMLParser(InputStream in, XMLMap xmlprop) throws SAXException,
            IOException {
        this.xmlmap = xmlprop;
        state = IN_NOTHING;
        value = new StringBuffer();
        parser = getParser();
        parser.parse(in, this);
    }

    public void characters(char[] ch, int start, int length)
            throws SAXException {
        super.characters(ch, start, length);
        switch (state) {
            case IN_KEY:
                compute(ch, start, length);
        }
    }

    /**
     * Compute the key's value.
     *
     * @param ch - all characters in xml file
     * @param start - start location
     * @param length - character's length
     * @see #value
     */
    private void compute(char[] ch, int start, int length) {
        int lOffset = 0;
        int rOffset = 0;
        int end = start + length;

        boolean hasValidChar = false;

        for (int i = 0; i < length; i++) {
            if (ch[start + i] != '\n' && ch[start + i] != '\t'
                    && ch[start + i] != ' ' && ch[start + i] != '\r') {
                lOffset = i;
                hasValidChar = true;
                break;
            }
        }

        if (!hasValidChar) {
            return;
        }

        for (int i = length - 1; i > 0; i--) {
            if (ch[start + i] != '\n' && ch[start + i] != '\t'
                    && ch[start + i] != ' ' && ch[start + i] != '\r') {
                rOffset = length - i - 1;
                break;
            }
        }

        for (int i = start + lOffset; i < end - rOffset; i++) {
            value.append(ch[i]);
        }

    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        super.endElement(uri, localName, qName);
        switch (state) {
            case IN_KEY:

                try {
                    AdvancedKey gkey = new AdvancedKey(namespace, key, order,
                            LocaleUtils.toLocale(locale));
                    if (xmlmap.keySet().contains(gkey)) {
                        log.warn("Property[" + gkey + ", " + value.toString()
                                + "] has been updated in xml map");
                    } else {
                        log.info("Property[" + gkey + ", " + value.toString()
                                + "] has been stored in xml map");
                    }
                    xmlmap.put(gkey, value.toString());
                } catch (Exception e) {
                    throw new SAXException("Error when put property " + namespace
                            + AdvancedKey.SEPARATOR + key + AdvancedKey.SEPARATOR
                            + order + AdvancedKey.SEPARATOR + locale, e);
                }
                state = IN_PROPERTY;
                key = null;
                value = new StringBuffer();
                break;
            case IN_PROPERTY:
                namespace = null;
                locale = null;
                state = IN_DOCUMENT;
                break;
            case IN_DOCUMENT:
                state = IN_NOTHING;
                break;
            default:
                throw new SAXException("Invalid element " + qName);
        }
    }

    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        switch (state) {
            case IN_NOTHING:
                if (qName.equals(PROPERTIES_TAG)) {
                    state = IN_DOCUMENT;
                } else {
                    throw new SAXException("Invalid root element, please replace <"
                            + qName + "> with <" + PROPERTIES_TAG + ">");
                }
                break;
            case IN_DOCUMENT:
                if (qName.equals(PROPERTY_TAG)) {
                    state = IN_PROPERTY;
                    namespace = attributes.getValue(NAMESPACE_ATTR);
                    locale = attributes.getValue(LOCALE_ATTR);

				// default namespace/locale will be used
                    // while no namespace/locale assigned
                    if (namespace == null) {
                        namespace = AdvancedKey.DEFAULT_NAMESPACE;
                    }
                    if (locale == null) {
                        locale = AdvancedKey.DEFAULT_LOCALE.toString();
                    }
                } else {
                    throw new SAXException("Invalid element, please replace <"
                            + qName + "> with <" + PROPERTY_TAG + ">");
                }
                break;
            case IN_PROPERTY:
                if (qName.equals(KEY_TAG)) {
                    state = IN_KEY;
                    key = attributes.getValue(NAME_ATTR);
                    if (key == null) {
                        throw new SAXException(
                                "Missing name attribute for element <" + KEY_TAG
                                + ">");
                    }

                    Object orderAttr = attributes.getValue(ORDER_ATTR);
                    if (orderAttr == null) {
                        order = AdvancedKey.DEFAULT_ORDER;
                    } else {
                        order = Integer.parseInt(orderAttr.toString());
                    }
                } else {
                    throw new SAXException("Invalid element, please replace <"
                            + qName + "> with <" + KEY_TAG + ">");
                }
                break;
            default:
                throw new SAXException("Invalid element " + qName);
        }
    }

}
