package gambler.commons.advmap;

import java.util.Iterator;
import java.util.List;

/**
 * merge maps from different types of source, like xml, database etc.,<br/>
 * 
 * it provides the base maps settings by constructor parameter,<br/>
 * and the value will be overridden by the given map's order<br/>
 * 
 * @auther Martin
 */

public final class SimpleAdvancedMap extends AbstractAdvancedMap {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7119271392506289248L;
	
	/**
	 * all base maps
	 */
	private List<AbstractAdvancedMap> baseMaps;
	
	public SimpleAdvancedMap(List<AbstractAdvancedMap> maps){
		this.baseMaps = maps;
		this.load();
	}
	
	/**
	 * load all base maps
	 */
	@Override
	public void load() {
		Iterator<AbstractAdvancedMap> iter = baseMaps.iterator();
		while(iter.hasNext()){
			AbstractAdvancedMap map = iter.next();
			Iterator<AdvancedKey> keys = map.keySet().iterator();
			while(keys.hasNext()){
				AdvancedKey key = keys.next();
				String value = map.get(key);
				super.put(key, value);
			}
		}
	}
	
}
