package gambler.commons.advmap;

import java.util.Iterator;
import java.util.List;

/**
 * merge maps from different types of source<br/>
 *
 * it provides the base maps settings by constructor parameter,<br/>
 * and the value will be overridden by the given map's order<br/>
 *
 * @auther Martin
 */
public final class ListMap extends AdvancedMap {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -7119271392506289248L;

    /**
     * all base maps
     */
    private List<AdvancedMap> baseMaps;

    public ListMap(String name, int refreshIntervalInSeconds) {
    	super(name, refreshIntervalInSeconds);
    }

    public ListMap(String name, int refreshIntervalInSeconds, List<AdvancedMap> maps) {
    	super(name, refreshIntervalInSeconds);
        this.baseMaps = maps;
        this.load();
    }

    /**
     * load all base maps
     */
    @Override
    public void load() {
        Iterator<AdvancedMap> iter = baseMaps.iterator();
        while (iter.hasNext()) {
            AdvancedMap map = iter.next();
            loadMap(map);
        }
    }

    public void loadMap(AdvancedMap map) {
        super.putAll(map);
    }

}
