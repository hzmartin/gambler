package gambler.commons.auth;

import java.io.Serializable;

/**
 * permission definition : (name, content)
 *
 * @auther Martin
 */

public class Permission implements Serializable{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -3005970060595537942L;

	private Long id;
	
	//for example: can_publish, update_news
	private String codename;
	
	//object model which permission related, for example: News
	private String content;
	
	private String description;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodename() {
		return codename;
	}

	public void setCodename(String codename) {
		this.codename = codename;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Permission(" + codename + "): " + content; 
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codename == null) ? 0 : codename.hashCode());
		result = prime * result
				+ ((content == null) ? 0 : content.hashCode());
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
		final Permission other = (Permission) obj;

		if (codename == null) {
			if (other.codename != null)
				return false;
		} else if (!codename.equals(other.codename))
			return false;
		
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		
		return true;
	}

}
