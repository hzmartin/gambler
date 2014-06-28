package gambler.examples.webapp2.domain.auth;

import java.io.Serializable;

/**
 * user role information
 * 
 * @auther Martin
 */

public class Role implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 3411365876968233083L;

	private Long rid;

    private String name;

    private String description;

    public Role() {
    }

    public Role(long rid) {
        this.rid = rid;
    }

    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rid == null) ? 0 : rid.hashCode());
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
		Role other = (Role) obj;
		if (rid == null) {
			if (other.rid != null)
				return false;
		} else if (!rid.equals(other.rid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Role [rid=" + rid + ", name=" + name + "]";
	}

}
