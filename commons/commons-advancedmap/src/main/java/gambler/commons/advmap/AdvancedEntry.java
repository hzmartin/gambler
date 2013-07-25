package gambler.commons.advmap;

/**
 * table mapped class
 * it stores a lot properties for the application
 * 
 * @auther Martin
 */

public class AdvancedEntry implements java.io.Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -783623282743111304L;

	private String value;
	
	private AdvancedKey id;
	
	public AdvancedEntry(){}
	
	public AdvancedEntry(AdvancedKey id){
		this.id = id;
	}

	public AdvancedEntry(AdvancedKey id, String value){
		this(id);
		this.value = value;
	}
	public AdvancedKey getId() {
		return id;
	}

	public void setId(AdvancedKey id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Property[" + id + ", " + value + "]";
	}
}
